-- SPDX-FileCopyrightText: 2021 City of Tampere
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

-- automatically update service needs (created by PlacementTool applications) from default to non-default
-- TODO: remove after 2025-08-06?
UPDATE service_need
SET option_id = '0a58d934-6fd1-11ed-a75e-c353faef5857'
WHERE option_id = '0bfc6c92-ff2a-11eb-a785-2724e8e5e7ee';

INSERT INTO service_need_option
    (id, name_fi, name_sv, name_en, valid_placement_type, default_option, fee_coefficient, occupancy_coefficient, occupancy_coefficient_under_3y, realized_occupancy_coefficient, realized_occupancy_coefficient_under_3y, daycare_hours_per_week, contract_days_per_month, part_day, part_week, fee_description_fi, fee_description_sv, voucher_value_description_fi, voucher_value_description_sv, valid_from, valid_to, show_for_citizen, display_order)
VALUES
    ('50358394-b961-11eb-b51f-67ac436e5636', 'Kokopäiväinen', 'Kokopäiväinen', 'Full-time', 'DAYCARE', TRUE, 1.0, 1.0, 1.75, 1.0, 1.75, 40, NULL, FALSE, FALSE, 'Kokopäiväinen', 'Kokopäiväinen', 'Kokopäiväinen', 'Kokopäiväinen', '2000-01-01', NULL, FALSE, null),
    ('86ef70a0-bf85-11eb-91e6-1fb57a101161', 'Kokopäiväinen', 'Kokopäiväinen', 'Full-time', 'DAYCARE', FALSE, 1.0, 1.0, 1.75, 1.0, 1.75, 40, NULL, FALSE, FALSE, 'Kokopäiväinen', 'Kokopäiväinen', 'Kokopäiväinen', 'Kokopäiväinen', '2000-01-01', NULL, TRUE, 0),
    ('503590f0-b961-11eb-b520-53740af3f7ee', 'Kokopäiväinen, 10 pv sopimus', 'Kokopäiväinen, 10 pv sopimus', 'Full-time, 10 day agreement', 'DAYCARE', FALSE, 0.5, 0.5, 0.88, 0.5, 1.75, 20, 10, FALSE, TRUE, 'Kokopäiväinen, 10 pv sopimus', 'Kokopäiväinen, 10 pv sopimus', 'Kokopäiväinen, 10 pv sopimus', 'Kokopäiväinen, 10 pv sopimus', '2000-01-01', NULL, TRUE, 1),
    ('503591ae-b961-11eb-b521-1fca99358eef', 'Kokopäiväinen, 15 pv sopimus', 'Kokopäiväinen, 15 pv sopimus', 'Full-time, 15 day agreement', 'DAYCARE', FALSE, 0.75, 1.0, 1.75, 1.0, 1.75, 30, 15, FALSE, TRUE, 'Kokopäiväinen, 15 pv sopimus', 'Kokopäiväinen, 15 pv sopimus', 'Kokopäiväinen, 15 pv sopimus', 'Kokopäiväinen, 15 pv sopimus', '2000-01-01', NULL, TRUE, 2),
    ('50359212-b961-11eb-b522-074fb05f7086', 'Osapäiväinen, max 5h päivässä', 'Osapäiväinen, max 5h päivässä', 'Part-time, max 5h per day', 'DAYCARE_PART_TIME', TRUE, 0.6, 0.5, 1.75, 0.5, 1.75, 25, NULL, TRUE, FALSE, 'Osapäiväinen, max 5h päivässä', 'Osapäiväinen, max 5h päivässä', 'Osapäiväinen, max 5h päivässä', 'Osapäiväinen, max 5h päivässä', '2000-01-01', NULL, TRUE, null),
    ('86ef7370-bf85-11eb-91e7-6fcd728c518d', 'Osapäiväinen, max 5h päivässä', 'Osapäiväinen, max 5h päivässä', 'Part-time, max 5h per day', 'DAYCARE_PART_TIME', FALSE, 0.6, 0.5, 1.75, 0.5, 1.75, 25, NULL, TRUE, FALSE, 'Osapäiväinen, max 5h päivässä', 'Osapäiväinen, max 5h päivässä', 'Osapäiväinen, max 5h päivässä', 'Osapäiväinen, max 5h päivässä', '2000-01-01', NULL, TRUE, 1001),
    ('50359280-b961-11eb-b523-237115533645', 'Osapäiväinen, max 5h päivässä; 10 pv sopimus', 'Osapäiväinen, max 5h päivässä; 10 pv sopimus', 'Part-time, max 5h per day; 10 day agreement', 'DAYCARE_PART_TIME', FALSE, 0.3, 0.25, 0.88, 0.50, 1.75, 12, 10, TRUE, TRUE, 'Osapäiväinen, max 5h päivässä; 10 pv sopimus', 'Osapäiväinen, max 5h päivässä; 10 pv sopimus', 'Osapäiväinen, max 5h päivässä; 10 pv sopimus', 'Osapäiväinen, max 5h päivässä; 10 pv sopimus', '2000-01-01', NULL, TRUE, 1002),
    ('503592da-b961-11eb-b524-7f27c780d83a', 'Osapäiväinen, max 5h päivässä; 15 pv sopimus', 'Osapäiväinen, max 5h päivässä; 15 pv sopimus', 'Part-time, max 5h per day; 15 day agreement', 'DAYCARE_PART_TIME', FALSE, 0.45, 0.5, 1.75, 0.5, 1.75, 18, 15, TRUE, TRUE, 'Osapäiväinen, max 5h päivässä; 15 pv sopimus', 'Osapäiväinen, max 5h päivässä; 15 pv sopimus', 'Osapäiväinen, max 5h päivässä; 15 pv sopimus', 'Osapäiväinen, max 5h päivässä; 15 pv sopimus', '2000-01-01', NULL, TRUE, 1003),
    ('50359334-b961-11eb-b525-f3febdfea5d3', 'Osapäiväinen, max 20h viikossa', 'Osapäiväinen, max 20h viikossa', 'Part-time, max 20h per week', 'DAYCARE_PART_TIME', FALSE, 0.5, 0.5, 0.88, 0.5, 1.75, 20, NULL, TRUE, FALSE, 'Osapäiväinen, max 20h viikossa', 'Osapäiväinen, max 20h viikossa', 'Osapäiväinen, max 20h viikossa', 'Osapäiväinen, max 20h viikossa', '2000-01-01', NULL, TRUE, 1000),
    ('5035938e-b961-11eb-b526-6b30323c87a8', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Temporary daycare', 'TEMPORARY_DAYCARE', TRUE, 1.0, 1.0, 1.75, 1.0, 1.75, 40, NULL, FALSE, FALSE, 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', '2000-01-01', NULL, FALSE, null),
    ('40733727-8344-444d-90fa-5a8c7b887e86', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Temporary daycare', 'TEMPORARY_DAYCARE', FALSE, 1.0, 1.0, 1.75, 1.0, 1.75, 40, NULL, FALSE, FALSE, 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', '2000-01-01', NULL, FALSE, null),
    ('cfeae50c-20c3-45ff-be28-e78f71b8bed1', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Temporary daycare', 'TEMPORARY_DAYCARE_PART_DAY', TRUE, 1.0, 1.0, 1.75, 1.0, 1.75, 40, NULL, FALSE, FALSE, 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', '2000-01-01', NULL, FALSE, null),
    ('7ad88407-dbaf-4618-8003-c4b39c2f2546', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Temporary daycare', 'TEMPORARY_DAYCARE_PART_DAY', FALSE, 1.0, 1.0, 1.75, 1.0, 1.75, 40, NULL, FALSE, FALSE, 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', '2000-01-01', NULL, FALSE, null),
    ('0bfc6c92-ff2a-11eb-a785-2724e8e5e7ee', 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Preschool daycare – over 5 h per day', 'PRESCHOOL_DAYCARE', TRUE, 0.8, 1, 1, 1, 1, 32, NULL, FALSE, FALSE, 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Täydentävä varhaiskasvatus, yli 5h päivässä', '2000-01-01', NULL, FALSE, null),
    ('503593e8-b961-11eb-b527-a3dcdfb628ec', 'Esiopetusta täydentävä varhaiskasvatus - max 5 h päivässä', 'Esiopetusta täydentävä varhaiskasvatus - max 5 h päivässä', 'Preschool daycare - max 5 h per day', 'PRESCHOOL_DAYCARE', FALSE, 0.6, 1, 1, 1, 1, 25, NULL, TRUE, FALSE, 'Esiopetusta täydentävä varhaiskasvatus - max 5 h päivässä', 'Esiopetusta täydentävä varhaiskasvatus - max 5 h päivässä', 'Esiopetusta täydentävä varhaiskasvatus - max 5 h päivässä', 'Esiopetusta täydentävä varhaiskasvatus - max 5 h päivässä', '2000-01-01', '2023-06-02', FALSE, 2000),
    ('50359442-b961-11eb-b528-df3290c0d63e', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä', 'Preschool daycare – over 5 h per day', 'PRESCHOOL_DAYCARE', FALSE, 0.8, 1, 1, 1, 1, 32, NULL, FALSE, FALSE, 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä', '2000-01-01', '2023-06-02', FALSE, 2100),
    ('bc6a42d0-fa74-11eb-9a2b-d315a7916074', 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 10 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 10 pv sopimus', 'Preschool daycare – max 5 h per day; 10 day agreement', 'PRESCHOOL_DAYCARE', FALSE, 0.3, 0.5, 0.5, 1, 1, 12, 10, TRUE, TRUE, 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 10 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 10 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 10 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 10 pv sopimus', '2000-01-01', '2023-06-02', FALSE, 2200),
    ('bc6a44ec-fa74-11eb-9a2c-73b53c2af869', 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 15 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 15 pv sopimus', 'Preschool daycare – max 5 h per day; 15 day agreement', 'PRESCHOOL_DAYCARE', FALSE, 0.45, 1, 1, 1, 1, 18, 15, TRUE, TRUE, 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 15 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 15 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 15 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 15 pv sopimus', '2000-01-01', '2023-06-02', FALSE, 2300),
    ('bc6a4550-fa74-11eb-9a2d-035acd5db9aa', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 10 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 10 pv sopimus', 'Preschool daycare – over 5 h per day; 10 day agreement', 'PRESCHOOL_DAYCARE', FALSE, 0.4, 0.5, 0.5, 1, 1, 18, 10, FALSE, TRUE, 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 10 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 10 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 10 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 10 pv sopimus', '2000-01-01', '2023-06-02', FALSE, 2400),
    ('bc6a45a0-fa74-11eb-9a2e-fb411a8588da', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 15 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 15 pv sopimus', 'Preschool daycare – over 5 h per day; 15 day agreement', 'PRESCHOOL_DAYCARE', FALSE, 0.6, 1, 1, 1, 1, 24, 15, FALSE, TRUE, 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 15 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 15 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 15 pv sopimus', 'Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 15 pv sopimus', '2000-01-01', '2023-06-02', FALSE, 2500),
    ('0a58d934-6fd1-11ed-a75e-c353faef5857', 'Täydentävä varhaiskasvatus, max 5h päivässä', 'Täydentävä varhaiskasvatus, max 5h päivässä', 'Preschool daycare - max 5 h per day', 'PRESCHOOL_DAYCARE', FALSE, 0.6, 1, 1, 1, 1, 25, NULL, TRUE, FALSE, 'Täydentävä varhaiskasvatus, max 5h päivässä', 'Täydentävä varhaiskasvatus, max 5h päivässä', 'Täydentävä varhaiskasvatus, max 5h päivässä', 'Täydentävä varhaiskasvatus, max 5h päivässä', '2000-01-01', NULL, TRUE, 2000),
    ('0a58db0a-6fd1-11ed-a75e-bbde95c1adef', 'Täydentävä varhaiskasvatus, max 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 10 pv sopimus', 'Preschool daycare – max 5 h per day; 10 day agreement', 'PRESCHOOL_DAYCARE', FALSE, 0.3, 0.5, 0.5, 1, 1, 12, 10, TRUE, TRUE, 'Täydentävä varhaiskasvatus, max 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 10 pv sopimus', '2000-01-01', NULL, TRUE, 2100),
    ('0a58dbe6-6fd1-11ed-a75e-5335f2b9a91b', 'Täydentävä varhaiskasvatus, max 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 15 pv sopimus', 'Preschool daycare – max 5 h per day; 15 day agreement', 'PRESCHOOL_DAYCARE', FALSE, 0.45, 1, 1, 1, 1, 18, 15, TRUE, TRUE, 'Täydentävä varhaiskasvatus, max 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 15 pv sopimus', '2000-01-01', NULL, TRUE, 2200),
    ('0a58da38-6fd1-11ed-a75e-9b2790b0b4f5', 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Preschool daycare – over 5 h per day', 'PRESCHOOL_DAYCARE', FALSE, 0.8, 1, 1, 1, 1, 32, NULL, FALSE, FALSE, 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Täydentävä varhaiskasvatus, yli 5h päivässä', '2000-01-01', NULL, TRUE, 2300),
    ('0a58dcae-6fd1-11ed-a75e-b3e10433b948', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 10 pv sopimus', 'Preschool daycare – over 5 h per day; 10 day agreement', 'PRESCHOOL_DAYCARE', FALSE, 0.4, 0.5, 0.5, 1, 1, 18, 10, FALSE, TRUE, 'Täydentävä varhaiskasvatus, yli 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 10 pv sopimus', '2000-01-01', NULL, TRUE, 2400),
    ('0a58dd94-6fd1-11ed-a75e-8390cdc6af62', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 15 pv sopimus', 'Preschool daycare – over 5 h per day; 15 day agreement', 'PRESCHOOL_DAYCARE', FALSE, 0.6, 1, 1, 1, 1, 24, 15, FALSE, TRUE, 'Täydentävä varhaiskasvatus, yli 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 15 pv sopimus', '2000-01-01', NULL, TRUE, 2500),
    ('9cc7713c-f153-489a-a7c2-e001640f5c29', 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Preschool daycare – over 5 h per day', 'PRESCHOOL_DAYCARE_ONLY', TRUE, 0.8, 1, 1, 1, 1, 32, NULL, FALSE, FALSE, 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Täydentävä varhaiskasvatus, yli 5h päivässä', '2000-01-01', NULL, FALSE, null),
    ('816d36b5-29b3-494b-8ac4-b2298764c803', 'Täydentävä varhaiskasvatus, max 5h päivässä', 'Täydentävä varhaiskasvatus, max 5h päivässä', 'Preschool daycare - max 5 h per day', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 0.6, 1, 1, 1, 1, 25, NULL, TRUE, FALSE, 'Täydentävä varhaiskasvatus, max 5h päivässä', 'Täydentävä varhaiskasvatus, max 5h päivässä', 'Täydentävä varhaiskasvatus, max 5h päivässä', 'Täydentävä varhaiskasvatus, max 5h päivässä', '2000-01-01', NULL, FALSE, 2000),
    ('9a2b393b-ebb6-49d9-aa1e-b86d80ba9eb5', 'Täydentävä varhaiskasvatus, max 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 10 pv sopimus', 'Preschool daycare – max 5 h per day; 10 day agreement', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 0.3, 0.5, 0.5, 1, 1, 12, 10, TRUE, TRUE, 'Täydentävä varhaiskasvatus, max 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 10 pv sopimus', '2000-01-01', NULL, FALSE, 2100),
    ('efcfd8f4-92d9-43a4-91e0-a7e2fde9c4e5', 'Täydentävä varhaiskasvatus, max 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 15 pv sopimus', 'Preschool daycare – max 5 h per day; 15 day agreement', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 0.45, 1, 1, 1, 1, 18, 15, TRUE, TRUE, 'Täydentävä varhaiskasvatus, max 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, max 5h päivässä; 15 pv sopimus', '2000-01-01', NULL, FALSE, 2200),
    ('313744dc-ac9e-46b1-8004-940d429664e3', 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Preschool daycare – over 5 h per day', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 0.8, 1, 1, 1, 1, 32, NULL, FALSE, FALSE, 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Täydentävä varhaiskasvatus, yli 5h päivässä', 'Täydentävä varhaiskasvatus, yli 5h päivässä', '2000-01-01', NULL, FALSE, 2300),
    ('9e1e61da-3362-4f8f-bbf9-ea1eb0d0206c', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 10 pv sopimus', 'Preschool daycare – over 5 h per day; 10 day agreement', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 0.4, 0.5, 0.5, 1, 1, 18, 10, FALSE, TRUE, 'Täydentävä varhaiskasvatus, yli 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 10 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 10 pv sopimus', '2000-01-01', NULL, FALSE, 2400),
    ('b5269ff0-a047-496f-9bda-6286520ef1a5', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 15 pv sopimus', 'Preschool daycare – over 5 h per day; 15 day agreement', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 0.6, 1, 1, 1, 1, 24, 15, FALSE, TRUE, 'Täydentävä varhaiskasvatus, yli 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 15 pv sopimus', 'Täydentävä varhaiskasvatus, yli 5h päivässä; 15 pv sopimus', '2000-01-01', NULL, FALSE, 2500),
    ('88f3bf1e-6646-11ed-8202-8f213a9146c2', 'Esiopetuksen kerho 1-3h päivässä', 'Esiopetuksen kerho 1-3h päivässä', 'Preschool club - 1-3 h per day', 'PRESCHOOL_CLUB', TRUE, 1.0, 0, 0, 0, 0, 15, NULL, TRUE, FALSE, 'Esiopetuksen kerho 1-3h päivässä', 'Esiopetuksen kerho 1-3h päivässä', 'Esiopetuksen kerho 1-3h päivässä', 'Esiopetuksen kerho 1-3h päivässä', '2000-01-01', '2024-05-31', FALSE, null),
    ('88f3dfd0-6646-11ed-8202-4fe90b8e5485', 'Esiopetuksen kerho 1-3h päivässä', 'Esiopetuksen kerho 1-3h päivässä', 'Preschool club - 1-3 h per day', 'PRESCHOOL_CLUB', FALSE, 1.0, 0, 0, 0, 0, 15, NULL, TRUE, FALSE, 'Esiopetuksen kerho 1-3h päivässä', 'Esiopetuksen kerho 1-3h päivässä', 'Esiopetuksen kerho 1-3h päivässä', 'Esiopetuksen kerho 1-3h päivässä', '2000-01-01', '2024-05-31', TRUE, 3000),
    ('88f3e214-6646-11ed-8202-f77aa4749644', 'Esiopetuksen kerho 3-5h päivässä', 'Esiopetuksen kerho 3-5h päivässä', 'Preschool club – 3-5 h per day', 'PRESCHOOL_CLUB', FALSE, 1.0, 0, 0, 0, 0, 25, NULL, TRUE, FALSE, 'Esiopetuksen kerho 3-5h päivässä', 'Esiopetuksen kerho 3-5h päivässä', 'Esiopetuksen kerho 3-5h päivässä', 'Esiopetuksen kerho 3-5h päivässä', '2000-01-01', '2024-05-31', TRUE, 3100),
    ('88f3e3e0-6646-11ed-8202-3bc1b45aaa73', 'Esiopetuksen kerho yli 5h päivässä', 'Esiopetuksen kerho yli 5h päivässä', 'Preschool club – over 5 h per day', 'PRESCHOOL_CLUB', FALSE, 1.0, 0, 0, 0, 0, 32, NULL, FALSE, FALSE, 'Esiopetuksen kerho yli 5h päivässä', 'Esiopetuksen kerho yli 5h päivässä', 'Esiopetuksen kerho yli 5h päivässä', 'Esiopetuksen kerho yli 5h päivässä', '2000-01-01', '2024-05-31', TRUE, 3200),
    ('88f3e5a2-6646-11ed-8202-0b8db1a29ca5', 'Esiopetuksen kerho 1-3h päivässä; 10 pv sopimus', 'Esiopetuksen kerho 1-3h päivässä; 10 pv sopimus', 'Preschool club – 1-3 h per day; 10 day agreement', 'PRESCHOOL_CLUB', FALSE, 0.6, 0, 0, 0, 0, 7, 10, TRUE, TRUE, 'Esiopetuksen kerho 1-3h päivässä; 10 pv sopimus', 'Esiopetuksen kerho 1-3h päivässä; 10 pv sopimus', 'Esiopetuksen kerho 1-3h päivässä; 10 pv sopimus', 'Esiopetuksen kerho 1-3h päivässä; 10 pv sopimus', '2000-01-01', '2024-05-31', TRUE, 3300),
    ('88f3e75a-6646-11ed-8202-b7867ae6358a', 'Esiopetuksen kerho 3-5h päivässä; 10pv sopimus', 'Esiopetuksen kerho 3-5h päivässä; 10pv sopimus', 'Preschool club – 3-5 h per day; 10 day agreement', 'PRESCHOOL_CLUB', FALSE, 0.5, 0, 0, 0, 0, 12, 10, TRUE, TRUE, 'Esiopetuksen kerho 3-5h päivässä; 10pv sopimus', 'Esiopetuksen kerho 3-5h päivässä; 10pv sopimus', 'Esiopetuksen kerho 3-5h päivässä; 10pv sopimus', 'Esiopetuksen kerho 3-5h päivässä; 10pv sopimus', '2000-01-01', '2024-05-31', TRUE, 3400),
    ('88f3e912-6646-11ed-8202-bb9fe8059b4a', 'Esiopetuksen kerho yli 5h päivässä; 10pv sopimus', 'Esiopetuksen kerho yli 5h päivässä; 10pv sopimus', 'Preschool club – over 5 h per day; 10 day agreement', 'PRESCHOOL_CLUB', FALSE, 0.5, 0, 0, 0, 0, 18, 10, FALSE, TRUE, 'Esiopetuksen kerho yli 5h päivässä; 10pv sopimus', 'Esiopetuksen kerho yli 5h päivässä; 10pv sopimus', 'Esiopetuksen kerho yli 5h päivässä; 10pv sopimus', 'Esiopetuksen kerho yli 5h päivässä; 10pv sopimus', '2000-01-01', '2024-05-31', TRUE, 3500),
    ('ff6ddcd4-fa8a-11eb-8592-2f2b4e398fcb', 'Kerho', 'Kerho', 'Club', 'CLUB', TRUE, 0.0, 1.0, 1.0, 1.0, 1.0, 0, NULL, TRUE, TRUE, 'Kerho', 'Kerho', 'Kerho', 'Kerho', '2000-01-01', NULL, FALSE, null),
    ('d8681652-246b-11ed-b02b-ebe37c3347e8', 'Esiopetus', 'Esiopetus', 'Preschool', 'PRESCHOOL', TRUE, 0.0, 0.5, 0.5, 0.5, 0.5, 0, NULL, TRUE, TRUE, 'Esiopetus', 'Esiopetus', 'Esiopetus', 'Esiopetus', '2000-01-01', NULL, FALSE, null),
    ('1b4413f6-d99d-11eb-89ac-a3a978104bce', 'Koululaisen vuorohoito', 'Koululaisen vuorohoito', 'School shift care', 'SCHOOL_SHIFT_CARE', TRUE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, TRUE, FALSE, 'Koululaisen vuorohoito', 'Koululaisen vuorohoito', 'Koululaisen vuorohoito', 'Koululaisen vuorohoito', '2000-01-01', NULL, FALSE, null),
    ('e20929c1-719a-42e1-93ca-da74fce495f8', 'Koululaisen vuorohoito', 'Koululaisen vuorohoito', 'School shift care', 'SCHOOL_SHIFT_CARE', FALSE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, TRUE, FALSE, 'Koululaisen vuorohoito', 'Koululaisen vuorohoito', 'Koululaisen vuorohoito', 'Koululaisen vuorohoito', '2000-01-01', NULL, FALSE, null)
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

INSERT INTO service_need_option_fee
    (id, service_need_option_id, validity, base_fee, sibling_discount_2, sibling_fee_2, sibling_discount_2_plus, sibling_fee_2_plus)
VALUES
    -- 2000-01-01 -
    ('f3a1936c-9410-4086-ad10-8767e6d8d27c', '88f3bf1e-6646-11ed-8202-8f213a9146c2', daterange('2000-01-01', NULL, '[]'), 5000, 0.4, 3000, 0.4, 3000),
    ('e549d486-26b4-429a-a102-160df7df36f7', '88f3dfd0-6646-11ed-8202-4fe90b8e5485', daterange('2000-01-01', NULL, '[]'), 5000, 0.4, 3000, 0.4, 3000),
    ('d8d8ee0e-398c-4b3d-bb3b-28951a581c0d', '88f3e214-6646-11ed-8202-f77aa4749644', daterange('2000-01-01', NULL, '[]'), 10000, 0.4, 6000, 0.4, 6000),
    ('007ec41b-77dd-4ad7-bece-332771bab262', '88f3e3e0-6646-11ed-8202-3bc1b45aaa73', daterange('2000-01-01', NULL, '[]'), 12000, 0.4, 7000, 0.4, 7000),
    ('63fb7f18-eb41-491a-8c70-765d980aa248', '88f3e5a2-6646-11ed-8202-0b8db1a29ca5', daterange('2000-01-01', NULL, '[]'), 5000, 0.4, 3000, 0.4, 3000),
    ('9e42259d-f28d-436c-9059-865fb1458df0', '88f3e75a-6646-11ed-8202-b7867ae6358a', daterange('2000-01-01', NULL, '[]'), 10000, 0.4, 6000, 0.4, 6000),
    ('2cb13db3-0f76-444f-bb8f-ebd90f3181a0', '88f3e912-6646-11ed-8202-bb9fe8059b4a', daterange('2000-01-01', NULL, '[]'), 12000, 0.4, 7200, 0.4, 7200)
ON CONFLICT (id) DO
UPDATE SET
    service_need_option_id = EXCLUDED.service_need_option_id,
    validity = EXCLUDED.validity,
    base_fee = EXCLUDED.base_fee,
    sibling_discount_2 = EXCLUDED.sibling_discount_2,
    sibling_fee_2 = EXCLUDED.sibling_fee_2,
    sibling_discount_2_plus = EXCLUDED.sibling_discount_2_plus,
    sibling_fee_2_plus = EXCLUDED.sibling_fee_2_plus
WHERE
    service_need_option_fee.service_need_option_id <> EXCLUDED.service_need_option_id OR
    service_need_option_fee.validity <> EXCLUDED.validity OR
    service_need_option_fee.base_fee <> EXCLUDED.base_fee OR
    service_need_option_fee.sibling_discount_2 <> EXCLUDED.sibling_discount_2 OR
    service_need_option_fee.sibling_fee_2 <> EXCLUDED.sibling_fee_2 OR
    service_need_option_fee.sibling_discount_2_plus <> EXCLUDED.sibling_discount_2_plus OR
    service_need_option_fee.sibling_fee_2_plus <> EXCLUDED.sibling_fee_2_plus;
