// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.bi

import fi.espoo.evaka.espoo.bi.EspooBiJob
import fi.tampere.trevaka.TampereProperties
import mu.KotlinLogging
import software.amazon.awssdk.core.async.AsyncRequestBody
import software.amazon.awssdk.services.s3.S3AsyncClient
import java.io.ByteArrayInputStream

class StreamingBiExportS3Client(
    private val asyncClient: S3AsyncClient,
    private val properties: TampereProperties
) : BiExportClient {
    private val logger = KotlinLogging.logger {}

    override fun sendBiCsvFile(
        fileName: String,
        stream: EspooBiJob.CsvInputStream
    ): Pair<String, String> {
        logger.info("Sending BI CSV file $fileName")
        val bucket = properties.bucket.export
        val key = "bi/${fileName}"

        val body = AsyncRequestBody.forBlockingInputStream(null)

        val futureResponse =
            asyncClient.putObject({ r -> r.bucket(bucket).key(key).contentType("text/csv") }, body)

        val contentLength = body.writeInputStream(stream)
        val response = futureResponse.join().sdkHttpResponse()
        if (response.isSuccessful) {
            logger.info("BI CSV file $fileName successfully sent ($contentLength bytes)")
        } else {
            logger.warn {
                "BI CSV file $fileName sending failed ($contentLength bytes): ${response.statusCode()} ${response.statusText()}"
            }
        }
        return bucket to key
    }
}

class S3MockBiExportS3Client(
    private val asyncClient: S3AsyncClient,
    private val properties: TampereProperties
) : BiExportClient {
    private val logger = KotlinLogging.logger {}

    override fun sendBiCsvFile(
        fileName: String,
        stream: EspooBiJob.CsvInputStream
    ): Pair<String, String> {
        logger.info("Sending BI CSV file $fileName")
        val bucket = properties.bucket.export
        val key = "bi/${fileName}"

        // S3 Mock does not seem to support streaming with CRT-client multiparts ->
        // test client needs to avoid sending multipart messages
        val content = stream.readAllBytes()
        val body = AsyncRequestBody.forBlockingInputStream(content.size.toLong())

        val futureResponse =
            asyncClient.putObject({ r -> r.bucket(bucket).key(key).contentType("text/csv") }, body)

        val contentLength = body.writeInputStream(ByteArrayInputStream(content))

        val response = futureResponse.join().sdkHttpResponse()
        if (response.isSuccessful) {
            logger.info("BI CSV file $fileName successfully sent ($contentLength bytes)")
        } else {
            logger.warn {
                "BI CSV file $fileName sending failed ($contentLength bytes): ${response.statusCode()} ${response.statusText()}"
            }
        }
        return bucket to key
    }
}
