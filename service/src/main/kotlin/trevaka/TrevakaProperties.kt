// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "trevaka")
data class TrevakaProperties(
    val enabledFeatures: TrevakaEnabledFeatures = TrevakaEnabledFeatures(),
    val vtjKyselyApiKey: String? = null,
    val vtjMutpaApiKey: String? = null,
)

data class TrevakaEnabledFeatures(
    val frendsVtjKysely: Boolean = false,
    val frendsVtjMutpa: Boolean = false,
)
