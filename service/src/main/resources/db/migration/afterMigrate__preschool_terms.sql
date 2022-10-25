-- SPDX-FileCopyrightText: 2021-2022 City of Tampere
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

INSERT INTO preschool_term
    (id, finnish_preschool, swedish_preschool, extended_term, application_period)
VALUES
    ('2d5aa324-1c73-11ed-bc5c-fbccc3898735', '[2021-08-10,2022-06-03]', '[2021-08-10,2022-06-03]', '[2021-08-10,2022-06-03]', '[2021-01-01,2021-08-10]'),
    ('5409a304-246a-11ed-b02b-fb2d9390be24', '[2022-08-10,2023-06-02]', '[2022-08-10,2023-06-02]', '[2022-08-10,2023-06-02]', '[2021-01-01,2022-08-10]')
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
