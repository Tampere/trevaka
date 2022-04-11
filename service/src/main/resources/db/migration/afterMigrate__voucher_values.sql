-- SPDX-FileCopyrightText: 2021 City of Tampere
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

INSERT INTO voucher_value
    (id, validity, base_value, age_under_three_coefficient)
VALUES
    ('084314dc-ed7f-4725-92f2-5c220bb4bb7e', daterange('2000-01-01', NULL, '[]'), 82600, 1.51)
ON CONFLICT (id) DO
UPDATE SET
    validity = EXCLUDED.validity,
    base_value = EXCLUDED.base_value,
    age_under_three_coefficient = EXCLUDED.age_under_three_coefficient
WHERE
    voucher_value.validity <> EXCLUDED.validity OR
    voucher_value.base_value <> EXCLUDED.base_value OR
    voucher_value.age_under_three_coefficient <> EXCLUDED.age_under_three_coefficient;
