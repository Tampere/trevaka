INSERT INTO care_area
    (name, area_code, sub_cost_center, short_name)
VALUES
    ('Etelä', NULL, NULL, 'etela'),
    ('Itä', NULL, NULL, 'ita'),
    ('Länsi', NULL, NULL, 'lansi')
ON CONFLICT (short_name) DO
UPDATE SET
    name = EXCLUDED.name,
    area_code = EXCLUDED.area_code,
    sub_cost_center = EXCLUDED.sub_cost_center
WHERE
    care_area.name <> EXCLUDED.name OR
    care_area.area_code <> EXCLUDED.area_code OR
    care_area.sub_cost_center <> EXCLUDED.sub_cost_center;
