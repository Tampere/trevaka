// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.titania

import fi.espoo.evaka.attendance.StaffAttendanceType
import fi.espoo.evaka.pis.createEmployee
import fi.espoo.evaka.shared.EmployeeId
import fi.espoo.evaka.shared.domain.FiniteDateRange
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.tampere.trevaka.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

internal class TitaniaQueriesTest : AbstractIntegrationTest() {

    @Test
    fun `getEmployeeIdsByNumbers works`() {
        db.transaction { tx ->
            val employee1 = tx.createEmployee(testEmployee.copy(employeeNumber = "1111"))
            val employee2 = tx.createEmployee(testEmployee.copy(employeeNumber = "2222"))
            val employee3 = tx.createEmployee(testEmployee.copy(employeeNumber = "3333"))

            assertThat(tx.getEmployeeIdsByNumbers(listOf("1111", "3333")))
                .containsExactlyInAnyOrderEntriesOf(mapOf("1111" to employee1.id, "3333" to employee3.id))
            assertThat(tx.getEmployeeIdsByNumbers(listOf("2222", "5555")))
                .containsExactlyEntriesOf(mapOf("2222" to employee2.id))
        }
    }

    @Test
    fun `getEmployeeIdsByNumbers works with empty input`() {
        val employeeNumbers = emptyList<String>()
        val employeeIds = db.transaction { tx -> tx.getEmployeeIdsByNumbers(employeeNumbers) }
        assertThat(employeeIds).isEmpty()
    }

    @Test
    fun `staff attendance plan insert and delete should work`() {
        db.transaction { tx ->
            val employee1 = tx.createEmployee(testEmployee)
            val employee2 = tx.createEmployee(testEmployee)
            val employee3 = tx.createEmployee(testEmployee)

            val inserted = tx.insertStaffAttendancePlans(
                listOf(
                    StaffAttendancePlan(
                        employeeId = employee1.id,
                        type = StaffAttendanceType.PRESENT,
                        startTime = HelsinkiDateTime.of(LocalDate.of(2022, 5, 31), LocalTime.of(7, 32)),
                        endTime = HelsinkiDateTime.of(LocalDate.of(2022, 5, 31), LocalTime.of(14, 54)),
                        description = null,
                    ),
                    StaffAttendancePlan(
                        employeeId = employee2.id,
                        type = StaffAttendanceType.PRESENT,
                        startTime = HelsinkiDateTime.of(LocalDate.of(2022, 5, 31), LocalTime.of(7, 32)),
                        endTime = HelsinkiDateTime.of(LocalDate.of(2022, 5, 31), LocalTime.of(14, 54)),
                        description = null,
                    ),
                    StaffAttendancePlan(
                        employeeId = employee2.id,
                        type = StaffAttendanceType.PRESENT,
                        startTime = HelsinkiDateTime.of(LocalDate.of(2022, 6, 1), LocalTime.of(7, 32)),
                        endTime = HelsinkiDateTime.of(LocalDate.of(2022, 6, 1), LocalTime.of(14, 54)),
                        description = null,
                    ),
                    StaffAttendancePlan(
                        employeeId = employee3.id,
                        type = StaffAttendanceType.PRESENT,
                        startTime = HelsinkiDateTime.of(LocalDate.of(2022, 5, 31), LocalTime.of(7, 32)),
                        endTime = HelsinkiDateTime.of(LocalDate.of(2022, 5, 31), LocalTime.of(14, 54)),
                        description = null,
                    )
                )
            )
            assertThat(inserted)
                .containsExactlyInAnyOrder(1, 1, 1, 1)

            val deleted = tx.deleteStaffAttendancePlansBy(
                employeeIds = listOf(employee2.id, employee3.id),
                period = LocalDate.of(2022, 5, 31).let { FiniteDateRange(it, it) },
            )
            assertThat(deleted).extracting<EmployeeId> { it.employeeId }
                .containsExactlyInAnyOrder(employee2.id, employee3.id)

            val existing = tx.findStaffAttendancePlansBy(
                period = FiniteDateRange(
                    LocalDate.of(2022, 1, 1),
                    LocalDate.of(2022, 12, 31)
                )
            )
            assertThat(existing).extracting<EmployeeId> { it.employeeId }
                .containsExactlyInAnyOrder(employee1.id, employee2.id)
        }
    }

    @Test
    fun `insertStaffAttendancePlans should work with empty`() {
        val plans = emptyList<StaffAttendancePlan>()
        val inserted = db.transaction { tx -> tx.insertStaffAttendancePlans(plans) }
        assertThat(inserted).isEmpty()
    }

    @Test
    fun `findStaffAttendancePlansBy should return all with empty criteria`() {
        val inserted = db.transaction { tx ->
            val employee = tx.createEmployee(testEmployee)
            tx.insertStaffAttendancePlans(listOf(testStaffAttendancePlan.copy(employeeId = employee.id)))
            employee
        }

        val selected = db.read { tx -> tx.findStaffAttendancePlansBy() }

        assertThat(selected).extracting<EmployeeId> { it.employeeId }.containsExactly(inserted.id)
    }

    @Test
    fun `findStaffAttendancePlansBy should return empty with empty employee ids`() {
        db.transaction { tx ->
            val employee = tx.createEmployee(testEmployee)
            tx.insertStaffAttendancePlans(listOf(testStaffAttendancePlan.copy(employeeId = employee.id)))
            employee
        }

        val selected = db.read { tx -> tx.findStaffAttendancePlansBy(employeeIds = emptyList()) }

        assertThat(selected).extracting<EmployeeId> { it.employeeId }.isEmpty()
    }

    @Test
    fun `deleteStaffAttendancePlansBy should return all with empty criteria`() {
        val inserted = db.transaction { tx ->
            val employee = tx.createEmployee(testEmployee)
            tx.insertStaffAttendancePlans(listOf(testStaffAttendancePlan.copy(employeeId = employee.id)))
            employee
        }

        val deleted = db.transaction { tx -> tx.deleteStaffAttendancePlansBy() }

        assertThat(deleted).extracting<EmployeeId> { it.employeeId }.containsExactly(inserted.id)
    }

    @Test
    fun `deleteStaffAttendancePlansBy should return empty with empty employee ids`() {
        db.transaction { tx ->
            val employee = tx.createEmployee(testEmployee)
            tx.insertStaffAttendancePlans(listOf(testStaffAttendancePlan.copy(employeeId = employee.id)))
            employee
        }

        val deleted = db.transaction { tx -> tx.deleteStaffAttendancePlansBy(employeeIds = emptyList()) }

        assertThat(deleted).isEmpty()
    }
}

private val testStaffAttendancePlan = StaffAttendancePlan(
    employeeId = EmployeeId(UUID.randomUUID()),
    type = StaffAttendanceType.PRESENT,
    startTime = HelsinkiDateTime.of(LocalDate.of(2022, 6, 7), LocalTime.of(8, 12)),
    endTime = HelsinkiDateTime.of(LocalDate.of(2022, 6, 7), LocalTime.of(15, 41)),
    description = null,
)
