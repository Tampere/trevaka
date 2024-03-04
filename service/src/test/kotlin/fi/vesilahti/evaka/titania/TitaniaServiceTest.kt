// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.vesilahti.evaka.titania

import fi.espoo.evaka.attendance.StaffAttendanceType
import fi.espoo.evaka.shared.EmployeeId
import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.dev.DevEmployee
import fi.espoo.evaka.shared.dev.DevStaffAttendance
import fi.espoo.evaka.shared.dev.insert
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.espoo.evaka.titania.GetStampedWorkingTimeEventsRequest
import fi.espoo.evaka.titania.GetStampedWorkingTimeEventsResponse
import fi.espoo.evaka.titania.TitaniaOccupation
import fi.espoo.evaka.titania.TitaniaPeriod
import fi.espoo.evaka.titania.TitaniaPerson
import fi.espoo.evaka.titania.TitaniaSchedulingUnit
import fi.espoo.evaka.titania.TitaniaService
import fi.espoo.evaka.titania.TitaniaStampedPersonRequest
import fi.espoo.evaka.titania.TitaniaStampedPersonResponse
import fi.espoo.evaka.titania.TitaniaStampedUnitRequest
import fi.espoo.evaka.titania.TitaniaStampedUnitResponse
import fi.espoo.evaka.titania.TitaniaStampedWorkingTimeEvent
import fi.espoo.evaka.titania.TitaniaStampedWorkingTimeEvents
import fi.espoo.evaka.titania.TitaniaWorkingTimeEvent
import fi.espoo.evaka.titania.TitaniaWorkingTimeEvents
import fi.espoo.evaka.titania.UpdateWorkingTimeEventsRequest
import fi.vesilahti.evaka.AbstractVesilahtiIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.time.LocalTime

class TitaniaServiceTest : AbstractVesilahtiIntegrationTest() {

    @Autowired
    private lateinit var titaniaService: TitaniaService

    @Test
    fun `updateWorkingTimeEvents should create employee when employee number is unknown`() {
        val request = newUpdateRequest(
            TitaniaPeriod.from(LocalDate.of(2024, 3, 4)),
            TitaniaPerson(
                employeeId = "1234",
                name = "ESIMERKKI ELLI",
                actualWorkingTimeEvents = TitaniaWorkingTimeEvents(
                    event = listOf(
                        TitaniaWorkingTimeEvent(
                            date = LocalDate.of(2024, 3, 4),
                            beginTime = "0800",
                            endTime = "1600",
                        ),
                    ),
                ),
            ),
        )
        val response = db.transaction { tx -> titaniaService.updateWorkingTimeEvents(tx, request) }
        assertThat(response).returns("OK") { it.updateWorkingTimeEventsResponse.message }

        val employees = db.read { tx -> tx.getDevEmployees() }
        assertThat(employees).extracting({ it.lastName }, { it.firstName }, { it.employeeNumber })
            .containsExactly(Tuple("ESIMERKKI", "ELLI", "ves1234"))
    }

    @Test
    fun `updateWorkingTimeEvents should ignore empty employee number`() {
        val request = newUpdateRequest(
            TitaniaPeriod.from(LocalDate.of(2024, 3, 4)),
            TitaniaPerson(
                employeeId = "",
                name = "ESIMERKKI ELLI",
                actualWorkingTimeEvents = TitaniaWorkingTimeEvents(
                    event = listOf(
                        TitaniaWorkingTimeEvent(
                            date = LocalDate.of(2024, 3, 4),
                            beginTime = "0800",
                            endTime = "1600",
                        ),
                    ),
                ),
            ),
        )
        val response = db.transaction { tx -> titaniaService.updateWorkingTimeEvents(tx, request) }
        assertThat(response).returns("OK") { it.updateWorkingTimeEventsResponse.message }

        val employees = db.read { tx -> tx.getDevEmployees() }
        assertThat(employees).isEmpty()
    }

