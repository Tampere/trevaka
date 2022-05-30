// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka

import fi.espoo.evaka.invoicing.service.DefaultInvoiceGenerationLogic
import fi.espoo.evaka.shared.FeatureConfig
import fi.espoo.evaka.shared.security.actionrule.ActionRuleMapping
import fi.tampere.trevaka.invoice.config.TampereInvoiceGeneratorLogicChooser
import fi.tampere.trevaka.security.TampereActionRuleMapping
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TampereConfig {

    @Bean
    fun featureConfig(): FeatureConfig = FeatureConfig(
        valueDecisionCapacityFactorEnabled = true,
        daycareApplicationServiceNeedOptionsEnabled = true,
        citizenReservationThresholdHours = 6 * 24, // Tue 00:00
        dailyFeeDivisorOperationalDaysOverride = 20,
        freeSickLeaveOnContractDays = true,
        alwaysUseDaycareFinanceDecisionHandler = true,
    )

    @Bean
    fun actionRuleMapping(): ActionRuleMapping = TampereActionRuleMapping()
}
