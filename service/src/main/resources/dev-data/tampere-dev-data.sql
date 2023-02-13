-- SPDX-FileCopyrightText: 2021 City of Tampere
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

INSERT INTO daycare (name, type, care_area_id, phone, url, backup_location, language_emphasis_id, opening_date, closing_date, email, schedule, additional_info, unit_manager_id, cost_center, upload_to_varda, capacity, decision_daycare_name, decision_preschool_name, decision_handler, decision_handler_address, street_address, postal_code, post_office, mailing_po_box, location, mailing_street_address, mailing_postal_code, mailing_post_office, invoiced_by_municipality, provider_type, language, upload_to_koski, oph_unit_oid, oph_organizer_oid, operation_days, ghost_unit, daycare_apply_period, preschool_apply_period, club_apply_period, finance_decision_handler, round_the_clock) VALUES
    ('Päiväkoti A', '{CENTRE}', (SELECT id FROM care_area WHERE name = 'Etelä'), NULL, NULL, NULL, NULL, '2021-04-20', NULL, NULL, NULL, NULL, NULL, '1', false, 0, 'Päiväkoti A', 'Päiväkoti A', 'Varhaiskasvatusyksikön johtaja', 'Suokatu 10', 'Runkokatu 5', '33340', 'Tampere', 'Runkokatu 5', '(23.60571,61.51667)', NULL, '33340', 'Tampere', TRUE, 'MUNICIPAL', 'fi', false, NULL, NULL, '{1,2,3,4,5}', NULL, '[2021-04-20,)', NULL, NULL, NULL, false),
    ('Päiväkoti B', '{CENTRE}', (SELECT id FROM care_area WHERE name = 'Etelä'), NULL, NULL, NULL, NULL, '2021-04-20', NULL, NULL, NULL, NULL, NULL, '1', false, 0, 'Päiväkoti B', 'Päiväkoti B', 'Varhaiskasvatusyksikön johtaja', 'Suokatu 10', 'Kraatarinkatu 5', '33270', 'Tampere', 'Kraatarinkatu 5', '(23.68205,61.5078)', NULL, '33270', 'Tampere', TRUE, 'MUNICIPAL', 'fi', false, NULL, NULL, '{1,2,3,4,5}', NULL, '[2021-04-20,)', NULL, NULL, NULL, false),
    ('Päiväkoti C', '{CENTRE}', (SELECT id FROM care_area WHERE name = 'Itä'), NULL, NULL, NULL, NULL, '2021-04-20', NULL, NULL, NULL, NULL, NULL, '2', false, 0, 'Päiväkoti C', 'Päiväkoti C', 'Varhaiskasvatusyksikön johtaja', 'Suokatu 10', 'Maamiehentie 2', '33340', 'TAMPERE', NULL, '(23.59596,61.51136)', NULL, '33340', 'TAMPERE', TRUE, 'MUNICIPAL', 'fi', false, NULL, NULL, '{1,2,3,4,5}', NULL, '[2021-04-20,)', NULL, NULL, NULL, false),
    ('Palveluseteli A', '{CENTRE}', (SELECT id FROM care_area WHERE name = 'Etelä'), NULL, NULL, NULL, NULL, '2021-04-20', NULL, NULL, NULL, NULL, NULL, NULL, false, 0, 'Palveluseteli A', 'Palveluseteli A', 'Varhaiskasvatusyksikön johtaja', 'Suokatu 10', 'Sairaalankatu 7', '33100', 'TAMPERE', 'Sairaalankatu 7', '(23.78589,61.49301)', NULL, '33100', 'TAMPERE', false, 'PRIVATE_SERVICE_VOUCHER', 'fi', false, NULL, NULL, '{1,2,3,4,5}', NULL, '[2021-04-20,)', NULL, NULL, NULL, false),
    ('Palveluseteli B', '{CENTRE}', (SELECT id FROM care_area WHERE name = 'Etelä'), NULL, NULL, NULL, NULL, '2021-04-20', NULL, NULL, NULL, NULL, NULL, NULL, false, 0, 'Palveluseteli B', 'Palveluseteli B', 'Varhaiskasvatusyksikön johtaja', 'Suokatu 10', 'Kuusitie 8', '36110', 'KANGASALA', 'Kuusitie 8', '(24.00936,61.53011)', NULL, '36110', 'KANGASALA', false, 'PRIVATE_SERVICE_VOUCHER', 'fi', false, NULL, NULL, '{1,2,3,4,5}', NULL, '[2021-04-20,)', NULL, NULL, NULL, false),
    ('Palveluseteli C', '{CENTRE}', (SELECT id FROM care_area WHERE name = 'Itä'), NULL, NULL, NULL, NULL, '2021-04-20', NULL, NULL, NULL, NULL, NULL, NULL, false, 0, 'Palveluseteli C', 'Palveluseteli C', 'Varhaiskasvatusyksikön johtaja', 'Suokatu 10', 'Tiiliruukinkatu 1', '33200', 'TAMPERE', 'Tiiliruukinkatu 1', '(23.75582,61.49336)', NULL, '33200', 'TAMPERE', false, 'PRIVATE_SERVICE_VOUCHER', 'fi', false, NULL, NULL, '{1,2,3,4,5}', NULL, '[2021-04-20,)', NULL, NULL, NULL, false),
    ('Perhepäivähoito', '{FAMILY}', (SELECT id FROM care_area WHERE name = 'Länsi'), NULL, NULL, NULL, NULL, '2021-04-20', NULL, NULL, NULL, NULL, NULL, NULL, false, 0, 'Perhepäivähoito', 'Perhepäivähoito', 'Varhaiskasvatusyksikön johtaja', 'Suokatu 10', 'Sairaalankatu 7', '33100', 'TAMPERE', 'Sairaalankatu 7', '(23.78589,61.49301)', NULL, '33100', 'TAMPERE', false, 'MUNICIPAL', 'fi', false, NULL, NULL, '{1,2,3,4,5}', NULL, '[2021-04-20,)', NULL, NULL, NULL, false),
    ('Perhepäivähoitaja A', '{FAMILY}', (SELECT id FROM care_area WHERE name = 'Etelä'), NULL, NULL, NULL, NULL, '2021-04-20', NULL, NULL, NULL, NULL, NULL, NULL, false, 0, 'Perhepäivähoitaja A', 'Perhepäivähoitaja A', 'Varhaiskasvatusyksikön johtaja', 'Suokatu 10', 'Sairaalankatu 7', '33100', 'TAMPERE', 'Sairaalankatu 7', '(23.78589,61.49301)', NULL, '33100', 'TAMPERE', false, 'MUNICIPAL', 'fi', false, NULL, NULL, '{1,2,3,4,5}', NULL, NULL, NULL, NULL, NULL, false),
    ('Perhepäivähoitaja B', '{FAMILY}', (SELECT id FROM care_area WHERE name = 'Etelä'), NULL, NULL, NULL, NULL, '2021-04-20', NULL, NULL, NULL, NULL, NULL, NULL, false, 0, 'Perhepäivähoitaja B', 'Perhepäivähoitaja B', 'Varhaiskasvatusyksikön johtaja', 'Suokatu 10', 'Kuusitie 8', '36110', 'KANGASALA', 'Kuusitie 8', '(24.00936,61.53011)', NULL, '36110', 'KANGASALA', false, 'MUNICIPAL', 'fi', false, NULL, NULL, '{1,2,3,4,5}', NULL, NULL, NULL, NULL, NULL, false),
    ('Perhepäivähoitaja C', '{FAMILY}', (SELECT id FROM care_area WHERE name = 'Itä'), NULL, NULL, NULL, NULL, '2021-04-20', NULL, NULL, NULL, NULL, NULL, NULL, false, 0, 'Perhepäivähoitaja C', 'Perhepäivähoitaja C', 'Varhaiskasvatusyksikön johtaja', 'Suokatu 10', 'Tiiliruukinkatu 1', '33200', 'TAMPERE', 'Tiiliruukinkatu 1', '(23.75582,61.49336)', NULL, '33200', 'TAMPERE', false, 'MUNICIPAL', 'fi', false, NULL, NULL, '{1,2,3,4,5}', NULL, NULL, NULL, NULL, NULL, false),
    ('Kerho A', '{CLUB}', (SELECT id FROM care_area WHERE name = 'Etelä'), NULL, NULL, NULL, NULL, '2021-04-20', NULL, NULL, NULL, NULL, NULL, NULL, false, 0, 'Kerho A', 'Kerho A', 'Varhaiskasvatusyksikön johtaja', 'Suokatu 10', 'Ollinojankatu 25', '33400', 'TAMPERE', 'Ollinojankatu 25', '(23.6701,61.525)', NULL, '33400', 'TAMPERE', false, 'MUNICIPAL', 'fi', false, NULL, NULL, '{1,2,3,4,5}', NULL, NULL, NULL, '[2021-04-20,)', NULL, false),
    ('Kerho B', '{CLUB}', (SELECT id FROM care_area WHERE name = 'Etelä'), NULL, NULL, NULL, NULL, '2021-04-20', NULL, NULL, NULL, NULL, NULL, NULL, false, 0, 'Kerho B', 'Kerho B', 'Varhaiskasvatusyksikön johtaja', 'Suokatu 10', 'Pispalan valtatie 79', '33270', 'TAMPERE', NULL, '(23.69093,61.50705)', NULL, '33270', 'TAMPERE', false, 'MUNICIPAL', 'fi', false, '', '', '{1,2,3,4,5}', false, NULL, NULL, '[2021-04-20,)', NULL, false),
    ('Kerho C', '{CLUB}', (SELECT id FROM care_area WHERE name = 'Itä'), NULL, NULL, NULL, NULL, '2021-04-20', NULL, NULL, NULL, NULL, NULL, NULL, false, 0, 'Kerho C', 'Kerho C', 'Varhaiskasvatusyksikön johtaja', 'Suokatu 10', 'Hämeenpuisto 10', '33210', 'TAMPERE', NULL, '(23.7493,61.50095)', NULL, '33210', 'TAMPERE', false, 'MUNICIPAL', 'fi', false, '', '', '{1,2,3,4,5}', false, NULL, NULL, '[2021-04-20,)', NULL, false),
    ('Koulupolun mukainen esiopetus', '{PRESCHOOL}', (SELECT id FROM care_area WHERE name = 'Länsi'), NULL, NULL, NULL, NULL, '2021-04-20', NULL, NULL, NULL, NULL, NULL, NULL, false, 0, '', '', '', '', 'Runkokatu 5', '33340', 'Tampere', 'Runkokatu 5', '(23.60571,61.51667)', NULL, '33340', 'Tampere', false, 'MUNICIPAL', 'fi', false, NULL, NULL, '{1,2,3,4,5}', NULL, NULL, '[2021-04-20,)', NULL, NULL, false),
    ('Koulu A', '{PRESCHOOL}', (SELECT id FROM care_area WHERE name = 'Etelä'), NULL, NULL, NULL, NULL, '2021-04-20', NULL, NULL, NULL, NULL, NULL, NULL, false, 0, 'Koulu A', 'Koulu A', 'Varhaiskasvatusyksikön johtaja', 'Suokatu 10', 'Runkokatu 5', '33340', 'Tampere', 'Runkokatu 5', '(23.60571,61.51667)', NULL, '33340', 'Tampere', false, 'MUNICIPAL', 'fi', false, NULL, NULL, '{1,2,3,4,5}', NULL, NULL, NULL, NULL, NULL, false),
    ('Koulu B', '{PRESCHOOL}', (SELECT id FROM care_area WHERE name = 'Etelä'), NULL, NULL, NULL, NULL, '2021-04-20', NULL, NULL, NULL, NULL, NULL, NULL, false, 0, 'Koulu B', 'Koulu B', 'Varhaiskasvatusyksikön johtaja', 'Suokatu 10', 'Runkokatu 5', '33340', 'Tampere', 'Runkokatu 5', '(23.60571,61.51667)', NULL, '33340', 'Tampere', false, 'MUNICIPAL', 'fi', false, NULL, NULL, '{1,2,3,4,5}', NULL, NULL, NULL, NULL, NULL, false),
    ('Koulu C', '{PRESCHOOL}', (SELECT id FROM care_area WHERE name = 'Itä'), NULL, NULL, NULL, NULL, '2021-04-20', NULL, NULL, NULL, NULL, NULL, NULL, false, 0, 'Koulu C', 'Koulu C', 'Varhaiskasvatusyksikön johtaja', 'Suokatu 10', 'Runkokatu 5', '33340', 'Tampere', 'Runkokatu 5', '(23.60571,61.51667)', NULL, '33340', 'Tampere', false, 'MUNICIPAL', 'fi', false, NULL, NULL, '{1,2,3,4,5}', NULL, NULL, NULL, NULL, NULL, false);

