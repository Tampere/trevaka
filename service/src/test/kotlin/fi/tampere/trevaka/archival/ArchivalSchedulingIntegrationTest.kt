// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.archival

import fi.espoo.evaka.application.ApplicationStatus
import fi.espoo.evaka.application.ApplicationType
import fi.espoo.evaka.application.persistence.daycare.Adult
import fi.espoo.evaka.application.persistence.daycare.Apply
import fi.espoo.evaka.application.persistence.daycare.CareDetails
import fi.espoo.evaka.application.persistence.daycare.Child
import fi.espoo.evaka.application.persistence.daycare.DaycareFormV0
import fi.espoo.evaka.decision.DecisionStatus
import fi.espoo.evaka.decision.DecisionType
import fi.espoo.evaka.document.ChildDocumentType
import fi.espoo.evaka.document.DocumentTemplateContent
import fi.espoo.evaka.document.childdocument.ChildDocumentDecisionStatus
import fi.espoo.evaka.document.childdocument.DocumentContent
import fi.espoo.evaka.document.childdocument.DocumentStatus
import fi.espoo.evaka.invoicing.domain.FeeDecisionStatus
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionStatus
import fi.espoo.evaka.placement.PlacementType
import fi.espoo.evaka.shared.AreaId
import fi.espoo.evaka.shared.ChildDocumentId
import fi.espoo.evaka.shared.ChildId
import fi.espoo.evaka.shared.DecisionId
import fi.espoo.evaka.shared.FeeDecisionId
import fi.espoo.evaka.shared.VoucherValueDecisionId
import fi.espoo.evaka.shared.async.AsyncJob
import fi.espoo.evaka.shared.async.AsyncJobRunner
import fi.espoo.evaka.shared.auth.AuthenticatedUser
import fi.espoo.evaka.shared.dev.DevChildDocument
import fi.espoo.evaka.shared.dev.DevChildDocumentDecision
import fi.espoo.evaka.shared.dev.DevDaycare
import fi.espoo.evaka.shared.dev.DevDocumentTemplate
import fi.espoo.evaka.shared.dev.DevEmployee
import fi.espoo.evaka.shared.dev.DevFeeDecision
import fi.espoo.evaka.shared.dev.DevPerson
import fi.espoo.evaka.shared.dev.DevPersonType
import fi.espoo.evaka.shared.dev.DevPlacement
import fi.espoo.evaka.shared.dev.DevVoucherValueDecision
import fi.espoo.evaka.shared.dev.TestDecision
import fi.espoo.evaka.shared.dev.insert
import fi.espoo.evaka.shared.dev.insertTestApplication
import fi.espoo.evaka.shared.dev.insertTestDecision
import fi.espoo.evaka.shared.domain.DateRange
import fi.espoo.evaka.shared.domain.FiniteDateRange
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.espoo.evaka.shared.domain.MockEvakaClock
import fi.espoo.evaka.shared.domain.UiLanguage
import fi.tampere.trevaka.AbstractTampereIntegrationTest
import fi.tampere.trevaka.TampereProperties
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import trevaka.archival.planDocumentArchival
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

class ArchivalSchedulingIntegrationTest : AbstractTampereIntegrationTest() {

    @Autowired
    private lateinit var asyncJobRunner: AsyncJobRunner<AsyncJob>

    @Autowired
    private lateinit var tampereProperties: TampereProperties

    private val now = HelsinkiDateTime.of(LocalDateTime.of(2025, 12, 5, 12, 0))
    private val clock = MockEvakaClock(now)
    private val today = clock.today()
    private val user = AuthenticatedUser.SystemInternalUser
    private val employee = DevEmployee()

    private val guardian = DevPerson(
        dateOfBirth = LocalDate.of(2007, 1, 1),
        ssn = "010107A995B",
        firstName = "Harrier",
        lastName = "Huoltaja",
    )
    private val childId = ChildId(UUID.randomUUID())
    private val emptyContent = DocumentContent(answers = emptyList())
    private val daycare =
        DevDaycare(name = "Test unit", areaId = AreaId(UUID.fromString("6529f5a2-9777-11eb-ba89-cfcda122ed3b")))

