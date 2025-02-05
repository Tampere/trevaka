// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.orivesi.evaka.invoice

import fi.espoo.evaka.invoicing.data.getInvoicesByIds
import fi.espoo.evaka.invoicing.domain.InvoiceStatus
import fi.espoo.evaka.invoicing.service.InvoiceService
import fi.espoo.evaka.shared.AreaId
import fi.espoo.evaka.shared.auth.UserRole
import fi.espoo.evaka.shared.dev.DevEmployee
import fi.espoo.evaka.shared.dev.DevInvoice
import fi.espoo.evaka.shared.dev.DevPerson
import fi.espoo.evaka.shared.dev.DevPersonType
import fi.espoo.evaka.shared.dev.insert
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.espoo.evaka.shared.domain.MockEvakaClock
import fi.orivesi.evaka.AbstractOrivesiIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class OrivesiInvoiceNumberTest : AbstractOrivesiIntegrationTest() {
    @Autowired
    private lateinit var invoiceService: InvoiceService

    @Test
    fun `first invoice has correct number`() {
        val invoices = db.transaction { tx ->
            val employee = DevEmployee(roles = setOf(UserRole.FINANCE_ADMIN)).also { tx.insert(it) }
            val clock = MockEvakaClock(HelsinkiDateTime.of(LocalDate.of(2025, 1, 7), LocalTime.of(12, 34)))
            val headOfFamilyId = tx.insert(DevPerson(), DevPersonType.ADULT)
            val draftInvoice1 = tx.insert(
                DevInvoice(
                    status = InvoiceStatus.DRAFT,
                    number = null,
                    invoiceDate = LocalDate.EPOCH,
                    dueDate = LocalDate.EPOCH,
                    periodStart = LocalDate.of(2024, 12, 1),
                    periodEnd = LocalDate.of(2024, 12, 31),
                    headOfFamilyId = headOfFamilyId,
                    areaId = AreaId(UUID.fromString("e463e978-4de3-44d2-ab5f-4ca883453385")),
                ),
            )
            val invoiceIds = listOf(draftInvoice1)
            val invoiceDate = LocalDate.of(2025, 1, 7)
            val dueDate = invoiceDate.plusWeeks(2)

            invoiceService.sendInvoices(tx, employee.evakaUserId, clock.now(), invoiceIds, invoiceDate, dueDate)
            tx.getInvoicesByIds(invoiceIds)
        }

        assertThat(invoices).extracting<Long?> { it.number }.containsExactly(30000001)
    }

    @Test
    fun `second invoice has correct number`() {
        val invoices = db.transaction { tx ->
            val employee = DevEmployee(roles = setOf(UserRole.FINANCE_ADMIN)).also { tx.insert(it) }
            val clock = MockEvakaClock(HelsinkiDateTime.of(LocalDate.of(2025, 1, 7), LocalTime.of(12, 34)))
            val headOfFamilyId = tx.insert(DevPerson(), DevPersonType.ADULT)
            tx.insert(
                DevInvoice(
                    status = InvoiceStatus.SENT,
                    number = 30000001, // current series start
                    invoiceDate = LocalDate.of(2024, 12, 9),
                    dueDate = LocalDate.of(2024, 12, 23),
                    periodStart = LocalDate.of(2024, 11, 1),
                    periodEnd = LocalDate.of(2024, 11, 30),
                    headOfFamilyId = headOfFamilyId,
                    areaId = AreaId(UUID.fromString("e463e978-4de3-44d2-ab5f-4ca883453385")),
                ),
            )
            val draftInvoice1 = tx.insert(
                DevInvoice(
                    status = InvoiceStatus.DRAFT,
                    number = null,
                    invoiceDate = LocalDate.EPOCH,
                    dueDate = LocalDate.EPOCH,
                    periodStart = LocalDate.of(2024, 12, 1),
                    periodEnd = LocalDate.of(2024, 12, 31),
                    headOfFamilyId = headOfFamilyId,
                    areaId = AreaId(UUID.fromString("e463e978-4de3-44d2-ab5f-4ca883453385")),
                ),
            )
            val invoiceIds = listOf(draftInvoice1)
            val invoiceDate = LocalDate.of(2025, 1, 7)
            val dueDate = invoiceDate.plusWeeks(2)

            invoiceService.sendInvoices(tx, employee.evakaUserId, clock.now(), invoiceIds, invoiceDate, dueDate)
            tx.getInvoicesByIds(invoiceIds)
        }

        assertThat(invoices).extracting<Long?> { it.number }.containsExactly(30000002)
    }

    @Test
    fun `first invoice with third series start has correct number`() {
        val invoices = db.transaction { tx ->
            val employee = DevEmployee(roles = setOf(UserRole.FINANCE_ADMIN)).also { tx.insert(it) }
            val clock = MockEvakaClock(HelsinkiDateTime.of(LocalDate.of(2025, 1, 7), LocalTime.of(12, 34)))
            val headOfFamilyId = tx.insert(DevPerson(), DevPersonType.ADULT)
            tx.insert(
                DevInvoice(
                    status = InvoiceStatus.SENT,
                    number = 152705, // first series start
                    invoiceDate = LocalDate.of(2024, 11, 11),
                    dueDate = LocalDate.of(2024, 11, 25),
                    periodStart = LocalDate.of(2024, 10, 1),
                    periodEnd = LocalDate.of(2024, 10, 31),
                    headOfFamilyId = headOfFamilyId,
                    areaId = AreaId(UUID.fromString("e463e978-4de3-44d2-ab5f-4ca883453385")),
                ),
            )
            tx.insert(
                DevInvoice(
                    status = InvoiceStatus.SENT,
                    number = 54000001, // second series start
                    invoiceDate = LocalDate.of(2024, 12, 9),
                    dueDate = LocalDate.of(2024, 12, 23),
                    periodStart = LocalDate.of(2024, 11, 1),
                    periodEnd = LocalDate.of(2024, 11, 30),
                    headOfFamilyId = headOfFamilyId,
                    areaId = AreaId(UUID.fromString("e463e978-4de3-44d2-ab5f-4ca883453385")),
                ),
            )
            val draftInvoice1 = tx.insert(
                DevInvoice(
                    status = InvoiceStatus.DRAFT,
                    number = null,
                    invoiceDate = LocalDate.EPOCH,
                    dueDate = LocalDate.EPOCH,
                    periodStart = LocalDate.of(2024, 12, 1),
                    periodEnd = LocalDate.of(2024, 12, 31),
                    headOfFamilyId = headOfFamilyId,
                    areaId = AreaId(UUID.fromString("e463e978-4de3-44d2-ab5f-4ca883453385")),
                ),
            )
            val invoiceIds = listOf(draftInvoice1)
            val invoiceDate = LocalDate.of(2025, 1, 7)
            val dueDate = invoiceDate.plusWeeks(2)

            invoiceService.sendInvoices(tx, employee.evakaUserId, clock.now(), invoiceIds, invoiceDate, dueDate)
            tx.getInvoicesByIds(invoiceIds)
        }

        assertThat(invoices).extracting<Long?> { it.number }.containsExactly(30000001)
    }
}
