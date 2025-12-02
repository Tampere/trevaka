// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.ylojarvi.evaka

import org.springframework.boot.context.properties.ConfigurationProperties
import trevaka.sftp.SftpProperties

@ConfigurationProperties(prefix = "ylojarvi")
data class YlojarviProperties(
    val invoice: InvoiceProperties,
    val bucket: BucketProperties,
)

data class InvoiceProperties(
    val municipalityCode: String,
    val invoiceType: String,
    val version: YlojarviInvoiceVersion = YlojarviInvoiceVersion.V2024,
    val sftp: SftpProperties? = null,
)

enum class YlojarviInvoiceVersion {
    V2024,
    V2026,
}

data class BucketProperties(
    val export: String,
) {
    fun allBuckets() = listOf(export)
}
