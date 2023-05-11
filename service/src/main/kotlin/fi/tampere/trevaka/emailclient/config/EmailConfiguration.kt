// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.emailclient.config

import fi.espoo.evaka.EvakaEnv
import fi.espoo.evaka.daycare.domain.Language
import fi.espoo.evaka.emailclient.EmailContent
import fi.espoo.evaka.emailclient.IEmailMessageProvider
import fi.espoo.evaka.invoicing.service.IncomeNotificationType
import fi.espoo.evaka.messaging.MessageThreadStub
import fi.espoo.evaka.messaging.MessageType
import fi.espoo.evaka.shared.ChildId
import fi.espoo.evaka.shared.domain.FiniteDateRange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Profile("trevaka")
@Configuration
class EmailConfiguration {

    @Bean
    fun emailMessageProvider(env: EvakaEnv): IEmailMessageProvider = EmailMessageProvider(env)
}

internal class EmailMessageProvider(private val env: EvakaEnv) : IEmailMessageProvider {
    private fun baseUrl(@Suppress("UNUSED_PARAMETER") language: Language) = env.frontendBaseUrlFi
    private val subjectForPendingDecisionEmail: String = "Toimenpiteitäsi odotetaan"
    private val subjectForClubApplicationReceivedEmail: String = "Hakemus vastaanotettu"
    private val subjectForDaycareApplicationReceivedEmail: String = "Hakemus vastaanotettu"
    private val subjectForPreschoolApplicationReceivedEmail: String = "Hakemus vastaanotettu"
    private val subjectForDecisionEmail: String = "Päätös eVakassa"

    override fun pendingDecisionNotification(language: Language): EmailContent = EmailContent(
        subject = subjectForPendingDecisionEmail,
        text = getPendingDecisionEmailText(),
        html = getPendingDecisionEmailHtml(),
    )

    override fun clubApplicationReceived(language: Language): EmailContent = EmailContent(
        subject = subjectForClubApplicationReceivedEmail,
        text = getClubApplicationReceivedEmailText(),
        html = getClubApplicationReceivedEmailHtml(),
    )

    override fun daycareApplicationReceived(language: Language): EmailContent = EmailContent(
        subject = subjectForDaycareApplicationReceivedEmail,
        text = getDaycareApplicationReceivedEmailText(),
        html = getDaycareApplicationReceivedEmailHtml(),
    )

    override fun preschoolApplicationReceived(language: Language, withinApplicationPeriod: Boolean): EmailContent = EmailContent(
        subject = subjectForPreschoolApplicationReceivedEmail,
        text = getPreschoolApplicationReceivedEmailText(),
        html = getPreschoolApplicationReceivedEmailHtml(),
    )

    override fun assistanceNeedDecisionNotification(language: Language): EmailContent = EmailContent(
        subject = subjectForDecisionEmail,
        text = getDecisionEmailText(),
        html = getDecisionEmailHtml(),
    )

    private fun getPendingDecisionEmailHtml(): String {
        return """
            <p>Olet saanut päätöksen/ilmoituksen Tampereen varhaiskasvatukselta, joka odottaa toimenpiteitäsi. Myönnetty varhaiskasvatus-/kerhopaikka tulee hyväksyä tai hylätä kahden viikon sisällä päätöksen saapumisesta.</p>
            
            <p>Hakemuksen tekijä voi hyväksyä tai hylätä varhaiskasvatus-/kerhopaikan kirjautumalla osoitteeseen <a href="https://varhaiskasvatus.tampere.fi">varhaiskasvatus.tampere.fi</a> tai ottamalla yhteyttä päätöksellä mainittuun päiväkodin johtajaan.</p>
            
            <p>Tähän viestiin ei voi vastata. Tarvittaessa ole yhteydessä Varhaiskasvatuksen asiakaspalveluun: <a href="mailto:varhaiskasvatus.asiakaspalvelu@tampere.fi">varhaiskasvatus.asiakaspalvelu@tampere.fi</a>, puh. <a href="tel:+358408007260">040 800 7260</a> (ma–to klo 9–11).</p>
        """.trimIndent()
    }

