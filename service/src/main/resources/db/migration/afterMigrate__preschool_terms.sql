-- SPDX-FileCopyrightText: 2021-2022 City of Tampere
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

DELETE FROM preschool_term WHERE id = '2d5aa324-1c73-11ed-bc5c-fbccc3898735';
DELETE FROM preschool_term WHERE id = '5409a304-246a-11ed-b02b-fb2d9390be24';

INSERT INTO preschool_term
    (id, finnish_preschool, swedish_preschool, extended_term, application_period)
VALUES
    ('4a2e558c-658d-11ed-8e91-0fa1ddc0fe69', '[2023-08-09,2024-05-31]', '[2023-08-09,2024-05-31]', '[2023-08-09,2024-05-31]', '[2022-10-01,2024-05-31]')
ON CONFLICT (id) DO
UPDATE SET
    finnish_preschool = EXCLUDED.finnish_preschool,
    swedish_preschool = EXCLUDED.swedish_preschool,
    extended_term = EXCLUDED.extended_term,
    application_period = EXCLUDED.application_period
WHERE
    preschool_term.finnish_preschool <> EXCLUDED.finnish_preschool OR
    preschool_term.swedish_preschool <> EXCLUDED.swedish_preschool OR
    preschool_term.extended_term <> EXCLUDED.extended_term OR
    preschool_term.application_period <> EXCLUDED.application_period;
