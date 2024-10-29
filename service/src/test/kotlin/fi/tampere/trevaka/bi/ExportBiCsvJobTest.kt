// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.bi

import fi.espoo.evaka.application.ApplicationType
import fi.espoo.evaka.application.persistence.daycare.Adult
import fi.espoo.evaka.application.persistence.daycare.Apply
import fi.espoo.evaka.application.persistence.daycare.CareDetails
import fi.espoo.evaka.application.persistence.daycare.Child
import fi.espoo.evaka.application.persistence.daycare.DaycareFormV0
import fi.espoo.evaka.invoicing.data.upsertFeeDecisions
import fi.espoo.evaka.invoicing.data.upsertValueDecisions
import fi.espoo.evaka.invoicing.domain.ChildWithDateOfBirth
import fi.espoo.evaka.invoicing.domain.FeeDecision
import fi.espoo.evaka.invoicing.domain.FeeDecisionChild
import fi.espoo.evaka.invoicing.domain.FeeDecisionDifference
import fi.espoo.evaka.invoicing.domain.FeeDecisionPlacement
import fi.espoo.evaka.invoicing.domain.FeeDecisionServiceNeed
import fi.espoo.evaka.invoicing.domain.FeeDecisionStatus
import fi.espoo.evaka.invoicing.domain.FeeDecisionType
import fi.espoo.evaka.invoicing.domain.FeeThresholds
import fi.espoo.evaka.invoicing.domain.VoucherValueDecision
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionDifference
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionPlacement
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionServiceNeed
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionStatus
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionType
import fi.espoo.evaka.placement.PlacementType
import fi.espoo.evaka.shared.AreaId
import fi.espoo.evaka.shared.FeeDecisionId
import fi.espoo.evaka.shared.ServiceNeedOptionId
import fi.espoo.evaka.shared.VoucherValueDecisionId
import fi.espoo.evaka.shared.db.QuerySql
import fi.espoo.evaka.shared.dev.DevDaycare
import fi.espoo.evaka.shared.dev.DevDaycareGroup
import fi.espoo.evaka.shared.dev.DevPerson
import fi.espoo.evaka.shared.dev.DevPersonType
import fi.espoo.evaka.shared.dev.DevPlacement
import fi.espoo.evaka.shared.dev.insert
import fi.espoo.evaka.shared.dev.insertTestApplication
import fi.espoo.evaka.shared.domain.DateRange
import fi.espoo.evaka.shared.domain.FiniteDateRange
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.espoo.evaka.shared.domain.MockEvakaClock
import fi.espoo.evaka.shared.security.PilotFeature
import fi.tampere.trevaka.AbstractTampereIntegrationTest
import fi.tampere.trevaka.BiExportProperties
import fi.tampere.trevaka.BucketProperties
import fi.tampere.trevaka.InvoiceProperties
import fi.tampere.trevaka.PaymentProperties
import fi.tampere.trevaka.SummertimeAbsenceProperties
import fi.tampere.trevaka.TampereProperties
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.beans.factory.annotation.Autowired
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.Delete
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.ListObjectsRequest
import software.amazon.awssdk.services.s3.model.ObjectIdentifier
import trevaka.ipaas.IpaasProperties
import trevaka.s3.createBucketsIfNeeded
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.UUID
import java.util.zip.ZipInputStream

class ExportBiCsvJobTest : AbstractTampereIntegrationTest() {
    private val clock =
        MockEvakaClock(HelsinkiDateTime.of(LocalDate.of(2022, 10, 23), LocalTime.of(21, 0)))

    @Autowired private lateinit var s3Client: S3Client

    @Autowired private lateinit var s3AsyncClient: S3AsyncClient

    private lateinit var exportJob: BiExportJob
    private lateinit var properties: TampereProperties

    @BeforeAll
    fun beforeAll() {
        // set up a separate test bucket
        properties =
            TampereProperties(
                IpaasProperties("", ""),
                InvoiceProperties(""),
                PaymentProperties(""),
                SummertimeAbsenceProperties(),
                BucketProperties(
                    export = "trevaka-bi-export-it",
                ),
                BiExportProperties(
                    prefix = "bi",
                ),
            )

        createBucketsIfNeeded(s3Client, properties.bucket.allBuckets())

        val exportClient = FileBiExportS3Client(s3AsyncClient, properties)
        exportJob = BiExportJob(exportClient)
    }

