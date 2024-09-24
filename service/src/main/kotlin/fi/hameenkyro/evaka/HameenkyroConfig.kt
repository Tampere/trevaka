// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.hameenkyro.evaka

import fi.espoo.evaka.invoicing.domain.PaymentIntegrationClient
import fi.espoo.evaka.mealintegration.DefaultMealTypeMapper
import fi.espoo.evaka.mealintegration.MealTypeMapper
import fi.espoo.evaka.shared.FeatureConfig
import fi.espoo.evaka.shared.auth.UserRole
import fi.espoo.evaka.shared.security.actionrule.ActionRuleMapping
import fi.espoo.evaka.titania.TitaniaEmployeeIdConverter
import fi.hameenkyro.evaka.security.HameenkyroActionRuleMapping
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.ws.transport.http.HttpComponents5MessageSender
import trevaka.ipaas.dvvModificationRequestCustomizer
import trevaka.ipaas.newIpaasHttpClient
import trevaka.titania.PrefixTitaniaEmployeeIdConverter
import trevaka.tomcat.tomcatAccessLoggingCustomizer
import java.time.MonthDay

@Configuration
class HameenkyroConfig {

    @Bean
    fun featureConfig() = FeatureConfig(
        valueDecisionCapacityFactorEnabled = true,
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
        postOffice = "HÄMEENKYRÖ",
        municipalMessageAccountName = "Hämeenkyrön kunta",
        serviceWorkerMessageAccountName = "Hämeenkyrön kunnan palveluohjaus",
        applyPlacementUnitFromDecision = true,
        preferredStartRelativeApplicationDueDate = true,
        fiveYearsOldDaycareEnabled = false,
        archiveMetadataOrganization = "Hämeenkyrön kunnan varhaiskasvatus",
        archiveMetadataConfigs = emptyMap(),
        daycarePlacementPlanEndMonthDay = MonthDay.of(8, 15),
    )

    @Bean
    fun paymentIntegrationClient(): PaymentIntegrationClient = PaymentIntegrationClient.FailingClient()

    @Bean
    fun actionRuleMapping(): ActionRuleMapping = HameenkyroActionRuleMapping()

    @Bean
    fun titaniaEmployeeIdConverter(): TitaniaEmployeeIdConverter = PrefixTitaniaEmployeeIdConverter("ham")

    @Bean
    fun accessLoggingCustomizer(env: Environment) = tomcatAccessLoggingCustomizer(env)

    @Bean
    fun webServiceMessageSender(properties: HameenkyroProperties) =
        HttpComponents5MessageSender(newIpaasHttpClient(properties.ipaas))

    @Bean
    fun basicAuthCustomizer(properties: HameenkyroProperties) = dvvModificationRequestCustomizer(properties.ipaas)

    @Bean
    fun mealTypeMapper(): MealTypeMapper = DefaultMealTypeMapper
}
