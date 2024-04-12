// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.bi

import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.espoo.evaka.shared.domain.MockEvakaClock
import fi.tampere.trevaka.AbstractTampereIntegrationTest
import fi.tampere.trevaka.TampereProperties
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.beans.factory.annotation.Autowired
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.Delete
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.ListObjectsRequest
import software.amazon.awssdk.services.s3.model.ObjectIdentifier
import java.time.LocalDate
import java.time.LocalTime

class ExportBiCsvJobTest : AbstractTampereIntegrationTest() {
    private val clock =
        MockEvakaClock(HelsinkiDateTime.of(LocalDate.of(2022, 10, 23), LocalTime.of(21, 0)))
    @Autowired private lateinit var properties: TampereProperties
    @Autowired private lateinit var s3Client: S3Client
    @Autowired private lateinit var s3AsyncClient: S3AsyncClient

    private lateinit var exportJob: BiExportJob

    @BeforeAll
    fun beforeAll() {
        val exportClient = S3MockBiExportS3Client(s3AsyncClient, properties)
        exportJob = BiExportJob(exportClient)

        clearBucket(properties.bucket.export)
    }

    @TestFactory
    fun testBiTableExports() =
        BiTable.entries.map {
            DynamicTest.dynamicTest("Test ${it.fileName} export") { sendAndAssertBiTableCsv(it) }
        }

    private fun sendAndAssertBiTableCsv(table: BiTable) {
        val bucket = properties.bucket.export
        val key = "bi/${table.fileName}_${clock.now().toLocalDate()}.csv"

        exportJob.sendBiTable(db, clock, table.fileName, table.query)

        val (data, contentType) = getCsv(bucket, key)
        assertEquals("text/csv", contentType)
        assertTrue(data.isNotEmpty())
    }

    private fun getCsv(bucket: String, key: String): Pair<String, String> {
        val request = GetObjectRequest.builder().bucket(bucket).key(key).build()
        return s3Client.getObject(request).use {
            it.readAllBytes().toString(Charsets.UTF_8) to it.response().contentType()
        }
    }

    private fun clearBucket(bucket: String) {
        val objectList =
            s3Client.listObjects(ListObjectsRequest.builder().bucket(bucket).build()).contents()

        if (objectList.size > 0) {
            val objectIdList = objectList.map { ObjectIdentifier.builder().key(it.key()).build() }

            s3Client.deleteObjects(
                DeleteObjectsRequest.builder()
                    .bucket(bucket)
                    .delete(Delete.builder().objects(objectIdList).build())
                    .build()
            )
        }
    }
}
