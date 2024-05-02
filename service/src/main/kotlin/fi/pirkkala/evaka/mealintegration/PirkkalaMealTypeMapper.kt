// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.pirkkala.evaka.mealintegration

import fi.espoo.evaka.mealintegration.MealType
import fi.espoo.evaka.mealintegration.MealTypeMapper

object PirkkalaMealTypeMapper : MealTypeMapper {
    override fun toMealId(mealType: MealType, specialDiet: Boolean): Int =
        if (specialDiet) {
            when (mealType) {
                MealType.BREAKFAST -> 28
                MealType.LUNCH -> 33
                MealType.LUNCH_PRESCHOOL -> 33
                MealType.SNACK -> 40
                MealType.SUPPER -> 45
                MealType.EVENING_SNACK -> 50
            }
        } else {
            when (mealType) {
                MealType.BREAKFAST -> 26
                MealType.LUNCH -> 32
                MealType.LUNCH_PRESCHOOL -> 32
                MealType.SNACK -> 39
                MealType.SUPPER -> 44
                MealType.EVENING_SNACK -> 49
            }
        }
}
