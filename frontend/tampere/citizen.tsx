{/*
SPDX-FileCopyrightText: 2021 City of Tampere

SPDX-License-Identifier: LGPL-2.1-or-later
*/}

import { CitizenCustomizations } from 'lib-customizations/types'
import enCustomizations from './enCustomizations'
import fiCustomizations from './fiCustomizations'
import svCustomizations from './svCustomizations'
import TampereLogo from './city-logo.svg'
import featureFlags from './featureFlags'
import mapConfig from './mapConfig'

const customizations: CitizenCustomizations = {
  fiCustomizations,
  svCustomizations,
  enCustomizations,
  cityLogo: {
    src: TampereLogo,
    alt: 'Tampere logo'
  },
  mapConfig,
  mapSearchAreaRect: {
    maxLatitude: 61.83715535012332,
    minLatitude: 61.42731906621412,
    maxLongitude: 24.118938446044925,
    minLongitude: 23.54256391525269
  },
  featureFlags
}

export default customizations
