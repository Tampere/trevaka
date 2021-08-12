{
  /*
SPDX-FileCopyrightText: 2021 City of Tampere

SPDX-License-Identifier: LGPL-2.1-or-later
*/
}

import { EmployeeCustomizations } from 'lib-customizations/types'
import TampereLogo from './city-logo.svg'
import featureFlags from './featureFlags'

const customizations: EmployeeCustomizations = {
  appConfig: {},
  translations: {
    fi: {
      // override translations here
      common: {
        careTypeLabels: {
          preschool: 'Esiopetusta täydentävä toiminta'
        }
      },
      childInformation: {
        assistanceAction: {
          fields: {
            measureTypes: {
              SPECIAL_ASSISTANCE_DECISION: 'Erityinen tuki',
              SPECIAL_ASSISTANCE_DECISION_INFO: 'Ei päätöstä',
              INTENSIFIED_ASSISTANCE: 'Tehostettu tuki',
              INTENSIFIED_ASSISTANCE_INFO: 'Ei päätöstä',
              EXTENDED_COMPULSORY_EDUCATION: 'Pidennetty oppivelvollisuus',
              EXTENDED_COMPULSORY_EDUCATION_INFO:
                'Lapsella erityisen tuen päätös',
              CHILD_ACCULTURATION_SUPPORT:
                'Lapsen ja perheen kotoutumisen tuki',
              CHILD_ACCULTURATION_SUPPORT_INFO: '',
              TRANSPORT_BENEFIT: 'Kuljetusetu',
              TRANSPORT_BENEFIT_INFO: 'Lapsella kuljetusetu'
            }
          }
        }
      },
      unit: {
        placementProposals: {
          rejectReasons: {
            REASON_1: 'Päiväkoti täynnä',
            REASON_2: 'Sisäilma tai muu rakenteellinen syy',
            REASON_3: 'Henkilökuntaa tilapäisesti vähennetty'
          },
          infoBoxTitle: '',
          infoBoxText: ''
        }
      },
      login: {
        loginAD: 'Tampere AD'
      },
      placement: {
        type: {
          DAYCARE: 'Varhaiskasvatus',
          DAYCARE_PART_TIME: 'Osapäiväinen varhaiskasvatus​',
          TEMPORARY_DAYCARE: 'Tilapäinen kokopäiväinen varhaiskasvatus',
          PRESCHOOL_DAYCARE: 'Esiopetusta täydentävä varhaiskasvatus',
          CLUB: 'Kerho',
          SCHOOL_SHIFT_CARE: 'Koululaisten vuorohoito'
        }
      }
    }
  },
  cityLogo: {
    src: TampereLogo,
    alt: 'Tampere logo'
  },
  featureFlags,
  assistanceMeasures: [
    'SPECIAL_ASSISTANCE_DECISION',
    'INTENSIFIED_ASSISTANCE',
    'EXTENDED_COMPULSORY_EDUCATION',
    'CHILD_ACCULTURATION_SUPPORT',
    'TRANSPORT_BENEFIT'
  ],
  placementTypes: [
    'DAYCARE',
    'DAYCARE_PART_TIME',
    'TEMPORARY_DAYCARE',
    'PRESCHOOL_DAYCARE',
    'CLUB',
    'SCHOOL_SHIFT_CARE'
  ],
  placementPlanRejectReasons: ['REASON_1', 'REASON_2', 'REASON_3', 'OTHER']
}

export default customizations
