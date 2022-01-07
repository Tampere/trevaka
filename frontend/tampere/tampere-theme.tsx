{
  /*
SPDX-FileCopyrightText: 2021 City of Tampere

SPDX-License-Identifier: LGPL-2.1-or-later
*/
}

import { Theme } from 'lib-common/theme'

const theme: Theme = {
  colors: {
    main: {
      dark: '#00417D',
      primary: '#29549A',
      light: '#4676BE',
      lighter: '#E5EEF8',
      primaryHover: '#00417D',
      primaryActive: '#00417D',
      primaryFocus: '#4676BE'
    },
    greyscale: {
      darkest: '#0F0F0F',
      dark: '#585857',
      medium: '#B1B1B1',
      lighter: '#D8D8D8',
      lightest: '#F5F5F5',
      white: '#FFFFFF'
    },
    accents: {
      dangerRed: '#CB181A',
      warningOrange: '#EB6A00',
      successGreen: '#799E3D',
      infoBlue: '#5390B5',
      greenDark: '#3C6100',
      orangeDark: '#B85300',
      emerald: '#397368',
      violet: '#7361A2',
      peach: '#F4D240',
      turquoise: '#91C9EA',
      mint: '#8CC1B3',
      lightBlue: '#CAD6E2',
      pink: '#D77094'
    }
  },
  typography: {
    h1: {
      weight: 600,
      bold: 800,
      mobile: {
        weight: 600
      }
    },
    h2: {
      weight: 500,
      bold: 700,
      mobile: {
        weight: 500
      }
    },
    h3: {
      weight: 400,
      bold: 600,
      mobile: {
        weight: 500
      }
    },
    h4: {
      weight: 400,
      bold: 600,
      mobile: {
        weight: 500
      }
    }
  }
}

export default theme
