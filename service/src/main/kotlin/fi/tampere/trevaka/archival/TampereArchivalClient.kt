// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.archival

import com.profium.reception._2022._03.Collections
import fi.espoo.evaka.caseprocess.CaseProcess
import fi.espoo.evaka.caseprocess.DocumentMetadata
import fi.espoo.evaka.document.archival.ArchivalIntegrationClient
import fi.espoo.evaka.document.childdocument.ChildDocumentDetails
import fi.espoo.evaka.pis.service.PersonDTO
import fi.espoo.evaka.s3.Document
import fi.espoo.evaka.shared.ChildDocumentId
import fi.espoo.evaka.user.EvakaUser
import fi.tampere.trevaka.ArchivalProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.withLoggingContext
import jakarta.xml.bind.JAXBContext
import jakarta.xml.bind.JAXBException
import jakarta.xml.bind.Marshaller
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.StringReader
import java.io.StringWriter
import java.util.UUID

private val logger = KotlinLogging.logger {}

class TampereArchivalClient(private val client: OkHttpClient, private val properties: ArchivalProperties) : ArchivalIntegrationClient {

    private val context = JAXBContext.newInstance(
        Collections::class.java,
        Success::class.java,
        Error::class.java,
    )

    override fun uploadChildDocumentToArchive(
        documentId: ChildDocumentId,
        caseProcess: CaseProcess?,
        childInfo: PersonDTO,
        childDocumentDetails: ChildDocumentDetails,
        documentMetadata: DocumentMetadata,
        documentContent: Document,
        evakaUser: EvakaUser,
    ): String? {
        val transactionId = UUID.randomUUID().toString()

        val data = transform(evakaUser).apply {
            collection.add(transform(childDocumentDetails, documentContent))
        }
        val xml = marshal(data)

        val metadata = xml.toRequestBody("application/xml".toMediaType())
        val file = documentContent.bytes.toRequestBody(documentContent.contentType.toMediaType())
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("xml", null, metadata)
            .addFormDataPart("content", childDocumentDetails.originalId(), file)
            .build()
        return postRecord(transactionId, body)?.records?.record?.archiveId
    }

    private fun transform(evakaUser: EvakaUser) = Collections().apply {
        initiator = Collections.Initiator().apply {
            sourceSystem = "eVaka"
            initiatorId = evakaUser.id.toString()
            initiatorName = evakaUser.name
        }
    }

    private fun postRecord(transactionId: String, body: MultipartBody): Success? {
        val request = Request.Builder()
            .url("${properties.baseUrl}/records/add")
            .header("Accept", "*/*")
            .header("X-API-key", properties.apiKey)
            .header("X-API-version", "1.0")
            .header("X-API-transactionid", transactionId)
            .post(body)
            .build()

        return withLoggingContext("transactionId" to transactionId) {
            client.newCall(request).execute().use { response ->
                val xml = response.body.string()
                val data = unmarshal(xml)
                if (response.isSuccessful) {
                    when (data) {
                        is Success -> {
                            logger.info { "Successfully post record, response body: $data" }
                            data
                        }
                        else -> {
                            logger.error { "Successfully post record, but response body was unexpected: $xml" }
                            null
                        }
                    }
                } else {
                    val message = when (data) {
                        is Error -> "Unsuccessfully post record, response body: $data"
                        else -> "Unsuccessfully post record and response body was unexpected: $xml"
                    }
                    error(message)
                }
            }
        }
    }

    private fun marshal(data: Any): String {
        logger.trace { "Marshal $data" }
        val marshaller = context.createMarshaller()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
        return StringWriter().use { writer ->
            marshaller.marshal(data, writer)
            writer.toString()
        }
    }

    private fun unmarshal(xml: String): Any? {
        try {
            logger.trace { "Unmarshal $xml" }
            val unmarshaller = context.createUnmarshaller()
            return unmarshaller.unmarshal(StringReader(xml))
        } catch (_: JAXBException) {
            logger.warn { "Unable to unmarshal $xml" }
            return null
        }
    }
}
