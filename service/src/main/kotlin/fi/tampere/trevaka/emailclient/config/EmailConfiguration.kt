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
    override val subjectForPendingDecisionEmail: String = "Päätös varhaiskasvatuksesta"
    override val subjectForClubApplicationReceivedEmail: String = "Hakemus vastaanotettu"
    override val subjectForDaycareApplicationReceivedEmail: String = "Hakemus vastaanotettu"
    override val subjectForPreschoolApplicationReceivedEmail: String = ""

    override fun getPendingDecisionEmailHtml(): String {
        return """
            <p>Sinulla on vastaamaton päätös Tampereen varhaiskasvatukselta. Päätös tulee hyväksyä tai hylätä kahden viikon sisällä sen saapumisesta.</p> 

            <p>Hakemuksen tekijä voi hyväksyä tai hylätä vastaamattomat päätökset kirjautumalla osoitteeseen varhaiskasvatus.tampere.fi</p>  

            <p>Tähän viestiin ei voi vastata. Tarvittaessa ole yhteydessä</p>  

            <pre>
            Varhaiskasvatuksen asiakaspalvelu 
            varhaiskasvatus.asiakaspalvelu@tampere.fi 
            p. 040 8007260, soittoaika ma-pe klo 9-12
            </pre>
        """.trimIndent()
    }

    override fun getPendingDecisionEmailText(): String {
        return """
            Sinulla on vastaamaton päätös Tampereen varhaiskasvatukselta. Päätös tulee hyväksyä tai hylätä kahden viikon sisällä sen saapumisesta. 

            Hakemuksen tekijä voi hyväksyä tai hylätä vastaamattomat päätökset kirjautumalla osoitteeseen varhaiskasvatus.tampere.fi  

            Tähän viestiin ei voi vastata. Tarvittaessa ole yhteydessä  

            Varhaiskasvatuksen asiakaspalvelu 
            varhaiskasvatus.asiakaspalvelu@tampere.fi 
            p. 040 8007260, soittoaika ma-pe klo 9-12
        """.trimIndent()
    }

    override fun getClubApplicationReceivedEmailHtml(): String {
        return """
            <p>Hyvä huoltaja,</p>
            
            <p>lapsenne kerhohakemus on vastaanotettu.</p>
            
            <p>Hakemuksen tehnyt huoltaja voi muokata hakemusta osoitteessa <a href="https://varhaiskasvatus.tampere.fi">varhaiskasvatus.tampere.fi</a> siihen saakka, kunnes se on otettu käsittelyyn asiakaspalvelussa.</p> 
            
            <p>Kirjallinen ilmoitus myönnetystä kerhopaikasta lähetetään huoltajalle Suomi.fi-viestit -palveluun. Mikäli huoltaja ei ole ottanut Suomi.fi-viestit -palvelua käyttöön, ilmoitus lähetetään hänelle postitse.</p> 
            
            <p>Myönnetyn kerhopaikan voi hyväksyä/hylätä sähköisesti osoitteessa <a href="https://varhaiskasvatus.tampere.fi">varhaiskasvatus.tampere.fi</a>. Kerhohakemus kohdistuu yhdelle kerhon toimintakaudelle. Kauden päättyessä hakemus poistetaan järjestelmästä.</p> 
            
            <p>Lisätietoa hakemuksen käsittelystä ja kerhopaikan myöntämisestä saa varhaiskasvatuksen ja esiopetuksen asiakaspalvelusta:</p>
            
            <pre>
            Tampereen kaupunki
            Sivistyspalvelut
            Varhaiskasvatus ja esiopetus
            Asiakaspalvelu
            <a href="mailto:varhaiskasvatus.asiakaspalvelu@tampere.fi">varhaiskasvatus.asiakaspalvelu@tampere.fi</a>
            <a href="tel:+358408007260">040&nbsp;800&nbsp;7260</a>, ma-pe klo 9-12
            <a href="https://www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus.html">www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus.html</a>
            </pre>
            
            <p><strong>HUOM!<strong>  Tämä on automaattinen viesti, joka kertoo lomakkeen tallennuksesta. Älä vastaa tähän viestin reply-/vastaa-toiminnolla!</p>
        """.trimIndent()
    }

    override fun getClubApplicationReceivedEmailText(): String {
        return """
            Hyvä huoltaja, 

            lapsenne kerhohakemus on vastaanotettu.
            
            Hakemuksen tehnyt huoltaja voi muokata hakemusta osoitteessa varhaiskasvatus.tampere.fi siihen saakka, kunnes se on otettu käsittelyyn asiakaspalvelussa. 
            
            Kirjallinen ilmoitus myönnetystä kerhopaikasta lähetetään huoltajalle Suomi.fi-viestit -palveluun. Mikäli huoltaja ei ole ottanut Suomi.fi-viestit -palvelua käyttöön, ilmoitus lähetetään hänelle postitse. 
            
            Myönnetyn kerhopaikan voi hyväksyä/hylätä sähköisesti osoitteessa varhaiskasvatus.tampere.fi. Kerhohakemus kohdistuu yhdelle kerhon toimintakaudelle. Kauden päättyessä hakemus poistetaan järjestelmästä.
            
            Lisätietoa hakemuksen käsittelystä ja kerhopaikan myöntämisestä saa varhaiskasvatuksen ja esiopetuksen asiakaspalvelusta:
            
            Tampereen kaupunki
            Sivistyspalvelut
            Varhaiskasvatus ja esiopetus
            Asiakaspalvelu
            varhaiskasvatus.asiakaspalvelu@tampere.fi
            040 800 7260, ma-pe klo 9-12
            www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus.html
            
            HUOM!  Tämä on automaattinen viesti, joka kertoo lomakkeen tallennuksesta. Älä vastaa tähän viestin reply-/vastaa-toiminnolla!
        """.trimIndent()
    }

    override fun getDaycareApplicationReceivedEmailHtml(): String {
        return """
            <p>Hyvä huoltaja,</p>
            
            <p>lapsenne varhaiskasvatushakemus on vastaanotettu.</p>
            
            <p>Varhaiskasvatushakemuksella on neljän (4) kuukauden hakuaika. Hakemuksen tehnyt huoltaja voi muokata hakemusta osoitteessa <a href="https://varhaiskasvatus.tampere.fi">varhaiskasvatus.tampere.fi</a> siihen saakka, kunnes se on otettu käsittelyyn.</p> 
            
            <p>Saatte tiedon lapsenne varhaiskasvatuspaikasta noin kuukautta ennen palvelutarpeen alkamista tai hakemuksen lakisääteisen järjestelyajan päättymistä. Hakemuksen lakisääteinen järjestelyaika on neljä (4) kuukautta hakemuksen saapumisesta.</p> 
            
            <p>Mikäli hakemuksenne on kiireellinen, ottakaa yhteyttä viipymättä Varhaiskasvatuksen asiakaspalveluun: <a href="mailto:varhaiskasvatus.asiakaspalvelu@tampere.fi">varhaiskasvatus.asiakaspalvelu@tampere.fi</a>, <a href="tel:0408007260">040&nbsp;800&nbsp;7260"</a> (ma-pe klo 9-12). Hakuaika kiireellisissä hakemuksissa on minimissään kaksi (2) viikkoa ja se alkaa siitä päivästä, kun olette olleet yhteydessä asiakaspalveluun.</p>
            
            <p>Kirjallinen päätös varhaiskasvatuspaikasta lähetetään huoltajalle Suomi.fi-viestit -palveluun. Mikäli huoltaja ei ole ottanut Suomi.fi-viestit -palvelua käyttöön, päätös lähetetään hänelle postitse.</p>
            
            <p>Myönnetyn varhaiskasvatuspaikan voi hyväksyä/hylätä sähköisesti osoitteessa <a href="https://varhaiskasvatus.tampere.fi">varhaiskasvatus.tampere.fi</a>. Mikäli haette paikkaa palvelusetelipäiväkodista, olkaa yhteydessä kyseiseen päiväkotiin viimeistään hakemuksen jättämisen jälkeen.</p>
            
            <p>Ilta- ja vuorohoitoa haettaessa, hakemuksen liitteeksi tulee toimittaa molempien samassa taloudessa asuvien huoltajien todistukset työnantajalta vuorotyöstä tai oppilaitoksesta iltaisin tapahtuvasta opiskelusta. Hakemusta käsitellään vuorohoidon hakemuksena vasta sen jälkeen, kun edellä mainitut todistukset on toimitettu. Tarvittavat liitteet voi lisätä suoraan sähköiselle hakemukselle tai toimittaa postitse osoitteeseen Tampereen kaupunki, Varhaiskasvatuksen asiakaspalvelu, PL 487, 33101 Tampere.</p> 
            
            <p>Hakiessanne lapsellenne siirtoa toiseen varhaiskasvatusyksikköön, hakemuksella ei ole hakuaikaa. Siirrot pystytään toteuttamaan pääsääntöisesti elokuusta alkaen. Mikäli lapsen nykyinen hoitopaikka irtisanotaan, myös siirtohakemus poistuu.</p> 
            
            <p>Lisätietoa hakemuksen käsittelystä ja varhaiskasvatuspaikan myöntämisestä saa varhaiskasvatuksen ja esiopetuksen asiakaspalvelusta:</p>
            
            <pre>
            Tampereen kaupunki
            Sivistyspalvelut
            Varhaiskasvatus ja esiopetus
            Asiakaspalvelu
            <a href="mailto:varhaiskasvatus.asiakaspalvelu@tampere.fi">varhaiskasvatus.asiakaspalvelu@tampere.fi</a>
            <a href="tel:+358408007260">040&nbsp;800&nbsp;7260</a>, ma-pe klo 9-12
            Postiosoite: PL 487, 33101 Tampere
            <a href="https://www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus.html">www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus.html</a>
            </pre>
            
            <p><strong>HUOM!</strong> Tämä on automaattinen viesti, joka kertoo lomakkeen tallennuksesta. Älä vastaa tähän viestiin reply-/vastaa-toiminnolla!</p>
        """.trimIndent()
    }

    override fun getDaycareApplicationReceivedEmailText(): String {
        return """
            Hyvä huoltaja,

            lapsenne varhaiskasvatushakemus on vastaanotettu.
            
            Varhaiskasvatushakemuksella on neljän (4) kuukauden hakuaika. Hakemuksen tehnyt huoltaja voi muokata hakemusta osoitteessa varhaiskasvatus.tampere.fi siihen saakka, kunnes se on otettu käsittelyyn.
            
            Saatte tiedon lapsenne varhaiskasvatuspaikasta noin kuukautta ennen palvelutarpeen alkamista tai hakemuksen lakisääteisen järjestelyajan päättymistä. Hakemuksen lakisääteinen järjestelyaika on neljä (4) kuukautta hakemuksen saapumisesta.
            
            Mikäli hakemuksenne on kiireellinen, ottakaa yhteyttä viipymättä Varhaiskasvatuksen asiakaspalveluun: varhaiskasvatus.asiakaspalvelu@tampere.fi, 040 800 7260 (ma-pe klo 9-12). Hakuaika kiireellisissä hakemuksissa on minimissään kaksi (2) viikkoa ja se alkaa siitä päivästä, kun olette olleet yhteydessä asiakaspalveluun.
            
            Kirjallinen päätös varhaiskasvatuspaikasta lähetetään huoltajalle Suomi.fi-viestit -palveluun. Mikäli huoltaja ei ole ottanut Suomi.fi-viestit -palvelua käyttöön, päätös lähetetään hänelle postitse.
            
            Myönnetyn varhaiskasvatuspaikan voi hyväksyä/hylätä sähköisesti osoitteessa varhaiskasvatus.tampere.fi. Mikäli haette paikkaa palvelusetelipäiväkodista, olkaa yhteydessä kyseiseen päiväkotiin viimeistään hakemuksen jättämisen jälkeen.
            
            Ilta- ja vuorohoitoa haettaessa, hakemuksen liitteeksi tulee toimittaa molempien samassa taloudessa asuvien huoltajien todistukset työnantajalta vuorotyöstä tai oppilaitoksesta iltaisin tapahtuvasta opiskelusta. Hakemusta käsitellään vuorohoidon hakemuksena vasta sen jälkeen, kun edellä mainitut todistukset on toimitettu. Tarvittavat liitteet voi lisätä suoraan sähköiselle hakemukselle tai toimittaa postitse osoitteeseen Tampereen kaupunki, Varhaiskasvatuksen asiakaspalvelu, PL 487, 33101 Tampere.
            
            Hakiessanne lapsellenne siirtoa toiseen varhaiskasvatusyksikköön, hakemuksella ei ole hakuaikaa. Siirrot pystytään toteuttamaan pääsääntöisesti elokuusta alkaen. Mikäli lapsen nykyinen hoitopaikka irtisanotaan, myös siirtohakemus poistuu.
            
            Lisätietoa hakemuksen käsittelystä ja varhaiskasvatuspaikan myöntämisestä saa varhaiskasvatuksen ja esiopetuksen asiakaspalvelusta:
            
            Tampereen kaupunki
            Sivistyspalvelut
            Varhaiskasvatus ja esiopetus
            Asiakaspalvelu
            varhaiskasvatus.asiakaspalvelu@tampere.fi
            040 800 7260, ma-pe klo 9-12
            Postiosoite: PL 487, 33101 Tampere
            www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus.html
            
            HUOM!  Tämä on automaattinen viesti, joka kertoo lomakkeen tallennuksesta. Älä vastaa tähän viestiin reply-/vastaa-toiminnolla!
        """.trimIndent()
    }

    override fun getPreschoolApplicationReceivedEmailHtml(withinApplicationPeriod: Boolean): String {
        throw Error("Preschool not in use!")
    }

    override fun getPreschoolApplicationReceivedEmailText(withinApplicationPeriod: Boolean): String {
        throw Error("Preschool not in use!")
    }

}