// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.lempaala.evaka.mealintegration

import fi.espoo.evaka.mealintegration.MealType
import fi.espoo.evaka.mealintegration.MealTypeMapper

object LempaalaMealTypeMapper : MealTypeMapper {
    override fun toMealId(mealType: MealType, specialDiet: Boolean): Int =
        if (specialDiet) {
            when (mealType) {
                MealType.BREAKFAST -> 6
                MealType.LUNCH -> 25
                MealType.LUNCH_PRESCHOOL -> 25
                MealType.SNACK -> 41
                MealType.SUPPER -> 69
                MealType.EVENING_SNACK -> 74
            }
        } else {
            when (mealType) {
                MealType.BREAKFAST -> 6
                MealType.LUNCH -> 24
                MealType.LUNCH_PRESCHOOL -> 24
                MealType.SNACK -> 41
                MealType.SUPPER -> 67
                MealType.EVENING_SNACK -> 74
            }
        }
}
