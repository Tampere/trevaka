// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.lempaala.evaka.mealintagration

import fi.espoo.evaka.mealintegration.MealType
import fi.lempaala.evaka.mealintegration.LempaalaMealTypeMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LempaalaMealTypeMapper {
    @Test
    fun testLempaalaMealTypeMapper() {
        val mapper = LempaalaMealTypeMapper
        // Normal diet tests
        assertEquals(6, mapper.toMealId(MealType.BREAKFAST, false))
        assertEquals(24, mapper.toMealId(MealType.LUNCH, false))
        assertEquals(41, mapper.toMealId(MealType.SNACK, false))
        assertEquals(67, mapper.toMealId(MealType.SUPPER, false))
        assertEquals(74, mapper.toMealId(MealType.EVENING_SNACK, false))
        // Special diet tests
        assertEquals(6, mapper.toMealId(MealType.BREAKFAST, true))
        assertEquals(25, mapper.toMealId(MealType.LUNCH, true))
        assertEquals(41, mapper.toMealId(MealType.SNACK, true))
        assertEquals(69, mapper.toMealId(MealType.SUPPER, true))
        assertEquals(74, mapper.toMealId(MealType.EVENING_SNACK, true))
    }
}
