// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.hameenkyro.evaka

import org.springframework.boot.context.properties.ConfigurationProperties
import trevaka.primus.PrimusProperties

@ConfigurationProperties(prefix = "hameenkyro")
data class HameenkyroProperties(
    val bucket: BucketProperties,
    val primus: PrimusProperties? = null,
)

data class BucketProperties(
    val export: String,
) {
    fun allBuckets() = listOf(export)
}