    @Test
    fun `updateWorkingTimeEvents should find employee without prefix`() {
        val employeeId = db.transaction { tx -> tx.insert(DevEmployee(employeeNumber = "ves1234")) }

        val request = newUpdateRequest(
            TitaniaPeriod.from(LocalDate.of(2024, 3, 4)),
            TitaniaPerson(
                employeeId = "1234",
                name = "ESIMERKKI ELLI",
                actualWorkingTimeEvents = TitaniaWorkingTimeEvents(
                    event = listOf(
                        TitaniaWorkingTimeEvent(
                            date = LocalDate.of(2024, 3, 4),
                            beginTime = "0800",
                            endTime = "1600",
                        ),
                    ),
                ),
            ),
        )
        val response = db.transaction { tx -> titaniaService.updateWorkingTimeEvents(tx, request) }
        assertThat(response).returns("OK") { it.updateWorkingTimeEventsResponse.message }

        val employees = db.read { tx -> tx.getDevEmployees() }
        assertThat(employees).extracting<EmployeeId> { it.id }.containsExactly(employeeId)
    }

    @Test
    fun `updateWorkingTimeEvents should find employee with prefix`() {
        val employeeId = db.transaction { tx -> tx.insert(DevEmployee(employeeNumber = "ves1234")) }

        val request = newUpdateRequest(
            TitaniaPeriod.from(LocalDate.of(2024, 3, 4)),
            TitaniaPerson(
                employeeId = "ves1234",
                name = "ESIMERKKI ELLI",
                actualWorkingTimeEvents = TitaniaWorkingTimeEvents(
                    event = listOf(
                        TitaniaWorkingTimeEvent(
                            date = LocalDate.of(2024, 3, 4),
                            beginTime = "0800",
                            endTime = "1600",
                        ),
                    ),
                ),
            ),
        )
        val response = db.transaction { tx -> titaniaService.updateWorkingTimeEvents(tx, request) }
        assertThat(response).returns("OK") { it.updateWorkingTimeEventsResponse.message }

        val employees = db.read { tx -> tx.getDevEmployees() }
        assertThat(employees).extracting<EmployeeId> { it.id }.containsExactly(employeeId)
    }

    @Test
    fun `getStampedWorkingTimeEvents should return data for employee without prefix`() {
        val date = LocalDate.of(2024, 4, 3)
        db.transaction { tx ->
            val employeeId = tx.insert(DevEmployee(employeeNumber = "ves1234"))
            tx.insert(
                DevStaffAttendance(
                    employeeId = employeeId,
                    arrived = HelsinkiDateTime.of(date = date, LocalTime.of(8, 0)),
                    departed = HelsinkiDateTime.of(date = date, LocalTime.of(16, 0)),
                    type = StaffAttendanceType.TRAINING,
                ),
            )
        }

        val request = newGetRequest(
            TitaniaPeriod.from(date),
            TitaniaStampedPersonRequest(employeeId = "1234"),
        )
        val response = db.transaction { tx -> titaniaService.getStampedWorkingTimeEvents(tx, request) }
        assertThat(response).isEqualTo(
            newGetResponse(
                TitaniaStampedPersonResponse(
                    employeeId = "1234",
                    name = "PERSON TEST",
                    stampedWorkingTimeEvents = TitaniaStampedWorkingTimeEvents(
                        event = listOf(
                            TitaniaStampedWorkingTimeEvent(
                                date = date,
                                beginTime = "0800",
                                beginReasonCode = "KO",
                                endTime = "1600",
                                endReasonCode = null,
                            ),
                        ),
                    ),
                ),
            ),
        )
    }
}

private fun newUpdateRequest(period: TitaniaPeriod, vararg person: TitaniaPerson) = UpdateWorkingTimeEventsRequest(
    period = period,
    schedulingUnit = listOf(
        TitaniaSchedulingUnit(
            code = "x",
            occupation = listOf(
                TitaniaOccupation(
                    code = "y",
                    name = "y",
                    person = person.toList(),
                ),
            ),
        ),
    ),
)

private fun newGetRequest(period: TitaniaPeriod, vararg person: TitaniaStampedPersonRequest) =
    GetStampedWorkingTimeEventsRequest(
        period = period,
        schedulingUnit = listOf(
            TitaniaStampedUnitRequest(
                code = "x",
                person = person.toList(),
            ),
        ),
    )

private fun newGetResponse(vararg person: TitaniaStampedPersonResponse) = GetStampedWorkingTimeEventsResponse(
    schedulingUnit = listOf(
        TitaniaStampedUnitResponse(
            code = "x",
            person = person.toList(),
        ),
    ),
)

private fun Database.Read.getDevEmployees(): List<DevEmployee> =
    createQuery { sql("SELECT * FROM employee") }.toList<DevEmployee>()
