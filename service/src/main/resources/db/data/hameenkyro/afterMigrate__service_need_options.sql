-- SPDX-FileCopyrightText: 2024 Tampere region
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

INSERT INTO service_need_option
(id, name_fi, name_sv, name_en, valid_placement_type, default_option, fee_coefficient, occupancy_coefficient, occupancy_coefficient_under_3y, realized_occupancy_coefficient, realized_occupancy_coefficient_under_3y, daycare_hours_per_week, contract_days_per_month, daycare_hours_per_month, part_day, part_week, fee_description_fi, fee_description_sv, voucher_value_description_fi, voucher_value_description_sv, valid_from, valid_to, show_for_citizen, display_order)
VALUES
    ('f9e7d841-49bf-43e5-8c65-028dad590a76', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Daycare - 85 h per month', 'DAYCARE', TRUE, 0.5, 1.0, 1.75, 1.0, 1.75, 40, NULL, 85, TRUE, TRUE, 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', '2023-08-01', NULL, FALSE, NULL),
    ('50358394-b961-11eb-b51f-67ac436e5637', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Daycare - 85 h per month', 'DAYCARE', FALSE, 0.5, 1.0, 1.75, 1.0, 1.75, 40, NULL, 85, TRUE, TRUE, 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 0),
    ('86ef70a0-bf85-11eb-91e6-1fb57a101165', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Daycare - 86-120 h per month', 'DAYCARE', FALSE, 0.8, 1.0, 1.75, 1.0, 1.75, 40, NULL, 120, TRUE, TRUE, 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', 'Varhaiskasvatus 86–120 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 1),
    ('503590f0-b961-11eb-b520-53740af3f7ef', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Daycare - 121-150 h per month', 'DAYCARE', FALSE, 0.9, 1.0, 1.75, 1.0, 1.75, 40, NULL, 150, TRUE, TRUE, 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', 'Varhaiskasvatus 121-150 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 2),
    ('503591ae-b961-11eb-b521-1fca99358eed', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Daycare - over 151 h per month', 'DAYCARE', FALSE, 1.0, 1.0, 1.75, 1.0, 1.75, 40, NULL, 210, FALSE, FALSE, 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 3),
    ('36c8c2ed-7543-47de-bc42-14d163a6277d', 'Palveluseteli Varhaiskasvatus 85 tuntia / kuukausi', 'Palveluseteli Varhaiskasvatus 85 tuntia / kuukausi', 'Voucher value Daycare - 85 h per month', 'DAYCARE', FALSE, 0.5, 1.0, 1.75, 1.0, 1.75, 40, NULL, 85, TRUE, TRUE, 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', '2023-08-01', '2024-07-31', TRUE, 4),
    ('9eb82822-82ef-46fb-a123-29cf0af757b7', 'Palveluseteli Varhaiskasvatus 86–139 tuntia / kuukausi', 'Palveluseteli Varhaiskasvatus 86–139 tuntia / kuukausi', 'Voucher value Daycare - 86-139 h per month', 'DAYCARE', FALSE, 0.8, 1.0, 1.75, 1.0, 1.75, 40, NULL, 139, TRUE, TRUE, 'Varhaiskasvatus 86–139 tuntia / kuukausi', 'Varhaiskasvatus 86–139 tuntia / kuukausi', 'Varhaiskasvatus 86–139 tuntia / kuukausi', 'Varhaiskasvatus 86–139 tuntia / kuukausi', '2023-08-01', '2024-07-31', TRUE, 5),
    ('f5d32585-2c78-4434-95d6-30b446db7d4d', 'Palveluseteli Varhaiskasvatus yli 140 tuntia / kuukausi', 'Palveluseteli Varhaiskasvatus yli 140 tuntia / kuukausi', 'Voucher value Daycare - over 140 h per month', 'DAYCARE', FALSE, 1.0, 1.0, 1.75, 1.0, 1.75, 40, NULL, 210, FALSE, FALSE, 'Varhaiskasvatus yli 140 tuntia / kuukausi', 'Varhaiskasvatus yli 140 tuntia / kuukausi', 'Varhaiskasvatus yli 140 tuntia / kuukausi', 'Varhaiskasvatus yli 140 tuntia / kuukausi', '2023-08-01', '2024-07-31', TRUE, 6),
    ('1b1e6e91-8d54-405c-88f8-2a95d88f8962', 'Palveluseteli pph Varhaiskasvatus 85 tuntia / kuukausi', 'Palveluseteli pph Varhaiskasvatus 85 tuntia / kuukausi', 'Voucher value cm Daycare - 85 h per month', 'DAYCARE', FALSE, 0.5, 1.0, 1.75, 1.0, 1.75, 40, NULL, 85, TRUE, TRUE, 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', '2023-08-01', '2024-07-31', TRUE, 7),
    ('1a2e76e8-64a1-4058-9d86-90753debbead', 'Palveluseteli pph Varhaiskasvatus 85 tuntia / kuukausi', 'Palveluseteli pph Varhaiskasvatus 85 tuntia / kuukausi', 'Voucher value cm Daycare - 85 h per month', 'DAYCARE', FALSE, 0.9, 1.0, 1.75, 1.0, 1.75, 40, NULL, 85, TRUE, TRUE, 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', 'Varhaiskasvatus 85 tuntia / kuukausi', '2024-08-01', NULL, TRUE, 7),
    ('4d6d632d-d8cf-4b5a-8437-decade30d0c0', 'Palveluseteli pph Varhaiskasvatus yli 151 tuntia / kuukausi', 'Palveluseteli pph Varhaiskasvatus yli 151 tuntia / kuukausi', 'Voucher value cm Daycare - over 151 h per month', 'DAYCARE', FALSE, 1.0, 1.0, 1.75, 1.0, 1.75, 40, NULL, 210, FALSE, FALSE, 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', 'Varhaiskasvatus yli 151 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 8),
    ('e1063bee-c19d-469d-85a5-6b0350872d76', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Temporary daycare', 'TEMPORARY_DAYCARE', TRUE, 1.0, 1.0, 1.75, 1.0, 1.75, 40, NULL, NULL, FALSE, FALSE, 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', 'Tilapäinen varhaiskasvatus', '2023-08-01', NULL, FALSE, NULL),
    ('94e44ef1-106b-401d-81b6-8e5c31cd0437', 'Esiopetus', 'Esiopetus', 'Preschool', 'PRESCHOOL', TRUE, 0.0, 0.5, 0.5, 0.5, 0.5, 0, NULL, NULL, TRUE, TRUE, 'Esiopetus', 'Esiopetus', 'Esiopetus', 'Esiopetus', '2023-08-01', NULL, FALSE, NULL),
    ('1a4b0997-b83d-44ec-8bd9-98b12e5d6d04', 'Valmistava opetus', 'Valmistava opetus', 'Preparatory education', 'PREPARATORY', TRUE, 0.0, 0.5, 0.5, 0.5, 0.5, 0, NULL, NULL, TRUE, TRUE, 'Valmistava opetus', 'Valmistava opetus', 'Valmistava opetus', 'Valmistava opetus', '2023-08-01', NULL, FALSE, NULL),
    ('4e02d217-947e-41c7-a147-73111d2dc753', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Prepatatory daycare - 0–50 h per month', 'PREPARATORY_DAYCARE', TRUE, 0.3, 1.0, 1.0, 1.0, 1.0, 40, NULL, 50, TRUE, TRUE, 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', '2023-08-01', NULL, FALSE, NULL),
    ('025d0374-d36d-4a9b-b014-46147e6ea13f', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Prepatatory daycare - 0–50 h per month', 'PREPARATORY_DAYCARE', FALSE, 0.3, 1.0, 1.0, 1.0, 1.0, 40, NULL, 50, TRUE, TRUE, 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', '2023-08-01', NULL, FALSE, 4),
    ('03c11898-1056-4e6e-be35-5e0886445d13', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Prepatatory daycare – 51-85 h per month', 'PREPARATORY_DAYCARE', FALSE, 0.5, 1.0, 1.0, 1.0, 1.0, 40, NULL, 85, TRUE, TRUE, 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', '2023-08-01', NULL, FALSE, 5),
    ('114fc456-7e61-47c0-a88e-6743181d1cfc', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Prepatatory daycare – 68-120 h per month', 'PREPARATORY_DAYCARE', FALSE, 0.8, 1.0, 1.0, 1.0, 1.0, 40, NULL, 120, TRUE, TRUE, 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', '2023-08-01', NULL, FALSE, 6),
    ('16f1be29-e979-403b-9002-251065c85d78', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Prepatatory daycare – 121-150 h per month', 'PREPARATORY_DAYCARE', FALSE, 0.9, 1.0, 1.0, 1.0, 1.0, 40, NULL, 150, TRUE, TRUE, 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', '2023-08-01', NULL, FALSE, 7),
    ('689d4104-b1ad-4dd5-82ae-6640196a070a', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Prepatatory daycare – over 151 h per month', 'PREPARATORY_DAYCARE', FALSE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, 210, FALSE, FALSE, 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', '2023-08-01', NULL, FALSE, 8),
    ('01610c55-ecb8-4446-ab04-fdc631428649', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Prepatatory daycare - 0–50 h per month', 'PREPARATORY_DAYCARE_ONLY', TRUE, 0.3, 1.0, 1.0, 1.0, 1.0, 40, NULL, 50, TRUE, TRUE, 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', '2023-08-01', NULL, FALSE, NULL),
    ('a83bf292-e33e-4733-9233-4ff052b14982', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Prepatatory daycare - 0–50 h per month', 'PREPARATORY_DAYCARE_ONLY', FALSE, 0.3, 1.0, 1.0, 1.0, 1.0, 40, NULL, 50, TRUE, TRUE, 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', '2023-08-01', NULL, FALSE, 4),
    ('7e87b656-8ee6-4f77-bf71-00343862c405', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Prepatatory daycare – 51-85 h per month', 'PREPARATORY_DAYCARE_ONLY', FALSE, 0.5, 1.0, 1.0, 1.0, 1.0, 40, NULL, 85, TRUE, TRUE, 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', '2023-08-01', NULL, FALSE, 5),
    ('49aca03f-1cc1-4b9d-abb5-7a0638b39b02', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Prepatatory daycare – 68-120 h per month', 'PREPARATORY_DAYCARE_ONLY', FALSE, 0.8, 1.0, 1.0, 1.0, 1.0, 40, NULL, 120, TRUE, TRUE, 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', '2023-08-01', NULL, FALSE, 6),
    ('256b9feb-fd78-453b-b7ca-fa8a41e87360', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Prepatatory daycare – 121-150 h per month', 'PREPARATORY_DAYCARE_ONLY', FALSE, 0.9, 1.0, 1.0, 1.0, 1.0, 40, NULL, 150, TRUE, TRUE, 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', '2023-08-01', NULL, FALSE, 7),
    ('785e1cbe-8eb9-431a-a33a-07b5b8578088', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Prepatatory daycare – over 151 h per month', 'PREPARATORY_DAYCARE_ONLY', FALSE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, 210, FALSE, FALSE, 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', '2023-08-01', NULL, FALSE, 8),
    ('21a0589d-d4db-4978-9a37-8e6993a7dafd', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Preschool daycare - 0–50 h per month', 'PRESCHOOL_DAYCARE', TRUE, 0.3, 1.0, 1.0, 1.0, 1.0, 40, NULL, 50, TRUE, TRUE, 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', '2023-08-01', NULL, FALSE, NULL),
    ('1c5d7ea4-669f-4b4e-8593-353be4c9cea0', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Preschool daycare - 0–50 h per month', 'PRESCHOOL_DAYCARE', FALSE, 0.3, 1.0, 1.0, 1.0, 1.0, 40, NULL, 50, TRUE, TRUE, 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 4),
    ('9e6a4660-2f83-40e3-bf27-d9590e93dbf2', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Preschool daycare – 51-85 h per month', 'PRESCHOOL_DAYCARE', FALSE, 0.5, 1.0, 1.0, 1.0, 1.0, 40, NULL, 85, TRUE, TRUE, 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 5),
    ('3b94630b-e01e-4b61-b040-8910baf96e97', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Preschool daycare – 68-120 h per month', 'PRESCHOOL_DAYCARE', FALSE, 0.8, 1.0, 1.0, 1.0, 1.0, 40, NULL, 120, TRUE, TRUE, 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 6),
    ('593d8cbf-fcf8-41a7-a5fa-8fe96a7b93d8', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Preschool daycare – 121-150 h per month', 'PRESCHOOL_DAYCARE', FALSE, 0.9, 1.0, 1.0, 1.0, 1.0, 40, NULL, 150, TRUE, TRUE, 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 7),
    ('c169bce7-6533-4409-8acd-445061f1ff34', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Preschool daycare – over 151 h per month', 'PRESCHOOL_DAYCARE', FALSE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, 210, FALSE, FALSE, 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', '2023-08-01', NULL, TRUE, 8),
    ('93a50270-3f75-4672-b17d-db721bcb8ed2', 'Palveluseteli Täydentävä varhaiskasvatus', 'Palveluseteli Täydentävä varhaiskasvatus', 'Voucher value Preschool daycare', 'PRESCHOOL_DAYCARE', FALSE, 0.5, 1.0, 1.0, 1.0, 1.0, 40, NULL, 210, FALSE, FALSE, 'Täydentävä varhaiskasvatus', 'Täydentävä varhaiskasvatus', 'Täydentävä varhaiskasvatus', 'Täydentävä varhaiskasvatus', '2023-08-01', NULL, TRUE, 9),
    ('9cdef927-f31a-45cb-af49-3ea5b1c1cb8a', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Preschool daycare - 0–50 h per month', 'PRESCHOOL_DAYCARE_ONLY', TRUE, 0.3, 1.0, 1.0, 1.0, 1.0, 40, NULL, 50, TRUE, TRUE, 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', '2023-08-01', NULL, FALSE, NULL),
    ('0a58d934-6fd1-11ed-a75e-c353faef5858', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Preschool daycare - 0–50 h per month', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 0.3, 1.0, 1.0, 1.0, 1.0, 40, NULL, 50, TRUE, TRUE, 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 0–50 tuntia / kuukausi', '2023-08-01', NULL, FALSE, 4),
    ('0a58db0a-6fd1-11ed-a75e-bbde95c1aded', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Preschool daycare – 51-85 h per month', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 0.5, 1.0, 1.0, 1.0, 1.0, 40, NULL, 85, TRUE, TRUE, 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi', '2023-08-01', NULL, FALSE, 5),
    ('0a58dbe6-6fd1-11ed-a75e-5335f2b9a91c', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Preschool daycare – 68-120 h per month', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 0.8, 1.0, 1.0, 1.0, 1.0, 40, NULL, 120, TRUE, TRUE, 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 86–120 tuntia / kuukausi', '2023-08-01', NULL, FALSE, 6),
    ('0a58da38-6fd1-11ed-a75e-9b2790b0b4f5', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Preschool daycare – 121-150 h per month', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 0.9, 1.0, 1.0, 1.0, 1.0, 40, NULL, 150, TRUE, TRUE, 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', 'Täydentävä varhaiskasvatus 121–150 tuntia / kuukausi', '2023-08-01', NULL, FALSE, 7),
    ('0a58dcae-6fd1-11ed-a75e-b3e10433b949', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Preschool daycare – over 151 h per month', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, 210, FALSE, FALSE, 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', 'Täydentävä varhaiskasvatus yli 151 tuntia / kuukausi', '2023-08-01', NULL, FALSE, 8),
    ('fe0972a5-6ce9-41cc-a635-82fb22e7891b', 'Palveluseteli Täydentävä varhaiskasvatus', 'Palveluseteli Täydentävä varhaiskasvatus', 'Voucher value Preschool daycare', 'PRESCHOOL_DAYCARE_ONLY', FALSE, 0.5, 1.0, 1.0, 1.0, 1.0, 40, NULL, 210, FALSE, FALSE, 'Täydentävä varhaiskasvatus', 'Täydentävä varhaiskasvatus', 'Täydentävä varhaiskasvatus', 'Täydentävä varhaiskasvatus', '2023-08-01', NULL, FALSE, 9),
    ('28396800-b51d-402a-ab20-9d13a476752e', 'Koululaisen vuorohoito', 'Koululaisen vuorohoito', 'School shift care', 'SCHOOL_SHIFT_CARE', TRUE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, NULL, TRUE, FALSE, 'Koululaisen vuorohoito', 'Koululaisen vuorohoito', 'Koululaisen vuorohoito', 'Koululaisen vuorohoito', '2023-08-01', NULL, FALSE, NULL),
    ('81da39d0-3523-11ef-bbd7-ef1ea86cea4c', 'Koululaisen vuorohoito', 'Koululaisen vuorohoito', 'School shift care', 'SCHOOL_SHIFT_CARE', FALSE, 1.0, 1.0, 1.0, 1.0, 1.0, 40, NULL, NULL, TRUE, FALSE, 'Koululaisen vuorohoito', 'Koululaisen vuorohoito', 'Koululaisen vuorohoito', 'Koululaisen vuorohoito', '2023-08-01', NULL, FALSE, NULL)
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
