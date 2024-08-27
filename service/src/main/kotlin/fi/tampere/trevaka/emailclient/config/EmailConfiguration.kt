// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.emailclient.config

import fi.espoo.evaka.daycare.domain.Language
import fi.espoo.evaka.emailclient.CalendarEventNotificationData
import fi.espoo.evaka.emailclient.DiscussionSurveyCreationNotificationData
import fi.espoo.evaka.emailclient.DiscussionSurveyReservationNotificationData
import fi.espoo.evaka.emailclient.DiscussionTimeReminderData
import fi.espoo.evaka.emailclient.EmailContent
import fi.espoo.evaka.emailclient.IEmailMessageProvider
import fi.espoo.evaka.invoicing.domain.FinanceDecisionType
import fi.espoo.evaka.invoicing.service.IncomeNotificationType
import fi.espoo.evaka.messaging.MessageThreadStub
import fi.espoo.evaka.messaging.MessageType
import fi.espoo.evaka.shared.ChildId
import fi.espoo.evaka.shared.domain.FiniteDateRange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Configuration
class EmailConfiguration {

    @Bean
    fun emailMessageProvider(): IEmailMessageProvider = EmailMessageProvider()
}

internal class EmailMessageProvider : IEmailMessageProvider {

    private val unsubscribeFi =
        """<p><small>Jos et halua enää saada tämänkaltaisia viestejä, voit muuttaa asetuksia eVakan Omat tiedot -sivulla</small></p>"""
    private val unsubscribeEn =
        """<p><small>If you no longer want to receive messages like this, you can change your settings on eVaka's Personal information page</small></p>"""
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
            
            <p>Hakemuksen tekijä voi hyväksyä tai hylätä varhaiskasvatus-/kerhopaikan kirjautumalla Tampereen varhaiskasvatuksen verkkopalveluun eVakaan tai ottamalla yhteyttä päätöksellä mainittuun päiväkodin johtajaan.</p>
            
            <p>Tähän viestiin ei voi vastata. Tarvittaessa ole yhteydessä Varhaiskasvatuksen asiakaspalveluun.</p>
            
            $unsubscribeFi
        """.trimIndent()
    }

    private fun getClubApplicationReceivedEmailHtml(): String {
        return """
            <p>Hyvä huoltaja,</p>
            
            <p>lapsenne kerhohakemus on vastaanotettu.</p>
            
            <p>Hakemuksen tehnyt huoltaja voi muokata hakemusta Tampereen varhaiskasvatuksen verkkopalvelussa eVakassa siihen saakka, kunnes se on otettu käsittelyyn asiakaspalvelussa.</p>
            
            <p>Kirjallinen ilmoitus myönnetystä kerhopaikasta lähetetään huoltajalle Suomi.fi-viestit -palveluun. Mikäli huoltaja ei ole ottanut Suomi.fi-viestit -palvelua käyttöön, ilmoitus lähetetään hänelle postitse.</p> 
            
            <p>Myönnetyn kerhopaikan voi hyväksyä / hylätä sähköisesti eVakassa. Kerhohakemus kohdistuu yhdelle kerhon toimintakaudelle. Kauden päättyessä hakemus poistetaan järjestelmästä.</p>
            
            <p>Lisätietoa hakemuksen käsittelystä ja kerhopaikan myöntämisestä saa varhaiskasvatuksen ja esiopetuksen asiakaspalvelusta:</p>
            
            <p>
            Tampereen kaupunki<br/>
            Sivistyspalvelut<br/>
            Varhaiskasvatus ja esiopetus<br/>
            Asiakaspalvelu
            </p>
            
            <p>Tämä on automaattinen viesti, joka kertoo lomakkeen tallennuksesta. Viestiin ei voi vastata reply-/ vastaa-toiminnolla.</p>
        """.trimIndent()
    }

    private fun getDaycareApplicationReceivedEmailHtml(): String {
        return """
            <p>Hyvä huoltaja,</p>
            
            <p>lapsenne varhaiskasvatushakemus on vastaanotettu.</p>
            
