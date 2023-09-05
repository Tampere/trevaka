// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.emailclient.config

import fi.espoo.evaka.EvakaEnv
import fi.espoo.evaka.daycare.domain.Language
import fi.espoo.evaka.emailclient.CalendarEventNotificationData
import fi.espoo.evaka.emailclient.EmailContent
import fi.espoo.evaka.emailclient.IEmailMessageProvider
import fi.espoo.evaka.invoicing.service.IncomeNotificationType
import fi.espoo.evaka.messaging.MessageThreadStub
import fi.espoo.evaka.messaging.MessageType
import fi.espoo.evaka.shared.ChildId
import fi.espoo.evaka.shared.MessageThreadId
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
    private fun link(language: Language, path: String): String {
        val baseUrl =
            when (language) {
                Language.sv -> env.frontendBaseUrlSv
                else -> env.frontendBaseUrlFi
            }
        val url = "$baseUrl$path"
        return """<a href="$url">$url</a>"""
    }
    private fun frontPageLink(language: Language) = link(language, "")
    private fun calendarLink(language: Language) = link(language, "/calendar")
    private fun messageLink(language: Language, threadId: MessageThreadId) =
        link(language, "/messages/$threadId")
    private fun childLink(language: Language, childId: ChildId) =
        link(language, "/children/$childId")
    private fun incomeLink(language: Language) = link(language, "/income")
    private val subjectForPendingDecisionEmail: String = "Toimenpiteitäsi odotetaan"
    private val subjectForClubApplicationReceivedEmail: String = "Hakemus vastaanotettu"
    private val subjectForDaycareApplicationReceivedEmail: String = "Hakemus vastaanotettu"
    private val subjectForPreschoolApplicationReceivedEmail: String = "Hakemus vastaanotettu"
    private val subjectForDecisionEmail: String = "Päätös eVakassa"

    override fun pendingDecisionNotification(language: Language): EmailContent = EmailContent.fromHtml(
        subject = subjectForPendingDecisionEmail,
        html = getPendingDecisionEmailHtml(),
    )

    override fun clubApplicationReceived(language: Language): EmailContent = EmailContent.fromHtml(
        subject = subjectForClubApplicationReceivedEmail,
        html = getClubApplicationReceivedEmailHtml(),
    )

    override fun daycareApplicationReceived(language: Language): EmailContent = EmailContent.fromHtml(
        subject = subjectForDaycareApplicationReceivedEmail,
        html = getDaycareApplicationReceivedEmailHtml(),
    )

    override fun preschoolApplicationReceived(language: Language, withinApplicationPeriod: Boolean): EmailContent = EmailContent.fromHtml(
        subject = subjectForPreschoolApplicationReceivedEmail,
        html = getPreschoolApplicationReceivedEmailHtml(),
    )

    override fun assistanceNeedDecisionNotification(language: Language): EmailContent = EmailContent.fromHtml(
        subject = subjectForDecisionEmail,
        html = getDecisionEmailHtml(),
    )

    override fun assistanceNeedPreschoolDecisionNotification(language: Language): EmailContent = EmailContent.fromHtml(
        subject = subjectForDecisionEmail,
        html = getDecisionEmailHtml(),
    )

    private fun getPendingDecisionEmailHtml(): String {
        return """
            <p>Olet saanut päätöksen/ilmoituksen Tampereen varhaiskasvatukselta, joka odottaa toimenpiteitäsi. Myönnetty varhaiskasvatus-/kerhopaikka tulee hyväksyä tai hylätä kahden viikon sisällä päätöksen saapumisesta.</p>
            
            <p>Hakemuksen tekijä voi hyväksyä tai hylätä varhaiskasvatus-/kerhopaikan kirjautumalla osoitteeseen ${frontPageLink(Language.fi)} tai ottamalla yhteyttä päätöksellä mainittuun päiväkodin johtajaan.</p>
            
            <p>Tähän viestiin ei voi vastata. Tarvittaessa ole yhteydessä Varhaiskasvatuksen asiakaspalveluun: <a href="https://www.tampere.fi/varhaiskasvatus-ja-esiopetus/varhaiskasvatuksen-ja-esiopetuksen-yhteystiedot">https://www.tampere.fi/varhaiskasvatus-ja-esiopetus/varhaiskasvatuksen-ja-esiopetuksen-yhteystiedot</a></p>
        """.trimIndent()
    }

    private fun getClubApplicationReceivedEmailHtml(): String {
        return """
            <p>Hyvä huoltaja,</p>
            
            <p>lapsenne kerhohakemus on vastaanotettu.</p>
            
            <p>Hakemuksen tehnyt huoltaja voi muokata hakemusta Tampereen varhaiskasvatuksen verkkopalvelussa osoitteessa ${frontPageLink(Language.fi)} siihen saakka, kunnes se on otettu käsittelyyn asiakaspalvelussa.</p>
            
            <p>Kirjallinen ilmoitus myönnetystä kerhopaikasta lähetetään huoltajalle Suomi.fi-viestit -palveluun. Mikäli huoltaja ei ole ottanut Suomi.fi-viestit -palvelua käyttöön, ilmoitus lähetetään hänelle postitse.</p> 
            
            <p>Myönnetyn kerhopaikan voi hyväksyä / hylätä sähköisesti Tampereen varhaiskasvatuksen verkkopalvelussa osoitteessa ${frontPageLink(Language.fi)}. Kerhohakemus kohdistuu yhdelle kerhon toimintakaudelle. Kauden päättyessä hakemus poistetaan järjestelmästä.</p>
            
            <p>Lisätietoa hakemuksen käsittelystä ja kerhopaikan myöntämisestä saa varhaiskasvatuksen ja esiopetuksen asiakaspalvelusta:</p>
            
            <p>
            Tampereen kaupunki<br/>
            Sivistyspalvelut<br/>
            Varhaiskasvatus ja esiopetus<br/>
            Asiakaspalvelu<br/>
            <a href="https://www.tampere.fi/varhaiskasvatus-ja-esiopetus/varhaiskasvatuksen-ja-esiopetuksen-yhteystiedot">https://www.tampere.fi/varhaiskasvatus-ja-esiopetus/varhaiskasvatuksen-ja-esiopetuksen-yhteystiedot</a>
            </p>
            
            <p>Tämä on automaattinen viesti, joka kertoo lomakkeen tallennuksesta. Viestiin ei voi vastata reply-/ vastaa-toiminnolla.</p>
        """.trimIndent()
    }

    private fun getDaycareApplicationReceivedEmailHtml(): String {
        return """
            <p>Hyvä huoltaja,</p>
            
            <p>lapsenne varhaiskasvatushakemus on vastaanotettu.</p>
            
            <p>Varhaiskasvatushakemuksella on neljän (4) kuukauden hakuaika. Hakemuksen tehnyt huoltaja voi muokata hakemusta Tampereen varhaiskasvatuksen verkkopalvelussa osoitteessa ${frontPageLink(Language.fi)} siihen saakka, kunnes se on otettu käsittelyyn.</p>
            
            <p>Saatte tiedon lapsenne varhaiskasvatuspaikasta noin kuukautta ennen palvelutarpeen alkamista tai hakemuksen lakisääteisen järjestelyajan päättymistä. Hakemuksen lakisääteinen järjestelyaika on neljä (4) kuukautta hakemuksen saapumisesta.</p> 
            
            <p>Mikäli hakemuksenne on kiireellinen, ottakaa yhteyttä viipymättä Varhaiskasvatuksen asiakaspalveluun: <a href="https://www.tampere.fi/varhaiskasvatus-ja-esiopetus/varhaiskasvatuksen-ja-esiopetuksen-yhteystiedot">https://www.tampere.fi/varhaiskasvatus-ja-esiopetus/varhaiskasvatuksen-ja-esiopetuksen-yhteystiedot</a>. Hakuaika kiireellisissä hakemuksissa on minimissään kaksi (2) viikkoa ja se alkaa siitä päivästä, kun olette olleet yhteydessä asiakaspalveluun.</p>
            
            <p>Kirjallinen päätös varhaiskasvatuspaikasta lähetetään huoltajalle Suomi.fi-viestit -palveluun. Mikäli huoltaja ei ole ottanut Suomi.fi-viestit -palvelua käyttöön, päätös lähetetään hänelle postitse.</p>
            
            <p>Myönnetyn varhaiskasvatuspaikan voi hyväksyä / hylätä sähköisesti Tampereen varhaiskasvatuksen verkkopalvelussa osoitteessa ${frontPageLink(Language.fi)}. Mikäli haette paikkaa palvelusetelipäiväkodista, olkaa yhteydessä kyseiseen päiväkotiin viimeistään hakemuksen jättämisen jälkeen.</p>
            
            <p>Ilta- ja vuorohoitoa haettaessa, hakemuksen liitteeksi tulee toimittaa molempien samassa taloudessa asuvien huoltajien todistukset työnantajalta vuorotyöstä tai oppilaitoksesta iltaisin tapahtuvasta opiskelusta. Hakemusta käsitellään vuorohoidon hakemuksena vasta sen jälkeen, kun edellä mainitut todistukset on toimitettu. Tarvittavat liitteet voi lisätä suoraan sähköiselle hakemukselle tai toimittaa postitse osoitteeseen Tampereen kaupunki, Varhaiskasvatuksen asiakaspalvelu, PL 487, 33101 Tampere.</p> 
            
            <p>Hakiessanne lapsellenne siirtoa toiseen varhaiskasvatusyksikköön, hakemuksella ei ole hakuaikaa. Siirrot pystytään toteuttamaan pääsääntöisesti elokuusta alkaen. Mikäli lapsen nykyinen hoitopaikka irtisanotaan, myös siirtohakemus poistuu.</p> 
            
            <p>Lisätietoa hakemuksen käsittelystä ja varhaiskasvatuspaikan myöntämisestä saa varhaiskasvatuksen ja esiopetuksen asiakaspalvelusta:</p>
            
            <p>
            Tampereen kaupunki<br/>
            Sivistyspalvelut<br/>
            Varhaiskasvatus ja esiopetus<br/>
            Asiakaspalvelu<br/>
            <a href="https://www.tampere.fi/varhaiskasvatus-ja-esiopetus/varhaiskasvatuksen-ja-esiopetuksen-yhteystiedot">https://www.tampere.fi/varhaiskasvatus-ja-esiopetus/varhaiskasvatuksen-ja-esiopetuksen-yhteystiedot</a>
            </p>
            
            <p>Tämä on automaattinen viesti, joka kertoo lomakkeen tallennuksesta. Viestiin ei voi vastata reply-/ vastaa-toiminnolla.</p>
        """.trimIndent()
    }

    private fun getPreschoolApplicationReceivedEmailHtml(): String {
        return """
            <p>Hyvä huoltaja,</p>

            <p>lapsenne esiopetukseen ilmoittautuminen on vastaanotettu.</p>

            <p>Hakemuksen tehnyt huoltaja voi muokata hakemusta Tampereen varhaiskasvatuksen verkkopalvelussa osoitteessa ${frontPageLink(Language.fi)} siihen saakka, kunnes se on otettu käsittelyyn.</p> 

            <p>Lisätietoa hakemuksen käsittelystä ja esiopetuspaikan myöntämisestä saa varhaiskasvatuksen ja esiopetuksen asiakaspalvelusta:</p>

            <p>
            Tampereen kaupunki<br/>
            Sivistyspalvelut<br/>
            Varhaiskasvatus ja esiopetus<br/>
            Asiakaspalvelu<br/>
            <a href="https://www.tampere.fi/varhaiskasvatus-ja-esiopetus/varhaiskasvatuksen-ja-esiopetuksen-yhteystiedot">https://www.tampere.fi/varhaiskasvatus-ja-esiopetus/varhaiskasvatuksen-ja-esiopetuksen-yhteystiedot</a>
            </p>

            <p>Tämä on automaattinen viesti, joka kertoo lomakkeen tallennuksesta. Viestiin ei voi vastata reply-/ vastaa-toiminnolla.</p>
        """.trimIndent()
    }

    private fun getDecisionEmailHtml(): String = """
        <p>Hyvä(t) huoltaja(t),</p>
        <p>Lapsellenne on tehty päätös.</p>
        <p>Päätös on nähtävissä eVakassa osoitteessa ${frontPageLink(Language.fi)}.</p>
    """.trimIndent()

    override fun missingReservationsNotification(language: Language, checkedRange: FiniteDateRange): EmailContent {
        val start =
            checkedRange.start.format(
                DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale("fi", "FI")),
            )
        return EmailContent.fromHtml(
            subject =
            "Läsnäolovarauksia puuttuu / There are missing attendance reservations",
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
        return EmailContent.fromHtml(
            subject = "Uusi $typeFi eVakassa / New $typeEn in eVaka",
            html =
            """
                <p>Sinulle on saapunut uusi $typeFi eVakaan. Lue viesti ${if (thread.urgent) "mahdollisimman pian " else ""}täältä: ${messageLink(Language.fi, thread.id)}</p>
                <p>Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.</p>

                <hr>

                <p>You have received a new $typeEn in eVaka. Read the message ${if (thread.urgent) "as soon as possible " else ""}here: ${messageLink(Language.en, thread.id)}</p>
                <p>This is an automatic message from the eVaka system. Do not reply to this message.</p>
        """
                .trimIndent(),
        )
    }

    override fun vasuNotification(language: Language, childId: ChildId): EmailContent {
        return EmailContent.fromHtml(
            subject = "Uusi dokumentti eVakassa / New document in eVaka",
            html =
            """
                <p>Sinulle on saapunut uusi dokumentti eVakaan. Lue dokumentti täältä: ${childLink(Language.fi, childId)}</p>
                <p>Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.</p>

                <hr>

                <p>You have received a new eVaka document. Read the document here: ${childLink(Language.en, childId)}</p>
                <p>This is an automatic message from the eVaka system. Do not reply to this message.</p>
        """
                .trimIndent(),
        )
    }

    override fun pedagogicalDocumentNotification(language: Language, childId: ChildId): EmailContent {
        return EmailContent.fromHtml(
            subject =
            "Uusi pedagoginen dokumentti eVakassa / New pedagogical document in eVaka",
            html =
            """
                <p>Sinulle on saapunut uusi pedagoginen dokumentti eVakaan. Lue dokumentti täältä: ${childLink(Language.fi, childId)}</p>
                <p>Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.</p>

                <hr>

                <p>You have received a new eVaka pedagogical document. Read the document here: ${childLink(Language.en, childId)}</p>
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
            IncomeNotificationType.INITIAL_EMAIL -> outdatedIncomeNotificationInitial()
            IncomeNotificationType.REMINDER_EMAIL -> outdatedIncomeNotificationReminder()
            IncomeNotificationType.EXPIRED_EMAIL -> outdatedIncomeNotificationExpired()
        }
    }

    private fun outdatedIncomeNotificationInitial(): EmailContent {
        return EmailContent.fromHtml(
            subject =
            "Tulotietojen tarkastuskehotus / Request to review income information",
            html =
            """
                <p>Hyvä asiakkaamme</p>
                
                <p>Varhaiskasvatuksen asiakasmaksun tai palvelusetelin omavastuuosuuden perusteena olevat tulotiedot tarkistetaan vuosittain.</p>
                
                <p>Pyydämme toimittamaan tuloselvityksen eVakassa 28 päivän kuluessa tästä ilmoituksesta. eVakassa voitte myös antaa suostumuksen korkeimpaan maksuluokkaan tai tulorekisterin käyttöön.</p>
                
                <p>Mikäli ette toimita uusia tulotietoja, asiakasmaksu määräytyy korkeimman maksuluokan mukaan. Uusi maksupäätös astuu voimaan sen kuukauden alusta, kun tulotiedot on toimitettu asiakasmaksuihin.</p>
                
                <p>Lisätietoja saatte tarvittaessa: <a href="https://www.tampere.fi/varhaiskasvatusasiakasmaksut">https://www.tampere.fi/varhaiskasvatusasiakasmaksut</a></p>
                
                <p>Tulotiedot: ${incomeLink(Language.fi)}</p>
                
                <p>Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.</p>
                
                <hr>
                
                <p>Dear client</p>
                
                <p>The income information used for determining the early childhood education fee or the out-of-pocket cost of a service voucher is reviewed every year.</p>
                
                <p>We ask you to submit your income statement through eVaka within 28 days of this notification. Through eVaka, you can also give your consent to the highest fee or the use of the Incomes Register.</p>
                
                <p>If you do not provide your latest income information, your client fee will be determined based on the highest fee category. The new payment decision takes effect at the beginning of the month when the income information has been submitted to customer services.</p>
                
                <p>Inquiries: <a href="https://www.tampere.fi/en/early-childhood-education-and-pre-primary-education/client-fees-early-childhood-education-and-care-and-pre-primary">https://www.tampere.fi/en/early-childhood-education-and-pre-primary-education/client-fees-early-childhood-education-and-care-and-pre-primary</a></p>
                
                <p>Income information: ${incomeLink(Language.en)}</p>
                
                <p>This is an automatic message from the eVaka system. Do not reply to this message.</p>
        """
                .trimIndent(),
        )
    }

    private fun outdatedIncomeNotificationReminder(): EmailContent {
        return EmailContent.fromHtml(
            subject =
            "Tulotietojen tarkastuskehotus / Request to review income information",
            html =
            """
                <p>Hyvä asiakkaamme</p>
                
                <p>Ette ole vielä toimittaneet uusia tulotietoja. Varhaiskasvatuksen asiakasmaksun tai palvelusetelin omavastuuosuuden perusteena olevat tulotiedot tarkistetaan vuosittain.</p>
                
                <p>Pyydämme toimittamaan tuloselvityksen eVakassa 14 päivän kuluessa tästä ilmoituksesta. eVakassa voitte myös antaa suostumuksen korkeimpaan maksuluokkaan tai tulorekisterin käyttöön.</p>
                
                <p>Mikäli ette toimita uusia tulotietoja, asiakasmaksu määräytyy korkeimman maksuluokan mukaan. Uusi maksupäätös astuu voimaan sen kuukauden alusta, kun tulotiedot on toimitettu asiakasmaksuihin.</p>
                
                <p>Lisätietoja saatte tarvittaessa: <a href="https://www.tampere.fi/varhaiskasvatusasiakasmaksut">https://www.tampere.fi/varhaiskasvatusasiakasmaksut</a></p>
                
                <p>Tulotiedot: ${incomeLink(Language.fi)}</p>
                
                <p>Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.</p>
                
                <hr>
                
                <p>Dear client</p>
                
                <p>You have not yet submitted your latest income information. The income information used for determining the early childhood education fee or the out-of-pocket cost of a service voucher is reviewed every year.</p>
                
                <p>We ask you to submit your income statement through eVaka within 14 days of this notification. Through eVaka, you can also give your consent to the highest fee or the use of the Incomes Register.</p>
                
                <p>If you do not provide your latest income information, your client fee will be determined based on the highest fee category. The new payment decision takes effect at the beginning of the month when the income information has been submitted to customer services.</p>
                
                <p>Inquiries: <a href="https://www.tampere.fi/en/early-childhood-education-and-pre-primary-education/client-fees-early-childhood-education-and-care-and-pre-primary">https://www.tampere.fi/en/early-childhood-education-and-pre-primary-education/client-fees-early-childhood-education-and-care-and-pre-primary</a></p>
                
                <p>Income information: ${incomeLink(Language.en)}</p>
                
                <p>This is an automatic message from the eVaka system. Do not reply to this message.</p>
        """
                .trimIndent(),
        )
    }

    private fun outdatedIncomeNotificationExpired(): EmailContent {
        return EmailContent.fromHtml(
            subject =
            "Tulotietojen tarkastuskehotus / Request to review income information",
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

    override fun calendarEventNotification(
        language: Language,
        events: List<CalendarEventNotificationData>,
    ): EmailContent {
        val format =
            DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale("fi", "FI"))
        val eventsHtml =
            "<ul>" +
                events.joinToString("\n") { event ->
                    var period = event.period.start.format(format)
                    if (event.period.end != event.period.start) {
                        period += "-${event.period.end.format(format)}"
                    }
                    "<li>$period: ${event.title}</li>"
                } +
                "</ul>"
        return EmailContent.fromHtml(
            subject =
            "Uusia kalenteritapahtumia eVakassa / New calendar events in eVaka",
            html =
            """
<p>eVakaan on lisätty uusia kalenteritapahtumia:</p>
$eventsHtml
<p>Katso lisää kalenterissa: ${calendarLink(Language.fi)}</p>
<hr>
<p>New calendar events in eVaka:</p>
$eventsHtml
<p>See more in the calendar: ${calendarLink(Language.en)}</p>
""",
        )
    }
}
