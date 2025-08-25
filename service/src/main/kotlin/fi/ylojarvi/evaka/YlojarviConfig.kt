// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.ylojarvi.evaka

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
import fi.ylojarvi.evaka.mealintegration.YlojarviMealTypeMapper
import fi.ylojarvi.evaka.security.YlojarviActionRuleMapping
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.ws.transport.http.SimpleHttpComponents5MessageSender
import trevaka.ipaas.dvvModificationRequestCustomizer
import trevaka.ipaas.newIpaasHttpClient
import trevaka.security.TrevakaActionRuleMapping
import trevaka.titania.PrefixTitaniaEmployeeIdConverter
import trevaka.tomcat.tomcatAccessLoggingCustomizer
import java.time.MonthDay

@Configuration
class YlojarviConfig {

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
        preschoolAssistanceDecisionMakerRoles = setOf(UserRole.DIRECTOR, UserRole.UNIT_SUPERVISOR),
        requestedStartUpperLimit = 14,
        postOffice = "YLÖJÄRVI",
        municipalMessageAccountName = "Ylöjärven kaupunki",
        serviceWorkerMessageAccountName = "Varhaiskasvatuksen ja esiopetuksen asiakaspalvelu",
        financeMessageAccountName = "Ylöjärven varhaiskasvatuksen asiakasmaksut",
        applyPlacementUnitFromDecision = true,
        preferredStartRelativeApplicationDueDate = true,
        fiveYearsOldDaycareEnabled = false,
        archiveMetadataOrganization = "Ylöjärven kaupunki, varhaiskasvatus",
        archiveMetadataConfigs = { type, year ->
            when (type) {
                ArchiveProcessType.APPLICATION_DAYCARE ->
                    ArchiveProcessConfig(
                        processDefinitionNumber = "12.07.01",
                        archiveDurationMonths = 10 * 12,
                    )
                ArchiveProcessType.APPLICATION_PRESCHOOL ->
                    ArchiveProcessConfig(
                        processDefinitionNumber = "12.01.00",
                        archiveDurationMonths = 10 * 12,
                    )
                ArchiveProcessType.APPLICATION_CLUB ->
                    ArchiveProcessConfig(
                        processDefinitionNumber = "12.07.01",
                        archiveDurationMonths = 10 * 12,
                    )
                ArchiveProcessType.ASSISTANCE_NEED_DECISION_DAYCARE ->
                    ArchiveProcessConfig(
                        processDefinitionNumber = "12.07.01",
                        archiveDurationMonths = 120 * 12,
                    )
                ArchiveProcessType.ASSISTANCE_NEED_DECISION_PRESCHOOL ->
                    ArchiveProcessConfig(
                        processDefinitionNumber = "12.01.02",
                        archiveDurationMonths = 120 * 12,
                    )
                ArchiveProcessType.FEE_DECISION -> ArchiveProcessConfig(
                    processDefinitionNumber = "12.06.01.23",
                    archiveDurationMonths = 10 * 12,
                )
                ArchiveProcessType.VOUCHER_VALUE_DECISION -> ArchiveProcessConfig(
                    processDefinitionNumber = "12.06.01.23",
                    archiveDurationMonths = 10 * 12,
                )
            }
        },
        daycarePlacementPlanEndMonthDay = MonthDay.of(8, 15),
    )

    @Bean
    fun paymentIntegrationClient(): PaymentIntegrationClient = PaymentIntegrationClient.FailingClient()

    @Bean
    fun actionRuleMapping(): ActionRuleMapping = YlojarviActionRuleMapping(TrevakaActionRuleMapping())

    @Bean
    fun titaniaEmployeeIdConverter(): TitaniaEmployeeIdConverter = PrefixTitaniaEmployeeIdConverter("ylo")

    @Bean
    fun accessLoggingCustomizer(env: Environment) = tomcatAccessLoggingCustomizer(env)

    @Bean
    fun webServiceMessageSender(properties: YlojarviProperties) = SimpleHttpComponents5MessageSender(newIpaasHttpClient(properties.ipaas))

    @Bean
    fun basicAuthCustomizer(properties: YlojarviProperties) = dvvModificationRequestCustomizer(properties.ipaas)

    @Bean
    fun mealTypeMapper(): MealTypeMapper = YlojarviMealTypeMapper

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
