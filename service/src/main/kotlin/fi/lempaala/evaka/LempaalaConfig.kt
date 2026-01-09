// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.lempaala.evaka

import fi.espoo.evaka.document.archival.ArchivalIntegrationClient
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
import fi.lempaala.evaka.mealintegration.LempaalaMealTypeMapper
import fi.lempaala.evaka.security.LempaalaActionRuleMapping
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import trevaka.security.TrevakaActionRuleMapping
import trevaka.titania.PrefixTitaniaEmployeeIdConverter
import trevaka.tomcat.tomcatAccessLoggingCustomizer
import java.time.MonthDay

@Configuration
class LempaalaConfig {

    @Bean
    fun featureConfig() = FeatureConfig(
        valueDecisionCapacityFactorEnabled = true,
        citizenReservationThresholdHours = (7 + 3) * 24 - 9, // Fri 09:00 (1 week + 3 days before)
        freeAbsenceGivesADailyRefund = true,
        alwaysUseDaycareFinanceDecisionHandler = true,
        paymentNumberSeriesStart = null,
        unplannedAbsencesAreContractSurplusDays = true,
        maxContractDaySurplusThreshold = null,
        useContractDaysAsDailyFeeDivisor = true,
        assistanceDecisionMakerRoles = setOf(UserRole.DIRECTOR, UserRole.UNIT_SUPERVISOR),
        preschoolAssistanceDecisionMakerRoles = setOf(UserRole.DIRECTOR),
        requestedStartUpperLimit = 14,
        postOffice = "LEMPÄÄLÄ",
        municipalMessageAccountName = "Lempäälän kunta",
        serviceWorkerMessageAccountName = "Varhaiskasvatuksen asiakaspalvelu",
        financeMessageAccountName = "Lempäälän varhaiskasvatuksen asiakasmaksut",
        applyPlacementUnitFromDecision = true,
        preferredStartRelativeApplicationDueDate = true,
        fiveYearsOldDaycareEnabled = false,
        archiveMetadataOrganization = "Lempäälän kunta, varhaiskasvatus",
        archiveMetadataConfigs = { type, year ->
            when (type) {
                ArchiveProcessType.APPLICATION_DAYCARE ->
                    ArchiveProcessConfig(
                        processDefinitionNumber = "12.07.01.00",
                        archiveDurationMonths = 10 * 12,
                    )

                ArchiveProcessType.APPLICATION_PRESCHOOL -> null

                ArchiveProcessType.APPLICATION_CLUB -> null

                ArchiveProcessType.ASSISTANCE_NEED_DECISION_DAYCARE ->
                    ArchiveProcessConfig(
                        processDefinitionNumber = "12.07.01.03",
                        archiveDurationMonths = 10 * 12,
                    )

                ArchiveProcessType.ASSISTANCE_NEED_DECISION_PRESCHOOL -> null

                ArchiveProcessType.FEE_DECISION -> ArchiveProcessConfig(
                    processDefinitionNumber = "12.07.01.01",
                    archiveDurationMonths = 10 * 12,
                )

                ArchiveProcessType.VOUCHER_VALUE_DECISION -> ArchiveProcessConfig(
                    processDefinitionNumber = "02.07.01.01",
                    archiveDurationMonths = 10 * 12,
                )
            }
        },
        daycarePlacementPlanEndMonthDay = MonthDay.of(8, 15),
    )

    @Bean
    fun paymentIntegrationClient(): PaymentIntegrationClient = PaymentIntegrationClient.FailingClient()

    @Bean
    fun actionRuleMapping(): ActionRuleMapping = LempaalaActionRuleMapping(TrevakaActionRuleMapping())

    @Bean
    fun titaniaEmployeeIdConverter(): TitaniaEmployeeIdConverter = PrefixTitaniaEmployeeIdConverter("lem")

    @Bean
    fun accessLoggingCustomizer(env: Environment) = tomcatAccessLoggingCustomizer(env)

    @Bean
    fun mealTypeMapper(): MealTypeMapper = LempaalaMealTypeMapper

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

    @Bean
    fun archivalIntegrationClient(): ArchivalIntegrationClient = ArchivalIntegrationClient.FailingClient()
}