            <p>Varhaiskasvatushakemuksella on neljän (4) kuukauden hakuaika. Hakemuksen tehnyt huoltaja voi muokata hakemusta Tampereen varhaiskasvatuksen verkkopalvelussa eVakassa siihen saakka, kunnes se on otettu käsittelyyn.</p>
            
            <p>Saatte tiedon lapsenne varhaiskasvatuspaikasta noin kuukautta ennen palvelutarpeen alkamista tai hakemuksen lakisääteisen järjestelyajan päättymistä. Hakemuksen lakisääteinen järjestelyaika on neljä (4) kuukautta hakemuksen saapumisesta.</p> 
            
            <p>Mikäli hakemuksenne on kiireellinen, ottakaa yhteyttä viipymättä Varhaiskasvatuksen asiakaspalveluun. Hakuaika kiireellisissä hakemuksissa on minimissään kaksi (2) viikkoa ja se alkaa siitä päivästä, kun olette olleet yhteydessä asiakaspalveluun.</p>
            
            <p>Kirjallinen päätös varhaiskasvatuspaikasta lähetetään huoltajalle Suomi.fi-viestit -palveluun. Mikäli huoltaja ei ole ottanut Suomi.fi-viestit -palvelua käyttöön, päätös lähetetään hänelle postitse.</p>
            
            <p>Myönnetyn varhaiskasvatuspaikan voi hyväksyä / hylätä sähköisesti eVakassa. Mikäli haette paikkaa palvelusetelipäiväkodista, olkaa yhteydessä kyseiseen päiväkotiin viimeistään hakemuksen jättämisen jälkeen.</p>
            
            <p>Ilta- ja vuorohoitoa haettaessa, hakemuksen liitteeksi tulee toimittaa molempien samassa taloudessa asuvien huoltajien todistukset työnantajalta vuorotyöstä tai oppilaitoksesta iltaisin tapahtuvasta opiskelusta. Hakemusta käsitellään vuorohoidon hakemuksena vasta sen jälkeen, kun edellä mainitut todistukset on toimitettu. Tarvittavat liitteet voi lisätä suoraan sähköiselle hakemukselle tai toimittaa postitse osoitteeseen Tampereen kaupunki, Varhaiskasvatuksen asiakaspalvelu, PL 487, 33101 Tampere.</p> 
            
            <p>Hakiessanne lapsellenne siirtoa toiseen varhaiskasvatusyksikköön, hakemuksella ei ole hakuaikaa. Siirrot pystytään toteuttamaan pääsääntöisesti elokuusta alkaen. Mikäli lapsen nykyinen hoitopaikka irtisanotaan, myös siirtohakemus poistuu.</p> 
            
            <p>Lisätietoa hakemuksen käsittelystä ja varhaiskasvatuspaikan myöntämisestä saa varhaiskasvatuksen ja esiopetuksen asiakaspalvelusta:</p>
            
            <p>
            Tampereen kaupunki<br/>
            Sivistyspalvelut<br/>
            Varhaiskasvatus ja esiopetus<br/>
            Asiakaspalvelu
            </p>
            
            <p>Tämä on automaattinen viesti, joka kertoo lomakkeen tallennuksesta. Viestiin ei voi vastata reply-/ vastaa-toiminnolla.</p>
        """.trimIndent()
    }

    private fun getPreschoolApplicationReceivedEmailHtml(): String {
        return """
            <p>Hyvä huoltaja,</p>

            <p>lapsenne esiopetukseen ilmoittautuminen on vastaanotettu.</p>

            <p>Hakemuksen tehnyt huoltaja voi muokata hakemusta Tampereen varhaiskasvatuksen verkkopalvelussa eVakassa siihen saakka, kunnes se on otettu käsittelyyn.</p> 

            <p>Lisätietoa hakemuksen käsittelystä ja esiopetuspaikan myöntämisestä saa varhaiskasvatuksen ja esiopetuksen asiakaspalvelusta:</p>

            <p>
            Tampereen kaupunki<br/>
            Sivistyspalvelut<br/>
            Varhaiskasvatus ja esiopetus<br/>
            Asiakaspalvelu
            </p>

