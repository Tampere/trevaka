// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.hameenkyro.evaka.template.config

import fi.espoo.evaka.decision.DecisionType
import fi.espoo.evaka.invoicing.service.DocumentLang
import fi.espoo.evaka.shared.template.ITemplateProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TemplateConfiguration {

    @Bean
    fun templateProvider(): ITemplateProvider = HameenkyroTemplateProvider()
}

class HameenkyroTemplateProvider : ITemplateProvider {
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
    override fun getFeeDecisionPath(): String = "hameenkyro/fee-decision/decision"
    override fun getVoucherValueDecisionPath(): String = "hameenkyro/fee-decision/voucher-value-decision"
    override fun getClubDecisionPath(): String = "hameenkyro/club/decision"
    override fun getDaycareVoucherDecisionPath(): String = "hameenkyro/daycare/voucher/decision"
    override fun getDaycareTransferDecisionPath(): String = "hameenkyro/daycare/decision"
    override fun getDaycareDecisionPath(): String = "hameenkyro/daycare/decision"
    override fun getPreschoolDecisionPath(): String = "hameenkyro/preschool/decision"
    override fun getPreparatoryDecisionPath(): String = "hameenkyro/preschool/decision"
    override fun getAssistanceNeedDecisionPath(): String = "hameenkyro/assistance-need/decision"
    override fun getAssistanceNeedPreschoolDecisionPath(): String = "hameenkyro/assistance-need-preschool/decision"
}
