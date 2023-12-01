// SPDX-FileCopyrightText: 2021-2023 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka.titania

import fi.espoo.evaka.titania.TitaniaEmployeeIdConverter

class TrimStartTitaniaEmployeeIdConverter : TitaniaEmployeeIdConverter {
    override fun fromTitania(employeeId: String): String = employeeId.trimStart('0')
}
