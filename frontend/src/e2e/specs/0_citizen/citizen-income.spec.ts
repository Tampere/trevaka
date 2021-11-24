// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import { newBrowserContext } from 'e2e-playwright/browser'
import config from 'e2e-test-common/config'
import { Page } from 'playwright'
import { waitUntilEqual } from 'e2e-playwright/utils'
import { RawElement } from 'e2e-playwright/utils/element'
import CitizenHeader from 'e2e-playwright/pages/citizen/citizen-header'
import CitizenIncomePage from 'e2e-playwright/pages/citizen/citizen-income'
import { enduserLogin } from 'e2e-playwright/utils/user'
import {
  resetDatabaseForE2ETests
} from '../../common/tampere-dev-api'
  

let page: Page
let header: CitizenHeader
let incomePage: CitizenIncomePage

beforeEach(async () => {
  await resetDatabaseForE2ETests()
  page = await (await newBrowserContext()).newPage()
  await page.goto(config.enduserUrl)
  await enduserLogin(page)
  header = new CitizenHeader(page)
  incomePage = new CitizenIncomePage(page)
})
afterEach(async () => {
  await page.close()
})

describe('Citizen income page', () => {
  test('Text customizations', async () => {
    await header.selectTab('income')

    let incomeDescriptionP1 = new RawElement(page, '[data-qa="income-description-p1"]')
    await incomeDescriptionP1.waitUntilVisible()
    await waitUntilEqual(() => incomeDescriptionP1.innerText, 'Tällä sivulla voit lähettää selvitykset varhaiskasvatusmaksuun vaikuttavista tuloistasi. Voit myös tarkastella palauttamiasi tuloselvityksiä ja muokata tai poistaa niitä kunnes viranomainen on käsitellyt tiedot. Lomakkeen käsittelyn jälkeen voit päivittää tulotietojasi toimittamalla uuden lomakkeen.')

    let incomeDescriptionP2 = new RawElement(page, '[data-qa="income-description-p2"]')
    await incomeDescriptionP2.waitUntilVisible()
    await waitUntilEqual(() => incomeDescriptionP2.innerText, 'Molempien samassa taloudessa asuvien aikuisten tulee toimittaa omat erilliset tuloselvitykset.')

    let incomeDescriptionP3 = new RawElement(page, '[data-qa="income-description-p3"]')
    await incomeDescriptionP3.waitUntilVisible()
    await waitUntilEqual(() => incomeDescriptionP3.innerText, 'Kunnallisen varhaiskasvatuksen asiakasmaksut määräytyvät prosenttiosuutena perheen bruttotuloista. Maksut vaihtelevat perheen koon ja tulojen sekä varhaiskasvatusajan mukaan.')

    let incomeDescriptionP4 = new RawElement(page, '[data-qa="income-description-p4"]')
    await incomeDescriptionP4.waitUntilVisible()
    await waitUntilEqual(() => incomeDescriptionP4.innerText, 'Lisätietoja asiakasmaksuista')

    await incomePage.createNewIncomeStatement()

    let incomeFormDescriptionP1 = new RawElement(page, '[data-qa="income-formDescription-p1"]')
    await incomeFormDescriptionP1.waitUntilVisible()
    await waitUntilEqual(() => incomeFormDescriptionP1.innerText, 'Tuloselvitys liitteineen palautetaan kuukauden kuluessa varhaiskasvatuksen aloittamisesta. Maksu voidaan määrätä puutteellisilla tulotiedoilla korkeimpaan maksuun. Puutteellisia tulotietoja ei korjata takautuvasti oikaisuvaatimusajan jälkeen.')
    let incomeFormDescriptionItem3 = new RawElement(page, '[data-qa="income-formDescription-ul"] li:nth-child(3)')
    await incomeFormDescriptionItem3.waitUntilVisible()
    await waitUntilEqual(() => incomeFormDescriptionItem3.innerText, 'Katso voimassaolevat tulorajat tästä.')
    let incomeFormDescriptionLink = incomeFormDescriptionItem3.find('a')
    await waitUntilEqual(() => incomeFormDescriptionLink.getAttribute('href'), 'https://www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus/asiakasmaksut.html')
  })
})
