import { Page } from 'playwright'
import config from 'e2e-test-common/config'
import { newBrowserContext } from 'e2e-playwright/browser'
import { waitUntilEqual } from 'e2e-playwright/utils'
import { RawElement } from 'e2e-playwright/utils/element'

let page: Page

beforeEach(async () => {
  page = await (await newBrowserContext()).newPage()
  await page.goto(config.enduserUrl)
})
afterEach(async () => {
  await page.close()
})

describe('Citizen footer', () => {
  test('Tampere footer label', async () => {
    let footerPolicyLink = new RawElement(page, '[data-qa="footer-citylabel"]')
    await waitUntilEqual(
      async () => (await footerPolicyLink.innerText),
      '© Tampereen kaupunki'
    )
  }),
  test('Tampere policy link', async () => {
    let footerPolicyLink = new RawElement(page, '[data-qa="footer-policy-link"]')
    await waitUntilEqual(
      async () => (await footerPolicyLink.getAttribute('href')),
      'https://www.tampere.fi/tampereen-kaupunki/yhteystiedot-ja-asiointi/verkkoasiointi/tietosuoja/tietosuojaselosteet.html'
    )
  }),
  test('Tampere feedback link', async () => {
    let footerFeedbackLink = new RawElement(page, '[data-qa="footer-feedback-link"]')
    await waitUntilEqual(
      async () => (await footerFeedbackLink.getAttribute('href')),
      'https://www.tampere.fi/palaute.html.stx'
    )
  })
})
