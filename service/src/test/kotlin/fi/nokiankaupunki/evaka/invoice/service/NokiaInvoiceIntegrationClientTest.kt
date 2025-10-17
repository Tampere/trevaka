// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.nokiankaupunki.evaka.invoice.service

import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.espoo.evaka.shared.domain.MockEvakaClock
import fi.nokiankaupunki.evaka.AbstractNokiaIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalTime

class NokiaInvoiceIntegrationClientTest : AbstractNokiaIntegrationTest() {
    @Autowired
    private lateinit var invoiceIntegrationClient: InvoiceIntegrationClient

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

        assertThat(result)
            .returns(invoices) { it.succeeded }
            .returns(emptyList()) { it.failed }
            .returns(emptyList()) { it.manuallySent }
        val data = getS3Object(properties.bucket.export, "invoices/536_56_202102011234.dat")
            .use { it.readAllBytes().toString(StandardCharsets.ISO_8859_1) }
        assertEquals(
            ClassPathResource("invoice-client/nokia-invoice-2024.dat").getContentAsString(Charsets.ISO_8859_1),
            data,
        )
    }
}
