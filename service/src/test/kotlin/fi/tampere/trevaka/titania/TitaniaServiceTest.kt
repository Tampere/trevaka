// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.titania

import fi.espoo.evaka.attendance.StaffAttendanceType
import fi.espoo.evaka.pis.createEmployee
import fi.espoo.evaka.shared.EmployeeId
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.tampere.trevaka.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.time.LocalTime

internal class TitaniaServiceTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var titaniaService: TitaniaService

    @Test
    fun `updateWorkingTimeEvents`() {
        val employeeId = runInTransaction { tx -> tx.createEmployee(testEmployee.copy(employeeNumber = "176716")).id }

        val response1 = runInTransaction { tx ->
            titaniaService.updateWorkingTimeEvents(tx, titaniaUpdateRequestValidExampleData)
        }
        assertThat(response1).returns("OK") { it.message }
        val plans1 = runInTransaction { tx -> tx.findStaffAttendancePlansBy() }
        assertThat(plans1).extracting<EmployeeId> { it.employeeId }.containsExactly(employeeId)

        val response2 = runInTransaction { tx ->
            titaniaService.updateWorkingTimeEvents(tx, titaniaUpdateRequestValidExampleData)
        }
        assertThat(response2).returns("OK") { it.message }
        val plans2 = runInTransaction { tx -> tx.findStaffAttendancePlansBy() }
        assertThat(plans2).extracting<EmployeeId> { it.employeeId }.containsExactly(employeeId)
    }

    @Test
    fun `updateWorkingTimeEventsInternal`() {
        val employeeId = runInTransaction { tx -> tx.createEmployee(testEmployee.copy(employeeNumber = "176716")).id }

        val response1 = runInTransaction { tx ->
            titaniaService.updateWorkingTimeEventsInternal(tx, titaniaUpdateRequestValidExampleData)
        }
        assertThat(response1.deleted).isEmpty()
        assertThat(response1.inserted).containsExactlyInAnyOrder(
            StaffAttendancePlan(
                employeeId = employeeId,
                type = StaffAttendanceType.PRESENT,
                HelsinkiDateTime.of(
                    LocalDate.of(2011, 1, 3),
                    LocalTime.of(7, 0)
                ),
                HelsinkiDateTime.of(
                    LocalDate.of(2011, 1, 3),
                    LocalTime.of(15, 0)
                ),
                description = null
            )
        )

        val response2 = runInTransaction { tx ->
            titaniaService.updateWorkingTimeEventsInternal(tx, titaniaUpdateRequestValidExampleData)
        }
        assertThat(response2.deleted).isEqualTo(response1.inserted)
        assertThat(response2.inserted).isEqualTo(response1.inserted)
    }

    @Test
    fun `updateWorkingTimeEvents throws when event date out of period`() {
        val request = UpdateWorkingTimeEventsRequest(
            period = TitaniaPeriod.from(LocalDate.of(2022, 6, 15)),
            schedulingUnit = listOf(
                TitaniaSchedulingUnit(
                    code = "sch1",
                    occupation = listOf(
                        TitaniaOccupation(
                            code = "occ2",
                            name = "Occupation 2",
                            person = listOf(
                                TitaniaPerson(
                                    employeeId = "emp1",
                                    name = "Employee 1",
                                    actualWorkingTimeEvents = TitaniaWorkingTimeEvents(
                                        event = listOf(
                                            TitaniaWorkingTimeEvent(
                                                date = LocalDate.of(2022, 6, 14),
                                                beginTime = LocalTime.of(9, 6),
                                                endTime = LocalTime.of(15, 22)
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        val response = catchThrowable { runInTransaction { tx -> titaniaService.updateWorkingTimeEvents(tx, request) } }

        assertThat(response)
            .isExactlyInstanceOf(TitaniaException::class.java)
            .hasMessage("Event date 2022-06-14 is out of period (2022-06-15 - 2022-06-15)")
    }

    @Test
    fun `updateWorkingTimeEvents throws when unknown employee numbers`() {
        val request = UpdateWorkingTimeEventsRequest(
            period = TitaniaPeriod.from(LocalDate.of(2022, 6, 15)),
            schedulingUnit = listOf(
                TitaniaSchedulingUnit(
                    code = "sch1",
                    occupation = listOf(
                        TitaniaOccupation(
                            code = "occ2",
                            name = "Occupation 2",
                            person = listOf(
                                TitaniaPerson(
                                    employeeId = "emp1",
                                    name = "Employee 1",
                                    actualWorkingTimeEvents = TitaniaWorkingTimeEvents(
                                        event = listOf(
                                            TitaniaWorkingTimeEvent(
                                                date = LocalDate.of(2022, 6, 15),
                                                beginTime = LocalTime.of(9, 6),
                                                endTime = LocalTime.of(15, 22)
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        val response = catchThrowable { runInTransaction { tx -> titaniaService.updateWorkingTimeEvents(tx, request) } }

        assertThat(response)
            .isExactlyInstanceOf(TitaniaException::class.java)
            .hasMessage("Unknown employee number: emp1")
    }

    @Test
    fun `getStampedWorkingTimeEvents`() {
        val response = runInTransaction { tx ->
            titaniaService.getStampedWorkingTimeEvents(tx, titaniaGetRequestValidExampleData)
        }

        assertThat(response).returns(
            listOf(
                TitaniaStampedUnitResponse(
                    code = "E1100",
                    name = "Suunnittelupiste A",
                    person = emptyList()
                )
            )
        ) { it.schedulingUnit }
    }

}
