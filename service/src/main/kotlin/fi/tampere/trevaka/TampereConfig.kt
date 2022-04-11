package fi.tampere.trevaka

import fi.espoo.evaka.shared.FeatureConfig
import fi.espoo.evaka.shared.security.actionrule.ActionRuleMapping
import fi.tampere.trevaka.security.TampereActionRuleMapping
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TampereConfig {

    @Bean
    fun featureConfig(): FeatureConfig = FeatureConfig(
        valueDecisionCapacityFactorEnabled = true,
        valueDecisionAgeCoefficientRoundingEnabled = true,
        daycareApplicationServiceNeedOptionsEnabled = true,
        citizenReservationThresholdHours = 6 * 24, // Tue 00:00
        dailyFeeDivisorOperationalDaysOverride = 20,
        freeSickLeaveOnContractDays = true
    )

    @Bean
    fun actionRuleMapping(): ActionRuleMapping = TampereActionRuleMapping()

}
