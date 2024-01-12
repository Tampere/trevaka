// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.hameenkyro.evaka

import fi.espoo.evaka.invoicing.domain.IncomeType
import fi.espoo.evaka.invoicing.service.IncomeTypesProvider

class HameenkyroIncomeTypesProvider : IncomeTypesProvider {
    override fun get(): Map<String, IncomeType> {
        TODO("Not yet implemented")
    }
}
