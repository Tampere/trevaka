// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import HelsinkiDateTime from 'lib-common/helsinki-date-time'
import TimeRange from 'lib-common/time-range'
import LocalTime from 'lib-common/local-time'
import config from 'e2e-test/config'
import { resetDatabaseForE2ETests } from '../../common/tampere-dev-api'
import { createDaycarePlacements } from 'e2e-test/generated/api-clients'
import {
  createDaycarePlacementFixture,
  daycareFixture,
  enduserChildFixturePorriHatterRestricted, enduserGuardianFixture,
  Fixture,
  uuidv4
} from 'e2e-test/dev-api/fixtures'
import { PersonDetail } from 'e2e-test/dev-api/types'
import CitizenCalendarPage from 'e2e-test/pages/citizen/citizen-calendar'
import CitizenHeader from 'e2e-test/pages/citizen/citizen-header'
import { Page } from 'e2e-test/utils/page'
import { waitUntilEqual } from "e2e-test/utils";
import { enduserLogin } from 'e2e-test/utils/user'

const mockedTime = HelsinkiDateTime.of(2024, 3, 14, 7, 26)
const mockedDate = mockedTime.toLocalDate()

let page: Page
let header: CitizenHeader
let calendarPage: CitizenCalendarPage
let children: PersonDetail[]

beforeEach(async () => {
    await resetDatabaseForE2ETests()
    await Fixture.person()
        .with(enduserChildFixturePorriHatterRestricted)
        .saveAndUpdateMockVtj()
    await Fixture.child(enduserChildFixturePorriHatterRestricted.id)
        .save()
    await Fixture.daycare()
        .with({
            id: '4f3a32f5-d1bd-4b8b-aa4e-4fd78b18354b',
            areaId: '6529e31e-9777-11eb-ba88-33a923255570', // Etelä
            name: 'Alkuräjähdyksen päiväkoti',
            type: ['CENTRE', 'PRESCHOOL', 'PREPARATORY_EDUCATION'],
            costCenter: '31500',
            visitingAddress: {
              streetAddress: 'Kamreerintie 1',
              postalCode: '02210',
              postOffice: 'Espoo'
            },
            decisionCustomization: {
              daycareName: 'Päiväkoti päätöksellä',
              preschoolName: 'Päiväkoti päätöksellä',
              handler: 'Käsittelijä',
              handlerAddress: 'Käsittelijän osoite',
            },
            providerType: 'MUNICIPAL',
            operationTimes: new Array(7).fill(new TimeRange(LocalTime.of(0, 0), LocalTime.of(23, 59))),
            location: {
                lat: 60.20377343765089,
                lon: 24.655715743526994
            },
            enabledPilotFeatures: [
                'MESSAGING',
                'MOBILE',
                'RESERVATIONS',
                'VASU_AND_PEDADOC',
                'MOBILE_MESSAGING',
                'PLACEMENT_TERMINATION'
            ]
        })
        .save()
    children = [
        enduserChildFixturePorriHatterRestricted
    ]
    await createDaycarePlacements({
        body: children.map((child) =>
          createDaycarePlacementFixture(
            uuidv4(),
            child.id,
            daycareFixture.id,
            mockedDate,
            mockedDate.addYears(1)
          )
        )
      }
    )
    await Fixture.person()
      .with(enduserGuardianFixture)
      .withDependants(...children)
      .saveAndUpdateMockVtj()

    page = await Page.open({ mockedTime })
    await page.goto(config.enduserUrl)
    await enduserLogin(page)
    header = new CitizenHeader(page)
    calendarPage = new CitizenCalendarPage(page, 'desktop')
    await header.selectTab('calendar')
})

describe('Citizen attendance reservations', () => {
    test('Open absence modal and check Tampere options', async () => {
        const reservationDay = mockedDate.addBusinessDays(10)

        const dayView = await calendarPage.openDayView(reservationDay)
        await dayView.createAbsence()
        await waitUntilEqual(
            () => page.find('[data-qa="title"]').text,
            'Ilmoita poissaolo'
        )
        let absenceChips = await page.findAll('[data-qa^="absence-"]')
        expect(await absenceChips.count()).toBe(2)

        await waitUntilEqual(
            () => page.findByDataQa('absence-SICKLEAVE').find('label').text,
            'Sairaus'
        )
        await waitUntilEqual(
            () => page.findByDataQa('absence-OTHER_ABSENCE').find('label').text,
            'Poissaolo'
        )
    })
})