WITH new_unit_manager AS (
    INSERT INTO unit_manager (id, name, phone, email)
    SELECT id, 'UNIT_MANAGER_NAME', 'UNIT_MANAGER_PHONE', 'UNIT_MANAGER_EMAIL@tampere.fi'
    FROM daycare d WHERE unit_manager_id IS NULL
    RETURNING id
)
UPDATE daycare SET unit_manager_id = new_unit_manager.id FROM new_unit_manager WHERE daycare.id = new_unit_manager.id;

UPDATE daycare SET enabled_pilot_features = enum_range(null::pilot_feature);

INSERT INTO daycare_group (daycare_id, name, start_date, end_date)
SELECT id, 'Ryhmä ' || r, opening_date, COALESCE(closing_date, NULL)
FROM daycare CROSS JOIN generate_series(1, 3) AS r;

INSERT INTO daycare_caretaker (group_id, amount, start_date, end_date)
SELECT id, 3, start_date, end_date FROM daycare_group;

INSERT INTO message_account (daycare_group_id, type)
SELECT id, 'GROUP'::message_account_type FROM daycare_group;

INSERT INTO employee (first_name, last_name, email, roles) VALUES
    ('Päivi', 'Pääkäyttäjä', 'paivi.paakayttaja@tampere.fi', '{ADMIN, SERVICE_WORKER, FINANCE_ADMIN}'::user_role[]),
    ('Paula', 'Palveluohjaaja', 'paula.palveluohjaaja@tampere.fi', '{SERVICE_WORKER}'::user_role[]),
    ('Lasse', 'Laskuttaja', 'lasse.laskuttaja@tampere.fi', '{FINANCE_ADMIN}'::user_role[]),
    ('Raisa', 'Raportoija', 'raisa.raportoija@tampere.fi', '{REPORT_VIEWER}'::user_role[]),
    ('Harri', 'Hallinto', 'harri.hallinto@tampere.fi', '{DIRECTOR}'::user_role[]),
    ('Essi', 'Esimies', 'essi.esimies@tampere.fi', '{}'::user_role[]),
    ('Eemeli', 'Esimies', 'eemeli.esimies@tampere.fi', '{}'::user_role[]),
    ('Kaisa', 'Kasvattaja', 'kaisa.kasvattaja@tampere.fi', '{}'::user_role[]),
    ('Kalle', 'Kasvattaja', 'kalle.kasvattaja@tampere.fi', '{}'::user_role[]),
    ('Erkki', 'Erityisopettaja', 'erkki.erityisopettaja@tampere.fi', '{}'::user_role[]),
    ('Vallu', 'Varhaiskasvatussihteeri', 'vallu.varhaiskasvatussihteeri@tampere.fi', '{}'::user_role[]);