    @AfterAll
    fun cleanUp() {
        clearBucket(properties.bucket.export)
    }

    @BeforeEach
    fun beforeEach() {
        insertCriticalTestData()
    }

    @TestFactory
    fun testBiTableExports() = BiTable.entries.map {
        DynamicTest.dynamicTest("Test '${it.fileName}' export") { sendAndAssertBiTableCsv(it) }
    }

    private fun sendAndAssertBiTableCsv(table: BiTable) {
        val bucket = properties.bucket.export
        val prefix = properties.biExport.prefix
        val fileName =
            "${table.fileName}_${clock.now().toLocalDate().format(DateTimeFormatter.ISO_DATE)}.zip"
        val key = "$prefix/$fileName"

        exportJob.sendBiTable(db, clock, table.fileName, table.query)

        val (data, name) = getZip(bucket, key)

        assertEquals("${table.fileName}.csv", name)
        assertTrue(data.isNotEmpty())
    }

    private fun getZip(bucket: String, key: String): Pair<String, String> {
        val request = GetObjectRequest.builder().bucket(bucket).key(key).build()
        return s3Client.getObject(request).use {
            ZipInputStream(it).use { zip ->
                val entry = zip.nextEntry
                val content = zip.readAllBytes().toString(CSV_CHARSET)
                content to (entry?.name ?: "")
            }
        }
    }

    private fun clearBucket(bucket: String) {
        val objectList =
            s3Client.listObjects(ListObjectsRequest.builder().bucket(bucket).build()).contents()

        if (objectList.size > 0) {
            val objectIdList = objectList.map { ObjectIdentifier.builder().key(it.key()).build() }

            s3Client.deleteObjects(
                DeleteObjectsRequest.builder()
                    .bucket(bucket)
                    .delete(Delete.builder().objects(objectIdList).build())
                    .build(),
            )
        }
    }

