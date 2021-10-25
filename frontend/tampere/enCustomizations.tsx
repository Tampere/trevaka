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

const customerContactText = function () {
  return (
    <>
      {' '}
      customer service of the Early childhood education at:{' '}
      <a href="mailto:varhaiskasvatus.asiakaspalvelu@tampere.fi">
        varhaiskasvatus.asiakaspalvelu@tampere.fi
      </a>{' '}
      / <a href="tel:+358408007260">040 800 7260</a> (Mon-Fri between 9am and 12noon).
    </>
  )
}

const en: DeepPartial<Translations> = {
  applications: {
    creation: {
      daycareInfo:
        'An applicant for early childhood education applies for a place in a municipal day care centre or family day care, an outsourced service day care centre or a day care centre supported by a service voucher.',
      clubInfo:
        'With a club application one may apply for a place at municipal clubs or clubs supported by a service voucher.  ',
      applicationInfo: (
        <P>
          The custodian can make amendments to the application on the web up
          until the moment that the application is accepted for processing by
          the customer service. After this, amendments or cancellation of the
          application are possible by getting in contact with the
          {customerContactText()}
        </P>
      ),
      transferApplicationInfo: {
        DAYCARE:
          'The child already has a place in early childhood education in Tampere.  With this application you can apply for a transfer to another unit offering early childhood education in Tampere.'
      }
    }
  },
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