UPDATE employee SET external_id = 'tampere-ad:' || id WHERE external_id IS NULL;

INSERT INTO daycare_acl (daycare_id, employee_id, role) VALUES
    ((SELECT id FROM daycare WHERE name = 'Päiväkoti A'), (SELECT id FROM employee WHERE first_name = 'Essi'), 'UNIT_SUPERVISOR'),
    ((SELECT id FROM daycare WHERE name = 'Päiväkoti B'), (SELECT id FROM employee WHERE first_name = 'Essi'), 'UNIT_SUPERVISOR'),
    ((SELECT id FROM daycare WHERE name = 'Päiväkoti A'), (SELECT id FROM employee WHERE first_name = 'Eemeli'), 'UNIT_SUPERVISOR'),
    ((SELECT id FROM daycare WHERE name = 'Päiväkoti C'), (SELECT id FROM employee WHERE first_name = 'Eemeli'), 'UNIT_SUPERVISOR'),
    ((SELECT id FROM daycare WHERE name = 'Päiväkoti A'), (SELECT id FROM employee WHERE first_name = 'Kaisa'), 'STAFF'),
    ((SELECT id FROM daycare WHERE name = 'Päiväkoti B'), (SELECT id FROM employee WHERE first_name = 'Kaisa'), 'STAFF'),
    ((SELECT id FROM daycare WHERE name = 'Päiväkoti A'), (SELECT id FROM employee WHERE first_name = 'Kalle'), 'STAFF'),
    ((SELECT id FROM daycare WHERE name = 'Päiväkoti C'), (SELECT id FROM employee WHERE first_name = 'Kalle'), 'STAFF'),
    ((SELECT id FROM daycare WHERE name = 'Päiväkoti A'), (SELECT id FROM employee WHERE first_name = 'Erkki'), 'SPECIAL_EDUCATION_TEACHER'),
    ((SELECT id FROM daycare WHERE name = 'Päiväkoti B'), (SELECT id FROM employee WHERE first_name = 'Erkki'), 'SPECIAL_EDUCATION_TEACHER'),
    ((SELECT id FROM daycare WHERE name = 'Päiväkoti A'), (SELECT id FROM employee WHERE first_name = 'Vallu'), 'EARLY_CHILDHOOD_EDUCATION_SECRETARY'),
    ((SELECT id FROM daycare WHERE name = 'Päiväkoti B'), (SELECT id FROM employee WHERE first_name = 'Vallu'), 'EARLY_CHILDHOOD_EDUCATION_SECRETARY');