            <p>Tämä on automaattinen viesti, joka kertoo lomakkeen tallennuksesta. Viestiin ei voi vastata reply-/ vastaa-toiminnolla.</p>
        """.trimIndent()
    }

    private fun getDecisionEmailHtml(): String = """
        <p>Hyvä(t) huoltaja(t),</p>
        <p>Lapsellenne on tehty päätös liittyen varhaiskasvatukseen/esiopetukseen.</p>
        <p>Päätös on nähtävissä Tampereen varhaiskasvatuksen verkkopalvelu eVakassa Päätökset-välilehdeltä.</p>
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
<p>Tampereen varhaiskasvatuksen verkkopalvelusta eVakasta puuttuu lapsen läsnäolovarauksia $start alkavalta viikolta. Käythän merkitsemässä ne mahdollisimman pian Kalenteri-välilehdelle, kiitos!</p>
$unsubscribeFi
<hr>
<p>There are missing attendance reservations in Tampere`s early childhood education system eVaka for the week starting $start. Please mark them as soon as possible on the Calendar page, thank you!</p>
$unsubscribeEn
            """
                .trimIndent(),
        )
    }

    override fun missingHolidayReservationsNotification(language: Language): EmailContent {
        return EmailContent.fromHtml(
            subject =
            "Loma-ajan ilmoitus sulkeutuu / Holiday notification period closing",
            html =
            """
<p>Loma-ajan kysely sulkeutuu kahden päivän päästä. Jos lapseltanne/lapsiltanne puuttuu loma-ajan ilmoitus yhdeltä tai useammalta lomapäivältä, teettehän ilmoituksen eVakan kalenterissa mahdollisimman pian.</p>
$unsubscribeFi
<hr>
<p>Two days left to submit a holiday notification. If you have not submitted a notification for each day, please submit them through the eVaka calendar as soon as possible.</p>
$unsubscribeEn
""",
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
                <p>Sinulle on saapunut uusi $typeFi Tampereen varhaiskasvatuksen verkkopalveluun eVakaan.</p>
                <p>Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.</p>
                $unsubscribeFi

                <hr>

                <p>You have received a new $typeEn in Tampere`s early childhood education system eVaka.</p>
                <p>This is an automatic message from the eVaka system. Do not reply to this message.</p>
                $unsubscribeEn
        """
                .trimIndent(),
        )
    }

    override fun childDocumentNotification(language: Language, childId: ChildId): EmailContent {
        return EmailContent.fromHtml(
            subject = "Uusi dokumentti eVakassa / New document in eVaka",
            html =
            """
<p>Sinulle on saapunut uusi dokumentti Tampereen varhaiskasvatuksen verkkopalveluun eVakaan.</p>
<p>Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.</p>
$unsubscribeFi
<hr>
<p>You have received a new document in Tampere`s early childhood education system eVaka.</p>
<p>This is an automatic message from the eVaka system. Do not reply to this message.</p>
$unsubscribeEn
""",
        )
    }

    override fun vasuNotification(language: Language, childId: ChildId): EmailContent {
        return EmailContent.fromHtml(
            subject = "Uusi dokumentti eVakassa / New document in eVaka",
            html =
            """
                <p>Sinulle on saapunut uusi dokumentti Tampereen varhaiskasvatuksen verkkopalveluun eVakaan. Löydät dokumentin Lapset-välilehdeltä.</p>
                <p>Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.</p>
                $unsubscribeFi

                <hr>

                <p>You have received a new document in Tampere`s early childhood education system eVaka. You can find the document on the Children page.</p>
                <p>This is an automatic message from the eVaka system. Do not reply to this message.</p>
                $unsubscribeEn
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
                <p>Sinulle on saapunut uusi pedagoginen dokumentti Tampereen varhaiskasvatuksen verkkopalveluun eVakaan. Löydät dokumentin Lapset-välilehdeltä.</p>
                <p>Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.</p>
                $unsubscribeFi

                <hr>

