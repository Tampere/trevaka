// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.nokiankaupunki.evaka

import org.springframework.boot.context.properties.ConfigurationProperties
import trevaka.ipaas.IpaasProperties
import trevaka.sftp.SftpProperties

@ConfigurationProperties(prefix = "nokia")
data class NokiaProperties(
    val ipaas: IpaasProperties,
    val invoice: InvoiceProperties,
    val bucket: BucketProperties,
    val archival: SftpArchivalProperties?,
)

data class InvoiceProperties(
    val municipalityCode: String,
    val invoiceType: String,
    val version: NokiaInvoiceVersion = NokiaInvoiceVersion.V2024,
    val sftp: SftpProperties? = null,
)

enum class NokiaInvoiceVersion {
    V2024,
    V2026,
}

data class BucketProperties(
    val export: String,
) {
    fun allBuckets() = listOf(export)
}

data class SftpArchivalProperties(
    val sftp: SftpProperties,
)
