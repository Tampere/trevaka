// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.kangasala.evaka.mealintagration

import fi.espoo.evaka.mealintegration.DefaultMealTypeMapper
import fi.espoo.evaka.mealintegration.MealType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class KangasalaMealTypeMapper {
    @Test
    fun testKangasalaMealTypeMapper() {
        val mapper = DefaultMealTypeMapper
        // Normal diet tests
        assertEquals(162, mapper.toMealId(MealType.BREAKFAST, false))
        assertEquals(175, mapper.toMealId(MealType.LUNCH, false))
        assertEquals(152, mapper.toMealId(MealType.SNACK, false))
        assertEquals(27, mapper.toMealId(MealType.SUPPER, false))
        assertEquals(30, mapper.toMealId(MealType.EVENING_SNACK, false))
        // Special diet tests
        assertEquals(143, mapper.toMealId(MealType.BREAKFAST, true))
        assertEquals(145, mapper.toMealId(MealType.LUNCH, true))
        assertEquals(160, mapper.toMealId(MealType.SNACK, true))
        assertEquals(28, mapper.toMealId(MealType.SUPPER, true))
        assertEquals(31, mapper.toMealId(MealType.EVENING_SNACK, true))
    }
}
