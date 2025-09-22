// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.archival

import com.profium.reception._2022._03.Collections
import fi.espoo.evaka.document.childdocument.ChildDocumentDetails
import fi.espoo.evaka.s3.Document
import org.apache.tika.mime.MimeTypes
import trevaka.jaxb.localDateToXMLGregorianCalendar

internal fun transform(childDocumentDetails: ChildDocumentDetails, document: Document) = Collections.Collection().apply {
    type = "record"
    folder = childDocumentDetails.template.processDefinitionNumber
    metadata = Collections.Collection.Metadata().apply {
        title = childDocumentDetails.template.name
        calculationBaseDate = localDateToXMLGregorianCalendar(childDocumentDetails.template.validity.start)
        created = childDocumentDetails.publishedAt?.toLocalDate()?.let { localDateToXMLGregorianCalendar(it) }
    }
    content = Collections.Collection.Content().apply {
        file.add(
            Collections.Collection.Content.File().apply {
                val mimeTypes = MimeTypes.getDefaultMimeTypes()
                val mimeType = mimeTypes.forName(document.contentType)
                name = "${childDocumentDetails.template.name}${mimeType.extension}"
                originalId = childDocumentDetails.originalId()
            },
        )
    }
}

internal fun ChildDocumentDetails.originalId() = id.toString()
