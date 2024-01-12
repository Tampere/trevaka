// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.hameenkyro.evaka

import org.springframework.boot.context.properties.ConfigurationProperties
import trevaka.ipaas.IpaasProperties

@ConfigurationProperties(prefix = "hameenkyro", ignoreUnknownFields = false)
data class HameenkyroProperties(
    val ipaas: IpaasProperties,
)
