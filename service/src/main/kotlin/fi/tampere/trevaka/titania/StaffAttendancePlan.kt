// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.titania

import fi.espoo.evaka.attendance.StaffAttendanceType
import fi.espoo.evaka.shared.EmployeeId
import fi.espoo.evaka.shared.domain.HelsinkiDateTime

data class StaffAttendancePlan(
    val employeeId: EmployeeId,
    val type: StaffAttendanceType,
    val startTime: HelsinkiDateTime,
    val endTime: HelsinkiDateTime,
    val description: String?,
)
