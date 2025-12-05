// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.archival

import fi.espoo.evaka.document.ChildDocumentType
import fi.espoo.evaka.document.DocumentTemplateContent
import fi.espoo.evaka.document.childdocument.ChildDocumentDecisionStatus
import fi.espoo.evaka.document.childdocument.DocumentContent
import fi.espoo.evaka.document.childdocument.DocumentStatus
import fi.espoo.evaka.placement.PlacementType
import fi.espoo.evaka.shared.AreaId
import fi.espoo.evaka.shared.ChildDocumentId
import fi.espoo.evaka.shared.ChildId
import fi.espoo.evaka.shared.async.AsyncJob
import fi.espoo.evaka.shared.async.AsyncJobRunner
import fi.espoo.evaka.shared.auth.AuthenticatedUser
import fi.espoo.evaka.shared.dev.DevChildDocument
import fi.espoo.evaka.shared.dev.DevChildDocumentDecision
import fi.espoo.evaka.shared.dev.DevDaycare
import fi.espoo.evaka.shared.dev.DevDocumentTemplate
import fi.espoo.evaka.shared.dev.DevEmployee
import fi.espoo.evaka.shared.dev.DevPerson
import fi.espoo.evaka.shared.dev.DevPersonType
import fi.espoo.evaka.shared.dev.DevPlacement
import fi.espoo.evaka.shared.dev.insert
import fi.espoo.evaka.shared.domain.DateRange
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.espoo.evaka.shared.domain.MockEvakaClock
import fi.espoo.evaka.shared.domain.UiLanguage
import fi.tampere.trevaka.AbstractTampereIntegrationTest
import fi.tampere.trevaka.TampereProperties
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import trevaka.archival.planDocumentArchival
import java.time.LocalDate
import java.time.LocalDateTime
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
    fun `schedules archival job for child document decision well after placement end`() {
        val schedule = tampereProperties.archival?.schedule!!

        // placement end + schedule delay
        val eligibleArchivalClock = MockEvakaClock(now.plusYears(1).plusDays(schedule.documentDecisionDelayDays))
        val validityEnd = today.plusYears(1)
        val docId = insertTemplateAndDocument(validityEnd, archiveExternally = true, ChildDocumentType.OTHER_DECISION)

        planDocumentArchival(db, eligibleArchivalClock, asyncJobRunner, schedule)

        val jobs = getScheduledChildDocumentArchivalJobs()
        assertEquals(1, jobs.size)
        assertEquals(docId.toString(), jobs.first().documentId)
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
    fun `does not schedule job for child document decision when not marked for archival or document already archived`() {
        val schedule = tampereProperties.archival?.schedule!!

        // placement end + schedule delay
        val eligibleArchivalClock = MockEvakaClock(now.plusYears(1).plusDays(schedule.documentDecisionDelayDays))
        val validityEnd = today.plusYears(1)

        insertTemplateAndDocument(validityEnd, archiveExternally = false, ChildDocumentType.OTHER_DECISION)

        val docId = insertTemplateAndDocument(validityEnd, archiveExternally = true)
        markArchived(docId)

        planDocumentArchival(db, eligibleArchivalClock, asyncJobRunner, schedule)

        val jobs = getScheduledChildDocumentArchivalJobs()
        assertEquals(0, jobs.size)
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
        markArchived(docId)

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

        // Plan with limit of 3
        planDocumentArchival(db, clock, asyncJobRunner, schedule)

        val jobs = getScheduledChildDocumentArchivalJobs()
        assertEquals(1, jobs.size)
    }

    private fun markArchived(documentId: ChildDocumentId) {
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

    private fun insertTemplateAndDocument(
        validityEnd: LocalDate,
        archiveExternally: Boolean,
        type: ChildDocumentType = ChildDocumentType.VASU,
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
                            validity = DateRange(LocalDate.of(2020, 1, 1), validityEnd),
                            modifiedBy = employee.id,
                            createdBy = employee.id,
                            status = ChildDocumentDecisionStatus.ACCEPTED,
                        ),
                        decisionMaker = employee.id,
                    ),
                )
            }
            return@transaction docId
        }
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
