// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.bi

import fi.espoo.evaka.espoo.bi.CSV_CHARSET
import fi.espoo.evaka.espoo.bi.EspooBiJob
import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.domain.EvakaClock
import fi.tampere.trevaka.TampereAsyncJob
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.time.Duration

class BiExportJob(private val client: BiExportClient) {
    fun sendBiTable(db: Database.Connection, clock: EvakaClock, msg: TampereAsyncJob.SendBiTable) =
        sendBiTable(db, clock, msg.table.fileName, msg.table.query)

    fun sendBiTable(
        db: Database.Connection,
        clock: EvakaClock,
        tableName: String,
        query: BiQueries.CsvQuery
    ) {
        val date = clock.now().toLocalDate()
        val fileName = "${tableName}_$date.csv"
        db.read { tx ->
            tx.setStatementTimeout(Duration.ofMinutes(10))
            query(tx) { records ->
                val stream = EspooBiJob.CsvInputStream(CSV_CHARSET, records)
                client.sendBiCsvFile(fileName, stream)
            }
        }
    }
}
