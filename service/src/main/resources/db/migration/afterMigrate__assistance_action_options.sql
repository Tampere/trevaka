-- SPDX-FileCopyrightText: 2021 City of Tampere
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

INSERT INTO assistance_action_option
    (value, name_fi, description_fi, display_order)
VALUES
    ('10', 'Avustajapalvelut', 'Lapsen ryhmässä työskentelee ryhmäavustaja tai lapsella on henkilökohtainen avustaja. Mikäli lapsi on integroidussa varhaiskasvatusryhmässä tai erityisryhmässä, tätä vaihtoehtoa ei valita.', 10),
    ('20', 'Erho', NULL, 20),
    ('30', 'Erityisryhmä', 'Lapsi on erityisryhmässä.', 30),
    ('40', 'Henkilökuntalisäys', 'Lapsen ryhmässä työskentelee lain vaatimaa resurssia enemmän varhaiskasvatuksen lastenhoitajia/opettajia. Mikäli lapsi on integroidussa varhaiskasvatusryhmässä tai erityisryhmässä, tätä vaihtoehtoa ei valita.', 40),
    ('50', 'Integroitu varhaiskasvatusryhmä ', 'Lapsi on integroidussa varhaiskasvatusryhmässä.', 50),
    ('60', 'Osa-aikainen erityisopetus', 'Lapsi saa osa-aikaista erityisopetusta. Mikäli lapsi on integroidussa varhaiskasvatusryhmässä tai erityisryhmässä, tätä vaihtoehtoa ei valita.', 60),
    ('70', 'Veon tuki', 'Lapsen ryhmän henkilökunta saa veon tukea ja konsultaatiota ja/tai veo on samanaikaisopettajana lapsen ryhmässä. Mikäli lapsi on integroidussa varhaiskasvatusryhmässä tai erityisryhmässä, tätä vaihtoehtoa ei valita.', 70),
    ('80', 'Tulkitsemispalvelut', 'Lapsi saa tulkitsemispalveluita. Huoltajien kanssa käytettävät tulkkipalvelut eivät sisälly tähän.', 80),
    ('90', 'Kuljetusetu', 'Lapsi on saanut päätöksen kuljetusedusta.', 90),
    ('100', 'Ryhmän pienennys', 'Ryhmän pienennys rakenteellisen tuen muotona eli lapsiryhmää pienennetään, jotta lasten tuki toteutuu tarkoituksenmukaisesti. Kirjaa myös tuen kerroin sille varattuun paikkaan.', 100)
ON CONFLICT (value) DO
UPDATE SET
    name_fi = EXCLUDED.name_fi,
    description_fi = EXCLUDED.description_fi,
    display_order = EXCLUDED.display_order
WHERE
    assistance_action_option.name_fi <> EXCLUDED.name_fi OR
    assistance_action_option.description_fi IS DISTINCT FROM EXCLUDED.description_fi OR
    assistance_action_option.display_order <> EXCLUDED.display_order;
