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

describe('Citizen header', () => {
  test('Tampere logo', async () => {
    let headerLogo = new RawElement(page, '[data-qa="header-city-logo"]')
    await waitUntilEqual(
      async () => (await headerLogo.getAttribute('alt')),
      'Tampere logo'
    )
  })
})
