-- SPDX-FileCopyrightText: 2021 City of Tampere
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

INSERT INTO service_need_option
    (id, name_fi, name_sv, name_en, valid_placement_type, default_option, fee_coefficient, occupancy_coefficient, occupancy_coefficient_under_3y, realized_occupancy_coefficient, realized_occupancy_coefficient_under_3y, daycare_hours_per_week, contract_days_per_month, part_day, part_week, fee_description_fi, fee_description_sv, voucher_value_description_fi, voucher_value_description_sv, active, show_for_citizen, display_order)
VALUES
    ('50358394-b961-11eb-b51f-67ac436e5636', 'Kokopäiväinen', 'Kokopäiväinen', 'Full-time', 'DAYCARE', TRUE, 1.0, 1.0, 1.75, 1.0, 1.75, 40, NULL, FALSE, FALSE, 'Kokopäiväinen', 'Kokopäiväinen', 'Kokopäiväinen', 'Kokopäiväinen', TRUE, TRUE, null),
    ('86ef70a0-bf85-11eb-91e6-1fb57a101161', 'Kokopäiväinen', 'Kokopäiväinen', 'Full-time', 'DAYCARE', FALSE, 1.0, 1.0, 1.75, 1.0, 1.75, 40, NULL, FALSE, FALSE, 'Kokopäiväinen', 'Kokopäiväinen', 'Kokopäiväinen', 'Kokopäiväinen', TRUE, TRUE, 0),
    ('503590f0-b961-11eb-b520-53740af3f7ee', 'Kokopäiväinen, 10 pv sopimus', 'Kokopäiväinen, 10 pv sopimus', 'Full-time, 10 day agreement', 'DAYCARE', FALSE, 0.5, 0.5, 0.88, 0.5, 1.75, 20, 10, FALSE, TRUE, 'Kokopäiväinen, 10 pv sopimus', 'Kokopäiväinen, 10 pv sopimus', 'Kokopäiväinen, 10 pv sopimus', 'Kokopäiväinen, 10 pv sopimus', TRUE, TRUE, 1),
    ('503591ae-b961-11eb-b521-1fca99358eef', 'Kokopäiväinen, 15 pv sopimus', 'Kokopäiväinen, 15 pv sopimus', 'Full-time, 15 day agreement', 'DAYCARE', FALSE, 0.75, 1.0, 1.75, 1.0, 1.75, 30, 15, FALSE, TRUE, 'Kokopäiväinen, 15 pv sopimus', 'Kokopäiväinen, 15 pv sopimus', 'Kokopäiväinen, 15 pv sopimus', 'Kokopäiväinen, 15 pv sopimus', TRUE, TRUE, 2),
    ('50359212-b961-11eb-b522-074fb05f7086', 'Osapäiväinen, max 5h päivässä', 'Osapäiväinen, max 5h päivässä', 'Part-time, max 5h per day', 'DAYCARE_PART_TIME', TRUE, 0.6, 0.5, 1.75, 0.5, 1.75, 25, NULL, TRUE, FALSE, 'Osapäiväinen, max 5h päivässä', 'Osapäiväinen, max 5h päivässä', 'Osapäiväinen, max 5h päivässä', 'Osapäiväinen, max 5h päivässä', TRUE, TRUE, null),
    ('86ef7370-bf85-11eb-91e7-6fcd728c518d', 'Osapäiväinen, max 5h päivässä', 'Osapäiväinen, max 5h päivässä', 'Part-time, max 5h per day', 'DAYCARE_PART_TIME', FALSE, 0.6, 0.5, 1.75, 0.5, 1.75, 25, NULL, TRUE, FALSE, 'Osapäiväinen, max 5h päivässä', 'Osapäiväinen, max 5h päivässä', 'Osapäiväinen, max 5h päivässä', 'Osapäiväinen, max 5h päivässä', TRUE, TRUE, 1001),
    ('50359280-b961-11eb-b523-237115533645', 'Osapäiväinen, max 5h päivässä; 10 pv sopimus', 'Osapäiväinen, max 5h päivässä; 10 pv sopimus', 'Part-time, max 5h per day; 10 day agreement', 'DAYCARE_PART_TIME', FALSE, 0.3, 0.25, 0.88, 0.50, 1.75, 12, 10, TRUE, TRUE, 'Osapäiväinen, max 5h päivässä; 10 pv sopimus', 'Osapäiväinen, max 5h päivässä; 10 pv sopimus', 'Osapäiväinen, max 5h päivässä; 10 pv sopimus', 'Osapäiväinen, max 5h päivässä; 10 pv sopimus', TRUE, TRUE, 1002),
    ('503592da-b961-11eb-b524-7f27c780d83a', 'Osapäiväinen, max 5h päivässä; 15 pv sopimus', 'Osapäiväinen, max 5h päivässä; 15 pv sopimus', 'Part-time, max 5h per day; 15 day agreement', 'DAYCARE_PART_TIME', FALSE, 0.45, 0.5, 1.75, 0.5, 1.75, 18, 15, TRUE, TRUE, 'Osapäiväinen, max 5h päivässä; 15 pv sopimus', 'Osapäiväinen, max 5h päivässä; 15 pv sopimus', 'Osapäiväinen, max 5h päivässä; 15 pv sopimus', 'Osapäiväinen, max 5h päivässä; 15 pv sopimus', TRUE, TRUE, 1003),
    ('50359334-b961-11eb-b525-f3febdfea5d3', 'Osapäiväinen, max 20h viikossa', 'Osapäiväinen, max 20h viikossa', 'Part-time, max 20h per week', 'DAYCARE_PART_TIME', FALSE, 0.5, 0.5, 0.88, 0.5, 1.75, 20, NULL, TRUE, FALSE, 'Osapäiväinen, max 20h viikossa', 'Osapäiväinen, max 20h viikossa', 'Osapäiväinen, max 20h viikossa', 'Osapäiväinen, max 20h viikossa', TRUE, TRUE, 1000),
    ('5035938e-b961-11eb-b526-6b30323c87a8', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Temporary daycare', 'TEMPORARY_DAYCARE', TRUE, 1.0, 1.0, 1.75, 1.0, 1.75, 40, NULL, FALSE, FALSE, 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', TRUE, TRUE, null),
    ('0bfc6c92-ff2a-11eb-a785-2724e8e5e7ee', 'Esiopetusta täydentävä varhaiskasvatus - max 5 h päivässä', 'Esiopetusta täydentävä varhaiskasvatus - max 5 h päivässä', 'Preschool daycare - max 5 h per day', 'PRESCHOOL_DAYCARE', TRUE, 0.6, 0.5, 0.5, 0.5, 0.5, 25, NULL, TRUE, FALSE, 'Esiopetusta täydentävä varhaiskasvatus - max 5 h päivässä', 'Esiopetusta täydentävä varhaiskasvatus - max 5 h päivässä', 'Esiopetusta täydentävä varhaiskasvatus - max 5 h päivässä', 'Esiopetusta täydentävä varhaiskasvatus - max 5 h päivässä', FALSE, FALSE, null),
    ('503593e8-b961-11eb-b527-a3dcdfb628ec', 'Esiopetusta täydentävä varhaiskasvatus - max 5 h päivässä', 'Esiopetusta täydentävä varhaiskasvatus - max 5 h päivässä', 'Preschool daycare - max 5 h per day', 'PRESCHOOL_DAYCARE', FALSE, 0.6, 0.5, 0.5, 0.5, 0.5, 25, NULL, TRUE, FALSE, 'Esiopetusta täydentävä varhaiskasvatus - max 5 h päivässä', 'Esiopetusta täydentävä varhaiskasvatus - max 5 h päivässä', 'Esiopetusta täydentävä varhaiskasvatus - max 5 h päivässä', 'Esiopetusta täydentävä varhaiskasvatus - max 5 h päivässä', FALSE, FALSE, 2000),
    ('50359442-b961-11eb-b528-df3290c0d63e', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä', 'Preschool daycare – over 5 h per day', 'PRESCHOOL_DAYCARE', FALSE, 0.8, 0.5, 0.5, 0.5, 0.5, 32, NULL, FALSE, FALSE, 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä', FALSE, FALSE, 2100),
    ('bc6a42d0-fa74-11eb-9a2b-d315a7916074', 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 10 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 10 pv sopimus', 'Preschool daycare – max 5 h per day; 10 day agreement', 'PRESCHOOL_DAYCARE', FALSE, 0.3, 0.25, 0.25, 0.5, 0.5, 12, 10, TRUE, TRUE, 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 10 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 10 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 10 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 10 pv sopimus', FALSE, FALSE, 2200),
    ('bc6a44ec-fa74-11eb-9a2c-73b53c2af869', 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 15 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 15 pv sopimus', 'Preschool daycare – max 5 h per day; 15 day agreement', 'PRESCHOOL_DAYCARE', FALSE, 0.45, 0.5, 0.5, 0.5, 0.5, 18, 15, TRUE, TRUE, 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 15 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 15 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 15 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 15 pv sopimus', FALSE, FALSE, 2300),
    ('bc6a4550-fa74-11eb-9a2d-035acd5db9aa', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 10 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 10 pv sopimus', 'Preschool daycare – over 5 h per day; 10 day agreement', 'PRESCHOOL_DAYCARE', FALSE, 0.4, 0.25, 0.25, 0.5, 0.5, 18, 10, FALSE, TRUE, 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 10 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 10 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 10 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 10 pv sopimus', FALSE, FALSE, 2400),
    ('bc6a45a0-fa74-11eb-9a2e-fb411a8588da', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 15 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 15 pv sopimus', 'Preschool daycare – over 5 h per day; 15 day agreement', 'PRESCHOOL_DAYCARE', FALSE, 0.6, 0.5, 0.5, 0.5, 0.5, 24, 15, FALSE, TRUE, 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 15 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 15 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 15 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 15 pv sopimus', FALSE, FALSE, 2500),
    ('0a58d934-6fd1-11ed-a75e-c353faef5857', 'Täydentävä varhaiskasvatus, max 5h päivässä', 'Täydentävä varhaiskasvatus, max 5h päivässä', 'Preschool daycare - max 5 h per day', 'PRESCHOOL_DAYCARE', FALSE, 0.6, 0.5, 0.5, 0.5, 0.5, 25, NULL, TRUE, FALSE, 'Täydentävä varhaiskasvatus, max 5h päivässä', 'Täydentävä varhaiskasvatus, max 5h päivässä', 'Täydentävä varhaiskasvatus, max 5h päivässä', 'Täydentävä varhaiskasvatus, max 5h päivässä', TRUE, TRUE, 2000),
    ('0a58db0a-6fd1-11ed-a75e-bbde95c1adef', 'Täydentävä varhaiskasvatus, max 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 10 pv sopimus', 'Preschool daycare – max 5 h per day; 10 day agreement', 'PRESCHOOL_DAYCARE', FALSE, 0.3, 0.25, 0.25, 0.5, 0.5, 12, 10, TRUE, TRUE, 'Täydentävä varhaiskasvatus, max 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 10 pv sopimus', TRUE, TRUE, 2100),
    ('0a58dbe6-6fd1-11ed-a75e-5335f2b9a91b', 'Täydentävä varhaiskasvatus, max 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 15 pv sopimus', 'Preschool daycare – max 5 h per day; 15 day agreement', 'PRESCHOOL_DAYCARE', FALSE, 0.45, 0.5, 0.5, 0.5, 0.5, 18, 15, TRUE, TRUE, 'Täydentävä varhaiskasvatus, max 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 15 pv sopimus', TRUE, TRUE, 2200),
    ('0a58da38-6fd1-11ed-a75e-9b2790b0b4f5', 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Preschool daycare – over 5 h per day', 'PRESCHOOL_DAYCARE', FALSE, 0.8, 0.5, 0.5, 0.5, 0.5, 32, NULL, FALSE, FALSE, 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Täydentävä varhaiskasvatus, yli 5h päivässä', TRUE, TRUE, 2300),
    ('0a58dcae-6fd1-11ed-a75e-b3e10433b948', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 10 pv sopimus', 'Preschool daycare – over 5 h per day; 10 day agreement', 'PRESCHOOL_DAYCARE', FALSE, 0.4, 0.25, 0.25, 0.5, 0.5, 18, 10, FALSE, TRUE, 'Täydentävä varhaiskasvatus, yli 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 10 pv sopimus', TRUE, TRUE, 2400),
    ('0a58dd94-6fd1-11ed-a75e-8390cdc6af62', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 15 pv sopimus', 'Preschool daycare – over 5 h per day; 15 day agreement', 'PRESCHOOL_DAYCARE', FALSE, 0.6, 0.5, 0.5, 0.5, 0.5, 24, 15, FALSE, TRUE, 'Täydentävä varhaiskasvatus, yli 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 15 pv sopimus', TRUE, TRUE, 2500),
    ('88f3bf1e-6646-11ed-8202-8f213a9146c2', 'Esiopetuksen kerho 1-3h päivässä', 'Esiopetuksen kerho 1-3h päivässä', 'Preschool club - 1-3 h per day', 'PRESCHOOL_CLUB', TRUE, 0.6, 0, 0, 0, 0, 25, NULL, TRUE, FALSE, 'Esiopetuksen kerho 1-3h päivässä', 'Esiopetuksen kerho 1-3h päivässä', 'Esiopetuksen kerho 1-3h päivässä', 'Esiopetuksen kerho 1-3h päivässä', TRUE, TRUE, null),
    ('88f3dfd0-6646-11ed-8202-4fe90b8e5485', 'Esiopetuksen kerho 1-3h päivässä', 'Esiopetuksen kerho 1-3h päivässä', 'Preschool club - 1-3 h per day', 'PRESCHOOL_CLUB', FALSE, 0.6, 0, 0, 0, 0, 25, NULL, TRUE, FALSE, 'Esiopetuksen kerho 1-3h päivässä', 'Esiopetuksen kerho 1-3h päivässä', 'Esiopetuksen kerho 1-3h päivässä', 'Esiopetuksen kerho 1-3h päivässä', TRUE, TRUE, 3000),
    ('88f3e214-6646-11ed-8202-f77aa4749644', 'Esiopetuksen kerho 3-5h päivässä', 'Esiopetuksen kerho 3-5h päivässä', 'Preschool club – 3-5 h per day', 'PRESCHOOL_CLUB', FALSE, 0.8, 0, 0, 0, 0, 32, NULL, FALSE, FALSE, 'Esiopetuksen kerho 3-5h päivässä', 'Esiopetuksen kerho 3-5h päivässä', 'Esiopetuksen kerho 3-5h päivässä', 'Esiopetuksen kerho 3-5h päivässä', TRUE, TRUE, 3100),
    ('88f3e3e0-6646-11ed-8202-3bc1b45aaa73', 'Esiopetuksen kerho yli 5h päivässä', 'Esiopetuksen kerho yli 5h päivässä', 'Preschool club – over 5 h per day', 'PRESCHOOL_CLUB', FALSE, 0.3, 0, 0, 0, 0, 12, 10, TRUE, TRUE, 'Esiopetuksen kerho yli 5h päivässä', 'Esiopetuksen kerho yli 5h päivässä', 'Esiopetuksen kerho yli 5h päivässä', 'Esiopetuksen kerho yli 5h päivässä', TRUE, TRUE, 3200),
    ('88f3e5a2-6646-11ed-8202-0b8db1a29ca5', 'Esiopetuksen kerho 1-3h päivässä; 10 pv sopimus', 'Esiopetuksen kerho 1-3h päivässä; 10 pv sopimus', 'Preschool club – 1-3 h per day; 10 day agreement', 'PRESCHOOL_CLUB', FALSE, 0.45, 0, 0, 0, 0, 18, 15, TRUE, TRUE, 'Esiopetuksen kerho 1-3h päivässä; 10 pv sopimus', 'Esiopetuksen kerho 1-3h päivässä; 10 pv sopimus', 'Esiopetuksen kerho 1-3h päivässä; 10 pv sopimus', 'Esiopetuksen kerho 1-3h päivässä; 10 pv sopimus', TRUE, TRUE, 3300),
    ('88f3e75a-6646-11ed-8202-b7867ae6358a', 'Esiopetuksen kerho 3-5h päivässä; 10pv sopimus', 'Esiopetuksen kerho 3-5h päivässä; 10pv sopimus', 'Preschool club – 3-5 h per day; 10 day agreement', 'PRESCHOOL_CLUB', FALSE, 0.4, 0, 0, 0, 0, 18, 10, FALSE, TRUE, 'Esiopetuksen kerho 3-5h päivässä; 10pv sopimus', 'Esiopetuksen kerho 3-5h päivässä; 10pv sopimus', 'Esiopetuksen kerho 3-5h päivässä; 10pv sopimus', 'Esiopetuksen kerho 3-5h päivässä; 10pv sopimus', TRUE, TRUE, 3400),
    ('88f3e912-6646-11ed-8202-bb9fe8059b4a', 'Esiopetuksen kerho yli 5h päivässä; 10pv sopimus', 'Esiopetuksen kerho yli 5h päivässä; 10pv sopimus', 'Preschool club – over 5 h per day; 10 day agreement', 'PRESCHOOL_CLUB', FALSE, 0.6, 0, 0, 0, 0, 24, 15, FALSE, TRUE, 'Esiopetuksen kerho yli 5h päivässä; 10pv sopimus', 'Esiopetuksen kerho yli 5h päivässä; 10pv sopimus', 'Esiopetuksen kerho yli 5h päivässä; 10pv sopimus', 'Esiopetuksen kerho yli 5h päivässä; 10pv sopimus', TRUE, TRUE, 3500),
    ('ff6ddcd4-fa8a-11eb-8592-2f2b4e398fcb', 'Kerho', 'Kerho', 'Club', 'CLUB', TRUE, 0.0, 1.0, 1.0, 1.0, 1.0, 0, NULL, TRUE, TRUE, 'Kerho', 'Kerho', 'Kerho', 'Kerho', TRUE, TRUE, null),
    ('d8681652-246b-11ed-b02b-ebe37c3347e8', 'Esiopetus', 'Esiopetus', 'Preschool', 'PRESCHOOL', TRUE, 0.0, 0.5, 0.5, 0.5, 0.5, 0, NULL, TRUE, TRUE, 'Esiopetus', 'Esiopetus', 'Esiopetus', 'Esiopetus', TRUE, TRUE, null),
    ('1b4413f6-d99d-11eb-89ac-a3a978104bce', 'Koululaisen vuorohoito', 'Koululaisen vuorohoito', 'School shift care', 'SCHOOL_SHIFT_CARE', TRUE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, TRUE, FALSE, 'Koululaisen vuorohoito', 'Koululaisen vuorohoito', 'Koululaisen vuorohoito', 'Koululaisen vuorohoito', TRUE, TRUE, null)
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
    service_need_option.part_day <> EXCLUDED.part_day OR
    service_need_option.part_week <> EXCLUDED.part_week OR
    service_need_option.fee_description_fi <> EXCLUDED.fee_description_fi OR
    service_need_option.fee_description_sv <> EXCLUDED.fee_description_sv OR
    service_need_option.voucher_value_description_fi <> EXCLUDED.voucher_value_description_fi OR
    service_need_option.voucher_value_description_sv <> EXCLUDED.voucher_value_description_sv OR
    service_need_option.active <> EXCLUDED.active OR
    service_need_option.show_for_citizen <> EXCLUDED.show_for_citizen OR
    service_need_option.display_order <> EXCLUDED.display_order;

INSERT INTO service_need_option_voucher_value
    (id, service_need_option_id, validity, base_value, coefficient, value, base_value_under_3y, coefficient_under_3y, value_under_3y)
VALUES
    -- 2000-01-01 - 2022-07-31
    ('422ee7f0-e7ea-11ec-8813-872424fb3290', '50358394-b961-11eb-b51f-67ac436e5636', daterange('2000-01-01', '2022-07-31', '[]'), 82600, 1.00, 82600, 124700, 1.00, 124700),
    ('422f00d2-e7ea-11ec-8813-cb592eaf8fcf', '86ef70a0-bf85-11eb-91e6-1fb57a101161', daterange('2000-01-01', '2022-07-31', '[]'), 82600, 1.00, 82600, 124700, 1.00, 124700),
    ('422f05dc-e7ea-11ec-8813-b3668e81d223', '503590f0-b961-11eb-b520-53740af3f7ee', daterange('2000-01-01', '2022-07-31', '[]'), 82600, 0.60, 49560, 124700, 0.60, 74820),
    ('422f09ec-e7ea-11ec-8813-f329e57bcb32', '503591ae-b961-11eb-b521-1fca99358eef', daterange('2000-01-01', '2022-07-31', '[]'), 82600, 0.80, 66080, 124700, 0.80, 99760),
    ('422f0d5c-e7ea-11ec-8813-cb1cd6093b78', '50359212-b961-11eb-b522-074fb05f7086', daterange('2000-01-01', '2022-07-31', '[]'), 82600, 0.60, 49560, 124700, 0.60, 74820),
    ('422f1130-e7ea-11ec-8813-cbfb64c2f2bf', '86ef7370-bf85-11eb-91e7-6fcd728c518d', daterange('2000-01-01', '2022-07-31', '[]'), 82600, 0.60, 49560, 124700, 0.60, 74820),
    ('422f1504-e7ea-11ec-8813-4b2e990ac90a', '50359280-b961-11eb-b523-237115533645', daterange('2000-01-01', '2022-07-31', '[]'), 82600, 0.36, 29736, 124700, 0.36, 44892),
    ('422f19be-e7ea-11ec-8813-43a22fd8beaf', '503592da-b961-11eb-b524-7f27c780d83a', daterange('2000-01-01', '2022-07-31', '[]'), 82600, 0.48, 39648, 124700, 0.48, 59856),
    ('422f1dba-e7ea-11ec-8813-1fdc7fa64ae4', '50359334-b961-11eb-b525-f3febdfea5d3', daterange('2000-01-01', '2022-07-31', '[]'), 82600, 0.60, 49560, 124700, 0.60, 74820),
    ('422f20f8-e7ea-11ec-8813-6fdb782754ed', '5035938e-b961-11eb-b526-6b30323c87a8', daterange('2000-01-01', '2022-07-31', '[]'), 82600, 1.00, 82600, 124700, 1.00, 124700),
    ('422f2440-e7ea-11ec-8813-3720f593ba77', '0bfc6c92-ff2a-11eb-a785-2724e8e5e7ee', daterange('2000-01-01', '2022-07-31', '[]'), 82600, 0.50, 41300, 124700, 0.50, 62350),
    ('422f2800-e7ea-11ec-8813-e7347cbbb31b', '503593e8-b961-11eb-b527-a3dcdfb628ec', daterange('2000-01-01', '2022-07-31', '[]'), 82600, 0.50, 41300, 124700, 0.50, 62350),
    ('422f2be8-e7ea-11ec-8813-2bff85b439a2', '50359442-b961-11eb-b528-df3290c0d63e', daterange('2000-01-01', '2022-07-31', '[]'), 82600, 0.50, 41300, 124700, 0.50, 62350),
    ('422f300c-e7ea-11ec-8813-d33333dc7f46', 'bc6a42d0-fa74-11eb-9a2b-d315a7916074', daterange('2000-01-01', '2022-07-31', '[]'), 82600, 0.30, 24780, 124700, 0.30, 37410),
    ('422f33cc-e7ea-11ec-8813-cf5d2fbb4dfe', 'bc6a44ec-fa74-11eb-9a2c-73b53c2af869', daterange('2000-01-01', '2022-07-31', '[]'), 82600, 0.40, 33040, 124700, 0.40, 49880),
    ('422f3796-e7ea-11ec-8813-a7107e0ebf0f', 'bc6a4550-fa74-11eb-9a2d-035acd5db9aa', daterange('2000-01-01', '2022-07-31', '[]'), 82600, 0.30, 24780, 124700, 0.30, 37410),
    ('422f3b60-e7ea-11ec-8813-f3318d9cf8c1', 'bc6a45a0-fa74-11eb-9a2e-fb411a8588da', daterange('2000-01-01', '2022-07-31', '[]'), 82600, 0.40, 33040, 124700, 0.40, 49880),
    ('422f3f52-e7ea-11ec-8813-bbc69dcf3860', 'ff6ddcd4-fa8a-11eb-8592-2f2b4e398fcb', daterange('2000-01-01', '2022-07-31', '[]'), 82600, 0.00, 0, 124700, 0.00, 0),
    ('422f43f8-e7ea-11ec-8813-af10257c13ae', '1b4413f6-d99d-11eb-89ac-a3a978104bce', daterange('2000-01-01', '2022-07-31', '[]'), 82600, 1.00, 82600, 124700, 1.00, 124700),
    -- 2022-08-01 -
    ('422f47ea-e7ea-11ec-8813-cbbf8463041a', '50358394-b961-11eb-b51f-67ac436e5636', daterange('2022-08-01', NULL, '[]'), 86200, 1.00, 86200, 130200, 1.00, 130200),
    ('422f4be6-e7ea-11ec-8813-1b45ad37a47d', '86ef70a0-bf85-11eb-91e6-1fb57a101161', daterange('2022-08-01', NULL, '[]'), 86200, 1.00, 86200, 130200, 1.00, 130200),
    ('422f4fba-e7ea-11ec-8813-7bf3e6b54280', '503590f0-b961-11eb-b520-53740af3f7ee', daterange('2022-08-01', NULL, '[]'), 86200, 0.60, 51720, 130200, 0.60, 78120),
    ('422f538e-e7ea-11ec-8813-0b41ec608608', '503591ae-b961-11eb-b521-1fca99358eef', daterange('2022-08-01', NULL, '[]'), 86200, 1.00, 86200, 130200, 1.00, 130200),
    ('422f5762-e7ea-11ec-8813-9b28d449fc32', '50359212-b961-11eb-b522-074fb05f7086', daterange('2022-08-01', NULL, '[]'), 86200, 0.60, 51720, 130200, 0.60, 78120),
    ('422f5b40-e7ea-11ec-8813-bf2e9fdae637', '86ef7370-bf85-11eb-91e7-6fcd728c518d', daterange('2022-08-01', NULL, '[]'), 86200, 0.60, 51720, 130200, 0.60, 78120),
    ('422f5f46-e7ea-11ec-8813-db5e80cf9a6b', '50359280-b961-11eb-b523-237115533645', daterange('2022-08-01', NULL, '[]'), 86200, 0.36, 31032, 130200, 0.36, 46872),
    ('422f6324-e7ea-11ec-8813-bfb1f547b2b4', '503592da-b961-11eb-b524-7f27c780d83a', daterange('2022-08-01', NULL, '[]'), 86200, 0.60, 51720, 130200, 0.60, 78120),
    ('422f6752-e7ea-11ec-8813-ebc71789683c', '50359334-b961-11eb-b525-f3febdfea5d3', daterange('2022-08-01', NULL, '[]'), 86200, 0.60, 51720, 130200, 0.60, 78120),
    ('422f6b58-e7ea-11ec-8813-3f6c93576cd8', '5035938e-b961-11eb-b526-6b30323c87a8', daterange('2022-08-01', NULL, '[]'), 86200, 1.00, 86200, 130200, 1.00, 130200),
    ('422f6f18-e7ea-11ec-8813-2f4bf5f3fdff', '0bfc6c92-ff2a-11eb-a785-2724e8e5e7ee', daterange('2022-08-01', NULL, '[]'), 86200, 0.50, 43100, 130200, 0.50, 65100),
    ('422f72ec-e7ea-11ec-8813-f7fcf18156cd', '503593e8-b961-11eb-b527-a3dcdfb628ec', daterange('2022-08-01', NULL, '[]'), 86200, 0.50, 43100, 130200, 0.50, 65100),
    ('422f76d4-e7ea-11ec-8813-1f7c5066085e', '50359442-b961-11eb-b528-df3290c0d63e', daterange('2022-08-01', NULL, '[]'), 86200, 0.50, 43100, 130200, 0.50, 65100),
    ('422f7ac6-e7ea-11ec-8813-d79f12b35fe8', 'bc6a42d0-fa74-11eb-9a2b-d315a7916074', daterange('2022-08-01', NULL, '[]'), 86200, 0.30, 25860, 130200, 0.30, 39060),
    ('422f7eae-e7ea-11ec-8813-87c6c3962177', 'bc6a44ec-fa74-11eb-9a2c-73b53c2af869', daterange('2022-08-01', NULL, '[]'), 86200, 0.50, 43100, 130200, 0.50, 65100),
    ('422f8296-e7ea-11ec-8813-27f63223bec5', 'bc6a4550-fa74-11eb-9a2d-035acd5db9aa', daterange('2022-08-01', NULL, '[]'), 86200, 0.30, 25860, 130200, 0.30, 39060),
    ('422f867e-e7ea-11ec-8813-0fc228bb529d', 'bc6a45a0-fa74-11eb-9a2e-fb411a8588da', daterange('2022-08-01', NULL, '[]'), 86200, 0.50, 43100, 130200, 0.50, 65100),
    ('f91b730a-6fd2-11ed-a75e-378477307155', '0a58d934-6fd1-11ed-a75e-c353faef5857', daterange('2022-08-01', NULL, '[]'), 86200, 0.50, 43100, 130200, 0.50, 65100),
    ('f91b8390-6fd2-11ed-a75e-7bb95afe470a', '0a58db0a-6fd1-11ed-a75e-bbde95c1adef', daterange('2022-08-01', NULL, '[]'), 86200, 0.30, 25860, 130200, 0.30, 39060),
    ('f91b8728-6fd2-11ed-a75e-3f57230a539d', '0a58dbe6-6fd1-11ed-a75e-5335f2b9a91b', daterange('2022-08-01', NULL, '[]'), 86200, 0.50, 43100, 130200, 0.50, 65100),
    ('f91b7f12-6fd2-11ed-a75e-539b788b60bb', '0a58da38-6fd1-11ed-a75e-9b2790b0b4f5', daterange('2022-08-01', NULL, '[]'), 86200, 0.50, 43100, 130200, 0.50, 65100),
    ('f91b8a66-6fd2-11ed-a75e-5fbc4afc5fd3', '0a58dcae-6fd1-11ed-a75e-b3e10433b948', daterange('2022-08-01', NULL, '[]'), 86200, 0.30, 25860, 130200, 0.30, 39060),
    ('f91b8d90-6fd2-11ed-a75e-a7c9e8f18799', '0a58dd94-6fd1-11ed-a75e-8390cdc6af62', daterange('2022-08-01', NULL, '[]'), 86200, 0.50, 43100, 130200, 0.50, 65100),
    ('a1fa1430-6647-11ed-8202-bb4dc37378c9', '88f3bf1e-6646-11ed-8202-8f213a9146c2', daterange('2022-08-01', NULL, '[]'), 86200, 0.50, 43100, 130200, 0.50, 65100),
    ('a1fb440e-6647-11ed-8202-2b499279fe71', '88f3dfd0-6646-11ed-8202-4fe90b8e5485', daterange('2022-08-01', NULL, '[]'), 86200, 0.50, 43100, 130200, 0.50, 65100),
    ('a1fb4828-6647-11ed-8202-d7d64698f305', '88f3e214-6646-11ed-8202-f77aa4749644', daterange('2022-08-01', NULL, '[]'), 86200, 0.50, 43100, 130200, 0.50, 65100),
    ('a1fb4b8e-6647-11ed-8202-b72f51e3f77d', '88f3e3e0-6646-11ed-8202-3bc1b45aaa73', daterange('2022-08-01', NULL, '[]'), 86200, 0.30, 25860, 130200, 0.30, 39060),
    ('a1fb4ee0-6647-11ed-8202-db7d7e99fa43', '88f3e5a2-6646-11ed-8202-0b8db1a29ca5', daterange('2022-08-01', NULL, '[]'), 86200, 0.50, 43100, 130200, 0.50, 65100),
    ('a1fb5250-6647-11ed-8202-5ba5a6f01f31', '88f3e75a-6646-11ed-8202-b7867ae6358a', daterange('2022-08-01', NULL, '[]'), 86200, 0.30, 25860, 130200, 0.30, 39060),
    ('a1fb5606-6647-11ed-8202-972da272f877', '88f3e912-6646-11ed-8202-bb9fe8059b4a', daterange('2022-08-01', NULL, '[]'), 86200, 0.50, 43100, 130200, 0.50, 65100),
    ('422f8a52-e7ea-11ec-8813-87e075796747', 'ff6ddcd4-fa8a-11eb-8592-2f2b4e398fcb', daterange('2022-08-01', NULL, '[]'), 86200, 0.00, 0, 130200, 0.00, 0),
    ('422f8ee4-e7ea-11ec-8813-e7010d4c8ab9', '1b4413f6-d99d-11eb-89ac-a3a978104bce', daterange('2022-08-01', NULL, '[]'), 86200, 1.00, 86200, 130200, 1.00, 130200)
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
