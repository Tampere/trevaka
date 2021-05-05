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

const en: DeepPartial<Translations> = {
  applicationsList: {
    title: 'TODO (en) Hakeminen varhaiskasvatukseen',
    summary: function ApplicationListSummaryText() {
      return (
        <P width="800px">
          TODO (en) Lapsen huoltaja voi tehdä lapselleen hakemuksen
          varhaiskasvatukseen ja kerhoon. Huoltajan lasten tiedot haetaan tähän
          näkymään automaattisesti Väestötietojärjestelmästä.
        </P>
      )
    }
  },
  footer: {
    cityLabel: '© City of Tampere',
    privacyPolicyLink: 'TODO',
    sendFeedbackLink: 'TODO'
  },
  map: {
    mainInfo: `TODO (en) Tässä näkymässä voit hakea kartalta kaikki Tampereen varhaiskasvatusyksiköt sekä kerhot. Kartalta löytyvät myös seudulliset palveluseteliyksiköt ja -kerhot.`,
    searchPlaceholder: 'E.g. Jukolankatu 7 tai Amurin päiväkoti'
  }
}

export default en
