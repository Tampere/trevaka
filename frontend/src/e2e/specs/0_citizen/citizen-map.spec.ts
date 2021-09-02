import { newBrowserContext } from 'e2e-playwright/browser'
import config from 'e2e-test-common/config'
import { Page } from 'playwright'
import { waitUntilTrue, waitUntilEqual } from 'e2e-playwright/utils'
import { RawElement } from 'e2e-playwright/utils/element'
import CitizenMapPage from 'e2e-playwright/pages/citizen/citizen-map'
import { Daycare } from 'e2e-test-common/dev-api/types'
import {
  putDigitransitAutocomplete,
  DigitransitFeature
} from 'e2e-test-common/dev-api'

let page: Page
let mapPage: CitizenMapPage

beforeEach(async () => {
  page = await (await newBrowserContext()).newPage()
  await page.goto(config.enduserUrl)
  mapPage = new CitizenMapPage(page)
})
afterEach(async () => {
  await page.close()
})

const testStreet: DigitransitFeature = {
  geometry: {
    coordinates: [23.84147, 61.49547]
  },
  properties: {
    name: 'Jukolankatu 7',
    postalcode: '33560',
    locality: 'Tampere'
  }
}

const teeriDaycare: Daycare = {
  name: 'Steinerpäiväkoti Teeri',
  id: '3f669ace-a1bc-11eb-b67d-2b82668ce31c',
  type: ['CENTRE'],
  careAreaId: '',
  costCenter: '',
  streetAddress: 'Jukolankatu 2 D 28',
  postalCode: '',
  postOffice: '',
  decisionDaycareName: '',
  decisionPreschoolName: '',
  decisionHandler: '',
  decisionHandlerAddress: '',
  providerType: 'PRIVATE_SERVICE_VOUCHER',
  roundTheClock: false,
  enabledPilotFeatures: []
}

describe('Citizen map page', () => {
  test('Unit type filters', async () => {
    await waitUntilTrue(() => mapPage.daycareFilter.visible)
    await waitUntilTrue(() => mapPage.clubFilter.visible)
    let preschoolFilter = await page.$('[data-qa="map-filter-preschool"]')
    expect(preschoolFilter).toEqual(null)
  }),
  test('Map main info', async () => {
    let mapMainInfo = new RawElement(page, '[data-qa="map-main-info"]')
    await waitUntilEqual(() => mapMainInfo.innerText, 'Tässä näkymässä voit hakea kartalta kaikki Tampereen varhaiskasvatusyksiköt sekä kerhot. Kartalta löytyvät myös seudulliset palveluseteliyksiköt ja -kerhot.')
    let privateUnitInfo = mapMainInfo.find('span')
    await waitUntilEqual(() => privateUnitInfo.innerText, '')
  })
  test('Map search input placeholder', async () => {
    await waitUntilEqual(() => mapPage.searchInput.find('[class$="placeholder"]').innerText, 'Esim. Jukolankatu 7 tai Amurin päiväkoti')
  })
  test('Service voucher link - MapPopup', async () => {
    await mapPage.map.markerFor(teeriDaycare).click()
    let mapPopup = await mapPage.map.popupFor(teeriDaycare)
    await waitUntilTrue(() => mapPopup.visible)
    await waitUntilTrue(() => mapPopup.find('a[href="https://www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus/paivakodit.html#palvelusetelipaivakodit"]').visible)
  })
  test('Service voucher link - UnitDetailsPanel', async () => {
    await mapPage.listItemFor(teeriDaycare).click()
    await waitUntilTrue(() => mapPage.unitDetailsPanel.find('a[href="https://www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus/paivakodit.html#palvelusetelipaivakodit"]').visible)
  })
  test('Reittiopas link - UnitDetailsPanel', async () => {
    await putDigitransitAutocomplete({
      features: [testStreet]
    })
    await mapPage.searchInput.type('Jukolankatu')
    await mapPage.searchInput.clickAddressResult('Jukolankatu 7')
    await waitUntilTrue(() => mapPage.map.addressMarker.visible)
    await page.click('[data-qa="toggle-show-more-units"]')
    let teeriDaycareItem = mapPage.listItemFor(teeriDaycare)
    await waitUntilTrue(() => teeriDaycareItem.visible)
    await teeriDaycareItem.click()
    await waitUntilEqual(
      () => mapPage.unitDetailsPanel.name,
      teeriDaycare.name
    )
    await waitUntilTrue(() => mapPage.unitDetailsPanel.find('a[href^="https://reittiopas.tampere.fi/reitti/"]').visible)
  })
})
