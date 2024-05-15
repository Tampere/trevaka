// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.kangasala.evaka.mealintegration

import fi.espoo.evaka.mealintegration.MealType
import fi.espoo.evaka.mealintegration.MealTypeMapper

object KangasalaMealTypeMapper : MealTypeMapper {
    override fun toMealId(mealType: MealType, specialDiet: Boolean): Int =
        if (specialDiet) {
            when (mealType) {
                MealType.BREAKFAST -> 143
                MealType.LUNCH -> 145
                MealType.LUNCH_PRESCHOOL -> 277
                MealType.SNACK -> 160
                MealType.SUPPER -> 173
                MealType.EVENING_SNACK -> 187
            }
        } else {
            when (mealType) {
                MealType.BREAKFAST -> 162
                MealType.LUNCH -> 175
                MealType.LUNCH_PRESCHOOL -> 276
                MealType.SNACK -> 152
                MealType.SUPPER -> 354
                MealType.EVENING_SNACK -> 188
            }
        }
}