    @BeforeEach
    fun setUp() {
        db.transaction { tx ->
            tx.insert(employee)
            tx.insert(daycare)
            val childId = tx.insert(
                DevPerson(id = childId, dateOfBirth = LocalDate.of(2020, 1, 1)),
                DevPersonType.CHILD,
            )

            tx.insert(
                guardian,
                DevPersonType.ADULT,
            )

            tx.insert(
                DevPlacement(
                    type = PlacementType.DAYCARE,
                    childId = childId,
                    unitId = daycare.id,
                    startDate = today,
                    endDate = today.plusYears(1),
                ),
            )
            tx.insert(
                DevPlacement(
                    type = PlacementType.DAYCARE,
                    childId = childId,
                    unitId = daycare.id,
                    startDate = today.plusYears(1).plusDays(1),
                    endDate = today.plusYears(2),
                ),
            )
        }
    }

    @Test
    fun `does not schedule jobs for child document decisions if child still placed`() {
        val schedule = tampereProperties.archival?.schedule!!

        // first placement end + schedule delay
        val earlyArchivalClock = MockEvakaClock(now.plusYears(1).plusDays(schedule.documentDecisionDelayDays))
        val validityEnd = today.plusYears(1)

        insertTemplateAndDocument(validityEnd, archiveExternally = true, ChildDocumentType.OTHER_DECISION)
        insertTemplateAndDocument(validityEnd, archiveExternally = true, ChildDocumentType.OTHER_DECISION, ChildDocumentDecisionStatus.REJECTED)
        val earlyAnnulledDocId = insertTemplateAndDocument(validityEnd, archiveExternally = true, ChildDocumentType.OTHER_DECISION)
        annulDocumentDecision(earlyAnnulledDocId)

        planDocumentArchival(db, earlyArchivalClock, asyncJobRunner, schedule)

        val earlyJobs = getScheduledChildDocumentArchivalJobs()
        assertEquals(0, earlyJobs.size)
    }

    @Test
    fun `schedules archival job for accepted, annulled and rejected child documents decisions after last placement end`() {
        val schedule = tampereProperties.archival?.schedule!!

        // last placement end + schedule delay
        val lateArchivalClock = MockEvakaClock(now.plusYears(2).plusDays(schedule.documentDecisionDelayDays))
        val validityEnd = today.plusYears(2)

        val lateDocId = insertTemplateAndDocument(validityEnd, archiveExternally = true, ChildDocumentType.OTHER_DECISION)
        val lateAnnulledDocId = insertTemplateAndDocument(validityEnd, archiveExternally = true, ChildDocumentType.OTHER_DECISION)
        val lateRejectedDocId = insertTemplateAndDocument(validityEnd, archiveExternally = true, ChildDocumentType.OTHER_DECISION, ChildDocumentDecisionStatus.REJECTED)
        annulDocumentDecision(lateAnnulledDocId)

        planDocumentArchival(db, lateArchivalClock, asyncJobRunner, schedule)

        val lateJobs = getScheduledChildDocumentArchivalJobs().map { it.documentId }
        assertEquals(3, lateJobs.size)
        assertThat(lateJobs).containsExactlyInAnyOrderElementsOf(listOf(lateDocId.toString(), lateAnnulledDocId.toString(), lateRejectedDocId.toString()))
    }

    @Test
    fun `schedules archival job for a sent fee decision well after approval`() {
        val schedule = tampereProperties.archival?.schedule!!

        // add schedule delay to simulated 'now'
        val eligibleArchivalClock = MockEvakaClock(now.plusDays(schedule.feeDecisionDelayDays))

        val feeDecisionId = insertFeeDecision()

        planDocumentArchival(db, eligibleArchivalClock, asyncJobRunner, schedule)

        val jobs = getScheduledFeeDecisionArchivalJobs()
        assertEquals(1, jobs.size)
        assertEquals(feeDecisionId.toString(), jobs.first().documentId)
    }

