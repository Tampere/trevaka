// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.kangasala.evaka

import fi.espoo.evaka.invoicing.domain.IncomeType
import fi.espoo.evaka.invoicing.service.IncomeTypesProvider

class KangasalaIncomeTypesProvider : IncomeTypesProvider {
    override fun get(): Map<String, IncomeType> = linkedMapOf(
        "MAIN_INCOME" to IncomeType("Palkkatulo", 1, false, false),
    )
}
