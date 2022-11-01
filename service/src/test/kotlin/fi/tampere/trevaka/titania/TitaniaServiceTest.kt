// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.titania

import fi.espoo.evaka.attendance.StaffAttendanceType
import fi.espoo.evaka.attendance.upsertStaffAttendance
import fi.espoo.evaka.pis.createEmployee
import fi.espoo.evaka.shared.AreaId
import fi.espoo.evaka.shared.EmployeeId
import fi.espoo.evaka.shared.dev.DevDaycare
import fi.espoo.evaka.shared.dev.DevDaycareGroup
import fi.espoo.evaka.shared.dev.DevStaffAttendancePlan
import fi.espoo.evaka.shared.dev.insertTestDaycare
import fi.espoo.evaka.shared.dev.insertTestDaycareGroup
import fi.espoo.evaka.shared.dev.insertTestStaffAttendancePlan
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.tampere.trevaka.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.UUID

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
                    LocalTime.of(23, 59)
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
    fun `updateWorkingTimeEventsInternal merge overnight events`() {
        val employee1Id = runInTransaction { tx -> tx.createEmployee(testEmployee.copy(employeeNumber = "176716")).id }
        val employee2Id = runInTransaction { tx -> tx.createEmployee(testEmployee.copy(employeeNumber = "949382")).id }

        val request = UpdateWorkingTimeEventsRequest(
            period = TitaniaPeriod(beginDate = LocalDate.of(2022, 10, 31), endDate = LocalDate.of(2022, 11, 2)),
            schedulingUnit = listOf(
                TitaniaSchedulingUnit(
                    code = "",
                    occupation = listOf(
                        TitaniaOccupation(
                            code = "",
                            name = "",
                            person = listOf(
                                TitaniaPerson(
                                    employeeId = "00176716",
                                    name = "",
                                    actualWorkingTimeEvents = TitaniaWorkingTimeEvents(
                                        event = listOf(
                                            TitaniaWorkingTimeEvent(
                                                date = LocalDate.of(2022, 11, 1),
                                                beginTime = LocalTime.of(0, 0),
                                                endTime = LocalTime.of(8, 0)
                                            ),
                                            TitaniaWorkingTimeEvent(
                                                date = LocalDate.of(2022, 11, 1),
                                                beginTime = LocalTime.of(20, 0),
                                                endTime = LocalTime.of(23, 59)
                                            ), TitaniaWorkingTimeEvent(
                                                date = LocalDate.of(2022, 10, 31),
                                                beginTime = LocalTime.of(20, 0),
                                                endTime = LocalTime.of(0, 0) // 24:00 from titania
                                            ), TitaniaWorkingTimeEvent(
                                                date = LocalDate.of(2022, 11, 2),
                                                beginTime = LocalTime.of(0, 0),
                                                endTime = LocalTime.of(8, 0)
                                            )
                                        )
                                    )
                                ),
                                TitaniaPerson(
                                    employeeId = "00949382",
                                    name = "",
                                    actualWorkingTimeEvents = TitaniaWorkingTimeEvents(
                                        event = listOf(
                                            TitaniaWorkingTimeEvent(
                                                date = LocalDate.of(2022, 11, 2),
                                                beginTime = LocalTime.of(0, 0),
                                                endTime = LocalTime.of(6, 0)
                                            ),
                                            TitaniaWorkingTimeEvent(
                                                date = LocalDate.of(2022, 11, 1),
                                                beginTime = LocalTime.of(0, 0),
                                                endTime = LocalTime.of(23, 59)
                                            ), TitaniaWorkingTimeEvent(
                                                date = LocalDate.of(2022, 10, 31),
                                                beginTime = LocalTime.of(23, 0),
                                                endTime = LocalTime.of(23, 58)
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

        val response = runInTransaction { tx -> titaniaService.updateWorkingTimeEventsInternal(tx, request) }

        assertThat(response.deleted).isEmpty()
        assertThat(response.inserted).containsExactlyInAnyOrder(
            StaffAttendancePlan(
                employeeId = employee1Id,
                type = StaffAttendanceType.PRESENT,
                HelsinkiDateTime.of(
                    LocalDate.of(2022, 10, 31),
                    LocalTime.of(20, 0)
                ),
                HelsinkiDateTime.of(
                    LocalDate.of(2022, 11, 1),
                    LocalTime.of(8, 0)
                ),
                description = null
            ),
            StaffAttendancePlan(
                employeeId = employee1Id,
                type = StaffAttendanceType.PRESENT,
                HelsinkiDateTime.of(
                    LocalDate.of(2022, 11, 1),
                    LocalTime.of(20, 0)
                ),
                HelsinkiDateTime.of(
                    LocalDate.of(2022, 11, 2),
                    LocalTime.of(8, 0)
                ),
                description = null
            ),
            StaffAttendancePlan(
                employeeId = employee2Id,
                type = StaffAttendanceType.PRESENT,
                HelsinkiDateTime.of(
                    LocalDate.of(2022, 10, 31),
                    LocalTime.of(23, 0)
                ),
                HelsinkiDateTime.of(
                    LocalDate.of(2022, 10, 31),
                    LocalTime.of(23, 58)
                ),
                description = null
            ),
            StaffAttendancePlan(
                employeeId = employee2Id,
                type = StaffAttendanceType.PRESENT,
                HelsinkiDateTime.of(
                    LocalDate.of(2022, 11, 1),
                    LocalTime.of(0, 0)
                ),
                HelsinkiDateTime.of(
                    LocalDate.of(2022, 11, 2),
                    LocalTime.of(6, 0)
                ),
                description = null
            )
        )
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            // vakiokoodit
            "U, PRESENT",
            "K, TRAINING",
            // muut koodit (aina PRESENT)
            "X, PRESENT",
            "a, PRESENT",
        ]
    )
    fun `event code is mapped to attendance type correctly`(givenEventCode: String, expectedType: StaffAttendanceType) {
        val employeeId = runInTransaction { tx -> tx.createEmployee(testEmployee.copy(employeeNumber = "176716")).id }

        val response = runInTransaction { tx ->
            titaniaService.updateWorkingTimeEventsInternal(
                tx, UpdateWorkingTimeEventsRequest(
                    period = TitaniaPeriod(
                        beginDate = LocalDate.of(2022, 10, 12),
                        endDate = LocalDate.of(2022, 10, 12),
                    ),
                    schedulingUnit = listOf(
                        TitaniaSchedulingUnit(
                            code = "",
                            occupation = listOf(
                                TitaniaOccupation(
                                    code = "",
                                    name = "",
                                    person = listOf(
                                        TitaniaPerson(
                                            employeeId = "176716",
                                            name = "",
                                            actualWorkingTimeEvents = TitaniaWorkingTimeEvents(
                                                event = listOf(
                                                    TitaniaWorkingTimeEvent(
                                                        date = LocalDate.of(2022, 10, 12),
                                                        code = givenEventCode,
                                                        beginTime = LocalTime.of(9, 42),
                                                        endTime = LocalTime.of(9, 44)
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
            )
        }

        assertThat(response.deleted).isEmpty()
        assertThat(response.inserted).containsExactlyInAnyOrder(
            StaffAttendancePlan(
                employeeId = employeeId,
                type = expectedType,
                HelsinkiDateTime.of(
                    LocalDate.of(2022, 10, 12),
                    LocalTime.of(9, 42)
                ),
                HelsinkiDateTime.of(
                    LocalDate.of(2022, 10, 12),
                    LocalTime.of(9, 44)
                ),
                description = null
            )
        )
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
        runInTransaction { tx ->
            val unitId = tx.insertTestDaycare(
                DevDaycare(
                    areaId = AreaId(UUID.fromString("6529e31e-9777-11eb-ba88-33a923255570")),
                )
            )
            val groupId = tx.insertTestDaycareGroup(
                DevDaycareGroup(
                    daycareId = unitId,
                )
            )
            tx.createEmployee(
                testEmployee.copy(
                    firstName = "IINES",
                    lastName = "ANKKA",
                    employeeNumber = "177111",
                )
            ).let { (employeeId) ->
                tx.upsertStaffAttendance(
                    attendanceId = null,
                    employeeId = employeeId,
                    groupId = groupId,
                    arrivalTime = HelsinkiDateTime.of(LocalDate.of(2014, 3, 3), LocalTime.of(7, 0)),
                    departureTime = HelsinkiDateTime.of(LocalDate.of(2014, 3, 3), LocalTime.of(15, 0)),
                    occupancyCoefficient = BigDecimal("7.0"),
                    type = StaffAttendanceType.PRESENT,
                )
                tx.upsertStaffAttendance(
                    attendanceId = null,
                    employeeId = employeeId,
                    groupId = groupId,
                    arrivalTime = HelsinkiDateTime.of(LocalDate.of(2014, 3, 4), LocalTime.of(6, 30)),
                    departureTime = HelsinkiDateTime.of(LocalDate.of(2014, 3, 4), LocalTime.of(12, 0)),
                    occupancyCoefficient = BigDecimal("7.0"),
                    type = StaffAttendanceType.OVERTIME,
                )
            }
            tx.createEmployee(
                testEmployee.copy(
                    firstName = "HESSU",
                    lastName = "HOPO",
                    employeeNumber = "255145",
                )
            ).let { (employeeId) ->
                tx.upsertStaffAttendance(
                    attendanceId = null,
                    employeeId = employeeId,
                    groupId = groupId,
                    arrivalTime = HelsinkiDateTime.of(LocalDate.of(2014, 3, 3), LocalTime.of(7, 0)),
                    departureTime = HelsinkiDateTime.of(LocalDate.of(2014, 3, 3), LocalTime.of(11, 0)),
                    occupancyCoefficient = BigDecimal("7.0"),
                    type = StaffAttendanceType.PRESENT,
                )
                tx.upsertStaffAttendance(
                    attendanceId = null,
                    employeeId = employeeId,
                    groupId = groupId,
                    arrivalTime = HelsinkiDateTime.of(LocalDate.of(2014, 3, 3), LocalTime.of(12, 5)),
                    departureTime = HelsinkiDateTime.of(LocalDate.of(2014, 3, 3), LocalTime.of(16, 10)),
                    occupancyCoefficient = BigDecimal("7.0"),
                    type = StaffAttendanceType.PRESENT,
                )
                tx.upsertStaffAttendance(
                    attendanceId = null,
                    employeeId = employeeId,
                    groupId = groupId,
                    arrivalTime = HelsinkiDateTime.of(LocalDate.of(2014, 3, 4), LocalTime.of(10, 15)),
                    departureTime = HelsinkiDateTime.of(LocalDate.of(2014, 3, 4), LocalTime.of(17, 15)),
                    occupancyCoefficient = BigDecimal("7.0"),
                    type = StaffAttendanceType.OTHER_WORK,
                )
            }
        }

        val response = runInTransaction { tx ->
            titaniaService.getStampedWorkingTimeEvents(tx, titaniaGetRequestValidExampleData)
        }

        assertThat(response).isEqualTo(titaniaGetResponseValidExampleData)
    }

    @Test
    fun `getStampedWorkingTimeEvents with plan and overtime`() {
        runInTransaction { tx ->
            val unitId = tx.insertTestDaycare(
                DevDaycare(
                    areaId = AreaId(UUID.fromString("6529e31e-9777-11eb-ba88-33a923255570")),
                )
            )
            val groupId = tx.insertTestDaycareGroup(
                DevDaycareGroup(
                    daycareId = unitId,
                )
            )
            tx.createEmployee(
                testEmployee.copy(
                    firstName = "IINES",
                    lastName = "ANKKA",
                    employeeNumber = "177111",
                )
            ).let { (employeeId) ->
                tx.insertTestStaffAttendancePlan(
                    DevStaffAttendancePlan(
                        employeeId = employeeId,
                        type = StaffAttendanceType.PRESENT,
                        startTime = HelsinkiDateTime.of(LocalDate.of(2022, 10, 19), LocalTime.of(8, 0)),
                        endTime = HelsinkiDateTime.of(LocalDate.of(2022, 10, 19), LocalTime.of(16, 0)),
                        description = null,
                    )
                )
                tx.upsertStaffAttendance(
                    attendanceId = null,
                    employeeId = employeeId,
                    groupId = groupId,
                    arrivalTime = HelsinkiDateTime.of(LocalDate.of(2022, 10, 19), LocalTime.of(7, 42)),
                    departureTime = HelsinkiDateTime.of(LocalDate.of(2022, 10, 19), LocalTime.of(8, 2)),
                    occupancyCoefficient = BigDecimal("7.0"),
                    type = StaffAttendanceType.OVERTIME,
                )
                tx.upsertStaffAttendance(
                    attendanceId = null,
                    employeeId = employeeId,
                    groupId = groupId,
                    arrivalTime = HelsinkiDateTime.of(LocalDate.of(2022, 10, 19), LocalTime.of(8, 2)),
                    departureTime = HelsinkiDateTime.of(LocalDate.of(2022, 10, 19), LocalTime.of(16, 3)),
                    occupancyCoefficient = BigDecimal("7.0"),
                    type = StaffAttendanceType.PRESENT,
                )
            }
        }

        val response = runInTransaction { tx ->
            titaniaService.getStampedWorkingTimeEvents(
                tx, GetStampedWorkingTimeEventsRequest(
                    period = TitaniaPeriod(
                        beginDate = LocalDate.of(2022, 10, 19),
                        endDate = LocalDate.of(2022, 10, 19),
                    ),
                    schedulingUnit = listOf(
                        TitaniaStampedUnitRequest(
                            code = "from titania",
                            person = listOf(
                                TitaniaStampedPersonRequest(
                                    employeeId = "00177111",
                                )
                            )
                        )
                    )
                )
            )
        }

        assertThat(response).isEqualTo(
            GetStampedWorkingTimeEventsResponse(
                schedulingUnit = listOf(
                    TitaniaStampedUnitResponse(
                        code = "from titania",
                        person = listOf(
                            TitaniaStampedPersonResponse(
                                employeeId = "00177111",
                                name = "ANKKA IINES",
                                stampedWorkingTimeEvents = TitaniaStampedWorkingTimeEvents(
                                    event = listOf(
                                        TitaniaStampedWorkingTimeEvent(
                                            date = LocalDate.of(2022, 10, 19),
                                            beginTime = LocalTime.of(7, 42),
                                            beginReasonCode = "YT",
                                            endTime = LocalTime.of(8, 0),
                                            endReasonCode = null,
                                        ), TitaniaStampedWorkingTimeEvent(
                                            date = LocalDate.of(2022, 10, 19),
                                            beginTime = LocalTime.of(8, 0),
                                            beginReasonCode = null,
                                            endTime = LocalTime.of(16, 0),
                                            endReasonCode = null
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    }

    @Test
    fun `getStampedWorkingTimeEvents with overnight plan`() {
        runInTransaction { tx ->
            val unitId = tx.insertTestDaycare(
                DevDaycare(
                    areaId = AreaId(UUID.fromString("6529e31e-9777-11eb-ba88-33a923255570")),
                )
            )
            val groupId = tx.insertTestDaycareGroup(
                DevDaycareGroup(
                    daycareId = unitId,
                )
            )
            tx.createEmployee(
                testEmployee.copy(
                    firstName = "IINES",
                    lastName = "ANKKA",
                    employeeNumber = "177111",
                )
            ).let { (employeeId) ->
                tx.insertTestStaffAttendancePlan(
                    DevStaffAttendancePlan(
                        employeeId = employeeId,
                        type = StaffAttendanceType.PRESENT,
                        startTime = HelsinkiDateTime.of(LocalDate.of(2022, 10, 20), LocalTime.of(20, 0)),
                        endTime = HelsinkiDateTime.of(LocalDate.of(2022, 10, 21), LocalTime.of(8, 0)),
                        description = null,
                    )
                )
                tx.upsertStaffAttendance(
                    attendanceId = null,
                    employeeId = employeeId,
                    groupId = groupId,
                    arrivalTime = HelsinkiDateTime.of(LocalDate.of(2022, 10, 20), LocalTime.of(20, 5)),
                    departureTime = HelsinkiDateTime.of(LocalDate.of(2022, 10, 21), LocalTime.of(7, 56)),
                    occupancyCoefficient = BigDecimal("7.0"),
                    type = StaffAttendanceType.PRESENT,
                )
            }
        }

        val response = runInTransaction { tx ->
            titaniaService.getStampedWorkingTimeEvents(
                tx, GetStampedWorkingTimeEventsRequest(
                    period = TitaniaPeriod(
                        beginDate = LocalDate.of(2022, 10, 20),
                        endDate = LocalDate.of(2022, 10, 20),
                    ),
                    schedulingUnit = listOf(
                        TitaniaStampedUnitRequest(
                            code = "from titania",
                            person = listOf(
                                TitaniaStampedPersonRequest(
                                    employeeId = "00177111",
                                )
                            )
                        )
                    )
                )
            )
        }

        assertThat(response).isEqualTo(
            GetStampedWorkingTimeEventsResponse(
                schedulingUnit = listOf(
                    TitaniaStampedUnitResponse(
                        code = "from titania",
                        person = listOf(
                            TitaniaStampedPersonResponse(
                                employeeId = "00177111",
                                name = "ANKKA IINES",
                                stampedWorkingTimeEvents = TitaniaStampedWorkingTimeEvents(
                                    event = listOf(
                                        TitaniaStampedWorkingTimeEvent(
                                            date = LocalDate.of(2022, 10, 20),
                                            beginTime = LocalTime.of(20, 0),
                                            beginReasonCode = null,
                                            endTime = LocalTime.MAX.truncatedTo(ChronoUnit.MICROS),
                                            endReasonCode = null,
                                        ), TitaniaStampedWorkingTimeEvent(
                                            date = LocalDate.of(2022, 10, 21),
                                            beginTime = LocalTime.MIN,
                                            beginReasonCode = null,
                                            endTime = LocalTime.of(8, 0),
                                            endReasonCode = null
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    }

    @Test
    fun `getStampedWorkingTimeEvents with attendance not within plan`() {
        runInTransaction { tx ->
            val unitId = tx.insertTestDaycare(
                DevDaycare(
                    areaId = AreaId(UUID.fromString("6529e31e-9777-11eb-ba88-33a923255570")),
                )
            )
            val groupId = tx.insertTestDaycareGroup(
                DevDaycareGroup(
                    daycareId = unitId,
                )
            )
            tx.createEmployee(
                testEmployee.copy(
                    firstName = "IINES",
                    lastName = "ANKKA",
                    employeeNumber = "177111",
                )
            ).let { (employeeId) ->
                tx.insertTestStaffAttendancePlan(
                    DevStaffAttendancePlan(
                        employeeId = employeeId,
                        type = StaffAttendanceType.PRESENT,
                        startTime = HelsinkiDateTime.of(LocalDate.of(2022, 10, 20), LocalTime.of(8, 0)),
                        endTime = HelsinkiDateTime.of(LocalDate.of(2022, 10, 20), LocalTime.of(16, 0)),
                        description = null,
                    )
                )
                tx.upsertStaffAttendance(
                    attendanceId = null,
                    employeeId = employeeId,
                    groupId = groupId,
                    arrivalTime = HelsinkiDateTime.of(LocalDate.of(2022, 10, 20), LocalTime.of(7, 54)),
                    departureTime = HelsinkiDateTime.of(LocalDate.of(2022, 10, 20), LocalTime.of(16, 6)),
                    occupancyCoefficient = BigDecimal("7.0"),
                    type = StaffAttendanceType.PRESENT,
                )
            }
        }

        val response = runInTransaction { tx ->
            titaniaService.getStampedWorkingTimeEvents(
                tx, GetStampedWorkingTimeEventsRequest(
                    period = TitaniaPeriod(
                        beginDate = LocalDate.of(2022, 10, 20),
                        endDate = LocalDate.of(2022, 10, 20),
                    ),
                    schedulingUnit = listOf(
                        TitaniaStampedUnitRequest(
                            code = "from titania",
                            person = listOf(
                                TitaniaStampedPersonRequest(
                                    employeeId = "00177111",
                                )
                            )
                        )
                    )
                )
            )
        }

        assertThat(response).isEqualTo(
            GetStampedWorkingTimeEventsResponse(
                schedulingUnit = listOf(
                    TitaniaStampedUnitResponse(
                        code = "from titania",
                        person = listOf(
                            TitaniaStampedPersonResponse(
                                employeeId = "00177111",
                                name = "ANKKA IINES",
                                stampedWorkingTimeEvents = TitaniaStampedWorkingTimeEvents(
                                    event = listOf(
                                        TitaniaStampedWorkingTimeEvent(
                                            date = LocalDate.of(2022, 10, 20),
                                            beginTime = LocalTime.of(7, 54),
                                            beginReasonCode = null,
                                            endTime = LocalTime.of(16, 6),
                                            endReasonCode = null,
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    }

}
