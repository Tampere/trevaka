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
        assertEquals(851, mapper.toMealId(MealType.BREAKFAST, false))
        assertEquals(853, mapper.toMealId(MealType.LUNCH, false))
        assertEquals(857, mapper.toMealId(MealType.SNACK, false))
        assertEquals(859, mapper.toMealId(MealType.SUPPER, false))
        assertEquals(861, mapper.toMealId(MealType.EVENING_SNACK, false))
        // Special diet tests
        assertEquals(852, mapper.toMealId(MealType.BREAKFAST, true))
        assertEquals(854, mapper.toMealId(MealType.LUNCH, true))
        assertEquals(858, mapper.toMealId(MealType.SNACK, true))
        assertEquals(860, mapper.toMealId(MealType.SUPPER, true))
        assertEquals(862, mapper.toMealId(MealType.EVENING_SNACK, true))
    }
}
