// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.vesilahti.evaka

import com.github.kittinunf.fuel.core.FuelManager
import fi.espoo.evaka.invoicing.domain.PaymentIntegrationClient
import fi.espoo.evaka.invoicing.service.DefaultInvoiceGenerationLogic
import fi.espoo.evaka.shared.FeatureConfig
import fi.espoo.evaka.shared.auth.UserRole
import fi.espoo.evaka.shared.security.actionrule.DefaultActionRuleMapping
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.ws.transport.http.HttpComponents5MessageSender
import trevaka.ipaas.dvvModificationRequestCustomizer
import trevaka.ipaas.newIpaasHttpClient
import trevaka.titania.TrimStartTitaniaEmployeeIdConverter
import trevaka.tomcat.tomcatAccessLoggingCustomizer

@Configuration
class VesilahtiConfig {

    @Bean
    fun invoiceIntegrationClient() = VesilahtiInvoiceIntegrationClient()

    @Bean
    fun invoiceGenerationLogicChooser() = DefaultInvoiceGenerationLogic

    @Bean
    fun messageProvider() = VesilahtiMessageProvider()

    @Bean
    fun emailMessageProvider() = VesilahtiEmailMessageProvider()

    @Bean
    fun templateProvider() = VesilahtiTemplateProvider()

    @Bean
    fun incomeTypesProvider() = VesilahtiIncomeTypesProvider()

    @Bean
    fun incomeCoefficientMultiplierProvider() = VesilahtiIncomeCoefficientMultiplierProvider()

    @Bean
    fun invoiceProductProvider() = VesilahtiInvoiceProductProvider()

    @Bean
    fun titaniaEmployeeIdConverter() = TrimStartTitaniaEmployeeIdConverter()

    @Bean
    fun featureConfig() = featureConfig

    @Bean
    fun accessLoggingCustomizer(env: Environment) = tomcatAccessLoggingCustomizer(env)

    @Bean
    fun actionRuleMapping() = DefaultActionRuleMapping()

    @Bean
    fun paymentIntegrationClient() = PaymentIntegrationClient.FailingClient()

    @Bean
    fun webServiceMessageSender(properties: VesilahtiProperties) = HttpComponents5MessageSender(newIpaasHttpClient(properties.ipaas))

    @Bean
    fun fuelManager() = FuelManager()

    @Bean
    fun basicAuthCustomizer(properties: VesilahtiProperties) = dvvModificationRequestCustomizer(properties.ipaas)
}

private val featureConfig = FeatureConfig(
    valueDecisionCapacityFactorEnabled = true,
    daycareApplicationServiceNeedOptionsEnabled = true,
    citizenReservationThresholdHours = 6 * 24,
    dailyFeeDivisorOperationalDaysOverride = 20,
    freeSickLeaveOnContractDays = true,
    freeAbsenceGivesADailyRefund = false,
    alwaysUseDaycareFinanceDecisionHandler = true,
    invoiceNumberSeriesStart = 1,
    paymentNumberSeriesStart = null,
    unplannedAbsencesAreContractSurplusDays = true,
    maxContractDaySurplusThreshold = null,
    useContractDaysAsDailyFeeDivisor = true,
    enabledChildConsentTypes = emptySet(),
    curriculumDocumentPermissionToShareRequired = false,
    assistanceDecisionMakerRoles = setOf(UserRole.DIRECTOR, UserRole.UNIT_SUPERVISOR),
    preschoolAssistanceDecisionMakerRoles = setOf(UserRole.DIRECTOR),
    requestedStartUpperLimit = 14,
    partialAbsenceThresholdsEnabled = true,
    postOffice = "VESILAHTI",
    municipalMessageAccountName = "Vesilahden kunta",
    serviceWorkerMessageAccountName = "Vesilahden kunnan palveluohjaus",
    applyPlacementUnitFromDecision = true,
    preferredStartRelativeApplicationDueDate = true,
)
