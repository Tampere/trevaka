// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.archival

import com.profium.reception._2022._03.Collections
import fi.espoo.evaka.caseprocess.CaseProcess
import fi.espoo.evaka.invoicing.domain.FeeDecisionDetailed
import fi.espoo.evaka.s3.Document
import org.apache.tika.mime.MimeTypes
import trevaka.jaxb.localDateToXMLGregorianCalendar

internal fun transform(caseProcess: CaseProcess, feeDecision: FeeDecisionDetailed, document: Document): Pair<Collections.Collection, Map<String, Document>> {
    val originalId = feeDecision.id.toString()
    val decisionApprovalDate = feeDecision.approvedAt?.let { localDateToXMLGregorianCalendar(it.toLocalDate()) } ?: error("Fee decision approval date missing, decision: ${feeDecision.id}")
    return Collections.Collection().apply {
        type = "record"
        folder = caseProcess.processDefinitionNumber
        metadata = Collections.Collection.Metadata().apply {
            title = "Maksupäätös, ${feeDecision.headOfFamily.firstName} ${feeDecision.headOfFamily.lastName}, ${feeDecision.headOfFamily.dateOfBirth.format(ARCHIVAL_DATE_FORMATTER)}"
            calculationBaseDate = decisionApprovalDate
            created = decisionApprovalDate
            agent.addAll(createDecisionMakerAgent(feeDecision.financeDecisionHandlerFirstName, feeDecision.financeDecisionHandlerLastName))
        }
        content = Collections.Collection.Content().apply {
            file.add(
                Collections.Collection.Content.File().apply {
                    val mimeTypes = MimeTypes.getDefaultMimeTypes()
                    val mimeType = mimeTypes.forName(document.contentType)
                    name = "Maksupäätös${mimeType.extension}"
                    this.originalId = originalId
                },
            )
        }
    } to mapOf(originalId to document)
}
