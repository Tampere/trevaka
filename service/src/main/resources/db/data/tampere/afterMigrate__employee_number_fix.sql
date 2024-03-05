-- SPDX-FileCopyrightText: 2023-2024 Tampere region
--
-- SPDX-License-Identifier: LGPL-2.1-or-later

UPDATE employee
SET employee_number = lower(employee_number)
WHERE employee_number != lower(employee_number);
