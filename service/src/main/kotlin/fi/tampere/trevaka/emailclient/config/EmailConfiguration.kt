// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.emailclient.config

import fi.espoo.evaka.emailclient.IEmailMessageProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("trevaka")
@Configuration
class EmailConfiguration {

    @Bean
    fun emailMessageProvider(): IEmailMessageProvider = EmailMessageProvider()
}

internal class EmailMessageProvider(): IEmailMessageProvider {
    override val subjectForPendingDecisionEmail: String = "Toimenpiteitäsi odotetaan"
    override val subjectForClubApplicationReceivedEmail: String = "Hakemus vastaanotettu"
    override val subjectForDaycareApplicationReceivedEmail: String = "Hakemus vastaanotettu"
    override val subjectForPreschoolApplicationReceivedEmail: String = ""

    override fun getPendingDecisionEmailHtml(): String {
        return """
            <p>Olet saanut päätöksen/ilmoituksen Tampereen varhaiskasvatukselta, joka odottaa toimenpiteitäsi. Myönnetty varhaiskasvatus-/kerhopaikka tulee hyväksyä tai hylätä kahden viikon sisällä päätöksen saapumisesta.</p>
            
            <p>Hakemuksen tekijä voi hyväksyä tai hylätä varhaiskasvatus-/kerhopaikan kirjautumalla osoitteeseen <a href="https://varhaiskasvatus.tampere.fi">varhaiskasvatus.tampere.fi</a> tai ottamalla yhteyttä päätöksellä mainittuun päiväkodin johtajaan.</p>
            
            <p>Tähän viestiin ei voi vastata. Tarvittaessa ole yhteydessä Varhaiskasvatuksen asiakaspalveluun: <a href="mailto:varhaiskasvatus.asiakaspalvelu@tampere.fi">varhaiskasvatus.asiakaspalvelu@tampere.fi</a>, puh. <a href="tel:+358408007260">040 800 7260</a> (ma–pe klo 9–12).</p>
        """.trimIndent()
    }

    override fun getPendingDecisionEmailText(): String {
        return """
            Olet saanut päätöksen/ilmoituksen Tampereen varhaiskasvatukselta, joka odottaa toimenpiteitäsi. Myönnetty varhaiskasvatus-/kerhopaikka tulee hyväksyä tai hylätä kahden viikon sisällä päätöksen saapumisesta.
            
            Hakemuksen tekijä voi hyväksyä tai hylätä varhaiskasvatus-/kerhopaikan kirjautumalla osoitteeseen https://varhaiskasvatus.tampere.fi tai ottamalla yhteyttä päätöksellä mainittuun päiväkodin johtajaan.
            
            Tähän viestiin ei voi vastata. Tarvittaessa ole yhteydessä Varhaiskasvatuksen asiakaspalveluun: varhaiskasvatus.asiakaspalvelu@tampere.fi, puh. 040 800 7260 (ma–pe klo 9–12).
        """.trimIndent()
    }

