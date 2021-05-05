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
  applications: {
    creation: {
      daycareInfo:
        'Varhaiskasvatushakemuksella haetaan paikkaa kunnallisesta päiväkodista tai perhepäivähoidosta, ostopalvelupäiväkodista tai palvelusetelillä tuetusta päiväkodista.',
      clubInfo:
        'Kerhohakemuksella haetaan paikkaa kunnallisista tai palvelusetelillä tuetuista kerhoista.',
      applicationInfo: function ApplicationInfoText() {
        return (
          <P>
            Huoltaja voi tehdä muutoksia hakemukseen verkkopalvelussa siihen
            asti, kun hakemus otetaan asiakaspalvelussa käsittelyyn. Tämän
            jälkeen muutokset tai hakemuksen peruminen on mahdollista ottamalla
            yhteyttä Varhaiskasvatuksen asiakaspalveluun:{' '}
            <a href="mailto:varhaiskasvatus.asiakaspalvelu@tampere.fi">
              varhaiskasvatus.asiakaspalvelu@tampere.fi
            </a>{' '}
            / 040 800 7260 (ma-pe klo 9-12).
          </P>
        )
      }
    },
    editor: {
      heading: {
        info: {
          CLUB: function EditorHeadingInfoClubText() {
            return (
              <>
                <P>
                  Kerhopaikkaa voi hakea ympäri vuoden. Kerhohakemuksella voi
                  hakea kunnallista tai palvelusetelillä tuettua kerhopaikkaa.
                  Kirjallinen ilmoitus kerhopaikasta lähetetään huoltajalle
                  Suomi.fi-viestit-palveluun. Mikäli huoltaja ei ole ottanut
                  Suomi.fi-viestit- palvelua käyttöön, ilmoitus lähetetään
                  hänelle postitse. Paikka myönnetään yhdeksi toimintakaudeksi
                  kerrallaan.
                </P>
                <P>
                  Kerhohakemus kohdistuu yhdelle kerhon toimintakaudelle.
                  Kyseisen kauden päättyessä hakemus poistetaan järjestelmästä.
                </P>
              </>
            )
          }
        }
      },
      serviceNeed: {
        clubDetails: {
          wasOnDaycareInfo:
            'Jos lapsi on ollut kunnallisessa päiväkodissa tai perhepäivähoidossa ja hän luopuu paikastaan kerhon alkaessa, hänellä on suurempi mahdollisuus saada kerhopaikka.',
          wasOnClubCareInfo:
            'Jos lapsi on ollut kerhossa jo edellisen toimintakauden aikana, hänellä on suurempi mahdollisuus saada paikka kerhosta myös tulevana toimintakautena.'
        },
        assistanceNeedInstructions:
          'Jos lapsella on tuen tarve, Tampereen varhaiskasvatuksesta otetaan yhteyttä hakemuksen jättämisen jälkeen.'
      },
      contactInfo: {
        info: function ContactInfoInfoText() {
          return (
            <P>
              Henkilötiedot on haettu väestötiedoista, eikä niitä voi muuttaa
              tällä hakemuksella. Jos henkilötiedoissa on virheitä, päivitäthän
              tiedot{' '}
              <a
                href="https://dvv.fi/henkiloasiakkaat"
                target="_blank"
                rel="noreferrer"
              >
                Digi- ja Väestötietoviraston sivuilla
              </a>
              . Mikäli osoitteenne on muuttumassa, voit lisätä tulevan osoitteen
              erilliseen kohtaan hakemuksella; lisää tuleva osoite sekä lapselle
              että huoltajalle. Virallisena osoitetietoa pidetään vasta, kun se
              on päivittynyt väestötietojärjestelmään. Varhaiskasvatus- ja
              palvelusetelipäätös sekä tieto avoimen varhaiskasvatuksen
              kerhopaikasta toimitetaan automaattisesti myös eri osoitteessa
              asuvalle väestötiedoista löytyvälle huoltajalle.
            </P>
          )
        },
        futureAddressInfo:
          'Tampereen varhaiskasvatuksessa virallisena osoitteena pidetään väestötiedoista saatavaa osoitetta. Osoite väestötiedoissa muuttuu hakijan tehdessä muuttoilmoituksen postiin tai maistraattiin.'
      }
    }
  },
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
  },
  map: {
    mainInfo: `Tässä näkymässä voit hakea kartalta kaikki Tampereen varhaiskasvatusyksiköt sekä kerhot. Kartalta löytyvät myös seudulliset palveluseteliyksiköt ja -kerhot.`,
    searchPlaceholder: 'Esim. Jukolankatu 7 tai Amurin päiväkoti'
  }
}

export default fi
