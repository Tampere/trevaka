-- SPDX-FileCopyrightText: 2023-2024 Tampere region
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

INSERT INTO service_need_option
(id, name_fi, name_sv, name_en, valid_placement_type, default_option, fee_coefficient, occupancy_coefficient, occupancy_coefficient_under_3y, realized_occupancy_coefficient, realized_occupancy_coefficient_under_3y, daycare_hours_per_week, contract_days_per_month, daycare_hours_per_month, part_day, part_week, fee_description_fi, fee_description_sv, voucher_value_description_fi, voucher_value_description_sv, valid_from, valid_to, show_for_citizen, display_order)
VALUES
    ('f9e7d841-49bf-43e5-8c65-028dad590a76', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Daycare - 85 h per month', 'DAYCARE', TRUE, 0.5, 1.0, 1.75, 1.0, 1.75, 40, NULL, 85, TRUE, TRUE, 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', '2023-08-01', NULL, TRUE, NULL),
    ('50358394-b961-11eb-b51f-67ac436e5637', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Daycare - 85 h per month', 'DAYCARE', FALSE, 0.5, 1.0, 1.75, 1.0, 1.75, 40, NULL, 85, TRUE, TRUE, 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 0),
    ('86ef70a0-bf85-11eb-91e6-1fb57a101165', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Daycare - 86-120 h per month', 'DAYCARE', FALSE, 0.8, 1.0, 1.75, 1.0, 1.75, 40, NULL, 120, TRUE, TRUE, 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 1),
    ('503590f0-b961-11eb-b520-53740af3f7ef', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Daycare - 121-150 h per month', 'DAYCARE', FALSE, 0.9, 1.0, 1.75, 1.0, 1.75, 40, NULL, 150, TRUE, TRUE, 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 2),
    ('503591ae-b961-11eb-b521-1fca99358eed', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Daycare - over 151 h per month', 'DAYCARE', FALSE, 1.0, 1.0, 1.75, 1.0, 1.75, 40, NULL, 210, FALSE, FALSE, 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 3),
    ('36c8c2ed-7543-47de-bc42-14d163a6277d', 'Palveluseteli 85 tuntia / kuukausi', 'Palveluseteli 85 tuntia / kuukausi', 'Voucher value - 85 h per month', 'DAYCARE', FALSE, 0.5, 1.0, 1.75, 1.0, 1.75, 40, NULL, 85, TRUE, TRUE, 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', '2023-08-01', '2024-07-31', TRUE, 4),
    ('9eb82822-82ef-46fb-a123-29cf0af757b7', 'Palveluseteli 86–139 tuntia / kuukausi', 'Palveluseteli 86–139 tuntia / kuukausi', 'Voucher value - 86-139 h per month', 'DAYCARE', FALSE, 0.8, 1.0, 1.75, 1.0, 1.75, 40, NULL, 139, TRUE, TRUE, 'Varhaiskasvatus 86–139 tuntia / kuukausi', 'Varhaiskasvatus 86–139 tuntia / kuukausi', 'Varhaiskasvatus 86–139 tuntia / kuukausi', 'Varhaiskasvatus 86–139 tuntia / kuukausi', '2023-08-01', '2024-07-31', TRUE, 5),
    ('f5d32585-2c78-4434-95d6-30b446db7d4d', 'Palveluseteli yli 140 tuntia / kuukausi', 'Palveluseteli yli 140 tuntia / kuukausi', 'Voucher value - over 140 h per month', 'DAYCARE', FALSE, 1.0, 1.0, 1.75, 1.0, 1.75, 40, NULL, 210, FALSE, FALSE, 'Varhaiskasvatus yli 140 tuntia / kuukausi', 'Varhaiskasvatus yli 140 tuntia / kuukausi', 'Varhaiskasvatus yli 140 tuntia / kuukausi', 'Varhaiskasvatus yli 140 tuntia / kuukausi', '2023-08-01', '2024-07-31', TRUE, 6),
    ('e1063bee-c19d-469d-85a5-6b0350872d76', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Temporary daycare', 'TEMPORARY_DAYCARE', TRUE, 1.0, 1.0, 1.75, 1.0, 1.75, 40, NULL, NULL, FALSE, FALSE, 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', '2023-08-01', NULL, TRUE, NULL),
    ('94e44ef1-106b-401d-81b6-8e5c31cd0437', 'Esiopetus', 'Esiopetus', 'Preschool', 'PRESCHOOL', TRUE, 0.0, 0.5, 0.5, 0.5, 0.5, 0, NULL, NULL, TRUE, TRUE, 'Esiopetus', 'Esiopetus', 'Esiopetus', 'Esiopetus', '2023-08-01', NULL, TRUE, NULL),
    ('1a4b0997-b83d-44ec-8bd9-98b12e5d6d04', 'Valmistava opetus', 'Valmistava opetus', 'Preparatory education', 'PREPARATORY', TRUE, 0.0, 0.5, 0.5, 0.5, 0.5, 0, NULL, NULL, TRUE, TRUE, 'Valmistava opetus', 'Valmistava opetus', 'Valmistava opetus', 'Valmistava opetus', '2023-08-01', NULL, TRUE, NULL),
    ('4e02d217-947e-41c7-a147-73111d2dc753', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Prepatatory daycare - 0–50 h per month', 'PREPARATORY_DAYCARE', TRUE, 0.3, 1.0, 1.0, 1.0, 1.0, 40, NULL, 50, TRUE, TRUE, 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', '2023-08-01', NULL, TRUE, NULL),
    ('025d0374-d36d-4a9b-b014-46147e6ea13f', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Prepatatory daycare - 0–50 h per month', 'PREPARATORY_DAYCARE', FALSE, 0.3, 1.0, 1.0, 1.0, 1.0, 40, NULL, 50, TRUE, TRUE, 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 4),
    ('03c11898-1056-4e6e-be35-5e0886445d13', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Prepatatory daycare – 51-85 h per month', 'PREPARATORY_DAYCARE', FALSE, 0.5, 1.0, 1.0, 1.0, 1.0, 40, NULL, 85, TRUE, TRUE, 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 5),
    ('114fc456-7e61-47c0-a88e-6743181d1cfc', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Prepatatory daycare – 68-120 h per month', 'PREPARATORY_DAYCARE', FALSE, 0.8, 1.0, 1.0, 1.0, 1.0, 40, NULL, 120, TRUE, TRUE, 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 6),
    ('16f1be29-e979-403b-9002-251065c85d78', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Prepatatory daycare – 121-150 h per month', 'PREPARATORY_DAYCARE', FALSE, 0.9, 1.0, 1.0, 1.0, 1.0, 40, NULL, 150, TRUE, TRUE, 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 7),
    ('689d4104-b1ad-4dd5-82ae-6640196a070a', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Prepatatory daycare – over 151 h per month', 'PREPARATORY_DAYCARE', FALSE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, 210, FALSE, FALSE, 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 8),
    ('01610c55-ecb8-4446-ab04-fdc631428649', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Prepatatory daycare - 0–50 h per month', 'PREPARATORY_DAYCARE_ONLY', TRUE, 0.3, 1.0, 1.0, 1.0, 1.0, 40, NULL, 50, TRUE, TRUE, 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', '2023-08-01', NULL, TRUE, NULL),
    ('a83bf292-e33e-4733-9233-4ff052b14982', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Prepatatory daycare - 0–50 h per month', 'PREPARATORY_DAYCARE_ONLY', FALSE, 0.3, 1.0, 1.0, 1.0, 1.0, 40, NULL, 50, TRUE, TRUE, 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 4),
    ('7e87b656-8ee6-4f77-bf71-00343862c405', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Prepatatory daycare – 51-85 h per month', 'PREPARATORY_DAYCARE_ONLY', FALSE, 0.5, 1.0, 1.0, 1.0, 1.0, 40, NULL, 85, TRUE, TRUE, 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 5),
    ('49aca03f-1cc1-4b9d-abb5-7a0638b39b02', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Prepatatory daycare – 68-120 h per month', 'PREPARATORY_DAYCARE_ONLY', FALSE, 0.8, 1.0, 1.0, 1.0, 1.0, 40, NULL, 120, TRUE, TRUE, 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 6),
    ('256b9feb-fd78-453b-b7ca-fa8a41e87360', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Prepatatory daycare – 121-150 h per month', 'PREPARATORY_DAYCARE_ONLY', FALSE, 0.9, 1.0, 1.0, 1.0, 1.0, 40, NULL, 150, TRUE, TRUE, 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 7),
    ('785e1cbe-8eb9-431a-a33a-07b5b8578088', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Prepatatory daycare – over 151 h per month', 'PREPARATORY_DAYCARE_ONLY', FALSE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, 210, FALSE, FALSE, 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 8),
    ('21a0589d-d4db-4978-9a37-8e6993a7dafd', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Preschool daycare - 0–50 h per month', 'PRESCHOOL_DAYCARE', TRUE, 0.3, 1.0, 1.0, 1.0, 1.0, 40, NULL, 50, TRUE, TRUE, 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', '2023-08-01', NULL, TRUE, NULL),
    ('1c5d7ea4-669f-4b4e-8593-353be4c9cea0', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Preschool daycare - 0–50 h per month', 'PRESCHOOL_DAYCARE', FALSE, 0.3, 1.0, 1.0, 1.0, 1.0, 40, NULL, 50, TRUE, TRUE, 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 4),
    ('9e6a4660-2f83-40e3-bf27-d9590e93dbf2', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Preschool daycare – 51-85 h per month', 'PRESCHOOL_DAYCARE', FALSE, 0.5, 1.0, 1.0, 1.0, 1.0, 40, NULL, 85, TRUE, TRUE, 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 5),
    ('3b94630b-e01e-4b61-b040-8910baf96e97', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Preschool daycare – 68-120 h per month', 'PRESCHOOL_DAYCARE', FALSE, 0.8, 1.0, 1.0, 1.0, 1.0, 40, NULL, 120, TRUE, TRUE, 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 6),
    ('593d8cbf-fcf8-41a7-a5fa-8fe96a7b93d8', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Preschool daycare – 121-150 h per month', 'PRESCHOOL_DAYCARE', FALSE, 0.9, 1.0, 1.0, 1.0, 1.0, 40, NULL, 150, TRUE, TRUE, 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 7),
    ('c169bce7-6533-4409-8acd-445061f1ff34', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Preschool daycare – over 151 h per month', 'PRESCHOOL_DAYCARE', FALSE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, 210, FALSE, FALSE, 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 8),
    ('93a50270-3f75-4672-b17d-db721bcb8ed2', 'Esiopetusta täydentävä 0-85h/kk (palveluseteli)', 'Esiopetusta täydentävä 0-85h/kk (palveluseteli)', 'Preschool daycare - 0-85 h per month (voucher value)', 'PRESCHOOL_DAYCARE', FALSE, 0.5, 1.0, 1.0, 1.0, 1.0, 40, NULL, 85, FALSE, FALSE, 'Esiopetusta täydentävä 0-85h/kk', 'Esiopetusta täydentävä 0-85h/kk', 'Esiopetusta täydentävä 0-85h/kk', 'Esiopetusta täydentävä 0-85h/kk', '2023-08-01', '2024-07-31', TRUE, 9),
    ('13920018-e4ce-4fd0-928c-673965a3ab19', 'Esiopetusta täydentävä 86-139h/kk (palveluseteli)', 'Esiopetusta täydentävä 86-139h/kk (palveluseteli)', 'Preschool daycare - 86-139 h per month (voucher value)', 'PRESCHOOL_DAYCARE', FALSE, 0.5, 1.0, 1.0, 1.0, 1.0, 40, NULL, 139, FALSE, FALSE, 'Esiopetusta täydentävä 86-139h/kk', 'Esiopetusta täydentävä 86-139h/kk', 'Esiopetusta täydentävä 86-139h/kk', 'Esiopetusta täydentävä 86-139h/kk', '2023-08-01', '2024-07-31', TRUE, 10),
    ('9cdef927-f31a-45cb-af49-3ea5b1c1cb8a', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Preschool daycare - 0–50 h per month', 'PRESCHOOL_DAYCARE_ONLY', TRUE, 0.3, 1.0, 1.0, 1.0, 1.0, 40, NULL, 50, TRUE, TRUE, 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', '2023-08-01', NULL, TRUE, NULL),
    ('0a58d934-6fd1-11ed-a75e-c353faef5858', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Preschool daycare - 0–50 h per month', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 0.3, 1.0, 1.0, 1.0, 1.0, 40, NULL, 50, TRUE, TRUE, 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 4),
    ('0a58db0a-6fd1-11ed-a75e-bbde95c1aded', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Preschool daycare – 51-85 h per month', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 0.5, 1.0, 1.0, 1.0, 1.0, 40, NULL, 85, TRUE, TRUE, 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 5),
    ('0a58dbe6-6fd1-11ed-a75e-5335f2b9a91c', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Preschool daycare – 68-120 h per month', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 0.8, 1.0, 1.0, 1.0, 1.0, 40, NULL, 120, TRUE, TRUE, 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 6),
    ('0a58da38-6fd1-11ed-a75e-9b2790b0b4f5', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Preschool daycare – 121-150 h per month', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 0.9, 1.0, 1.0, 1.0, 1.0, 40, NULL, 150, TRUE, TRUE, 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 7),
    ('0a58dcae-6fd1-11ed-a75e-b3e10433b949', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Preschool daycare – over 151 h per month', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, 210, FALSE, FALSE, 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 8),
    ('fe0972a5-6ce9-41cc-a635-82fb22e7891b', 'Esiopetusta täydentävä 0-85h/kk (palveluseteli)', 'Esiopetusta täydentävä 0-85h/kk (palveluseteli)', 'Preschool daycare - 0-85 h per month (voucher value)', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 0.5, 1.0, 1.0, 1.0, 1.0, 40, NULL, 85, FALSE, FALSE, 'Esiopetusta täydentävä 0-85h/kk', 'Esiopetusta täydentävä 0-85h/kk', 'Esiopetusta täydentävä 0-85h/kk', 'Esiopetusta täydentävä 0-85h/kk', '2023-08-01', '2024-07-31', TRUE, 9),
    ('b3102992-df96-45d5-a1c3-578791c2193c', 'Esiopetusta täydentävä 86-139h/kk (palveluseteli)', 'Esiopetusta täydentävä 86-139h/kk (palveluseteli)', 'Preschool daycare - 86-139 h per month (voucher value)', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 0.5, 1.0, 1.0, 1.0, 1.0, 40, NULL, 139, FALSE, FALSE, 'Esiopetusta täydentävä 86-139h/kk', 'Esiopetusta täydentävä 86-139h/kk', 'Esiopetusta täydentävä 86-139h/kk', 'Esiopetusta täydentävä 86-139h/kk', '2023-08-01', '2024-07-31', TRUE, 10)
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
    ('03ae36de-7058-45a1-ba72-514afca39dcd', 'f9e7d841-49bf-43e5-8c65-028dad590a76', daterange('2023-08-01', '2024-07-31', '[]'), 90100, 0.60, 54060, 140600, 0.60, 84360),
    ('b279657c-3f63-4aa1-92e8-d99f47ba1581', '36c8c2ed-7543-47de-bc42-14d163a6277d', daterange('2023-08-01', NULL, '[]'), 90100, 0.60, 54060, 140600, 0.60, 84360),
    ('92658f48-0320-4d19-8d56-6fe50c8bf285', '9eb82822-82ef-46fb-a123-29cf0af757b7', daterange('2023-08-01', NULL, '[]'), 90100, 0.80, 72080, 140600, 0.80, 112480),
    ('3c3c1e2b-cac4-418e-941d-2db046d4ced9', 'f5d32585-2c78-4434-95d6-30b446db7d4d', daterange('2023-08-01', NULL, '[]'), 90100, 1.00, 90100, 140600, 1.00, 140600),
    ('0ec6c2c9-ae7c-46a1-b111-f0be593abcd0', '94e44ef1-106b-401d-81b6-8e5c31cd0437', daterange('2023-08-01', '2024-07-31', '[]'), 0, 0, 0, 0, 0, 0),
    ('3d32b3f0-9033-437f-928d-3167def161aa', '93a50270-3f75-4672-b17d-db721bcb8ed2', daterange('2023-08-01', NULL, '[]'), 90100, 0.50, 45050, 140600, 0.50, 70300),
    ('9d64811c-40ca-4e90-ba29-ca8fb36e01b4', '13920018-e4ce-4fd0-928c-673965a3ab19', daterange('2023-08-01', NULL, '[]'), 90100, 0.50, 45050, 140600, 0.50, 70300),
    ('920c3c3d-e4e1-4226-8207-78135eabacbb', 'fe0972a5-6ce9-41cc-a635-82fb22e7891b', daterange('2023-08-01', NULL, '[]'), 90100, 0.50, 45050, 140600, 0.50, 70300),
    ('91a9e71d-fe1e-4b72-8851-6d1014e3b809', 'b3102992-df96-45d5-a1c3-578791c2193c', daterange('2023-08-01', NULL, '[]'), 90100, 0.50, 45050, 140600, 0.50, 70300),
    -- 2024-08-01 -
    ('5097b7ee-f707-486b-ac89-a2bf22ff8aef', 'f9e7d841-49bf-43e5-8c65-028dad590a76', daterange('2024-08-01', NULL, '[]'), 94900, 0.50, 47450, 148000, 0.50, 74000),
    ('56a14601-8453-4928-9370-b9e45e18564b', '50358394-b961-11eb-b51f-67ac436e5637', daterange('2024-08-01', NULL, '[]'), 94900, 0.50, 47450, 148000, 0.50, 74000),
    ('6cf9cc60-8c94-4e15-a8a5-bc3692ac601a', '86ef70a0-bf85-11eb-91e6-1fb57a101165', daterange('2024-08-01', NULL, '[]'), 94900, 0.80, 75920, 148000, 0.80, 118400),
    ('c9007b40-a70a-4e82-97b9-88a791dc2071', '503590f0-b961-11eb-b520-53740af3f7ef', daterange('2024-08-01', NULL, '[]'), 94900, 1.00, 94900, 148000, 1.00, 148000),
    ('3129e833-6768-4466-8f68-97eab63ccf0b', '503591ae-b961-11eb-b521-1fca99358eed', daterange('2024-08-01', NULL, '[]'), 94900, 1.00, 94900, 148000, 1.00, 148000),
    ('e0a5384b-cd8a-4a44-84ad-7e8d9bfc2f61', '94e44ef1-106b-401d-81b6-8e5c31cd0437', daterange('2024-08-01', NULL, '[]'), 0, 0, 0, 0, 0, 0),
    ('d9ee44f3-0cb0-4f66-a4df-7dfd0f56bcbf', '21a0589d-d4db-4978-9a37-8e6993a7dafd', daterange('2024-08-01', NULL, '[]'), 94900, 0.50, 47450, 148000, 0.50, 74000),
    ('92cb65db-d2b2-476f-a872-d7fc03fb2d21', '1c5d7ea4-669f-4b4e-8593-353be4c9cea0', daterange('2024-08-01', NULL, '[]'), 94900, 0.50, 47450, 148000, 0.50, 74000),
    ('2811d68a-f909-4530-9097-68f882a93e8f', '9e6a4660-2f83-40e3-bf27-d9590e93dbf2', daterange('2024-08-01', NULL, '[]'), 94900, 0.50, 47450, 148000, 0.50, 74000),
    ('1f07e892-4e9f-4d37-9acc-c81df42f3fad', '3b94630b-e01e-4b61-b040-8910baf96e97', daterange('2024-08-01', NULL, '[]'), 94900, 0.50, 47450, 148000, 0.50, 74000),
    ('1cb0cc18-59ff-4a8d-8c36-2d54a90d0c02', '593d8cbf-fcf8-41a7-a5fa-8fe96a7b93d8', daterange('2024-08-01', NULL, '[]'), 94900, 0.50, 47450, 148000, 0.50, 74000),
    ('b8c5c2f2-217f-48eb-99f7-175d83d20c74', 'c169bce7-6533-4409-8acd-445061f1ff34', daterange('2024-08-01', NULL, '[]'), 94900, 0.50, 47450, 148000, 0.50, 74000),
    ('c6eb0cdb-c9c0-40e7-8206-b425cf78991d', '9cdef927-f31a-45cb-af49-3ea5b1c1cb8a', daterange('2024-08-01', NULL, '[]'), 94900, 0.50, 47450, 148000, 0.50, 74000),
    ('9b3b7fb2-bbd8-488e-ad14-7f2e1e77fbc2', '0a58d934-6fd1-11ed-a75e-c353faef5858', daterange('2024-08-01', NULL, '[]'), 94900, 0.50, 47450, 148000, 0.50, 74000),
    ('7e848986-6472-4d1b-a9af-8be257980a02', '0a58db0a-6fd1-11ed-a75e-bbde95c1aded', daterange('2024-08-01', NULL, '[]'), 94900, 0.50, 47450, 148000, 0.50, 74000),
    ('7d22aabc-a8f7-4925-882c-693b62a8ce81', '0a58dbe6-6fd1-11ed-a75e-5335f2b9a91c', daterange('2024-08-01', NULL, '[]'), 94900, 0.50, 47450, 148000, 0.50, 74000),
    ('97b07714-be83-480d-b8b1-219a960e34b0', '0a58da38-6fd1-11ed-a75e-9b2790b0b4f5', daterange('2024-08-01', NULL, '[]'), 94900, 0.50, 47450, 148000, 0.50, 74000),
    ('87328f80-d4f3-45ef-a2c0-8c654d23afe7', '0a58dcae-6fd1-11ed-a75e-b3e10433b949', daterange('2024-08-01', NULL, '[]'), 94900, 0.50, 47450, 148000, 0.50, 74000)
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