                <p>You have received a new pedagogical document in Tampere`s early childhood education system eVaka. You can find the document on the Children page.</p>
                <p>This is an automatic message from the eVaka system. Do not reply to this message.</p>
                $unsubscribeEn
        """
                .trimIndent(),
        )
    }

    override fun incomeNotification(
        notificationType: IncomeNotificationType,
        language: Language,
    ): EmailContent {
        return when (notificationType) {
            IncomeNotificationType.INITIAL_EMAIL -> outdatedIncomeNotificationInitial()
            IncomeNotificationType.REMINDER_EMAIL -> outdatedIncomeNotificationReminder()
            IncomeNotificationType.EXPIRED_EMAIL -> outdatedIncomeNotificationExpired()
            IncomeNotificationType.NEW_CUSTOMER -> newCustomerIncomeNotification()
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
                
                <p>Pyydämme toimittamaan tuloselvityksen eVakassa 28 päivän kuluessa tästä ilmoituksesta. Tampereen varhaiskasvatuksen verkkopalvelussa eVakassa voitte myös antaa suostumuksen korkeimpaan maksuluokkaan tai tulorekisterin käyttöön.</p>
                
                <p>Mikäli ette toimita uusia tulotietoja, asiakasmaksu määräytyy korkeimman maksuluokan mukaan. Uusi maksupäätös astuu voimaan sen kuukauden alusta, kun tulotiedot on toimitettu asiakasmaksuihin.</p>
                
                <p>Lisätietoja saatte tarvittaessa Tampereen kaupungin verkkosivuilta.</p>
                
                <p>Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.</p>
                
                $unsubscribeFi
                
                <hr>
                
                <p>Dear client</p>
                
                <p>The income information used for determining the early childhood education fee or the out-of-pocket cost of a service voucher is reviewed every year.</p>
                
                <p>We ask you to submit your income statement through eVaka within 28 days of this notification. Through Tampere`s early childhood education system eVaka, you can also give your consent to the highest fee or the use of the Incomes Register.</p>
                
                <p>If you do not provide your latest income information, your client fee will be determined based on the highest fee category. The new payment decision takes effect at the beginning of the month when the income information has been submitted to customer services.</p>
                
                <p>If necessary, you can get more information from the website of the city of Tampere.</p>
                
                <p>This is an automatic message from the eVaka system. Do not reply to this message.</p>
                
                $unsubscribeEn
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
                
                <p>Pyydämme toimittamaan tuloselvityksen eVakassa 14 päivän kuluessa tästä ilmoituksesta. Tampereen varhaiskasvatuksen verkkopalvelussa eVakassa voitte myös antaa suostumuksen korkeimpaan maksuluokkaan tai tulorekisterin käyttöön.</p>
                
                <p>Mikäli ette toimita uusia tulotietoja, asiakasmaksu määräytyy korkeimman maksuluokan mukaan. Uusi maksupäätös astuu voimaan sen kuukauden alusta, kun tulotiedot on toimitettu asiakasmaksuihin.</p>
                
                <p>Lisätietoja saatte tarvittaessa Tampereen kaupungin verkkosivuilta.</p>
                
                <p>Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.</p>
                
                $unsubscribeFi
                
                <hr>
                
                <p>Dear client</p>
                
                <p>You have not yet submitted your latest income information. The income information used for determining the early childhood education fee or the out-of-pocket cost of a service voucher is reviewed every year.</p>
                
