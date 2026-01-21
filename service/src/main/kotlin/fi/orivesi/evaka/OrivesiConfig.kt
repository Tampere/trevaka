// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.orivesi.evaka

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
import fi.orivesi.evaka.mealintegration.OrivesiMealTypeMapper
import fi.orivesi.evaka.security.OrivesiActionRuleMapping
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import trevaka.security.TrevakaActionRuleMapping
import trevaka.titania.PrefixTitaniaEmployeeIdConverter
import trevaka.tomcat.tomcatAccessLoggingCustomizer
import java.time.MonthDay

@Configuration
class OrivesiConfig {

    @Bean
    fun featureConfig() = FeatureConfig(
        valueDecisionCapacityFactorEnabled = true,
        citizenReservationThresholdHours = 7 * 24 - 9, // Mon 09:00
        freeAbsenceGivesADailyRefund = true,
        alwaysUseDaycareFinanceDecisionHandler = true,
        paymentNumberSeriesStart = null,
        unplannedAbsencesAreContractSurplusDays = true,
        maxContractDaySurplusThreshold = null,
        useContractDaysAsDailyFeeDivisor = true,
        requestedStartUpperLimit = 14,
        postOffice = "ORIVESI",
        municipalMessageAccountName = "Oriveden kaupunki",
        serviceWorkerMessageAccountName = "Varhaiskasvatuksen asiakaspalvelu",
        financeMessageAccountName = "Oriveden varhaiskasvatuksen asiakasmaksut",
        applyPlacementUnitFromDecision = true,
        preferredStartRelativeApplicationDueDate = true,
        fiveYearsOldDaycareEnabled = false,
        archiveMetadataOrganization = "Oriveden kaupunki, varhaiskasvatus",
        archiveMetadataConfigs = { type, year ->
            when (type) {
                ArchiveProcessType.APPLICATION_DAYCARE ->
                    ArchiveProcessConfig(
                        processDefinitionNumber = "12.07.01",
                        archiveDurationMonths = 10 * 12,
                    )

                ArchiveProcessType.APPLICATION_PRESCHOOL ->
                    ArchiveProcessConfig(
                        processDefinitionNumber = "12.01.00.00",
                        archiveDurationMonths = 10 * 12,
                    )

                ArchiveProcessType.APPLICATION_CLUB ->
                    ArchiveProcessConfig(
                        processDefinitionNumber = "12.01.00.04",
                        archiveDurationMonths = 10 * 12,
                    )

                ArchiveProcessType.FEE_DECISION -> ArchiveProcessConfig(
                    processDefinitionNumber = "12.07.02",
                    archiveDurationMonths = 10 * 12,
                )

                ArchiveProcessType.VOUCHER_VALUE_DECISION -> ArchiveProcessConfig(
                    processDefinitionNumber = "12.07.02",
                    archiveDurationMonths = 10 * 12,
                )
            }
        },
        daycarePlacementPlanEndMonthDay = MonthDay.of(8, 15),
    )

    @Bean
    fun paymentIntegrationClient(): PaymentIntegrationClient = PaymentIntegrationClient.FailingClient()

    @Bean
    fun actionRuleMapping(): ActionRuleMapping = OrivesiActionRuleMapping(TrevakaActionRuleMapping())

    @Bean
    fun titaniaEmployeeIdConverter(): TitaniaEmployeeIdConverter = PrefixTitaniaEmployeeIdConverter("ori")

    @Bean
    fun accessLoggingCustomizer(env: Environment) = tomcatAccessLoggingCustomizer(env)

    @Bean
    fun mealTypeMapper(): MealTypeMapper = OrivesiMealTypeMapper()

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
