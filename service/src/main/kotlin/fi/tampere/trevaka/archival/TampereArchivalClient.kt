// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.archival

import com.profium.reception._2022._03.Collections
import com.profium.sahke2.Agent
import fi.espoo.evaka.caseprocess.CaseProcess
import fi.espoo.evaka.caseprocess.CaseProcessHistoryRow
import fi.espoo.evaka.caseprocess.CaseProcessState
import fi.espoo.evaka.caseprocess.DocumentMetadata
import fi.espoo.evaka.decision.Decision
import fi.espoo.evaka.document.archival.ArchivalIntegrationClient
import fi.espoo.evaka.document.childdocument.ChildDocumentDetails
import fi.espoo.evaka.invoicing.domain.FeeDecisionDetailed
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionDetailed
import fi.espoo.evaka.pis.service.PersonDTO
import fi.espoo.evaka.s3.Document
import fi.espoo.evaka.shared.ChildDocumentId
import fi.espoo.evaka.user.EvakaUser
import fi.espoo.evaka.user.EvakaUserType
import fi.tampere.trevaka.ArchivalProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.xml.bind.JAXBContext
import jakarta.xml.bind.JAXBException
import jakarta.xml.bind.Marshaller
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.MDC
import java.io.StringReader
import java.io.StringWriter
import java.lang.IllegalStateException
import java.time.format.DateTimeFormatter

private val logger = KotlinLogging.logger {}

internal val ARCHIVAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy")

class TampereArchivalClient(private val client: OkHttpClient, private val properties: ArchivalProperties) : ArchivalIntegrationClient {

    private val context = JAXBContext.newInstance(
        Collections::class.java,
        Success::class.java,
        Errors::class.java,
    )

    override fun uploadDecisionToArchive(
        caseProcess: CaseProcess,
        child: PersonDTO,
        decision: Decision,
        document: Document,
        user: EvakaUser,
    ): String? {
        val (collection, content) = transform(caseProcess, decision, document, child)
        val collections = transform(user).apply {
            this.collection.add(collection)
        }
        return postRecord(collections, content)?.records?.record?.archiveId
    }

    override fun uploadFeeDecisionToArchive(
        caseProcess: CaseProcess,
        decision: FeeDecisionDetailed,
        document: Document,
        user: EvakaUser,
    ): String? {
        val (collection, content) = transform(caseProcess, decision, document)
        val collections = transform(user).apply {
            this.collection.add(collection)
        }
        return postRecord(collections, content)?.records?.record?.archiveId
    }

    override fun uploadVoucherValueDecisionToArchive(
        caseProcess: CaseProcess,
        decision: VoucherValueDecisionDetailed,
        document: Document,
        user: EvakaUser,
    ): String? {
        val (collection, content) = transform(caseProcess, decision, document)
        val collections = transform(user).apply {
            this.collection.add(collection)
        }
        return postRecord(collections, content)?.records?.record?.archiveId
    }

    override fun uploadChildDocumentToArchive(
        documentId: ChildDocumentId,
        caseProcess: CaseProcess?,
        childInfo: PersonDTO,
        childDocumentDetails: ChildDocumentDetails,
        documentMetadata: DocumentMetadata,
        documentContent: Document,
        evakaUser: EvakaUser,
    ): String? {
        val ownerDetails = OwnerDetails("${childInfo.firstName} ${childInfo.lastName}", childInfo.dateOfBirth)
        val (collection, content) = transform(childDocumentDetails, documentContent, ownerDetails, extractAgents(caseProcess))
        val collections = transform(evakaUser).apply {
            this.collection.add(collection)
        }
        return postRecord(collections, content)?.records?.record?.archiveId
    }

    private fun transform(evakaUser: EvakaUser) = Collections().apply {
        initiator = Collections.Initiator().apply {
            sourceSystem = "eVaka"
            initiatorId = evakaUser.id.toString()
            initiatorName = evakaUser.name
        }
    }

    private fun postRecord(collections: Collections, content: Map<String, Document>): Success? {
        val xml = marshal(collections)
        val metadata = xml.toRequestBody("application/xml".toMediaType())
        val files = content.mapValues { (_, document) -> document.bytes.toRequestBody(document.contentType.toMediaType()) }
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("xml", null, metadata)
            .apply { files.forEach { (originalId, file) -> addFormDataPart("content", originalId, file) } }
            .build()
        val request = Request.Builder()
            .url("${properties.baseUrl}/records/add")
            .header("Accept", "*/*")
            // .header("X-API-key", properties.apiKey) // Integration platform adds this header
            .header("X-API-version", "1.0")
            .apply { transactionId()?.let { header("X-API-transactionid", it) } }
            .post(body)
            .build()

        return client.newCall(request).execute().use { response ->
            val code = response.code
            val xml = response.body.string()
            val data = unmarshal(xml)
            if (response.isSuccessful) {
                when (data) {
                    is Success -> {
                        logger.info { "Successfully post record (status=$code), response body: $data" }
                        data
                    }
                    else -> {
                        logger.error { "Successfully post record (status=$code), but response body was unexpected: $xml" }
                        null
                    }
                }
            } else {
                val message = when (data) {
                    is Errors -> "Unsuccessfully post record (status=$code), response body: $data"
                    else -> "Unsuccessfully post record (status=$code) and response body was unexpected: $xml"
                }
                error(message)
            }
        }
    }

    private fun marshal(data: Any): String {
        val marshaller = context.createMarshaller()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
        return StringWriter().use { writer ->
            marshaller.marshal(data, writer)
            writer.toString()
        }.also { xml -> logger.trace { "Marshalled $xml" } }
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

    private fun transactionId(): String? = MDC.get("traceId")
}

private fun extractAgentRole(historyRows: List<CaseProcessHistoryRow>) = if (historyRows.any { it.state == CaseProcessState.DECIDING }) "Päättäjä" else "Laatija"

private fun extractAgents(caseProcess: CaseProcess?): List<AuthorDetails> = caseProcess?.history
    ?.filter { it.enteredBy.type == EvakaUserType.EMPLOYEE }
    ?.groupBy { it.enteredBy.id }
    ?.map { AuthorDetails(it.value[0].enteredBy.name, extractAgentRole(it.value)) }
    ?: throw IllegalStateException("No employee agents found for case process ${caseProcess?.id}")

internal fun transformToAgents(caseProcess: CaseProcess): List<Agent> = extractAgents(caseProcess).map {
    Agent().apply {
        agentRole = it.role
        agentName = it.name
        // agent corporateName left empty
    }
}
