// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.invoice.service

import fi.espoo.evaka.daycare.CareType
import fi.espoo.evaka.daycare.domain.ProviderType
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
import fi.tampere.trevaka.BiExportProperties
import fi.tampere.trevaka.BucketProperties
import fi.tampere.trevaka.InvoiceProperties
import fi.tampere.trevaka.PaymentProperties
import fi.tampere.trevaka.SummertimeAbsenceProperties
import fi.tampere.trevaka.TampereProperties
import fi.tampere.trevaka.invoice.config.InvoiceConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import org.springframework.ws.soap.client.SoapFaultClientException
import org.springframework.ws.test.client.MockWebServiceServer
import org.springframework.ws.test.client.RequestMatchers.connectionTo
import org.springframework.ws.test.client.RequestMatchers.payload
import org.springframework.ws.test.client.ResponseCreators.*
import trevaka.addClientInterceptors
import trevaka.ipaas.IpaasProperties
import trevaka.newPayloadValidatingInterceptor
import java.time.LocalDate
import java.util.Locale
import java.util.UUID

internal class TampereInvoiceClientTest {

    private lateinit var client: InvoiceIntegrationClient
    private lateinit var server: MockWebServiceServer

    @BeforeEach
    fun setup() {
        val properties = TampereProperties(
            IpaasProperties("user", "pass"),
            InvoiceProperties("http://localhost:8080/salesOrder"),
            PaymentProperties(""),
            SummertimeAbsenceProperties(),
            BucketProperties(
                export = "trevaka-export-it",
            ),
            BiExportProperties(
                prefix = "bi",
            ),
        )
        val configuration = InvoiceConfiguration()
        val webServiceTemplate = configuration.webServiceTemplate(configuration.httpClient(properties), properties)
        addClientInterceptors(
            webServiceTemplate,
            newPayloadValidatingInterceptor(
                "iPaaS_Common_Types_v1_0.xsd",
                "SalesOrder_iPaaS_v11_2.xsd",
                "SAPSD_Myyntitilaus_v1_0_InlineSchema1.xsd",
            ),
        )
        client = configuration.invoiceIntegrationClient(webServiceTemplate, properties)
        server = MockWebServiceServer.createServer(webServiceTemplate)
    }

    @Test
    fun sendWithValidData() {
        val invoice1 = validInvoice()
        val invoice2 = validInvoice().copy(headOfFamily = personWithoutSSN())
        server.expect(connectionTo("http://localhost:8080/salesOrder"))
            .andExpect(payload(ClassPathResource("invoice-client/sales-order-request-1.xml")))
            .andRespond(withPayload(ClassPathResource("invoice-client/sales-order-response-ok.xml")))

        assertThat(client.send(listOf(invoice1, invoice2)))
            .returns(listOf(invoice1)) { it.succeeded }
            .returns(listOf()) { it.failed }
            .returns(listOf(invoice2)) { it.manuallySent }

        server.verify()
    }

    @Test
    fun sendWithInvoicingDetails() {
        val invoice1 = validInvoice().copy(
            headOfFamily = validPerson().copy(
                invoiceRecipientName = "Leena Meikäläinen",
                invoicingStreetAddress = "Kotikatu 3",
                invoicingPostalCode = "33960",
                invoicingPostOffice = "PIRKKALA",
            ),
        )
        server.expect(connectionTo("http://localhost:8080/salesOrder"))
            .andExpect(payload(ClassPathResource("invoice-client/sales-order-request-invoicing-details.xml")))
            .andRespond(withPayload(ClassPathResource("invoice-client/sales-order-response-ok.xml")))

        assertThat(client.send(listOf(invoice1)))
            .returns(listOf(invoice1)) { it.succeeded }
            .returns(listOf()) { it.failed }
            .returns(listOf()) { it.manuallySent }

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
            .returns(listOf()) { it.manuallySent }

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
            .returns(listOf()) { it.manuallySent }

        server.verify()
    }

