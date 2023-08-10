-- SPDX-FileCopyrightText: 2021-2023 City of Tampere
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

CREATE OR REPLACE TEMPORARY VIEW assistance_need_view AS
SELECT child_id, need.updated AS modified, updated_by AS modified_by, daterange(start_date, end_date, '[]') AS valid_during, option.value
FROM assistance_basis_option_ref ref
JOIN assistance_basis_option option ON option.id = ref.option_id
JOIN assistance_need need ON need.id = ref.need_id;

-- assistance factor
INSERT INTO assistance_factor (child_id, modified, modified_by, valid_during, capacity_factor)
SELECT child_id, updated AS modified, updated_by AS modified_by, daterange(start_date, end_date, '[]') AS valid_during, capacity_factor
FROM assistance_need;

-- daycare assistance
INSERT INTO daycare_assistance (child_id, modified, modified_by, valid_during, level)
SELECT child_id, modified, modified_by, valid_during, 'INTENSIFIED_SUPPORT'
FROM assistance_need_view intensified WHERE value = 'INTENSIFIED_ASSISTANCE'
AND NOT EXISTS (
    SELECT
    FROM assistance_need_view special
    WHERE intensified.child_id = special.child_id
    AND intensified.valid_during = special.valid_during
    AND special.value = 'SPECIAL_ASSISTANCE_DECISION'
);

INSERT INTO daycare_assistance (child_id, modified, modified_by, valid_during, level)
SELECT child_id, modified, modified_by, valid_during, 'SPECIAL_SUPPORT'
FROM assistance_need_view special WHERE value = 'SPECIAL_ASSISTANCE_DECISION'
AND NOT EXISTS (
    SELECT
    FROM assistance_need_view intensified
    WHERE special.child_id = intensified.child_id
    AND special.valid_during = intensified.valid_during
    AND intensified.value = 'INTENSIFIED_ASSISTANCE'
);

INSERT INTO daycare_assistance (child_id, modified, modified_by, valid_during, level)
SELECT intensified.child_id, intensified.modified, intensified.modified_by, intensified.valid_during,
    CASE
        WHEN array_length(array_agg(previous.value), 1) > 1 THEN 'SPECIAL_SUPPORT'::daycare_assistance_level
        WHEN (array_agg(previous.value))[1] = 'SPECIAL_ASSISTANCE_DECISION' THEN 'INTENSIFIED_SUPPORT'::daycare_assistance_level
        WHEN (array_agg(previous.value))[1] = 'INTENSIFIED_ASSISTANCE' THEN 'SPECIAL_SUPPORT'::daycare_assistance_level
        ELSE 'INTENSIFIED_SUPPORT'::daycare_assistance_level
    END
FROM assistance_need_view intensified, assistance_need_view special, assistance_need_view previous
WHERE intensified.child_id = special.child_id AND
    previous.child_id = intensified.child_id AND
    previous.child_id = special.child_id AND
    intensified.valid_during = special.valid_during AND
    upper(previous.valid_during) = lower(intensified.valid_during) AND
    upper(previous.valid_during) = lower(special.valid_during) AND
    intensified.value = 'INTENSIFIED_ASSISTANCE' AND
    special.value = 'SPECIAL_ASSISTANCE_DECISION' AND
    previous.value IN ('SPECIAL_ASSISTANCE_DECISION', 'INTENSIFIED_ASSISTANCE')
GROUP BY 1, 2, 3, 4;

-- preschool assistance
INSERT INTO preschool_assistance (child_id, modified, modified_by, valid_during, level)
SELECT child_id, modified, modified_by, valid_during, 'SPECIAL_SUPPORT_WITH_DECISION_LEVEL_1'
FROM assistance_need_view WHERE value = 'EXTENDED_COMPULSORY_EDUCATION';

-- other assistance measure
WITH
deleted_transport_benefits AS (
    DELETE FROM assistance_action_option_ref ref
    USING assistance_action_option option, assistance_action action
    WHERE option.id = ref.option_id AND action.id = ref.action_id AND option.value = '90'
    RETURNING child_id, action.updated AS modified, updated_by AS modified_by, daterange(start_date, end_date, '[]') AS valid_during
)
INSERT INTO other_assistance_measure (child_id, modified, modified_by, valid_during, type)
SELECT child_id, modified, modified_by, valid_during, 'TRANSPORT_BENEFIT'
FROM deleted_transport_benefits;

INSERT INTO other_assistance_measure (child_id, modified, modified_by, valid_during, type)
SELECT child_id, modified, modified_by, valid_during, 'ACCULTURATION_SUPPORT'
FROM assistance_need_view WHERE value = 'CHILD_ACCULTURATION_SUPPORT';

-- cleanup
DELETE FROM assistance_action_option WHERE value = '90';
