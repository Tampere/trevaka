// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import config from 'e2e-test-common/config'
import { waitUntilEqual } from 'e2e-playwright/utils'
import { Page } from 'e2e-playwright/utils/page'

let page: Page

beforeEach(async () => {
  page = await Page.open()
  await page.goto(config.enduserUrl)
})
afterEach(async () => {
  await page.close()
})

describe('Citizen footer', () => {
  test('Tampere footer label', async () => {
    await waitUntilEqual(
      () => page.find('[data-qa="footer-citylabel"]').innerText,
      '© Tampereen kaupunki'
    )
  })
  test('Tampere policy link', async () => {
    await waitUntilEqual(
      () => page.find('[data-qa="footer-policy-link"]').getAttribute('href'),
      'https://www.tampere.fi/tietosuoja'
    )
  })
  test('Tampere feedback link', async () => {
    await waitUntilEqual(
      () => page.find('[data-qa="footer-feedback-link"]').getAttribute('href'),
      'https://www.tampere.fi/palaute'
    )
  })
})
