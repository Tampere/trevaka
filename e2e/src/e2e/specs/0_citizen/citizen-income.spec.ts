// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import config from 'e2e-test/config'
import { Page } from 'e2e-test/utils/page'
import { waitUntilEqual } from 'e2e-test/utils'
import CitizenHeader from 'e2e-test/pages/citizen/citizen-header'
import CitizenIncomePage from 'e2e-test/pages/citizen/citizen-income'
import { enduserLogin } from 'e2e-test/utils/user'
import {
  resetDatabaseForE2ETests
} from '../../common/tampere-dev-api'
import { testAdult, Fixture } from 'e2e-test/dev-api/fixtures'

let page: Page
let header: CitizenHeader
let incomePage: CitizenIncomePage

beforeEach(async () => {
  await resetDatabaseForE2ETests()
  const adult = await Fixture.person(testAdult)
    .saveAdult({ updateMockVtjWithDependants: [] })
  page = await Page.open()
  await page.goto(config.enduserUrl)
  await enduserLogin(page, adult)
  header = new CitizenHeader(page)
  incomePage = new CitizenIncomePage(page, 'desktop')
})
afterEach(async () => {
  await page.close()
})

describe('Citizen income page', () => {
  test('Text customizations', async () => {
    await header.selectTab('income')

    let incomeDescriptionP1 = page.find('[data-qa="income-description-p1"]')
    await incomeDescriptionP1.waitUntilVisible()
    await waitUntilEqual(
      () => incomeDescriptionP1.text,
      'Tällä sivulla voit lähettää selvitykset varhaiskasvatusmaksuun vaikuttavista tuloistasi. Voit myös tarkastella palauttamiasi tuloselvityksiä ja muokata tai poistaa niitä kunnes viranomainen on käsitellyt tiedot. Lomakkeen käsittelyn jälkeen voit päivittää tulotietojasi toimittamalla uuden lomakkeen.'
    )

    let incomeDescriptionP2 = page.find('[data-qa="income-description-p2"]')
    await incomeDescriptionP2.waitUntilVisible()
    await waitUntilEqual(
      () => incomeDescriptionP2.text,
      'Molempien samassa taloudessa asuvien aikuisten tulee toimittaa omat erilliset tuloselvitykset.'
    )

    let incomeDescriptionP3 = page.find('[data-qa="income-description-p3"]')
    await incomeDescriptionP3.waitUntilVisible()
    await waitUntilEqual(
      () => incomeDescriptionP3.text,
      'Kunnallisen varhaiskasvatuksen asiakasmaksut määräytyvät prosenttiosuutena perheen bruttotuloista. Maksut vaihtelevat perheen koon ja tulojen sekä varhaiskasvatusajan mukaan.'
    )

    let incomeDescriptionP4 = page.find('[data-qa="income-description-p4"]')
    await incomeDescriptionP4.waitUntilVisible()
    await waitUntilEqual(
      () => incomeDescriptionP4.text,
      'Lisätietoja asiakasmaksuista'
    )

    await incomePage.createNewIncomeStatement()

    let incomeFormDescriptionP1 = page.find('[data-qa="income-formDescription-p1"]')
    await incomeFormDescriptionP1.waitUntilVisible()
    await waitUntilEqual(
      () => incomeFormDescriptionP1.text,
      'Tuloselvitys liitteineen palautetaan kuukauden kuluessa varhaiskasvatuksen aloittamisesta. Maksu voidaan määrätä puutteellisilla tulotiedoilla korkeimpaan maksuun. Puutteellisia tulotietoja ei korjata takautuvasti oikaisuvaatimusajan jälkeen.'
    )
    let incomeFormDescriptionItem3 = page.find('[data-qa="income-formDescription-ul"] li:nth-child(3)')
    await incomeFormDescriptionItem3.waitUntilVisible()
    await waitUntilEqual(
      () => incomeFormDescriptionItem3.text,
      'Katso voimassaolevat tulorajat tästä.'
    )
    let incomeFormDescriptionLink = incomeFormDescriptionItem3.find('a')
    await waitUntilEqual(
      () => incomeFormDescriptionLink.getAttribute('href'),
      'https://www.tampere.fi/varhaiskasvatusasiakasmaksut'
    )
  })
})
