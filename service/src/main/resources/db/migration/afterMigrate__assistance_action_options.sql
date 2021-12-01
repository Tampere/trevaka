-- SPDX-FileCopyrightText: 2021 City of Tampere
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

INSERT INTO assistance_action_option
    (value, name_fi, display_order)
VALUES
    ('10', 'Avustajapalvelut', 10),
    ('20', 'Erho', 20),
    ('30', 'Erityisryhmä', 30),
    ('40', 'Henkilökuntalisäys', 40),
    ('50', 'Integroitu varhaiskasvatusryhmä ', 50),
    ('60', 'Osa-aikainen erityisopetus', 60),
    ('70', 'Veon tuki', 70),
    ('80', 'Tulkitsemispalvelut', 80),
    ('90', 'Kuljetusetu', 90)
ON CONFLICT (value) DO
UPDATE SET
    name_fi = EXCLUDED.name_fi,
    display_order = EXCLUDED.display_order
WHERE
    assistance_action_option.name_fi <> EXCLUDED.name_fi OR
    assistance_action_option.display_order <> EXCLUDED.display_order;
