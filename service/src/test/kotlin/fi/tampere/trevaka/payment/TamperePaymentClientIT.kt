// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.payment

import com.github.tomakehurst.wiremock.client.BasicCredentials
import com.github.tomakehurst.wiremock.client.WireMock.*
import fi.espoo.evaka.invoicing.domain.PaymentIntegrationClient
import fi.tampere.trevaka.AbstractTampereIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.ws.soap.client.SoapFaultClientException

class TamperePaymentClientIT : AbstractTampereIntegrationTest() {

    @Autowired
    private lateinit var client: PaymentIntegrationClient

    @Test
    fun send() {
        val payment1 = testPayment
        stubFor(
            post(urlEqualTo("/mock/frends/payableAccounting")).willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/soap+xml")
                    .withBodyFile("payment-client/payable-accounting-response-ok.xml"),
            ),
        )

        assertThat(db.read { tx -> client.send(listOf(payment1), tx) })
            .returns(listOf(payment1)) { it.succeeded }
            .returns(listOf()) { it.failed }

        verify(
            postRequestedFor(urlEqualTo("/mock/frends/payableAccounting"))
                .withoutHeader("Authorization")
                .withHeader("X-API-KEY", equalTo("finance-api-key-123"))
                .withHeader(
                    "Content-Type",
                    equalTo("application/soap+xml; charset=utf-8; action=\"http://www.tampere.fi/services/sapfico/payableaccounting/v1.0/SendPayableAccounting\""),
                )
                .withoutHeader("SOAPAction")
                .withRequestBody(equalToXml(ClassPathResource("__files/payment-client/payable-accounting-request.xml").getContentAsString(Charsets.UTF_8))),
        )
    }

    @Test
    fun sendWithApplicationFaultResponse() {
        val payment1 = testPayment
        stubFor(
            post(urlEqualTo("/mock/frends/payableAccounting")).willReturn(
                aResponse()
                    .withStatus(400)
                    .withHeader("Content-Type", "application/soap+xml")
                    .withBodyFile("payment-client/payable-accounting-response-application-fault.xml"),
            ),
        )

        val thrown = catchThrowable { db.read { tx -> client.send(listOf(payment1), tx) } }

        assertThat(thrown).isInstanceOf(SoapFaultClientException::class.java)
        verify(
            postRequestedFor(urlEqualTo("/mock/frends/payableAccounting"))
                .withoutHeader("Authorization")
                .withHeader("X-API-KEY", equalTo("finance-api-key-123"))
                .withHeader(
                    "Content-Type",
                    equalTo("application/soap+xml; charset=utf-8; action=\"http://www.tampere.fi/services/sapfico/payableaccounting/v1.0/SendPayableAccounting\""),
                )
                .withoutHeader("SOAPAction"),
        )
    }

    @Test
    fun sendWithSystemFaultResponse() {
        val payment1 = testPayment
        stubFor(
            post(urlEqualTo("/mock/frends/payableAccounting")).willReturn(
                aResponse()
                    .withStatus(500)
                    .withHeader("Content-Type", "application/soap+xml")
                    .withBodyFile("payment-client/payable-accounting-response-system-fault.xml"),
            ),
        )

        val thrown = catchThrowable { db.read { tx -> client.send(listOf(payment1), tx) } }

        assertThat(thrown).isInstanceOf(SoapFaultClientException::class.java)
        verify(
            postRequestedFor(urlEqualTo("/mock/frends/payableAccounting"))
                .withoutHeader("Authorization")
                .withHeader("X-API-KEY", equalTo("finance-api-key-123"))
                .withHeader(
                    "Content-Type",
                    equalTo("application/soap+xml; charset=utf-8; action=\"http://www.tampere.fi/services/sapfico/payableaccounting/v1.0/SendPayableAccounting\""),
                )
                .withoutHeader("SOAPAction"),
        )
    }
}
