// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka.archival.tweb

import com.profium.reception._2022._03.Collections
import fi.espoo.evaka.caseprocess.CaseProcess
import fi.espoo.evaka.document.childdocument.ChildDocumentDetails
import fi.espoo.evaka.pis.service.PersonDTO
import fi.espoo.evaka.s3.Document
import org.apache.tika.mime.MimeTypes
import trevaka.jaxb.localDateToXMLGregorianCalendar

internal fun transformChildDocument(childDocumentDetails: ChildDocumentDetails, document: Document, childInfo: PersonDTO, caseProcess: CaseProcess): Pair<Collections.Collection, Map<String, Document>> {
    val originalId = childDocumentDetails.id.toString()
    return Collections.Collection().apply {
        type = "record"
        folder = childDocumentDetails.template.processDefinitionNumber
        metadata = Collections.Collection.Metadata().apply {
            title = childTitle(childDocumentDetails.template.name, childInfo)
            calculationBaseDate = localDateToXMLGregorianCalendar(childDocumentDetails.template.validity.start)
            created = childDocumentDetails.publishedAt?.toLocalDate()?.let { localDateToXMLGregorianCalendar(it) }
            agent.addAll(transformToAgents(caseProcess))
        }
        content = Collections.Collection.Content().apply {
            file.add(
                Collections.Collection.Content.File().apply {
                    val mimeTypes = MimeTypes.getDefaultMimeTypes()
                    val mimeType = mimeTypes.forName(document.contentType)
                    name = "$originalId${mimeType.extension}"
                    this.originalId = originalId
                },
            )
        }
    } to mapOf(originalId to document)
}