    @Test
    fun `schedules archival job for a sent voucher value decision well after approval`() {
        val schedule = tampereProperties.archival?.schedule!!

        // add schedule delay to simulated 'now'
        val eligibleArchivalClock = MockEvakaClock(now.plusDays(schedule.feeDecisionDelayDays))

        val voucherValueDecisionId = insertVoucherValueDecision()

        planDocumentArchival(db, eligibleArchivalClock, asyncJobRunner, schedule)

        val jobs = getScheduledVoucherValueDecisionArchivalJobs()
        assertEquals(1, jobs.size)
        assertEquals(voucherValueDecisionId.toString(), jobs.first().documentId)
    }

    @Test
    fun `schedules archival job for a placement decision well after resolution`() {
        val schedule = tampereProperties.archival?.schedule!!

        // add schedule delay to simulated 'now'
        val eligibleArchivalClock = MockEvakaClock(now.plusDays(schedule.decisionDelayDays))

        val decisionId = insertDecisionData()

        planDocumentArchival(db, eligibleArchivalClock, asyncJobRunner, schedule)

        val jobs = getScheduledDecisionArchivalJobs()
        assertEquals(1, jobs.size)
        assertEquals(decisionId.toString(), jobs.first().documentId)
    }

    @Test
    fun `does not schedule job for child document decision when placement end too recent`() {
        val schedule = tampereProperties.archival?.schedule!!

        val validityEnd = today.plusYears(1)

        insertTemplateAndDocument(validityEnd, archiveExternally = true, ChildDocumentType.OTHER_DECISION)

        planDocumentArchival(db, clock, asyncJobRunner, schedule)

        val jobs = getScheduledChildDocumentArchivalJobs()
        assertEquals(0, jobs.size)
    }

    @Test
    fun `does not schedule job for fee decision when approval too recent`() {
        val schedule = tampereProperties.archival?.schedule!!

        insertFeeDecision()

        planDocumentArchival(db, clock, asyncJobRunner, schedule)

        val jobs = getScheduledFeeDecisionArchivalJobs()
        assertEquals(0, jobs.size)
    }

    @Test
    fun `does not schedule job for voucher value decision when approval too recent`() {
        val schedule = tampereProperties.archival?.schedule!!

        insertVoucherValueDecision()

        planDocumentArchival(db, clock, asyncJobRunner, schedule)

        val jobs = getScheduledVoucherValueDecisionArchivalJobs()
        assertEquals(0, jobs.size)
    }

    @Test
    fun `does not schedule archival job for a placement decision too soon after resolution`() {
        val schedule = tampereProperties.archival?.schedule!!

        insertDecisionData()

        planDocumentArchival(db, clock, asyncJobRunner, schedule)

        val jobs = getScheduledDecisionArchivalJobs()
        assertEquals(0, jobs.size)
    }

    @Test
    fun `does not schedule job for child document decision when not marked for archival or document already archived`() {
        val schedule = tampereProperties.archival?.schedule!!

        // placement end + schedule delay
        val eligibleArchivalClock = MockEvakaClock(now.plusYears(1).plusDays(schedule.documentDecisionDelayDays))
        val validityEnd = today.plusYears(1)

        insertTemplateAndDocument(validityEnd, archiveExternally = false, ChildDocumentType.OTHER_DECISION)

        val docId = insertTemplateAndDocument(validityEnd, archiveExternally = true)
        markDocumentArchived(docId)

        planDocumentArchival(db, eligibleArchivalClock, asyncJobRunner, schedule)

        val jobs = getScheduledChildDocumentArchivalJobs()
        assertEquals(0, jobs.size)
    }

    @Test
    fun `does not schedule archival job for a sent fee decision it it's already archived`() {
        val schedule = tampereProperties.archival?.schedule!!

        // add schedule delay to simulated 'now'
        val eligibleArchivalClock = MockEvakaClock(now.plusDays(schedule.feeDecisionDelayDays))

        val feeDecisionId = insertFeeDecision()
        markFeeDecisionArchived(feeDecisionId)

        planDocumentArchival(db, eligibleArchivalClock, asyncJobRunner, schedule)

        val jobs = getScheduledFeeDecisionArchivalJobs()
        assertEquals(0, jobs.size)
    }

