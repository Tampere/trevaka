{
  /*
SPDX-FileCopyrightText: 2021 City of Tampere

SPDX-License-Identifier: LGPL-2.1-or-later
*/
}

import { CitizenCustomizations } from 'lib-customizations/types'
import enCustomizations from './enCustomizations'
import fiCustomizations from './fiCustomizations'
import svCustomizations from './svCustomizations'
import TampereLogo from './city-logo.svg'
import featureFlags from './featureFlags'
import mapConfig from './mapConfig'

const customizations: CitizenCustomizations = {
  translations: {
    fi: fiCustomizations,
    sv: svCustomizations,
    en: enCustomizations
  },
  cityLogo: {
    src: TampereLogo,
    alt: 'Tampere logo'
  },
  routeLinkRootUrl: 'https://reittiopas.tampere.fi/reitti/',
  mapConfig,
  featureFlags
}

export default customizations
