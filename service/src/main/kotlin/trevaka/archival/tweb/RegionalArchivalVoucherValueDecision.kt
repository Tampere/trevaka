// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka.archival.tweb

import com.profium.reception._2022._03.Collections
import fi.espoo.evaka.caseprocess.CaseProcess
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionDetailed
import fi.espoo.evaka.s3.Document
import org.apache.tika.mime.MimeTypes
import trevaka.jaxb.localDateToXMLGregorianCalendar

internal fun transformVoucherDecision(caseProcess: CaseProcess, voucherValueDecision: VoucherValueDecisionDetailed, document: Document): Pair<Collections.Collection, Map<String, Document>> {
    val originalId = voucherValueDecision.id.toString()
    return Collections.Collection().apply {
        type = "record"
        folder = caseProcess.processDefinitionNumber
        metadata = Collections.Collection.Metadata().apply {
            title = hofTitle("Arvopäätös", voucherValueDecision.headOfFamily)
            calculationBaseDate = localDateToXMLGregorianCalendar(voucherValueDecision.validTo.plusDays(1))
            created = voucherValueDecision.approvedAt?.let { localDateToXMLGregorianCalendar(it.toLocalDate()) }
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
