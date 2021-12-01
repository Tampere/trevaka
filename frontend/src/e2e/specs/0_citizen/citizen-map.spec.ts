// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import { newBrowserContext } from 'e2e-playwright/browser'
import config from 'e2e-test-common/config'
import { Page } from 'playwright'
import { waitUntilTrue, waitUntilEqual } from 'e2e-playwright/utils'
import { RawElementDEPRECATED as RawElement } from 'e2e-playwright/utils/element'

let page: Page

beforeEach(async () => {
  page = await (await newBrowserContext()).newPage()
  await page.goto(config.enduserUrl)
})
afterEach(async () => {
  await page.close()
})

describe('Citizen map page', () => {
  test('Unit type filters', async () => {
    let preschoolFilter = await page.$$('[data-qa="map-filter-preschool"]')
    expect(preschoolFilter).toEqual([])

    let daycareFilter = new RawElement(page, '[data-qa="map-filter-daycare"]')
    await waitUntilTrue(() => daycareFilter.visible)

    let clubFilter = new RawElement(page, '[data-qa="map-filter-club"]')
    await waitUntilTrue(() => clubFilter.visible)
  }),
  test('Map main info', async () => {
    let mapMainInfo = new RawElement(page, '[data-qa="map-main-info"]')
    await waitUntilEqual(() => mapMainInfo.innerText, 'Tässä näkymässä voit hakea kartalta kaikki Tampereen varhaiskasvatusyksiköt sekä kerhot. Kartalta löytyvät myös seudulliset palveluseteliyksiköt ja -kerhot.')

    let privateUnitInfo = mapMainInfo.find('span')
    await waitUntilEqual(() => privateUnitInfo.innerText, '')
  })
})
