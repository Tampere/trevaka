// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import config from 'e2e-test/config'
import { test, expect } from 'e2e-test/playwright'
import CitizenHeader from 'e2e-test/pages/citizen/citizen-header'
import CitizenIncomePage from 'e2e-test/pages/citizen/citizen-income'
import { enduserLogin } from 'e2e-test/utils/user'
import {
  resetDatabaseForE2ETests
} from '../../common/tampere-dev-api'
import { testAdult, Fixture } from 'e2e-test/dev-api/fixtures'

let header: CitizenHeader
let incomePage: CitizenIncomePage

test.describe('Citizen income page', () => {
  test.beforeEach(async ({ evaka: page }) => {
    await resetDatabaseForE2ETests()
    const adult = await Fixture.person(testAdult)
      .saveAdult({ updateMockVtjWithDependants: [] })
    await page.goto(config.enduserUrl)
    await enduserLogin(page, adult, '/')
    header = new CitizenHeader(page)
    incomePage = new CitizenIncomePage(page, 'desktop')
  })

  test('Text customizations', async ({ evaka: page }) => {
    await header.selectTab('income')

    let incomeDescriptionP1 = page.find('[data-qa="income-description-p1"]')
    await expect(incomeDescriptionP1).toBeVisible()
    await expect.poll(
      () => incomeDescriptionP1.text)
        .toEqual(
      'Tällä sivulla voit lähettää selvitykset varhaiskasvatusmaksuun vaikuttavista tuloistasi. Voit myös tarkastella palauttamiasi tuloselvityksiä ja muokata tai poistaa niitä kunnes viranomainen on käsitellyt tiedot. Lomakkeen käsittelyn jälkeen voit päivittää tulotietojasi toimittamalla uuden lomakkeen.'
    )

    let incomeDescriptionP2 = page.find('[data-qa="income-description-p2"]')
    await expect(incomeDescriptionP2).toBeVisible()
    await expect.poll(
      () => incomeDescriptionP2.text)
    .toEqual(
      'Molempien samassa taloudessa asuvien aikuisten tulee toimittaa omat erilliset tuloselvitykset.'
    )

    let incomeDescriptionP3 = page.find('[data-qa="income-description-p3"]')
    await expect(incomeDescriptionP3).toBeVisible()
    await expect.poll(
      () => incomeDescriptionP3.text)
        .toEqual(
      'Kunnallisen varhaiskasvatuksen asiakasmaksut määräytyvät prosenttiosuutena perheen bruttotuloista. Maksut vaihtelevat perheen koon ja tulojen sekä varhaiskasvatusajan mukaan.'
    )

    let incomeDescriptionP4 = page.find('[data-qa="income-description-p4"]')
    await expect(incomeDescriptionP4).toBeVisible()
    await expect.poll(
      () => incomeDescriptionP4.text)
    .toEqual(
      'Lisätietoja asiakasmaksuista'
    )

    await incomePage.createNewIncomeStatement()

    let incomeFormDescriptionP1 = page.find('[data-qa="income-formDescription-p1"]')
    await expect(incomeFormDescriptionP1).toBeVisible()
    await expect.poll(
      () => incomeFormDescriptionP1.text)
    .toEqual(
      'Tuloselvitys liitteineen palautetaan kuukauden kuluessa varhaiskasvatuksen aloittamisesta. Maksu voidaan määrätä puutteellisilla tulotiedoilla korkeimpaan maksuun. Puutteellisia tulotietoja ei korjata takautuvasti oikaisuvaatimusajan jälkeen.'
    )
    let incomeFormDescriptionItem3 = page.find('[data-qa="income-formDescription-ul"] li:nth-child(3)')
    await expect(incomeFormDescriptionItem3).toBeVisible()
    await expect.poll(
      () => incomeFormDescriptionItem3.text)
    .toEqual(
      'Katso voimassaolevat tulorajat tästä.'
    )
    let incomeFormDescriptionLink = incomeFormDescriptionItem3.find('a')
    await expect.poll(
      () => incomeFormDescriptionLink.getAttribute('href'))
    .toEqual(
      'https://www.tampere.fi/varhaiskasvatusasiakasmaksut'
    )
  })
})