    private fun getPendingDecisionEmailText(): String {
        return """
            Olet saanut päätöksen/ilmoituksen Tampereen varhaiskasvatukselta, joka odottaa toimenpiteitäsi. Myönnetty varhaiskasvatus-/kerhopaikka tulee hyväksyä tai hylätä kahden viikon sisällä päätöksen saapumisesta.
            
            Hakemuksen tekijä voi hyväksyä tai hylätä varhaiskasvatus-/kerhopaikan kirjautumalla osoitteeseen https://varhaiskasvatus.tampere.fi tai ottamalla yhteyttä päätöksellä mainittuun päiväkodin johtajaan.
            
            Tähän viestiin ei voi vastata. Tarvittaessa ole yhteydessä Varhaiskasvatuksen asiakaspalveluun: varhaiskasvatus.asiakaspalvelu@tampere.fi, puh. 040 800 7260 (ma–to klo 9–11).
        """.trimIndent()
    }

    private fun getClubApplicationReceivedEmailHtml(): String {
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
            Puh. <a href="tel:+358408007260">040&nbsp;800&nbsp;7260</a> (ma–to klo 9–11)<br/>
            <a href="https://www.tampere.fi/varhaiskasvatus">www.tampere.fi/varhaiskasvatus</a>
            </p>
            
            <p>Tämä on automaattinen viesti, joka kertoo lomakkeen tallennuksesta. Viestiin ei voi vastata reply-/ vastaa-toiminnolla.</p>
        """.trimIndent()
    }

    private fun getClubApplicationReceivedEmailText(): String {
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
            Puh. 040 800 7260 (ma–to klo 9–11)
            www.tampere.fi/varhaiskasvatus
            
            Tämä on automaattinen viesti, joka kertoo lomakkeen tallennuksesta. Viestiin ei voi vastata reply-/ vastaa-toiminnolla.
        """.trimIndent()
    }

    private fun getDaycareApplicationReceivedEmailHtml(): String {
        return """
            <p>Hyvä huoltaja,</p>
            
            <p>lapsenne varhaiskasvatushakemus on vastaanotettu.</p>
            
            <p>Varhaiskasvatushakemuksella on neljän (4) kuukauden hakuaika. Hakemuksen tehnyt huoltaja voi muokata hakemusta Tampereen varhaiskasvatuksen verkkopalvelussa osoitteessa <a href="https://varhaiskasvatus.tampere.fi">varhaiskasvatus.tampere.fi</a> siihen saakka, kunnes se on otettu käsittelyyn.</p>
            
            <p>Saatte tiedon lapsenne varhaiskasvatuspaikasta noin kuukautta ennen palvelutarpeen alkamista tai hakemuksen lakisääteisen järjestelyajan päättymistä. Hakemuksen lakisääteinen järjestelyaika on neljä (4) kuukautta hakemuksen saapumisesta.</p> 
            
            <p>Mikäli hakemuksenne on kiireellinen, ottakaa yhteyttä viipymättä Varhaiskasvatuksen asiakaspalveluun: <a href="mailto:varhaiskasvatus.asiakaspalvelu@tampere.fi">varhaiskasvatus.asiakaspalvelu@tampere.fi</a>, puh. <a href="tel:0408007260">040&nbsp;800&nbsp;7260</a> (ma–to klo 9–11). Hakuaika kiireellisissä hakemuksissa on minimissään kaksi (2) viikkoa ja se alkaa siitä päivästä, kun olette olleet yhteydessä asiakaspalveluun.</p>
            
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
            Puh. <a href="tel:+358408007260">040&nbsp;800&nbsp;7260</a>, ma–to klo 9–11<br/>
            Postiosoite: PL 487, 33101 Tampere<br/>
            <a href="https://www.tampere.fi/varhaiskasvatus">www.tampere.fi/varhaiskasvatus</a>
            </p>
            
            <p>Tämä on automaattinen viesti, joka kertoo lomakkeen tallennuksesta. Viestiin ei voi vastata reply-/ vastaa-toiminnolla.</p>
        """.trimIndent()
    }

