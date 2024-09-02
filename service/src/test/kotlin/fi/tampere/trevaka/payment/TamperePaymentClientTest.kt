// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.payment

import fi.espoo.evaka.daycare.CareType
import fi.espoo.evaka.invoicing.domain.Payment
import fi.espoo.evaka.invoicing.domain.PaymentIntegrationClient
import fi.espoo.evaka.invoicing.domain.PaymentStatus
import fi.espoo.evaka.invoicing.domain.PaymentUnit
import fi.espoo.evaka.shared.DaycareId
import fi.espoo.evaka.shared.PaymentId
import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.domain.DateRange
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.tampere.trevaka.BiExportProperties
import fi.tampere.trevaka.BucketProperties
import fi.tampere.trevaka.InvoiceProperties
import fi.tampere.trevaka.PaymentProperties
import fi.tampere.trevaka.SummertimeAbsenceProperties
import fi.tampere.trevaka.TampereConfig
import fi.tampere.trevaka.TampereProperties
import fi.tampere.trevaka.invoice.config.InvoiceConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.springframework.core.io.ClassPathResource
import org.springframework.ws.test.client.MockWebServiceServer
import org.springframework.ws.test.client.RequestMatchers.connectionTo
import org.springframework.ws.test.client.RequestMatchers.payload
import org.springframework.ws.test.client.ResponseCreators.withPayload
import trevaka.ipaas.IpaasProperties
import java.time.LocalDate
import java.time.Month
import java.util.*

class TamperePaymentClientTest {

    private lateinit var client: PaymentIntegrationClient
    private lateinit var server: MockWebServiceServer
    private lateinit var tx: Database.Read

    @BeforeEach
    fun setup() {
        val properties = TampereProperties(
            IpaasProperties("user", "pass"),
            InvoiceProperties(""),
            PaymentProperties("http://localhost:8080/payableAccounting"),
            SummertimeAbsenceProperties(),
            BucketProperties(
                export = "trevaka-export-it",
            ),
            BiExportProperties(
                prefix = "bi",
            ),
        )
        val configuration = TampereConfig()
        val webServiceTemplate = configuration.webServiceTemplate(InvoiceConfiguration().httpClient(properties))
        client = configuration.paymentIntegrationClient(webServiceTemplate, properties)
        server = MockWebServiceServer.createServer(webServiceTemplate)
        tx = mock {}
    }

    @Test
    fun `send with multiple payments`() {
        val payment1 = testPayment
        val payment2 = testPayment
        server.expect(connectionTo("http://localhost:8080/payableAccounting"))
            .andExpect(payload(ClassPathResource("payment-client/payable-accounting.xml")))
            .andRespond(withPayload(ClassPathResource("payment-client/payable-accounting-response-ok.xml")))

        val result = client.send(listOf(payment1, payment2), tx)

        assertThat(result)
            .returns(listOf(payment1, payment2)) { it.succeeded }
            .returns(emptyList()) { it.failed }
    }
}

internal val testPayment = Payment(
    id = PaymentId(UUID.randomUUID()),
    created = HelsinkiDateTime.now(),
    updated = HelsinkiDateTime.now(),
    unit = PaymentUnit(
        id = DaycareId(UUID.randomUUID()),
        name = "Esimerkkiyksikkö",
        businessId = "1234567-9",
        iban = "FIxxxxxxxxxxxx",
        providerId = "81591",
        careType = setOf(CareType.CENTRE),
    ),
    number = 134910,
    period = DateRange.ofMonth(2024, Month.JULY),
    amount = 35000,
    status = PaymentStatus.CONFIRMED,
    paymentDate = LocalDate.of(2024, 7, 27),
    dueDate = LocalDate.of(2024, 8, 31),
    sentAt = null,
    sentBy = null,
)
