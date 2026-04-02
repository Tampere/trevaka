// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import config from 'e2e-test/config'
import { test, expect } from 'e2e-test/playwright'

test.describe('Citizen map page', () => {
  test('Unit type filters', async ({ evaka: page }) => {
    await page.goto(`${config.enduserUrl}/map`)
    await expect(page.find('[data-qa="map-filter-PRESCHOOL"]')).toBeVisible()
    await expect(page.find('[data-qa="map-filter-DAYCARE"]')).toBeVisible()
    await expect(page.find('[data-qa="map-filter-CLUB"]')).toBeVisible()
  })

  test('Map main info', async ({ evaka: page }) => {
    await page.goto(`${config.enduserUrl}/map`)
    let mapMainInfo = page.find('[data-qa="map-main-info"]')
    await expect.poll(
      () => mapMainInfo.text)
        .toEqual(
      'Tässä näkymässä voit hakea kartalta kaikki Tampereen varhaiskasvatusyksiköt sekä kerhot. Kartalta löytyvät myös seudulliset palveluseteliyksiköt ja -kerhot. Esiopetusta järjestetään pääsääntöisesti kouluilla.'
    )
    let privateUnitInfo = mapMainInfo.findAll('span')
    await expect.poll(() => privateUnitInfo.first().text).toEqual('')
  })
})