    @Test
    fun sendWithClientFault() {
        val invoice1 = validInvoice()
        val invoice2 = validInvoice().copy(headOfFamily = personWithoutSSN())

        server.expect(connectionTo("http://localhost:8080/salesOrder"))
            .andRespond(withClientOrSenderFault("test", Locale.ENGLISH))

        val thrown = catchThrowable { client.send(listOf(invoice1, invoice2)) }

        assertThat(thrown).isInstanceOf(SoapFaultClientException::class.java)
        server.verify()
    }

    @Test
    fun sendWithServerFault() {
        val invoice1 = validInvoice()
        val invoice2 = validInvoice().copy(headOfFamily = personWithoutSSN())
        server.expect(connectionTo("http://localhost:8080/salesOrder"))
            .andRespond(withServerOrReceiverFault("test", Locale.ENGLISH))

        val thrown = catchThrowable { client.send(listOf(invoice1, invoice2)) }

        assertThat(thrown).isInstanceOf(SoapFaultClientException::class.java)
        server.verify()
    }

    @Test
    fun sendZeroTotalSumInvoices() {
        val invoice1 = validInvoice().copy(rows = listOf(validInvoiceRow(15000), validInvoiceRow(-15000, ProductKey("DAYCARE_DISCOUNT"))))
        val invoice2 = validInvoice().copy(headOfFamily = personWithoutSSN(), rows = listOf(validInvoiceRow(25000), validInvoiceRow(-25000, ProductKey("DAYCARE_DISCOUNT"))))

        assertThat(client.send(listOf(invoice1, invoice2)))
            .returns(listOf(invoice1, invoice2)) { it.succeeded }
            .returns(listOf()) { it.failed }
            .returns(listOf()) { it.manuallySent }

        server.verify()
    }
}

fun validInvoice(): InvoiceDetailed {
    val headOfFamily = validPerson()
    val invoiceRow1 = validInvoiceRow(24300)
    val invoiceRow2 = validInvoiceRow(48200).copy(
        child = PersonDetailed(
            PersonId(UUID.randomUUID()), LocalDate.of(2015, 11, 26), null,
            "Maiju", "Meikäläinen",
            null, "", "", "",
            "", null, "", null, restrictedDetailsEnabled = false,
        ),
        costCenter = "284823",
        product = ProductKey("PRESCHOOL_WITH_DAYCARE"),
        description = "kuvaus2",
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
        null,
        emptyList(),
    )
}

fun validInvoiceRow(unitPrice: Int, productKey: ProductKey = ProductKey("DAYCARE"), costCenter: String = "131885", description: String = "kuvaus1") = InvoiceRowDetailed(
    InvoiceRowId(UUID.randomUUID()),
    PersonDetailed(
        PersonId(UUID.randomUUID()), LocalDate.of(2018, 1, 1), null,
        "Matti", "Meikäläinen",
        null, "", "", "",
        "", null, "", null, restrictedDetailsEnabled = false,
    ),
    1, unitPrice,
    LocalDate.of(2021, 1, 1),
    LocalDate.of(2021, 1, 31),
    productKey, DaycareId(UUID.randomUUID()), "unit1", ProviderType.MUNICIPAL, setOf(CareType.CENTRE), costCenter, null, null, description,
    correctionId = null,
    note = null,
)

fun validPerson() = PersonDetailed(
    PersonId(UUID.randomUUID()), LocalDate.of(1982, 3, 31), null,
    "Maija", "Meikäläinen",
    "310382-956D", "Meikäläisenkuja 6 B 7", "33730", "TAMPERE",
    "", null, "", null, restrictedDetailsEnabled = false,
)

fun personWithoutSSN() = PersonDetailed(
    PersonId(UUID.randomUUID()), LocalDate.of(1982, 3, 31), null,
    "Maija", "Meikäläinen",
    null, "Meikäläisenkuja 6 B 7", "33730", "TAMPERE",
    "", null, "", null, restrictedDetailsEnabled = false,
)
