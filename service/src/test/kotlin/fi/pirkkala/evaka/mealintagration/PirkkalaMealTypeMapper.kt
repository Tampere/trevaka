// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.pirkkala.evaka.mealintagration

import fi.espoo.evaka.mealintegration.MealType
import fi.pirkkala.evaka.mealintegration.PirkkalaMealTypeMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PirkkalaMealTypeMapper {

    @Test
    fun testPirkkalaMealTypeMapper() {
        val mapper = PirkkalaMealTypeMapper
        // Normal diet tests
        assertEquals(26, mapper.toMealId(MealType.BREAKFAST, false))
        assertEquals(32, mapper.toMealId(MealType.LUNCH, false))
        assertEquals(39, mapper.toMealId(MealType.SNACK, false))
        assertEquals(44, mapper.toMealId(MealType.SUPPER, false))
        assertEquals(49, mapper.toMealId(MealType.EVENING_SNACK, false))
        // Special diet tests
        assertEquals(28, mapper.toMealId(MealType.BREAKFAST, true))
        assertEquals(33, mapper.toMealId(MealType.LUNCH, true))
        assertEquals(40, mapper.toMealId(MealType.SNACK, true))
        assertEquals(45, mapper.toMealId(MealType.SUPPER, true))
        assertEquals(50, mapper.toMealId(MealType.EVENING_SNACK, true))
    }
}
