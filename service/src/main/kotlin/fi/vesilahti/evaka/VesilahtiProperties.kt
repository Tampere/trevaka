// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.vesilahti.evaka

import org.springframework.boot.context.properties.ConfigurationProperties
import trevaka.ipaas.IpaasProperties

@ConfigurationProperties(prefix = "vesilahti", ignoreUnknownFields = false)
data class VesilahtiProperties(
    val ipaas: IpaasProperties,
    val intimeInvoices: SftpProperties,
    val intimePayments: SftpProperties,
)

data class SftpProperties(
    val address: String,
    val path: String,
    val username: String,
    val password: String,
)
