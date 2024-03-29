// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.vesilahti.evaka

import org.springframework.boot.context.properties.ConfigurationProperties
import trevaka.ipaas.IpaasProperties

@ConfigurationProperties(prefix = "vesilahti")
data class VesilahtiProperties(
    val ipaas: IpaasProperties,
    val bucket: BucketProperties,
)

data class BucketProperties(
    val export: String,
) {
    fun allBuckets() = listOf(export)
}
