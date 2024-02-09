-- SPDX-FileCopyrightText: 2024 Tampere region
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

INSERT INTO assistance_action_option
    (value, name_fi, description_fi, display_order)
VALUES
    ('10', 'Avustajapalvelut', 'Lapsen ryhmässä työskentelee ryhmäavustaja tai lapsella on henkilökohtainen avustaja.', 30),
    ('40', 'Henkilökuntalisäys tai -muutos', 'Henkilökuntalisäys: Lapsen ryhmässä työskentelee lain vaatimaan resurssia enemmän varhaiskasvatuksen lastenhoitajia/sosionomeja/opettajia. Henkilökuntamuutos: Ryhmää on vahvistettu pedagogisesti siten, että henkilöstöön kuuluu kaksi varhaiskasvatuksen opettajaa. Mikäli lapsi on erityisryhmässä tai integroidussa ryhmässä, tätä vaihtoehtoa ei valita. Älä valitse tätä vaihtoehtoa esiopetuksessa.', 40),
    ('60', 'Osa-aikainen erityisopetus', 'Lapsi saa osa-aikaista erityisopetusta.', 10),
    ('70', 'Erityisopettajan konsultaatio', 'Lapsen ryhmän henkilökunta saa erityisopettajan konsultaatiota.', 20),
    ('80', 'Tulkitsemispalvelut', 'Lapsi saa tulkitsemispalveluita kuulo- ja/tai näkövammansa vuoksi. Huoltajien kanssa käytettävät tulkkipalvelut eivät sisälly tähän.', 60),
    ('100', 'Ryhmän pienennys', 'Ryhmän pienennys rakenteellisen tuen muotona eli lapsiryhmää pienennetään, jotta lasten tuki toteutuu tarkoituksenmukaisesti. Kirjaa myös tuen kerroin kohtaan Tuen tarve. Älä valitse tätä vaihtoehtoa esiopetuksessa.', 50)
ON CONFLICT (value) DO
UPDATE SET
    name_fi = EXCLUDED.name_fi,
    description_fi = EXCLUDED.description_fi,
    display_order = EXCLUDED.display_order
WHERE
    assistance_action_option.name_fi <> EXCLUDED.name_fi OR
    assistance_action_option.description_fi IS DISTINCT FROM EXCLUDED.description_fi OR
    assistance_action_option.display_order <> EXCLUDED.display_order;
