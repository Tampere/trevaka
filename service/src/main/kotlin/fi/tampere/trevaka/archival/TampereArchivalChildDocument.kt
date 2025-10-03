// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.archival

import com.profium.reception._2022._03.Collections
import com.profium.sahke2.Agent
import fi.espoo.evaka.document.childdocument.ChildDocumentDetails
import fi.espoo.evaka.s3.Document
import org.apache.tika.mime.MimeTypes
import trevaka.jaxb.localDateToXMLGregorianCalendar
import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal fun transform(childDocumentDetails: ChildDocumentDetails, document: Document, ownerDetails: OwnerDetails, authorDetails: List<AuthorDetails>): Pair<Collections.Collection, Map<String, Document>> {
    val originalId = childDocumentDetails.id.toString()
    return Collections.Collection().apply {
        type = "record"
        folder = childDocumentDetails.template.processDefinitionNumber
        metadata = Collections.Collection.Metadata().apply {
            title = "${childDocumentDetails.template.name}, ${ownerDetails.name}, ${ownerDetails.dateOfBirth.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}"
            calculationBaseDate = localDateToXMLGregorianCalendar(childDocumentDetails.template.validity.start)
            created = childDocumentDetails.publishedAt?.toLocalDate()?.let { localDateToXMLGregorianCalendar(it) }
            authorDetails.forEach {
                agent.add(
                    Agent().apply {
                        agentRole = it.role
                        agentName = it.name
                        // agent corporateName left empty
                    },
                )
            }
        }
        content = Collections.Collection.Content().apply {
            file.add(
                Collections.Collection.Content.File().apply {
                    val mimeTypes = MimeTypes.getDefaultMimeTypes()
                    val mimeType = mimeTypes.forName(document.contentType)
                    name = "${childDocumentDetails.template.name}${mimeType.extension}"
                    this.originalId = originalId
                },
            )
        }
    } to mapOf(originalId to document)
}

data class AuthorDetails(val name: String, val role: String)
data class OwnerDetails(val name: String, val dateOfBirth: LocalDate)
