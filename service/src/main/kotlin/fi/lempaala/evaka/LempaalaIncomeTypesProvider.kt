// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.lempaala.evaka

import fi.espoo.evaka.invoicing.domain.IncomeType
import fi.espoo.evaka.invoicing.service.IncomeTypesProvider

class LempaalaIncomeTypesProvider : IncomeTypesProvider {
    override fun get(): Map<String, IncomeType> {
        TODO("Not yet implemented")
    }
}
