// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.template.config

import fi.espoo.evaka.decision.DecisionType
import fi.espoo.evaka.shared.domain.OfficialLanguage
import fi.espoo.evaka.shared.template.ITemplateProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TemplateConfiguration {

    @Bean
    fun templateProvider(): ITemplateProvider = TampereTemplateProvider()
}

internal class TampereTemplateProvider : ITemplateProvider {
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
            DecisionType.PREPARATORY_EDUCATION -> throw Error("Not supported")
        }

    override fun getFeeDecisionPath(): String = "tampere/fee-decision/decision"
    override fun getVoucherValueDecisionPath(): String = "tampere/fee-decision/voucher-value-decision"
    override fun getClubDecisionPath(): String = "tampere/club/decision"
    override fun getDaycareVoucherDecisionPath(): String = "tampere/daycare/voucher/decision"
    override fun getDaycareTransferDecisionPath(): String = "tampere/daycare/decision"
    override fun getDaycareDecisionPath(): String = "tampere/daycare/decision"
    override fun getPreschoolDecisionPath(): String = "tampere/daycare/decision"
    override fun getPreparatoryDecisionPath(): String = throw Error("Not supported")
    override fun getAssistanceNeedDecisionPath(): String = "tampere/assistance/decision"
    override fun getAssistanceNeedPreschoolDecisionPath(): String = "tampere/assistance-preschool/decision"
}
