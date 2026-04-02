// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import config from 'e2e-test/config'
import { test, expect } from 'e2e-test/playwright'

test.describe('Citizen footer', () => {
  test('Tampere footer label', async ({ evaka: page }) => {
    await page.goto(config.enduserUrl)
    await expect.poll(
      () => page.find('[data-qa="footer-citylabel"]').text)
        .toEqual(
      '© Tampereen kaupunki'
    )
  })

  test('Tampere policy link', async ({ evaka: page }) => {
    await page.goto(config.enduserUrl)
    await expect.poll(
      () => page.find('[data-qa="footer-policy-link"]').getAttribute('href'))
    .toEqual(
      'https://www.tampere.fi/tietosuojaselosteet'
    )
  })

  test('Tampere feedback link', async ({ evaka: page }) => {
    await page.goto(config.enduserUrl)
    await expect.poll(
      () => page.find('[data-qa="footer-feedback-link"]').getAttribute('href'))
    .toEqual(
      'https://www.tampere.fi/palaute'
    )
  })
})
