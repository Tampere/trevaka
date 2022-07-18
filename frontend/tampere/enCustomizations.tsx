{
  /*
SPDX-FileCopyrightText: 2021 City of Tampere

SPDX-License-Identifier: LGPL-2.1-or-later
*/
}

import React from 'react'

import ExternalLink from 'lib-components/atoms/ExternalLink'
import UnorderedList from 'lib-components/atoms/UnorderedList'
import { H1, H2, P } from 'lib-components/typography'
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
      / <a href="tel:+358408007260">040 800 7260</a> (Mon–Fri between 9am and
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
      preschoolInfo:
        'TODO: Maksutonta esiopetusta on neljä tuntia päivässä. Tämän lisäksi lapselle voidaan hakea maksullista täydentävää varhaiskasvatusta, jota tarjotaan esiopetuspaikoissa aamulla ennen esiopetuksen alkua ja iltapäivisin esiopetuksen jälkeen. Täydentävään varhaiskasvatukseen voi hakea myös palveluseteliä, valitsemalla Hakutoiveet-kohtaan palveluseteliyksikön, johon halutaan hakea. Hakemuksen täydentävään varhaiskasvatukseen voi tehdä esiopetukseen ilmoittautumisen yhteydessä tai erillisenä hakemuksena opetuksen jo alettua. Samalla hakemuksella voit hakea myös maksuttomaan valmistavaan opetukseen sekä valmistavaa opetusta täydentävään varhaiskasvatukseen.',
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
          'The child already has a place in early childhood education in Tampere. With this application you can apply for a transfer to another unit offering early childhood education in Tampere.',
        PRESCHOOL:
          'TODO: Lapsella on jo esiopetuspaikka. Tällä hakemuksella voit hakea esiopetusta täydentävää varhaiskasvatusta tai siirtoa toiseen esiopetusta tarjoavaan yksikköön.'
      }
    },
    editor: {
      verification: {
        serviceNeed: {
          connectedDaycare: {
            title: 'TODO: Esiopetusta täydentävän varhaiskasvatuksen tarve',
            label: 'TODO: Täydentävä varhaiskasvatus',
            withConnectedDaycare:
              'TODO: Haen myös esiopetusta täydentävää varhaiskasvatusta.'
          }
        }
      },
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
          ),
          info: {
            PRESCHOOL: [
              'TODO: Suomen- ja ruotsinkielinen esiopetus alkaa 11.8.2022. Jos tarvitsette varhaiskasvatusta 1.8.2022 lähtien ennen esiopetuksen alkua, voitte hakea sitä tällä hakemuksella valitsemalla ”Haen myös esiopetusta täydentävää varhaiskasvatusta'
            ]
          }
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
            DAYCARE: 'Service options',
            PRESCHOOL: 'TODO: Esiopetusta täydentävän varhaiskasvatuksen tarve'
          },
          connectedDaycare:
            'TODO: Haen myös esiopetusta täydentävää varhaiskasvatusta.',
          connectedDaycareInfo: (
            <>
              <P>
                TODO: Voit hakea lapselle tarvittaessa{' '}
                <strong>
                  esiopetusta täydentävää varhaiskasvatusta, joka on maksullista
                  ja jota annetaan esiopetuksen (4 tuntia/päivä) lisäksi
                </strong>{' '}
                aamuisin ja/tai iltapäivisin samassa paikassa kuin esiopetus.
                Jos haluat aloittaa varhaiskasvatuksen myöhemmin kuin esiopetus
                alkaa, kirjoita haluttu aloituspäivämäärä hakemuksen “Muut
                lisätiedot” -kohtaan.
              </P>
              <P>
                Yksityisiin esiopetusyksiköihin haettassa täydentävä
                varhaiskasvatus haetaan suoraan yksiköstä (pois lukien
                palveluseteliyksiköt). Yksiköt informoivat asiakkaita
                hakutavasta. Näissä tapauksissa palveluohjaus muuttaa hakemuksen
                pelkäksi esiopetushakemukseksi.
              </P>
              <P>
                Palveluseteliä haetaan valitsemalla hakutoiveeksi se
                palveluseteliyksikkö, johon halutaan hakea.
              </P>
              <P>
                Saat varhaiskasvatuspaikasta erillisen kirjallisen päätöksen, ja
                päätös tulee{' '}
                <a
                  href="https://www.suomi.fi/viestit"
                  target="_blank"
                  rel="noreferrer"
                >
                  Suomi.fi-viestit
                </a>{' '}
                -palveluun tai postitse, mikäli et ole ottanut Suomi.fi-viestit
                -palvelua käyttöön.
              </P>
            </>
          )
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
          ),
          PRESCHOOL: (
            <P>
              TODO: Esiopetus on maksutonta, mutta sitä täydentävä
              varhaiskasvatus on maksullista. Jos lapsi osallistuu täydentävään
              varhaiskasvatukseen, perhe toimittaa tuloselvityksen
              bruttotuloistaan tuloselvityslomakkeella viimeistään kahden viikon
              kuluessa siitä, kun lapsi on aloittanut varhaiskasvatuksessa.
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
        href="https://www.tampere.fi/tietosuojaselosteet"
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
        'view pictures and other documents related to your child’s early childhood',
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
    ),
    applicationDecisions: {
      type: {
        PRESCHOOL_DAYCARE: 'TODO: täydentävästä varhaiskasvatuksesta'
      },
      confirmationInfo: {
        preschool:
          'TODO: Esiopetuksen, valmistavan opetuksen ja/tai täydentävän varhaiskasvatuksen hyväksymis- tai hylkäämisilmoitus on toimitettava välittömästi, viimeistään kahden viikon kuluessa tämän ilmoituksen saamisesta. Jos olet hakenut useampaa palvelua, saat jokaisesta oman päätöksen erikseen vahvistettavaksi'
      },
      warnings: {
        doubleRejectWarning: {
          text: 'TODO: Olet hylkäämässä tarjotun esiopetus / valmistavan paikan. Täydentävän varhaiskasvatuksen paikka merkitään samalla hylätyksi.'
        }
      },
      response: {
        disabledInfo:
          'TODO: HUOM! Pääset hyväksymään/hylkäämään täydentävää varhaiskasvatusta koskevan päätöksen mikäli hyväksyt ensin esiopetusta / valmistavaa opetusta koskevan päätöksen.'
      }
    }
  },
  placement: {
    type: {
      PRESCHOOL_DAYCARE: 'TODO: Esiopetus ja täydentävä varhaiskasvatus',
      PRESCHOOL_WITH_DAYCARE: 'TODO: Esiopetus ja täydentävä varhaiskasvatus',
      PREPARATORY_WITH_DAYCARE:
        'TODO: Valmistava opetus ja täydentävä varhaiskasvatus',
      PREPARATORY_DAYCARE:
        'TODO: Valmistava opetus ja täydentävä varhaiskasvatus'
    }
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
  },
  accessibilityStatement: (
    <>
      <H1>Accessibility statement</H1>
      <P>
        This accessibility statement applies to the City of Tampere’s early
        childhood education online service eVaka at{' '}
        <a href="https://varhaiskasvatus.tampere.fi">varhaiskasvatus.tampere.fi</a>.
        The City of Tampere endeavours to ensure the accessibility of the online
        service, continuously improve the user experience and apply appropriate
        accessibility standards.
      </P>
      <P>
        The accessibility of the service was assessed by the development team of
        the service, and this statement was drawn up on 12 April 2022.
      </P>
      <H2>Compliance of the service</H2>
      <P>
        The online service complies with the statutory critical accessibility
        requirements in accordance with Level AA of the Accessibility Guidelines
        for the WCAG v2.1. The service is not yet fully compliant with the
        requirements.
      </P>
      <H2>Measures to support accessibility</H2>
      <P>
        The accessibility of the online service is ensured, among other things,
        by the following measures:
      </P>
      <ul>
        <li>
          Accessibility has been taken into account from the beginning of the
          design phase, for example, when choosing the colours and font sizes of
          the service.
        </li>
        <li>
          The service elements have been defined in consistently in terms of
          semantics.
        </li>
        <li>The service is continuously tested with a screen reader.</li>
        <li>
          Various users test the service and give feedback on its accessibility.
        </li>
        <li>
          When website technology or content changes, its accessibility is
          ensured through constant monitoring.
        </li>
      </ul>
      <P>
        This statement will be updated in conjunction with website changes and
        accessibility evaluations.
      </P>
      <H2>Known accessibility issues</H2>
      <P>
        Users may still encounter some issues on the website. The following
        contains a description of known accessibility issues. If you notice an
        issue on the site that is not listed, please contact us.
      </P>
      <ul>
        <li>
          Navigating on the Messages page using the keyboard or screen reader
          still requires revision for part of moving and targeted elements.
        </li>
        <li>
          The service’s unit map cannot be navigated using the keyboard/screen
          reader, but the units can be browsed on the list available in the same
          view. The map used in the service is produced by a third party.
        </li>
      </ul>
      <H2>Third parties</H2>
      <P>
        The online service uses the following third party services, the
        accessibility of which we cannot be responsible for.
      </P>
      <ul>
        <li>Keycloak user identification service</li>
        <li>Suomi.fi identification</li>
        <li>Leaflet map service</li>
      </ul>
      <H2>Alternative ways of accessing the service</H2>
      <P>
        <strong>Early childhood and pre-primary education customer service</strong>
        <br />
        email{' '}
        <a href="mailto:varhaiskasvatus.asiakaspalvelu@tampere.fi">
          varhaiskasvatus.asiakaspalvelu@tampere.fi
        </a>
        <br />
        telephone <a href="tel:040 800 7260">040 800 7260</a>, calling hours Mon-Fri 9.00–12.00
      </P>
      <H2>Give feedback</H2>
      <P>
        If you notice an accessibility gap in our online service, please let us
        know! You can give us feedback using the{' '}
        <ExternalLink
          href="https://elomake.tampere.fi/lomakkeet/21932/lomakkeet.html"
          text="Give accessibility feedback with this web form"
        />
      </P>
      <H2>Supervisory authority</H2>
      <P>
        If you notice any accessibility issues on the website, please send us,
        the site administrator, feedback first. It may take us up to 14 days to
        reply. If you are not satisfied with the reply or you do not receive a
        reply within two weeks, you can give feedback to the Regional State
        Administrative Agency for Southern Finland. The website of the Regional
        State Administrative Agency for Southern Finland explains in detail how
        a complaint can be submitted, and how the matter will be processed.
      </P>

      <P>
        <strong>Contact information of the supervisory authority</strong>
        <br />
        Regional State Administrative Agency of Southern Finland
        <br />
        Accessibility Supervision Unit
        <br />
        <ExternalLink
          href="https://www.webaccessibility.fi/"
          text="https://www.webaccessibility.fi/"
        />
        <br />
        <a href="mailto:saavutettavuus@avi.fi">saavutettavuus@avi.fi</a>
        <br />
        tel. (exchange) 0295 016 000
        <br />
        Open: Mon-Fri 8.00–16.15
      </P>
    </>
  ),
  vasu: {
    givePermissionToShareInfoVasu:
      "If you want to change the recipients mentioned in the plan, contact the staff of your child's early childhood education unit. When a child changes to another early childhood education unit in the City of Tampere, the early childhood education plan and other early childhood education documents are transferred to the new unit (also applies to early childhood education organized by Tampere as a purchasing service). The guardian will be informed about the transfer in advance. We ask the guardian for permission to hand over the early childhood education plan to an outsider. However, if a child enters private or other municipality's early childhood education, the early childhood education plan must be submitted to the new early childhood education provider even without permission, if the information is necessary for arranging the child's early childhood education (section 41 (3) of the Early Childhood Education Act). In addition, the early childhood education plan must be submitted to the organizer of pre-primary or primary education if it is necessary for the organization of the child's education (section 41 (4) of the Basic Education Act). The guardian will be informed of the transfer in advance."
  }
}

export default en
