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
      Varhaiskasvatuksen asiakaspalveluun:{' '}
      <a
        style={{ wordBreak: 'break-word' }}
        href="mailto:varhaiskasvatus.asiakaspalvelu@tampere.fi"
      >
        varhaiskasvatus.asiakaspalvelu@tampere.fi
      </a>{' '}
      / <a href="tel:+358408007260">040 800 7260</a> (ma–pe klo 9–12).
    </>
  )
}

const fi: DeepPartial<Translations> = {
  applications: {
    creation: {
      daycareInfo:
        'Varhaiskasvatushakemuksella haetaan paikkaa kunnallisesta päiväkodista tai perhepäivähoidosta, ostopalvelupäiväkodista tai palvelusetelillä tuetusta päiväkodista.',
      clubInfo:
        'Kerhohakemuksella haetaan paikkaa kunnallisista tai palvelusetelillä tuetuista kerhoista.',
      preschoolInfo:
        'TODO: Maksutonta esiopetusta on neljä tuntia päivässä. Tämän lisäksi lapselle voidaan hakea maksullista täydentävää varhaiskasvatusta, jota tarjotaan esiopetuspaikoissa aamulla ennen esiopetuksen alkua ja iltapäivisin esiopetuksen jälkeen. Täydentävään varhaiskasvatukseen voi hakea myös palveluseteliä, valitsemalla Hakutoiveet-kohtaan palveluseteliyksikön, johon halutaan hakea. Hakemuksen täydentävään varhaiskasvatukseen voi tehdä esiopetukseen ilmoittautumisen yhteydessä tai erillisenä hakemuksena opetuksen jo alettua. Samalla hakemuksella voit hakea myös maksuttomaan valmistavaan opetukseen sekä valmistavaa opetusta täydentävään varhaiskasvatukseen.',
      preschoolDaycareInfo:
        'TODO: Täydentävän varhaiskasvatuksen hakeminen lapsille, jotka ilmoitetaan / on ilmoitettu esiopetukseen tai perusopetukseen valmistavaan opetukseen',
      applicationInfo: (
        <P>
          Huoltaja voi tehdä muutoksia hakemukseen verkkopalvelussa siihen asti,
          kun hakemus otetaan asiakaspalvelussa käsittelyyn. Tämän jälkeen
          muutokset tai hakemuksen peruminen on mahdollista ottamalla yhteyttä
          {customerContactText()}
        </P>
      ),
      duplicateWarning:
        'Lapsella on jo samantyyppinen, keskeneräinen hakemus. Palaa Hakemukset-näkymään ja muokkaa olemassa olevaa hakemusta tai ota yhteyttä Varhaiskasvatuksen asiakaspalveluun.',
      transferApplicationInfo: {
        DAYCARE:
          'Lapsella on jo paikka Tampereen varhaiskasvatuksessa. Tällä hakemuksella voit hakea siirtoa toiseen varhaiskasvatusta tarjoavaan yksikköön Tampereella.',
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
                Varhaiskasvatuspaikkaa voi hakea ympäri vuoden.
                Varhaiskasvatushakemus tulee jättää viimeistään neljä kuukautta
                ennen hoidon toivottua alkamisajankohtaa. Mikäli
                varhaiskasvatuksen tarve johtuu työllistymisestä, opinnoista tai
                koulutuksesta, eikä hoidon tarpeen ajankohtaa ole pystynyt
                ennakoimaan, on varhaiskasvatuspaikkaa haettava mahdollisimman
                pian - kuitenkin viimeistään kaksi viikkoa ennen kuin lapsi
                tarvitsee hoitopaikan.
              </P>
              <P>
                Kirjallinen päätös varhaiskasvatuspaikasta lähetetään
                Suomi.fi-viestit -palveluun. Mikäli haluatte päätöksen
                sähköisenä tiedoksiantona, teidän tulee ottaa Suomi.fi-viestit
                -palvelu käyttöön. Palvelusta ja sen käyttöönotosta saatte
                lisätietoa{' '}
                <ExternalLink
                  text="https://www.suomi.fi/viestit"
                  href="https://www.suomi.fi/viestit"
                  newTab
                />
                . Mikäli ette ota Suomi.fi-viestit -palvelua käyttöön, päätös
                lähetetään teille postitse.
              </P>
              <P fitted={true}>* Tähdellä merkityt tiedot ovat pakollisia</P>
            </>
          ),
          CLUB: (
            <>
              <P>
                Kerhopaikkaa voi hakea ympäri vuoden. Kerhohakemuksella voi
                hakea kunnallista tai palvelusetelillä tuettua kerhopaikkaa.
                Kirjallinen ilmoitus kerhopaikasta lähetään Suomi.fi-viestit
                -palveluun. Mikäli haluatte ilmoituksen sähköisenä
                tiedoksiantona, teidän tulee ottaa Suomi.fi-viestit -palvelu
                käyttöön. Palvelusta ja sen käyttöönotosta saatte lisätietoa{' '}
                <ExternalLink
                  text="https://www.suomi.fi/viestit"
                  href="https://www.suomi.fi/viestit"
                  newTab
                />
                . Mikäli ette ota Suomi.fi-viestit -palvelua käyttöön, ilmoitus
                kerhopaikasta lähetetään teille postitse. Paikka myönnetään
                yhdeksi toimintakaudeksi kerrallaan.
              </P>
              <P>
                Kerhohakemus kohdistuu yhdelle kerhon toimintakaudelle. Kyseisen
                kauden päättyessä hakemus poistetaan järjestelmästä.
              </P>
            </>
          )
        }
      },
      serviceNeed: {
        startDate: {
          instructions: (
            <>
              Toivottua aloituspäivää on mahdollista muuttaa myöhemmäksi niin
              kauan, kun hakemusta ei ole otettu käsittelyyn. Tämän jälkeen
              toivotun aloituspäivän muutokset tehdään ottamalla yhteyttä
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
            'Jos lapsi on ollut kunnallisessa päiväkodissa tai perhepäivähoidossa ja hän luopuu paikastaan kerhon alkaessa, hänellä on suurempi mahdollisuus saada kerhopaikka.',
          wasOnClubCareInfo:
            'Jos lapsi on ollut kerhossa jo edellisen toimintakauden aikana, hänellä on suurempi mahdollisuus saada paikka kerhosta myös tulevana toimintakautena.'
        },
        urgent: {
          attachmentsMessage: {
            text: (
              <P fitted={true}>
                Mikäli varhaiskasvatuspaikan tarve johtuu äkillisestä
                työllistymisestä tai opiskelupaikan saamisesta, tulee paikkaa
                hakea viimeistään kaksi viikkoa ennen kuin hoidon tarve alkaa.
                Lisäksi huoltajan tulee ottaa yhteyttä viipymättä
                {customerContactText()}
              </P>
            )
          }
        },
        shiftCare: {
          instructions:
            'Päiväkodit palvelevat normaalisti arkisin klo 6.00–18.00. Iltahoito on tarkoitettu lapsille, jotka vanhempien työn tai tutkintoon johtavan opiskelun vuoksi tarvitsevat säännöllisesti hoitoa klo 18.00 jälkeen. Iltahoitoa tarjoavat päiväkodit aukeavat tarvittaessa klo 5.30 ja menevät kiinni viimeistään klo 22.30. Osa iltahoitoa antavista päiväkodeista on auki myös viikonloppuisin. Vuorohoito on tarkoitettu lapsille, joiden vanhemmat tekevät vuorotyötä ja lapsen hoitoon sisältyy myös öitä.',
          message: {
            text: 'Ilta- ja vuorohoito on tarkoitettu lapsille, jotka vanhempien työn tai tutkintoon johtavan opiskelun vuoksi tarvitsevat ilta- ja vuorohoitoa. Hakemuksen liitteeksi on toimitettava vanhempien osalta työnantajan todistus vuorotyöstä tai opiskelusta johtuvasta ilta- tai vuorohoidon tarpeesta.'
          },
          attachmentsMessage: {
            text: 'Ilta- ja vuorohoito on tarkoitettu lapsille, jotka vanhempien työn tai tutkintoon johtavan opiskelun vuoksi tarvitsevat ilta- ja vuorohoitoa. Hakemuksen liitteeksi on toimitettava vanhempien osalta työnantajan todistus vuorotyöstä tai opiskelusta johtuvasta ilta- tai vuorohoidon tarpeesta.'
          }
        },
        assistanceNeedInstructions: {
          DAYCARE:
            'Tehostettua tai erityistä tukea annetaan lapselle heti tarpeen ilmettyä. Mikäli lapsella on olemassa tuen tarpeesta asiantuntijalausunto, tämä tulee ilmoittaa varhaiskasvatushakemuksella. Tukitoimet toteutuvat lapsen arjessa osana varhaiskasvatuksen toimintaa. Tampereen varhaiskasvatuksesta otetaan erikseen yhteyttä hakemuksen jättämisen jälkeen, jos lapsella on tuen tarve.',
          CLUB: 'Jos lapsella on tuen tarve, Tampereen varhaiskasvatuksesta otetaan yhteyttä hakemuksen jättämisen jälkeen.'
        },
        partTime: {
          true: 'Osapäiväinen'
        },
        dailyTime: {
          label: {
            DAYCARE: 'Palveluntarve',
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
            Henkilötiedot on haettu väestötiedoista, eikä niitä voi muuttaa
            tällä hakemuksella. Jos henkilötiedoissa on virheitä, päivitäthän
            tiedot{' '}
            <ExternalLink
              text="Digi- ja Väestötietoviraston sivuilla"
              href="https://dvv.fi/henkiloasiakkaat"
              newTab
            />
            . Mikäli osoitteenne on muuttumassa, voit lisätä tulevan osoitteen
            erilliseen kohtaan hakemuksella; lisää tuleva osoite sekä lapselle
            että huoltajalle. Virallisena osoitetietoa pidetään vasta, kun se on
            päivittynyt väestötietojärjestelmään. Varhaiskasvatus- ja
            palvelusetelipäätös sekä tieto avoimen varhaiskasvatuksen
            kerhopaikasta toimitetaan automaattisesti myös eri osoitteessa
            asuvalle väestötiedoista löytyvälle huoltajalle.
          </P>
        ),
        futureAddressInfo:
          'Tampereen varhaiskasvatuksessa virallisena osoitteena pidetään väestötiedoista saatavaa osoitetta. Osoite väestötiedoissa muuttuu hakijan tehdessä muuttoilmoituksen postiin tai maistraattiin.'
      },
      fee: {
        info: {
          DAYCARE: (
            <P>
              Kunnallisen varhaiskasvatuksen asiakasmaksu ja palvelusetelin
              omavastuuosuus perustuu varhaiskasvatuksen asiakasmaksuista
              annettuun lakiin (Laki varhaiskasvatuksen asiakasmaksuista
              (1503/2016)). Asiakasmaksu määräytyy perheen koon, palveluntarpeen
              sekä bruttotulojen mukaan. Uusien asiakkaiden tulee täyttää
              asiakasmaksulomake ja toimittaa tarvittavat liitteet
              Varhaiskasvatuksen asiakasmaksuihin viimeistään kuukauden kuluessa
              hoidon alkamisesta.
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
            Lisätietoa varhaiskasvatuksen asiakasmaksuista löydät{' '}
            <ExternalLink
              href="https://www.tampere.fi/varhaiskasvatusasiakasmaksut"
              text="Tampereen kaupungin sivuilta"
              newTab
            />
          </P>
        )
      },
      additionalDetails: {
        dietInfo: (
          <>
            Erityisruokavaliosta huoltaja toimittaa varhaiskasvatuspaikkaan
            lääkärin tai ravitsemusterapeutin täyttämän ja allekirjoittaman{' '}
            <ExternalLink
              href="https://www.tampere.fi/erityisruokavaliot"
              text="Selvitys erityisruokavaliosta -lomakkeen"
              newTab
            />
            , joka on määräaikainen.
          </>
        )
      }
    }
  },
  applicationsList: {
    title: 'Hakeminen varhaiskasvatukseen',
    summary: (
      <P width="800px">
        Lapsen huoltaja voi tehdä lapselleen hakemuksen varhaiskasvatukseen ja
        kerhoon. Huoltajan lasten tiedot haetaan tähän näkymään automaattisesti
        Väestötietojärjestelmästä.
      </P>
    )
  },
  children: {
    pageDescription:
      'Tällä sivulla näet lastesi varhaiskasvatukseen liittyvät yleiset tiedot.'
  },
  footer: {
    cityLabel: '© Tampereen kaupunki',
    privacyPolicyLink: (
      <ExternalLink
        href="https://www.tampere.fi/tietosuoja"
        text="Tietosuojaselosteet"
        newTab={true}
        data-qa="footer-policy-link"
      ></ExternalLink>
    ),
    sendFeedbackLink: (
      <ExternalLink
        href="https://www.tampere.fi/palaute"
        text="Lähetä palautetta"
        newTab={true}
        data-qa="footer-feedback-link"
      ></ExternalLink>
    )
  },
  loginPage: {
    applying: {
      infoBullets: [
        'hakea lapsellesi varhaiskasvatus- tai kerhopaikkaa tai tarkastella aiemmin tekemääsi hakemusta',
        'tarkastella lapsesi varhaiskasvatukseen liittyviä kuvia ja muita dokumentteja',
        'ilmoittaa omat tai lapsesi tulotiedot',
        'hyväksyä lapsesi varhaiskasvatus- tai kerhopaikan',
        'irtisanoa lapsen varhaiskasvatus- tai kerhopaikan.'
      ]
    },
    login: {
      paragraph:
        'Huoltajat, joiden lapsi on jo varhaiskasvatuksessa: hoida lapsesi päivittäisiä varhaiskasvatusasioita kuten lue viestejä ja ilmoita lapsen läsnäoloajat ja poissaolot.'
    },
    title: 'Tampereen kaupungin varhaiskasvatus'
  },
  map: {
    mainInfo: `Tässä näkymässä voit hakea kartalta kaikki Tampereen varhaiskasvatusyksiköt sekä kerhot. Kartalta löytyvät myös seudulliset palveluseteliyksiköt ja -kerhot.`,
    privateUnitInfo: <></>,
    serviceVoucherLink:
      'https://www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus/paivakodit.html#palvelusetelipaivakodit',
    searchPlaceholder: 'Esim. Amurin päiväkoti'
  },
  decisions: {
    summary: (
      <P width="800px">
        Tälle sivulle saapuvat lapsen varhaiskasvatus- ja kerhohakemuksiin
        liittyvät päätökset ja ilmoitukset.
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
          Tällä sivulla voit lähettää selvitykset varhaiskasvatusmaksuun
          vaikuttavista tuloistasi. Voit myös tarkastella palauttamiasi
          tuloselvityksiä ja muokata tai poistaa niitä kunnes viranomainen on
          käsitellyt tiedot. Lomakkeen käsittelyn jälkeen voit päivittää
          tulotietojasi toimittamalla uuden lomakkeen.
        </p>
        <p data-qa="income-description-p2">
          <strong>
            Molempien samassa taloudessa asuvien aikuisten tulee toimittaa omat
            erilliset tuloselvitykset.
          </strong>
        </p>
        <p data-qa="income-description-p3">
          Kunnallisen varhaiskasvatuksen asiakasmaksut määräytyvät
          prosenttiosuutena perheen bruttotuloista. Maksut vaihtelevat perheen
          koon ja tulojen sekä varhaiskasvatusajan mukaan.
        </p>
        <p data-qa="income-description-p4">
          <a href="https://www.tampere.fi/varhaiskasvatusasiakasmaksut">
            Lisätietoja asiakasmaksuista
          </a>
        </p>
      </>
    ),
    formDescription: (
      <>
        <P data-qa="income-formDescription-p1">
          Tuloselvitys liitteineen palautetaan kuukauden kuluessa
          varhaiskasvatuksen aloittamisesta. Maksu voidaan määrätä
          puutteellisilla tulotiedoilla korkeimpaan maksuun. Puutteellisia
          tulotietoja ei korjata takautuvasti oikaisuvaatimusajan jälkeen.
        </P>
        <P>
          Asiakasmaksu peritään päätöksen mukaisesta varhaiskasvatuksen
          alkamispäivästä lähtien.
        </P>
        <P>
          Asiakkaan on viipymättä ilmoitettava tulojen ja perhekoon muutoksista
          varhaiskasvutuksen asiakasmaksuihin. Viranomainen on tarvittaessa
          oikeutettu perimään varhaiskasvatusmaksuja myös takautuvasti.
        </P>
        <P>
          <strong>Huomioitavaa:</strong>
        </P>
        <Gap size="xs" />
        <UnorderedList data-qa="income-formDescription-ul">
          <li>
            Jos tulosi ylittävät perhekoon mukaisen tulorajan, hyväksy korkein
            varhaiskasvatusmaksu. Tällöin sinun ei tarvitse selvittää tulojasi
            lainkaan.
          </li>
          <li>
            Jos perheeseesi kuuluu toinen aikuinen, myös hänen on toimitettava
            tuloselvitys tunnistautumalla eVakaan omilla henkilötiedoillaan ja
            täyttämällä tämä lomake.
          </li>
          <li>
            Katso voimassaolevat tulorajat{' '}
            <a
              target="_blank"
              rel="noreferrer"
              href="https://www.tampere.fi/varhaiskasvatusasiakasmaksut"
            >
              tästä
            </a>
            .
          </li>
        </UnorderedList>
        <P>* Tähdellä merkityt tiedot ovat pakollisia</P>
      </>
    )
  },
  calendar: {
    absenceModal: {
      selectChildrenInfo:
        'Ilmoita tässä vain koko päivän poissaolot.'
    }
  }
}

export default fi
