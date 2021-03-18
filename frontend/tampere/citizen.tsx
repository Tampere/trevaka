import { CitizenCustomizations } from '@evaka/lib-customizations/types'
import TampereLogo from './city-logo.svg'
import enCustomizations from './en-customizations'
import fiCustomizations from './fi-customizations'
import svCustomizations from './sv-customizations'


const customizations: CitizenCustomizations = {
  cityLogo: {
    src: TampereLogo,
    alt: 'Tampere logo'
  },
  fiCustomizations,
  svCustomizations,
  enCustomizations
}

export default customizations
