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
      m1: '#00417D',
      m2: '#29549A',
      m3: '#4676BE',
      m4: '#E5EEF8',
      m2Hover: '#00417D',
      m2Active: '#00417D',
      m2Focus: '#4676BE'
    },
    grayscale: {
      g100: '#0F0F0F',
      g70: '#585857',
      g35: '#B1B1B1',
      g15: '#D8D8D8',
      g4: '#F5F5F5',
      g0: '#FFFFFF'
    },
    status: {
      danger: '#CB181A',
      warning: '#EB6A00',
      success: '#93B858',
      info: '#4676BE'
    },
    accents: {
      a1greenDark: '#508004',
      a2orangeDark: '#B85300',
      a3emerald: '#397368',
      a4violet: '#7361A2',
      a5orangeLight: '#F4D240',
      a6turquoise: '#91C9EA',
      a7mint: '#8CC1B3',
      a8lightBlue: '#CAD6E2',
      a9pink: '#D77094',
      a10powder: '#CAD6E2'
    },
    absences: {
      PLANNED_ABSENCE: '#508004',
      UNAUTHORIZED_ABSENCE: '#4676BE'
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
