// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import { newBrowserContext } from 'e2e-playwright/browser'
import config from 'e2e-test-common/config'
import { ElementHandle, Page } from 'playwright'
import { waitUntilTrue, waitUntilEqual, waitUntilFalse } from 'e2e-playwright/utils'
import { RawElementDEPRECATED as RawElement, Radio, Checkbox } from 'e2e-playwright/utils/element'
import CitizenHeader from 'e2e-playwright/pages/citizen/citizen-header'
import CitizenApplicationsPage from 'e2e-playwright/pages/citizen/citizen-applications'
import { enduserLogin } from 'e2e-playwright/utils/user'
import {
  resetDatabaseForE2ETests
} from '../../common/tampere-dev-api'
import { enduserChildFixturePorriHatterRestricted, Fixture } from "e2e-test-common/dev-api/fixtures";
  

let page: Page
let header: CitizenHeader
let applicationsPage: CitizenApplicationsPage

beforeEach(async () => {
  await resetDatabaseForE2ETests()
  await Fixture.person()
      .with(enduserChildFixturePorriHatterRestricted)
      .save()
  page = await (await newBrowserContext()).newPage()
  await page.goto(config.enduserUrl)
  await enduserLogin(page)
  header = new CitizenHeader(page)
  applicationsPage = new CitizenApplicationsPage(page)
})
afterEach(async () => {
  await page.close()
})

const customerContactText = 'Varhaiskasvatuksen asiakaspalveluun: varhaiskasvatus.asiakaspalvelu@tampere.fi / 040 800 7260 (ma-pe klo 9-12).'
const customerContactEmailHref = 'mailto:varhaiskasvatus.asiakaspalvelu@tampere.fi'
const customerContactTelHref = 'tel:+358408007260'

const isChecked = async (p: Page, e: ElementHandle): Promise<boolean> => {
  let dataQa = await e.getAttribute('data-qa');
  let element = new Radio(p, `[data-qa="${dataQa}"]`)
  return element.checked
}

