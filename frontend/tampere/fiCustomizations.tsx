{
  /*
SPDX-FileCopyrightText: 2021 City of Tampere

SPDX-License-Identifier: LGPL-2.1-or-later
*/
}

import { Translations } from 'lib-customizations/citizen'
import { DeepPartial } from 'lib-customizations/types'

const fi: DeepPartial<Translations> = {
  applicationsList: {
    title: 'Hakeminen varhaiskasvatukseen',
    summary: `Lapsen huoltaja voi tehdä lapselleen hakemuksen varhaiskasvatukseen ja kerhoon. Huoltajan lasten tiedot haetaan tähän näkymään automaattisesti Väestötietojärjestelmästä.`
  },
  footer: {
    cityLabel: '© Tampereen kaupunki',
    privacyPolicyLink:
      'https://www.tampere.fi/tampereen-kaupunki/yhteystiedot-ja-asiointi/verkkoasiointi/tietosuoja/tietosuojaselosteet.html',
    sendFeedbackLink: 'https://www.tampere.fi/palaute.html.stx'
  }
}

export default fi
