-- SPDX-FileCopyrightText: 2024 Tampere region
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

INSERT INTO assistance_action_option
    (value, name_fi, description_fi, display_order, category)
VALUES
    ('10', 'Avustajapalvelut', 'Lapsen ryhmässä työskentelee ryhmäavustaja tai lapsella on henkilökohtainen avustaja.', 80, 'DAYCARE'),
    ('40', 'Henkilökuntalisäys tai -muutos', 'Henkilökuntalisäys: Lapsen ryhmässä työskentelee lain vaatimaan resurssia enemmän varhaiskasvatuksen lastenhoitajia/sosionomeja/opettajia. Henkilökuntamuutos: Ryhmää on vahvistettu pedagogisesti siten, että henkilöstöön kuuluu kaksi varhaiskasvatuksen opettajaa. Mikäli lapsi on erityisryhmässä tai integroidussa ryhmässä, tätä vaihtoehtoa ei valita.', 90, 'DAYCARE'),
    ('50', 'Tuettu varhaiskasvatusryhmä', 'Lapsi on tuetussa varhaiskasvatusryhmässä.', 30, 'DAYCARE'),
    ('55', 'Tuettu esiopetusryhmä', 'Lapsi on tuetussa esiopetusryhmässä.', 40, 'DAYCARE'),
    ('57', 'Vaativan erityisen tuen varhaiskasvatusryhmä', 'Lapsen ryhmässä työskentelee yhtenä työntekijänä varhaiskasvatuksen erityisopettaja.', 50, 'DAYCARE'),
    ('60', 'Vaativan erityisen tuen perusopetuksen ryhmä/esiopetus', 'Lapsi saa osa-aikaista erityisopetusta.', 60, 'DAYCARE'),
    ('70', 'Erityisopettajan antama säännöllinen tuki', 'Ryhmän pienennys rakenteellisen tuen muotona eli lapsiryhmää pienennetään, jotta lasten tuki toteutuu tarkoituksenmukaisesti. Kirjaa myös tuen kerroin kohtaan Tuen tarve.', 70, 'DAYCARE')
ON CONFLICT (value) DO
UPDATE SET
    name_fi = EXCLUDED.name_fi,
    description_fi = EXCLUDED.description_fi,
    display_order = EXCLUDED.display_order,
    category = EXCLUDED.category
WHERE
    assistance_action_option.name_fi <> EXCLUDED.name_fi OR
    assistance_action_option.description_fi IS DISTINCT FROM EXCLUDED.description_fi OR
    assistance_action_option.display_order <> EXCLUDED.display_order OR
    assistance_action_option.category <> EXCLUDED.category;
