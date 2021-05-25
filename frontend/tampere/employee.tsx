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
              INTENSIFIED_ASSISTANCE: 'Tehostettu tuki',
              EXTENDED_COMPULSORY_EDUCATION: 'Pidennetty oppivelvollisuus',
              CHILD_ACCULTURATION_SUPPORT:
                'Lapsen ja perheen kotoutumisen tuki',
              TRANSPORT_BENEFIT: 'Kuljetusetu'
            }
          }
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
  ]
}

export default customizations
