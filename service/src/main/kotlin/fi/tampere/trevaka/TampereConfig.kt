// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka

import fi.espoo.evaka.BucketEnv
import fi.espoo.evaka.ScheduledJobsEnv
import fi.espoo.evaka.invoicing.domain.PaymentIntegrationClient
import fi.espoo.evaka.mealintegration.DefaultMealTypeMapper
import fi.espoo.evaka.mealintegration.MealTypeMapper
import fi.espoo.evaka.shared.ArchiveProcessConfig
import fi.espoo.evaka.shared.ArchiveProcessType
import fi.espoo.evaka.shared.FeatureConfig
import fi.espoo.evaka.shared.async.AsyncJobRunner
import fi.espoo.evaka.shared.auth.UserRole
import fi.espoo.evaka.shared.security.actionrule.ActionRuleMapping
import fi.espoo.evaka.titania.TitaniaEmployeeIdConverter
import fi.tampere.trevaka.bi.BiExportClient
import fi.tampere.trevaka.bi.BiExportJob
import fi.tampere.trevaka.bi.FileBiExportS3Client
import fi.tampere.trevaka.export.ExportUnitsAclService
import fi.tampere.trevaka.invoice.config.HTTP_CLIENT_INVOICE
import fi.tampere.trevaka.payment.TamperePaymentClient
import fi.tampere.trevaka.security.TampereActionRuleMapping
import io.opentelemetry.api.trace.Tracer
import org.apache.hc.client5.http.classic.HttpClient
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import org.springframework.ws.client.core.WebServiceTemplate
import org.springframework.ws.soap.SoapVersion
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory
import org.springframework.ws.transport.http.HttpComponents5MessageSender
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import trevaka.titania.TrimStartTitaniaEmployeeIdConverter
import trevaka.tomcat.tomcatAccessLoggingCustomizer
import java.time.MonthDay

const val WEB_SERVICE_TEMPLATE_PAYMENT = "webServiceTemplatePayment"
internal val PAYMENT_SOAP_PACKAGES = arrayOf(
    "fi.tampere.messages.ipaas.commontypes.v1",
    "fi.tampere.messages.sapfico.payableaccounting.v05",
    "fi.tampere.services.sapfico.payableaccounting.v1",
)

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
        paymentNumberSeriesStart = 1,
        unplannedAbsencesAreContractSurplusDays = true,
        maxContractDaySurplusThreshold = null,
        useContractDaysAsDailyFeeDivisor = true,
        assistanceDecisionMakerRoles = setOf(UserRole.DIRECTOR, UserRole.UNIT_SUPERVISOR),
        preschoolAssistanceDecisionMakerRoles = setOf(UserRole.DIRECTOR),
        requestedStartUpperLimit = 14,
        postOffice = "TAMPERE",
        municipalMessageAccountName = "Tampereen kaupunki",
        serviceWorkerMessageAccountName = "Tampereen kaupungin palveluohjaus",
        applyPlacementUnitFromDecision = true,
        preferredStartRelativeApplicationDueDate = true,
        fiveYearsOldDaycareEnabled = false,
        temporaryDaycarePartDayAbsenceGivesADailyRefund = false,
        archiveMetadataOrganization = "Tampereen kaupunki, varhaiskasvatus ja esiopetus",
        archiveMetadataConfigs =
        mapOf(
            ArchiveProcessType.APPLICATION_DAYCARE to
                ArchiveProcessConfig(
                    processDefinitionNumber = "12.06.01.17",
                    archiveDurationMonths = 10 * 12,
                ),
            ArchiveProcessType.APPLICATION_PRESCHOOL to
                ArchiveProcessConfig(
                    processDefinitionNumber = "12.06.01.17",
                    archiveDurationMonths = 10 * 12,
                ),
            ArchiveProcessType.APPLICATION_CLUB to
                ArchiveProcessConfig(
                    processDefinitionNumber = "12.06.01.19",
                    archiveDurationMonths = 10 * 12,
                ),
            ArchiveProcessType.ASSISTANCE_NEED_DECISION_DAYCARE to
                ArchiveProcessConfig(
                    processDefinitionNumber = "12.06.01.14",
                    archiveDurationMonths = 120 * 12,
                ),
            ArchiveProcessType.ASSISTANCE_NEED_DECISION_PRESCHOOL to
                ArchiveProcessConfig(
                    processDefinitionNumber = "12.06.01.15",
                    archiveDurationMonths = 120 * 12,
                ),
        ),
        daycarePlacementPlanEndMonthDay = MonthDay.of(8, 15),
    )

    @Bean
    @Profile("production")
    fun productionS3AsyncClient(
        bucketEnv: BucketEnv,
        credentialsProvider: AwsCredentialsProvider,
    ): S3AsyncClient = S3AsyncClient.crtBuilder()
        .credentialsProvider(credentialsProvider)
        .build()

    @Bean
    @Profile("local")
    fun localS3AsyncClient(
        bucketEnv: BucketEnv,
        credentialsProvider: AwsCredentialsProvider,
    ): S3AsyncClient = S3AsyncClient.crtBuilder()
        .region(Region.EU_WEST_1)
        .credentialsProvider(credentialsProvider)
        .build()

    @Bean
    fun fileS3Client(asyncClient: S3AsyncClient, properties: TampereProperties): BiExportClient =
        FileBiExportS3Client(asyncClient, properties)

    @Bean
    fun tampereAsyncJobRunner(
        jdbi: Jdbi,
        tracer: Tracer,
        env: Environment,
    ): AsyncJobRunner<TampereAsyncJob> =
        AsyncJobRunner(TampereAsyncJob::class, listOf(TampereAsyncJob.pool), jdbi, tracer)

    @Bean
    fun tampereBiJob(biExportClient: BiExportClient): BiExportJob = BiExportJob(biExportClient)

    @Bean
    fun paymentIntegrationClient(
        @Qualifier(WEB_SERVICE_TEMPLATE_PAYMENT) webServiceTemplate: WebServiceTemplate,
        properties: TampereProperties,
    ): PaymentIntegrationClient = TamperePaymentClient(webServiceTemplate, properties.payment)

    @Bean(WEB_SERVICE_TEMPLATE_PAYMENT)
    fun webServiceTemplate(@Qualifier(HTTP_CLIENT_INVOICE) httpClient: HttpClient): WebServiceTemplate {
        val messageFactory = SaajSoapMessageFactory().apply {
            setSoapVersion(SoapVersion.SOAP_12)
            afterPropertiesSet()
        }
        val marshaller = Jaxb2Marshaller().apply {
            setPackagesToScan(*PAYMENT_SOAP_PACKAGES)
            afterPropertiesSet()
        }
        return WebServiceTemplate(messageFactory).apply {
            this.marshaller = marshaller
            unmarshaller = marshaller
            setMessageSender(HttpComponents5MessageSender(httpClient))
        }
    }

    @Bean fun actionRuleMapping(): ActionRuleMapping = TampereActionRuleMapping()

    @Bean
    fun titaniaEmployeeIdConverter(): TitaniaEmployeeIdConverter =
        TrimStartTitaniaEmployeeIdConverter()

    @Bean fun accessLoggingCustomizer(env: Environment) = tomcatAccessLoggingCustomizer(env)

    @Bean
    fun mealTypeMapper(): MealTypeMapper = DefaultMealTypeMapper

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
        env: ScheduledJobsEnv<TampereScheduledJob>,
    ): TampereScheduledJobs = TampereScheduledJobs(exportUnitsAclService, tampereRunner, env)
}
