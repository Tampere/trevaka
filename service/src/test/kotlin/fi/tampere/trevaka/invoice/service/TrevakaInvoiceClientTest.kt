// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.invoice.service

import fi.espoo.evaka.invoicing.domain.InvoiceDetailed
import fi.espoo.evaka.invoicing.domain.InvoiceRowDetailed
import fi.espoo.evaka.invoicing.domain.InvoiceStatus
import fi.espoo.evaka.invoicing.domain.PersonDetailed
import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient
import fi.espoo.evaka.invoicing.service.ProductKey
import fi.espoo.evaka.shared.AreaId
import fi.espoo.evaka.shared.DaycareId
import fi.espoo.evaka.shared.InvoiceId
import fi.espoo.evaka.shared.InvoiceRowId
import fi.espoo.evaka.shared.PersonId
import fi.tampere.trevaka.InvoiceProperties
import fi.tampere.trevaka.IpaasProperties
import fi.tampere.trevaka.TrevakaProperties
import fi.tampere.trevaka.invoice.config.InvoiceConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.custommonkey.xmlunit.XMLUnit
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import org.springframework.ws.test.client.MockWebServiceServer
import org.springframework.ws.test.client.RequestMatchers.connectionTo
import org.springframework.ws.test.client.RequestMatchers.payload
import org.springframework.ws.test.client.ResponseCreators.*
import java.time.LocalDate
import java.util.Locale
import java.util.UUID

internal class TrevakaInvoiceClientTest {

    private lateinit var client: InvoiceIntegrationClient
    private lateinit var server: MockWebServiceServer

    companion object {
        private val xmlUnitIgnoreWhitespace = XMLUnit.getIgnoreWhitespace()

        @BeforeAll
        @JvmStatic
        fun setupXmlUnit() {
            XMLUnit.setIgnoreWhitespace(true)
        }

        @AfterAll
        @JvmStatic
        fun cleanupXmlUnit() {
            XMLUnit.setIgnoreWhitespace(xmlUnitIgnoreWhitespace)
        }
    }

    @BeforeEach
    fun setup() {
        val properties = TrevakaProperties(
            IpaasProperties("user", "pass"),
            InvoiceProperties("http://localhost:8080/salesOrder")
        )
        val configuration = InvoiceConfiguration()
        val webServiceTemplate = configuration.webServiceTemplate(configuration.httpClient(properties), properties)
        client = configuration.invoiceIntegrationClient(webServiceTemplate, properties)
        server = MockWebServiceServer.createServer(webServiceTemplate)
    }

    @Test
    fun sendWithValidData() {
        val invoice1 = validInvoice()
        server.expect(connectionTo("http://localhost:8080/salesOrder"))
            .andExpect(payload(ClassPathResource("invoice-client/sales-order-request-1.xml")))
            .andRespond(withPayload(ClassPathResource("invoice-client/sales-order-response-ok.xml")))

        assertThat(client.send(listOf(invoice1)))
            .returns(listOf(invoice1)) { it.succeeded }
            .returns(listOf()) { it.failed }

        server.verify()
    }

    @Test
    fun sendWithRestrictedDetails() {
        val invoice1 = validInvoice().copy(headOfFamily = validPerson().copy(restrictedDetailsEnabled = true))
        server.expect(connectionTo("http://localhost:8080/salesOrder"))
            .andExpect(payload(ClassPathResource("invoice-client/sales-order-request-restricted-details.xml")))
            .andRespond(withPayload(ClassPathResource("invoice-client/sales-order-response-ok.xml")))

        assertThat(client.send(listOf(invoice1)))
            .returns(listOf(invoice1)) { it.succeeded }
            .returns(listOf()) { it.failed }

        server.verify()
    }

    @Test
    fun sendWithCodebtor() {
        val invoice1 = validInvoice().copy(codebtor = validPerson().copy(firstName = "Mikko"))
        server.expect(connectionTo("http://localhost:8080/salesOrder"))
            .andExpect(payload(ClassPathResource("invoice-client/sales-order-request-codebtor.xml")))
            .andRespond(withPayload(ClassPathResource("invoice-client/sales-order-response-ok.xml")))

        assertThat(client.send(listOf(invoice1)))
            .returns(listOf(invoice1)) { it.succeeded }
            .returns(listOf()) { it.failed }

        server.verify()
    }

    @Test
    fun sendWithClientFault() {
        val invoice1 = validInvoice()
        server.expect(connectionTo("http://localhost:8080/salesOrder"))
            .andRespond(withClientOrSenderFault("test", Locale.ENGLISH))

        assertThat(client.send(listOf(invoice1)))
            .returns(listOf()) { it.succeeded }
            .returns(listOf(invoice1)) { it.failed }

        server.verify()
    }

    @Test
    fun sendWithServerFault() {
        val invoice1 = validInvoice()
        server.expect(connectionTo("http://localhost:8080/salesOrder"))
            .andRespond(withServerOrReceiverFault("test", Locale.ENGLISH))

        assertThat(client.send(listOf(invoice1)))
            .returns(listOf()) { it.succeeded }
            .returns(listOf(invoice1)) { it.failed }

        server.verify()
    }

}

fun validInvoice(): InvoiceDetailed {
    val headOfFamily = validPerson()
    val invoiceRow1 = InvoiceRowDetailed(
        InvoiceRowId(UUID.randomUUID()), PersonDetailed(
            PersonId(UUID.randomUUID()), LocalDate.of(2018, 1, 1), null,
            "Matti", "Meikäläinen",
            null, "", "", "",
            "", null, "", null, restrictedDetailsEnabled = false
        ), 1, 24300,
        LocalDate.of(2021, 1, 1),
        LocalDate.of(2021, 1, 31),
        ProductKey("DAYCARE"), DaycareId(UUID.randomUUID()), "131885", null, null, "kuvaus1"
    )
    val invoiceRow2 = InvoiceRowDetailed(
        InvoiceRowId(UUID.randomUUID()), PersonDetailed(
            PersonId(UUID.randomUUID()), LocalDate.of(2015, 11, 26), null,
            "Maiju", "Meikäläinen",
            null, "", "", "",
            "", null, "", null, restrictedDetailsEnabled = false
        ), 1, 48200,
        LocalDate.of(2021, 1, 1),
        LocalDate.of(2021, 1, 31),
        ProductKey("PRESCHOOL_WITH_DAYCARE"), DaycareId(UUID.randomUUID()), "284823", null, null, "kuvaus2"
    )
    return InvoiceDetailed(
        (InvoiceId(UUID.randomUUID())),
        InvoiceStatus.WAITING_FOR_SENDING,
        LocalDate.now(),
        LocalDate.now(),
        LocalDate.of(2021, 3, 6),
        LocalDate.of(2021, 2, 4),
        null,
        AreaId(UUID.randomUUID()),
        headOfFamily,
        null,
        listOf(invoiceRow1, invoiceRow2),
        null,
        null,
        null
    )
}

fun validPerson() = PersonDetailed(
    PersonId(UUID.randomUUID()), LocalDate.of(1982, 3, 31), null,
    "Maija", "Meikäläinen",
    "310382-956D", "Meikäläisenkuja 6 B 7", "33730", "TAMPERE",
    "", null, "", null, restrictedDetailsEnabled = false
)