    @Test
    fun `does not schedule archival job for a sent voucher value decision if it's already archived`() {
        val schedule = tampereProperties.archival?.schedule!!

        // add schedule delay to simulated 'now'
        val eligibleArchivalClock = MockEvakaClock(now.plusDays(schedule.feeDecisionDelayDays))

        val voucherValueDecisionId = insertVoucherValueDecision()
        markVoucherValueDecisionArchived(voucherValueDecisionId)

        planDocumentArchival(db, eligibleArchivalClock, asyncJobRunner, schedule)

        val jobs = getScheduledVoucherValueDecisionArchivalJobs()
        assertEquals(0, jobs.size)
    }

    @Test
    fun `does not schedule archival job for a placement decision that's already archived`() {
        val schedule = tampereProperties.archival?.schedule!!

        // add schedule delay to simulated 'now'
        val eligibleArchivalClock = MockEvakaClock(now.plusDays(schedule.decisionDelayDays))

        val decisionId = insertDecisionData()
        markDecisionArchived(decisionId)

        planDocumentArchival(db, eligibleArchivalClock, asyncJobRunner, schedule)

        val jobs = getScheduledDecisionArchivalJobs()
        assertEquals(0, jobs.size)
    }

    @Test
    fun `does not schedule archival job for a draft fee decision`() {
        val schedule = tampereProperties.archival?.schedule!!

        // add schedule delay to simulated 'now'
        val eligibleArchivalClock = MockEvakaClock(now.plusDays(schedule.feeDecisionDelayDays))

        insertFeeDecision(status = FeeDecisionStatus.DRAFT)

        planDocumentArchival(db, eligibleArchivalClock, asyncJobRunner, schedule)

        val jobs = getScheduledFeeDecisionArchivalJobs()
        assertEquals(0, jobs.size)
    }

    @Test
    fun `schedules archival job for an annulled fee decision`() {
        val schedule = tampereProperties.archival?.schedule!!

        // add schedule delay to simulated 'now'
        val eligibleArchivalClock = MockEvakaClock(now.plusDays(schedule.feeDecisionDelayDays))

        val decisionId = insertFeeDecision(status = FeeDecisionStatus.ANNULLED)

        planDocumentArchival(db, eligibleArchivalClock, asyncJobRunner, schedule)

        val jobs = getScheduledFeeDecisionArchivalJobs()
        assertEquals(1, jobs.size)
        assertEquals(decisionId.toString(), jobs.first().documentId)
    }

    @Test
    fun `schedules archival job for an annulled voucher value decision`() {
        val schedule = tampereProperties.archival?.schedule!!

        // add schedule delay to simulated 'now'
        val eligibleArchivalClock = MockEvakaClock(now.plusDays(schedule.feeDecisionDelayDays))

        val decisionId = insertVoucherValueDecision(status = VoucherValueDecisionStatus.ANNULLED)

        planDocumentArchival(db, eligibleArchivalClock, asyncJobRunner, schedule)

        val jobs = getScheduledVoucherValueDecisionArchivalJobs()
        assertEquals(1, jobs.size)
        assertEquals(decisionId.toString(), jobs.first().documentId)
    }

    @Test
    fun `does not schedule archival job for a draft voucher value decision`() {
        val schedule = tampereProperties.archival?.schedule!!

        // add schedule delay to simulated 'now'
        val eligibleArchivalClock = MockEvakaClock(now.plusDays(schedule.feeDecisionDelayDays))

        insertVoucherValueDecision(status = VoucherValueDecisionStatus.DRAFT)

        planDocumentArchival(db, eligibleArchivalClock, asyncJobRunner, schedule)

        val jobs = getScheduledVoucherValueDecisionArchivalJobs()
        assertEquals(0, jobs.size)
    }

    @Test
    fun `schedules archival job for a placement decision that is rejected`() {
        val schedule = tampereProperties.archival?.schedule!!

        // add schedule delay to simulated 'now'
        val eligibleArchivalClock = MockEvakaClock(now.plusDays(schedule.decisionDelayDays))

        val decisionId = insertDecisionData(status = DecisionStatus.REJECTED)

        planDocumentArchival(db, eligibleArchivalClock, asyncJobRunner, schedule)

        val jobs = getScheduledDecisionArchivalJobs()
        assertEquals(1, jobs.size)
        assertEquals(decisionId.toString(), jobs.first().documentId)
    }