INSERT INTO message_account (employee_id, type)
SELECT DISTINCT employee_id, 'PERSONAL'::message_account_type FROM daycare_acl;

INSERT INTO fee_thresholds (
    valid_during,
    min_income_threshold_2, min_income_threshold_3, min_income_threshold_4, min_income_threshold_5, min_income_threshold_6,
    income_multiplier_2, income_multiplier_3, income_multiplier_4, income_multiplier_5, income_multiplier_6,
    max_income_threshold_2, max_income_threshold_3, max_income_threshold_4, max_income_threshold_5, max_income_threshold_6,
    income_threshold_increase_6_plus,
    sibling_discount_2, sibling_discount_2_plus,
    max_fee, min_fee,
    temporary_fee, temporary_fee_part_day, temporary_fee_sibling, temporary_fee_sibling_part_day
) VALUES (
    daterange('2000-01-01', '2020-07-31', '[]'),
    210200, 271300, 308000, 344700, 381300,
    0.1070, 0.1070, 0.1070, 0.1070, 0.1070,
    479900, 541000, 577700, 614400, 651000,
    14200,
    0.5, 0.8,
    28900, 2700,
    2900, 1500, 1500, 800
),
(
    daterange('2020-08-01', NULL),
    213600, 275600, 312900, 350200, 387400,
    0.1070, 0.1070, 0.1070, 0.1070, 0.1070,
    482300, 544300, 581600, 618900, 656100,
    14200,
    0.5, 0.8,
    28800, 2700,
    2900, 1500, 1500, 800
);
