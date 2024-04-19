// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.bi

import fi.espoo.evaka.espoo.bi.EspooBiJob
import fi.espoo.evaka.shared.domain.EvakaClock

interface BiExportClient {
    fun sendBiCsvFile(tableName: String, clock: EvakaClock, stream: EspooBiJob.CsvInputStream): Pair<String, String>
}