    @Test
    fun `does not schedule archival job for a placement decision that is pending`() {
        val schedule = tampereProperties.archival?.schedule!!

        // add schedule delay to simulated 'now'
        val eligibleArchivalClock = MockEvakaClock(now.plusDays(schedule.decisionDelayDays))

        insertDecisionData(status = DecisionStatus.PENDING)

        planDocumentArchival(db, eligibleArchivalClock, asyncJobRunner, schedule)

        val jobs = getScheduledDecisionArchivalJobs()
        assertEquals(0, jobs.size)
    }

    @Test
    fun `schedules archival job for all decision types except CLUB`() {
        val schedule = tampereProperties.archival?.schedule!!

        // add schedule delay to simulated 'now'
        val eligibleArchivalClock = MockEvakaClock(now.plusDays(schedule.decisionDelayDays))

        val archivableDecisionIds = listOf(
            DecisionType.DAYCARE,
            DecisionType.PRESCHOOL,
            DecisionType.PRESCHOOL_DAYCARE,
            DecisionType.PRESCHOOL_CLUB,
            DecisionType.PREPARATORY_EDUCATION,
            DecisionType.DAYCARE_PART_TIME,
        ).map { insertDecisionData(type = it) }

        listOf(
            DecisionType.CLUB,
        ).forEach { insertDecisionData(type = it) }

        planDocumentArchival(db, eligibleArchivalClock, asyncJobRunner, schedule)

        val jobDocs = getScheduledDecisionArchivalJobs().map { it.documentId }
        assertEquals(archivableDecisionIds.size, jobDocs.size)
        assertThat(jobDocs).containsExactlyInAnyOrderElementsOf(archivableDecisionIds.map { it.toString() })
    }

    @Test
    fun `schedules archival job for vasu when template old enough`() {
        // Template validity ended long enough ago for archival
        val schedule = tampereProperties.archival?.schedule!!

        val validityEnd = today.minusDays(schedule.documentPlanDelayDays)
        val docId = insertTemplateAndDocument(validityEnd, archiveExternally = true)

        planDocumentArchival(db, clock, asyncJobRunner, schedule)

        val jobs = getScheduledChildDocumentArchivalJobs()
        assertEquals(1, jobs.size)
        assertEquals(docId.toString(), jobs.first().documentId)
    }

    @Test
    fun `does not schedule job for vasu when template too recent, not marked for archival or document already archived`() {
        val schedule = tampereProperties.archival?.schedule!!

        val tooRecent = today.minusDays(schedule.documentPlanDelayDays - 1)
        val oldEnough = today.minusDays(schedule.documentPlanDelayDays)

        insertTemplateAndDocument(tooRecent, archiveExternally = true)
        insertTemplateAndDocument(oldEnough, archiveExternally = false)

        val docId = insertTemplateAndDocument(oldEnough, archiveExternally = true)
        markDocumentArchived(docId)

        planDocumentArchival(db, clock, asyncJobRunner, schedule)

        val jobs = getScheduledChildDocumentArchivalJobs()
        assertEquals(0, jobs.size)
    }

    @Test
    fun `respects archival limit when specified`() {
        val schedule = tampereProperties.archival?.schedule!!.copy(dailyDocumentLimit = 1)
        val oldEnough = today.minusDays(schedule.documentPlanDelayDays)

        insertTemplateAndDocument(oldEnough, archiveExternally = true)
        insertTemplateAndDocument(oldEnough, archiveExternally = true)
        insertTemplateAndDocument(oldEnough, archiveExternally = true)

        // Plan with limit of 1
        planDocumentArchival(db, clock, asyncJobRunner, schedule)

        val jobs = getScheduledChildDocumentArchivalJobs()
        assertEquals(1, jobs.size)
    }

    private fun markDocumentArchived(documentId: ChildDocumentId) {
        db.transaction { tx ->
            tx.execute {
                sql(
                    """
                    UPDATE child_document
                    SET archived_at = ${bind(now)}
                    WHERE id = ${bind(documentId)}
                    """,
                )
            }
        }
    }

