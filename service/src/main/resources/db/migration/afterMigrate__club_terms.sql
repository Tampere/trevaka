-- SPDX-FileCopyrightText: 2021 City of Tampere
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

INSERT INTO club_term
    (id, term, application_period)
VALUES
    ('4df642ba-ac98-11eb-9ca8-2ff9d825b593', '[2021-08-10,2022-06-03]', '[2021-01-01,2021-08-10]'),
    ('4df67de8-ac98-11eb-9ca9-93954620f29f', '[2022-08-10,2023-06-02]', '[2021-01-01,2022-08-10]'),
    ('c296a9d8-b80e-11ed-b4d9-9ffc264cd12a', '[2023-08-09,2024-05-31]', '[2021-01-01,2023-08-09]')
ON CONFLICT (id) DO
UPDATE SET
    term = EXCLUDED.term,
    application_period = EXCLUDED.application_period
WHERE
    club_term.term <> EXCLUDED.term OR
    club_term.application_period <> EXCLUDED.application_period;
