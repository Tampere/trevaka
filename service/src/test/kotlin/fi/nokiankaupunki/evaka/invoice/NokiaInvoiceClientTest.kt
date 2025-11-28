// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.nokiankaupunki.evaka.invoice

import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.nokiankaupunki.evaka.InvoiceProperties
import fi.nokiankaupunki.evaka.invoice.service.validInvoice
import fi.nokiankaupunki.evaka.invoice.service.validPerson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import trevaka.invoice.expectedRowLength

private val properties = InvoiceProperties("123", "45")

class NokiaInvoiceClientTest {
    @Test
    fun `toInvoiceHeaderRow returns correct length`() {
        val fields = toInvoiceHeaderRow("", validInvoice(), HelsinkiDateTime.now(), properties)
        assertEquals(expectedRowLength(803, 70), fields.sumOf { it.length })
    }

    @Test
    fun `toCodebtorRow returns correct length`() {
        val fields = toCodebtorRow("", validPerson())
        assertEquals(expectedRowLength(312, 50), fields.sumOf { it.length })
    }

    @Test
    fun `toInvoiceRow returns correct length`() {
        val fields = toInvoiceRow("", validInvoice().rows[0])
        assertEquals(expectedRowLength(265, 9 + 2), fields.sumOf { it.length })
    }

    @Test
    fun `toDescriptionRow returns correct length`() {
        val fields = toDescriptionRow("", "")
        assertEquals(expectedRowLength(121, 10), fields.sumOf { it.length })
    }
}
