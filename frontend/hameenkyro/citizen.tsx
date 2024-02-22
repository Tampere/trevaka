// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import { CitizenCustomizations } from 'lib-customizations/types'

import HameenkyroLogo from './HameenkyroLogo.png'
import enCustomizations from './enCustomizations'
import featureFlags from './featureFlags'
import fiCustomizations from './fiCustomizations'
import mapConfig from './mapConfig'

const customizations: CitizenCustomizations = {
  appConfig: {},
  langs: ['fi', 'en'],
  translations: {
    fi: fiCustomizations,
    sv: {},
    en: enCustomizations
  },
  cityLogo: {
    src: HameenkyroLogo,
    alt: 'Hämeenkyrö logo'
  },
  mapConfig,
  featureFlags,
  getMaxPreferredUnits(type) {
    if (type === 'PRESCHOOL') {
      return 1
    }
    return 3
  }
}

export default customizations
