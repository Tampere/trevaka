// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import config from 'e2e-test/config'
import { test } from 'e2e-test/playwright'
import { waitUntilEqual } from 'e2e-test/utils'

test.describe('Citizen map page', () => {
  test('Unit type filters', async ({ evaka: page }) => {
    await page.goto(`${config.enduserUrl}/map`)
    await page.find('[data-qa="map-filter-PRESCHOOL"]').waitUntilVisible()
    await page.find('[data-qa="map-filter-DAYCARE"]').waitUntilVisible()
    await page.find('[data-qa="map-filter-CLUB"]').waitUntilVisible()
  })

  test('Map main info', async ({ evaka: page }) => {
    await page.goto(`${config.enduserUrl}/map`)
    let mapMainInfo = page.find('[data-qa="map-main-info"]')
    await waitUntilEqual(
      () => mapMainInfo.text,
      'Tässä näkymässä voit hakea kartalta kaikki Tampereen varhaiskasvatusyksiköt sekä kerhot. Kartalta löytyvät myös seudulliset palveluseteliyksiköt ja -kerhot. Esiopetusta järjestetään pääsääntöisesti kouluilla.'
    )
    let privateUnitInfo = mapMainInfo.findAll('span')
    await waitUntilEqual(() => privateUnitInfo.first().text, '')
  })
})
