// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.nokiankaupunki.evaka.mealintegration

import fi.espoo.evaka.mealintegration.MealType
import fi.espoo.evaka.mealintegration.MealTypeMapper

object NokiaMealTypeMapper : MealTypeMapper {
    override fun toMealId(mealType: MealType, specialDiet: Boolean): Int =
        if (specialDiet) {
            when (mealType) {
                MealType.BREAKFAST -> 13
                MealType.LUNCH -> 18
                MealType.LUNCH_PRESCHOOL -> 18
                MealType.SNACK -> 6
                MealType.SUPPER -> 23
                MealType.EVENING_SNACK -> 9
            }
        } else {
            when (mealType) {
                MealType.BREAKFAST -> 2
                MealType.LUNCH -> 4
                MealType.LUNCH_PRESCHOOL -> 4
                MealType.SNACK -> 5
                MealType.SUPPER -> 7
                MealType.EVENING_SNACK -> 8
            }
        }
}
