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
    title: 'Applying for early childhood education',
    summary: (
      <P width="800px">
        The child’s custodian can apply for early childhood education and a club
        for the child.  Information about the custodian’s children is
        automatically retrieved from the Population data register for this view.
      </P>
    )
  },
  footer: {
    cityLabel: '© City of Tampere',
    privacyPolicyLink: 'https://www.tampere.fi/tampereen-kaupunki/yhteystiedot-ja-asiointi/verkkoasiointi/tietosuoja/tietosuojaselosteet.html',
    sendFeedbackLink: 'https://www.tampere.fi/en/feedback.html.stx'
  }
}

export default en