    private fun insertCriticalTestData() {
        db.transaction { tx ->
            val areaId =
                tx.createQuery(
                    QuerySql { sql("select id from care_area order by short_name limit 1") },
                )
                    .exactlyOne<AreaId>()

            val snoId =
                tx.createQuery(
                    QuerySql {
                        sql("select id from service_need_option order by name_fi limit 1")
                    },
                )
                    .exactlyOne<ServiceNeedOptionId>()

            val daycareId =
                tx.insert(
                    DevDaycare(
                        areaId = areaId,
                        enabledPilotFeatures = setOf(PilotFeature.MESSAGING, PilotFeature.MOBILE),
                    ),
                )
            tx.insert(DevDaycareGroup(daycareId = daycareId))
            val childId = tx.insert(DevPerson(), DevPersonType.CHILD)
            val guardianId = tx.insert(DevPerson(), DevPersonType.RAW_ROW)
            tx.insert(DevPlacement(childId = childId, unitId = daycareId))

            val testFeeThresholds =
                FeeThresholds(
                    validDuring = DateRange(LocalDate.of(2000, 1, 1), null),
                    maxFee = 28900,
                    minFee = 2700,
                    minIncomeThreshold2 = 210200,
                    minIncomeThreshold3 = 271300,
                    minIncomeThreshold4 = 308000,
                    minIncomeThreshold5 = 344700,
                    minIncomeThreshold6 = 381300,
                    maxIncomeThreshold2 = 479900,
                    maxIncomeThreshold3 = 541000,
                    maxIncomeThreshold4 = 577700,
                    maxIncomeThreshold5 = 614400,
                    maxIncomeThreshold6 = 651000,
                    incomeMultiplier2 = BigDecimal("0.1070"),
                    incomeMultiplier3 = BigDecimal("0.1070"),
                    incomeMultiplier4 = BigDecimal("0.1070"),
                    incomeMultiplier5 = BigDecimal("0.1070"),
                    incomeMultiplier6 = BigDecimal("0.1070"),
                    incomeThresholdIncrease6Plus = 14200,
                    siblingDiscount2 = BigDecimal("0.5000"),
                    siblingDiscount2Plus = BigDecimal("0.8000"),
                    temporaryFee = 2900,
                    temporaryFeePartDay = 1500,
                    temporaryFeeSibling = 1500,
                    temporaryFeeSiblingPartDay = 800,
                )
            tx.insert(testFeeThresholds)

            val applicationId =
                tx.insertTestApplication(
                    type = ApplicationType.DAYCARE,
                    childId = childId,
                    guardianId = guardianId,
                    document = DaycareFormV0(
                        type = ApplicationType.DAYCARE,
                        connectedDaycare = false,
                        urgent = true,
                        careDetails =
                        CareDetails(
                            assistanceNeeded = true,
                        ),
                        extendedCare = true,
                        child = Child(dateOfBirth = null),
                        guardian = Adult(),
                        apply = Apply(preferredUnits = listOf(daycareId)),
                        preferredStartDate = LocalDate.of(2019, 1, 1),
                    ),
                )

            FeeDecisionId(UUID.randomUUID()).also { id ->
                tx.upsertFeeDecisions(
                    listOf(
                        FeeDecision(
                            id = id,
                            children =
                            listOf(
                                FeeDecisionChild(
                                    child =
                                    ChildWithDateOfBirth(
                                        id = childId,
                                        dateOfBirth = LocalDate.of(2020, 1, 1),
                                    ),
                                    placement =
                                    FeeDecisionPlacement(
                                        unitId = daycareId,
                                        type = PlacementType.DAYCARE,
                                    ),
                                    serviceNeed =
                                    FeeDecisionServiceNeed(
                                        optionId = snoId,
                                        feeCoefficient = BigDecimal.ONE,
                                        contractDaysPerMonth = null,
                                        descriptionFi = "",
                                        descriptionSv = "",
                                        missing = false,
                                    ),
                                    baseFee = 10_000,
                                    siblingDiscount = 0,
                                    fee = 10_000,
                                    finalFee = 10_000,
                                    feeAlterations = emptyList(),
                                    childIncome = null,
                                ),
                            ),
                            headOfFamilyId = guardianId,
                            validDuring = FiniteDateRange.ofMonth(2019, Month.JANUARY),
                            status = FeeDecisionStatus.SENT,
                            decisionNumber = 999L,
                            decisionType = FeeDecisionType.NORMAL,
                            partnerId = null,
                            headOfFamilyIncome = null,
                            partnerIncome = null,
                            familySize = 1,
                            feeThresholds = testFeeThresholds.getFeeDecisionThresholds(1),
                            difference = setOf(FeeDecisionDifference.PLACEMENT),
                        ),
                    ),
                )
            }

            VoucherValueDecisionId(UUID.randomUUID()).also { id ->
                tx.upsertValueDecisions(
                    listOf(
                        VoucherValueDecision(
                            id = id,
                            validFrom = LocalDate.of(2022, 1, 1),
                            validTo = LocalDate.of(2022, 2, 1),
                            headOfFamilyId = guardianId,
                            status = VoucherValueDecisionStatus.SENT,
                            decisionNumber = 999L,
                            decisionType = VoucherValueDecisionType.NORMAL,
                            partnerId = null,
                            headOfFamilyIncome = null,
                            partnerIncome = null,
                            childIncome = null,
                            familySize = 1,
                            feeThresholds = testFeeThresholds.getFeeDecisionThresholds(1),
                            child =
                            ChildWithDateOfBirth(
                                id = childId,
                                dateOfBirth = LocalDate.of(2020, 1, 1),
                            ),
                            placement =
                            VoucherValueDecisionPlacement(
                                unitId = daycareId,
                                type = PlacementType.DAYCARE,
                            ),
                            serviceNeed =
                            VoucherValueDecisionServiceNeed(
                                feeCoefficient = BigDecimal.ONE,
                                voucherValueCoefficient = BigDecimal.ONE,
                                feeDescriptionFi = "",
                                feeDescriptionSv = "",
                                voucherValueDescriptionFi = "",
                                voucherValueDescriptionSv = "",
                                missing = false,
                            ),
                            baseCoPayment = 1,
                            siblingDiscount = 0,
                            coPayment = 1,
                            feeAlterations = listOf(),
                            finalCoPayment = 1,
                            baseValue = 1,
                            assistanceNeedCoefficient = BigDecimal.ONE,
                            voucherValue = 1,
                            difference = setOf(VoucherValueDecisionDifference.PLACEMENT),
                        ),
                    ),
                )
            }
        }
    }
}
