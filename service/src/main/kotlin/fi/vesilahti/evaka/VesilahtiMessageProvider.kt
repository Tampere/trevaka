// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.vesilahti.evaka

import fi.espoo.evaka.decision.DecisionSendAddress
import fi.espoo.evaka.shared.message.IMessageProvider
import fi.espoo.evaka.shared.message.MessageLanguage

class VesilahtiMessageProvider : IMessageProvider {
    override fun getDecisionHeader(lang: MessageLanguage) = "Vesilahden varhaiskasvatukseen liittyvät päätökset"

    override fun getDecisionContent(lang: MessageLanguage) = """
Olette hakeneet lapsellenne varhaiskasvatus- tai kerhopaikkaa Vesilahden varhaiskasvatuksesta. Päätös on luettavissa Suomi.fi-viestit -palvelusta.

Varhaiskasvatus tai kerhopaikka on hakemuksen tehneen huoltajan hyväksyttävissä Vesilahden kunnan varhaiskasvatuksen sähköisessä palvelussa osoitteessa evaka.vesilahti.fi. Paikkaa ei voi hyväksyä Suomi.fi-viestit -palvelussa.

Huomioittehan, että vastaus päätökseen tulee antaa kahden viikon kuluessa.
"""

    override fun getFeeDecisionHeader(lang: MessageLanguage) = getDecisionHeader(lang)

    override fun getFeeDecisionContent(lang: MessageLanguage) = """
Olette saaneet päätöksen varhaiskasvatuksen asiakasmaksusta Vesilahden kunnalta. Päätös on luettavissa Suomi.fi-viestit -palvelusta.

Varhaiskasvatuksen asiakasmaksu on voimassa toistaiseksi ja perheellä on velvollisuus ilmoittaa, mikäli perheen tulot olennaisesti muuttuvat (+/- 10 %).

Lisätietoja varhaiskasvatuksen asiakasmaksuista

https://www.vesilahti.fi/kasvatus-ja-opetus/varhaiskasvatus/asiakasmaksut/
"""

    override fun getVoucherValueDecisionHeader(lang: MessageLanguage) = getDecisionHeader(lang)

    override fun getVoucherValueDecisionContent(lang: MessageLanguage) = getFeeDecisionContent(lang)

    override fun getAssistanceNeedDecisionHeader(lang: MessageLanguage) = getDecisionHeader(lang)

    override fun getAssistanceNeedDecisionContent(lang: MessageLanguage) = """
Lapsellenne on tehty päätös tuen tarpeesta. Voit katsella päätöstä eVakassa.

Koska olette ottanut Suomi.fi -palvelun käyttöönne, on päätös myös luettavissa alla olevista liitteistä.
"""

    override fun getAssistanceNeedPreschoolDecisionHeader(lang: MessageLanguage) = getDecisionHeader(lang)

    override fun getAssistanceNeedPreschoolDecisionContent(lang: MessageLanguage) = getAssistanceNeedDecisionContent(lang)

    override fun getDefaultDecisionAddress(lang: MessageLanguage): DecisionSendAddress = when (lang) {
        MessageLanguage.FI -> DecisionSendAddress(
            street = "PL 75",
            postalCode = "90015",
            postOffice = "Oulu",
            row1 = "Varhaiskasvatuksen palveluohjaus",
            row2 = "Asiakaspalvelu",
            row3 = "PL 75, 90015 Oulu",
        )
        MessageLanguage.SV -> DecisionSendAddress(
            street = "PL 75",
            postalCode = "90015",
            postOffice = "Oulu",
            row1 = "Varhaiskasvatuksen palveluohjaus",
            row2 = "Asiakaspalvelu",
            row3 = "PL 75, 90015 Oulu",
        )
    }

    override fun getDefaultFinancialDecisionAddress(lang: MessageLanguage) = getDefaultDecisionAddress(lang)
}