    private fun getDaycareApplicationReceivedEmailText(): String {
        return """
            Hyvä huoltaja,

            lapsenne varhaiskasvatushakemus on vastaanotettu.
            
            Varhaiskasvatushakemuksella on neljän (4) kuukauden hakuaika. Hakemuksen tehnyt huoltaja voi muokata hakemusta Tampereen varhaiskasvatuksen verkkopalvelussa osoitteessa varhaiskasvatus.tampere.fi siihen saakka, kunnes se on otettu käsittelyyn.
            
            Saatte tiedon lapsenne varhaiskasvatuspaikasta noin kuukautta ennen palvelutarpeen alkamista tai hakemuksen lakisääteisen järjestelyajan päättymistä. Hakemuksen lakisääteinen järjestelyaika on neljä (4) kuukautta hakemuksen saapumisesta.
            
            Mikäli hakemuksenne on kiireellinen, ottakaa yhteyttä viipymättä Varhaiskasvatuksen asiakaspalveluun: varhaiskasvatus.asiakaspalvelu@tampere.fi, puh. 040 800 7260 (ma–to klo 9–11). Hakuaika kiireellisissä hakemuksissa on minimissään kaksi (2) viikkoa ja se alkaa siitä päivästä, kun olette olleet yhteydessä asiakaspalveluun.
            
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
            Puh. 040 800 7260, ma–to klo 9–11
            Postiosoite: PL 487, 33101 Tampere
            www.tampere.fi/varhaiskasvatus
            
            Tämä on automaattinen viesti, joka kertoo lomakkeen tallennuksesta. Viestiin ei voi vastata reply-/ vastaa-toiminnolla.
        """.trimIndent()
    }

    private fun getPreschoolApplicationReceivedEmailHtml(): String {
        return """
            <p>Hyvä huoltaja,</p>

            <p>lapsenne esiopetukseen ilmoittautuminen on vastaanotettu.</p>

            <p>Hakemuksen tehnyt huoltaja voi muokata hakemusta Tampereen varhaiskasvatuksen verkkopalvelussa osoitteessa <a href="https://varhaiskasvatus.tampere.fi">varhaiskasvatus.tampere.fi</a> siihen saakka, kunnes se on otettu käsittelyyn.</p> 

            <p>Lisätietoa hakemuksen käsittelystä ja esiopetuspaikan myöntämisestä saa varhaiskasvatuksen ja esiopetuksen asiakaspalvelusta:</p>

            <p>
            Tampereen kaupunki<br/>
            Sivistyspalvelut<br/>
            Varhaiskasvatus ja esiopetus<br/>
            Asiakaspalvelu<br/>
            <a href="mailto:varhaiskasvatus.asiakaspalvelu@tampere.fi">varhaiskasvatus.asiakaspalvelu@tampere.fi</a><br/>
            Puh. <a href="tel:+358408007260">040&nbsp;800&nbsp;7260</a>, ma–to klo 9–11<br/>
            Postiosoite: PL 487, 33101 Tampere<br/>
            <a href="https://www.tampere.fi/varhaiskasvatus">www.tampere.fi/varhaiskasvatus</a>
            </p>

            <p>Tämä on automaattinen viesti, joka kertoo lomakkeen tallennuksesta. Viestiin ei voi vastata reply-/ vastaa-toiminnolla.</p>
        """.trimIndent()
    }

    private fun getPreschoolApplicationReceivedEmailText(): String {
        return """
            Hyvä huoltaja,

            lapsenne esiopetukseen ilmoittautuminen on vastaanotettu.

            Hakemuksen tehnyt huoltaja voi muokata hakemusta Tampereen varhaiskasvatuksen verkkopalvelussa osoitteessa varhaiskasvatus.tampere.fi siihen saakka, kunnes se on otettu käsittelyyn.

            Lisätietoa hakemuksen käsittelystä ja esiopetuspaikan myöntämisestä saa varhaiskasvatuksen ja esiopetuksen asiakaspalvelusta:

            Tampereen kaupunki
            Sivistyspalvelut
            Varhaiskasvatus ja esiopetus
            Asiakaspalvelu
            varhaiskasvatus.asiakaspalvelu@tampere.fi
            Puh. 040 800 7260, ma–to klo 9–11
            Postiosoite: PL 487, 33101 Tampere
            www.tampere.fi/varhaiskasvatus

            Tämä on automaattinen viesti, joka kertoo lomakkeen tallennuksesta. Viestiin ei voi vastata reply-/ vastaa-toiminnolla.
        """.trimIndent()
    }

