-- SPDX-FileCopyrightText: 2021 City of Tampere
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

INSERT INTO voucher_value
    (id, validity, base_value, base_value_age_under_three)
VALUES
    ('084314dc-ed7f-4725-92f2-5c220bb4bb7e', daterange('2000-01-01', '2022-07-31', '[]'), 82600, 124700),
    ('2f39fec0-d514-11ec-9d64-0242ac120002', daterange('2022-08-01', NULL, '[]'), 86200, 130200)
ON CONFLICT (id) DO
UPDATE SET
    validity = EXCLUDED.validity,
    base_value = EXCLUDED.base_value,
    base_value_age_under_three = EXCLUDED.base_value_age_under_three
WHERE
    voucher_value.validity <> EXCLUDED.validity OR
    voucher_value.base_value <> EXCLUDED.base_value OR
    voucher_value.base_value_age_under_three <> EXCLUDED.base_value_age_under_three;
