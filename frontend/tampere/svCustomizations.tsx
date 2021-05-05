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

const sv: DeepPartial<Translations> = {
  applicationsList: {
    title: 'TODO (sv) Hakeminen varhaiskasvatukseen',
    summary: function ApplicationListSummaryText() {
      return (
        <P width="800px">
          TODO (sv) Lapsen huoltaja voi tehdä lapselleen hakemuksen
          varhaiskasvatukseen ja kerhoon. Huoltajan lasten tiedot haetaan tähän
          näkymään automaattisesti Väestötietojärjestelmästä.
        </P>
      )
    }
  },
  footer: {
    cityLabel: '© Tammerfors stad',
    privacyPolicyLink: 'TODO',
    sendFeedbackLink: 'TODO'
  }
}

export default sv
