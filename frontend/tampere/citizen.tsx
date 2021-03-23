import { CitizenCustomizations } from '@evaka/lib-customizations/types'
import enCustomizations from './enCustomizations'
import fiCustomizations from './fiCustomizations'
import svCustomizations from './svCustomizations'
import TampereLogo from './city-logo.svg'
import featureFlags from './featureFlags'

const customizations: CitizenCustomizations = {
  fiCustomizations,
  svCustomizations,
  enCustomizations,
  cityLogo: {
    src: TampereLogo,
    alt: 'Tampere logo'
  },
  featureFlags
}

export default customizations
