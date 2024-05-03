// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.ylojarvi.evaka.mealintegration

import fi.espoo.evaka.mealintegration.MealType
import fi.espoo.evaka.mealintegration.MealTypeMapper

object YlojarviMealTypeMapper : MealTypeMapper {
    override fun toMealId(mealType: MealType, specialDiet: Boolean): Int =
        if (specialDiet) {
            when (mealType) {
                MealType.BREAKFAST -> 2
                MealType.LUNCH -> 4
                MealType.LUNCH_PRESCHOOL -> 6
                MealType.SNACK -> 8
                MealType.SUPPER -> 10
                MealType.EVENING_SNACK -> 12
            }
        } else {
            when (mealType) {
                MealType.BREAKFAST -> 1
                MealType.LUNCH -> 3
                MealType.LUNCH_PRESCHOOL -> 5
                MealType.SNACK -> 7
                MealType.SUPPER -> 9
                MealType.EVENING_SNACK -> 11
            }
        }
}
