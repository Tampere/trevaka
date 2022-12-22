// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import config from 'e2e-test/config'
import { Page, Radio, Checkbox } from 'e2e-test/utils/page'
import { waitUntilEqual } from 'e2e-test/utils'
import CitizenHeader from 'e2e-test/pages/citizen/citizen-header'
import CitizenApplicationsPage from 'e2e-test/pages/citizen/citizen-applications'
import { enduserLogin } from 'e2e-test/utils/user'
import {
  resetDatabaseForE2ETests
} from '../../common/tampere-dev-api'
import { enduserChildFixturePorriHatterRestricted, Fixture } from "e2e-test/dev-api/fixtures";
  

let page: Page
let header: CitizenHeader
let applicationsPage: CitizenApplicationsPage

beforeEach(async () => {
  await resetDatabaseForE2ETests()
  await Fixture.person()
      .with(enduserChildFixturePorriHatterRestricted)
      .save()
  page = await Page.open()
  await page.goto(config.enduserUrl)
  await enduserLogin(page)
  header = new CitizenHeader(page)
  applicationsPage = new CitizenApplicationsPage(page)
})
afterEach(async () => {
  await page.close()
})

const customerContactText = 'Varhaiskasvatuksen asiakaspalveluun: varhaiskasvatus.asiakaspalvelu@tampere.fi / 040 800 7260 (ma–pe klo 9–12).'
const customerContactEmailHref = 'mailto:varhaiskasvatus.asiakaspalvelu@tampere.fi'
const customerContactTelHref = 'tel:+358408007260'

