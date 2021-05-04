{
  /*
SPDX-FileCopyrightText: 2021 City of Tampere

SPDX-License-Identifier: LGPL-2.1-or-later
*/
}

import { P } from 'lib-components/typography'
import { Translations } from 'lib-customizations/citizen'
import { DeepPartial } from 'lib-customizations/types'
import React from 'react'

const fi: DeepPartial<Translations> = {
  applicationsList: {
    title: 'Hakeminen varhaiskasvatukseen',
    summary: function ApplicationListSummaryText() {
      return (
        <P width="800px">
          Lapsen huoltaja voi tehdä lapselleen hakemuksen varhaiskasvatukseen ja
          kerhoon. Huoltajan lasten tiedot haetaan tähän näkymään
          automaattisesti Väestötietojärjestelmästä.
        </P>
      )
    }
  },
  footer: {
    cityLabel: '© Tampereen kaupunki',
    privacyPolicyLink:
      'https://www.tampere.fi/tampereen-kaupunki/yhteystiedot-ja-asiointi/verkkoasiointi/tietosuoja/tietosuojaselosteet.html',
    sendFeedbackLink: 'https://www.tampere.fi/palaute.html.stx'
  }
}

export default fi
