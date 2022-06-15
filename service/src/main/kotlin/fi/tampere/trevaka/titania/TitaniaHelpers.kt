// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.titania

import fi.espoo.evaka.attendance.RawAttendance
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
