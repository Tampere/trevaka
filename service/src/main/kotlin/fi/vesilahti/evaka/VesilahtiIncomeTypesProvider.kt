// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.vesilahti.evaka

import fi.espoo.evaka.invoicing.domain.IncomeType
import fi.espoo.evaka.invoicing.service.IncomeTypesProvider

class VesilahtiIncomeTypesProvider : IncomeTypesProvider {
    override fun get(): Map<String, IncomeType> {
        return linkedMapOf(
            "MAIN_INCOME" to IncomeType("Palkkatulo", 1, true, false),
            "HOLIDAY_BONUS" to IncomeType("Lomaraha", 1, true, false),
            "PERKS" to IncomeType("Luontaisetu", 1, true, false),
            "DAILY_ALLOWANCE" to IncomeType("Päiväraha", 1, true, false),
            "HOME_CARE_ALLOWANCE" to IncomeType("Kotihoidontuki", 1, true, false),
            "PENSION" to IncomeType("Eläke", 1, true, false),
            "RELATIVE_CARE_SUPPORT" to IncomeType("Omaishoidontuki", 1, true, false),
            "STUDENT_INCOME" to IncomeType("Opiskelijan tulot", 1, true, false),
            "GRANT" to IncomeType("Apuraha", 1, true, false),
            "STARTUP_GRANT" to IncomeType("Starttiraha", 1, true, false),
            "BUSINESS_INCOME" to IncomeType("Yritystoiminnan tulo", 1, true, false),
            "CAPITAL_INCOME" to IncomeType("Pääomatulo", 1, true, false),
            "RENTAL_INCOME" to IncomeType("Vuokratulot", 1, true, false),
            "PAID_ALIMONY" to IncomeType("Maksetut elatusavut", -1, true, false),
            "ALIMONY" to IncomeType("Saadut elatusavut", 1, true, false),
            "OTHER_INCOME" to IncomeType("Muu tulo", 1, true, false),
            "ADJUSTED_DAILY_ALLOWANCE" to IncomeType("Soviteltu päiväraha", 1, true, false),
        )
    }
}
