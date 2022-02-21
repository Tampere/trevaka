{
  /*
SPDX-FileCopyrightText: 2021 City of Tampere

SPDX-License-Identifier: LGPL-2.1-or-later
*/
}

import { CitizenCustomizations } from 'lib-customizations/types'

import TampereLogo from './city-logo-citizen.svg'
import enCustomizations from './enCustomizations'
import featureFlags from './featureFlags'
import fiCustomizations from './fiCustomizations'
import FooterLogo from './footer-logo-citizen.png'
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
    src: TampereLogo,
    alt: 'Tampere logo'
  },
  footerLogo: {
    src: FooterLogo,
    alt: 'Tampere logo'
  },
  routeLinkRootUrl: 'https://reittiopas.tampere.fi/reitti/',
  mapConfig,
  featureFlags
}

export default customizations
