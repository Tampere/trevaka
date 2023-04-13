// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Month

/**
 * All trevaka-specific configuration properties.
 */
@ConfigurationProperties(prefix = "trevaka", ignoreUnknownFields = false)
data class TrevakaProperties(
    val ipaas: IpaasProperties,
    val invoice: InvoiceProperties,
    val summertimeAbsenceProperties: SummertimeAbsenceProperties = SummertimeAbsenceProperties(),
)

data class IpaasProperties(
    val username: String,
    val password: String,
)

data class InvoiceProperties(
    val url: String,
    val paymentTerm: String = "V000",
    val salesOrganisation: String = "1312",
    val distributionChannel: String = "00",
    val division: String = "00",
    val salesOrderType: String = "ZPH",
    val interfaceID: String = "352",
    val plant: String = "1310",
)

data class SummertimeAbsenceProperties(
    val freeMonth: Month = Month.JUNE,
)
