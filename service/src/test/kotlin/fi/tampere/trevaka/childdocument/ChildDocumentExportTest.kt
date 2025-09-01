// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.childdocument

import fi.espoo.evaka.document.CheckboxGroupQuestionOption
import fi.espoo.evaka.document.ChildDocumentType
import fi.espoo.evaka.document.DocumentTemplateContent
import fi.espoo.evaka.document.Question
import fi.espoo.evaka.document.RadioButtonGroupQuestionOption
import fi.espoo.evaka.document.Section
import fi.espoo.evaka.document.childdocument.AnsweredQuestion
import fi.espoo.evaka.document.childdocument.CheckboxGroupAnswerContent
import fi.espoo.evaka.document.childdocument.DocumentContent
import fi.espoo.evaka.document.childdocument.DocumentStatus
import fi.espoo.evaka.shared.dev.DevChildDocument
import fi.espoo.evaka.shared.dev.DevDocumentTemplate
import fi.espoo.evaka.shared.dev.DevEmployee
import fi.espoo.evaka.shared.dev.DevPerson
import fi.espoo.evaka.shared.dev.DevPersonType
import fi.espoo.evaka.shared.dev.insert
import fi.espoo.evaka.shared.domain.DateRange
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.espoo.evaka.shared.domain.MockEvakaClock
import fi.tampere.trevaka.AbstractTampereIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.jdbi.v3.json.Json
import org.junit.jupiter.api.Test
import java.time.LocalDate

class ChildDocumentExportTest : AbstractTampereIntegrationTest() {

