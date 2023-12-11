// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.vesilahti.evaka

import fi.espoo.evaka.decision.DecisionType
import fi.espoo.evaka.invoicing.service.DocumentLang
import fi.espoo.evaka.shared.template.ITemplateProvider

class VesilahtiTemplateProvider : ITemplateProvider {
    override fun getFeeDecisionPath(): String = "vesilahti/fee-decision/decision"
    override fun getVoucherValueDecisionPath(): String = "vesilahti/fee-decision/voucher-value-decision"
    override fun getClubDecisionPath(): String = "vesilahti/club/decision"
    override fun getDaycareVoucherDecisionPath(): String = "vesilahti/daycare/voucher/decision"
    override fun getDaycareTransferDecisionPath(): String = "vesilahti/daycare/decision"
    override fun getDaycareDecisionPath(): String = "vesilahti/daycare/decision"
    override fun getPreschoolDecisionPath(): String = "vesilahti/preschool/decision"
    override fun getPreparatoryDecisionPath(): String = "vesilahti/preschool/decision"
    override fun getAssistanceNeedDecisionPath(): String = "vesilahti/assistance-need/decision"
    override fun getAssistanceNeedPreschoolDecisionPath(): String = "vesilahti/assistance-need-preschool/decision"

    override fun getLocalizedFilename(type: DecisionType, lang: DocumentLang): String =
        when (type) {
            DecisionType.CLUB -> "Kerhopäätös"
            DecisionType.DAYCARE,
            DecisionType.DAYCARE_PART_TIME,
            -> "Varhaiskasvatuspäätös"
            DecisionType.PRESCHOOL -> "Esiopetuspäätös"
            DecisionType.PRESCHOOL_DAYCARE,
            DecisionType.PRESCHOOL_CLUB,
            -> "Esiopetukseen liittyvän toiminnan päätös"
            DecisionType.PREPARATORY_EDUCATION -> "Valmistavan opetuksen päätös"
        }
}
