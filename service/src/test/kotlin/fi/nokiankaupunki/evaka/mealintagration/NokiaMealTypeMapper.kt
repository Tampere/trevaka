// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.nokiankaupunki.evaka.mealintagration

import fi.espoo.evaka.mealintegration.MealType
import fi.nokiankaupunki.evaka.mealintegration.NokiaMealTypeMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NokiaMealTypeMapper {

    @Test
    fun testNokiaMealTypeMapper() {
        val mapper = NokiaMealTypeMapper
        // Normal diet tests
        assertEquals(2, mapper.toMealId(MealType.BREAKFAST, false))
        assertEquals(4, mapper.toMealId(MealType.LUNCH, false))
        assertEquals(5, mapper.toMealId(MealType.SNACK, false))
        assertEquals(7, mapper.toMealId(MealType.SUPPER, false))
        assertEquals(8, mapper.toMealId(MealType.EVENING_SNACK, false))
        // Special diet tests
        assertEquals(13, mapper.toMealId(MealType.BREAKFAST, true))
        assertEquals(18, mapper.toMealId(MealType.LUNCH, true))
        assertEquals(6, mapper.toMealId(MealType.SNACK, true))
        assertEquals(23, mapper.toMealId(MealType.SUPPER, true))
        assertEquals(9, mapper.toMealId(MealType.EVENING_SNACK, true))
    }
}
