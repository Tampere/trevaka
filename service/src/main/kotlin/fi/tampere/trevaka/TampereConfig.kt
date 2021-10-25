package fi.tampere.trevaka

import fi.espoo.evaka.shared.FeatureFlags
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TampereConfig {

    @Bean
    fun featureFlags(): FeatureFlags = FeatureFlags.defaults().copy(daycareApplicationServiceNeedOptionsEnabled = true)

}
