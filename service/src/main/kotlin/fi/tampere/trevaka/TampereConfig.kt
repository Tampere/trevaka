// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka

import fi.espoo.evaka.BucketEnv
import fi.espoo.evaka.EvakaEnv
import fi.espoo.evaka.ScheduledJobsEnv
import fi.espoo.evaka.invoicing.domain.PaymentIntegrationClient
import fi.espoo.evaka.lookup
import fi.espoo.evaka.shared.FeatureConfig
import fi.espoo.evaka.shared.async.AsyncJobRunner
import fi.espoo.evaka.shared.auth.UserRole
import fi.espoo.evaka.shared.security.actionrule.ActionRuleMapping
import fi.espoo.evaka.titania.TitaniaEmployeeIdConverter
import fi.tampere.trevaka.bi.BiExportClient
import fi.tampere.trevaka.bi.BiExportJob
import fi.tampere.trevaka.bi.S3MockBiExportS3Client
import fi.tampere.trevaka.bi.StreamingBiExportS3Client
import fi.tampere.trevaka.export.ExportUnitsAclService
import fi.tampere.trevaka.security.TampereActionRuleMapping
import io.opentracing.Tracer
import org.jdbi.v3.core.Jdbi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.services.s3.S3AsyncClient
import trevaka.titania.TrimStartTitaniaEmployeeIdConverter
import trevaka.tomcat.tomcatAccessLoggingCustomizer

@Configuration
@Import(TampereAsyncJobRegistration::class)
class TampereConfig {

    @Bean
    fun featureConfig(): FeatureConfig = FeatureConfig(
        valueDecisionCapacityFactorEnabled = true,
        citizenReservationThresholdHours = 6 * 24, // Tue 00:00
        dailyFeeDivisorOperationalDaysOverride = 20,
        freeSickLeaveOnContractDays = true,
        freeAbsenceGivesADailyRefund = false,
        alwaysUseDaycareFinanceDecisionHandler = true,
        invoiceNumberSeriesStart = 5000000000, // previously hardcoded value in use
        paymentNumberSeriesStart = null, // Payments-feature not currently used in Tampere
        unplannedAbsencesAreContractSurplusDays = true,
        maxContractDaySurplusThreshold = null,
        useContractDaysAsDailyFeeDivisor = true,
        curriculumDocumentPermissionToShareRequired = false,
        assistanceDecisionMakerRoles = setOf(UserRole.DIRECTOR, UserRole.UNIT_SUPERVISOR),
        preschoolAssistanceDecisionMakerRoles = setOf(UserRole.DIRECTOR),
        requestedStartUpperLimit = 14,
        postOffice = "TAMPERE",
        municipalMessageAccountName = "Tampereen kaupunki",
        serviceWorkerMessageAccountName = "Tampereen kaupungin palveluohjaus",
        applyPlacementUnitFromDecision = true,
        preferredStartRelativeApplicationDueDate = true,
        fiveYearsOldDaycareEnabled = false,
    )

    @Bean fun tampereEnv(env: Environment) = TampereEnv.fromEnvironment(env)

    @Bean
    @Profile("production")
    fun productionS3AsyncClient(
        evakaEnv: EvakaEnv,
        bucketEnv: BucketEnv,
        credentialsProvider: AwsCredentialsProvider
    ): S3AsyncClient {
        return S3AsyncClient.crtBuilder()
            .region(evakaEnv.awsRegion)
            .credentialsProvider(credentialsProvider)
            .build()
    }

    @Bean
    @Profile("local")
    fun localS3AsyncClient(
        evakaEnv: EvakaEnv,
        bucketEnv: BucketEnv,
        credentialsProvider: AwsCredentialsProvider
    ): S3AsyncClient {
        return S3AsyncClient.crtBuilder()
            .region(evakaEnv.awsRegion)
            .credentialsProvider(credentialsProvider)
            .build()
    }

    @Bean
    @Profile("local")
    fun s3MockBiClient(
        asyncClient: S3AsyncClient,
        properties: TampereProperties,
        env: TampereEnv
    ): BiExportClient = S3MockBiExportS3Client(asyncClient, properties)

    @Bean
    @Profile("production")
    fun streamingBiClient(
        asyncClient: S3AsyncClient,
        properties: TampereProperties,
        env: TampereEnv
    ): BiExportClient = StreamingBiExportS3Client(asyncClient, properties)

    @Bean
    fun tampereAsyncJobRunner(
        jdbi: Jdbi,
        tracer: Tracer,
        env: Environment
    ): AsyncJobRunner<TampereAsyncJob> =
        AsyncJobRunner(TampereAsyncJob::class, listOf(TampereAsyncJob.pool), jdbi, tracer)

    @Bean
    fun tampereBiJob(biExportClient: BiExportClient): BiExportJob = BiExportJob(biExportClient)

    @Bean
    fun paymentIntegrationClient(): PaymentIntegrationClient =
        PaymentIntegrationClient.FailingClient()

    @Bean fun actionRuleMapping(): ActionRuleMapping = TampereActionRuleMapping()

    @Bean
    fun titaniaEmployeeIdConverter(): TitaniaEmployeeIdConverter =
        TrimStartTitaniaEmployeeIdConverter()

    @Bean fun accessLoggingCustomizer(env: Environment) = tomcatAccessLoggingCustomizer(env)

    @Bean
    fun tampereScheduledJobEnv(env: Environment): ScheduledJobsEnv<TampereScheduledJob> =
        ScheduledJobsEnv.fromEnvironment(
            TampereScheduledJob.entries.associateWith { it.defaultSettings },
            "tampere.job",
            env,
        )

    @Bean
    fun tampereScheduledJobs(
        exportUnitsAclService: ExportUnitsAclService,
        tampereRunner: AsyncJobRunner<TampereAsyncJob>,
        env: ScheduledJobsEnv<TampereScheduledJob>
    ): TampereScheduledJobs = TampereScheduledJobs(exportUnitsAclService, tampereRunner, env)
}

data class TampereEnv(val biIntegrationEnabled: Boolean) {
    companion object {
        fun fromEnvironment(env: Environment): TampereEnv =
            TampereEnv(biIntegrationEnabled = env.lookup("tampere.integration.bi.enabled") ?: false)
    }
}
