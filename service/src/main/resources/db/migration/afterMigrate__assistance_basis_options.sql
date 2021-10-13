-- SPDX-FileCopyrightText: 2021 City of Tampere
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

INSERT INTO assistance_basis_option
    (value, name_fi, description_fi, display_order)
VALUES
    ('DEVELOPMENTAL_DISABILITY', 'Kehitysvamma', NULL, 10)
ON CONFLICT (value) DO
UPDATE SET
    name_fi = EXCLUDED.name_fi,
    description_fi = EXCLUDED.description_fi,
    display_order = EXCLUDED.display_order
WHERE
    assistance_basis_option.name_fi <> EXCLUDED.name_fi OR
    assistance_basis_option.description_fi <> EXCLUDED.description_fi OR
    assistance_basis_option.display_order <> EXCLUDED.display_order;
