// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.vesilahti.evaka.invoice.service

import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.vesilahti.evaka.VesilahtiProperties
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.nio.charset.StandardCharsets
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class S3Sender(private val s3Client: S3Client, private val properties: VesilahtiProperties) {
    fun send(content: String, now: HelsinkiDateTime) {
        val bucket = properties.bucket.export
        val fileName = now.toLocalDateTime().format(DateTimeFormatter.ofPattern("'invoices/Vesilahti_eVaka_'ddMMyyyy_HHmmss'.dat'"))

        val request = PutObjectRequest.builder()
            .bucket(bucket)
            .key(fileName)
            .contentType("text/plain")
            .build()
        val body = RequestBody.fromString(content, StandardCharsets.ISO_8859_1)
        s3Client.putObject(request, body)
    }
}
