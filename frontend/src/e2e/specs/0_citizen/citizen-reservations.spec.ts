import LocalDate from 'lib-common/local-date'
import config from 'e2e-test/config'
import { resetDatabaseForE2ETests } from '../../common/tampere-dev-api'
import { insertDaycarePlacementFixtures } from 'e2e-test/dev-api'
import {
    createDaycarePlacementFixture,
    daycareFixture,
    enduserChildFixturePorriHatterRestricted,
    Fixture,
    uuidv4
} from 'e2e-test/dev-api/fixtures'
import { PersonDetail } from 'e2e-test/dev-api/types'
import CitizenCalendarPage from 'e2e-test/pages/citizen/citizen-calendar'
import CitizenHeader from 'e2e-test/pages/citizen/citizen-header'
import { Page } from 'e2e-test/utils/page'
import { waitUntilEqual } from "e2e-test/utils";
import { enduserLogin } from 'e2e-test/utils/user'

let page: Page
let header: CitizenHeader
let calendarPage: CitizenCalendarPage
let children: PersonDetail[]

beforeEach(async () => {
    await resetDatabaseForE2ETests()
    await Fixture.person()
        .with(enduserChildFixturePorriHatterRestricted)
        .save()
    await Fixture.child(enduserChildFixturePorriHatterRestricted.id)
        .save()
    await Fixture.daycare()
        .with({
            id: '4f3a32f5-d1bd-4b8b-aa4e-4fd78b18354b',
            careAreaId: '6529e31e-9777-11eb-ba88-33a923255570', // Etelä
            name: 'Alkuräjähdyksen päiväkoti',
            type: ['CENTRE', 'PRESCHOOL', 'PREPARATORY_EDUCATION'],
            costCenter: '31500',
            streetAddress: 'Kamreerintie 1',
            postalCode: '02210',
            postOffice: 'Espoo',
            decisionDaycareName: 'Päiväkoti päätöksellä',
            decisionPreschoolName: 'Päiväkoti päätöksellä',
            decisionHandler: 'Käsittelijä',
            decisionHandlerAddress: 'Käsittelijän osoite',
            providerType: 'MUNICIPAL',
            operationDays: [1, 2, 3, 4, 5],
            roundTheClock: true,
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
    await insertDaycarePlacementFixtures(
        children.map((child) =>
            createDaycarePlacementFixture(
                uuidv4(),
                child.id,
                daycareFixture.id,
                LocalDate.today().formatIso(),
                LocalDate.today().addYears(1).formatIso()
            )
        )
    )

    page = await Page.open()
    await page.goto(config.enduserUrl)
    await enduserLogin(page)
    header = new CitizenHeader(page)
    calendarPage = new CitizenCalendarPage(page, 'desktop')
    await header.selectTab('calendar')
})

describe('Citizen attendance reservations', () => {
    test('Open absence modal and check Tampere options', async () => {
        const reservationDay = LocalDate.today().addDays(14)

        const dayView = await calendarPage.openDayView(reservationDay)
        await dayView.createAbsence()
        let absenceChips = await page.findAll('[data-qa^="absence-"]')
        expect(await absenceChips.count()).toBe(3)

        await waitUntilEqual(
            () => page.findByDataQa('absence-SICKLEAVE').find('label').innerText,
            'Sairaus'
        )
        await waitUntilEqual(
            () => page.findByDataQa('absence-OTHER_ABSENCE').find('label').innerText,
            'Poissaolo'
        )
        await waitUntilEqual(
            () => page.findByDataQa('absence-PLANNED_ABSENCE').find('label').innerText,
            'Sopimuksen mukainen poissaolo' // TODO: At some point will remove via featureFlag
        )
    })
})
