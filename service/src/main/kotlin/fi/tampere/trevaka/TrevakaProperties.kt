// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * All trevaka-specific configuration properties.
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "fi.tampere.trevaka")
data class TrevakaProperties(
    val ipaas: IpaasProperties,
    val invoice: InvoiceProperties = InvoiceProperties(),
)

data class IpaasProperties(
    val username: String,
    val password: String,
)

data class InvoiceProperties(
    val paymentTerm: String = "V000",
    val salesOrganisation: String = "1312",
    val distributionChannel: String = "00",
    val division: String = "00",
    val salesOrderType: String = "ZPH",
    val interfaceID: String = "352",
    val plant: String = "1310"
)
