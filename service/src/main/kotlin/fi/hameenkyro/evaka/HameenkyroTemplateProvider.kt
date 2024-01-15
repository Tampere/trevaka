// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.hameenkyro.evaka

import fi.espoo.evaka.decision.DecisionType
import fi.espoo.evaka.invoicing.service.DocumentLang
import fi.espoo.evaka.shared.template.ITemplateProvider

class HameenkyroTemplateProvider : ITemplateProvider {
    override fun getLocalizedFilename(type: DecisionType, lang: DocumentLang): String {
        TODO("Not yet implemented")
    }

    override fun getFeeDecisionPath(): String {
        TODO("Not yet implemented")
    }

    override fun getVoucherValueDecisionPath(): String {
        TODO("Not yet implemented")
    }

    override fun getClubDecisionPath(): String {
        TODO("Not yet implemented")
    }

    override fun getDaycareVoucherDecisionPath(): String {
        TODO("Not yet implemented")
    }

    override fun getDaycareTransferDecisionPath(): String {
        TODO("Not yet implemented")
    }

    override fun getDaycareDecisionPath(): String {
        TODO("Not yet implemented")
    }

    override fun getPreschoolDecisionPath(): String {
        TODO("Not yet implemented")
    }

    override fun getPreparatoryDecisionPath(): String {
        TODO("Not yet implemented")
    }

    override fun getAssistanceNeedDecisionPath(): String {
        TODO("Not yet implemented")
    }

    override fun getAssistanceNeedPreschoolDecisionPath(): String {
        TODO("Not yet implemented")
    }
}