describe('Citizen applications page', () => {
  test('Applications and ApplicationCreation customizations', async () => {
    await header.selectTab('applications')
    await waitUntilEqual(() => page.find('h1 + p').text, 'Lapsen huoltaja voi tehdä lapselleen hakemuksen varhaiskasvatukseen, esiopetukseen ja kerhoon. Huoltajan lasten tiedot haetaan tähän näkymään automaattisesti Väestötietojärjestelmästä.')
    await page.find(`[data-qa="new-application-${enduserChildFixturePorriHatterRestricted.id}"]`).click()
    // Check that all options are visible
    let applicationRadios = await page.findAll('[data-qa^="type-radio-"]')
    expect(await applicationRadios.count()).toBe(3)
    await waitUntilEqual(
      () => applicationRadios.first().find('label').text,
      'Varhaiskasvatus- ja palvelusetelihakemus'
    )
    await waitUntilEqual(
      () => applicationRadios.nth(1).find('label').text,
      'Esiopetukseen ilmoittautuminen'
    )
    await waitUntilEqual(
      () => applicationRadios.nth(2).find('label').text,
      'Kerhohakemus'
    )
    // Check all texts, including expanding infos
    await page.find('[data-qa="daycare-expanding-info"]').click()
    await waitUntilEqual(() => page.find('[data-qa="daycare-expanding-info-text"]').text, 'Varhaiskasvatushakemuksella haetaan paikkaa kunnallisesta päiväkodista tai perhepäivähoidosta, ostopalvelupäiväkodista tai palvelusetelillä tuetusta päiväkodista.')
    await page.find('[data-qa="club-expanding-info"]').click()
    await waitUntilEqual(
      () => page.find('[data-qa="club-expanding-info-text"]').text,
      'Kerhohakemuksella haetaan paikkaa kunnallisista tai palvelusetelillä tuetuista kerhoista.'
    )
    await waitUntilEqual(
      () => page.findText(/^Huoltaja voi tehdä muutoksia hakemukseen verkkopalvelussa/).text,
      `Huoltaja voi tehdä muutoksia hakemukseen verkkopalvelussa siihen asti, kun hakemus otetaan asiakaspalvelussa käsittelyyn. Tämän jälkeen muutokset tai hakemuksen peruminen on mahdollista ottamalla yhteyttä ${customerContactText}`
    )
    await waitUntilEqual(
      () => page.find('[data-qa="application-options-area"] p:last-of-type a:first-of-type').getAttribute( 'href'),
      customerContactEmailHref
    )
    await waitUntilEqual(
      () => page.find('[data-qa="application-options-area"] p:last-of-type a:last-of-type').getAttribute( 'href'),
      customerContactTelHref
    )
  })
  test('Daycare application form customizations', async () => {
    await header.selectTab('applications')
    let editorPage = await applicationsPage.createApplication(enduserChildFixturePorriHatterRestricted.id, 'DAYCARE')
    await waitUntilEqual(
      () => page.find('[data-qa="application-child-name-title"] + p').text,
      'Varhaiskasvatuspaikkaa voi hakea ympäri vuoden. Varhaiskasvatushakemus tulee jättää viimeistään neljä kuukautta ennen hoidon toivottua alkamisajankohtaa. Mikäli varhaiskasvatuksen tarve johtuu työllistymisestä, opinnoista tai koulutuksesta, eikä hoidon tarpeen ajankohtaa ole pystynyt ennakoimaan, on varhaiskasvatuspaikkaa haettava mahdollisimman pian - kuitenkin viimeistään kaksi viikkoa ennen kuin lapsi tarvitsee hoitopaikan.'
    )
    await waitUntilEqual(
      () => page.find('[data-qa="application-child-name-title"] + p + p').text,
      'Kirjallinen päätös varhaiskasvatuspaikasta lähetetään Suomi.fi-viestit -palveluun. Mikäli haluatte päätöksen sähköisenä tiedoksiantona, teidän tulee ottaa Suomi.fi-viestit -palvelu käyttöön. Palvelusta ja sen käyttöönotosta saatte lisätietoa https://www.suomi.fi/viestit . Mikäli ette ota Suomi.fi-viestit -palvelua käyttöön, päätös lähetetään teille postitse.'
    )
    await page.find('[data-qa="startdate-instructions"]').click()
    await waitUntilEqual(
      () => page.find('[data-qa="startdate-instructions-text"]').text,
      `Toivottua aloituspäivää on mahdollista muuttaa myöhemmäksi niin kauan, kun hakemusta ei ole otettu käsittelyyn. Tämän jälkeen toivotun aloituspäivän muutokset tehdään ottamalla yhteyttä ${customerContactText}`
    )
    await waitUntilEqual(
      () => page.find('[data-qa="startdate-instructions-text"] a:first-of-type').getAttribute('href'),
      customerContactEmailHref
    )
    await waitUntilEqual(
      () => page.find('[data-qa="startdate-instructions-text"] a:last-of-type').getAttribute('href'),
      customerContactTelHref
    )
    await waitUntilEqual(
      () => page.find('[data-qa="urgent-input"] + div + p').text,
      `Mikäli varhaiskasvatuspaikan tarve johtuu äkillisestä työllistymisestä tai opiskelupaikan saamisesta, tulee paikkaa hakea viimeistään kaksi viikkoa ennen kuin hoidon tarve alkaa. Lisäksi huoltajan tulee ottaa yhteyttä viipymättä ${customerContactText}`
    )
    await editorPage.setCheckbox('urgent', true)
    // Missing Fileupload for urgent application attachments
    await page.find('[data-qa="urgent-file-upload"]').waitUntilHidden()
    // Check service need options (order, it is working, aso.)
    const partTimeRadio = page.find('[data-qa="partTime-input-true"]')
    const fullTimeRadio = page.find('[data-qa="partTime-input-false"]')
    await fullTimeRadio.waitUntilVisible()
    await partTimeRadio.waitUntilVisible()
    let fullTimeOptions = await page.findAll('[data-qa^="full-time-option-"]')
    let partTimeOptions = await page.findAll('[data-qa^="part-time-option-"]')
    expect(await fullTimeOptions.count()).toBe(3)
    expect(await partTimeOptions.count()).toBe(0)
    await new Radio(fullTimeOptions.first()).waitUntilChecked()
    await waitUntilEqual(
      () => fullTimeOptions.first().find('label').text,
      'Kokopäiväinen'
    )
    await waitUntilEqual(
      () => fullTimeOptions.nth(1).find('label').text,
      'Kokopäiväinen, 10 pv sopimus'
    )
    await waitUntilEqual(
      () => fullTimeOptions.nth(2).find('label').text,
      'Kokopäiväinen, 15 pv sopimus'
    )
    await partTimeRadio.click()
    fullTimeOptions = await page.findAll('[data-qa^="full-time-option-"]')
    partTimeOptions = await page.findAll('[data-qa^="part-time-option-"]')
    expect(await fullTimeOptions.count()).toBe(0)
    expect(await partTimeOptions.count()).toBe(4)
    // After changing to part time the first sub radio option should be selected
    await new Radio(partTimeOptions.first()).waitUntilChecked()
    await waitUntilEqual(
        () => partTimeOptions.first().find('label').text,
        'Osapäiväinen, max 20h viikossa'
    )
    await waitUntilEqual(
      () => partTimeOptions.nth(1).find('label').text,
      'Osapäiväinen, max 5h päivässä'
    )
    await waitUntilEqual(
      () => partTimeOptions.nth(2).find('label').text,
      'Osapäiväinen, max 5h päivässä; 10 pv sopimus')

    await waitUntilEqual(
      () => partTimeOptions.nth(3).find('label').text,
      'Osapäiväinen, max 5h päivässä; 15 pv sopimus'
    )
    await fullTimeRadio.click()
    fullTimeOptions = await page.findAll('[data-qa^="full-time-option-"]')
    partTimeOptions = await page.findAll('[data-qa^="part-time-option-"]')
    expect(await partTimeOptions.count()).toBe(0)
    expect(await fullTimeOptions.count()).toBe(3)
    // After changing to full time the first sub radio option should be selected
    await new Radio(fullTimeOptions.first()).waitUntilChecked()
    // Check that there are no inputs for start and end date
    await page.find('[data-qa="startTime-input"]').waitUntilHidden()
    await page.find('[data-qa="endTime-input"]').waitUntilHidden()
    // Check texts
    await page.find('[data-qa="shiftcare-instructions"]').click()
    let shiftcareCheckbox = new Checkbox(page.find('[data-qa="shiftCare-input"]'))
    await shiftcareCheckbox.check()
    await shiftcareCheckbox.waitUntilChecked()
    await waitUntilEqual(
      () => page.find('[data-qa="shiftcare-instructions-text"]').text,
      'Mikäli lapsi tarvitsee esiopetuksen lisäksi ilta-/vuorohoitoa, hänet pitää ilmoittaa ilta- tai vuorohoidon esiopetukseen. Lisäksi täydentäväksi toiminnaksi on lapselle valittava täydentävä varhaiskasvatus, yli 5h päivässä. Päiväkodit palvelevat normaalisti arkisin klo 6.00–18.00. Iltahoito on tarkoitettu lapsille, jotka vanhempien työn tai tutkintoon johtavan opiskelun vuoksi tarvitsevat säännöllisesti hoitoa klo 18.00 jälkeen. Iltahoitoa tarjoavat päiväkodit aukeavat tarvittaessa klo 5.30 ja menevät kiinni viimeistään klo 22.30. Osa iltahoitoa antavista päiväkodeista on auki myös viikonloppuisin. Vuorohoito on tarkoitettu niille lapsille, joiden vanhemmat tekevät vuorotyötä ja lapsen hoitoon sisältyy myös öitä.'
    )
    await waitUntilEqual(
      () => page.find('[data-qa="shiftcare-attachments-message"]').text,
      'Ilta- ja vuorohoito on tarkoitettu lapsille, jotka vanhempien työn tai tutkintoon johtavan opiskelun vuoksi tarvitsevat ilta- ja vuorohoitoa. Hakemuksen liitteeksi on toimitettava vanhempien osalta työnantajan todistus vuorotyöstä tai opiskelusta johtuvasta ilta- tai vuorohoidon tarpeesta.'
    )
    const assistanceNeedInstructionsDaycare = page.find('[data-qa="assistanceNeedInstructions-DAYCARE"]')
    await assistanceNeedInstructionsDaycare.click()
    await waitUntilEqual(
      () => page.find('[data-qa="assistanceNeedInstructions-DAYCARE-text"]').text,
      'Tehostettua tai erityistä tukea annetaan lapselle heti tarpeen ilmettyä. Mikäli lapsella on olemassa tuen tarpeesta asiantuntijalausunto, tämä tulee ilmoittaa varhaiskasvatushakemuksella. Tukitoimet toteutuvat lapsen arjessa osana varhaiskasvatuksen toimintaa. Tampereen varhaiskasvatuksesta otetaan erikseen yhteyttä hakemuksen jättämisen jälkeen, jos lapsella on tuen tarve.'
    )
    await editorPage.openSection('contactInfo')
    let contactInfoText = page.find('[data-qa="contactInfo-section"] p[data-qa="contact-info-text"]')
    await waitUntilEqual(
      () => contactInfoText.text,
      'Henkilötiedot on haettu väestötiedoista, eikä niitä voi muuttaa tällä hakemuksella. Jos henkilötiedoissa on virheitä, päivitäthän tiedot Digi- ja Väestötietoviraston sivuilla . Mikäli osoitteenne on muuttumassa, voit lisätä tulevan osoitteen erilliseen kohtaan hakemuksella; lisää tuleva osoite sekä lapselle että huoltajalle. Virallisena osoitetietoa pidetään vasta, kun se on päivittynyt väestötietojärjestelmään. Varhaiskasvatus-, palveluseteli- ja esiopetuspäätös sekä tieto avoimen varhaiskasvatuksen kerhopaikasta toimitetaan automaattisesti myös eri osoitteessa asuvalle väestötiedoista löytyvälle huoltajalle.'
    )
    await waitUntilEqual(
      () => contactInfoText.find('a').getAttribute('href'),
      'https://dvv.fi/henkiloasiakkaat'
    )
    const childFutureAddrInfo = page.find('[data-qa="child-future-address-info"]')
    await childFutureAddrInfo.click()
    await waitUntilEqual(
      () => page.find('[data-qa="child-future-address-info-text"]').text,
      'Tampereen varhaiskasvatuksessa ja esiopetuksessa virallisena osoitteena pidetään väestötiedoista saatavaa osoitetta. Osoite väestötiedoissa muuttuu hakijan tehdessä muuttoilmoituksen postiin tai maistraattiin.'
    )
    await page.find('[data-qa="guardian-future-address-info"]').click()
    await waitUntilEqual(
      () => page.find('[data-qa="guardian-future-address-info-text"]').text,
      'Tampereen varhaiskasvatuksessa ja esiopetuksessa virallisena osoitteena pidetään väestötiedoista saatavaa osoitetta. Osoite väestötiedoissa muuttuu hakijan tehdessä muuttoilmoituksen postiin tai maistraattiin.'
  )
    await editorPage.openSection('additionalDetails')
    const dietInfo = page.find('[data-qa="diet-expanding-info"]')
    await dietInfo.click()
    await waitUntilEqual(
      () => page.find('[data-qa="diet-expanding-info-text"]').text,
      'Erityisruokavaliosta huoltaja toimittaa varhaiskasvatus tai esiopetuspaikkaan lääkärin tai ravitsemusterapeutin täyttämän ja allekirjoittaman Selvitys erityisruokavaliosta -lomakkeen , joka on määräaikainen.'
    )
    await waitUntilEqual(
      () => page.find('[data-qa="diet-expanding-info-text"] a').getAttribute('href'),
      'https://www.tampere.fi/erityisruokavaliot'
    )
    // Click cancel and check text for an existing application
    await page.find('[data-qa="cancel-application-button"]').click()
    await header.selectTab('applications')
    await page.find(`[data-qa="new-application-${enduserChildFixturePorriHatterRestricted.id}"]`).click()
    await page.find('[data-qa="type-radio-DAYCARE"]').click()
    await waitUntilEqual(
      () => page.find('[data-qa="duplicate-application-notification"]').text,
      'Lapsella on jo samantyyppinen, keskeneräinen hakemus. Palaa Hakemukset-näkymään ja muokkaa olemassa olevaa hakemusta tai ota yhteyttä Varhaiskasvatuksen asiakaspalveluun.'
    )
  })
  test('Club application form customizations', async () => {
    await header.selectTab('applications')
    let editorPage = await applicationsPage.createApplication(enduserChildFixturePorriHatterRestricted.id, 'CLUB')
    await waitUntilEqual(
      () => page.find('[data-qa="application-child-name-title"] + p').text,
      'Kerhopaikkaa voi hakea ympäri vuoden. Kerhohakemuksella voi hakea kunnallista tai palvelusetelillä tuettua kerhopaikkaa. Kirjallinen ilmoitus kerhopaikasta lähetään Suomi.fi-viestit -palveluun. Mikäli haluatte ilmoituksen sähköisenä tiedoksiantona, teidän tulee ottaa Suomi.fi-viestit -palvelu käyttöön. Palvelusta ja sen käyttöönotosta saatte lisätietoa https://www.suomi.fi/viestit . Mikäli ette ota Suomi.fi-viestit -palvelua käyttöön, ilmoitus kerhopaikasta lähetetään teille postitse. Paikka myönnetään yhdeksi toimintakaudeksi kerrallaan.'
    )
    await waitUntilEqual(
      () => page.find('[data-qa="application-child-name-title"] + p + p').text,
      'Kerhohakemus kohdistuu yhdelle kerhon toimintakaudelle. Kyseisen kauden päättyessä hakemus poistetaan järjestelmästä.'
    )
    await page.find('[data-qa="wasOnDaycare-info"]').click()
    await waitUntilEqual(
      () => page.find('[data-qa="wasOnDaycare-info-text"]').text,
      'Jos lapsi on ollut kunnallisessa päiväkodissa tai perhepäivähoidossa ja hän luopuu paikastaan kerhon alkaessa, hänellä on suurempi mahdollisuus saada kerhopaikka.'
    )
    await page.find('[data-qa="wasOnClubCare-info"]').click()
    await waitUntilEqual(
      () => page.find('[data-qa="wasOnClubCare-info-text"]').text,
      'Jos lapsi on ollut kerhossa jo edellisen toimintakauden aikana, hänellä on suurempi mahdollisuus saada paikka kerhosta myös tulevana toimintakautena.'
    )
    await page.find('[data-qa="assistanceNeedInstructions-CLUB"]').click()
    await waitUntilEqual(
      () => page.find('[data-qa="assistanceNeedInstructions-CLUB-text"]').text,
      'Jos lapsella on tuen tarve, Tampereen varhaiskasvatuksesta otetaan yhteyttä hakemuksen jättämisen jälkeen.'
    )
    await editorPage.openSection('contactInfo')
    await waitUntilEqual(
      () => page.find('[data-qa="contactInfo-section"] p[data-qa="contact-info-text"]').text,
      'Henkilötiedot on haettu väestötiedoista, eikä niitä voi muuttaa tällä hakemuksella. Jos henkilötiedoissa on virheitä, päivitäthän tiedot Digi- ja Väestötietoviraston sivuilla . Mikäli osoitteenne on muuttumassa, voit lisätä tulevan osoitteen erilliseen kohtaan hakemuksella; lisää tuleva osoite sekä lapselle että huoltajalle. Virallisena osoitetietoa pidetään vasta, kun se on päivittynyt väestötietojärjestelmään. Varhaiskasvatus-, palveluseteli- ja esiopetuspäätös sekä tieto avoimen varhaiskasvatuksen kerhopaikasta toimitetaan automaattisesti myös eri osoitteessa asuvalle väestötiedoista löytyvälle huoltajalle.'
    )
    await page.find('[data-qa="child-future-address-info"]').click()
    await waitUntilEqual(
      () => page.find('[data-qa="child-future-address-info-text"]').text,
      'Tampereen varhaiskasvatuksessa ja esiopetuksessa virallisena osoitteena pidetään väestötiedoista saatavaa osoitetta. Osoite väestötiedoissa muuttuu hakijan tehdessä muuttoilmoituksen postiin tai maistraattiin.'
    )
    await page.find('[data-qa="guardian-future-address-info"]').click()
    await waitUntilEqual(
      () => page.find('[data-qa="guardian-future-address-info-text"]').text,
      'Tampereen varhaiskasvatuksessa ja esiopetuksessa virallisena osoitteena pidetään väestötiedoista saatavaa osoitetta. Osoite väestötiedoissa muuttuu hakijan tehdessä muuttoilmoituksen postiin tai maistraattiin.'
    )
  })
})
