// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.pirkkala.evaka

import fi.espoo.evaka.espoo.DefaultPasswordSpecification
import fi.espoo.evaka.invoicing.domain.PaymentIntegrationClient
import fi.espoo.evaka.mealintegration.MealTypeMapper
import fi.espoo.evaka.shared.ArchiveProcessConfig
import fi.espoo.evaka.shared.ArchiveProcessType
import fi.espoo.evaka.shared.FeatureConfig
import fi.espoo.evaka.shared.auth.PasswordConstraints
import fi.espoo.evaka.shared.auth.PasswordSpecification
import fi.espoo.evaka.shared.auth.UserRole
import fi.espoo.evaka.shared.security.actionrule.ActionRuleMapping
import fi.espoo.evaka.titania.TitaniaEmployeeIdConverter
import fi.pirkkala.evaka.mealintegration.PirkkalaMealTypeMapper
import fi.pirkkala.evaka.security.PirkkalaActionRuleMapping
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
class PirkkalaConfig {

    @Bean
    fun featureConfig() = FeatureConfig(
        valueDecisionCapacityFactorEnabled = true,
        citizenReservationThresholdHours = 7 * 24 - 9, // Mon 09:00
        dailyFeeDivisorOperationalDaysOverride = 20,
        freeSickLeaveOnContractDays = true,
        freeAbsenceGivesADailyRefund = false,
        alwaysUseDaycareFinanceDecisionHandler = true,
        paymentNumberSeriesStart = null,
        unplannedAbsencesAreContractSurplusDays = true,
        maxContractDaySurplusThreshold = null,
        useContractDaysAsDailyFeeDivisor = true,
        assistanceDecisionMakerRoles = setOf(UserRole.DIRECTOR, UserRole.UNIT_SUPERVISOR),
        preschoolAssistanceDecisionMakerRoles = setOf(UserRole.DIRECTOR),
        requestedStartUpperLimit = 14,
        postOffice = "PIRKKALA",
        municipalMessageAccountName = "Pirkkalan kunta",
        serviceWorkerMessageAccountName = "Pirkkalan kunnan varhaiskasvatus",
        applyPlacementUnitFromDecision = true,
        preferredStartRelativeApplicationDueDate = true,
        fiveYearsOldDaycareEnabled = false,
        archiveMetadataOrganization = "Pirkkalan kunta, varhaiskasvatus",
        archiveMetadataConfigs =
        mapOf(
            ArchiveProcessType.APPLICATION_DAYCARE to
                ArchiveProcessConfig(
                    processDefinitionNumber = "4801.22",
                    archiveDurationMonths = 12 * 12,
                ),
            ArchiveProcessType.APPLICATION_CLUB to
                ArchiveProcessConfig(
                    processDefinitionNumber = "4801.23",
                    archiveDurationMonths = 12 * 12,
                ),
            ArchiveProcessType.ASSISTANCE_NEED_DECISION_DAYCARE to
                ArchiveProcessConfig(
                    processDefinitionNumber = "4801.03",
                    archiveDurationMonths = 15 * 12,
                ),
            ArchiveProcessType.ASSISTANCE_NEED_DECISION_PRESCHOOL to
                ArchiveProcessConfig(
                    processDefinitionNumber = "4102.03",
                    archiveDurationMonths = 20 * 12,
                ),
        ),
        daycarePlacementPlanEndMonthDay = MonthDay.of(8, 15),
    )

    @Bean
    fun paymentIntegrationClient(): PaymentIntegrationClient = PaymentIntegrationClient.FailingClient()

    @Bean
    fun actionRuleMapping(): ActionRuleMapping = PirkkalaActionRuleMapping()

    @Bean
    fun titaniaEmployeeIdConverter(): TitaniaEmployeeIdConverter = PrefixTitaniaEmployeeIdConverter("pir")

    @Bean
    fun accessLoggingCustomizer(env: Environment) = tomcatAccessLoggingCustomizer(env)

    @Bean
    fun webServiceMessageSender(properties: PirkkalaProperties) = HttpComponents5MessageSender(newIpaasHttpClient(properties.ipaas))

    @Bean
    fun basicAuthCustomizer(properties: PirkkalaProperties) = dvvModificationRequestCustomizer(properties.ipaas)

    @Bean
    fun mealTypeMapper(): MealTypeMapper = PirkkalaMealTypeMapper

    @Bean
    fun passwordSpecification(): PasswordSpecification = DefaultPasswordSpecification(
        PasswordConstraints.UNCONSTRAINED.copy(
            minLength = 10,
            minLowers = 1,
            minUppers = 1,
            minDigits = 1,
            minSymbols = 0,
        ),
    )
}
