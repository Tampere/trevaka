{
  /*
SPDX-FileCopyrightText: 2021 City of Tampere

SPDX-License-Identifier: LGPL-2.1-or-later
*/
}

import React from 'react'

import ExternalLink from 'lib-components/atoms/ExternalLink'
import UnorderedList from 'lib-components/atoms/UnorderedList'
import { P } from 'lib-components/typography'
import { Gap } from 'lib-components/white-space'
import { Translations } from 'lib-customizations/citizen'
import { DeepPartial } from 'lib-customizations/types'

const customerContactText = function () {
  return (
    <>
      {' '}
      customer service of the Early childhood education at:{' '}
      <a href="mailto:varhaiskasvatus.asiakaspalvelu@tampere.fi">
        varhaiskasvatus.asiakaspalvelu@tampere.fi
      </a>{' '}
      / <a href="tel:+358408007260">040 800 7260</a> (Mon-Fri between 9am and
      12noon).
    </>
  )
}

const en: DeepPartial<Translations> = {
  applications: {
    creation: {
      daycareInfo:
        'An applicant for early childhood education applies for a place in a municipal day care centre or family day care, an outsourced service day care centre or a day care centre supported by a service voucher.',
      clubInfo:
        'With a club application one may apply for a place at municipal clubs or clubs supported by a service voucher.',
      applicationInfo: (
        <P>
          The custodian can make amendments to the application on the web up
          until the moment that the application is accepted for processing by
          the customer service. After this, amendments or cancellation of the
          application are possible by getting in contact with the
          {customerContactText()}
        </P>
      ),
      duplicateWarning:
        'The child already has a similar unfinished application. Please return to the Applications view and complete the existing application or contact the customer service of the Early childhood education.',
      transferApplicationInfo: {
        DAYCARE:
          'The child already has a place in early childhood education in Tampere. With this application you can apply for a transfer to another unit offering early childhood education in Tampere.'
      }
    },
    editor: {
      unitPreference: {
        units: {
          serviceVoucherLink:
            'https://www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus/paivakodit.html#palvelusetelipaivakodit'
        }
      },
      heading: {
        info: {
          DAYCARE: (
            <>
              <P>
                An early child education place may be applied for all year
                round. The application for early childhood education must be
                submitted no later than four months prior to the desired start
                date. If the need for early childhood education is due to
                employment, studies or training, and it has not been possible to
                anticipate the need for care, an early childhood education place
                must be sought as soon as possible – however, no later than two
                weeks before the child needs the place.
              </P>
              <P>
                A written decision on the early childhood education place will
                be sent to the Suomi.fi Messages service. If you wish to be
                notified of the decision electronically, you will need to
                activate the Suomi.fi Messages service. Further information on
                the service and its activation is available at{' '}
                <ExternalLink
                  text="https://www.suomi.fi/messages"
                  href="https://www.suomi.fi/messages"
                  newTab
                />
                . If you do not activate the Suomi.fi Messages service, the
                decision will be sent to you by post.
              </P>
              <P fitted={true}>
                * The information denoted with an asterisk is mandatory.
              </P>
            </>
          ),
          CLUB: (
            <>
              <P>
                A place at a club can be applied for all year round. A municipal
                place in a club or one supported with a service voucher can be
                applied for with a club application. A written confirmation of a
                place in a club will be sent to the Suomi.fi Messages service.
                If you wish to have the notice in electronic form, you must
                activate the Suomi.fi Messages service. Further information on
                the service and its activation is available at{' '}
                <ExternalLink
                  text="https://www.suomi.fi/messages"
                  href="https://www.suomi.fi/messages"
                  newTab
                />
                . If you do not activate the suomi.fi/messages service, the
                notice of the place at the club will be sent to you by post. A
                place is granted for one administrative period at a time.
              </P>
              <P>
                The club application is for one such period. Once the period in
                question ends, the application is removed from the system.
              </P>
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
        clubDetails: {
          wasOnDaycareInfo:
            'If a child has been in municipal day care or family care or they give up their place when the club starts, they have a greater chance to obtain the place in the club.',
          wasOnClubCareInfo:
            'If the child has been in the club already during the previous period, they have a greater chance also to obtain a place from the club during the forthcoming period.'
        },
        urgent: {
          attachmentsMessage: {
            text: (
              <P fitted={true}>
                If the need for an early child education place is due to sudden
                employment or obtaining a study place, the early childhood
                education place must be sought no later than two weeks before
                the need for care starts. Furthermore, the custodian must make
                contact, without delay, with the {customerContactText()}
              </P>
            )
          }
        },
        shiftCare: {
          instructions:
            'The day care centres are normally open on weekdays from 6.00am to 6pm. Evening care is intended for the children who, due to the parents’ work or studies that lead to a qualification, regularly require care after 6pm. Day care centres that offer evening care open, if necessary, at 5.30am and close at 10.30pm at the latest. Some day care centres that offer evening care are also open during the weekends. Shift care is intended for children whose parents work in shifts, when the child’s care also includes nights.',
          message: {
            text: 'Evening and shift care is intended for those children who, due to the parents work or studies that lead to a qualification, require evening and shift care. In the case of the parents, an employer’s certificate of a need for evening or shift care due to shift work or study must be attached to the application.'
          },
          attachmentsMessage: {
            text: 'Evening and shift care is intended for those children who, due to the parents’ work or studies that lead to a qualification, require evening and shift care. In the case of the parents, an employer’s certificate of a need for evening or shift care due to shift work or study must be attached to the application.'
          }
        },
        assistanceNeedInstructions: {
          DAYCARE:
            'Intensified or special care is given to a child as soon as the need arises. If a child has received an expert opinion backing the need for support, this must be notified in the early childhood education application. The support measures are carried out in the child’s daily life as part of the early childhood educational activities. Tampere’s early childhood education will separately be in contact after the application has been submitted, if the child has a need for support.',
          CLUB: 'If the child has a need for support, the staff of Tampere’s early childhood education will get in contact the application has been submitted.'
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
            . If your address is about to change, you can add the new address in
            a separate field in the application. Add a future address for both
            the child and guardian. The address information will be official
            only when it has been updated to the database of the Digital and
            Population Data Services Agency. The decision on both early
            childhood education and service vouchers as well as the information
            about a club place for open early childhood education is
            automatically submitted to a custodian living at another address
            based on the information in the population data register.
          </P>
        ),
        futureAddressInfo:
          'In Tampere’s early childhood education, the official address is considered the one obtainable from the population data register. The address in the population data register changes when an applicant gives notice of a move to the post office or to the registry office.'
      },
      fee: {
        info: {
          DAYCARE: (
            <P>
              The client fees for municipal early childhood education and the
              own deductible part of the service voucher are based on the Act on
              Client Fees in Early Childhood Education and Care (1503/2016). The
              client fee is determined by the size of the family, the need for
              service as well the gross income. New clients must fill in the
              client fee form and submit the required appendices to the Client
              fees of Early childhood education within a month from when the
              care started at the latest.
            </P>
          )
        },
        links: (
          <P>
            You will find further information on client fees for early childhood
            education on{' '}
            <ExternalLink
              href="https://www.tampere.fi/varhaiskasvatusasiakasmaksut"
              text="the website of the City of Tampere"
              newTab
            />
          </P>
        )
      },
      additionalDetails: {
        dietInfo: (
          <>
            For special diets, a custodian shall submit to the early childhood
            education location{' '}
            <ExternalLink
              href="https://www.tampere.fi/erityisruokavaliot"
              text="the form ‘Clarification of a special diet’"
              newTab
            />
            , filled in and signed by a doctor or a nutritional therapist, which
            is for a fixed time period.
          </>
        )
      }
    }
  },
  applicationsList: {
    title: 'Applying for early childhood education',
    summary: (
      <P width="800px">
        The child&apos;s custodian can apply for early childhood education and a
        club for the child. Information about the custodian&apos;s children is
        automatically retrieved from the Population data register for this view.
      </P>
    )
  },
  children: {
    pageDescription:
      "General information related to your children's early childhood education is displayed on this page."
  },
  footer: {
    cityLabel: '© City of Tampere',
    privacyPolicyLink: (
      <ExternalLink
        href="https://www.tampere.fi/tietosuoja"
        text="Privacy Notices"
        newTab={true}
        data-qa="footer-policy-link"
      ></ExternalLink>
    ),
    sendFeedbackLink: (
      <ExternalLink
        href="https://www.tampere.fi/palaute"
        text="Give feedback"
        newTab={true}
        data-qa="footer-feedback-link"
      ></ExternalLink>
    )
  },
  loginPage: {
    applying: {
      infoBullets: [
        'apply for an early childhood or club place for your child or view a previous application',
        "report your or your child's income information",
        "accept your child's early childhood or club place",
        "terminate your child's early childhood or club place."
      ]
    },
    title: 'City of Tampere early childhood education'
  },
  map: {
    mainInfo: `In this view you can locate on the map all of Tampere’s early childhood education units and clubs. Regional service voucher units and clubs can also be found on the map.`,
    privateUnitInfo: <></>,
    serviceVoucherLink:
      'https://www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus/paivakodit.html#palvelusetelipaivakodit',
    searchPlaceholder: 'E.g. Amurin päiväkoti'
  },
  decisions: {
    summary: (
      <P width="800px">
        This page displays the received decisions regarding child&apos;s early
        childhood education and club applications.
      </P>
    )
  },
  income: {
    description: (
      <>
        <p data-qa="income-description-p1">
          On this page, you can submit statements on your earnings that affect
          the early childhood education fee. You can also view, edit, or delete
          income statements that you have submitted until the authority has
          processed the information. After the form has been processed, you can
          update your income information by submitting a new form.
        </p>
        <p>
          <strong>
            Both adults living in the same household must submit their own
            separate income statements.
          </strong>
        </p>
        <p data-qa="income-description-p2">
          The client fees for municipal early childhood education are determined
          as a percentage of the family’s gross income. The fees vary according
          to family size, income and time in early childhood education.
        </p>
        <p>
          <a href="https://www.tampere.fi/varhaiskasvatusasiakasmaksut">
            Further information on client fees.
          </a>
        </p>
      </>
    ),
    formDescription: (
      <>
        <P data-qa="income-formDescription-p1">
          The income statement and its attachments must be submitted within a
          month from the beginning of early childhood education. In case of
          incomplete income information, the fee may be set at the highest fee.
        </P>
        <P>
          The client fee is charged from the first day of early education in
          accordance with the decision.
        </P>
        <P>
          The client must immediately inform the client fees for Early childhood
          education of changes in income and family size.{' '}
        </P>
        <P>
          <strong>To be noted:</strong>
        </P>
        <Gap size="xs" />
        <UnorderedList>
          <li>
            If your income exceeds the income threshold according to family
            size, accept the highest early childhood education fee. In this
            case, you do not need to submit an income statement.
          </li>
          <li>
            If there&apos;s another adult in your family, they must also submit
            an income statement by personally logging into eVaka and filling out
            this form.
          </li>
          <li>
            See current income thresholds{' '}
            <a
              target="_blank"
              rel="noreferrer"
              href="https://www.tampere.fi/varhaiskasvatusasiakasmaksut"
            >
              hare
            </a>
            .
          </li>
        </UnorderedList>
        <P>* The information denoted with an asterisk is mandatory.</P>
      </>
    )
  }
}

export default en
