// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka.archival.tweb

import com.profium.reception._2022._03.Collections
import com.profium.sahke2.Agent
import fi.espoo.evaka.caseprocess.CaseProcess
import fi.espoo.evaka.caseprocess.CaseProcessHistoryRow
import fi.espoo.evaka.caseprocess.CaseProcessState
import fi.espoo.evaka.caseprocess.DocumentMetadata
import fi.espoo.evaka.decision.Decision
import fi.espoo.evaka.document.archival.ArchivalIntegrationClient
import fi.espoo.evaka.document.childdocument.ChildDocumentDetails
import fi.espoo.evaka.identity.ExternalIdentifier
import fi.espoo.evaka.invoicing.domain.FeeDecisionDetailed
import fi.espoo.evaka.invoicing.domain.PersonDetailed
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionDetailed
import fi.espoo.evaka.pis.service.PersonDTO
import fi.espoo.evaka.s3.Document
import fi.espoo.evaka.shared.ChildDocumentId
import fi.espoo.evaka.shared.FeatureConfig
import fi.espoo.evaka.shared.sftp.SftpClient
import fi.espoo.evaka.user.EvakaUser
import fi.espoo.evaka.user.EvakaUserType
import fi.nokiankaupunki.evaka.SftpArchivalProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.xml.bind.JAXBContext
import jakarta.xml.bind.Marshaller
import java.io.StringWriter

private val logger = KotlinLogging.logger {}

internal const val SEPARATOR_CHARACTER = ";"

class RegionalTwebArchivalClient(
    private val client: SftpClient,
    private val properties: SftpArchivalProperties,
    private val featureConfig: FeatureConfig,
) : ArchivalIntegrationClient {

    private val context = JAXBContext.newInstance(
        Collections::class.java,
    )

    override fun uploadDecisionToArchive(
        caseProcess: CaseProcess,
        child: PersonDTO,
        decision: Decision,
        document: Document,
        user: EvakaUser,
    ): String {
        val (collection, content) = transformDecision(caseProcess, decision, document, child, featureConfig)
        val collections = transform(user).apply {
            this.collection.add(collection)
        }
        return postRecord(collections, content)
    }

    override fun uploadFeeDecisionToArchive(
        caseProcess: CaseProcess,
        decision: FeeDecisionDetailed,
        document: Document,
        user: EvakaUser,
    ): String {
        val (collection, content) = transformFeeDecision(caseProcess, decision, document)
        val collections = transform(user).apply {
            this.collection.add(collection)
        }
        return postRecord(collections, content)
    }

    override fun uploadVoucherValueDecisionToArchive(
        caseProcess: CaseProcess,
        decision: VoucherValueDecisionDetailed,
        document: Document,
        user: EvakaUser,
    ): String {
        val (collection, content) = transformVoucherDecision(caseProcess, decision, document)
        val collections = transform(user).apply {
            this.collection.add(collection)
        }
        return postRecord(collections, content)
    }

    override fun uploadChildDocumentToArchive(
        documentId: ChildDocumentId,
        caseProcess: CaseProcess?,
        childInfo: PersonDTO,
        childDocumentDetails: ChildDocumentDetails,
        documentMetadata: DocumentMetadata,
        documentContent: Document,
        evakaUser: EvakaUser,
    ): String {
        val (collection, content) = transformChildDocument(
            childDocumentDetails,
            documentContent,
            childInfo,
            caseProcess ?: error("No child document case process available"),
        )
        val collections = transform(evakaUser).apply {
            this.collection.add(collection)
        }
        return postRecord(collections, content)
    }

    private fun transform(evakaUser: EvakaUser) = Collections().apply {
        initiator = Collections.Initiator().apply {
            sourceSystem = "eVaka"
            initiatorId = evakaUser.id.toString()
            initiatorName = evakaUser.name
        }
    }

    private fun postRecord(collections: Collections, content: Map<String, Document>): String {
        val xml = marshal(collections)
        collections.collection.first().content.file
            .map { file ->
                Pair(
                    file,
                    content[file.originalId] ?: error("Metadata to content mismatch for ${file.originalId}"),
                )
            }
            .forEach { (meta, content) ->
                content.bytes.inputStream()
                    .use { client.put(it, "${properties.sftp.prefix}${meta.name}") }
                xml.byteInputStream().use { client.put(it, "${properties.sftp.prefix}${meta.originalId}.xml") }
            }

        return "static-sftp-response"
    }

    private fun marshal(data: Any): String {
        val marshaller = context.createMarshaller()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
        return StringWriter().use { writer ->
            marshaller.marshal(data, writer)
            writer.toString()
        }.also { xml -> logger.trace { "Marshalled $xml" } }
    }
}

private fun extractAgentRole(historyRows: List<CaseProcessHistoryRow>) = if (historyRows.any { it.state == CaseProcessState.DECIDING }) "Päättäjä" else "Laatija"

private fun extractAgents(caseProcess: CaseProcess): List<AuthorDetails> = caseProcess.history
    .filter { it.enteredBy.type == EvakaUserType.EMPLOYEE }
    .groupBy { it.enteredBy.id }
    .map { AuthorDetails(it.value[0].enteredBy.name, extractAgentRole(it.value)) }

internal fun transformToAgents(caseProcess: CaseProcess, isRequired: Boolean = true): List<Agent> = extractAgents(caseProcess)
    .also { if (it.isEmpty() && isRequired) error("No employee agents found for case process ${caseProcess.id}") }
    .map {
        Agent().apply {
            agentRole = it.role
            agentName = it.name
            // agent corporateName left empty
        }
    }

internal fun hofTitle(type: String, status: String, owner: PersonDetailed): String = listOf(type, status, "${owner.firstName} ${owner.lastName}", owner.ssn ?: error("No owner ssn available"))
    .joinToString(SEPARATOR_CHARACTER)

internal fun childTitle(type: String, status: String, owner: PersonDTO): String = listOf(
    type,
    status,
    "${owner.firstName} ${owner.lastName}",
    if (owner.identity == ExternalIdentifier.NoID) error("No owner ssn available") else owner.identity.toString(),
)
    .joinToString(SEPARATOR_CHARACTER)

data class AuthorDetails(val name: String, val role: String)