    private fun getDecisionEmailHtml(): String = """
        <p>Hyvä(t) huoltaja(t),</p>
        <p>Lapsellenne on tehty päätös.</p>
        <p>Päätös on nähtävissä eVakassa osoitteessa <a href="https://varhaiskasvatus.tampere.fi/">https://varhaiskasvatus.tampere.fi/</a>.</p>
    """.trimIndent()

    private fun getDecisionEmailText(): String = """
        Hyvä(t) huoltaja(t),

        Lapsellenne on tehty päätös.

        Päätös on nähtävissä eVakassa osoitteessa https://varhaiskasvatus.tampere.fi/.
    """.trimIndent()

    override fun missingReservationsNotification(language: Language, checkedRange: FiniteDateRange): EmailContent {
        val start =
            checkedRange.start.format(
                DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale("fi", "FI")),
            )
        return EmailContent(
            subject =
            "Läsnäolovarauksia puuttuu / There are missing attendance reservations",
            text =
            """
Läsnäolovarauksia puuttuu $start alkavalta viikolta. Käythän merkitsemässä ne mahdollisimman pian.

----

There are missing attendance reservations for the week starting $start. Please mark them as soon as possible.
                """
                .trimIndent(),
            html =
            """
<p>Läsnäolovarauksia puuttuu $start alkavalta viikolta. Käythän merkitsemässä ne mahdollisimman pian.</p>
<hr>
<p>There are missing attendance reservations for the week starting $start. Please mark them as soon as possible.</p>
            """
                .trimIndent(),
        )
    }

    override fun messageNotification(language: Language, thread: MessageThreadStub): EmailContent {
        val messageUrl = "${baseUrl(language)}/messages/${thread.id}"
        val (typeFi, typeEn) =
            when (thread.type) {
                MessageType.MESSAGE ->
                    if (thread.urgent) {
                        Pair("kiireellinen viesti", "urgent message")
                    } else {
                        Pair("viesti", "message")
                    }
                MessageType.BULLETIN ->
                    if (thread.urgent) {
                        Pair("kiireellinen tiedote", "urgent bulletin")
                    } else {
                        Pair("tiedote", "bulletin")
                    }
            }
        return EmailContent(
            subject = "Uusi $typeFi eVakassa / New $typeEn in eVaka",
            text =
            """
                Sinulle on saapunut uusi $typeFi eVakaan. Lue viesti ${if (thread.urgent) "mahdollisimman pian " else ""}täältä: $messageUrl

                Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.

                -----

                You have received a new $typeEn in eVaka. Read the message ${if (thread.urgent) "as soon as possible " else ""}here: $messageUrl

                This is an automatic message from the eVaka system. Do not reply to this message.
        """
                .trimIndent(),
            html =
            """
                <p>Sinulle on saapunut uusi $typeFi eVakaan. Lue viesti ${if (thread.urgent) "mahdollisimman pian " else ""}täältä: <a href="$messageUrl">$messageUrl</a></p>
                <p>Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.</p>

                <hr>

                <p>You have received a new $typeEn in eVaka. Read the message ${if (thread.urgent) "as soon as possible " else ""}here: <a href="$messageUrl">$messageUrl</a></p>
                <p>This is an automatic message from the eVaka system. Do not reply to this message.</p>
        """
                .trimIndent(),
        )
    }

    override fun vasuNotification(language: Language, childId: ChildId): EmailContent {
        val documentsUrl = "${baseUrl(language)}/children/$childId"
        return EmailContent(
            subject = "Uusi dokumentti eVakassa / New document in eVaka",
            text =
            """
                Sinulle on saapunut uusi dokumentti eVakaan. Lue dokumentti täältä: $documentsUrl

                Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.

                -----

                You have received a new eVaka document. Read the document here: $documentsUrl

                This is an automatic message from the eVaka system. Do not reply to this message.
        """
                .trimIndent(),
            html =
            """
                <p>Sinulle on saapunut uusi dokumentti eVakaan. Lue dokumentti täältä: <a href="$documentsUrl">$documentsUrl</a></p>
                <p>Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.</p>

                <hr>

                <p>You have received a new eVaka document. Read the document here: <a href="$documentsUrl">$documentsUrl</a></p>
                <p>This is an automatic message from the eVaka system. Do not reply to this message.</p>
        """
                .trimIndent(),
        )
    }

    override fun pedagogicalDocumentNotification(language: Language): EmailContent {
        val documentsUrl = "${baseUrl(language)}/pedagogical-documents"
        return EmailContent(
            subject =
            "Uusi pedagoginen dokumentti eVakassa / New pedagogical document in eVaka",
            text =
            """
                Sinulle on saapunut uusi pedagoginen dokumentti eVakaan. Lue dokumentti täältä: $documentsUrl

                Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.

                -----

                You have received a new eVaka pedagogical document. Read the document here: $documentsUrl

                This is an automatic message from the eVaka system. Do not reply to this message.
        """
                .trimIndent(),
            html =
            """
                <p>Sinulle on saapunut uusi pedagoginen dokumentti eVakaan. Lue dokumentti täältä: <a href="$documentsUrl">$documentsUrl</a></p>
                <p>Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.</p>

                <hr>

                <p>You have received a new eVaka pedagogical document. Read the document here: <a href="$documentsUrl">$documentsUrl</a></p>
                <p>This is an automatic message from the eVaka system. Do not reply to this message.</p>
        """
                .trimIndent(),
        )
    }

    override fun outdatedIncomeNotification(
        notificationType: IncomeNotificationType,
        language: Language,
    ): EmailContent {
        return when (notificationType) {
            IncomeNotificationType.INITIAL_EMAIL -> outdatedIncomeNotificationInitial(language)
            IncomeNotificationType.REMINDER_EMAIL -> outdatedIncomeNotificationReminder(language)
            IncomeNotificationType.EXPIRED_EMAIL -> outdatedIncomeNotificationExpired()
        }
    }

    private fun outdatedIncomeNotificationInitial(language: Language): EmailContent {
        val documentsUrl = "${baseUrl(language)}/income"
        return EmailContent(
            subject =
            "Tulotietojen tarkastuskehotus / Request to review income information",
            text =
            """
                Hyvä asiakkaamme
                
                Varhaiskasvatuksen asiakasmaksun tai palvelusetelin omavastuuosuuden perusteena olevat tulotiedot tarkistetaan vuosittain.
                
                Pyydämme toimittamaan tuloselvityksen eVakassa 28 päivän kuluessa tästä ilmoituksesta. eVakassa voitte myös antaa suostumuksen korkeimpaan maksuluokkaan tai tulorekisterin käyttöön.
                
                Mikäli ette toimita uusia tulotietoja, asiakasmaksu määräytyy korkeimman maksuluokan mukaan. Uusi maksupäätös astuu voimaan sen kuukauden alusta, kun tulotiedot on toimitettu asiakasmaksuihin.
                
                Lisätietoja saatte tarvittaessa: https://www.tampere.fi/varhaiskasvatusasiakasmaksut
                
                Tulotiedot: $documentsUrl
                
                Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.
                
                -----
                
                Dear client
                
                The income information used for determining the early childhood education fee or the out-of-pocket cost of a service voucher is reviewed every year.
                
                We ask you to submit your income statement through eVaka within 28 days of this notification. Through eVaka, you can also give your consent to the highest fee or the use of the Incomes Register.
                
                If you do not provide your latest income information, your client fee will be determined based on the highest fee category. The new payment decision takes effect at the beginning of the month when the income information has been submitted to customer services.
                
                Inquiries: https://www.tampere.fi/en/early-childhood-education-and-pre-primary-education/client-fees-early-childhood-education-and-care-and-pre-primary
                
                Income information: $documentsUrl
                
                This is an automatic message from the eVaka system. Do not reply to this message.
        """
                .trimIndent(),
            html =
            """
                <p>Hyvä asiakkaamme</p>
                
                <p>Varhaiskasvatuksen asiakasmaksun tai palvelusetelin omavastuuosuuden perusteena olevat tulotiedot tarkistetaan vuosittain.</p>
                
                <p>Pyydämme toimittamaan tuloselvityksen eVakassa 28 päivän kuluessa tästä ilmoituksesta. eVakassa voitte myös antaa suostumuksen korkeimpaan maksuluokkaan tai tulorekisterin käyttöön.</p>
                
                <p>Mikäli ette toimita uusia tulotietoja, asiakasmaksu määräytyy korkeimman maksuluokan mukaan. Uusi maksupäätös astuu voimaan sen kuukauden alusta, kun tulotiedot on toimitettu asiakasmaksuihin.</p>
                
                <p>Lisätietoja saatte tarvittaessa: <a href="https://www.tampere.fi/varhaiskasvatusasiakasmaksut">https://www.tampere.fi/varhaiskasvatusasiakasmaksut</a></p>
                
                <p>Tulotiedot: <a href="$documentsUrl">$documentsUrl</a></p>
                
                <p>Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.</p>
                
                <hr>
                
                <p>Dear client</p>
                
                <p>The income information used for determining the early childhood education fee or the out-of-pocket cost of a service voucher is reviewed every year.</p>
                
                <p>We ask you to submit your income statement through eVaka within 28 days of this notification. Through eVaka, you can also give your consent to the highest fee or the use of the Incomes Register.</p>
                
                <p>If you do not provide your latest income information, your client fee will be determined based on the highest fee category. The new payment decision takes effect at the beginning of the month when the income information has been submitted to customer services.</p>
                
                <p>Inquiries: <a href="https://www.tampere.fi/en/early-childhood-education-and-pre-primary-education/client-fees-early-childhood-education-and-care-and-pre-primary">https://www.tampere.fi/en/early-childhood-education-and-pre-primary-education/client-fees-early-childhood-education-and-care-and-pre-primary</a></p>
                
                <p>Income information: <a href="$documentsUrl">$documentsUrl</a></p>
                
                <p>This is an automatic message from the eVaka system. Do not reply to this message.</p>
        """
                .trimIndent(),
        )
    }

    private fun outdatedIncomeNotificationReminder(language: Language): EmailContent {
        val documentsUrl = "${baseUrl(language)}/income"
        return EmailContent(
            subject =
            "Tulotietojen tarkastuskehotus / Request to review income information",
            text =
            """
                Hyvä asiakkaamme
                
                Ette ole vielä toimittaneet uusia tulotietoja. Varhaiskasvatuksen asiakasmaksun tai palvelusetelin omavastuuosuuden perusteena olevat tulotiedot tarkistetaan vuosittain.
                
                Pyydämme toimittamaan tuloselvityksen eVakassa 14 päivän kuluessa tästä ilmoituksesta. eVakassa voitte myös antaa suostumuksen korkeimpaan maksuluokkaan tai tulorekisterin käyttöön.
                
                Mikäli ette toimita uusia tulotietoja, asiakasmaksu määräytyy korkeimman maksuluokan mukaan. Uusi maksupäätös astuu voimaan sen kuukauden alusta, kun tulotiedot on toimitettu asiakasmaksuihin.
                
                Lisätietoja saatte tarvittaessa: https://www.tampere.fi/varhaiskasvatusasiakasmaksut
                
                Tulotiedot: $documentsUrl
                
                Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.
                
                -----
                
                Dear client
                
                You have not yet submitted your latest income information. The income information used for determining the early childhood education fee or the out-of-pocket cost of a service voucher is reviewed every year.
                
                We ask you to submit your income statement through eVaka within 14 days of this notification. Through eVaka, you can also give your consent to the highest fee or the use of the Incomes Register.
                
                If you do not provide your latest income information, your client fee will be determined based on the highest fee category. The new payment decision takes effect at the beginning of the month when the income information has been submitted to customer services.
                
                Inquiries: https://www.tampere.fi/en/early-childhood-education-and-pre-primary-education/client-fees-early-childhood-education-and-care-and-pre-primary
                
                Income information: $documentsUrl
                
                This is an automatic message from the eVaka system. Do not reply to this message.
        """
                .trimIndent(),
            html =
            """
                <p>Hyvä asiakkaamme</p>
                
                <p>Ette ole vielä toimittaneet uusia tulotietoja. Varhaiskasvatuksen asiakasmaksun tai palvelusetelin omavastuuosuuden perusteena olevat tulotiedot tarkistetaan vuosittain.</p>
                
                <p>Pyydämme toimittamaan tuloselvityksen eVakassa 14 päivän kuluessa tästä ilmoituksesta. eVakassa voitte myös antaa suostumuksen korkeimpaan maksuluokkaan tai tulorekisterin käyttöön.</p>
                
                <p>Mikäli ette toimita uusia tulotietoja, asiakasmaksu määräytyy korkeimman maksuluokan mukaan. Uusi maksupäätös astuu voimaan sen kuukauden alusta, kun tulotiedot on toimitettu asiakasmaksuihin.</p>
                
                <p>Lisätietoja saatte tarvittaessa: <a href="https://www.tampere.fi/varhaiskasvatusasiakasmaksut">https://www.tampere.fi/varhaiskasvatusasiakasmaksut</a></p>
                
                <p>Tulotiedot: <a href="$documentsUrl">$documentsUrl</a></p>
                
                <p>Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.</p>
                
                <hr>
                
                <p>Dear client</p>
                
                <p>You have not yet submitted your latest income information. The income information used for determining the early childhood education fee or the out-of-pocket cost of a service voucher is reviewed every year.</p>
                
                <p>We ask you to submit your income statement through eVaka within 14 days of this notification. Through eVaka, you can also give your consent to the highest fee or the use of the Incomes Register.</p>
                
                <p>If you do not provide your latest income information, your client fee will be determined based on the highest fee category. The new payment decision takes effect at the beginning of the month when the income information has been submitted to customer services.</p>
                
                <p>Inquiries: <a href="https://www.tampere.fi/en/early-childhood-education-and-pre-primary-education/client-fees-early-childhood-education-and-care-and-pre-primary">https://www.tampere.fi/en/early-childhood-education-and-pre-primary-education/client-fees-early-childhood-education-and-care-and-pre-primary</a></p>
                
                <p>Income information: <a href="$documentsUrl">$documentsUrl</a></p>
                
                <p>This is an automatic message from the eVaka system. Do not reply to this message.</p>
        """
                .trimIndent(),
        )
    }

    private fun outdatedIncomeNotificationExpired(): EmailContent {
        return EmailContent(
            subject =
            "Tulotietojen tarkastuskehotus / Request to review income information",
            text =
            """
                Hyvä asiakkaamme
                
                Seuraava asiakasmaksunne määräytyy korkeimman maksuluokan mukaan, sillä ette ole toimittaneet uusia tulotietoja määräaikaan mennessä.
                
                Lisätietoja saatte tarvittaessa: https://www.tampere.fi/varhaiskasvatusasiakasmaksut
                
                Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.
                
                -----
                
                Dear client
                
                Your next client fee will be determined based on the highest fee category as you did not provide your latest income information by the deadline.
                
                Inquiries: https://www.tampere.fi/en/early-childhood-education-and-pre-primary-education/client-fees-early-childhood-education-and-care-and-pre-primary
                
                This is an automatic message from the eVaka system. Do not reply to this message.
        """
                .trimIndent(),
            html =
            """
                <p>Hyvä asiakkaamme</p>
                
                <p>Seuraava asiakasmaksunne määräytyy korkeimman maksuluokan mukaan, sillä ette ole toimittaneet uusia tulotietoja määräaikaan mennessä.</p>
                
                <p>Lisätietoja saatte tarvittaessa: <a href="https://www.tampere.fi/varhaiskasvatusasiakasmaksut">https://www.tampere.fi/varhaiskasvatusasiakasmaksut</a></p>
                
                <p>Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.</p>
                
                <hr>
                
                <p>Dear client</p>
                
                <p>Your next client fee will be determined based on the highest fee category as you did not provide your latest income information by the deadline.</p>
                
                <p>Inquiries: <a href="https://www.tampere.fi/en/early-childhood-education-and-pre-primary-education/client-fees-early-childhood-education-and-care-and-pre-primary">https://www.tampere.fi/en/early-childhood-education-and-pre-primary-education/client-fees-early-childhood-education-and-care-and-pre-primary</a></p>
                
                <p>This is an automatic message from the eVaka system. Do not reply to this message.</p>
               """
                .trimIndent(),
        )
    }
}
