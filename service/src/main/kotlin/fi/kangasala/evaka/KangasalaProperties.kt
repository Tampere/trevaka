// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.kangasala.evaka

import fi.nokiankaupunki.evaka.SftpArchivalProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import trevaka.ipaas.IpaasProperties

@ConfigurationProperties(prefix = "kangasala")
data class KangasalaProperties(
    val ipaas: IpaasProperties,
    val bucket: BucketProperties,
    val archival: SftpArchivalProperties? = null,
)

data class BucketProperties(
    val export: String,
) {
    fun allBuckets() = listOf(export)
}
