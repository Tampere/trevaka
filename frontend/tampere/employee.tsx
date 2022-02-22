{
  /*
SPDX-FileCopyrightText: 2021 City of Tampere

SPDX-License-Identifier: LGPL-2.1-or-later
*/
}

import React from 'react'

import { EmployeeCustomizations } from 'lib-customizations/types'

import TampereLogo from './city-logo.svg'
import featureFlags from './featureFlags'

const customizations: EmployeeCustomizations = {
  appConfig: {},
  translations: {
    fi: {
      // override translations here
      absences: {
        absenceCategories: {
          NONBILLABLE: 'Ei laskuteta eVakasta',
          BILLABLE: 'Varhaiskasvatus'
        },
        absenceTypes: {
          PLANNED_ABSENCE: 'Sopimuksen mukainen poissaolo',
          FORCE_MAJEURE: 'Hyvityspäivä'
        },
        absenceTypesShort: {
          PLANNED_ABSENCE: 'Sopim, muk.',
          FORCE_MAJEURE: 'Hyvitys'
        },
        modal: {
          absenceTypes: {
            PLANNED_ABSENCE: 'Sopimuksen mukainen poissaolo',
            FORCE_MAJEURE: 'Hyvityspäivä'
          }
        }
      },
      common: {
        careTypeLabels: {
          preschool: 'Esiopetusta täydentävä toiminta'
        }
      },
      childInformation: {
        assistanceNeed: {
          fields: {
            // @ts-expect-error: Type 'Element' is not assignable to type 'string'
            capacityFactorInfo: (
              <ol style={{ margin: '0', padding: '0 1em' }}>
                <li>
                  Kaupungin päiväkodeissa kerroin merkitään integroidussa
                  varhaiskasvatusryhmässä oleville tehostettua tai erityistä
                  tukea tarvitseville lapsille ja missä tahansa ryhmässä
                  kotoutumisen tukea saaville lapsille. Kertoimen tallentaa
                  varhaiskasvatuksen erityisopettaja.
                </li>
                <li>
                  Mikäli ostopalvelu- tai palvelusetelipäiväkodissa olevalla
                  lapsella on tehostetun tai erityisen tuen tarve, voidaan
                  hänelle määritellä tuen kerroin. Päätöksen kertoimesta tekee
                  varhaiskasvatusjohtaja, varhaiskasvatuksen erityisopettajan
                  esityksen perusteella. Kertoimen tallentaa varhaiskasvatuksen
                  asiakaspalvelu.
                </li>
              </ol>
            ),
            bases: 'Tuen tarve'
          }
        },
        assistanceAction: {
          title: 'Tukitoimet ja tukipalvelut',
          fields: {
            actions: 'Tukitoimet ja tukipalvelut'
          }
        },
        dailyServiceTimes: {
          info: 'Tallenna tähän varhaiskasvatussopimuksella sovittu päivittäinen läsnäoloaika.',
          info2: ''
        }
      },
      footer: {
        cityLabel: 'Tampereen kaupunki',
        linkLabel: 'Tampereen varhaiskasvatus',
        linkHref:
          'https://www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus.html'
      },
      unit: {
        placementProposals: {
          rejectReasons: {
            REASON_1: 'Päiväkoti täynnä',
            REASON_2: 'Sisäilma tai muu rakenteellinen syy',
            REASON_3: 'Henkilökuntaa tilapäisesti vähennetty'
          },
          infoTitle: '',
          infoText: ''
        }
      },
      login: {
        loginAD: 'Tampere AD'
      },
      placement: {
        type: {
          DAYCARE: 'Varhaiskasvatus',
          DAYCARE_PART_TIME: 'Osapäiväinen varhaiskasvatus',
          TEMPORARY_DAYCARE: 'Tilapäinen kokopäiväinen varhaiskasvatus',
          PRESCHOOL_DAYCARE: 'Esiopetusta täydentävä varhaiskasvatus',
          CLUB: 'Kerho',
          SCHOOL_SHIFT_CARE: 'Koululaisten vuorohoito'
        }
      },
      unitEditor: {
        placeholder: {
          phone: 'esim. +358 40 555 5555',
          email: 'etunimi.sukunimi@tampere.fi',
          url: 'esim. https://www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus/paivakodit/alueesi-paivakodit/keskusta/amurinpaivakoti.html',
          streetAddress: 'Kadun nimi esim. Suokatu 10',
          decisionCustomization: {
            name: 'esim. Amurin päiväkoti'
          }
        },
        field: {
          decisionCustomization: {
            handler: ['Palveluohjaus', 'Varhaiskasvatusyksikön johtaja']
          }
        }
      },
      welcomePage: {
        text: 'Olet kirjautunut sisään eVaka Tampere -palveluun. Käyttäjätunnuksellesi ei ole vielä annettu oikeuksia, jotka mahdollistavat palvelun käytön. Tarvittavat käyttöoikeudet saat omalta esimieheltäsi.'
      }
    }
  },
  vasuTranslations: {
    FI: {},
    SV: {}
  },
  cityLogo: {
    src: TampereLogo,
    alt: 'Tampere logo'
  },
  featureFlags,
  assistanceMeasures: [],
  placementTypes: [
    'DAYCARE',
    'DAYCARE_PART_TIME',
    'TEMPORARY_DAYCARE',
    'PRESCHOOL_DAYCARE',
    'CLUB',
    'SCHOOL_SHIFT_CARE'
  ],
  placementPlanRejectReasons: ['REASON_1', 'REASON_2', 'REASON_3', 'OTHER'],
  unitProviderTypes: [
    'MUNICIPAL',
    'PURCHASED',
    'PRIVATE',
    'PRIVATE_SERVICE_VOUCHER'
  ],
  voucherValueDecisionTypes: ['NORMAL', 'RELIEF_ACCEPTED', 'RELIEF_REJECTED']
}

export default customizations
