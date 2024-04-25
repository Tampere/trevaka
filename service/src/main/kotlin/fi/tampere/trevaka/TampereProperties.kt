// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka

import org.springframework.boot.context.properties.ConfigurationProperties
import trevaka.ipaas.IpaasProperties
import java.time.Month

/**
 * All Tampere-specific configuration properties.
 */
@ConfigurationProperties(prefix = "tampere")
data class TampereProperties(
    val ipaas: IpaasProperties,
    val invoice: InvoiceProperties,
    val summertimeAbsence: SummertimeAbsenceProperties = SummertimeAbsenceProperties(),
    val bucket: BucketProperties,
    val biExport: BiExportProperties,
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

data class BucketProperties(
    val export: String,
) {
    fun allBuckets() = listOf(export)
}

data class BiExportProperties(
    val prefix: String,
)
