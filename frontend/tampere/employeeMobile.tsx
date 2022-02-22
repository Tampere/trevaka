{
  /*
SPDX-FileCopyrightText: 2021 City of Tampere

SPDX-License-Identifier: LGPL-2.1-or-later
*/
}

import { EmployeeMobileCustomizations } from 'lib-customizations/types'

const customizations: EmployeeMobileCustomizations = {
  appConfig: {},
  translations: {
    fi: {
      mobile: {
        landerText1: 'Tervetuloa käyttämään eVaka Tampere -mobiilisovellusta!'
      },
      absences: {
        absenceTypes: {
          PLANNED_ABSENCE: 'Sopimuksen mukainen poissaolo',
          FORCE_MAJEURE: 'Hyvityspäivä'
        }
      }
    }
  }
}

export default customizations
