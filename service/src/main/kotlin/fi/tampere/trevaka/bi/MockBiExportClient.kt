// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.bi

import com.google.common.io.ByteStreams
import fi.espoo.evaka.application.utils.exhaust
import fi.espoo.evaka.espoo.bi.EspooBiJob
import mu.KotlinLogging

class MockBiExportClient : BiExportClient {
    private val logger = KotlinLogging.logger {}

    override fun sendBiCsvFile(
        fileName: String,
        stream: EspooBiJob.CsvInputStream,
    ): Pair<String, String> {
        logger.info { "Mock BI client ignored export request for $fileName" }
        ByteStreams.exhaust(stream)
        return "" to ""
    }
}
