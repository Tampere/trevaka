{
  /*
SPDX-FileCopyrightText: 2021 City of Tampere

SPDX-License-Identifier: LGPL-2.1-or-later
*/
}

import { CitizenCustomizations } from 'lib-customizations/types'
import enCustomizations from './enCustomizations'
import fiCustomizations from './fiCustomizations'
import TampereLogo from './city-logo-citizen.svg'
import FooterLogo from './footer-logo-citizen.png'
import featureFlags from './featureFlags'
import mapConfig from './mapConfig'

const customizations: CitizenCustomizations = {
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
