// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.nokiankaupunki.evaka.invoice

import fi.espoo.evaka.invoicing.domain.InvoiceDetailed
import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.espoo.evaka.shared.domain.MockEvakaClock
import fi.espoo.evaka.shared.sftp.SftpClient
import fi.nokiankaupunki.evaka.AbstractNokiaIntegrationTest
import fi.nokiankaupunki.evaka.invoice.service.validInvoice
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.test.context.TestPropertySource
import java.time.LocalDate
import java.time.LocalTime

@TestPropertySource(
    properties = [
        "nokia.invoice.version=V2026",
    ],
)
class NokiaInvoiceClientIT : AbstractNokiaIntegrationTest() {
    @Autowired private lateinit var invoiceIntegrationClient: InvoiceIntegrationClient
    private lateinit var sftpClient: SftpClient

    @BeforeEach
    fun init() {
        sftpClient = SftpClient(properties.invoice.sftp!!.toSftpEnv())
    }

    @Test
    fun `valid invoice with invoice number`() {
        whenever(clockService.clock()).thenReturn(
            MockEvakaClock(
                HelsinkiDateTime.of(
                    LocalDate.of(2021, 2, 1),
                    LocalTime.of(12, 34),
                ),
            ),
        )
        val invoices = listOf(validInvoice().copy(number = 1))

        val result = invoiceIntegrationClient.send(invoices)

        assertEquals(invoices, result.succeeded)
        assertEquals(emptyList<InvoiceDetailed>(), result.failed)
        assertEquals(emptyList<InvoiceDetailed>(), result.manuallySent)
        val data = sftpClient.getAsString("upload/536_56_202102011234.dat", Charsets.ISO_8859_1)
        assertEquals(
            ClassPathResource("invoice-client/nokia-invoice-2026.dat").getContentAsString(Charsets.ISO_8859_1),
            data,
        )
    }

    @Test
    fun `invoice without ssn is marked as manually sent`() {
        whenever(clockService.clock()).thenReturn(
            MockEvakaClock(
                HelsinkiDateTime.of(
                    LocalDate.of(2021, 2, 1),
                    LocalTime.of(12, 34),
                ),
            ),
        )
        val invoices = listOf(validInvoice().copy(headOfFamily = validInvoice().headOfFamily.copy(ssn = null)))

        val result = invoiceIntegrationClient.send(invoices)

        assertEquals(emptyList<InvoiceDetailed>(), result.succeeded)
        assertEquals(emptyList<InvoiceDetailed>(), result.failed)
        assertEquals(invoices, result.manuallySent)
    }
}
