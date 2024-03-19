// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.lempaala.evaka
import fi.espoo.evaka.invoicing.domain.FeeAlterationType
import fi.espoo.evaka.invoicing.service.InvoiceProductProvider
import fi.espoo.evaka.invoicing.service.ProductKey
import fi.espoo.evaka.invoicing.service.ProductWithName
import fi.espoo.evaka.placement.PlacementType

class LempaalaInvoiceProductProvider : InvoiceProductProvider {
    override val products: List<ProductWithName>
        get() = TODO("Not yet implemented")
    override val dailyRefund: ProductKey
        get() = TODO("Not yet implemented")
    override val partMonthSickLeave: ProductKey
        get() = TODO("Not yet implemented")
    override val fullMonthSickLeave: ProductKey
        get() = TODO("Not yet implemented")
    override val fullMonthAbsence: ProductKey
        get() = TODO("Not yet implemented")
    override val contractSurplusDay: ProductKey
        get() = TODO("Not yet implemented")

    override fun mapToProduct(placementType: PlacementType): ProductKey {
        TODO("Not yet implemented")
    }

    override fun mapToFeeAlterationProduct(productKey: ProductKey, feeAlterationType: FeeAlterationType): ProductKey {
        TODO("Not yet implemented")
    }
}
