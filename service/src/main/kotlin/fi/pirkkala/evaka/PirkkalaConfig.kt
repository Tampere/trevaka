// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.pirkkala.evaka

import com.github.kittinunf.fuel.core.FuelManager
import fi.espoo.evaka.invoicing.domain.PaymentIntegrationClient
import fi.espoo.evaka.invoicing.service.DefaultInvoiceGenerationLogic
import fi.espoo.evaka.shared.FeatureConfig
import fi.espoo.evaka.shared.auth.UserRole
import fi.espoo.evaka.shared.security.actionrule.ActionRuleMapping
import fi.espoo.evaka.shared.security.actionrule.DefaultActionRuleMapping
import fi.espoo.evaka.titania.TitaniaEmployeeIdConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.ws.transport.http.HttpComponents5MessageSender
import trevaka.ipaas.dvvModificationRequestCustomizer
import trevaka.ipaas.newIpaasHttpClient
import trevaka.titania.PrefixTitaniaEmployeeIdConverter
import trevaka.tomcat.tomcatAccessLoggingCustomizer

@Configuration
class PirkkalaConfig {

    @Bean
    fun featureConfig() = FeatureConfig(
        valueDecisionCapacityFactorEnabled = true,
        daycareApplicationServiceNeedOptionsEnabled = true,
        citizenReservationThresholdHours = 7 * 24 - 9, // Mon 09:00
        dailyFeeDivisorOperationalDaysOverride = 20,
        freeSickLeaveOnContractDays = true,
        freeAbsenceGivesADailyRefund = false,
        alwaysUseDaycareFinanceDecisionHandler = true,
        invoiceNumberSeriesStart = 1,
        paymentNumberSeriesStart = null,
        unplannedAbsencesAreContractSurplusDays = true,
        maxContractDaySurplusThreshold = null,
        useContractDaysAsDailyFeeDivisor = true,
        curriculumDocumentPermissionToShareRequired = false,
        assistanceDecisionMakerRoles = setOf(UserRole.DIRECTOR, UserRole.UNIT_SUPERVISOR),
        preschoolAssistanceDecisionMakerRoles = setOf(UserRole.DIRECTOR),
        requestedStartUpperLimit = 14,
        postOffice = "PIRKKALA",
        municipalMessageAccountName = "Pirkkalan kunta",
        serviceWorkerMessageAccountName = "Pirkkalan kunnan varhaiskasvatus",
        applyPlacementUnitFromDecision = true,
        preferredStartRelativeApplicationDueDate = true,
        fiveYearsOldDaycareEnabled = false,
    )

    @Bean
    fun invoiceIntegrationClient() = PirkkalaInvoiceIntegrationClient()

    @Bean
    fun invoiceGenerationLogicChooser() = DefaultInvoiceGenerationLogic

    @Bean
    fun incomeTypesProvider() = PirkkalaIncomeTypesProvider()

    @Bean
    fun incomeCoefficientMultiplierProvider() = PirkkalaIncomeCoefficientMultiplierProvider()

    @Bean
    fun invoiceProductProvider() = PirkkalaInvoiceProductProvider()

    @Bean
    fun paymentIntegrationClient(): PaymentIntegrationClient = PaymentIntegrationClient.FailingClient()

    @Bean
    fun actionRuleMapping(): ActionRuleMapping = DefaultActionRuleMapping()

    @Bean
    fun titaniaEmployeeIdConverter(): TitaniaEmployeeIdConverter = PrefixTitaniaEmployeeIdConverter("pir")

    @Bean
    fun accessLoggingCustomizer(env: Environment) = tomcatAccessLoggingCustomizer(env)

    @Bean
    fun webServiceMessageSender(properties: PirkkalaProperties) =
        HttpComponents5MessageSender(newIpaasHttpClient(properties.ipaas))

    @Bean
    fun fuelManager() = FuelManager()

    @Bean
    fun basicAuthCustomizer(properties: PirkkalaProperties) = dvvModificationRequestCustomizer(properties.ipaas)
}
