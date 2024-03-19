// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.lempaala.evaka

import fi.espoo.evaka.invoicing.domain.IncomeCoefficient
import fi.espoo.evaka.invoicing.service.IncomeCoefficientMultiplierProvider
import java.math.BigDecimal

class LempaalaIncomeCoefficientMultiplierProvider : IncomeCoefficientMultiplierProvider {
    override fun multiplier(coefficient: IncomeCoefficient): BigDecimal {
        TODO("Not yet implemented")
    }
}
