// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.titania

import fi.espoo.evaka.attendance.RawAttendance
import fi.espoo.evaka.attendance.StaffAttendanceType
import fi.espoo.evaka.shared.domain.FiniteDateRange
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import java.time.LocalTime

internal fun splitOvernight(attendance: RawAttendance): Iterable<RawAttendance> {
    if (attendance.departed == null) {
        return listOf(attendance)
    }
    val arrivedDate = attendance.arrived.toLocalDate()
    val departedDate = attendance.departed!!.toLocalDate()
    if (arrivedDate == departedDate) {
        return listOf(attendance)
    }
    return FiniteDateRange(arrivedDate, departedDate).dates().map { date ->
        attendance.copy(
            arrived = if (date == arrivedDate) attendance.arrived else HelsinkiDateTime.of(date, LocalTime.MIN),
            departed = if (date == departedDate) attendance.departed else HelsinkiDateTime.of(date, LocalTime.MAX)
        )
    }.toList()
}

/**
 * Returns type as Titania reason code.
 *
 * From Titania_WS_Sanomakuvaus_HaeLeimaustiedot.pdf v1.1
 */
fun StaffAttendanceType.asTitaniaReasonCode(): String? = when (this) {
    StaffAttendanceType.PRESENT -> null
    StaffAttendanceType.OTHER_WORK -> "TA"
    StaffAttendanceType.TRAINING -> "KO"
    StaffAttendanceType.OVERTIME -> "YT"
    StaffAttendanceType.JUSTIFIED_CHANGE -> "PM"
}

fun staffAttendanceTypeFromTitaniaEventCode(code: String): StaffAttendanceType = when (code) {
    "U" -> StaffAttendanceType.PRESENT
    "K" -> StaffAttendanceType.TRAINING
    else -> StaffAttendanceType.PRESENT
}
