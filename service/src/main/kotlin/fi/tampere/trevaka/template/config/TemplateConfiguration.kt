// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.template.config

import fi.espoo.evaka.shared.template.ITemplateProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TemplateConfiguration {

    @Bean
    fun templateProvider(): ITemplateProvider = TrevakaTemplateProvider()

}

internal class TrevakaTemplateProvider : ITemplateProvider {
    override fun getFeeDecisionPath(): String = "tampere/fee-decision/decision"
    override fun getVoucherValueDecisionPath(): String = "tampere/fee-decision/voucher-value-decision"
    override fun getClubDecisionPath(): String = "tampere/club/decision"
    override fun getDaycareVoucherDecisionPath(): String = "tampere/daycare/voucher/decision"
    override fun getDaycareTransferDecisionPath(): String = "tampere/daycare/transfer/decision"
    override fun getDaycareDecisionPath(): String = "tampere/daycare/decision"

    override fun getPreschoolDecisionPath(): String =
        throw UnsupportedOperationException("Preschool decision is not supported")

    override fun getPreparatoryDecisionPath(): String =
        throw UnsupportedOperationException("Preparatory decision is not supported")
}