    private fun markFeeDecisionArchived(feeDecisionId: FeeDecisionId) {
        db.transaction { tx ->
            tx.execute {
                sql(
                    """
                    UPDATE fee_decision
                    SET archived_at = ${bind(now)}
                    WHERE id = ${bind(feeDecisionId)}
                    """,
                )
            }
        }
    }

    private fun markVoucherValueDecisionArchived(voucherDecisionId: VoucherValueDecisionId) {
        db.transaction { tx ->
            tx.execute {
                sql(
                    """
                    UPDATE voucher_value_decision
                    SET archived_at = ${bind(now)}
                    WHERE id = ${bind(voucherDecisionId)}
                    """,
                )
            }
        }
    }

    private fun markDecisionArchived(decisionId: DecisionId) {
        db.transaction { tx ->
            tx.execute {
                sql(
                    """
                    UPDATE decision
                    SET archived_at = ${bind(now)}
                    WHERE id = ${bind(decisionId)}
                    """,
                )
            }
        }
    }

    private fun annulDocumentDecision(documentId: ChildDocumentId) {
        db.transaction { tx ->
            tx.execute {
                sql(
                    """
                    UPDATE child_document_decision
                    SET status = 'ANNULLED', annulment_reason = 'TEST REASON'
                    WHERE id = (SELECT decision_id from child_document
                        WHERE id = ${bind(documentId)}
                        LIMIT 1)
                    """,
                )
            }
        }
    }

    private fun insertTemplateAndDocument(
        validityEnd: LocalDate,
        archiveExternally: Boolean,
        type: ChildDocumentType = ChildDocumentType.VASU,
        status: ChildDocumentDecisionStatus = ChildDocumentDecisionStatus.ACCEPTED,
    ): ChildDocumentId {
        return db.transaction { tx ->
            val templateId = tx.insert(
                DevDocumentTemplate(
                    name = "Test Template",
                    type = type,
                    language = UiLanguage.FI,
                    validity = DateRange(LocalDate.of(2020, 1, 1), validityEnd),
                    content = DocumentTemplateContent(sections = emptyList()),
                    archiveExternally = archiveExternally,
                    processDefinitionNumber = if (archiveExternally) "12.06.01" else null,
                    archiveDurationMonths = if (archiveExternally) 120 else null,
                    endDecisionWhenUnitChanges = if (type == ChildDocumentType.OTHER_DECISION) true else null,
                ),
            )

            var docId: ChildDocumentId
            if (type != ChildDocumentType.OTHER_DECISION) {
                docId = tx.insert(
                    DevChildDocument(
                        childId = childId,
                        templateId = templateId,
                        status = DocumentStatus.COMPLETED,
                        content = emptyContent,
                        publishedContent = emptyContent,
                        modifiedAt = now,
                        modifiedBy = user.evakaUserId,
                        contentLockedAt = now,
                        contentLockedBy = null,
                        publishedAt = now,
                        publishedBy = user.evakaUserId,
                    ),
                )
            } else {
                docId = tx.insert(
                    DevChildDocument(
                        childId = childId,
                        templateId = templateId,
                        status = DocumentStatus.COMPLETED,
                        content = emptyContent,
                        publishedContent = emptyContent,
                        modifiedAt = now,
                        modifiedBy = user.evakaUserId,
                        contentLockedAt = now,
                        contentLockedBy = null,
                        publishedAt = now,
                        publishedBy = user.evakaUserId,
                        decision = DevChildDocumentDecision(
                            daycareId = daycare.id,
                            validity = if (status != ChildDocumentDecisionStatus.REJECTED) DateRange(LocalDate.of(2020, 1, 1), validityEnd) else null,
                            modifiedBy = employee.id,
                            createdBy = employee.id,
                            status = status,
                        ),
                        decisionMaker = employee.id,
                    ),
                )
            }
            return@transaction docId
        }
    }

    private fun insertFeeDecision(
        status: FeeDecisionStatus = FeeDecisionStatus.SENT,
        approvedAt: HelsinkiDateTime = HelsinkiDateTime.of(today, LocalTime.of(0, 0)),
    ): FeeDecisionId = db.transaction { tx ->
        tx.insert(
            DevFeeDecision(
                headOfFamilyId = guardian.id,
                status = status,
                validDuring = FiniteDateRange(today, today.plusMonths(1)),
                approvedAt = approvedAt,
            ),
        )
    }

