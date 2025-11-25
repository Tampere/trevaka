// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.kangasala.evaka

import fi.espoo.evaka.EvakaEnv
import fi.espoo.evaka.application.ApplicationStatus
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
import fi.espoo.evaka.shared.sftp.SftpClient
import fi.espoo.evaka.titania.TitaniaEmployeeIdConverter
import fi.kangasala.evaka.mealintegration.KangasalaMealTypeMapper
import fi.kangasala.evaka.security.KangasalaActionRuleMapping
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import trevaka.archival.tweb.RegionalTwebArchivalClient
import trevaka.security.TrevakaActionRuleMapping
import trevaka.titania.PrefixTitaniaEmployeeIdConverter
import trevaka.tomcat.tomcatAccessLoggingCustomizer
import java.time.MonthDay

@Configuration
class KangasalaConfig {

    @Bean
    fun featureConfig() = FeatureConfig(
        valueDecisionCapacityFactorEnabled = true,
        citizenReservationThresholdHours = 7 * 24 - 9, // Mon 09:00
        dailyFeeDivisorOperationalDaysOverride = null,
        freeSickLeaveOnContractDays = true,
        freeAbsenceGivesADailyRefund = true,
        alwaysUseDaycareFinanceDecisionHandler = true,
        paymentNumberSeriesStart = null,
        unplannedAbsencesAreContractSurplusDays = true,
        maxContractDaySurplusThreshold = null,
        useContractDaysAsDailyFeeDivisor = true,
        assistanceDecisionMakerRoles = setOf(UserRole.DIRECTOR, UserRole.UNIT_SUPERVISOR),
        preschoolAssistanceDecisionMakerRoles = setOf(UserRole.DIRECTOR),
        requestedStartUpperLimit = 14,
        postOffice = "KANGASALA",
        municipalMessageAccountName = "Kangasalan kaupunki",
        serviceWorkerMessageAccountName = "Varhaiskasvatuksen ja esiopetuksen asiakaspalvelu",
        financeMessageAccountName = "Kangasalan varhaiskasvatuksen asiakasmaksut",
        applyPlacementUnitFromDecision = true,
        preferredStartRelativeApplicationDueDate = true,
        fiveYearsOldDaycareEnabled = false,
        archiveMetadataOrganization = "Kangasalan kaupunki, varhaiskasvatus",
        archiveMetadataConfigs = { type, year ->
            when (type) {
                ArchiveProcessType.APPLICATION_DAYCARE ->
                    ArchiveProcessConfig(
                        processDefinitionNumber = "46.00.63",
                        archiveDurationMonths = 120 * 12,
                    )

                ArchiveProcessType.APPLICATION_PRESCHOOL ->
                    ArchiveProcessConfig(
                        processDefinitionNumber = "41.01.05",
                        archiveDurationMonths = 120 * 12,
                    )

                ArchiveProcessType.APPLICATION_CLUB -> null

                ArchiveProcessType.ASSISTANCE_NEED_DECISION_DAYCARE ->
                    ArchiveProcessConfig(
                        processDefinitionNumber = "46.00.65",
                        archiveDurationMonths = 120 * 12,
                    )

                ArchiveProcessType.ASSISTANCE_NEED_DECISION_PRESCHOOL ->
                    ArchiveProcessConfig(
                        processDefinitionNumber = "41.04.05",
                        archiveDurationMonths = 120 * 12,
                    )

                ArchiveProcessType.FEE_DECISION -> ArchiveProcessConfig(
                    processDefinitionNumber = "46.00.64",
                    archiveDurationMonths = 10 * 12,
                )

                ArchiveProcessType.VOUCHER_VALUE_DECISION -> ArchiveProcessConfig(
                    processDefinitionNumber = "46.02.07",
                    archiveDurationMonths = 10 * 12,
                )
            }
        },
        daycarePlacementPlanEndMonthDay = MonthDay.of(8, 15),
        placementToolApplicationStatus = ApplicationStatus.WAITING_DECISION,
    )

    @Bean
    fun paymentIntegrationClient(): PaymentIntegrationClient = PaymentIntegrationClient.FailingClient()

    @Bean
    fun actionRuleMapping(): ActionRuleMapping = KangasalaActionRuleMapping(TrevakaActionRuleMapping())

    @Bean
    fun titaniaEmployeeIdConverter(): TitaniaEmployeeIdConverter = PrefixTitaniaEmployeeIdConverter("kan")

    @Bean
    fun accessLoggingCustomizer(env: Environment) = tomcatAccessLoggingCustomizer(env)

    @Bean
    fun ipaasProperties(properties: KangasalaProperties) = properties.ipaas

    @Bean
    fun mealTypeMapper(): MealTypeMapper = KangasalaMealTypeMapper

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
    fun archivalIntegrationClient(evakaEnv: EvakaEnv, properties: KangasalaProperties): ArchivalIntegrationClient = if (evakaEnv.archivalEnabled && properties.archival != null) {
        RegionalTwebArchivalClient(SftpClient(properties.archival.sftp.toSftpEnv()), properties.archival)
    } else {
        ArchivalIntegrationClient.FailingClient()
    }
}
