// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.vesilahti.evaka

import fi.espoo.evaka.invoicing.domain.FeeAlterationType
import fi.espoo.evaka.invoicing.service.InvoiceProductProvider
import fi.espoo.evaka.invoicing.service.ProductKey
import fi.espoo.evaka.invoicing.service.ProductWithName
import fi.espoo.evaka.placement.PlacementType

class VesilahtiInvoiceProductProvider : InvoiceProductProvider {
    override val products = Product.entries.map { ProductWithName(it.key, it.nameFi) }
    override val dailyRefund = Product.FREE_OF_CHARGE.key
    override val partMonthSickLeave = Product.SICK_LEAVE_50.key
    override val fullMonthSickLeave = Product.SICK_LEAVE_100.key
    override val fullMonthAbsence = Product.ABSENCE.key
    override val contractSurplusDay = Product.OVER_CONTRACT.key

    override fun mapToProduct(placementType: PlacementType): ProductKey {
        val product = when (placementType) {
            PlacementType.DAYCARE, PlacementType.DAYCARE_PART_TIME, PlacementType.DAYCARE_FIVE_YEAR_OLDS, PlacementType.DAYCARE_PART_TIME_FIVE_YEAR_OLDS -> Product.DAYCARE
            PlacementType.PRESCHOOL_DAYCARE -> Product.PRESCHOOL_WITH_DAYCARE
            PlacementType.PREPARATORY_DAYCARE -> Product.PRESCHOOL_WITH_DAYCARE
            PlacementType.TEMPORARY_DAYCARE, PlacementType.TEMPORARY_DAYCARE_PART_DAY -> Product.TEMPORARY_CARE
            PlacementType.PRESCHOOL, PlacementType.PREPARATORY, PlacementType.SCHOOL_SHIFT_CARE, PlacementType.CLUB, PlacementType.PRESCHOOL_CLUB -> error(
                "No product mapping found for placement type $placementType",
            )
        }
        return product.key
    }

    override fun mapToFeeAlterationProduct(productKey: ProductKey, feeAlterationType: FeeAlterationType): ProductKey {
        val product = when (findProduct(productKey) to feeAlterationType) {
            Product.DAYCARE to FeeAlterationType.DISCOUNT, Product.DAYCARE to FeeAlterationType.RELIEF, Product.PRESCHOOL_WITH_DAYCARE to FeeAlterationType.DISCOUNT, Product.PRESCHOOL_WITH_DAYCARE to FeeAlterationType.RELIEF -> Product.DAYCARE_DISCOUNT
            Product.DAYCARE to FeeAlterationType.INCREASE -> Product.CORRECTION
            Product.PRESCHOOL_WITH_DAYCARE to FeeAlterationType.INCREASE -> Product.PRESCHOOL_DAYCARE_CORRECTION
            else -> error("No product mapping found for product + fee alteration type combo ($productKey + $feeAlterationType)")
        }
        return product.key
    }
}

fun findProduct(key: ProductKey) = Product.entries.find { it.key == key } ?: error("Product with key $key not found")

enum class Product(val nameFi: String, val code: String) {
    DAYCARE("Varhaiskasvatus", ""),
    DAYCARE_DISCOUNT("Alennus", ""),
    PRESCHOOL_WITH_DAYCARE("Esiopetusta täydentävä varhaiskasvatus", ""),
    TEMPORARY_CARE("Tilapäinen varhaiskasvatus", ""),
    SICK_LEAVE_50("Sairaspoissaolovähennys 50 %", ""),
    SICK_LEAVE_100("Sairaspoissaolovähennys 100 %", ""),
    ABSENCE("Poissaolovähennys 50%", ""),
    FREE_OF_CHARGE("Maksuton päivä", ""),
    CORRECTION("Oikaisu", ""),
    FREE_MONTH("Poissaolovähennys 100 %", ""),
    OVER_CONTRACT("Sovittujen päivien ylitys", ""),
    PRESCHOOL_DAYCARE_CORRECTION("Kokoaikainen varhaiskasvatus", ""),
    ;

    val key = ProductKey(this.name)
}
