-- SPDX-FileCopyrightText: 2024 Tampere region
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

INSERT INTO assistance_action_option
    (value, name_fi, description_fi, display_order)
VALUES
    ('10', 'Avustajapalvelut', 'Lapsen ryhmässä työskentelee ryhmäavustaja tai lapsella on henkilökohtainen avustaja.', 80),
    ('20', 'Erho', NULL, 110),
    ('30', 'Varhaiskasvatuksen erityisryhmä', 'Lapsi on erityisryhmässä.', 10),
    ('35', 'Esiopetuksen erityisryhmä', NULL, 20),
    ('40', 'Henkilökuntalisäys tai -muutos', 'Henkilökuntalisäys: Lapsen ryhmässä työskentelee lain vaatimaan resurssia enemmän varhaiskasvatuksen lastenhoitajia/sosionomeja/opettajia. Henkilökuntamuutos: Ryhmää on vahvistettu pedagogisesti siten, että henkilöstöön kuuluu kaksi varhaiskasvatuksen opettajaa. Mikäli lapsi on erityisryhmässä tai integroidussa ryhmässä, tätä vaihtoehtoa ei valita. Älä valitse tätä vaihtoehtoa esiopetuksessa.', 90),
    ('50', 'Integroitu varhaiskasvatusryhmä', 'Lapsi on integroidussa varhaiskasvatusryhmässä.', 30),
    ('55', 'Integroitu esiopetusryhmä', 'Lapsi on integroidussa esiopetusryhmässä.', 40),
    ('57', 'Kokoaikainen erityisopetus', 'Lapsen ryhmässä työskentelee yhtenä työntekijänä varhaiskasvatuksen erityisopettaja.', 50),
    ('60', 'Osa-aikainen erityisopetus', 'Lapsi saa osa-aikaista erityisopetusta.', 60),
    ('70', 'Erityisopettajan konsultaatio', 'Lapsen ryhmän henkilökunta saa erityisopettajan konsultaatiota.', 70),
    ('80', 'Tulkitsemispalvelut', 'Lapsi saa tulkitsemispalveluita kuulo- ja/tai näkövammansa vuoksi. Huoltajien kanssa käytettävät tulkkipalvelut eivät sisälly tähän.', 120),
    ('100', 'Ryhmän pienennys', 'Ryhmän pienennys rakenteellisen tuen muotona eli lapsiryhmää pienennetään, jotta lasten tuki toteutuu tarkoituksenmukaisesti. Kirjaa myös tuen kerroin kohtaan Tuen tarve. Älä valitse tätä vaihtoehtoa esiopetuksessa.', 100)
ON CONFLICT (value) DO
UPDATE SET
    name_fi = EXCLUDED.name_fi,
    description_fi = EXCLUDED.description_fi,
    display_order = EXCLUDED.display_order
WHERE
    assistance_action_option.name_fi <> EXCLUDED.name_fi OR
    assistance_action_option.description_fi IS DISTINCT FROM EXCLUDED.description_fi OR
    assistance_action_option.display_order <> EXCLUDED.display_order;
