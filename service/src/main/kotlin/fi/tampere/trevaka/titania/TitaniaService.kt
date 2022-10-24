// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.titania

import fi.espoo.evaka.attendance.StaffAttendanceType
import fi.espoo.evaka.shared.EmployeeId
import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.Duration

private val logger = KotlinLogging.logger {}

private val MAX_DRIFT: Duration = Duration.ofMinutes(5)

@Service
class TitaniaService(private val idConverter: TitaniaEmployeeIdConverter) {

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
                occupation.person.map { person -> idConverter.fromTitania(person.employeeId) to person.actualWorkingTimeEvents }
            }
        }
        val employeeNumbers = persons.map { (employeeNumber, _) -> employeeNumber }.distinct()
        val employeeNumberToId = tx.getEmployeeIdsByNumbers(employeeNumbers)
        val newPlans = persons.flatMap { (employeeNumber, events) ->
            events.event
                .filter { event -> !IGNORED_EVENT_CODES.contains(event.code) }
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
                        event.code?.let { staffAttendanceTypeFromTitaniaEventCode(it) } ?: StaffAttendanceType.PRESENT,
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
        tx.insertStaffAttendancePlans(newPlans)

        return TitaniaUpdateResponse(deleted, newPlans)
    }

    fun getStampedWorkingTimeEvents(
        tx: Database.Read,
        request: GetStampedWorkingTimeEventsRequest
    ): GetStampedWorkingTimeEventsResponse {
        logger.debug { "Titania request: $request" }
        val period = request.period.toDateRange()
        val employeeNumbers = request.schedulingUnit
            .flatMap { unit -> unit.person.map { person -> idConverter.fromTitania(person.employeeId) } }
            .distinct()
        val employeeIdToNumber = tx.getEmployeeIdsByNumbersMapById(employeeNumbers)
        logger.info { "Finding staff attendances for ${employeeIdToNumber.size} employees in period $period" }
        val attendances = tx.findStaffAttendancesBy(
            employeeIds = employeeIdToNumber.keys,
            period = period
        )
        val plans = tx.findStaffAttendancePlansBy(
            employeeIds = employeeIdToNumber.keys,
            period = period
        ).groupBy { it.employeeId }

        data class EmployeeKey(val id: EmployeeId, val firstName: String, val lastName: String)

        val persons = attendances
            .groupBy { EmployeeKey(it.employeeId, it.firstName, it.lastName) }
            .map { (employee, attendances) ->
                val employeePlans = plans[employee.id]
                TitaniaStampedPersonResponse(
                    employeeId = idConverter.toTitania(employeeIdToNumber[employee.id]!!),
                    name = "${employee.lastName} ${employee.firstName}".uppercase(),
                    stampedWorkingTimeEvents = TitaniaStampedWorkingTimeEvents(
                        event = attendances.flatMap(::splitOvernight).map { attendance ->
                            val arrived = calculateFromPlans(employeePlans, attendance.arrived)
                            val departed = calculateFromPlans(employeePlans, attendance.departed)
                            TitaniaStampedWorkingTimeEvent(
                                date = attendance.arrived.toLocalDate(),
                                beginTime = arrived?.toLocalTime(),
                                beginReasonCode = attendance.type.asTitaniaReasonCode(),
                                endTime = departed?.toLocalTime(),
                                endReasonCode = null,
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

    private fun calculateFromPlans(plans: List<StaffAttendancePlan>?, event: HelsinkiDateTime?): HelsinkiDateTime? {
        if (event == null) {
            return null
        }
        return plans?.firstNotNullOfOrNull { plan ->
            when {
                event.durationSince(plan.startTime).abs() <= MAX_DRIFT -> plan.startTime
                event.durationSince(plan.endTime).abs() <= MAX_DRIFT -> plan.endTime
                else -> null
            }
        } ?: event
    }
}

data class TitaniaUpdateResponse(
    val deleted: List<StaffAttendancePlan>,
    val inserted: List<StaffAttendancePlan>,
)

interface TitaniaEmployeeIdConverter {
    fun fromTitania(employeeId: String): String
    fun toTitania(employeeNumber: String): String

    companion object {
        fun default(): TitaniaEmployeeIdConverter = object : TitaniaEmployeeIdConverter {
            override fun fromTitania(employeeId: String): String  = employeeId
            override fun toTitania(employeeNumber: String) = employeeNumber
        }
    }
}
