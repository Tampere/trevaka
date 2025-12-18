// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka.archival.tweb

import com.profium.reception._2022._03.Collections
import fi.espoo.evaka.caseprocess.CaseProcess
import fi.espoo.evaka.decision.Decision
import fi.espoo.evaka.decision.DecisionType
import fi.espoo.evaka.pis.service.PersonDTO
import fi.espoo.evaka.s3.Document
import org.apache.tika.mime.MimeTypes
import trevaka.archival.status
import trevaka.jaxb.localDateToXMLGregorianCalendar

internal fun transformDecision(
    caseProcess: CaseProcess,
    decision: Decision,
    document: Document,
    child: PersonDTO,
): Pair<Collections.Collection, Map<String, Document>> {
    val originalId = decision.id.toString()
    return Collections.Collection().apply {
        type = "record"
        folder = caseProcess.processDefinitionNumber
        metadata = Collections.Collection.Metadata().apply {
            title = childTitle(type(decision), status(decision), child)
            calculationBaseDate = localDateToXMLGregorianCalendar(decision.endDate.plusDays(1))
            created = decision.sentDate?.let { localDateToXMLGregorianCalendar(it) }
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

private fun type(decision: Decision): String = when (decision.type) {
    DecisionType.CLUB -> "Kerhopäätös"
    DecisionType.DAYCARE -> "Varhaiskasvatuspäätös"
    DecisionType.DAYCARE_PART_TIME -> "Varhaiskasvatuspäätös"
    DecisionType.PRESCHOOL -> "Esiopetuspäätös"
    DecisionType.PRESCHOOL_DAYCARE -> "Täydentävän varhaiskasvatuksen päätös"
    DecisionType.PRESCHOOL_CLUB -> "Esiopetuksen kerhon päätös"
    DecisionType.PREPARATORY_EDUCATION -> throw UnsupportedOperationException("Preparatory education")
}
