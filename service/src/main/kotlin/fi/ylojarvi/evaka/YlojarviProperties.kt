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
    val sftp: SftpProperties,
)

data class BucketProperties(
    val export: String,
) {
    fun allBuckets() = listOf(export)
}
