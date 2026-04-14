// SPDX-FileCopyrightText: 2026 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.vesilahti.evaka.export

import fi.espoo.evaka.document.DocumentTemplateContent
import fi.espoo.evaka.document.Question.TextQuestion
import fi.espoo.evaka.document.Section
import fi.espoo.evaka.document.childdocument.AnsweredQuestion
import fi.espoo.evaka.document.childdocument.DocumentContent
import fi.espoo.evaka.document.childdocument.DocumentStatus
import fi.espoo.evaka.shared.dev.DevChildDocument
import fi.espoo.evaka.shared.dev.DevChildDocumentPublishedVersion
import fi.espoo.evaka.shared.dev.DevDocumentTemplate
import fi.espoo.evaka.shared.dev.DevEmployee
import fi.espoo.evaka.shared.dev.DevPerson
import fi.espoo.evaka.shared.dev.DevPersonType
import fi.espoo.evaka.shared.dev.insert
import fi.espoo.evaka.shared.domain.DateRange
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.espoo.evaka.shared.domain.MockEvakaClock
import fi.espoo.evaka.shared.sftp.SftpClient
import fi.vesilahti.evaka.AbstractVesilahtiIntegrationTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import trevaka.export.ChildDocumentTransferType
import trevaka.export.exportChildDocumentsViaSftp
import java.time.LocalDate
import java.time.LocalTime

private val timestamp = HelsinkiDateTime.of(LocalDate.of(2025, 8, 1), LocalTime.of(5, 1))
private val clock = MockEvakaClock(timestamp)

class ExportDaycareToPreschoolChildDocumentsTest : AbstractVesilahtiIntegrationTest() {

    @Test
    fun `no template throws`() {
        val primus = properties.primus ?: error("Primus not configured")
        assertThrows<IllegalStateException> {
            exportChildDocumentsViaSftp(db, clock, "922", primus, ChildDocumentTransferType.DAYCARE_TO_PRESCHOOL)
        }
    }

    @Test
    fun `template without documents exports empty array`() {
        db.transaction { tx ->
            tx.insert(
                DevDocumentTemplate(
                    name = "Tiedonsiirto varhaiskasvatuksesta esiopetukseen 2024-2025",
                    validity = DateRange(LocalDate.of(2024, 8, 1), LocalDate.of(2025, 7, 31)),
                    content = DocumentTemplateContent(sections = emptyList()),
                ),
            )
        }

        val primus = properties.primus ?: error("Primus not configured")
        exportChildDocumentsViaSftp(db, clock, "922", primus, ChildDocumentTransferType.DAYCARE_TO_PRESCHOOL)

        val sftpClient = SftpClient(primus.sftp.toSftpEnv())
        val data = sftpClient.getAsString("upload/922_daycare_to_preschool_transfer_2025-08-01.json", Charsets.UTF_8)
        assertEquals("[]", data)
    }

    @Test
    fun `latest template is selected`() {
        db.transaction { tx ->
            tx.insert(
                DevDocumentTemplate(
                    name = "Tiedonsiirto varhaiskasvatuksesta esiopetukseen 2023-2024",
                    validity = DateRange(LocalDate.of(2023, 8, 1), LocalDate.of(2024, 7, 31)),
                    content = DocumentTemplateContent(sections = emptyList()),
                ),
            )
            tx.insert(
                DevDocumentTemplate(
                    name = "Tiedonsiirto varhaiskasvatuksesta esiopetukseen 2024-2025",
                    validity = DateRange(LocalDate.of(2024, 8, 1), LocalDate.of(2025, 7, 31)),
                    content = DocumentTemplateContent(sections = emptyList()),
                ),
            )
            tx.insert(
                DevDocumentTemplate(
                    name = "Tiedonsiirto varhaiskasvatuksesta esiopetukseen 2025-2026",
                    validity = DateRange(LocalDate.of(2025, 8, 1), LocalDate.of(2026, 7, 31)),
                    content = DocumentTemplateContent(sections = emptyList()),
                ),
            )
            tx.insert(
                DevDocumentTemplate(
                    name = "Tiedonsiirto esiopetuksesta perusopetukseen 2024-2025",
                    validity = DateRange(LocalDate.of(2024, 8, 1), LocalDate.of(2025, 7, 31)),
                    content = DocumentTemplateContent(sections = emptyList()),
                ),
            )
        }

        val primus = properties.primus ?: error("Primus not configured")
        exportChildDocumentsViaSftp(db, clock, "922", primus, ChildDocumentTransferType.DAYCARE_TO_PRESCHOOL)

        val sftpClient = SftpClient(primus.sftp.toSftpEnv())
        val data = sftpClient.getAsString("upload/922_daycare_to_preschool_transfer_2025-08-01.json", Charsets.UTF_8)
        assertEquals("[]", data)
    }

    @Test
    fun `document data is exported`() {
        val questionId = "1.1"
        val answer = AnsweredQuestion.TextAnswer(questionId, answer = "vastaus")
        val content = DocumentContent(answers = listOf(answer))

        db.transaction { tx ->
            val employee = DevEmployee().also { tx.insert(it) }
            val childId = tx.insert(DevPerson(), DevPersonType.CHILD)
            val templateId = tx.insert(
                DevDocumentTemplate(
                    name = "Tiedonsiirto varhaiskasvatuksesta esiopetukseen 2024-2025",
                    validity = DateRange(LocalDate.of(2024, 8, 1), LocalDate.of(2025, 7, 31)),
                    content = DocumentTemplateContent(
                        sections = listOf(
                            Section(
                                id = "1",
                                label = "section",
                                questions = listOf(TextQuestion(questionId, "kysymys")),
                            ),
                        ),
                    ),
                ),
            )
            tx.insert(
                DevChildDocument(
                    status = DocumentStatus.COMPLETED,
                    childId = childId,
                    templateId = templateId,
                    content = content,
                    modifiedAt = timestamp,
                    modifiedBy = employee.evakaUserId,
                    contentLockedAt = timestamp,
                    contentLockedBy = null,
                    publishedVersions = listOf(
                        DevChildDocumentPublishedVersion(
                            versionNumber = 1,
                            createdAt = timestamp,
                            createdBy = employee.evakaUserId,
                            publishedContent = content,
                        ),
                    ),
                ),
            )
        }

        val primus = properties.primus ?: error("Primus not configured")
        exportChildDocumentsViaSftp(db, clock, "922", primus, ChildDocumentTransferType.DAYCARE_TO_PRESCHOOL)

        val sftpClient = SftpClient(primus.sftp.toSftpEnv())
        val data = sftpClient.getAsString("upload/922_daycare_to_preschool_transfer_2025-08-01.json", Charsets.UTF_8)
        assertEquals(
            "[{\"child\": {\"oid\": null, \"last_name\": \"Person\", \"first_name\": \"Test\", \"date_of_birth\": \"1980-01-01\"}, \"document\": {\"kysymys\": \"vastaus\"}}]",
            data,
        )
    }
}
