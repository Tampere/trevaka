// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.frends

import com.github.tomakehurst.wiremock.client.WireMock.*
import fi.espoo.evaka.dvv.DvvModificationsServiceClient
import fi.espoo.evaka.identity.ExternalIdentifier
import fi.espoo.evaka.invoicing.domain.PaymentIntegrationClient
import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient
import fi.espoo.evaka.shared.auth.AuthenticatedUser
import fi.espoo.evaka.vtjclient.service.persondetails.IPersonDetailsService
import fi.tampere.trevaka.AbstractTampereIntegrationTest
import fi.tampere.trevaka.invoice.service.validInvoice
import fi.tampere.trevaka.payment.testPayment
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.TestPropertySource
import java.time.LocalDate

@TestPropertySource(
    properties = [
        "trevaka.enabled-features.frends-vtj-kysely=true",
        "trevaka.enabled-features.frends-vtj-mutpa=true",
        "tampere.enabled-features.frends-invoice=true",
        "tampere.enabled-features.frends-payment=true",
    ],
)
class FrendsTest : AbstractTampereIntegrationTest() {

    @Autowired
    lateinit var personDetailsService: IPersonDetailsService

    @Autowired
    lateinit var dvvModificationsServiceClient: DvvModificationsServiceClient

    @Autowired
    private lateinit var invoiceClient: InvoiceIntegrationClient

    @Autowired
    private lateinit var paymentClient: PaymentIntegrationClient

    @Test
    fun `IPersonDetailsService#getBasicDetailsFor`() {
        stubFor(
            post(urlEqualTo("/mock/vtj")).willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/xml")
                    .withBodyFile("person-client/henkilotunnuskysely-response-ok.xml")
                    .withTransformers("response-template")
                    .withTransformerParameter("ssn", "070644-937X"),
            ),
        )

        val person = personDetailsService.getBasicDetailsFor(
            IPersonDetailsService.DetailsQuery(
                AuthenticatedUser.SystemInternalUser.evakaUserId,
                ExternalIdentifier.SSN.getInstance("070644-937X"),
            ),
        )

        assertThat(person).returns("070644-937X", { it.socialSecurityNumber })
        verify(
            postRequestedFor(urlEqualTo("/mock/vtj"))
                .withoutHeader("Authorization")
                .withHeader("X-API-KEY", equalTo("frends-api-key-123"))
                .withHeader("Content-Type", equalTo("text/xml; charset=UTF-8"))
                .withHeader("SOAPAction", equalTo("\"\"")),
        )
    }

    @Test
    fun `DvvModificationsServiceClient#getFirstModificationToken`() {
        stubFor(
            get(urlEqualTo("/mock/modifications/kirjausavain/2021-04-01")).willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBodyFile("modifications-client/first-modification-token-response-ok.json"),
            ),
        )

        val response = dvvModificationsServiceClient.getFirstModificationToken(LocalDate.of(2021, 4, 1))
        assertThat(response).returns(5446623423) { it?.latestModificationToken }

        verify(
            getRequestedFor(urlEqualTo("/mock/modifications/kirjausavain/2021-04-01"))
                .withoutHeader("Authorization")
                .withHeader("X-API-KEY", equalTo("frends-api-key-123")),
        )
    }

    @Test
    fun `DvvModificationsServiceClient#getModifications`() {
        stubFor(
            post(urlEqualTo("/mock/modifications/muutokset")).willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBodyFile("modifications-client/modifications-response-ok.json"),
            ),
        )

        val response = dvvModificationsServiceClient.getModifications("5446623423", listOf())
        assertThat(response).returns("3494393") { it.viimeisinKirjausavain }

        verify(
            postRequestedFor(urlEqualTo("/mock/modifications/muutokset"))
                .withoutHeader("Authorization")
                .withHeader("X-API-KEY", equalTo("frends-api-key-123")),
        )
    }

    @Test
    fun `InvoiceIntegrationClient#send`() {
        val invoice1 = validInvoice()
        stubFor(
            post(urlEqualTo("/mock/ipaas/salesOrder")).willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/soap+xml")
                    .withBodyFile("invoice-client/sales-order-response-ok.xml"),
            ),
        )

        assertThat(invoiceClient.send(listOf(invoice1)))
            .returns(listOf(invoice1)) { it.succeeded }
            .returns(listOf()) { it.failed }

        verify(
            postRequestedFor(urlEqualTo("/mock/ipaas/salesOrder"))
                .withoutHeader("Authorization")
                .withHeader("X-API-KEY", equalTo("frends-api-key-123"))
                .withHeader(
                    "Content-Type",
                    equalTo("application/soap+xml; charset=utf-8; action=\"http://www.tampere.fi/services/sapsd/salesorder/v1.0/SendSalesOrder\""),
                )
                .withoutHeader("SOAPAction"),
        )
    }

    @Test
    fun `PaymentIntegrationClient#send`() {
        val payment1 = testPayment
        stubFor(
            post(urlEqualTo("/mock/ipaas/payableAccounting")).willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/soap+xml")
                    .withBodyFile("payment-client/payable-accounting-response-ok.xml"),
            ),
        )

        assertThat(db.read { tx -> paymentClient.send(listOf(payment1), tx) })
            .returns(listOf(payment1)) { it.succeeded }
            .returns(listOf()) { it.failed }

        verify(
            postRequestedFor(urlEqualTo("/mock/ipaas/payableAccounting"))
                .withoutHeader("Authorization")
                .withHeader("X-API-KEY", equalTo("frends-api-key-123"))
                .withHeader(
                    "Content-Type",
                    equalTo("application/soap+xml; charset=utf-8; action=\"http://www.tampere.fi/services/sapfico/payableaccounting/v1.0/SendPayableAccounting\""),
                )
                .withoutHeader("SOAPAction"),
        )
    }
}
