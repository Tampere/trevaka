INSERT INTO fee_thresholds (
    id,
    valid_during,
    min_income_threshold_2, min_income_threshold_3, min_income_threshold_4, min_income_threshold_5, min_income_threshold_6,
    income_multiplier_2, income_multiplier_3, income_multiplier_4, income_multiplier_5, income_multiplier_6,
    max_income_threshold_2, max_income_threshold_3, max_income_threshold_4, max_income_threshold_5, max_income_threshold_6,
    income_threshold_increase_6_plus,
    sibling_discount_2, sibling_discount_2_plus,
    max_fee, min_fee
) VALUES (
    '51c2ec8a-bc76-40b3-9b5e-abba4042e361',
    daterange('2000-01-01', '2020-07-31', '[]'),
    210200, 271300, 308000, 344700, 381300,
    0.1070, 0.1070, 0.1070, 0.1070, 0.1070,
    479900, 541000, 577700, 614400, 651000,
    14200,
    0.5, 0.8,
    28900, 2700
),
(
    '236e3ee8-a97f-11ea-889d-eb365ac53e7c',
    daterange('2020-08-01', NULL),
    213600, 275600, 312900, 350200, 387400,
    0.1070, 0.1070, 0.1070, 0.1070, 0.1070,
    482300, 544300, 581600, 618900, 656100,
    14200,
    0.5, 0.8,
    28800, 2700
)
ON CONFLICT (id) DO
UPDATE SET
    valid_during = EXCLUDED.valid_during,
    min_income_threshold_2 = EXCLUDED.min_income_threshold_2,
    min_income_threshold_3 = EXCLUDED.min_income_threshold_3,
    min_income_threshold_4 = EXCLUDED.min_income_threshold_4,
    min_income_threshold_5 = EXCLUDED.min_income_threshold_5,
    min_income_threshold_6 = EXCLUDED.min_income_threshold_6,
    income_multiplier_2 = EXCLUDED.income_multiplier_2,
    income_multiplier_3 = EXCLUDED.income_multiplier_3,
    income_multiplier_4 = EXCLUDED.income_multiplier_4,
    income_multiplier_5 = EXCLUDED.income_multiplier_5,
    income_multiplier_6 = EXCLUDED.income_multiplier_6,
    max_income_threshold_2 = EXCLUDED.max_income_threshold_2,
    max_income_threshold_3 = EXCLUDED.max_income_threshold_3,
    max_income_threshold_4 = EXCLUDED.max_income_threshold_4,
    max_income_threshold_5 = EXCLUDED.max_income_threshold_5,
    max_income_threshold_6 = EXCLUDED.max_income_threshold_6,
    income_threshold_increase_6_plus = EXCLUDED.income_threshold_increase_6_plus,
    sibling_discount_2 = EXCLUDED.sibling_discount_2,
    sibling_discount_2_plus = EXCLUDED.sibling_discount_2_plus,
    max_fee = EXCLUDED.max_fee,
    min_fee = EXCLUDED.min_fee
WHERE
    fee_thresholds.valid_during <> EXCLUDED.valid_during OR
    fee_thresholds.min_income_threshold_2 <> EXCLUDED.min_income_threshold_2 OR
    fee_thresholds.min_income_threshold_3 <> EXCLUDED.min_income_threshold_3 OR
    fee_thresholds.min_income_threshold_4 <> EXCLUDED.min_income_threshold_4 OR
    fee_thresholds.min_income_threshold_5 <> EXCLUDED.min_income_threshold_5 OR
    fee_thresholds.min_income_threshold_6 <> EXCLUDED.min_income_threshold_6 OR
    fee_thresholds.income_multiplier_2 <> EXCLUDED.income_multiplier_2 OR
    fee_thresholds.income_multiplier_3 <> EXCLUDED.income_multiplier_3 OR
    fee_thresholds.income_multiplier_4 <> EXCLUDED.income_multiplier_4 OR
    fee_thresholds.income_multiplier_5 <> EXCLUDED.income_multiplier_5 OR
    fee_thresholds.income_multiplier_6 <> EXCLUDED.income_multiplier_6 OR
    fee_thresholds.max_income_threshold_2 <> EXCLUDED.max_income_threshold_2 OR
    fee_thresholds.max_income_threshold_3 <> EXCLUDED.max_income_threshold_3 OR
    fee_thresholds.max_income_threshold_4 <> EXCLUDED.max_income_threshold_4 OR
    fee_thresholds.max_income_threshold_5 <> EXCLUDED.max_income_threshold_5 OR
    fee_thresholds.max_income_threshold_6 <> EXCLUDED.max_income_threshold_6 OR
    fee_thresholds.income_threshold_increase_6_plus <> EXCLUDED.income_threshold_increase_6_plus OR
    fee_thresholds.sibling_discount_2 <> EXCLUDED.sibling_discount_2 OR
    fee_thresholds.sibling_discount_2_plus <> EXCLUDED.sibling_discount_2_plus OR
    fee_thresholds.max_fee <> EXCLUDED.max_fee OR
    fee_thresholds.min_fee <> EXCLUDED.min_fee;
