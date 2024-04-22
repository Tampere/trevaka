// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.lempaala.evaka

import com.github.kittinunf.fuel.core.FuelManager
import fi.espoo.evaka.invoicing.domain.PaymentIntegrationClient
import fi.espoo.evaka.invoicing.service.DefaultInvoiceGenerationLogic
import fi.espoo.evaka.shared.FeatureConfig
import fi.espoo.evaka.shared.auth.UserRole
import fi.espoo.evaka.shared.security.actionrule.ActionRuleMapping
import fi.espoo.evaka.titania.TitaniaEmployeeIdConverter
import fi.lempaala.evaka.security.LempaalaActionRuleMapping
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.ws.transport.http.HttpComponents5MessageSender
import trevaka.ipaas.dvvModificationRequestCustomizer
import trevaka.ipaas.newIpaasHttpClient
import trevaka.titania.PrefixTitaniaEmployeeIdConverter
import trevaka.tomcat.tomcatAccessLoggingCustomizer

@Configuration
class LempaalaConfig {

    @Bean
    fun featureConfig() = FeatureConfig(
        valueDecisionCapacityFactorEnabled = true,
        citizenReservationThresholdHours = (7 + 3) * 24 - 9, // Fri 09:00 (1 week + 3 days before)
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
        postOffice = "LEMPÄÄLÄ",
        municipalMessageAccountName = "Lempäälän kunnan varhaiskasvatuspalvelut",
        serviceWorkerMessageAccountName = "Lempäälän kunnan varhaiskasvatuksen palveluohjaus",
        applyPlacementUnitFromDecision = true,
        preferredStartRelativeApplicationDueDate = true,
        fiveYearsOldDaycareEnabled = false,
    )

    @Bean
    fun invoiceIntegrationClient() = LempaalaInvoiceIntegrationClient()

    @Bean
    fun invoiceGenerationLogicChooser() = DefaultInvoiceGenerationLogic

    @Bean
    fun incomeTypesProvider() = LempaalaIncomeTypesProvider()

    @Bean
    fun incomeCoefficientMultiplierProvider() = LempaalaIncomeCoefficientMultiplierProvider()

    @Bean
    fun invoiceProductProvider() = LempaalaInvoiceProductProvider()

    @Bean
    fun paymentIntegrationClient(): PaymentIntegrationClient = PaymentIntegrationClient.FailingClient()

    @Bean
    fun actionRuleMapping(): ActionRuleMapping = LempaalaActionRuleMapping()

    @Bean
    fun titaniaEmployeeIdConverter(): TitaniaEmployeeIdConverter = PrefixTitaniaEmployeeIdConverter("lem")

    @Bean
    fun accessLoggingCustomizer(env: Environment) = tomcatAccessLoggingCustomizer(env)

    @Bean
    fun webServiceMessageSender(properties: LempaalaProperties) =
        HttpComponents5MessageSender(newIpaasHttpClient(properties.ipaas))

    @Bean
    fun fuelManager() = FuelManager()

    @Bean
    fun basicAuthCustomizer(properties: LempaalaProperties) = dvvModificationRequestCustomizer(properties.ipaas)
}
