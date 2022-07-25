// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.titania

import fi.espoo.evaka.attendance.RawAttendance
import fi.espoo.evaka.shared.EmployeeId
import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.db.bindNullable
import fi.espoo.evaka.shared.domain.FiniteDateRange
import org.jdbi.v3.core.generic.GenericType
import org.jdbi.v3.core.kotlin.mapTo

fun Database.Read.getEmployeeIdsByNumbers(employeeNumbers: List<String>): Map<String, EmployeeId> {
    val sql = """
        SELECT id, employee_number
        FROM employee
        WHERE employee_number = ANY (:employeeNumbers)
    """.trimIndent()
    return createQuery(sql)
        .bind("employeeNumbers", employeeNumbers.toTypedArray())
        .setMapKeyColumn("employee_number")
        .setMapValueColumn("id")
        .collectInto(object : GenericType<Map<String, EmployeeId>>() {})
}

fun Database.Read.getEmployeeIdsByNumbersMapById(employeeNumbers: List<String>): Map<EmployeeId, String> {
    val sql = """
        SELECT id, employee_number
        FROM employee
        WHERE employee_number = ANY (:employeeNumbers)
    """.trimIndent()
    return createQuery(sql)
        .bind("employeeNumbers", employeeNumbers.toTypedArray())
        .setMapKeyColumn("id")
        .setMapValueColumn("employee_number")
        .collectInto(object : GenericType<Map<EmployeeId, String>>() {})
}

fun Database.Read.findStaffAttendancePlansBy(
    employeeIds: Collection<EmployeeId>? = null,
    period: FiniteDateRange? = null,
): List<StaffAttendancePlan> {
    val sql = """
        SELECT employee_id, type, start_time, end_time, description
        FROM staff_attendance_plan
        WHERE (:employeeIds::uuid[] IS NULL OR employee_id = ANY (:employeeIds))
        AND (:period::daterange IS NULL OR :period::daterange @> date_trunc('day', start_time at time zone 'Europe/Helsinki')::date)
    """.trimIndent()
    return createQuery(sql)
        .bindNullable("employeeIds", employeeIds?.toTypedArray())
        .bindNullable("period", period)
        .mapTo<StaffAttendancePlan>()
        .list()
}

fun Database.Transaction.insertStaffAttendancePlans(plans: List<StaffAttendancePlan>): List<StaffAttendancePlan> {
    if (plans.isEmpty()) {
        return emptyList()
    }
    val sql = """
        INSERT INTO staff_attendance_plan (employee_id, type, start_time, end_time, description)
        VALUES <values>
        RETURNING employee_id, type, start_time, end_time, description
    """.trimIndent()
    return createUpdate(sql)
        .bindBeanList("values", plans, listOf("employeeId", "type", "startTime", "endTime", "description"))
        .executeAndReturnGeneratedKeys()
        .mapTo<StaffAttendancePlan>()
        .toList()
}

fun Database.Transaction.deleteStaffAttendancePlansBy(
    employeeIds: Collection<EmployeeId>? = null,
    period: FiniteDateRange? = null,
): List<StaffAttendancePlan> {
    val sql = """
        DELETE FROM staff_attendance_plan
        WHERE (:employeeIds::uuid[] IS NULL OR employee_id = ANY (:employeeIds))
        AND (:period::daterange IS NULL OR :period::daterange @> date_trunc('day', start_time at time zone 'Europe/Helsinki')::date)
        RETURNING employee_id, type, start_time, end_time, description
    """.trimIndent()
    return createUpdate(sql)
        .bindNullable("employeeIds", employeeIds?.toTypedArray())
        .bindNullable("period", period)
        .executeAndReturnGeneratedKeys()
        .mapTo<StaffAttendancePlan>()
        .toList()
}

fun Database.Read.findStaffAttendancesBy(
    employeeIds: Collection<EmployeeId>? = null,
    period: FiniteDateRange? = null
): List<RawAttendance> =
    createQuery(
        """
SELECT
    sa.id,
    sa.employee_id,
    sa.arrived,
    sa.departed,
    sa.group_id,
    sa.occupancy_coefficient,
    sa.type,
    emp.first_name,
    emp.last_name,
    soc.coefficient AS currentOccupancyCoefficient
FROM staff_attendance_realtime sa
JOIN daycare_group dg on sa.group_id = dg.id
JOIN employee emp ON sa.employee_id = emp.id
LEFT JOIN staff_occupancy_coefficient soc ON soc.daycare_id = dg.daycare_id AND soc.employee_id = emp.id
WHERE (:employeeIds::uuid[] IS NULL OR sa.employee_id = ANY(:employeeIds))
AND (:start IS NULL OR :end IS NULL OR daterange((sa.arrived at time zone 'Europe/Helsinki')::date, (sa.departed at time zone 'Europe/Helsinki')::date, '[]') && daterange(:start, :end, '[]'))
        """.trimIndent()
    )
        .bindNullable("employeeIds", employeeIds?.toTypedArray())
        .bindNullable("start", period?.start)
        .bindNullable("end", period?.end)
        .mapTo<RawAttendance>()
        .list()