    override fun getClubApplicationReceivedEmailHtml(): String {
        return """
            <p>Hyvä huoltaja,</p>
            
            <p>lapsenne kerhohakemus on vastaanotettu.</p>
            
            <p>Hakemuksen tehnyt huoltaja voi muokata hakemusta Tampereen varhaiskasvatuksen verkkopalvelussa osoitteessa <a href="https://varhaiskasvatus.tampere.fi">varhaiskasvatus.tampere.fi</a> siihen saakka, kunnes se on otettu käsittelyyn asiakaspalvelussa.</p>
            
            <p>Kirjallinen ilmoitus myönnetystä kerhopaikasta lähetetään huoltajalle Suomi.fi-viestit -palveluun. Mikäli huoltaja ei ole ottanut Suomi.fi-viestit -palvelua käyttöön, ilmoitus lähetetään hänelle postitse.</p> 
            
            <p>Myönnetyn kerhopaikan voi hyväksyä / hylätä sähköisesti Tampereen varhaiskasvatuksen verkkopalvelussa osoitteessa <a href="https://varhaiskasvatus.tampere.fi">varhaiskasvatus.tampere.fi</a>. Kerhohakemus kohdistuu yhdelle kerhon toimintakaudelle. Kauden päättyessä hakemus poistetaan järjestelmästä.</p>
            
            <p>Lisätietoa hakemuksen käsittelystä ja kerhopaikan myöntämisestä saa varhaiskasvatuksen ja esiopetuksen asiakaspalvelusta:</p>
            
            <p>
            Tampereen kaupunki<br/>
            Sivistyspalvelut<br/>
            Varhaiskasvatus ja esiopetus<br/>
            Asiakaspalvelu<br/>
            <a href="mailto:varhaiskasvatus.asiakaspalvelu@tampere.fi">varhaiskasvatus.asiakaspalvelu@tampere.fi</a><br/>
            Puh. <a href="tel:+358408007260">040&nbsp;800&nbsp;7260</a> (ma–pe klo 9–12)<br/>
            <a href="https://www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus.html">www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus.html</a>
            </p>
            
            <p>Tämä on automaattinen viesti, joka kertoo lomakkeen tallennuksesta. Viestiin ei voi vastata reply-/ vastaa-toiminnolla.</p>
        """.trimIndent()
    }

    override fun getClubApplicationReceivedEmailText(): String {
        return """
            Hyvä huoltaja, 

            lapsenne kerhohakemus on vastaanotettu.
            
            Hakemuksen tehnyt huoltaja voi muokata hakemusta Tampereen varhaiskasvatuksen verkkopalvelussa osoitteessa varhaiskasvatus.tampere.fi siihen saakka, kunnes se on otettu käsittelyyn asiakaspalvelussa.

            Kirjallinen ilmoitus myönnetystä kerhopaikasta lähetetään huoltajalle Suomi.fi-viestit -palveluun. Mikäli huoltaja ei ole ottanut Suomi.fi-viestit -palvelua käyttöön, ilmoitus lähetetään hänelle postitse.
            
            Myönnetyn kerhopaikan voi hyväksyä / hylätä sähköisesti Tampereen varhaiskasvatuksen verkkopalvelussa osoitteessa varhaiskasvatus.tampere.fi. Kerhohakemus kohdistuu yhdelle kerhon toimintakaudelle. Kauden päättyessä hakemus poistetaan järjestelmästä.
            
            Lisätietoa hakemuksen käsittelystä ja kerhopaikan myöntämisestä saa varhaiskasvatuksen ja esiopetuksen asiakaspalvelusta:
            
            Tampereen kaupunki
            Sivistyspalvelut
            Varhaiskasvatus ja esiopetus
            Asiakaspalvelu
            varhaiskasvatus.asiakaspalvelu@tampere.fi
            Puh. 040 800 7260 (ma–pe klo 9–12)
            www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus.html
            
            Tämä on automaattinen viesti, joka kertoo lomakkeen tallennuksesta. Viestiin ei voi vastata reply-/ vastaa-toiminnolla.
        """.trimIndent()
    }

