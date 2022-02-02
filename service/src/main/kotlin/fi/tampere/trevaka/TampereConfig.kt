package fi.tampere.trevaka

import fi.espoo.evaka.invoicing.service.InvoiceGenerator
import fi.espoo.evaka.invoicing.service.InvoiceProductProvider
import fi.espoo.evaka.invoicing.service.NewDraftInvoiceGenerator
import fi.espoo.evaka.shared.FeatureConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TampereConfig {

    @Bean
    fun featureConfig(): FeatureConfig = FeatureConfig(
        valueDecisionCapacityFactorEnabled = true,
        daycareApplicationServiceNeedOptionsEnabled = true,
        citizenReservationThresholdHours = 6 * 24, // Tue 00:00
        dailyFeeDivisorOperationalDaysOverride = 20
    )

    @Bean
    fun invoiceGenerator(productProvider: InvoiceProductProvider, featureConfig: FeatureConfig): InvoiceGenerator =
        InvoiceGenerator(NewDraftInvoiceGenerator(productProvider, featureConfig))

}
