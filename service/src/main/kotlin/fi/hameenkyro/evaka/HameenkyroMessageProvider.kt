// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.hameenkyro.evaka

import fi.espoo.evaka.shared.message.IMessageProvider
import fi.espoo.evaka.shared.message.MessageLanguage

class HameenkyroMessageProvider : IMessageProvider {
    override fun getDecisionHeader(lang: MessageLanguage) = "Hämeenkyrön varhaiskasvatukseen liittyvät päätökset"

    override fun getDecisionContent(lang: MessageLanguage) = """
Olette hakeneet lapsellenne varhaiskasvatus- tai kerhopaikkaa Hämeenkyrön varhaiskasvatuksesta. Päätös on luettavissa Suomi.fi-viestit -palvelusta.

Varhaiskasvatus tai kerhopaikka on hakemuksen tehneen huoltajan hyväksyttävissä Hämeenkyrön kunnan varhaiskasvatuksen sähköisessä palvelussa osoitteessa evaka.hameenkyro.fi. Paikkaa ei voi hyväksyä Suomi.fi-viestit -palvelussa.

Huomioittehan, että vastaus päätökseen tulee antaa kahden viikon kuluessa.
"""

    override fun getFeeDecisionHeader(lang: MessageLanguage) = getDecisionHeader(lang)

    override fun getFeeDecisionContent(lang: MessageLanguage) = """
Olette saaneet päätöksen varhaiskasvatuksen asiakasmaksusta Hämeenkyrön kunnalta. Päätös on luettavissa Suomi.fi-viestit -palvelusta.

Varhaiskasvatuksen asiakasmaksu on voimassa toistaiseksi ja perheellä on velvollisuus ilmoittaa, mikäli perheen tulot olennaisesti muuttuvat (+/- 10 %).

Lisätietoja varhaiskasvatuksen asiakasmaksuista

https://www.hameenkyro.fi/kasvatus-ja-opetus/varhaiskasvatus/asiakasmaksut/
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

    override fun getDefaultDecisionAddress(lang: MessageLanguage) = TODO("Not yet implemented")

    override fun getDefaultFinancialDecisionAddress(lang: MessageLanguage) = getDefaultDecisionAddress(lang)
}
