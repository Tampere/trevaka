// SPDX-FileCopyrightText: 2017-2021 City of Espoo
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import { Page } from 'playwright'
import config from 'e2e-test-evaka-common/config'
import { newBrowserContext } from 'e2e-playwright-evaka/browser'
import { waitUntilEqual } from 'e2e-playwright-evaka/utils'
import { RawElement } from 'e2e-playwright-evaka/utils/element'

let page: Page

beforeEach(async () => {
  page = await (await newBrowserContext()).newPage()
  await page.goto(config.enduserUrl)
})
afterEach(async () => {
  await page.close()
})

describe('Citizen header', () => {
  test('Tampere logo', async () => {
    let footerPolicyLink = new RawElement(page, '[data-qa="header-city-logo"]')
    await waitUntilEqual(
      async () => (await footerPolicyLink.getAttribute('alt')),
      'Tampere logo'
    )
  })
})
