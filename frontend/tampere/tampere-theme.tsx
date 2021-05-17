import { Theme } from 'lib-common/theme'

const theme: Theme = {
  colors: {
    brand: {
      primary: '#00417D',
      secondary: '#39A7D7',
      secondaryLight: '#e9f5ff'
    },
    main: {
      dark: '#00417D',
      medium: '#4676BE',
      primary: '#29549A',
      primaryHover: '#4676BE',
      primaryActive: '#00417D',
      light: '#C6D7E3',
      lighter: '#E5EEF8'
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
      orange: '#FF7300',
      orangeDark: '#B85300',
      green: '#ABC872',
      greenDark: '#4C7A00',
      water: '#91C9EA',
      yellow: '#F4D240',
      red: '#CB181A',
      petrol: '#5390B5',
      emerald: '#397368',
      violet: '#7361A2'
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
        weight: 500,
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
    },
    h5: {
      weight: 400,
      bold: 600,
      mobile: {
        weight: 500
      }
    }
  }
}

export default theme
