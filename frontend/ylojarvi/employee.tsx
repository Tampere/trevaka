// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import React from 'react'

import {
  daycareAssistanceLevels,
  otherAssistanceMeasureTypes,
  preschoolAssistanceLevels
} from 'lib-common/generated/api-types/assistance'
import ExternalLink from 'lib-components/atoms/ExternalLink'
import { H3, P } from 'lib-components/typography'
import { EmployeeCustomizations } from 'lib-customizations/types'

import YlojarviLogo from './YlojarviLogo.svg'
import featureFlags from './featureFlags'

const customizations: EmployeeCustomizations = {
  appConfig: {},
  translations: {
    fi: {
      application: {
        serviceNeed: {
          connectedLabel: 'Täydentävä toiminta',
          connectedValue: 'Haen myös täydentävää toimintaa',
          connectedDaycarePreferredStartDateLabel:
            'Täydentävän toivottu aloituspäivä'
        },
        types: {
          PRESCHOOL_DAYCARE: 'Täydentävä varhaiskasvatus'
        },
        decisions: {
          types: {
            PRESCHOOL_DAYCARE: 'Täydentävän varhaiskasvatuksen päätös'
          }
        }
      },
      applications: {
        types: {
          PRESCHOOL_DAYCARE: 'Esiopetus & täydentävä varhaiskasvatus',
          PRESCHOOL_CLUB: 'Esiopetus & esiopetuksen kerho',
          PREPARATORY_DAYCARE: 'Valmistava & täydentävä',
          DAYCARE_ONLY: 'Myöhemmin haettu täydentävä toiminta'
        }
      },
      absences: {
        absenceCategories: {
          NONBILLABLE: 'Esiopetus / kerhotoiminta / koululaisen vuorohoito',
          BILLABLE: 'Varhaiskasvatus / esiopetuksen kerho'
        },
        absenceTypes: {
          PLANNED_ABSENCE: 'Suunniteltu poissaolo',
          FORCE_MAJEURE: 'Hyvityspäivä',
          FREE_ABSENCE: 'Kesäajan maksuton poissaolo'
        },
        absenceTypesShort: {
          PLANNED_ABSENCE: 'Suunniteltu',
          FORCE_MAJEURE: 'Hyvitys'
        },
        absenceTypeInfo: {
          NO_ABSENCE: 'Lapsi läsnä varhaiskasvatuksessa/esiopetustoiminnassa.',
          OTHER_ABSENCE:
            'Käytetään tapauksissa, kun lapsi on poissa varhaiskasvatuksesta/esiopetustoiminnasta huoltajan ilmoituksesta.',
          UNKNOWN_ABSENCE:
            'Käytetään tapauksissa, kun lapsen poissaolosta ei tule huoltajalta mitään ilmoitusta.',
          PLANNED_ABSENCE:
            'Käytetään etukäteen ilmoitetuista poissaoloista. Ilta- ja vuorohoitoyksiköissä kaikille (ml. päiväryhmät), on laitettava merkintä myös viikonlopuille.',
          SICKLEAVE: 'Lapsi sairaana.',
          TEMPORARY_RELOCATION:
            'Käytetään tapauksissa, kun lapselle on tehty varasijoitus toiseen yksikköön.',
          PARENTLEAVE:
            'Käytetään tapauksissa, kun lapsi on poissa varhaiskasvatuksesta/esiopetustoiminnasta isyysvapaan ajan.',
          FORCE_MAJEURE:
            'Käytetään erityistapauksissa, kun lapsen poissaolosta hyvitetään asiakasmaksua.',
          FREE_ABSENCE:
            'Käytetään kesäaikana 8 viikon maksuttomalla ajanjaksolla.'
        },
        careTypes: {
          PRESCHOOL_DAYCARE: 'Täydentävä varhaiskasvatus'
        },
        modal: {
          absenceTypes: {
            PLANNED_ABSENCE: 'Suunniteltu poissaolo',
            FORCE_MAJEURE: 'Hyvityspäivä',
            FREE_ABSENCE: 'Kesäajan maksuton poissaolo'
          }
        }
      },
      common: {
        careTypeLabels: {
          'connected-daycare': 'Täydentävä'
        },
        types: {
          PRESCHOOL_DAYCARE: 'Täydentävä varhaiskasvatus',
          PREPARATORY_DAYCARE: 'Täydentävä varhaiskasvatus'
        }
      },
      childInformation: {
        assistance: {
          assistanceFactor: {
            info: () => (
              <ol style={{ margin: '0', padding: '0 1em' }}>
                <li>
                  Kaupungin päiväkodeissa kerroin merkitään aina integroidussa
                  varhaiskasvatusryhmässä oleville tehostettua tai erityistä
                  tukea saaville lapsille ja missä tahansa ryhmässä kotoutumisen
                  tukea saaville lapsille. Lisäksi kerroin voidaan merkitä missä
                  tahansa ryhmässä olevalle tehostettua tai erityistä tukea
                  saavalle lapselle, mikäli ryhmässä ei ole avustajapalvelua.
                  Kertoimen tallentaa varhaiskasvatuksen erityisopettaja.
                </li>
                <li>
                  Mikäli ostopalvelu- tai palvelusetelipäiväkodissa olevalla
                  lapsella on tehostetun tai erityisen tuen tarve, voidaan
                  hänelle määritellä tuen kerroin. Hallintopäätöksen lapsen
                  tuesta tekee palvelupäällikkö. Päätöksen palvelusetelin
                  korotuksesta tekee varhaiskasvatusjohtaja. Molemmat päätökset
                  tehdään varhaiskasvatuksen erityisopettajan esityksen
                  perusteella. Kertoimen tallentaa varhaiskasvatuksen
                  erityisopettaja.
                </li>
              </ol>
            )
          },
          otherAssistanceMeasure: {
            info: {
              TRANSPORT_BENEFIT: () => (
                <>Lapsi on saanut päätöksen kuljetusedusta.</>
              ),
              ACCULTURATION_SUPPORT: () => (
                <>
                  Lapsen ja perheen kotoutumisen tuki voidaan myöntää, kun
                  perheen lapsi tulee ensimmäistä kertaa suomalaiseen
                  päiväkotiin. Jos perheen muita lapsia on tällä hetkellä tai on
                  ollut aiemmin suomalaisessa päiväkodissa, kotoutumisen tukea
                  ei enää myönnetä. Pakolaistaustaisen perheen ollessa kyseessä
                  aika on 6 kk, muiden osalta 3kk. Kotoutumisen tuki alkaa
                  sijoituksen aloituspäivämäärästä.
                </>
              )
            }
          }
        },
        assistanceNeed: {
          fields: {
            capacityFactorInfo: (
              <ol style={{ margin: '0', padding: '0 1em' }}>
                <li>
                  Kaupungin päiväkodeissa kerroin merkitään aina integroidussa
                  varhaiskasvatusryhmässä oleville tehostettua tai erityistä
                  tukea saaville lapsille ja missä tahansa ryhmässä kotoutumisen
                  tukea saaville lapsille. Lisäksi kerroin voidaan merkitä missä
                  tahansa ryhmässä olevalle tehostettua tai erityistä tukea
                  saavalle lapselle, mikäli näin on yksikössä sovittu. Kertoimen
                  tallentaa varhaiskasvatuksen erityisopettaja.
                </li>
                <li>
                  Mikäli ostopalvelu- tai palvelusetelipäiväkodissa olevalla
                  lapsella on tehostetun tai erityisen tuen tarve, voidaan
                  hänelle määritellä tuen kerroin. Hallintopäätöksen lapsen
                  tuesta tekee palvelupäällikkö. Päätöksen palvelusetelin
                  korotuksesta tekee varhaiskasvatusjohtaja. Molemmat päätökset
                  tehdään varhaiskasvatuksen erityisopettajan esityksen
                  perusteella. Kertoimen tallentaa varhaiskasvatuksen
                  erityisopettaja.
                </li>
              </ol>
            ),
            bases: 'Tuen tarve'
          }
        },
        assistanceNeedDecision: {
          structuralMotivationInfo:
            'Valitse lapsen tarvitsemat rakenteellisen tuen muodot. Jos lapsi on integroidussa varhaiskasvatusryhmässä, valitse "Pienryhmä" ja kirjoita tuen muotojen kuvaukseen, että lapsi on integroidussa varhaiskasvatusryhmässä. Perustele, miksi lapsi saa näitä tuen muotoja.',
          careMotivationInfo:
            'Kirjaa tähän lapsen mahdollisesti tarvitsemat yksilölliset hoidollisen tuen muodot, esim. menetelmät lapsen hoitoon, hoivaan ja avustamiseen huomioiden pitkäaikaissairauksien hoito, erityinen ruokavalio (esim. turvaruoka ravitsemusterapeutin suosituksesta), liikkuminen ja näihin liittyvät, varhaiskasvatukseen osallistumisen kannalta tarpeelliset apuvälineet. Kirjaa tähän vain sellaiset apuvälineet, jotka varhaiskasvatus omistaa tai hankkii. Perustele, miksi lapsi saa näitä tuen muotoja.',
          unitMayChange: '',
          appealInstructions: (
            <>
              <H3>Oikaisuvaatimusoikeus</H3>
              <P>
                Oikaisuvaatimuksen saa tehdä se, johon päätös on kohdistettu tai
                jonka oikeuteen, velvollisuuteen tai etuun päätös välittömästi
                vaikuttaa (asianosainen).
              </P>
              <H3>Oikaisuvaatimusaika</H3>
              <P>
                Oikaisuvaatimus on tehtävä 30 päivän kuluessa päätöksen
                tiedoksisaannista.
              </P>
              <H3>Tiedoksisaanti</H3>
              <P>
                Asianosaisen katsotaan saaneen päätöksestä tiedon, jollei muuta
                näytetä, seitsemän päivän kuluttua kirjeen lähettämisestä tai
                saantitodistukseen tai tiedoksiantotodistukseen merkittynä
                päivänä. Käytettäessä tavallista sähköistä tiedoksiantoa
                asianosaisen katsotaan saaneen päätöksestä tiedon, jollei muuta
                näytetä kolmantena päivänä viestin lähettämisestä.
                Tiedoksisaantipäivää ei lueta määräaikaan. Jos määräajan
                viimeinen päivä on pyhäpäivä, itsenäisyyspäivä, vapunpäivä,
                joulu- tai juhannusaatto tai arkilauantai, saa tehtävän
                toimittaa ensimmäisenä arkipäivänä sen jälkeen.
              </P>
              <H3>Oikaisuviranomainen</H3>
              <P>
                Oikaisu tehdään Länsi- ja Sisä-Suomen aluehallintovirastolle
                (Vaasan päätoimipaikka).
              </P>
              <P>
                Länsi- ja Sisä-Suomen aluehallintovirasto
                <br />
                Wolffintie 35
                <br />
                PL 200, 65101 Vaasa
                <br />
                faksi: 06 317 4817
                <br />
                sähköposti:{' '}
                <ExternalLink
                  href="mailto:kirjaamo.lansi@avi.fi"
                  text="kirjaamo.lansi@avi.fi"
                  newTab
                />
              </P>
              <P>
                Sähköinen lomake oikaisuvaatimuksen tekoa varten on saatavilla
                aluehallintoviraston asiointipalvelussa:
                <br />
                <ExternalLink
                  href="https://www.suomi.fi/palvelut/oppilas-ja-opiskelija-asioita-seka-varhaiskasvatuksen-tukiasioita-koskeva-oikaisuvaatimus-aluehallintovirasto/ee86d56c-1717-4993-b772-8dde0df57b69"
                  text="https://www.suomi.fi/palvelut/oppilas-ja-opiskelija-asioita-seka-varhaiskasvatuksen-tukiasioita-koskeva-oikaisuvaatimus-aluehallintovirasto/ee86d56c-1717-4993-b772-8dde0df57b69"
                  newTab
                />
              </P>
              <H3>Oikaisuvaatimuksen muoto ja sisältö</H3>
              <P>
                Oikaisuvaatimus on tehtävä kirjallisesti. Myös sähköinen
                asiakirja täyttää vaatimuksen kirjallisesta muodosta.
              </P>
              <P noMargin>Oikaisuvaatimuksessa on ilmoitettava</P>
              <ul>
                <li>
                  Oikaisuvaatimuksen tekijän nimi, kotikunta, postiosoite,
                  puhelinnumero ja muut asian hoitamiseksi tarvittavat
                  yhteystiedot
                </li>
                <li>päätös, johon haetaan oikaisua</li>
                <li>
                  miltä osin päätökseen haetaan oikaisua ja mitä oikaisua siihen
                  vaaditaan tehtäväksi
                </li>
                <li>vaatimuksen perusteet</li>
              </ul>
              <P>
                Jos oikaisuvaatimuspäätös voidaan antaa tiedoksi sähköisenä
                viestinä, yhteystietona pyydetään ilmoittamaan myös
                sähköpostiosoite.
              </P>
              <P>
                Jos oikaisuvaatimuksen tekijän puhevaltaa käyttää hänen
                laillinen edustajansa tai asiamiehensä tai jos
                oikaisuvaatimuksen laatijana on joku muu henkilö,
                oikaisuvaatimuksessa on ilmoitettava myös tämän nimi ja
                kotikunta.
              </P>
              <P noMargin>Oikaisuvaatimukseen on liitettävä</P>
              <ul>
                <li>
                  päätös, johon haetaan oikaisua, alkuperäisenä tai
                  jäljennöksenä
                </li>
                <li>
                  todistus siitä, minä päivänä päätös on annettu tiedoksi, tai
                  muu selvitys oikaisuvaatimusajan alkamisen ajankohdasta
                </li>
                <li>
                  asiakirjat, joihin oikaisuvaatimuksen tekijä vetoaa
                  oikaisuvaatimuksensa tueksi, jollei niitä ole jo aikaisemmin
                  toimitettu viranomaiselle.
                </li>
              </ul>
              <H3>Oikaisuvaatimuksen toimittaminen</H3>
              <P>
                Oikaisuvaatimuskirjelmä on toimitettava oikaisuvaatimusajan
                kuluessa oikaisuvaatimusviranomaiselle. Oikaisuvaatimuskirjelmän
                tulee olla perillä oikaisuvaatimusajan viimeisenä päivänä ennen
                viraston aukiolon päättymistä. Oikaisuvaatimuksen lähettäminen
                postitse tai sähköisesti tapahtuu lähettäjän omalla vastuulla.
              </P>
            </>
          ),
          jurisdictionText: () =>
            'Hallintosääntö 113 §, varhaiskasvatuksen aluejohtaja kohta 9.'
        },
        assistanceNeedPreschoolDecision: {
          validFromInfo: () => (
            <div>
              <p>
                Valitse mistä asiasta päätös tehdään: erityisen tuen
                alkamisesta/jatkumisesta/päättymisestä.
              </p>
              <p>
                Jos tuen voimassaolo alkaa ns. heti, kenttään laitetaan
                huoltajien kuulemisen päivämäärä.
              </p>
              <p>
                Jos taas päätös tehdään etukäteen, kenttään laitetaan esim.
                lukuvuoden alkamispäivä tai muu sovittu, tulevaisuuden
                päivämäärä.
              </p>
            </div>
          ),
          extendedCompulsoryEducationInfoInfo: () => (
            <div>
              <p>
                Pidennetyn oppivelvollisuuden ja samalla kertaa tehtävän
                erityisen tuen ensikertaisen päätöksen perusteluina tulee olla
                psykologinen tai lääketieteellinen lausunto, josta tulisi käydä
                ilmi asiantuntijan näkemys vammaisuuden asteesta. Mikäli
                lapselle on jo aiemmin tehty päätös pidennetystä
                oppivelvollisuudesta, uutta lausuntoa ei tarvita.
              </p>
              <p>
                Jos lapselle on jo POV-suosituksessa suositeltu kahta
                esiopetusvuotta, kirjoita se tähän.
              </p>
              <p>
                Kirjoita tähän, koskeeko tämä päätös pidennetyn
                oppivelvollisuuden ensimmäistä vai toista esiopetusvuotta.
              </p>
            </div>
          ),
          grantedAssistanceSectionInfo: () => (
            <div>
              <p>
                Valitse lapsen tarvitsemat palvelut tai apuvälineet.
                Apuvälineet, joista lapsella on jo päätös muualta, eivät sisälly
                tähän päätökseen.
              </p>
              <p>
                Kirjaa alle perustelut-kohtaan, mikäli lapsen ryhmässä ei ole
                avustajaa, mutta lapsen tuen toteuttaminen on jotenkin muuten
                otettu henkilöstöresurssissa huomioon, esim. ryhmässä on
                lisäresurssina lastenhoitaja.
              </p>
            </div>
          ),
          primaryGroupInfo: () =>
            'Kirjoita tähän, millaisessa ryhmässä lapsi saa esiopetusta (esim. integroitu varhaiskasvatusryhmä, esiopetusryhmä, integroitu esiopetusryhmä, esiopetuksen erityisryhmä). Ryhmän nimeä ei kirjoiteta tähän.',
          decisionBasisInfo: () =>
            'Perustele, miksi lapsi tarvitsee mainittua tukea ja tukimuotoja.',
          documentBasisInfo: () =>
            'Valitse, mitä asiakirjoja on tehty ennen tätä päätöstä.',
          heardGuardiansInfo: () => (
            <div>
              <p>
                Kirjaa tähän millä keinoin huoltajaa on kuultu (esim. palaveri,
                etäyhteys, huoltajan kirjallinen vastine). Jos huoltajaa ei ole
                kuultu, kirjaa tähän selvitys siitä, miten ja milloin hänet on
                kutsuttu kuultavaksi, ja miten ja milloin lapsen
                varhaiskasvatussuunnitelma on annettu huoltajalle tiedoksi.
              </p>

              <p>
                Kaikilla lapsen huoltajilla tulee olla mahdollisuus tulla
                kuulluksi. Huoltaja voi tarvittaessa valtuuttaa toisen huoltajan
                edustamaan itseään valtakirjalla
              </p>
            </div>
          ),
          viewOfGuardiansInfo: () =>
            'Kirjaa tähän huoltajien näkemys lapselle esitetystä tuesta.',
          appealInstructions: (
            <>
              <P>
                Tähän päätökseen tyytymätön voi tehdä kirjallisen
                oikaisuvaatimuksen.
              </P>
              <H3>Oikaisuvaatimusoikeus</H3>
              <P>
                Oikaisuvaatimuksen saa tehdä se, johon päätös on kohdistettu tai
                jonka oikeuteen, velvollisuuteen tai etuun päätös välittömästi
                vaikuttaa (asianosainen). Alle 15-vuotiaan lapsen päätökseen
                oikaisua voi hakea lapsen huoltaja tai muu laillinen edustaja.
              </P>
              <H3>Oikaisuviranomainen</H3>
              <P>
                Oikaisu tehdään Länsi- ja Sisä-Suomen aluehallintovirastolle
                (Vaasan päätoimipaikka).
              </P>
              <P>
                Länsi- ja Sisä-Suomen aluehallintovirasto
                <br />
                Wolffintie 35
                <br />
                PL 200, 65101 Vaasa
                <br />
                faksi: 06 317 4817
                <br />
                sähköposti:{' '}
                <ExternalLink
                  href="mailto:kirjaamo.lansi@avi.fi"
                  text="kirjaamo.lansi@avi.fi"
                  newTab
                />
              </P>
              <P>
                Sähköinen lomake oikaisuvaatimuksen tekoa varten on saatavilla
                aluehallintoviraston asiointipalvelussa:
                <br />
                <ExternalLink
                  href="https://www.suomi.fi/palvelut/oppilas-ja-opiskelija-asioita-seka-varhaiskasvatuksen-tukiasioita-koskeva-oikaisuvaatimus-aluehallintovirasto/ee86d56c-1717-4993-b772-8dde0df57b69"
                  text="https://www.suomi.fi/palvelut/oppilas-ja-opiskelija-asioita-seka-varhaiskasvatuksen-tukiasioita-koskeva-oikaisuvaatimus-aluehallintovirasto/ee86d56c-1717-4993-b772-8dde0df57b69"
                  newTab
                />
              </P>
              <H3>Oikaisuvaatimusaika</H3>
              <P>
                Oikaisuvaatimus on tehtävä 14 päivän kuluessa päätöksen
                tiedoksisaannista.
              </P>
              <H3>Tiedoksisaanti</H3>
              <P>
                Asianosaisen katsotaan saaneen päätöksestä tiedon, jollei muuta
                näytetä, 7 päivän kuluttua kirjeen lähettämisestä, 3 päivän
                kuluttua sähköpostin lähettämisestä, saantitodistuksen
                osoittamana aikana tai erilliseen tiedoksisaantitodistukseen
                merkittynä aikana. Tiedoksisaantipäivää ei lueta määräaikaan.
                Jos määräajan viimeinen päivä on pyhäpäivä, itsenäisyyspäivä,
                vapunpäivä, joulu- tai juhannusaatto tai arkilauantai, saa
                tehtävän toimittaa ensimmäisenä arkipäivänä sen jälkeen.
              </P>
              <H3>Oikaisuvaatimus</H3>
              <P noMargin>Oikaisuvaatimuksessa on ilmoitettava</P>
              <ul>
                <li>
                  Oikaisuvaatimuksen tekijän nimi, kotikunta, postiosoite,
                  puhelinnumero
                </li>
                <li>päätös, johon haetaan oikaisua</li>
                <li>
                  miltä osin päätökseen haetaan oikaisua ja mitä oikaisua siihen
                  vaaditaan tehtäväksi
                </li>
                <li>vaatimuksen perusteet</li>
              </ul>
              <P noMargin>Oikaisuvaatimukseen on liitettävä</P>
              <ul>
                <li>
                  päätös, johon haetaan oikaisua, alkuperäisenä tai
                  jäljennöksenä
                </li>
                <li>
                  todistus siitä, minä päivänä päätös on annettu tiedoksi, tai
                  muu selvitys oikaisuvaatimusajan alkamisen ajankohdasta
                </li>
                <li>
                  asiakirjat, joihin oikaisuvaatimuksen tekijä vetoaa
                  oikaisuvaatimuksensa tueksi, jollei niitä ole jo aikaisemmin
                  toimitettu viranomaiselle.
                </li>
              </ul>
              <P>
                Asiamiehen on liitettävä valituskirjelmään valtakirja, kuten
                oikeudenkäynnistä hallintoasioissa annetun lain (808/2019) 32
                §:ssä säädetään
              </P>
              <H3>Oikaisuvaatimuksen toimittaminen</H3>
              <P>
                Oikaisuvaatimuskirjelmä on toimitettava oikaisuvaatimusajan
                kuluessa oikaisuvaatimusviranomaiselle. Oikaisuvaatimuskirjelmän
                tulee olla perillä oikaisuvaatimusajan viimeisenä päivänä ennen
                viraston aukiolon päättymistä. Oikaisuvaatimuksen lähettäminen
                postitse tai sähköisesti tapahtuu lähettäjän omalla vastuulla.
              </P>
            </>
          ),
          jurisdictionText:
            'Hallintosääntö 113 §, varhaiskasvatuksen aluejohtaja kohta 9.',
          lawReference:
            'Laki viranomaisen toiminnan julkisuudesta 24 § 1 mom. 30 kohta'
        },
        assistanceAction: {
          title: 'Tukitoimet ja tukipalvelut',
          fields: {
            actions: 'Tukitoimet ja tukipalvelut'
          }
        },
        dailyServiceTimes: {
          info: 'Tallenna tähän varhaiskasvatussopimuksella sovittu päivittäinen läsnäoloaika.',
          info2: ''
        },
        application: {
          types: {
            PRESCHOOL_DAYCARE: 'Täydentävä varhaiskasvatus'
          }
        }
      },
      personProfile: {
        application: {
          types: {
            PRESCHOOL_WITH_DAYCARE: 'Esiopetus + täydentävä',
            PRESCHOOL_DAYCARE: 'Täydentävä varhaiskasvatus',
            PREPARATORY_EDUCATION: 'Valmistava opetus',
            PREPARATORY_WITH_DAYCARE: 'Valmistava opetus + täydentävä'
          }
        }
      },
      placementDraft: {
        preschoolDaycare: 'Täydentävä toiminta'
      },
      footer: {
        cityLabel: 'Ylöjärven kaupunki',
        linkLabel: 'Ylöjärven varhaiskasvatus',
        linkHref: 'https://www.ylojarvi.fi/kasvatus-ja-opetus/varhaiskasvatus/'
      },
      unit: {
        placementProposals: {
          rejectReasons: {
            REASON_1: 'Päiväkoti täynnä',
            REASON_2: 'Sisäilma tai muu rakenteellinen syy',
            REASON_3: 'Henkilökuntaa tilapäisesti vähennetty'
          },
          infoTitle: '',
          infoText: ''
        }
      },
      login: {
        loginAD: 'Ylöjärvi AD'
      },
      placement: {
        type: {
          DAYCARE: 'Kokopäiväinen varhaiskasvatus',
          DAYCARE_PART_TIME: 'Osapäiväinen varhaiskasvatus',
          TEMPORARY_DAYCARE: 'Tilapäinen kokopäiväinen varhaiskasvatus',
          PRESCHOOL_DAYCARE:
            'Esiopetus ja esiopetusta täydentävä varhaiskasvatus',
          PRESCHOOL_DAYCARE_ONLY: 'Esiopetusta täydentävä varhaiskasvatus',
          PRESCHOOL_CLUB: 'Esiopetus ja esiopetuksen kerhotoiminta',
          CLUB: 'Kerho',
          SCHOOL_SHIFT_CARE: 'Koululaisten vuorohoito',
          PRESCHOOL_WITH_DAYCARE:
            'Esiopetus ja esiopetusta täydentävä varhaiskasvatus',
          PREPARATORY_WITH_DAYCARE:
            'Valmistava opetus ja täydentävä varhaiskasvatus',
          PREPARATORY_DAYCARE:
            'Valmistava opetus ja täydentävä varhaiskasvatus',
          PREPARATORY_DAYCARE_ONLY:
            'Valmistavaa opetusta täydentävä varhaiskasvatus'
        }
      },
      decisionDraft: {
        types: {
          PRESCHOOL_DAYCARE: 'Esiopetusta täydentävä varhaiskasvatus',
          PREPARATORY_DAYCARE: 'Valmistavaa opetusta täydentävä varhaiskasvatus'
        }
      },
      unitEditor: {
        placeholder: {
          phone: 'esim. +358 40 555 5555',
          email: 'etunimi.sukunimi@ylojarvi.fi',
          url: 'esim. https://www.ylojarvi.fi/yhteystiedot/peiponpellon-paivakoti/',
          streetAddress: 'Kadun nimi esim. Rautialantie 60',
          decisionCustomization: {
            name: 'esim. Peiponpellon päiväkoti'
          }
        },
        field: {
          decisionCustomization: {
            handler: ['Palveluohjaus', 'Varhaiskasvatusyksikön johtaja']
          }
        }
      },
      welcomePage: {
        text: 'Olet kirjautunut sisään eVaka Ylöjärvi -palveluun. Käyttäjätunnuksellesi ei ole vielä annettu oikeuksia, jotka mahdollistavat palvelun käytön. Tarvittavat käyttöoikeudet saat omalta esihenkilöltäsi.'
      },
      invoices: {
        buttons: {
          individualSendAlertText: ''
        }
      },
      preferredFirstName: {
        description:
          'Voit määritellä eVakassa käytössä olevan kutsumanimesi. Kutsumanimen tulee olla jokin etunimistäsi.'
      },
      reports: {
        decisions: {
          connectedDaycareOnly: 'Myöhemmin haetun täydentävän päätöksiä',
          preschoolDaycare: 'Esiopetus+täydentävä päätöksiä',
          preparatoryDaycare: 'Valmistava+täydentävä päätöksiä'
        },
        placementSketching: {
          connected: 'Täydentävä'
        }
      },
      roles: {
        adRoles: {
          DIRECTOR: 'Vaka-päälliköt'
        }
      },
      terms: {
        extendedTermStartInfo:
          'Aika, jolloin varhaiskasvatusmaksu määräytyy täydentävän varhaiskasvatuksen mukaan.'
      }
    },
    sv: {}
  },
  cityLogo: {
    src: YlojarviLogo,
    alt: 'Ylöjärvi logo'
  },
  featureFlags,
  absenceTypes: [
    'OTHER_ABSENCE',
    'SICKLEAVE',
    'UNKNOWN_ABSENCE',
    'PLANNED_ABSENCE',
    'PARENTLEAVE',
    'FORCE_MAJEURE',
    'FREE_ABSENCE',
    'UNAUTHORIZED_ABSENCE'
  ],
  daycareAssistanceLevels: daycareAssistanceLevels.filter(
    (level) => level !== 'GENERAL_SUPPORT'
  ),
  otherAssistanceMeasureTypes: otherAssistanceMeasureTypes.filter(
    (level) => level !== 'ANOMALOUS_EDUCATION_START'
  ),
  placementTypes: [
    'DAYCARE',
    'TEMPORARY_DAYCARE',
    'PRESCHOOL',
    'PRESCHOOL_DAYCARE',
    'PRESCHOOL_DAYCARE_ONLY',
    'PREPARATORY',
    'PREPARATORY_DAYCARE',
    'PREPARATORY_DAYCARE_ONLY',
    'CLUB',
    'SCHOOL_SHIFT_CARE'
  ],
  placementPlanRejectReasons: ['REASON_1', 'REASON_2', 'REASON_3', 'OTHER'],
  preschoolAssistanceLevels: [...preschoolAssistanceLevels],
  unitProviderTypes: [
    'MUNICIPAL',
    'PURCHASED',
    'PRIVATE',
    'PRIVATE_SERVICE_VOUCHER'
  ],
  voucherValueDecisionTypes: ['NORMAL', 'RELIEF_ACCEPTED', 'RELIEF_REJECTED']
}

export default customizations
