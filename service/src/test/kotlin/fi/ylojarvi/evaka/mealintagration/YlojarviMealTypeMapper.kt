// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.ylojarvi.evaka.mealintagration

import fi.espoo.evaka.mealintegration.MealType
import fi.ylojarvi.evaka.mealintegration.YlojarviMealTypeMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class YlojarviMealTypeMapper {

    @Test
    fun testYlojarviMealTypeMapper() {
        val mapper = YlojarviMealTypeMapper
        // Normal diet tests
        assertEquals(1, mapper.toMealId(MealType.BREAKFAST, false))
        assertEquals(3, mapper.toMealId(MealType.LUNCH, false))
        assertEquals(7, mapper.toMealId(MealType.SNACK, false))
        assertEquals(9, mapper.toMealId(MealType.SUPPER, false))
        assertEquals(11, mapper.toMealId(MealType.EVENING_SNACK, false))
        // Special diet tests
        assertEquals(2, mapper.toMealId(MealType.BREAKFAST, true))
        assertEquals(4, mapper.toMealId(MealType.LUNCH, true))
        assertEquals(8, mapper.toMealId(MealType.SNACK, true))
        assertEquals(10, mapper.toMealId(MealType.SUPPER, true))
        assertEquals(12, mapper.toMealId(MealType.EVENING_SNACK, true))
    }
}
