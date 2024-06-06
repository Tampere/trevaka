// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import { EmployeeMobileCustomizations } from 'lib-customizations/types'

import featureFlags from './featureFlags'

const customizations: EmployeeMobileCustomizations = {
  appConfig: {},
  featureFlags,
  translations: {
    fi: {
      mobile: {
        landerText1: 'Tervetuloa käyttämään eVaka Orivesi -mobiilisovellusta!'
      },
      absences: {
        absenceTypes: {
          PLANNED_ABSENCE: 'Suunniteltu poissaolo',
          FORCE_MAJEURE: 'Hyvityspäivä',
          FREE_ABSENCE: 'Kesäajan maksuton poissaolo'
        },
        careTypes: {
          PRESCHOOL_DAYCARE: 'Täydentävä varhaiskasvatus'
        }
      },
      common: {
        types: {
          PRESCHOOL_DAYCARE: 'Täydentävä varhaiskasvatus',
          PREPARATORY_DAYCARE: 'Täydentävä varhaiskasvatus'
        },
        placement: {
          PRESCHOOL_DAYCARE: 'Täydentävä varhaiskasvatus'
        }
      }
    }
  }
}

export default customizations
