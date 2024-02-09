-- SPDX-FileCopyrightText: 2024 Tampere region
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

UPDATE placement SET type = 'PRESCHOOL_DAYCARE_ONLY' WHERE type = 'DAYCARE_PART_TIME';

INSERT INTO service_need_option
(id, name_fi, name_sv, name_en, valid_placement_type, default_option, fee_coefficient, occupancy_coefficient, occupancy_coefficient_under_3y, realized_occupancy_coefficient, realized_occupancy_coefficient_under_3y, daycare_hours_per_week, contract_days_per_month, daycare_hours_per_month, part_day, part_week, fee_description_fi, fee_description_sv, voucher_value_description_fi, voucher_value_description_sv, active, show_for_citizen, display_order)
VALUES
    ('50358394-b961-11eb-b51f-67ac436e5637', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Daycare - 85 h per month', 'DAYCARE', FALSE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, 85, TRUE, TRUE, 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', TRUE, TRUE, 0),
    ('86ef70a0-bf85-11eb-91e6-1fb57a101165', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Daycare - 86-120 h per month', 'DAYCARE', FALSE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, 120, TRUE, TRUE, 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', TRUE, TRUE, 1),
    ('503590f0-b961-11eb-b520-53740af3f7ef', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Daycare - 121-150 h per month', 'DAYCARE', FALSE, 1.0, 1.0, 1., 1.0, 1.0, 40, NULL, 150, TRUE, TRUE, 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', TRUE, TRUE, 2),
    ('503591ae-b961-11eb-b521-1fca99358eed', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Daycare - over 151 h per month', 'DAYCARE', FALSE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, 210, FALSE, FALSE, 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', TRUE, TRUE, 3),
    ('1c5d7ea4-669f-4b4e-8593-353be4c9cea0', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Preschool daycare - 0–50 h per month', 'PRESCHOOL_DAYCARE', FALSE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, 50, TRUE, TRUE, 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', TRUE, TRUE, 4),
    ('9e6a4660-2f83-40e3-bf27-d9590e93dbf2', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Preschool daycare – 51-85 h per month', 'PRESCHOOL_DAYCARE', FALSE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, 85, TRUE, TRUE, 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', TRUE, TRUE, 5),
    ('3b94630b-e01e-4b61-b040-8910baf96e97', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Preschool daycare – 68-120 h per month', 'PRESCHOOL_DAYCARE', FALSE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, 120, TRUE, TRUE, 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', TRUE, TRUE, 6),
    ('593d8cbf-fcf8-41a7-a5fa-8fe96a7b93d8', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Preschool daycare – 121-150 h per month', 'PRESCHOOL_DAYCARE', FALSE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, 150, TRUE, TRUE, 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', TRUE, TRUE, 7),
    ('c169bce7-6533-4409-8acd-445061f1ff34', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Preschool daycare – over 151 h per month', 'PRESCHOOL_DAYCARE', FALSE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, 210, FALSE, FALSE, 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', TRUE, TRUE, 8),
    ('0a58d934-6fd1-11ed-a75e-c353faef5858', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Preschool daycare - 0–50 h per month', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, 50, TRUE, TRUE, 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', TRUE, TRUE, 4),
    ('0a58db0a-6fd1-11ed-a75e-bbde95c1aded', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Preschool daycare – 51-85 h per month', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, 85, TRUE, TRUE, 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', TRUE, TRUE, 5),
    ('0a58dbe6-6fd1-11ed-a75e-5335f2b9a91c', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Preschool daycare – 68-120 h per month', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, 120, TRUE, TRUE, 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', TRUE, TRUE, 6),
    ('0a58da38-6fd1-11ed-a75e-9b2790b0b4f5', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Preschool daycare – 121-150 h per month', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, 150, TRUE, TRUE, 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', TRUE, TRUE, 7),
    ('0a58dcae-6fd1-11ed-a75e-b3e10433b949', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Preschool daycare – over 151 h per month', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, 210, FALSE, FALSE, 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', TRUE, TRUE, 8)
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
               active = EXCLUDED.active,
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
    service_need_option.active <> EXCLUDED.active OR
    service_need_option.show_for_citizen <> EXCLUDED.show_for_citizen OR
    service_need_option.display_order <> EXCLUDED.display_order;
