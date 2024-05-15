-- SPDX-FileCopyrightText: 2023-2024 Tampere region
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

INSERT INTO service_need_option
    (id, name_fi, name_sv, name_en, valid_placement_type, default_option, fee_coefficient, occupancy_coefficient, occupancy_coefficient_under_3y, realized_occupancy_coefficient, realized_occupancy_coefficient_under_3y, daycare_hours_per_week, contract_days_per_month, daycare_hours_per_month, part_day, part_week, fee_description_fi, fee_description_sv, voucher_value_description_fi, voucher_value_description_sv, valid_from, valid_to, show_for_citizen, display_order)
VALUES
    ('4b07f3ec-503c-4493-bef2-c5be84fc92b3', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Daycare - 85 h per month', 'DAYCARE', TRUE, 0.5, 1.0, 1.75, 1.0, 1.75, 40, NULL, 85, TRUE, TRUE, 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', '2023-08-01', NULL, TRUE, NULL),
    ('489dcf01-e11a-4ab8-8c36-1e672581eb6d', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Daycare - 85 h per month', 'DAYCARE', FALSE, 0.5, 1.0, 1.75, 1.0, 1.75, 40, NULL, 85, TRUE, TRUE, 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 0),
    ('f24a1b37-0be9-4004-8a00-eefda8ed925a', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Daycare - 86-120 h per month', 'DAYCARE', FALSE, 0.8, 1.0, 1.75, 1.0, 1.75, 40, NULL, 120, TRUE, TRUE, 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 1),
    ('a92cf108-0939-45f5-8976-ab31e456b84d', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Daycare - 121-150 h per month', 'DAYCARE', FALSE, 0.9, 1.0, 1.75, 1.0, 1.75, 40, NULL, 150, TRUE, TRUE, 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 2),
    ('22adbfd9-50d8-4192-9a35-1c5ce872e1a7', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Daycare - over 151 h per month', 'DAYCARE', FALSE, 1.0, 1.0, 1.75, 1.0, 1.75, 40, NULL, 210, FALSE, FALSE, 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 3),
    ('6e0ae089-8e18-4fac-a10b-8c5ac2f875b2', 'Palveluseteli 85 tuntia / kuukausi', 'Palveluseteli 85 tuntia / kuukausi', 'Voucher value - 85 h per month', 'DAYCARE', FALSE, 0.5, 1.0, 1.75, 1.0, 1.75, 40, NULL, 85, TRUE, TRUE, 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', '2023-08-01', '2024-07-31', TRUE, 4),
    ('15ec591b-9dc7-42da-8cae-1a01ac36c62f', 'Palveluseteli 86–120 tuntia / kuukausi', 'Palveluseteli 86–120 tuntia / kuukausi', 'Voucher value - 86-120 h per month', 'DAYCARE', FALSE, 0.7, 1.0, 1.75, 1.0, 1.75, 40, NULL, 120, TRUE, TRUE, 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', '2023-08-01', '2024-07-31', TRUE, 5),
    ('27f8b3df-ebf8-4e39-aed0-902c1ad13763', 'Palveluseteli 121-150 tuntia / kuukausi', 'Palveluseteli 121-150 tuntia / kuukausi', 'Voucher value - 121-150 h per month', 'DAYCARE', FALSE, 0.85, 1.0, 1.75, 1.0, 1.75, 40, NULL, 150, TRUE, TRUE, 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', '2023-08-01', '2024-07-31', TRUE, 6),
    ('ac8f67e0-745d-49bc-a0ed-9295e45a0774', 'Palveluseteli yli 151 tuntia / kuukausi', 'Palveluseteli yli 151 tuntia / kuukausi', 'Voucher value - over 151 h per month', 'DAYCARE', FALSE, 1.0, 1.0, 1.75, 1.0, 1.75, 40, NULL, 210, FALSE, FALSE, 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', '2023-08-01', '2024-07-31', TRUE, 7),
    ('e945728f-2671-4158-8941-1d39a3a36dce', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Temporary daycare', 'TEMPORARY_DAYCARE', TRUE, 1.0, 1.0, 1.75, 1.0, 1.75, 40, NULL, NULL, FALSE, FALSE, 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', '2023-08-01', NULL, TRUE, NULL)
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
    valid_to = EXCLUDED.valid_to,
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
    service_need_option.valid_to IS DISTINCT FROM EXCLUDED.valid_to OR
    service_need_option.show_for_citizen <> EXCLUDED.show_for_citizen OR
    service_need_option.display_order <> EXCLUDED.display_order;

INSERT INTO service_need_option_voucher_value
(id, service_need_option_id, validity, base_value, coefficient, value, base_value_under_3y, coefficient_under_3y, value_under_3y)
VALUES
    -- 2023-08-01 - 2024-07-31
    ('94347253-ba0a-472f-a33a-a628ce5d727c', '4b07f3ec-503c-4493-bef2-c5be84fc92b3', daterange('2023-08-01', '2024-07-31', '[]'), 90100, 0.50, 45050, 140600, 0.50, 70300),
    ('abb09e4d-317b-426a-ae0f-c906e2c0dafb', '489dcf01-e11a-4ab8-8c36-1e672581eb6d', daterange('2023-08-01', '2024-07-31', '[]'), 90100, 0.50, 45050, 140600, 0.50, 70300),
    ('87c4588b-e786-433e-9a51-27213d193e7b', 'f24a1b37-0be9-4004-8a00-eefda8ed925a', daterange('2023-08-01', '2024-07-31', '[]'), 90100, 0.80, 72080, 140600, 0.80, 112480),
    ('85772d47-a7ef-4dd8-ad69-10b1353227aa', 'a92cf108-0939-45f5-8976-ab31e456b84d', daterange('2023-08-01', '2024-07-31', '[]'), 90100, 1.00, 90100, 140600, 1.00, 140600),
    ('2343782f-25f3-49ca-b0fb-e6f2b50ecc0c', '22adbfd9-50d8-4192-9a35-1c5ce872e1a7', daterange('2023-08-01', '2024-07-31', '[]'), 90100, 1.00, 90100, 140600, 1.00, 140600),
    ('1766d216-8fae-4e9f-a0f3-67aebf42ecbd', '6e0ae089-8e18-4fac-a10b-8c5ac2f875b2', daterange('2023-08-01', NULL, '[]'), 90100, 0.50, 45050, 140600, 0.50, 70300),
    ('5a0e2949-b2dd-4062-b129-895910f92751', '15ec591b-9dc7-42da-8cae-1a01ac36c62f', daterange('2023-08-01', NULL, '[]'), 90100, 0.80, 72080, 140600, 0.80, 112480),
    ('523ebcbe-edc9-42a5-8220-c2dae2eecf78', '27f8b3df-ebf8-4e39-aed0-902c1ad13763', daterange('2023-08-01', NULL, '[]'), 90100, 1.00, 90100, 140600, 1.00, 140600),
    ('284f8290-ac7d-4474-b97f-9243283e4c2c', 'ac8f67e0-745d-49bc-a0ed-9295e45a0774', daterange('2023-08-01', NULL, '[]'), 90100, 1.00, 90100, 140600, 1.00, 140600),
    -- 2024-08-01 -
    ('3ffe03ff-4d3d-4c03-93b4-d0e7b0b481b1', '4b07f3ec-503c-4493-bef2-c5be84fc92b3', daterange('2024-08-01', NULL, '[]'), 94900, 0.50, 47450, 148000, 0.50, 74000),
    ('5bbebcd6-8183-4c85-8c22-4dc8e4ecf592', '489dcf01-e11a-4ab8-8c36-1e672581eb6d', daterange('2024-08-01', NULL, '[]'), 94900, 0.50, 47450, 148000, 0.50, 74000),
    ('91405240-966e-48bc-b0c7-780b7b9ad5fe', 'f24a1b37-0be9-4004-8a00-eefda8ed925a', daterange('2024-08-01', NULL, '[]'), 94900, 0.80, 75920, 148000, 0.80, 118400),
    ('2fd9f3e6-8e5f-429d-a8ca-15154d0931dc', 'a92cf108-0939-45f5-8976-ab31e456b84d', daterange('2024-08-01', NULL, '[]'), 94900, 1.00, 94900, 148000, 1.00, 148000),
    ('343fe9b8-0dfa-4962-b2e7-ed5540cfdfea', '22adbfd9-50d8-4192-9a35-1c5ce872e1a7', daterange('2024-08-01', NULL, '[]'), 94900, 1.00, 94900, 148000, 1.00, 148000)
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
