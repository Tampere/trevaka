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
import ExternalLink from 'lib-components/atoms/ExternalLink'

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
    },
    editor: {
      heading: {
        info: {
          DAYCARE: (
            <>
              <P>
                An early child education place may be applied for all year
                round. The application for early childhood education must be
                submitted no later than four months prior to the desired start
                date. If the need for early childhood education is due to
                employment, studies or training, and it has not been possible
                to anticipate the need for care, an early childhood education
                place must be sought as soon as possible – however, no later
                than two weeks before the child needs the place.
              </P>
              <P>
                A written decision on the early childhood education place will
                be sent to the Suomi.fi Messages service. If you wish to be
                notified of the decision electronically, you will need to
                activate the Suomi.fi Messages service. Further information
                on the service and its activation is available at{' '}
                <ExternalLink
                  text="https://www.suomi.fi/messages"
                  href="https://www.suomi.fi/messages"
                  newTab
                />
                . If you do not activate the Suomi.fi Messages service, the
                decision will be sent to you by post.
              </P>
              <P fitted={true}>* Information marked with a star is required</P>
            </>
          )
        }
      },
      serviceNeed: {
        startDate: {
          instructions: (
            <>
              It is possible to postpone the preferred starting day as long as
              the application has not been processed by the customer service.
              After this, any desired amendments can be made by contacting the
              {customerContactText()}
            </>
          )
        },
        urgent: {
          attachmentsMessage: {
            text: (
              <P fitted={true}>
                If the need for an early child education place is due to sudden employment or obtaining a study place, the early childhood education place must be sought no later than two weeks before the need for care starts. Furthermore, the custodian must make contact, without delay, with the{' '}
                {customerContactText()}
              </P>
            )
          }
        },
        shiftCare: {
          instructions:
            'The day care centres are normally open on weekdays from 6.00am to 6pm. Evening care is intended for the children who, due to the parents’ work or studies that lead to a qualification, regularly require care after 6pm. Day care centres that offer evening care open, if necessary, at 5.30am and close at 10.30pm at the latest. Some day care centres that offer evening care are also open during the weekends. Shift care is intended for children whose parents work in shifts, when the child’s care also includes nights.',
          message: {
            text: 'Evening and shift care is intended for those children who, due to the parents’ work or studies that lead to a qualification, require evening and shift care.    In the case of the parents, an employer’s certificate of a need for evening or shift care due to shift work or study must be attached to the application.'
          },
          attachmentsMessage: {
            text: 'Evening and shift care is intended for those children who, due to the parents’ work or studies that lead to a qualification, require evening and shift care.    In the case of the parents, an employer’s certificate of a need for evening or shift care due to shift work or study must be attached to the application.'
          }
        },
        assistanceNeedInstructions: {
          DAYCARE:
            'Intensified or special care is given to a child as soon as the need arises. If a child has received an expert opinion backing the need for support, this must be notified in the early childhood education application. The support measures are carried out in the child’s daily life as part of the early childhood educational activities. Tampere’s early childhood education will separately be in contact after the application has been submitted, if the child has a need for support.',
        },
        partTime: {
          true: 'Part-time'
        },
        dailyTime: {
          label: {
            DAYCARE: 'Service options'
          }
        }
      },
      contactInfo: {
        info: (
          <P data-qa="contact-info-text">
            The personal information has been retrieved from the population data
            services and cannot be changed with this application. If the
            personal information is incorrect, please update the information on{' '}
            <ExternalLink
              text="dvv.fi"
              href="https://dvv.fi/en/certificates-from-the-population-information-system."
              newTab
            />
            . If your address is about to change, you can add the new address in a
            separate field in the application. Add a future address for both the
            child and guardian. The address information will be official only
            when it has been updated to the database of the Digital and
            Population Data Services Agency. The decision on early childhood
            education and service vouchers, as well as the information on the
            location of an early childhood education club is automatically also
            submitted to a custodian living at another address found in the
            population data register.
          </P>
        ),
        futureAddressInfo:
          'In Tampere’s early childhood education, the official address is considered the one obtainable from the population data register. The address in the population data register changes when an applicant gives notice of a move to the post office or to the registry office.'
      },
      fee: {
        info: {
          DAYCARE: (
            <P>
              The client fees for municipal early childhood education and the own deductible part of the service voucher are based on the Act on Client Fees in Early Childhood Education and Care (1503/2016). The client fee is determined by the size of the family, the need for service as well the gross income.  New clients must fill in the client fee form and submit the required appendices to the Client fees of Early childhood education within a month from when the care started at the latest.
            </P>
          )
        },
        links: (
          <P>
            You will find further information on client fees for early childhood education on{' '}
            <ExternalLink
              href="https://www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus/asiakasmaksut.html"
              text="the website of the City of Tampere"
              newTab
            />
          </P>
        )
      },
      additionalDetails: {
        dietInfo: (
          <>
            For special diets, a custodian shall submit to the early childhood education location{' '}
            <ExternalLink
              href="https://www.tampere.fi/sosiaali-ja-terveyspalvelut/erityisruokavaliot.html"
              text="the form ‘Clarification of a special diet’"
              newTab
            />
            , filled in and signed by a doctor or a nutritional therapist, which is for a fixed time period.
          </>
        )
      }
    }
  },
  applicationsList: {
    title: 'Applying for early childhood education',
    summary: (
      <P width="800px">
        The child’s custodian can apply for early childhood education and a club
        for the child. Information about the custodian’s children isautomatically
        retrieved from the Population data register for this view.
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
