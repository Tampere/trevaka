// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.titania

import fi.espoo.evaka.shared.EmployeeId
import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import mu.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class TitaniaService {

    fun updateWorkingTimeEvents(
        tx: Database.Transaction,
        request: UpdateWorkingTimeEventsRequest
    ): UpdateWorkingTimeEventsResponse {
        logger.debug { "Titania request: $request" }
        val internal = updateWorkingTimeEventsInternal(tx, request)
        logger.debug { "Titania internal response: $internal" }
        val response = UpdateWorkingTimeEventsResponse.ok()
        logger.debug { "Titania response: $response" }
        return response
    }

    internal fun updateWorkingTimeEventsInternal(
        tx: Database.Transaction,
        request: UpdateWorkingTimeEventsRequest
    ): TitaniaUpdateResponse {
        val period = request.period.toDateRange()
        val persons = request.schedulingUnit.flatMap { unit ->
            unit.occupation.flatMap { occupation ->
                occupation.person.map { person -> person.employeeId to person.actualWorkingTimeEvents }
            }
        }
        val employeeNumbers = persons.map { (employeeNumber, _) -> employeeNumber }.distinct()
        val employeeNumberToId = tx.getEmployeeIdsByNumbers(employeeNumbers)
        val newPlans = persons.flatMap { (employeeNumber, events) ->
            events.event
                .filter { event -> event.beginTime != null && event.endTime != null }
                .map { event ->
                    if (!period.includes(event.date)) {
                        throw TitaniaException(
                            TitaniaErrorDetail(
                                errorcode = TitaniaError.EVENT_DATE_OUT_OF_PERIOD,
                                message = "Event date ${event.date} is out of period (${period.start} - ${period.end})"
                            )
                        )
                    }
                    StaffAttendancePlan(
                        employeeNumberToId[employeeNumber] ?: throw TitaniaException(
                            TitaniaErrorDetail(
                                errorcode = TitaniaError.UNKNOWN_EMPLOYEE_NUMBER,
                                message = "Unknown employee number: $employeeNumber"
                            )
                        ),
                        event.code,
                        HelsinkiDateTime.of(event.date, event.beginTime!!),
                        HelsinkiDateTime.of(event.date, event.endTime!!),
                        event.description
                    )
                }
        }

        logger.info { "Removing staff attendance plans for ${employeeNumberToId.size} employees in period $period" }
        val deleted = tx.deleteStaffAttendancePlansBy(
            employeeIds = employeeNumberToId.values,
            period = period,
        )
        logger.info { "Adding ${newPlans.size} new staff attendance plans" }
        val inserted = tx.insertStaffAttendancePlans(newPlans)

        return TitaniaUpdateResponse(deleted, inserted)
    }

    fun getStampedWorkingTimeEvents(
        tx: Database.Read,
        request: GetStampedWorkingTimeEventsRequest
    ): GetStampedWorkingTimeEventsResponse {
        logger.debug { "Titania request: $request" }
        val period = request.period.toDateRange()
        val employeeNumbers = request.schedulingUnit
            .flatMap { unit -> unit.person.map { person -> person.employeeId } }
            .distinct()
        val employeeIdToNumber = tx.getEmployeeIdsByNumbersMapById(employeeNumbers)
        logger.info { "Finding staff attendances for ${employeeIdToNumber.size} employees in period $period" }
        val attendances = tx.findStaffAttendancesBy(
            employeeIds = employeeIdToNumber.keys,
            period = period
        )

        data class EmployeeKey(val id: EmployeeId, val firstName: String, val lastName: String)

        val persons = attendances
            .groupBy { EmployeeKey(it.employeeId, it.firstName, it.lastName) }
            .map { (employee, attendances) ->
                TitaniaStampedPersonResponse(
                    employeeId = employeeIdToNumber[employee.id]!!,
                    name = "${employee.lastName} ${employee.firstName}".uppercase(),
                    stampedWorkingTimeEvents = TitaniaStampedWorkingTimeEvents(
                        event = attendances.flatMap(::splitOvernight).map { attendance ->
                            TitaniaStampedWorkingTimeEvent(
                                date = attendance.arrived.toLocalDate(),
                                beginTime = attendance.arrived.toLocalTime(),
                                beginReasonCode = null, // TODO: reason code
                                endTime = attendance.departed?.toLocalTime(),
                                endReasonCode = null // TODO: reason code
                            )
                        }
                    )
                )
            }

        val response = GetStampedWorkingTimeEventsResponse(schedulingUnit = request.schedulingUnit.map { unit ->
            TitaniaStampedUnitResponse(
                code = unit.code,
                name = unit.name,
                person = persons.filter { unit.person.find { person -> person.employeeId == it.employeeId } != null }
            )
        })
        logger.debug { "Titania response: $response" }
        return response
    }

}

data class TitaniaUpdateResponse(
    val deleted: List<StaffAttendancePlan>,
    val inserted: List<StaffAttendancePlan>,
)