describe('Citizen applications page', () => {
  test('Applications and ApplicationCreation customizations', async () => {
    await header.selectTab('applications')
    await waitUntilEqual(() => page.innerText('h1 + p'), 'Lapsen huoltaja voi tehdä lapselleen hakemuksen varhaiskasvatukseen ja kerhoon. Huoltajan lasten tiedot haetaan tähän näkymään automaattisesti Väestötietojärjestelmästä.')
    let newApplicationButton = new RawElement(page, `[data-qa="new-application-${enduserChildFixturePorriHatterRestricted.id}"]`)
    await newApplicationButton.click()
    // Check that only daycare and club options are visible
    let applicationRadios = await page.$$('[data-qa^="type-radio-"]')
    expect(applicationRadios.length).toBe(2)
    await waitUntilEqual(() => applicationRadios[0].$eval('label', label => label.innerText), 'Varhaiskasvatus- ja palvelusetelihakemus')
    await waitUntilEqual(() => applicationRadios[1].$eval('label', label => label.innerText), 'Kerhohakemus')
    // Check all texts, including expanding infos
    await page.click('[data-qa="daycare-expanding-info"]')
    await waitUntilEqual(() => page.innerText('[data-qa="daycare-expanding-info-text"]'), 'Varhaiskasvatushakemuksella haetaan paikkaa kunnallisesta päiväkodista tai perhepäivähoidosta, ostopalvelupäiväkodista tai palvelusetelillä tuetusta päiväkodista.')
    await page.click('[data-qa="club-expanding-info"]')
    await waitUntilEqual(() => page.innerText('[data-qa="club-expanding-info-text"]'), 'Kerhohakemuksella haetaan paikkaa kunnallisista tai palvelusetelillä tuetuista kerhoista.')
    await waitUntilEqual(() => page.innerText('[data-qa="application-options-area"] p:last-of-type'), `Huoltaja voi tehdä muutoksia hakemukseen verkkopalvelussa siihen asti, kun hakemus otetaan asiakaspalvelussa käsittelyyn. Tämän jälkeen muutokset tai hakemuksen peruminen on mahdollista ottamalla yhteyttä ${customerContactText}`)
    await waitUntilEqual(() => page.getAttribute('[data-qa="application-options-area"] p:last-of-type a:first-of-type', 'href'), customerContactEmailHref)
    await waitUntilEqual(() => page.getAttribute('[data-qa="application-options-area"] p:last-of-type a:last-of-type', 'href'), customerContactTelHref)
  }),
  test('Daycare application form customizations', async () => {
    await header.selectTab('applications')
    let editorPage = await applicationsPage.createApplication(enduserChildFixturePorriHatterRestricted.id, 'DAYCARE')
    await waitUntilEqual(() => page.innerText('[data-qa="application-child-name-title"] + p'), 'Varhaiskasvatuspaikkaa voi hakea ympäri vuoden. Varhaiskasvatushakemus tulee jättää viimeistään neljä kuukautta ennen hoidon toivottua alkamisajankohtaa. Mikäli varhaiskasvatuksen tarve johtuu työllistymisestä, opinnoista tai koulutuksesta, eikä hoidon tarpeen ajankohtaa ole pystynyt ennakoimaan, on varhaiskasvatuspaikkaa haettava mahdollisimman pian - kuitenkin viimeistään kaksi viikkoa ennen kuin lapsi tarvitsee hoitopaikan.')
    await waitUntilEqual(() => page.innerText('[data-qa="application-child-name-title"] + p + p'), 'Kirjallinen päätös varhaiskasvatuspaikasta lähetetään Suomi.fi-viestit -palveluun. Mikäli haluatte päätöksen sähköisenä tiedoksiantona, teidän tulee ottaa Suomi.fi-viestit -palvelu käyttöön. Palvelusta ja sen käyttöönotosta saatte lisätietoa https://www.suomi.fi/viestit. Mikäli ette ota Suomi.fi-viestit -palvelua käyttöön, päätös lähetetään teille postitse.')
    const startdateInstructions = new RawElement(page, '[data-qa="startdate-instructions"]')
    await startdateInstructions.click()
    await waitUntilEqual(() => page.innerText('[data-qa="startdate-instructions-text"]'), `Toivottua aloituspäivää on mahdollista muuttaa myöhemmäksi niin kauan, kun hakemusta ei ole otettu käsittelyyn. Tämän jälkeen toivotun aloituspäivän muutokset tehdään ottamalla yhteyttä ${customerContactText}`)
    await waitUntilEqual(() => page.getAttribute('[data-qa="startdate-instructions-text"] a:first-of-type', 'href'), customerContactEmailHref)
    await waitUntilEqual(() => page.getAttribute('[data-qa="startdate-instructions-text"] a:last-of-type', 'href'), customerContactTelHref)
    await waitUntilEqual(() => page.innerText('[data-qa="urgent-input"] + div + p'), `Mikäli varhaiskasvatuspaikan tarve johtuu äkillisestä työllistymisestä tai opiskelupaikan saamisesta, tulee paikkaa hakea viimeistään kaksi viikkoa ennen kuin hoidon tarve alkaa. Lisäksi huoltajan tulee ottaa yhteyttä viipymättä ${customerContactText}`)
    await editorPage.setCheckbox('urgent', true)
    // Missing Fileupload for urgent application attachments
    await waitUntilFalse(() => page.isVisible('[data-qa="urgent-file-upload"]'))
    // Check service need options (order, it is working, aso.)
    const partTimeRadio = new RawElement(page, '[data-qa="partTime-input-true"]')
    const fullTimeRadio = new RawElement(page, '[data-qa="partTime-input-false"]')
    await waitUntilTrue(() => fullTimeRadio.visible)
    await waitUntilTrue(() => partTimeRadio.visible)
    let fullTimeOptions = await page.$$('[data-qa^="full-time-option-"]')
    let partTimeOptions = await page.$$('[data-qa^="part-time-option-"]')
    expect(fullTimeOptions.length).toBe(3)
    expect(partTimeOptions.length).toBe(0)
    await waitUntilTrue(() => isChecked(page, fullTimeOptions[0]))
    await waitUntilEqual(() => fullTimeOptions[0].$eval('label', node => node.innerText), 'Kokopäiväinen')
    await waitUntilEqual(() => fullTimeOptions[1].$eval('label', node => node.innerText), 'Kokopäiväinen, 10 pv sopimus')
    await waitUntilEqual(() => fullTimeOptions[2].$eval('label', node => node.innerText), 'Kokopäiväinen, 15 pv sopimus')
    await partTimeRadio.click()
    fullTimeOptions = await page.$$('[data-qa^="full-time-option-"]')
    partTimeOptions = await page.$$('[data-qa^="part-time-option-"]')
    expect(fullTimeOptions.length).toBe(0)
    expect(partTimeOptions.length).toBe(4)
    // After changing to part time the first sub radio option should be selected
    await waitUntilTrue(() => isChecked(page, partTimeOptions[0]))
    await waitUntilEqual(() => partTimeOptions[0].$eval('label', node => node.innerText), 'Osapäiväinen, max 20h viikossa')
    await waitUntilEqual(() => partTimeOptions[1].$eval('label', node => node.innerText), 'Osapäiväinen, max 5h päivässä')
    await waitUntilEqual(() => partTimeOptions[2].$eval('label', node => node.innerText), 'Osapäiväinen, max 5h päivässä; 10 pv sopimus')
    await waitUntilEqual(() => partTimeOptions[3].$eval('label', node => node.innerText), 'Osapäiväinen, max 5h päivässä; 15 pv sopimus')
    await fullTimeRadio.click()
    fullTimeOptions = await page.$$('[data-qa^="full-time-option-"]')
    partTimeOptions = await page.$$('[data-qa^="part-time-option-"]')
    expect(partTimeOptions.length).toBe(0)
    expect(fullTimeOptions.length).toBe(3)
    // After changing to full time the first sub radio option should be selected
    await waitUntilTrue(() => isChecked(page, fullTimeOptions[0]))
    // Check that there are no inputs for start and end date
    await waitUntilFalse(() => page.isVisible('[data-qa="startTime-input"]'))
    await waitUntilFalse(() => page.isVisible('[data-qa="endTime-input"]'))
    // Check texts
    const shiftcareInstructions = new RawElement(page, '[data-qa="shiftcare-instructions"]')
    await shiftcareInstructions.click()
    let shiftcareCheckbox = new Checkbox(page, '[data-qa="shiftCare-input"]')
    await shiftcareCheckbox.click()
    await waitUntilTrue(() => shiftcareCheckbox.checked)
    await waitUntilEqual(() => page.innerText('[data-qa="shiftcare-instructions-text"]'), 'Päiväkodit palvelevat normaalisti arkisin klo 6.00–18.00. Iltahoito on tarkoitettu lapsille, jotka vanhempien työn tai tutkintoon johtavan opiskelun vuoksi tarvitsevat säännöllisesti hoitoa klo 18.00 jälkeen. Iltahoitoa tarjoavat päiväkodit aukeavat tarvittaessa klo 5.30 ja menevät kiinni viimeistään klo 22.30. Osa iltahoitoa antavista päiväkodeista on auki myös viikonloppuisin. Vuorohoito on tarkoitettu lapsille, joiden vanhemmat tekevät vuorotyötä ja lapsen hoitoon sisältyy myös öitä.')
    await waitUntilEqual(() => page.innerText('[data-qa="shiftcare-attachments-message"]'), 'Ilta- ja vuorohoito on tarkoitettu lapsille, jotka vanhempien työn tai tutkintoon johtavan opiskelun vuoksi tarvitsevat ilta- ja vuorohoitoa. Hakemuksen liitteeksi on toimitettava vanhempien osalta työnantajan todistus vuorotyöstä tai opiskelusta johtuvasta ilta- tai vuorohoidon tarpeesta.')
    const assistanceNeedInstructionsDaycare = new RawElement(page, '[data-qa="assistanceNeedInstructions-DAYCARE"]')
    await assistanceNeedInstructionsDaycare.click()
    await waitUntilEqual(() => page.innerText('[data-qa="assistanceNeedInstructions-DAYCARE-text"]'), 'Tehostettua tai erityistä tukea annetaan lapselle heti tarpeen ilmettyä. Mikäli lapsella on olemassa tuen tarpeesta asiantuntijalausunto, tämä tulee ilmoittaa varhaiskasvatushakemuksella. Tukitoimet toteutuvat lapsen arjessa osana varhaiskasvatuksen toimintaa. Tampereen varhaiskasvatuksesta otetaan erikseen yhteyttä hakemuksen jättämisen jälkeen, jos lapsella on tuen tarve.')
    await editorPage.openSection('contactInfo')
    let contactInfoText = new RawElement(page, '[data-qa="contactInfo-section"] p[data-qa="contact-info-text"]')
    await waitUntilEqual(() => contactInfoText.innerText, 'Henkilötiedot on haettu väestötiedoista, eikä niitä voi muuttaa tällä hakemuksella. Jos henkilötiedoissa on virheitä, päivitäthän tiedot Digi- ja Väestötietoviraston sivuilla. Mikäli osoitteenne on muuttumassa, voit lisätä tulevan osoitteen erilliseen kohtaan hakemuksella; lisää tuleva osoite sekä lapselle että huoltajalle. Virallisena osoitetietoa pidetään vasta, kun se on päivittynyt väestötietojärjestelmään. Varhaiskasvatus- ja palvelusetelipäätös sekä tieto avoimen varhaiskasvatuksen kerhopaikasta toimitetaan automaattisesti myös eri osoitteessa asuvalle väestötiedoista löytyvälle huoltajalle.')
    await waitUntilEqual(() => contactInfoText.find('a').getAttribute('href'), 'https://dvv.fi/henkiloasiakkaat')
    const childFutureAddrInfo = new RawElement(page, '[data-qa="child-future-address-info"]')
    await childFutureAddrInfo.click()
    await waitUntilEqual(() => page.innerText('[data-qa="child-future-address-info-text"]'), 'Tampereen varhaiskasvatuksessa virallisena osoitteena pidetään väestötiedoista saatavaa osoitetta. Osoite väestötiedoissa muuttuu hakijan tehdessä muuttoilmoituksen postiin tai maistraattiin.')
    const guardianFutureAddrInfo = new RawElement(page, '[data-qa="guardian-future-address-info"]')
    await guardianFutureAddrInfo.click()
    await waitUntilEqual(() => page.innerText('[data-qa="guardian-future-address-info-text"]'), 'Tampereen varhaiskasvatuksessa virallisena osoitteena pidetään väestötiedoista saatavaa osoitetta. Osoite väestötiedoissa muuttuu hakijan tehdessä muuttoilmoituksen postiin tai maistraattiin.')
    await editorPage.openSection('fee')
    await waitUntilEqual(() => page.innerText('[data-qa="fee-section"] p:first-of-type'), 'Kunnallisen varhaiskasvatuksen asiakasmaksu ja palvelusetelin omavastuuosuus perustuu varhaiskasvatuksen asiakasmaksuista annettuun lakiin (Laki varhaiskasvatuksen asiakasmaksuista (1503/2016)). Asiakasmaksu määräytyy perheen koon, palveluntarpeen sekä bruttotulojen mukaan. Uusien asiakkaiden tulee täyttää asiakasmaksulomake ja toimittaa tarvittavat liitteet Varhaiskasvatuksen asiakasmaksuihin viimeistään kuukauden kuluessa hoidon alkamisesta.')
    await waitUntilEqual(() => page.innerText('[data-qa="fee-section"] p:last-of-type'), 'Lisätietoa varhaiskasvatuksen asiakasmaksuista löydät Tampereen kaupungin sivuilta')
    await waitUntilEqual(() => page.getAttribute('[data-qa="fee-section"] p:last-of-type a', 'href'), 'https://www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus/asiakasmaksut.html')
    await editorPage.openSection('additionalDetails')
    const dietInfo = new RawElement(page, '[data-qa="diet-expanding-info"]')
    await dietInfo.click()
    await waitUntilEqual(() => page.innerText('[data-qa="diet-expanding-info-text"]'), 'Erityisruokavaliosta huoltaja toimittaa varhaiskasvatuspaikkaan lääkärin tai ravitsemusterapeutin täyttämän ja allekirjoittaman Selvitys erityisruokavaliosta -lomakkeen, joka on määräaikainen.')
    await waitUntilEqual(() => page.getAttribute('[data-qa="diet-expanding-info-text"] a', 'href'), 'https://www.tampere.fi/sosiaali-ja-terveyspalvelut/erityisruokavaliot.html')
    // Click cancel and check text for an existing application
    let cancelButton = new RawElement(page, '[data-qa="cancel-application-button"]')
    await cancelButton.click()
    await header.selectTab('applications')
    let newApplicationButton = new RawElement(page, `[data-qa="new-application-${enduserChildFixturePorriHatterRestricted.id}"]`)
    await newApplicationButton.click()
    let daycareApplicationRadio = new RawElement(page, '[data-qa="type-radio-DAYCARE"]')
    await daycareApplicationRadio.click()
    await waitUntilEqual(() => page.innerText('[data-qa="duplicate-application-notification"]'), 'Lapsella on jo samantyyppinen, keskeneräinen hakemus. Palaa Hakemukset-näkymään ja muokkaa olemassa olevaa hakemusta tai ota yhteyttä Varhaiskasvatuksen asiakaspalveluun.')
  }),
  test('Club application form customizations', async () => {
    await header.selectTab('applications')
    let editorPage = await applicationsPage.createApplication(enduserChildFixturePorriHatterRestricted.id, 'CLUB')
    await waitUntilEqual(() => page.innerText('[data-qa="application-child-name-title"] + p'), 'Kerhopaikkaa voi hakea ympäri vuoden. Kerhohakemuksella voi hakea kunnallista tai palvelusetelillä tuettua kerhopaikkaa. Kirjallinen ilmoitus kerhopaikasta lähetään Suomi.fi-viestit -palveluun. Mikäli haluatte ilmoituksen sähköisenä tiedoksiantona, teidän tulee ottaa Suomi.fi-viestit -palvelu käyttöön. Palvelusta ja sen käyttöönotosta saatte lisätietoa https://www.suomi.fi/viestit. Mikäli ette ota Suomi.fi-viestit -palvelua käyttöön, ilmoitus kerhopaikasta lähetetään teille postitse. Paikka myönnetään yhdeksi toimintakaudeksi kerrallaan.')
    await waitUntilEqual(() => page.innerText('[data-qa="application-child-name-title"] + p + p'), 'Kerhohakemus kohdistuu yhdelle kerhon toimintakaudelle. Kyseisen kauden päättyessä hakemus poistetaan järjestelmästä.')
    const wasOnDaycareInfo = new RawElement(page, '[data-qa="wasOnDaycare-info"]')
    await wasOnDaycareInfo.click()
    await waitUntilEqual(() => page.innerText('[data-qa="wasOnDaycare-info-text"]'), 'Jos lapsi on ollut kunnallisessa päiväkodissa tai perhepäivähoidossa ja hän luopuu paikastaan kerhon alkaessa, hänellä on suurempi mahdollisuus saada kerhopaikka.')
    const wasOnClubCareInfo = new RawElement(page, '[data-qa="wasOnClubCare-info"]')
    await wasOnClubCareInfo.click()
    await waitUntilEqual(() => page.innerText('[data-qa="wasOnClubCare-info-text"]'), 'Jos lapsi on ollut kerhossa jo edellisen toimintakauden aikana, hänellä on suurempi mahdollisuus saada paikka kerhosta myös tulevana toimintakautena.')
    const assistanceNeedInstructionsClub = new RawElement(page, '[data-qa="assistanceNeedInstructions-CLUB"]')
    await assistanceNeedInstructionsClub.click()
    await waitUntilEqual(() => page.innerText('[data-qa="assistanceNeedInstructions-CLUB-text"]'), 'Jos lapsella on tuen tarve, Tampereen varhaiskasvatuksesta otetaan yhteyttä hakemuksen jättämisen jälkeen.')
    await editorPage.openSection('contactInfo')
    await waitUntilEqual(() => page.innerText('[data-qa="contactInfo-section"] p[data-qa="contact-info-text"]'), 'Henkilötiedot on haettu väestötiedoista, eikä niitä voi muuttaa tällä hakemuksella. Jos henkilötiedoissa on virheitä, päivitäthän tiedot Digi- ja Väestötietoviraston sivuilla. Mikäli osoitteenne on muuttumassa, voit lisätä tulevan osoitteen erilliseen kohtaan hakemuksella; lisää tuleva osoite sekä lapselle että huoltajalle. Virallisena osoitetietoa pidetään vasta, kun se on päivittynyt väestötietojärjestelmään. Varhaiskasvatus- ja palvelusetelipäätös sekä tieto avoimen varhaiskasvatuksen kerhopaikasta toimitetaan automaattisesti myös eri osoitteessa asuvalle väestötiedoista löytyvälle huoltajalle.')
    const childFutureAddrInfo = new RawElement(page, '[data-qa="child-future-address-info"]')
    await childFutureAddrInfo.click()
    await waitUntilEqual(() => page.innerText('[data-qa="child-future-address-info-text"]'), 'Tampereen varhaiskasvatuksessa virallisena osoitteena pidetään väestötiedoista saatavaa osoitetta. Osoite väestötiedoissa muuttuu hakijan tehdessä muuttoilmoituksen postiin tai maistraattiin.')
    const guardianFutureAddrInfo = new RawElement(page, '[data-qa="guardian-future-address-info"]')
    await guardianFutureAddrInfo.click()
    await waitUntilEqual(() => page.innerText('[data-qa="guardian-future-address-info-text"]'), 'Tampereen varhaiskasvatuksessa virallisena osoitteena pidetään väestötiedoista saatavaa osoitetta. Osoite väestötiedoissa muuttuu hakijan tehdessä muuttoilmoituksen postiin tai maistraattiin.')
  })
})