    private fun insertVoucherValueDecision(
        status: VoucherValueDecisionStatus = VoucherValueDecisionStatus.SENT,
        approvedAt: HelsinkiDateTime = HelsinkiDateTime.of(today, LocalTime.of(0, 0)),
    ): VoucherValueDecisionId = db.transaction { tx ->
        tx.insert(
            DevVoucherValueDecision(
                headOfFamilyId = guardian.id,
                status = status,
                validFrom = today,
                validTo = today.plusMonths(1),
                approvedAt = approvedAt,
                childId = childId,
                placementUnitId = daycare.id,
            ),
        )
    }

    private fun insertDecisionData(
        status: DecisionStatus = DecisionStatus.ACCEPTED,
        type: DecisionType = DecisionType.DAYCARE,
        resolvedAt: HelsinkiDateTime = HelsinkiDateTime.of(today, LocalTime.of(0, 0)),
    ): DecisionId = db.transaction { tx ->
        val appId = tx.insertTestApplication(
            type = when (type) {
                DecisionType.DAYCARE -> ApplicationType.DAYCARE
                DecisionType.PRESCHOOL -> ApplicationType.PRESCHOOL
                DecisionType.CLUB -> ApplicationType.CLUB
                DecisionType.PRESCHOOL_DAYCARE -> ApplicationType.DAYCARE
                DecisionType.PRESCHOOL_CLUB -> ApplicationType.CLUB
                DecisionType.PREPARATORY_EDUCATION -> ApplicationType.PRESCHOOL
                DecisionType.DAYCARE_PART_TIME -> ApplicationType.DAYCARE
            },
            status = ApplicationStatus.ACTIVE,
            guardianId = guardian.id,
            childId = childId,
            confidential = true,
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
                apply = Apply(preferredUnits = listOf(daycare.id)),
                preferredStartDate = LocalDate.of(2026, 1, 1),
            ),
        )
        tx.insertTestDecision(
            TestDecision(
                applicationId = appId,
                unitId = daycare.id,
                status = status,
                createdBy = user.evakaUserId,
                type = type,
                startDate = today,
                endDate = today.plusMonths(1),
                resolved = resolvedAt,
                resolvedBy = user.evakaUserId.raw,
            ),
        )
    }

    private fun getScheduledDecisionArchivalJobs(): List<AsyncJobInfo> = db.read { tx ->
        tx.createQuery {
            sql(
                """
                        SELECT payload->>'decisionId' as document_id
                        FROM async_job
                        WHERE type = 'ArchiveDecision'
                        ORDER BY id
                        """,
            )
        }
            .toList<AsyncJobInfo>()
    }

    private fun getScheduledFeeDecisionArchivalJobs(): List<AsyncJobInfo> = db.read { tx ->
        tx.createQuery {
            sql(
                """
                        SELECT payload->>'feeDecisionId' as document_id
                        FROM async_job
                        WHERE type = 'ArchiveFeeDecision'
                        ORDER BY id
                        """,
            )
        }
            .toList<AsyncJobInfo>()
    }

    private fun getScheduledVoucherValueDecisionArchivalJobs(): List<AsyncJobInfo> = db.read { tx ->
        tx.createQuery {
            sql(
                """
                        SELECT payload->>'voucherValueDecisionId' as document_id
                        FROM async_job
                        WHERE type = 'ArchiveVoucherValueDecision'
                        ORDER BY id
                        """,
            )
        }
            .toList<AsyncJobInfo>()
    }

    private fun getScheduledChildDocumentArchivalJobs(): List<AsyncJobInfo> = db.read { tx ->
        tx.createQuery {
            sql(
                """
                        SELECT payload->>'documentId' as document_id
                        FROM async_job
                        WHERE type = 'ArchiveChildDocument'
                        ORDER BY id
                        """,
            )
        }
            .toList<AsyncJobInfo>()
    }

    data class AsyncJobInfo(val documentId: String)
}
