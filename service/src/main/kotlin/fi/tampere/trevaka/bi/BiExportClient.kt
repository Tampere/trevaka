// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.bi

import fi.espoo.evaka.espoo.bi.EspooBiJob

interface BiExportClient {
    fun sendBiCsvFile(fileName: String, stream: EspooBiJob.CsvInputStream): Pair<String, String>
}
