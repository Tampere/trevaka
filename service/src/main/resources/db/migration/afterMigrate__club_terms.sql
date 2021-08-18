INSERT INTO club_term
    (id, term, application_period)
VALUES
    ('4df642ba-ac98-11eb-9ca8-2ff9d825b593', '[2021-08-10,2022-06-03]', '[2021-01-01,2021-08-10]'),
    ('4df67de8-ac98-11eb-9ca9-93954620f29f', '[2022-08-10,2023-06-02]', '[2021-01-01,2022-08-10]')
ON CONFLICT (id) DO
UPDATE SET
    term = EXCLUDED.term,
    application_period = EXCLUDED.application_period
WHERE
    club_term.term <> EXCLUDED.term OR
    club_term.application_period <> EXCLUDED.application_period;
