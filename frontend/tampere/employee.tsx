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
  translations: {
    fi: {
      // override translations here
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
          infoBoxTitle: '',
          infoBoxText: ''
        }
      },
      login: {
        loginAD: 'Tampere AD'
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
  ]
}

export default customizations
