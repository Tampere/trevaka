// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.ylojarvi.evaka

import org.springframework.boot.context.properties.ConfigurationProperties
import trevaka.ipaas.IpaasProperties

@ConfigurationProperties(prefix = "ylojarvi")
data class YlojarviProperties(
    val ipaas: IpaasProperties,
    val invoice: InvoiceProperties,
    val bucket: BucketProperties,
)

data class InvoiceProperties(
    val municipalityCode: String,
    val invoiceType: String,
)

data class BucketProperties(
    val export: String,
) {
    fun allBuckets() = listOf(export)
}
