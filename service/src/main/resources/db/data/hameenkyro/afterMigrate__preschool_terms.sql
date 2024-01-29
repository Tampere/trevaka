-- SPDX-FileCopyrightText: 2024 Tampere region
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

INSERT INTO preschool_term
    (id, finnish_preschool, swedish_preschool, extended_term, application_period, term_breaks)
VALUES
    ('4a2e558c-658d-11ed-8e91-0fa1ddc0fe71', '[2023-08-09,2024-05-31]', '[2023-08-09,2024-05-31]', '[2023-08-09,2024-05-31]', '[2022-10-01,2024-05-31]', datemultirange('[2023-10-16,2023-10-22]', '[2023-12-23,2024-01-07]', '[2024-02-26,2024-03-03]', '[2024-03-28,2024-04-01]', '[2024-05-09,2024-05-10]'))
ON CONFLICT (id) DO
UPDATE SET
    finnish_preschool = EXCLUDED.finnish_preschool,
    swedish_preschool = EXCLUDED.swedish_preschool,
    extended_term = EXCLUDED.extended_term,
    application_period = EXCLUDED.application_period,
    term_breaks = EXCLUDED.term_breaks
WHERE
    preschool_term.finnish_preschool <> EXCLUDED.finnish_preschool OR
    preschool_term.swedish_preschool <> EXCLUDED.swedish_preschool OR
    preschool_term.extended_term <> EXCLUDED.extended_term OR
    preschool_term.application_period <> EXCLUDED.application_period OR
    preschool_term.term_breaks <> EXCLUDED.term_breaks;
