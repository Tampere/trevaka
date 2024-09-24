// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.lempaala.evaka

import fi.espoo.evaka.invoicing.domain.PaymentIntegrationClient
import fi.espoo.evaka.mealintegration.MealTypeMapper
import fi.espoo.evaka.shared.FeatureConfig
import fi.espoo.evaka.shared.auth.UserRole
import fi.espoo.evaka.shared.security.actionrule.ActionRuleMapping
import fi.espoo.evaka.titania.TitaniaEmployeeIdConverter
import fi.lempaala.evaka.mealintegration.LempaalaMealTypeMapper
import fi.lempaala.evaka.security.LempaalaActionRuleMapping
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
class LempaalaConfig {

    @Bean
    fun featureConfig() = FeatureConfig(
        valueDecisionCapacityFactorEnabled = true,
        citizenReservationThresholdHours = (7 + 3) * 24 - 9, // Fri 09:00 (1 week + 3 days before)
        dailyFeeDivisorOperationalDaysOverride = 20,
        freeSickLeaveOnContractDays = true,
        freeAbsenceGivesADailyRefund = false,
        alwaysUseDaycareFinanceDecisionHandler = true,
        invoiceNumberSeriesStart = 5400042258,
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
        archiveMetadataOrganization = "Lempäälän kunnan varhaiskasvatus",
        archiveMetadataConfigs = emptyMap(),
        daycarePlacementPlanEndMonthDay = MonthDay.of(8, 15),
    )

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
    fun basicAuthCustomizer(properties: LempaalaProperties) = dvvModificationRequestCustomizer(properties.ipaas)

    @Bean
    fun mealTypeMapper(): MealTypeMapper = LempaalaMealTypeMapper
}
