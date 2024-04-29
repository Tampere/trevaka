-- SPDX-FileCopyrightText: 2023-2024 Tampere region
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

INSERT INTO service_need_option
    (id, name_fi, name_sv, name_en, valid_placement_type, default_option, fee_coefficient, occupancy_coefficient, occupancy_coefficient_under_3y, realized_occupancy_coefficient, realized_occupancy_coefficient_under_3y, daycare_hours_per_week, contract_days_per_month, daycare_hours_per_month, part_day, part_week, fee_description_fi, fee_description_sv, voucher_value_description_fi, voucher_value_description_sv, valid_from, show_for_citizen, display_order)
VALUES
    ('4b07f3ec-503c-4493-bef2-c5be84fc92b3', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Daycare - 85 h per month', 'DAYCARE', TRUE, 0.5, 1.0, 1.75, 1.0, 1.75, 40, NULL, 85, TRUE, TRUE, 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', '2023-08-01', TRUE, NULL),
    ('489dcf01-e11a-4ab8-8c36-1e672581eb6d', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Daycare - 85 h per month', 'DAYCARE', FALSE, 0.5, 1.0, 1.75, 1.0, 1.75, 40, NULL, 85, TRUE, TRUE, 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', '2023-08-01', TRUE, 0),
    ('f24a1b37-0be9-4004-8a00-eefda8ed925a', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Daycare - 86-120 h per month', 'DAYCARE', FALSE, 0.8, 1.0, 1.75, 1.0, 1.75, 40, NULL, 120, TRUE, TRUE, 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', '2023-08-01', TRUE, 1),
    ('a92cf108-0939-45f5-8976-ab31e456b84d', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Daycare - 121-150 h per month', 'DAYCARE', FALSE, 0.9, 1.0, 1.75, 1.0, 1.75, 40, NULL, 150, TRUE, TRUE, 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', '2023-08-01', TRUE, 2),
    ('22adbfd9-50d8-4192-9a35-1c5ce872e1a7', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Daycare - over 151 h per month', 'DAYCARE', FALSE, 1.0, 1.0, 1.75, 1.0, 1.75, 40, NULL, 210, FALSE, FALSE, 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', '2023-08-01', TRUE, 3),
    ('18a83b84-51e5-49a3-9bc7-52fd7563944a', 'Palveluseteli 85 tuntia / kuukausi', 'Palveluseteli 85 tuntia / kuukausi', 'Voucher value - 85 h per month', 'DAYCARE', FALSE, 0.5, 1.0, 1.75, 1.0, 1.75, 40, NULL, 85, TRUE, TRUE, 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', '2023-08-01', TRUE, 4),
    ('6a08d6ae-4580-4d70-b303-179be13e77f8', 'Palveluseteli 86–120 tuntia / kuukausi', 'Palveluseteli 86–120 tuntia / kuukausi', 'Voucher value - 86-120 h per month', 'DAYCARE', FALSE, 0.75, 1.0, 1.75, 1.0, 1.75, 40, NULL, 120, TRUE, TRUE, 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', '2023-08-01', TRUE, 5),
    ('2bf66308-3bf3-442b-81c4-cdc41b5a7fcd', 'Palveluseteli 121-150 tuntia / kuukausi', 'Palveluseteli 121-150 tuntia / kuukausi', 'Voucher value - 121-150 h per month', 'DAYCARE', FALSE, 0.85, 1.0, 1.75, 1.0, 1.75, 40, NULL, 150, TRUE, TRUE, 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', '2023-08-01', TRUE, 6),
    ('dad9361a-1400-4cf0-8ade-f8772f2a299d', 'Palveluseteli yli 151 tuntia / kuukausi', 'Palveluseteli yli 151 tuntia / kuukausi', 'Voucher value - over 151 h per month', 'DAYCARE', FALSE, 1.0, 1.0, 1.75, 1.0, 1.75, 40, NULL, 210, FALSE, FALSE, 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', '2023-08-01', TRUE, 7),
    ('e945728f-2671-4158-8941-1d39a3a36dce', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Temporary daycare', 'TEMPORARY_DAYCARE', TRUE, 1.0, 1.0, 1.75, 1.0, 1.75, 40, NULL, NULL, FALSE, FALSE, 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', '2023-08-01', TRUE, NULL),
    ('e067df64-7ff0-42c1-a409-537db7c202dd', 'Esiopetusta täydentävä varhaiskasvatus', 'Esiopetusta täydentävä varhaiskasvatus', 'Preschool daycare', 'PRESCHOOL_DAYCARE_ONLY', TRUE, 0.6, 1.0, 1.0, 1.0, 1.0, 40, NULL, 210, FALSE, FALSE, 'Esiopetusta täydentävä varhaiskasvatus', 'Esiopetusta täydentävä varhaiskasvatus', 'Esiopetusta täydentävä varhaiskasvatus', 'Esiopetusta täydentävä varhaiskasvatus', '2023-08-01', FALSE, NULL),
    ('ff6ddcd4-fa8a-11eb-8592-2f2b4e398fcb', 'Kerho', 'Kerho', 'Club', 'CLUB', TRUE, 0.0, 1.0, 1.0, 1.0, 1.0, 0, NULL, NULL, TRUE, TRUE, 'Kerho', 'Kerho', 'Kerho', 'Kerho', '2023-08-01', TRUE, NULL)
ON CONFLICT (id) DO
UPDATE SET
    name_fi = EXCLUDED.name_fi,
    name_sv = EXCLUDED.name_sv,
    name_en = EXCLUDED.name_en,
    valid_placement_type = EXCLUDED.valid_placement_type,
    default_option = EXCLUDED.default_option,
    fee_coefficient = EXCLUDED.fee_coefficient,
    occupancy_coefficient = EXCLUDED.occupancy_coefficient,
    occupancy_coefficient_under_3y = EXCLUDED.occupancy_coefficient_under_3y,
    realized_occupancy_coefficient = EXCLUDED.realized_occupancy_coefficient,
    realized_occupancy_coefficient_under_3y = EXCLUDED.realized_occupancy_coefficient_under_3y,
    daycare_hours_per_week = EXCLUDED.daycare_hours_per_week,
    contract_days_per_month = EXCLUDED.contract_days_per_month,
    daycare_hours_per_month = EXCLUDED.daycare_hours_per_month,
    part_day = EXCLUDED.part_day,
    part_week = EXCLUDED.part_week,
    fee_description_fi = EXCLUDED.fee_description_fi,
    fee_description_sv = EXCLUDED.fee_description_sv,
    voucher_value_description_fi = EXCLUDED.voucher_value_description_fi,
    voucher_value_description_sv = EXCLUDED.voucher_value_description_sv,
    valid_from = EXCLUDED.valid_from,
    show_for_citizen = EXCLUDED.show_for_citizen,
    display_order = EXCLUDED.display_order
WHERE
    service_need_option.name_fi <> EXCLUDED.name_fi OR
    service_need_option.name_sv <> EXCLUDED.name_sv OR
    service_need_option.name_en <> EXCLUDED.name_en OR
    service_need_option.valid_placement_type <> EXCLUDED.valid_placement_type OR
    service_need_option.default_option <> EXCLUDED.default_option OR
    service_need_option.fee_coefficient <> EXCLUDED.fee_coefficient OR
    service_need_option.occupancy_coefficient <> EXCLUDED.occupancy_coefficient OR
    service_need_option.occupancy_coefficient_under_3y <> EXCLUDED.occupancy_coefficient_under_3y OR
    service_need_option.realized_occupancy_coefficient <> EXCLUDED.realized_occupancy_coefficient OR
    service_need_option.realized_occupancy_coefficient_under_3y <> EXCLUDED.realized_occupancy_coefficient_under_3y OR
    service_need_option.daycare_hours_per_week <> EXCLUDED.daycare_hours_per_week OR
    service_need_option.contract_days_per_month IS DISTINCT FROM EXCLUDED.contract_days_per_month OR
    service_need_option.daycare_hours_per_month IS DISTINCT FROM EXCLUDED.daycare_hours_per_month OR
    service_need_option.part_day <> EXCLUDED.part_day OR
    service_need_option.part_week <> EXCLUDED.part_week OR
    service_need_option.fee_description_fi <> EXCLUDED.fee_description_fi OR
    service_need_option.fee_description_sv <> EXCLUDED.fee_description_sv OR
    service_need_option.voucher_value_description_fi <> EXCLUDED.voucher_value_description_fi OR
    service_need_option.voucher_value_description_sv <> EXCLUDED.voucher_value_description_sv OR
    service_need_option.valid_from <> EXCLUDED.valid_from OR
    service_need_option.show_for_citizen <> EXCLUDED.show_for_citizen OR
    service_need_option.display_order <> EXCLUDED.display_order;

INSERT INTO service_need_option_voucher_value
(id, service_need_option_id, validity, base_value, coefficient, value, base_value_under_3y, coefficient_under_3y, value_under_3y)
VALUES
    -- 2023-08-01 -
    ('94347253-ba0a-472f-a33a-a628ce5d727c', '4b07f3ec-503c-4493-bef2-c5be84fc92b3', daterange('2023-08-01', NULL, '[]'), 90100, 0.60, 54060, 140600, 0.60, 84360),
    ('abb09e4d-317b-426a-ae0f-c906e2c0dafb', '489dcf01-e11a-4ab8-8c36-1e672581eb6d', daterange('2023-08-01', NULL, '[]'), 90100, 0.60, 54060, 140600, 0.60, 84360),
    ('87c4588b-e786-433e-9a51-27213d193e7b', 'f24a1b37-0be9-4004-8a00-eefda8ed925a', daterange('2023-08-01', NULL, '[]'), 90100, 0.80, 72080, 140600, 0.80, 112480),
    ('85772d47-a7ef-4dd8-ad69-10b1353227aa', 'a92cf108-0939-45f5-8976-ab31e456b84d', daterange('2023-08-01', NULL, '[]'), 90100, 1.00, 90100, 140600, 1.00, 140600),
    ('2343782f-25f3-49ca-b0fb-e6f2b50ecc0c', '22adbfd9-50d8-4192-9a35-1c5ce872e1a7', daterange('2023-08-01', NULL, '[]'), 90100, 1.00, 90100, 140600, 1.00, 140600),
    ('0f1ed4c1-6b7d-404c-8871-6925f74d087a', '18a83b84-51e5-49a3-9bc7-52fd7563944a', daterange('2023-08-01', NULL, '[]'), 90100, 0.60, 54060, 140600, 0.60, 84360),
    ('706da296-4b42-4a28-88f2-0d2b298169d2', '6a08d6ae-4580-4d70-b303-179be13e77f8', daterange('2023-08-01', NULL, '[]'), 90100, 0.80, 72080, 140600, 0.80, 112480),
    ('add03833-cbdc-4529-b167-3fb1fda337a1', '2bf66308-3bf3-442b-81c4-cdc41b5a7fcd', daterange('2023-08-01', NULL, '[]'), 90100, 1.00, 90100, 140600, 1.00, 140600),
    ('d2570626-0fce-4106-b93b-ab930f45b71e', 'dad9361a-1400-4cf0-8ade-f8772f2a299d', daterange('2023-08-01', NULL, '[]'), 90100, 1.00, 90100, 140600, 1.00, 140600),
    ('662570db-c149-45b9-a52f-2748b8995e19', 'e067df64-7ff0-42c1-a409-537db7c202dd', daterange('2023-08-01', NULL, '[]'), 90100, 0.50, 45050, 140600, 0.50, 70300)
ON CONFLICT (id) DO
    UPDATE SET
               service_need_option_id = EXCLUDED.service_need_option_id,
               validity = EXCLUDED.validity,
               base_value = EXCLUDED.base_value,
               coefficient = EXCLUDED.coefficient,
               value = EXCLUDED.value,
               base_value_under_3y = EXCLUDED.base_value_under_3y,
               coefficient_under_3y = EXCLUDED.coefficient_under_3y,
               value_under_3y = EXCLUDED.value_under_3y
WHERE
    service_need_option_voucher_value.service_need_option_id <> EXCLUDED.service_need_option_id OR
    service_need_option_voucher_value.validity <> EXCLUDED.validity OR
    service_need_option_voucher_value.base_value <> EXCLUDED.base_value OR
    service_need_option_voucher_value.coefficient <> EXCLUDED.coefficient OR
    service_need_option_voucher_value.value <> EXCLUDED.value OR
    service_need_option_voucher_value.base_value_under_3y <> EXCLUDED.base_value_under_3y OR
    service_need_option_voucher_value.coefficient_under_3y <> EXCLUDED.coefficient_under_3y OR
    service_need_option_voucher_value.value_under_3y <> EXCLUDED.value_under_3y;

DELETE
FROM service_need_option
WHERE id IN (
             'acfb9f35-efff-40d9-8838-4a021cfc7446',
             'a6dcce89-632c-4bb3-a3ba-1b3c6b570348',
             'b0f13ba8-ff8a-4bb8-88b7-b260aefe8e0e',
             'd5a5824b-ae3f-4cd3-9cd6-affee036bd1d',
             '8d155bac-8be4-4988-9835-b99789ba51e4',
             '347a19fc-26a3-44c2-85a5-37ac8a56ae57',
             '5a574d58-7d6f-4eb3-b91f-23ea7094e0de',
             'f04ff070-35d7-4e2f-82ba-f130ef150c6d',
             'c8e7bb24-584b-4624-865b-0cab145edfab',
             'cf8b946d-45e6-4049-9439-ad0814f22592',
             '131d5fbe-a72d-4f6e-82c0-5df88c657690'
    );

UPDATE service_need
SET option_id = new_id::uuid
FROM placement,
     daycare unit,
     (VALUES ('489dcf01-e11a-4ab8-8c36-1e672581eb6d', '18a83b84-51e5-49a3-9bc7-52fd7563944a'),
             ('f24a1b37-0be9-4004-8a00-eefda8ed925a', '6a08d6ae-4580-4d70-b303-179be13e77f8'),
             ('a92cf108-0939-45f5-8976-ab31e456b84d', '2bf66308-3bf3-442b-81c4-cdc41b5a7fcd'),
             ('22adbfd9-50d8-4192-9a35-1c5ce872e1a7', 'dad9361a-1400-4cf0-8ade-f8772f2a299d')) AS map(old_id, new_id)
WHERE service_need.placement_id = placement.id
  AND placement.unit_id = unit.id
  AND unit.provider_type = 'PRIVATE_SERVICE_VOUCHER'
  AND service_need.option_id = map.old_id::uuid;
