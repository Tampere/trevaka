// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.nokiankaupunki.evaka

import fi.espoo.evaka.Sensitive
import fi.espoo.evaka.SftpEnv
import org.springframework.boot.context.properties.ConfigurationProperties
import trevaka.ipaas.IpaasProperties

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
)

data class BucketProperties(
    val export: String,
) {
    fun allBuckets() = listOf(export)
}

data class SftpArchivalProperties(
    val sftp: SftpProperties,
)

data class SftpProperties(
    val host: String,
    val port: Int,
    val hostKeys: List<String>,
    val username: String,
    val password: String,
    val prefix: String,
) {
    companion object
    fun toSftpEnv(): SftpEnv = SftpEnv(
        host = host,
        port = port,
        username = username,
        password = Sensitive(password),
        hostKeys = hostKeys,
    )
}
