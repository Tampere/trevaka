// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.invoice.service

import fi.espoo.evaka.invoicing.domain.InvoiceDetailed
import fi.espoo.evaka.invoicing.domain.InvoiceRowDetailed
import fi.espoo.evaka.invoicing.domain.InvoiceStatus
import fi.espoo.evaka.invoicing.domain.PersonData
import fi.espoo.evaka.invoicing.domain.Product
import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient
import fi.espoo.evaka.shared.DatabaseTable
import fi.espoo.evaka.shared.Id
import fi.espoo.evaka.shared.InvoiceId
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
    fun sendBatchWithValidData() {
        server.expect(connectionTo("http://localhost:8080/salesOrder"))
            .andExpect(payload(ClassPathResource("invoice-client/sales-order-request-1.xml")))
            .andRespond(withPayload(ClassPathResource("invoice-client/sales-order-response-ok.xml")))

        assertThat(client.sendBatch(listOf(validInvoice(1)), 1)).isTrue()

        server.verify()
    }

    @Test
    fun sendBatchWithClientFault() {
        server.expect(connectionTo("http://localhost:8080/salesOrder"))
            .andRespond(withClientOrSenderFault("test", Locale.ENGLISH))

        assertThat(client.sendBatch(listOf(), 1)).isFalse()

        server.verify()
    }

    @Test
    fun sendBatchWithServerFault() {
        server.expect(connectionTo("http://localhost:8080/salesOrder"))
            .andRespond(withServerOrReceiverFault("test", Locale.ENGLISH))

        assertThat(client.sendBatch(listOf(), 1)).isFalse()

        server.verify()
    }

    private fun validInvoice(agreementType: Int): InvoiceDetailed {
        val headOfFamily = PersonData.Detailed(
            UUID.randomUUID(), LocalDate.of(1982, 3, 31), null,
            "Maija", "Meikäläinen",
            "310382-956D", "Meikäläisenkuja 6 B 7", "33730", "TAMPERE",
            "", null, "", null, restrictedDetailsEnabled = false
        )
        val invoiceRow1 = InvoiceRowDetailed(
            UUID.randomUUID(), PersonData.Detailed(
                UUID.randomUUID(), LocalDate.of(2018, 1, 1), null,
                "Matti", "Meikäläinen",
                null, "", "", "",
                "", null, "", null, restrictedDetailsEnabled = false
            ), 1, 24300,
            LocalDate.of(2021, 1, 1),
            LocalDate.of(2021, 1, 31),
            Product.DAYCARE, "131885", null, "kuvaus1"
        )
        val invoiceRow2 = InvoiceRowDetailed(
            UUID.randomUUID(), PersonData.Detailed(
                UUID.randomUUID(), LocalDate.of(2015, 11, 26), null,
                "Maiju", "Meikäläinen",
                null, "", "", "",
                "", null, "", null, restrictedDetailsEnabled = false
            ), 1, 48200,
            LocalDate.of(2021, 1, 1),
            LocalDate.of(2021, 1, 31),
            Product.PRESCHOOL_WITH_DAYCARE, "284823", null, "kuvaus2"
        )
        return InvoiceDetailed(
            (InvoiceId(UUID.randomUUID())),
            InvoiceStatus.WAITING_FOR_SENDING,
            LocalDate.now(),
            LocalDate.now(),
            LocalDate.of(2021, 3, 6),
            LocalDate.of(2021, 2, 4),
            agreementType,
            headOfFamily,
            null,
            listOf(invoiceRow1, invoiceRow2),
            null,
            null,
            null
        )
    }

}
