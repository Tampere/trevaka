-- SPDX-FileCopyrightText: 2023-2025 Tampere region
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

-- Usage:
-- \t
-- \a
-- \o child_document_export.json
-- select data from child_document_export;

DROP VIEW IF EXISTS child_document_export;

CREATE VIEW child_document_export AS
WITH template AS (SELECT id, content
                  FROM document_template
                  WHERE type = 'LEOPS'
                    AND validity @> date(timezone('Europe/Helsinki', now()))),
     question AS (SELECT template.id AS template_id, question
                  FROM template
                           CROSS JOIN jsonb_array_elements(template.content -> 'sections') section
                           CROSS JOIN jsonb_array_elements(section -> 'questions') question
                  WHERE question ->> 'type' != 'STATIC_TEXT_DISPLAY'),
     answer AS (SELECT child_document.child_id,
                       child_document.template_id,
                       answer
                FROM child_document
                         CROSS JOIN jsonb_array_elements(child_document.published_content -> 'answers') answer
                WHERE child_document.status = 'COMPLETED'
                  AND EXISTS (SELECT FROM template WHERE child_document.template_id = template.id)),
     child_and_document AS (SELECT jsonb_build_object('child',
                                                      jsonb_build_object('oid', person.oph_person_oid,
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
                                     JOIN answer ON question.template_id = answer.template_id AND
                                                    question -> 'id' = answer -> 'questionId'
                                     JOIN person ON answer.child_id = person.id
                            GROUP BY person.id)
SELECT jsonb_agg(child_and_document.data) AS data
FROM child_and_document;
