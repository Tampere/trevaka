// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import config from 'e2e-test/config'
import { test } from 'e2e-test/playwright'
import { waitUntilEqual } from 'e2e-test/utils'

test.describe('Citizen footer', () => {
  test('Tampere footer label', async ({ evaka: page }) => {
    await page.goto(config.enduserUrl)
    await waitUntilEqual(
      () => page.find('[data-qa="footer-citylabel"]').text,
      '© Tampereen kaupunki'
    )
  })

  test('Tampere policy link', async ({ evaka: page }) => {
    await page.goto(config.enduserUrl)
    await waitUntilEqual(
      () => page.find('[data-qa="footer-policy-link"]').getAttribute('href'),
      'https://www.tampere.fi/tietosuojaselosteet'
    )
  })

  test('Tampere feedback link', async ({ evaka: page }) => {
    await page.goto(config.enduserUrl)
    await waitUntilEqual(
      () => page.find('[data-qa="footer-feedback-link"]').getAttribute('href'),
      'https://www.tampere.fi/palaute'
    )
  })
})
