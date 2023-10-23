// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.titania

import fi.espoo.evaka.attendance.RawAttendance
import fi.espoo.evaka.pis.NewEmployee
import fi.espoo.evaka.shared.EmployeeId
import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.domain.FiniteDateRange

fun Database.Read.getEmployeeIdsByNumbers(employeeNumbers: List<String>): Map<String, EmployeeId> {
    val sql = """
        SELECT id, employee_number
        FROM employee
        WHERE employee_number = ANY (:employeeNumbers)
    """.trimIndent()
    return createQuery(sql)
        .bind("employeeNumbers", employeeNumbers.toTypedArray())
        .toMap { columnPair("employee_number", "id") }
}

fun Database.Transaction.createEmployees(employees: List<NewEmployee>): Map<String, EmployeeId> {
    val sql = """
        INSERT INTO employee (first_name, last_name, email, external_id, employee_number, roles, active)
        VALUES (:employee.firstName, :employee.lastName, :employee.email, :employee.externalId, :employee.employeeNumber, :employee.roles::user_role[], :employee.active)
        RETURNING id, employee_number
    """.trimIndent()
    val batch = prepareBatch(sql)
    employees.forEach { employee -> batch.bindKotlin("employee", employee).add() }
    return batch.executeAndReturn()
        .toMap { columnPair("employee_number", "id") }
}

fun Database.Read.getEmployeeIdsByNumbersMapById(employeeNumbers: Collection<String>): Map<EmployeeId, String> {
    val sql = """
        SELECT id, employee_number
        FROM employee
        WHERE employee_number = ANY (:employeeNumbers)
    """.trimIndent()
    return createQuery(sql)
        .bind("employeeNumbers", employeeNumbers.toTypedArray())
        .toMap { columnPair("id", "employee_number") }
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
        .bind("employeeIds", employeeIds?.toTypedArray())
        .bind("period", period)
        .mapTo<StaffAttendancePlan>()
        .toList()
}

fun Database.Transaction.insertStaffAttendancePlans(plans: List<StaffAttendancePlan>): IntArray {
    if (plans.isEmpty()) {
        return IntArray(0)
    }
    val sql = """
        INSERT INTO staff_attendance_plan (employee_id, type, start_time, end_time, description)
        VALUES (:employeeId, :type, :startTime, :endTime, :description)
    """.trimIndent()
    val batch = prepareBatch(sql)
    plans.forEach { plan -> batch.bindKotlin(plan).add() }
    return batch.execute()
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
        .bind("employeeIds", employeeIds?.toTypedArray())
        .bind("period", period)
        .executeAndReturnGeneratedKeys()
        .mapTo<StaffAttendancePlan>()
        .toList()
}

fun Database.Read.findStaffAttendancesBy(
    employeeIds: Collection<EmployeeId>? = null,
    period: FiniteDateRange? = null,
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
LEFT JOIN daycare_group dg on sa.group_id = dg.id
JOIN employee emp ON sa.employee_id = emp.id
LEFT JOIN staff_occupancy_coefficient soc ON soc.daycare_id = dg.daycare_id AND soc.employee_id = emp.id
WHERE (:employeeIds::uuid[] IS NULL OR sa.employee_id = ANY(:employeeIds))
AND (:start IS NULL OR :end IS NULL OR daterange((sa.arrived at time zone 'Europe/Helsinki')::date, (sa.departed at time zone 'Europe/Helsinki')::date, '[]') && daterange(:start, :end, '[]'))
        """.trimIndent(),
    )
        .bind("employeeIds", employeeIds?.toTypedArray())
        .bind("start", period?.start)
        .bind("end", period?.end)
        .mapTo<RawAttendance>()
        .toList()