    override fun getDaycareApplicationReceivedEmailHtml(): String {
        return """
            <p>Hyvä huoltaja,</p>
            
            <p>lapsenne varhaiskasvatushakemus on vastaanotettu.</p>
            
            <p>Varhaiskasvatushakemuksella on neljän (4) kuukauden hakuaika. Hakemuksen tehnyt huoltaja voi muokata hakemusta Tampereen varhaiskasvatuksen verkkopalvelussa osoitteessa <a href="https://varhaiskasvatus.tampere.fi">varhaiskasvatus.tampere.fi</a> siihen saakka, kunnes se on otettu käsittelyyn.</p>
            
            <p>Saatte tiedon lapsenne varhaiskasvatuspaikasta noin kuukautta ennen palvelutarpeen alkamista tai hakemuksen lakisääteisen järjestelyajan päättymistä. Hakemuksen lakisääteinen järjestelyaika on neljä (4) kuukautta hakemuksen saapumisesta.</p> 
            
            <p>Mikäli hakemuksenne on kiireellinen, ottakaa yhteyttä viipymättä Varhaiskasvatuksen asiakaspalveluun: <a href="mailto:varhaiskasvatus.asiakaspalvelu@tampere.fi">varhaiskasvatus.asiakaspalvelu@tampere.fi</a>, puh. <a href="tel:0408007260">040&nbsp;800&nbsp;7260</a> (ma–pe klo 9–12). Hakuaika kiireellisissä hakemuksissa on minimissään kaksi (2) viikkoa ja se alkaa siitä päivästä, kun olette olleet yhteydessä asiakaspalveluun.</p>
            
            <p>Kirjallinen päätös varhaiskasvatuspaikasta lähetetään huoltajalle Suomi.fi-viestit -palveluun. Mikäli huoltaja ei ole ottanut Suomi.fi-viestit -palvelua käyttöön, päätös lähetetään hänelle postitse.</p>
            
            <p>Myönnetyn varhaiskasvatuspaikan voi hyväksyä / hylätä sähköisesti Tampereen varhaiskasvatuksen verkkopalvelussa osoitteessa <a href="https://varhaiskasvatus.tampere.fi">varhaiskasvatus.tampere.fi</a>. Mikäli haette paikkaa palvelusetelipäiväkodista, olkaa yhteydessä kyseiseen päiväkotiin viimeistään hakemuksen jättämisen jälkeen.</p>
            
            <p>Ilta- ja vuorohoitoa haettaessa, hakemuksen liitteeksi tulee toimittaa molempien samassa taloudessa asuvien huoltajien todistukset työnantajalta vuorotyöstä tai oppilaitoksesta iltaisin tapahtuvasta opiskelusta. Hakemusta käsitellään vuorohoidon hakemuksena vasta sen jälkeen, kun edellä mainitut todistukset on toimitettu. Tarvittavat liitteet voi lisätä suoraan sähköiselle hakemukselle tai toimittaa postitse osoitteeseen Tampereen kaupunki, Varhaiskasvatuksen asiakaspalvelu, PL 487, 33101 Tampere.</p> 
            
            <p>Hakiessanne lapsellenne siirtoa toiseen varhaiskasvatusyksikköön, hakemuksella ei ole hakuaikaa. Siirrot pystytään toteuttamaan pääsääntöisesti elokuusta alkaen. Mikäli lapsen nykyinen hoitopaikka irtisanotaan, myös siirtohakemus poistuu.</p> 
            
            <p>Lisätietoa hakemuksen käsittelystä ja varhaiskasvatuspaikan myöntämisestä saa varhaiskasvatuksen ja esiopetuksen asiakaspalvelusta:</p>
            
            <p>
            Tampereen kaupunki<br/>
            Sivistyspalvelut<br/>
            Varhaiskasvatus ja esiopetus<br/>
            Asiakaspalvelu<br/>
            <a href="mailto:varhaiskasvatus.asiakaspalvelu@tampere.fi">varhaiskasvatus.asiakaspalvelu@tampere.fi</a><br/>
            Puh. <a href="tel:+358408007260">040&nbsp;800&nbsp;7260</a>, ma–pe klo 9–12<br/>
            Postiosoite: PL 487, 33101 Tampere<br/>
            <a href="https://www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus.html">www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus.html</a>
            </p>
            
            <p>Tämä on automaattinen viesti, joka kertoo lomakkeen tallennuksesta. Viestiin ei voi vastata reply-/ vastaa-toiminnolla.</p>
        """.trimIndent()
    }

