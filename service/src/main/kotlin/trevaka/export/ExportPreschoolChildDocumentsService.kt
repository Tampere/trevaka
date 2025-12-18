// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka.export

import fi.espoo.evaka.shared.DocumentTemplateId
import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

@Service
class ExportPreschoolChildDocumentsService(private val s3Client: S3Client) {
    fun exportPreschoolChildDocuments(
        tx: Database.Read,
        timestamp: HelsinkiDateTime,
        bucket: String,
        municipalityCode: String,
    ): Pair<String, String> {
        val date = timestamp.toLocalDate()
        val templateId = tx.getTemplateIdForExport(date)
        logger.info { "Export child documents for template $templateId" }
        val documents = tx.getDocumentsJsonForExport(templateId)

        val key = "reporting/preschool/${municipalityCode}_evaka_child_documents_$date.json"
        val request = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .contentType("application/json")
            .build()
        val body = RequestBody.fromString(documents)
        s3Client.putObject(request, body)
        return bucket to key
    }
}

private fun Database.Read.getTemplateIdForExport(date: LocalDate): DocumentTemplateId = createQuery {
    sql(
        """
SELECT id
FROM document_template
WHERE name ILIKE '%tiedonsiirto esiopetuksesta perusopetukseen%'
  AND upper(validity) <= ${bind(date)}
  AND published
ORDER BY validity DESC
LIMIT 1
        """.trimIndent(),
    )
}.exactlyOne<DocumentTemplateId>()

private fun Database.Read.getDocumentsJsonForExport(templateId: DocumentTemplateId): String = createQuery {
    sql(
        """
WITH question AS (SELECT question
                  FROM document_template
                  CROSS JOIN jsonb_array_elements(document_template.content -> 'sections') section
                  CROSS JOIN jsonb_array_elements(section -> 'questions') question
                  WHERE document_template.id = ${bind(templateId)}
                    AND question ->> 'type' != 'STATIC_TEXT_DISPLAY'),
     document AS (SELECT DISTINCT ON (child_id) *
                  FROM child_document
                  WHERE template_id = ${bind(templateId)}
                    AND status = 'COMPLETED'
                  ORDER BY child_id, published_at DESC NULLS LAST),
     answer AS (SELECT document.child_id, answer
                FROM document
                CROSS JOIN jsonb_array_elements(document.published_content -> 'answers') answer),
     child_and_document AS (SELECT jsonb_build_object('child', jsonb_build_object('oid', person.oph_person_oid,
                                                                                  'date_of_birth', person.date_of_birth,
                                                                                  'first_name', person.first_name,
                                                                                  'last_name', person.last_name),
                                                      'document', jsonb_object_agg(question ->> 'label',
                                                                                   CASE answer ->> 'type'
                                                                                       WHEN 'CHECKBOX_GROUP'
                                                                                           THEN (SELECT jsonb_agg(option -> 'label')
                                                                                                 FROM jsonb_array_elements(question -> 'options') option,
                                                                                                      jsonb_array_elements(answer -> 'answer') selection
                                                                                                 WHERE selection -> 'optionId' = option -> 'id')
                                                                                       WHEN 'RADIO_BUTTON_GROUP'
                                                                                           THEN (SELECT option -> 'label'
                                                                                                 FROM jsonb_array_elements(question -> 'options') option
                                                                                                 WHERE answer -> 'answer' = option -> 'id')
                                                                                       WHEN 'DATE'
                                                                                           THEN to_jsonb(to_char(to_date(answer ->> 'answer', 'YYYY-MM-DD'), 'DD.MM.YYYY'))
                                                                                       WHEN 'GROUPED_TEXT_FIELDS'
                                                                                           THEN (SELECT jsonb_agg(row.object)
                                                                                                 FROM (SELECT jsonb_object_agg(label.value #>> '{}', cell.value) AS object
                                                                                                       FROM jsonb_array_elements(question -> 'fieldLabels') WITH ORDINALITY AS label (value, ordinal)
                                                                                                       CROSS JOIN jsonb_array_elements(answer -> 'answer') WITH ORDINALITY AS row (value, ordinal)
                                                                                                       CROSS JOIN jsonb_array_elements(row.value) WITH ORDINALITY AS cell (value, ordinal)
                                                                                                       WHERE cell.ordinal = label.ordinal
                                                                                                       GROUP BY row.ordinal
                                                                                                       ORDER BY row.ordinal) row)
                                                                                       ELSE answer -> 'answer' END)) AS data
                            FROM question
                            JOIN answer ON question -> 'id' = answer -> 'questionId'
                            JOIN person ON answer.child_id = person.id
                            GROUP BY person.id)
SELECT coalesce(jsonb_agg(child_and_document.data), '[]') AS data
FROM child_and_document
        """.trimIndent(),
    )
}.exactlyOne<String>()
