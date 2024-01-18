-- SPDX-FileCopyrightText: 2021 City of Tampere
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

INSERT INTO club_term
    (id, term, application_period, term_breaks)
VALUES
    ('4df642ba-ac98-11eb-9ca8-2ff9d825b593', '[2021-08-10,2022-06-03]', '[2021-01-01,2021-08-10]', datemultirange()),
    ('4df67de8-ac98-11eb-9ca9-93954620f29f', '[2022-08-10,2023-06-02]', '[2021-01-01,2022-08-10]', datemultirange()),
    ('c296a9d8-b80e-11ed-b4d9-9ffc264cd12a', '[2023-08-09,2024-05-31]', '[2021-01-01,2023-08-09]', datemultirange('[2023-10-16,2023-10-22]', '[2023-12-23,2024-01-07]', '[2024-02-26,2024-03-03]', '[2024-03-28,2024-04-01]', '[2024-05-09,2024-05-10]')),
    ('aba0d4dc-565f-43b6-9f4e-690e5f42422d', '[2024-08-07,2025-05-28]', '[2024-01-23,2025-05-28]', datemultirange('[2024-10-14,2024-10-20]', '[2024-12-21,2025-01-06]', '[2025-02-24,2025-03-02]', '[2025-04-17,2025-04-21]'))
ON CONFLICT (id) DO
UPDATE SET
    term = EXCLUDED.term,
    application_period = EXCLUDED.application_period,
    term_breaks = EXCLUDED.term_breaks
WHERE
    club_term.term <> EXCLUDED.term OR
    club_term.application_period <> EXCLUDED.application_period OR
    club_term.term_breaks <> EXCLUDED.term_breaks;
