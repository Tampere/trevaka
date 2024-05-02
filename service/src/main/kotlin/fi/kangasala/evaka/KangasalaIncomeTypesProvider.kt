// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.kangasala.evaka

import fi.espoo.evaka.invoicing.domain.IncomeType
import fi.espoo.evaka.invoicing.service.IncomeTypesProvider

class KangasalaIncomeTypesProvider : IncomeTypesProvider {
    override fun get(): Map<String, IncomeType> {
        return linkedMapOf(
            "MAIN_INCOME" to IncomeType("Palkkatulo", 1, false, false),
            "HOLIDAY_BONUS" to IncomeType("Lomaraha", 1, false, false),
            "PERKS" to IncomeType("Luontaisetu", 1, false, false),
            "DAILY_ALLOWANCE" to IncomeType("Päiväraha", 1, true, false),
            "HOME_CARE_ALLOWANCE" to IncomeType("Kotihoidontuki", 1, false, false),
            "PENSION" to IncomeType("Eläke", 1, false, false),
            "RELATIVE_CARE_SUPPORT" to IncomeType("Omaishoidontuki", 1, false, false),
            "STUDENT_INCOME" to IncomeType("Opiskelijan tulot", 1, false, false),
            "GRANT" to IncomeType("Apuraha", 1, false, false),
            "STARTUP_GRANT" to IncomeType("Starttiraha", 1, true, false),
            "BUSINESS_INCOME" to IncomeType("Yritystoiminnan tulo", 1, false, false),
            "CAPITAL_INCOME" to IncomeType("Pääomatulo", 1, false, false),
            "RENTAL_INCOME" to IncomeType("Vuokratulot", 1, false, false),
            "PAID_ALIMONY" to IncomeType("Maksetut elatusavut", -1, false, false),
            "ALIMONY" to IncomeType("Saadut elatusavut", 1, false, false),
            "OTHER_INCOME" to IncomeType("Muu tulo", 1, true, false),
            "ADJUSTED_DAILY_ALLOWANCE" to IncomeType("Soviteltu päiväraha", 1, true, false),
        )
    }
}