                <p>We ask you to submit your income statement through eVaka within 14 days of this notification. Through Tampere`s early childhood education system eVaka, you can also give your consent to the highest fee or the use of the Incomes Register.</p>
                
                <p>If you do not provide your latest income information, your client fee will be determined based on the highest fee category. The new payment decision takes effect at the beginning of the month when the income information has been submitted to customer services.</p>
                
                <p>If necessary, you can get more information from the website of the city of Tampere.</p>
                
                <p>This is an automatic message from the eVaka system. Do not reply to this message.</p>
                
                $unsubscribeEn
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
                
                <p>Lisätietoja saatte tarvittaessa Tampereen kaupungin verkkosivuilta.</p>
                
                <p>Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.</p>
                
                $unsubscribeFi
                
                <hr>
                
                <p>Dear client</p>
                
                <p>Your next client fee will be determined based on the highest fee category as you did not provide your latest income information by the deadline.</p>
                
                <p>If necessary, you can get more information from the website of the city of Tampere.</p>
                
                <p>This is an automatic message from the eVaka system. Do not reply to this message.</p>
                
                $unsubscribeEn
               """
                .trimIndent(),
        )
    }

    private fun newCustomerIncomeNotification(): EmailContent {
        return EmailContent.fromHtml(
            subject =
            "Tulotietojen tarkastuskehotus / Request to review income information",
            html =
            """
<p>Hyvä asiakkaamme</p>
<p>Lapsenne on aloittamassa varhaiskasvatuksessa tämän kuukauden aikana. Pyydämme teitä toimittamaan tulotiedot eVaka-järjestelmän kautta tämän kuukauden loppuun mennessä.</p>
<p>Lisätietoja saatte tarvittaessa Tampereen kaupungin verkkosivuilta.</p>
<p>Tämä on eVaka-järjestelmän automaattisesti lähettämä ilmoitus. Älä vastaa tähän viestiin.</p>
$unsubscribeFi
<hr>
<p>Dear client</p>
<p>Your child is starting early childhood education during this month. We ask you to submit your income information via eVaka system by the end of this month.</p>
<p>If necessary, you can get more information from the website of the city of Tampere.</p>
<p>This is an automatic message from the eVaka system. Do not reply to this message.</p>
$unsubscribeEn
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
<p>Tampereen varhaiskasvatuksen verkkopalveluun eVakaan on lisätty uusia kalenteritapahtumia:</p>
$eventsHtml
<p>Löydät tapahtumat Kalenteri-välilehdeltä.</p>
$unsubscribeFi
<hr>
<p>New calendar events in Tampere`s early childhood education system eVaka:</p>
$eventsHtml
<p>You can find events on the Calendar page.</p>
$unsubscribeEn
""",
        )
    }

    override fun financeDecisionNotification(
        decisionType: FinanceDecisionType,
    ): EmailContent {
        val (decisionTypeFi, decisionTypeEn) =
            when (decisionType) {
                FinanceDecisionType.VOUCHER_VALUE_DECISION ->
                    Pair(
                        "arvopäätös",
                        "voucher value decision",
                    )
                FinanceDecisionType.FEE_DECISION ->
                    Pair("maksupäätös", "fee decision")
            }
        return EmailContent.fromHtml(
            subject =
            "Uusi $decisionTypeFi eVakassa / New $decisionTypeEn in eVaka",
            html =
            """
<p>Sinulle on saapunut uusi $decisionTypeFi Tampereen varhaiskasvatuksen verkkopalveluun eVakaan.</p>
$unsubscribeFi
<hr>
<p>You have received a new $decisionTypeEn in Tampere`s early childhood education system eVaka.</p>
$unsubscribeEn
            """
                .trimIndent(),
        )
    }

    override fun discussionSurveyReservationNotification(
        language: Language,
        notificationDetails: DiscussionSurveyReservationNotificationData,
    ): EmailContent {
        return EmailContent.fromHtml(
            subject =
            "Uusi keskusteluaika varattu eVakassa / New discussion time reserved in eVaka",
            html =
            """
<p>Lapsellenne on varattu keskusteluaika</p>
<p>${notificationDetails.title}</p>
<p>${notificationDetails.childName}</p>
<p>${notificationDetails.calendarEventTime.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}</p>
<p>${notificationDetails.calendarEventTime.startTime.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${notificationDetails.calendarEventTime.endTime.format(DateTimeFormatter.ofPattern("HH:mm"))}</p>
<p>Varauksen voi peruuttaa 2 arkipäivää ennen varattua aikaa suoraan eVakan kalenterinäkymästä. Myöhempää peruutusta varten ota yhteyttä henkilökuntaan.</p>
$unsubscribeFi
<hr>
<p>New discussion time reserved for your child</p>
<p>${notificationDetails.title}</p>
<p>${notificationDetails.childName}</p>
<p>${notificationDetails.calendarEventTime.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}</p>
<p>${notificationDetails.calendarEventTime.startTime.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${notificationDetails.calendarEventTime.endTime.format(DateTimeFormatter.ofPattern("HH:mm"))}</p>
<p>Reservation can be cancelled 2 business days before the reserved time using the eVaka calendar view. For later cancellations contact the daycare staff.</p>
$unsubscribeEn
<hr>
            """
                .trimIndent(),
        )
    }

    override fun discussionSurveyReservationCancellationNotification(
        language: Language,
        notificationDetails: DiscussionSurveyReservationNotificationData,
    ): EmailContent {
        return EmailContent.fromHtml(
            subject =
            "Keskusteluaika peruttu eVakassa / Discussion time cancelled in eVaka",
            html =
            """
<p>Lapsellenne varattu keskusteluaika on peruttu</p>
<p>${notificationDetails.title}</p>
<p>${notificationDetails.childName}</p>
<p>${notificationDetails.calendarEventTime.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}</p>
<p>${notificationDetails.calendarEventTime.startTime.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${notificationDetails.calendarEventTime.endTime.format(DateTimeFormatter.ofPattern("HH:mm"))}</p>
$unsubscribeFi
<hr>
<p>Discussion time reserved for your child has been cancelled</p>
<p>${notificationDetails.title}</p>
<p>${notificationDetails.childName}</p>
<p>${notificationDetails.calendarEventTime.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}</p>
<p>${notificationDetails.calendarEventTime.startTime.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${notificationDetails.calendarEventTime.endTime.format(DateTimeFormatter.ofPattern("HH:mm"))}</p>
$unsubscribeEn
<hr>
            """
                .trimIndent(),
        )
    }

    override fun discussionSurveyCreationNotification(
        language: Language,
        notificationDetails: DiscussionSurveyCreationNotificationData,
    ): EmailContent {
        return EmailContent.fromHtml(
            subject =
            "Varaa keskusteluaika varhaiskasvatukseen / Reserve a discussion time for early childhood education",
            html =
            """
<p>${notificationDetails.eventTitle}</p>
<p>${notificationDetails.eventDescription}</p>
<p>Ajan voi varata eVakan kalenterinäkymästä</p>
$unsubscribeFi
<hr>
<p>${notificationDetails.eventTitle}</p>
<p>${notificationDetails.eventDescription}</p>
<p>You can reserve a time using eVaka calendar view</p>
$unsubscribeEn
<hr>
            """
                .trimIndent(),
        )
    }

    override fun discussionTimeReservationReminder(
        language: Language,
        reminderData: DiscussionTimeReminderData,
    ): EmailContent {
        return EmailContent.fromHtml(
            subject =
            "Muistutus tulevasta keskusteluajasta / Reminder for an upcoming discussion time",
            html =
            """
<p>Lapsellenne on varattu keskusteluaika</p>
<p>${reminderData.title}</p>
<p>${reminderData.firstName} ${reminderData.lastName}</p>
<p>${reminderData.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}</p>
<p>${reminderData.startTime.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${
                reminderData.endTime.format(
                    DateTimeFormatter.ofPattern("HH:mm"),
                )
            }</p>
<p>Varauksen voi peruuttaa 2 arkipäivää ennen varattua aikaa suoraan eVakan kalenterinäkymästä. Myöhempää peruutusta varten ota yhteyttä henkilökuntaan.</p>
$unsubscribeFi
<hr>
<p>New discussion time reserved for your child</p>
<p>${reminderData.title}</p>
<p>${reminderData.firstName} ${reminderData.lastName}</p>
<p>${reminderData.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}</p>
<p>${reminderData.startTime.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${
                reminderData.endTime.format(
                    DateTimeFormatter.ofPattern("HH:mm"),
                )
            }</p>
<p>Reservation can be cancelled 2 business days before the reserved time using the eVaka calendar view. For later cancellations contact the daycare staff.</p>
$unsubscribeEn
<hr>
            """
                .trimIndent(),
        )
    }
}
