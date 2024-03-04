// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka.titania

import fi.espoo.evaka.titania.TitaniaEmployeeIdConverter

class PrefixTitaniaEmployeeIdConverter(private val prefix: String) : TitaniaEmployeeIdConverter {
    override fun fromTitania(employeeId: String): String {
        if (employeeId.isEmpty() || employeeId.startsWith(prefix)) {
            return employeeId
        }
        return "$prefix$employeeId"
    }
}
