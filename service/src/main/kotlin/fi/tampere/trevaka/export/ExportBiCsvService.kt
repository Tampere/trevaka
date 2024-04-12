// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.export

import fi.espoo.evaka.espoo.bi.CSV_CHARSET
import fi.espoo.evaka.espoo.bi.EspooBiJob
import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.tampere.trevaka.bi.BiExportClient
import fi.tampere.trevaka.bi.BiExportJob
import fi.tampere.trevaka.bi.BiTable
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class ExportBiCsvService(private val biExportClient: BiExportClient) {

    fun exportBiCsv(
        db: Database.Connection,
        timestamp: HelsinkiDateTime,
        biTable: BiTable
    ): Pair<String, String> {
        val tableName = biTable.fileName
        val query = biTable.query
        val fileName = "${tableName}_${timestamp.toLocalDate()}.csv"
        return db.read { tx ->
            tx.setStatementTimeout(Duration.ofMinutes(10))
            val result =
                query(tx) { records ->
                    val stream = EspooBiJob.CsvInputStream(CSV_CHARSET, records)
                    biExportClient.sendBiCsvFile(fileName, stream)
                }
            result
        }
    }
}
