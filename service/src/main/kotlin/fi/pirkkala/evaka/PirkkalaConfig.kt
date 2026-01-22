// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.pirkkala.evaka

import fi.espoo.evaka.EvakaEnv
import fi.espoo.evaka.ScheduledJobsEnv
import fi.espoo.evaka.document.archival.ArchivalIntegrationClient
import fi.espoo.evaka.espoo.DefaultPasswordSpecification
import fi.espoo.evaka.invoicing.domain.PaymentIntegrationClient
import fi.espoo.evaka.mealintegration.MealTypeMapper
import fi.espoo.evaka.shared.ArchiveProcessConfig
import fi.espoo.evaka.shared.ArchiveProcessType
import fi.espoo.evaka.shared.FeatureConfig
import fi.espoo.evaka.shared.auth.PasswordConstraints
import fi.espoo.evaka.shared.auth.PasswordSpecification
import fi.espoo.evaka.shared.security.actionrule.ActionRuleMapping
import fi.espoo.evaka.shared.sftp.SftpClient
import fi.espoo.evaka.titania.TitaniaEmployeeIdConverter
import fi.pirkkala.evaka.mealintegration.PirkkalaMealTypeMapper
import fi.pirkkala.evaka.security.PirkkalaActionRuleMapping
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import trevaka.archival.tweb.RegionalTwebArchivalClient
import trevaka.security.TrevakaActionRuleMapping
import trevaka.titania.PrefixTitaniaEmployeeIdConverter
import trevaka.tomcat.tomcatAccessLoggingCustomizer
import java.time.MonthDay

@Configuration
class PirkkalaConfig {

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
        postOffice = "PIRKKALA",
        municipalMessageAccountName = "Pirkkalan kunta",
        serviceWorkerMessageAccountName = "Varhaiskasvatuksen asiakaspalvelu",
        financeMessageAccountName = "Pirkkalan varhaiskasvatuksen asiakasmaksut",
        applyPlacementUnitFromDecision = true,
        preferredStartRelativeApplicationDueDate = true,
        fiveYearsOldDaycareEnabled = false,
        archiveMetadataOrganization = "Pirkkalan kunta, varhaiskasvatus",
        archiveMetadataConfigs = { type, year ->
            when (type) {
                ArchiveProcessType.APPLICATION_DAYCARE ->
                    ArchiveProcessConfig(
                        processDefinitionNumber = "4801.101",
                        archiveDurationMonths = 12 * 12,
                    )

                ArchiveProcessType.APPLICATION_PRESCHOOL -> null

                ArchiveProcessType.APPLICATION_CLUB ->
                    ArchiveProcessConfig(
                        processDefinitionNumber = "4801.23",
                        archiveDurationMonths = 12 * 12,
                    )

                ArchiveProcessType.FEE_DECISION -> ArchiveProcessConfig(
                    processDefinitionNumber = "4801.102",
                    archiveDurationMonths = 15 * 12,
                )

                ArchiveProcessType.VOUCHER_VALUE_DECISION -> ArchiveProcessConfig(
                    processDefinitionNumber = "4803.03",
                    archiveDurationMonths = 15 * 12,
                )
            }
        },
        daycarePlacementPlanEndMonthDay = MonthDay.of(8, 15),
    )

    @Bean
    fun pirkkalaScheduledJobEnv(env: Environment): ScheduledJobsEnv<PirkkalaScheduledJob> = ScheduledJobsEnv.fromEnvironment(
        PirkkalaScheduledJob.entries.associateWith { it.defaultSettings },
        "pirkkala.job",
        env,
    )

    @Bean
    fun paymentIntegrationClient(): PaymentIntegrationClient = PaymentIntegrationClient.FailingClient()

    @Bean
    fun actionRuleMapping(): ActionRuleMapping = PirkkalaActionRuleMapping(TrevakaActionRuleMapping())

    @Bean
    fun titaniaEmployeeIdConverter(): TitaniaEmployeeIdConverter = PrefixTitaniaEmployeeIdConverter("pir")

    @Bean
    fun accessLoggingCustomizer(env: Environment) = tomcatAccessLoggingCustomizer(env)

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

    @Bean
    fun archivalIntegrationClient(evakaEnv: EvakaEnv, properties: PirkkalaProperties): ArchivalIntegrationClient = if (evakaEnv.archivalEnabled && properties.archival != null) {
        RegionalTwebArchivalClient(SftpClient(properties.archival.sftp.toSftpEnv()), properties.archival)
    } else {
        ArchivalIntegrationClient.FailingClient()
    }
}
