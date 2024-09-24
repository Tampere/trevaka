// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.pirkkala.evaka.template.config

import fi.espoo.evaka.decision.DecisionType
import fi.espoo.evaka.shared.domain.OfficialLanguage
import fi.espoo.evaka.shared.template.ITemplateProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TemplateConfiguration {

    @Bean
    fun templateProvider(): ITemplateProvider = PirkkalaTemplateProvider()
}

class PirkkalaTemplateProvider : ITemplateProvider {
    override fun getLocalizedFilename(type: DecisionType, lang: OfficialLanguage): String =
        when (type) {
            DecisionType.CLUB -> "Kerhopäätös"
            DecisionType.DAYCARE,
            DecisionType.DAYCARE_PART_TIME,
            -> "Varhaiskasvatuspäätös"
            DecisionType.PRESCHOOL -> "Esiopetuspäätös"
            DecisionType.PRESCHOOL_DAYCARE,
            DecisionType.PRESCHOOL_CLUB,
            -> "Esiopetusta_täydentävän_toiminnan_päätös"
            DecisionType.PREPARATORY_EDUCATION -> "Valmistavan_opetuksen_päätös"
        }
    override fun getFeeDecisionPath(): String = "pirkkala/fee-decision/decision"
    override fun getVoucherValueDecisionPath(): String = "pirkkala/fee-decision/voucher-value-decision"
    override fun getClubDecisionPath(): String = "pirkkala/club/decision"
    override fun getDaycareVoucherDecisionPath(): String = "pirkkala/daycare/voucher/decision"
    override fun getDaycareTransferDecisionPath(): String = "pirkkala/daycare/decision"
    override fun getDaycareDecisionPath(): String = "pirkkala/daycare/decision"
    override fun getPreschoolDecisionPath(): String = "pirkkala/daycare/decision"
    override fun getPreparatoryDecisionPath(): String = throw Error("Not supported")
    override fun getAssistanceNeedDecisionPath(): String = "pirkkala/assistance/decision"
    override fun getAssistanceNeedPreschoolDecisionPath(): String = "pirkkala/assistance-preschool/decision"
}