    @Test
    fun `child_document_export executes`() {
        val clock = MockEvakaClock(HelsinkiDateTime.now())
        val employee = DevEmployee()
        val child1 = DevPerson(
            ophPersonOid = "1.2.3.4",
        )
        val child2 = DevPerson()
        db.transaction { tx ->
            tx.insert(employee)
            val templateId = tx.insert(
                DevDocumentTemplate(
                    type = ChildDocumentType.LEOPS,
                    validity = DateRange(clock.today(), null),
                    content = DocumentTemplateContent(
                        sections = listOf(
                            Section(
                                id = "1",
                                label = "1",
                                questions = listOf(
                                    Question.TextQuestion(
                                        id = "1.1",
                                        label = "1.1",
                                    ),
                                    Question.CheckboxQuestion(
                                        id = "1.2",
                                        label = "1.2",
                                    ),
                                    Question.CheckboxGroupQuestion(
                                        id = "1.3",
                                        label = "1.3",
                                        options = listOf(
                                            CheckboxGroupQuestionOption(
                                                id = "1.3.1",
                                                label = "1.3.1",
                                            ),
                                            CheckboxGroupQuestionOption(
                                                id = "1.3.2",
                                                label = "1.3.2",
                                            ),
                                            CheckboxGroupQuestionOption(
                                                id = "1.3.3",
                                                label = "1.3.3",
                                            ),
                                        ),
                                    ),
                                    Question.RadioButtonGroupQuestion(
                                        id = "1.4",
                                        label = "1.4",
                                        options = listOf(
                                            RadioButtonGroupQuestionOption(
                                                id = "1.4.1",
                                                label = "1.4.1",
                                            ),
                                            RadioButtonGroupQuestionOption(
                                                id = "1.4.2",
                                                label = "1.4.2",
                                            ),
                                            RadioButtonGroupQuestionOption(
                                                id = "1.4.3",
                                                label = "1.4.3",
                                            ),
                                        ),
                                    ),
                                    Question.StaticTextDisplayQuestion(
                                        id = "1.5",
                                        label = "1.5",
                                    ),
                                    Question.DateQuestion(
                                        id = "1.6",
                                        label = "1.6",
                                    ),
                                    Question.GroupedTextFieldsQuestion(
                                        id = "1.7",
                                        label = "1.7",
                                        fieldLabels = listOf("1.7.1", "1.7.2", "1.7.3"),
                                        allowMultipleRows = false,
                                    ),
                                    Question.GroupedTextFieldsQuestion(
                                        id = "1.8",
                                        label = "1.8",
                                        fieldLabels = listOf("1.8.1", "1.8.2", "1.8.3"),
                                        allowMultipleRows = true,
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            )
            val child1Id = tx.insert(
                child1,
                DevPersonType.CHILD,
            )
            tx.insert(
                DevChildDocument(
                    status = DocumentStatus.COMPLETED,
                    childId = child1Id,
                    templateId = templateId,
                    content = DocumentContent(
                        answers = emptyList(),
                    ),
                    publishedContent = DocumentContent(
                        answers = listOf(
                            AnsweredQuestion.TextAnswer(
                                questionId = "1.1",
                                answer = "hello world",
                            ),
                            AnsweredQuestion.CheckboxAnswer(
                                questionId = "1.2",
                                answer = true,
                            ),
                            AnsweredQuestion.CheckboxGroupAnswer(
                                questionId = "1.3",
                                answer = listOf(
                                    CheckboxGroupAnswerContent(optionId = "1.3.1"),
                                    CheckboxGroupAnswerContent(optionId = "1.3.3"),
                                ),
                            ),
                            AnsweredQuestion.RadioButtonGroupAnswer(
                                questionId = "1.4",
                                answer = "1.4.2",
                            ),
                            AnsweredQuestion.DateAnswer(
                                questionId = "1.6",
                                answer = LocalDate.of(2025, 2, 11),
                            ),
                            AnsweredQuestion.GroupedTextFieldsAnswer(
                                questionId = "1.7",
                                answer = listOf(
                                    listOf(
                                        "1.7.1 answer",
                                        "1.7.2 answer",
                                        "1.7.3 answer",
                                    ),
                                ),
                            ),
                            AnsweredQuestion.GroupedTextFieldsAnswer(
                                questionId = "1.8",
                                answer = listOf(
                                    listOf("1.8.1 answer1", "1.8.2 answer1", "1.8.3 answer1"),
                                    listOf("1.8.1 answer2", "1.8.2 answer2", "1.8.3 answer2"),
                                ),
                            ),
                        ),
                    ),
                    modifiedAt = clock.now(),
                    contentLockedAt = clock.now(),
                    contentLockedBy = employee.id,
                    publishedAt = clock.now(),
                    answeredAt = null,
                    modifiedBy = employee.evakaUserId,
                    publishedBy = employee.evakaUserId,
                ),
            )
            val child2Id = tx.insert(
                child2,
                DevPersonType.CHILD,
            )
            tx.insert(
                DevChildDocument(
                    status = DocumentStatus.COMPLETED,
                    childId = child2Id,
                    templateId = templateId,
                    content = DocumentContent(
                        answers = emptyList(),
                    ),
                    publishedContent = DocumentContent(
                        answers = listOf(
                            AnsweredQuestion.TextAnswer(
                                questionId = "1.1",
                                answer = "hello world",
                            ),
                            AnsweredQuestion.TextAnswer(
                                questionId = "1.0",
                                answer = "Hämy",
                            ),
                        ),
                    ),
                    modifiedAt = clock.now(),
                    contentLockedAt = clock.now(),
                    contentLockedBy = employee.id,
                    publishedAt = clock.now(),
                    answeredAt = null,
                    modifiedBy = employee.evakaUserId,
                    publishedBy = employee.evakaUserId,
                ),
            )
        }

        val export = db.read { tx ->
            tx.createQuery { sql("SELECT data FROM child_document_export") }.exactlyOne<ChildDocumentExport>()
        }

        assertThat(export.data)
            .containsExactlyInAnyOrder(
                ChildDocumentExportRow(
                    child = ChildDocumentExportChild(
                        oid = child1.ophPersonOid,
                        date_of_birth = child1.dateOfBirth,
                        first_name = child1.firstName,
                        last_name = child1.lastName,
                    ),
                    document = mapOf(
                        "1.1" to "hello world",
                        "1.2" to true,
                        "1.3" to listOf("1.3.1", "1.3.3"),
                        "1.4" to "1.4.2",
                        "1.6" to "11.02.2025",
                        "1.7" to listOf(mapOf("1.7.1" to "1.7.1 answer", "1.7.2" to "1.7.2 answer", "1.7.3" to "1.7.3 answer")),
                        "1.8" to listOf(
                            mapOf("1.8.1" to "1.8.1 answer1", "1.8.2" to "1.8.2 answer1", "1.8.3" to "1.8.3 answer1"),
                            mapOf("1.8.1" to "1.8.1 answer2", "1.8.2" to "1.8.2 answer2", "1.8.3" to "1.8.3 answer2"),
                        ),
                    ),
                ),
                ChildDocumentExportRow(
                    child = ChildDocumentExportChild(
                        oid = child2.ophPersonOid,
                        date_of_birth = child2.dateOfBirth,
                        first_name = child2.firstName,
                        last_name = child2.lastName,
                    ),
                    document = mapOf(
                        "1.1" to "hello world",
                    ),
                ),
            )
    }
}

private data class ChildDocumentExport(@param:Json val data: List<ChildDocumentExportRow>)
private data class ChildDocumentExportRow(val child: ChildDocumentExportChild, val document: Map<String, Any?>)
private data class ChildDocumentExportChild(
    val oid: String?,
    val date_of_birth: LocalDate,
    val first_name: String,
    val last_name: String,
)