    override fun getDaycareApplicationReceivedEmailText(): String {
        return """
            Hyvä huoltaja,

            lapsenne varhaiskasvatushakemus on vastaanotettu.
            
            Varhaiskasvatushakemuksella on neljän (4) kuukauden hakuaika. Hakemuksen tehnyt huoltaja voi muokata hakemusta Tampereen varhaiskasvatuksen verkkopalvelussa osoitteessa varhaiskasvatus.tampere.fi siihen saakka, kunnes se on otettu käsittelyyn.
            
            Saatte tiedon lapsenne varhaiskasvatuspaikasta noin kuukautta ennen palvelutarpeen alkamista tai hakemuksen lakisääteisen järjestelyajan päättymistä. Hakemuksen lakisääteinen järjestelyaika on neljä (4) kuukautta hakemuksen saapumisesta.
            
            Mikäli hakemuksenne on kiireellinen, ottakaa yhteyttä viipymättä Varhaiskasvatuksen asiakaspalveluun: varhaiskasvatus.asiakaspalvelu@tampere.fi, puh. 040 800 7260 (ma–pe klo 9–12). Hakuaika kiireellisissä hakemuksissa on minimissään kaksi (2) viikkoa ja se alkaa siitä päivästä, kun olette olleet yhteydessä asiakaspalveluun.
            
            Kirjallinen päätös varhaiskasvatuspaikasta lähetetään huoltajalle Suomi.fi-viestit -palveluun. Mikäli huoltaja ei ole ottanut Suomi.fi-viestit -palvelua käyttöön, päätös lähetetään hänelle postitse.
            
            Myönnetyn varhaiskasvatuspaikan voi hyväksyä / hylätä sähköisesti Tampereen varhaiskasvatuksen verkkopalvelussa osoitteessa varhaiskasvatus.tampere.fi. Mikäli haette paikkaa palvelusetelipäiväkodista, olkaa yhteydessä kyseiseen päiväkotiin viimeistään hakemuksen jättämisen jälkeen.
            
            Ilta- ja vuorohoitoa haettaessa, hakemuksen liitteeksi tulee toimittaa molempien samassa taloudessa asuvien huoltajien todistukset työnantajalta vuorotyöstä tai oppilaitoksesta iltaisin tapahtuvasta opiskelusta. Hakemusta käsitellään vuorohoidon hakemuksena vasta sen jälkeen, kun edellä mainitut todistukset on toimitettu. Tarvittavat liitteet voi lisätä suoraan sähköiselle hakemukselle tai toimittaa postitse osoitteeseen Tampereen kaupunki, Varhaiskasvatuksen asiakaspalvelu, PL 487, 33101 Tampere.
            
            Hakiessanne lapsellenne siirtoa toiseen varhaiskasvatusyksikköön, hakemuksella ei ole hakuaikaa. Siirrot pystytään toteuttamaan pääsääntöisesti elokuusta alkaen. Mikäli lapsen nykyinen hoitopaikka irtisanotaan, myös siirtohakemus poistuu.
            
            Lisätietoa hakemuksen käsittelystä ja varhaiskasvatuspaikan myöntämisestä saa varhaiskasvatuksen ja esiopetuksen asiakaspalvelusta:
            
            Tampereen kaupunki
            Sivistyspalvelut
            Varhaiskasvatus ja esiopetus
            Asiakaspalvelu
            varhaiskasvatus.asiakaspalvelu@tampere.fi
            Puh. 040 800 7260, ma–pe klo 9–12
            Postiosoite: PL 487, 33101 Tampere
            www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus.html
            
            Tämä on automaattinen viesti, joka kertoo lomakkeen tallennuksesta. Viestiin ei voi vastata reply-/ vastaa-toiminnolla.
        """.trimIndent()
    }

    override fun getPreschoolApplicationReceivedEmailHtml(withinApplicationPeriod: Boolean): String {
        throw Error("Preschool not in use!")
    }

    override fun getPreschoolApplicationReceivedEmailText(withinApplicationPeriod: Boolean): String {
        throw Error("Preschool not in use!")
    }

}