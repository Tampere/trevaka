// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import React from 'react'

import ExternalLink from 'lib-components/atoms/ExternalLink'
import UnorderedList from 'lib-components/atoms/UnorderedList'
import { H1, H2, H3, P } from 'lib-components/typography'
import { Gap } from 'lib-components/white-space'
import { Translations } from 'lib-customizations/citizen'
import { DeepPartial } from 'lib-customizations/types'

import featureFlags from '../lempaala/featureFlags'

export const preschoolEnabled = featureFlags.preschool

const customerContactText = function () {
  return (
    <ExternalLink
      href="https://lempaala.fi/palvelut/kasvatus-ja-opetus/varhaiskasvatus/"
      text="varhaiskasvatuksen asiakaspalveluun"
    />
  )
}

const fi: DeepPartial<Translations> = {
  applications: {
    creation: {
      daycareInfo:
        'Varhaiskasvatushakemuksella haetaan paikkaa kunnallisesta päiväkodista tai perhepäivähoidosta, ostopalvelupäiväkodista tai palvelusetelillä tuetusta päiväkodista.',
      clubInfo:
        'Kerhohakemuksella haetaan paikkaa kunnallisista tai palvelusetelillä tuetuista kerhoista.',
      preschoolLabel: 'Esiopetukseen ilmoittautuminen',
      preschoolInfo:
        'Maksutonta esiopetusta on neljä tuntia päivässä. Tämän lisäksi lapselle voidaan hakea maksullista täydentävää toimintaa. Hakemuksen täydentävään toimintaan voi tehdä esiopetukseen ilmoittautumisen yhteydessä tai erillisenä hakemuksena opetuksen jo alettua. Täydentävän toiminnan paikkaa ei pysty hakemaan tietystä yksiköstä, vaan se määräytyy aina esiopetusyksikön mukaan. Mikäli täydentävän toiminnan paikkaa haetaan myöhemmin, hakemukselle merkitään myös esiopetuksen tarve.',
      preschoolDaycareInfo:
        'Täydentävän toiminnan hakeminen lapsille, jotka ilmoitetaan / on ilmoitettu esiopetukseen',
      applicationInfo: (
        <P>
          Huoltaja voi tehdä muutoksia hakemukseen verkkopalvelussa siihen asti,
          kun hakemus otetaan asiakaspalvelussa käsittelyyn. Tämän jälkeen
          muutokset tai hakemuksen peruminen on mahdollista ottamalla yhteyttä
          {customerContactText()}.
        </P>
      ),
      duplicateWarning:
        'Lapsella on jo samantyyppinen, keskeneräinen hakemus. Palaa Hakemukset-näkymään ja muokkaa olemassa olevaa hakemusta tai ota yhteyttä Varhaiskasvatuksen asiakaspalveluun.',
      transferApplicationInfo: {
        DAYCARE:
          'Lapsella on jo paikka Lempäälän varhaiskasvatuksessa. Tällä hakemuksella voit hakea siirtoa toiseen varhaiskasvatusta tarjoavaan yksikköön Lempäälässä.',
        PRESCHOOL:
          'Lapsella on jo esiopetuspaikka. Tällä hakemuksella voit hakea esiopetusta täydentävää toimintaa tai siirtoa toiseen esiopetusta tarjoavaan yksikköön.'
      }
    },
    editor: {
      actions: {
        allowOtherGuardianAccess: (
          <span>
            Ymmärrän, että tieto hakemuksesta menee myös lapsen toiselle
            huoltajalle. Jos tieto ei saa mennä toiselle huoltajalle, ole
            yhteydessä {customerContactText()}.
          </span>
        )
      },
      verification: {
        serviceNeed: {
          connectedDaycare: {
            label: 'Esiopetusta täydentävä toiminta',
            withConnectedDaycare: 'Haen esiopetusta täydentävää toimintaa.'
          },
          startDate: {
            title: {
              PRESCHOOL: 'Esiopetuksen aloitus'
            }
          }
        }
      },
      unitPreference: {
        siblingBasis: {
          checkbox: {
            PRESCHOOL:
              'Toivomme ensisijaisesti esiopetukseen samaan kouluun, jossa lapsen vanhempi sisarus on.'
          },
          info: {
            PRESCHOOL: null
          }
        },
        units: {
          info: {
            PRESCHOOL: (
              <>
                <P>
                  Esiopetuspaikka määräytyy lapsen kotiosoitteen mukaan, tulevan
                  koulupolun mukaisesti. Ilmoittautuessa lapsi ilmoitetaan
                  koulupolun mukaiseen esiopetukseen tai painotettuun
                  esiopetukseen. Mikäli lapsi tarvitsee säännöllisesti ilta- tai
                  vuorohoitoa, hänet ilmoitetaan ilta- tai vuorohoidon
                  esiopetukseen.
                </P>
                <P>
                  Tieto tulevasta esiopetuspaikasta ilmoitetaan huoltajille
                  sähköisesti Suomi.fi-viestit palvelun kautta. Mikäli huoltaja
                  ei ole ottanut palvelua käyttöönsä, tieto lähetetään hänelle
                  kirjeitse.
                </P>
                <P>
                  Lisätietoa{' '}
                  <ExternalLink
                    href="https://lempaala.fi/palvelut/kasvatus-ja-opetus/esiopetus/"
                    text="https://lempaala.fi/palvelut/kasvatus-ja-opetus/esiopetus/"
                    newTab
                  />
                </P>
              </>
            )
          },
          serviceVoucherLink:
            'https://lempaala.fi/palvelut/kasvatus-ja-opetus/varhaiskasvatus/yksityiset-paivakodit/'
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
          PRESCHOOL: (
            <>
              <P>
                Perusopetuslain (26 a §) mukaan lasten on oppivelvollisuuden
                alkamista edeltävänä vuonna osallistuttava vuoden kestävään
                esiopetukseen tai muuhun esiopetuksen tavoitteet saavuttavaan
                toimintaan. Esiopetus on maksutonta.
              </P>
              <P>
                Päätökset tulevat{' '}
                <a
                  href="https://www.suomi.fi/viestit"
                  target="_blank"
                  rel="noreferrer"
                >
                  Suomi.fi-viestit
                </a>{' '}
                -palveluun tai postitse, mikäli et ole ottanut Suomi.fi
                -palvelua käyttöön.
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
          instructions: {
            DAYCARE: (
              <>
                Toivottua aloituspäivää on mahdollista muuttaa myöhemmäksi niin
                kauan, kun hakemusta ei ole otettu käsittelyyn. Tämän jälkeen
                toivotun aloituspäivän muutokset tehdään ottamalla yhteyttä{' '}
                {customerContactText()}.
              </>
            ),
            PRESCHOOL: null
          },
          info: {
            PRESCHOOL: [
              'Lukuvuosi 2024-2025 alkaa keskiviikkona 7.8.2024 ja päättyy keskiviikkona 28.5.2025.'
            ]
          },
          label: {
            PRESCHOOL: 'Tarve alkaen'
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
                Lisäksi huoltajan tulee ottaa yhteyttä viipymättä{' '}
                {customerContactText()}.
              </P>
            )
          }
        },
        shiftCare: {
          instructions:
            'Mikäli lapsi tarvitsee esiopetuksen lisäksi ilta-/vuorohoitoa, hänet pitää ilmoittaa ilta- tai vuorohoidon esiopetukseen. Lisäksi täydentäväksi toiminnaksi on lapselle valittava täydentävä varhaiskasvatus, yli 5h päivässä. Päiväkodit palvelevat normaalisti arkisin klo 6.00–18.00. Iltahoito on tarkoitettu lapsille, jotka vanhempien työn tai tutkintoon johtavan opiskelun vuoksi tarvitsevat säännöllisesti hoitoa klo 18.00 jälkeen. Iltahoitoa tarjoavat päiväkodit aukeavat tarvittaessa klo 5.30 ja menevät kiinni viimeistään klo 22.30. Osa iltahoitoa antavista päiväkodeista on auki myös viikonloppuisin. Vuorohoito on tarkoitettu niille lapsille, joiden vanhemmat tekevät vuorotyötä ja lapsen hoitoon sisältyy myös öitä.',
          message: {
            text: 'Ilta- ja vuorohoito on tarkoitettu lapsille, jotka vanhempien työn tai tutkintoon johtavan opiskelun vuoksi tarvitsevat ilta- ja vuorohoitoa. Hakemuksen liitteeksi on toimitettava vanhempien osalta työnantajan todistus vuorotyöstä tai opiskelusta johtuvasta ilta- tai vuorohoidon tarpeesta.'
          },
          attachmentsMessage: {
            text: 'Ilta- ja vuorohoito on tarkoitettu lapsille, jotka vanhempien työn tai tutkintoon johtavan opiskelun vuoksi tarvitsevat ilta- ja vuorohoitoa. Hakemuksen liitteeksi on toimitettava vanhempien osalta työnantajan todistus vuorotyöstä tai opiskelusta johtuvasta ilta- tai vuorohoidon tarpeesta.'
          }
        },
        assistanceNeedInstructions: {
          DAYCARE:
            'Tehostettua tai erityistä tukea annetaan lapselle heti tarpeen ilmettyä. Mikäli lapsella on olemassa tuen tarpeesta asiantuntijalausunto, tämä tulee ilmoittaa varhaiskasvatushakemuksella. Tukitoimet toteutuvat lapsen arjessa osana varhaiskasvatuksen toimintaa. Lempäälän varhaiskasvatuksesta otetaan erikseen yhteyttä hakemuksen jättämisen jälkeen, jos lapsella on tuen tarve. Lisätiedot näkyvät varhaiskasvatuksen palveluohjauksessa ja varhaiskasvatuksen erityisopettajalle paikan järjestämiseksi.',
          CLUB: 'Jos lapsella on tuen tarve, Lempäälän varhaiskasvatuksesta otetaan yhteyttä hakemuksen jättämisen jälkeen.',
          PRESCHOOL:
            'Valitse hakemuksesta tämä kohta, jos lapsi tarvitsee kehitykselleen ja/tai oppimiselleen tukea esiopetusvuonna. Tukea toteutetaan lapsen arjessa osana esiopetuksen toimintaa. Osa esiopetuspaikoista on varattu tukea tarvitseville lapsille. Jos lapsellanne on kehityksen ja/tai oppimisen tuen tarvetta, varhaiskasvatuksen erityisopettaja ottaa teihin yhteyttä, jotta lapsen tarpeet voidaan ottaa huomioon esiopetuspaikkaa osoitettaessa. Lisätiedot näkyvät varhaiskasvatuksen palveluohjauksessa ja varhaiskasvatuksen erityisopettajalle paikan järjestämiseksi.'
        },
        partTime: {
          true: 'Osapäiväinen'
        },
        dailyTime: {
          label: {
            DAYCARE: 'Palveluntarve',
            PRESCHOOL: 'Esiopetusta täydentävä toiminta'
          },
          connectedDaycare: 'Haen esiopetusta täydentävää toimintaa.',
          connectedDaycareInfo: (
            <>
              <P>
                Esiopetusaika on neljä tuntia päivässä, pääsääntöisesti klo
                9–13. Esiopetuksen lisäksi lapsi voi osallistua maksulliseen
                täydentävään toimintaan aamuisin ja iltapäivisin. Täydentävän
                toiminnan vaihtoehtoina ovat päiväkodeissa annettava täydentävä
                varhaiskasvatus ja kouluilla annettava esiopetuksen kerho.
              </P>
              <P>
                Lisätietoa täydentävästä toiminnasta ja asiakasmaksuista{' '}
                <ExternalLink
                  href="https://lempaala.fi/palvelut/kasvatus-ja-opetus/esiopetus/"
                  text="https://lempaala.fi/palvelut/kasvatus-ja-opetus/esiopetus/"
                  newTab
                />
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
            päivittynyt väestötietojärjestelmään. Varhaiskasvatus
            {preschoolEnabled ? '-, ja esiopetus' : ''}päätös toimitetaan
            automaattisesti myös eri osoitteessa asuvalle väestötiedoista
            löytyvälle huoltajalle.
          </P>
        ),
        futureAddressInfo: `Lempäälän varhaiskasvatuksessa ja esiopetuksessa virallisena osoitteena pidetään väestötiedoista saatavaa osoitetta. Osoite väestötiedoissa muuttuu hakijan tehdessä muuttoilmoituksen postiin tai maistraattiin.`
      },
      fee: {
        info: {
          DAYCARE: (
            <P>
              Kunnallisen varhaiskasvatuksen asiakasmaksu perustuu
              varhaiskasvatuksen asiakasmaksuista annettuun lakiin (Laki
              varhaiskasvatuksen asiakasmaksuista (1503/2016)). Asiakasmaksu
              määräytyy perheen koon, palveluntarpeen sekä bruttotulojen mukaan.
              Uusien asiakkaiden tulee täyttää asiakasmaksulomake ja toimittaa
              tarvittavat liitteet Varhaiskasvatuksen asiakasmaksuihin
              viimeistään kuukauden kuluessa hoidon alkamisesta.
            </P>
          ),
          PRESCHOOL: (
            <P>
              Esiopetus on maksutonta, mutta sitä täydentävä toiminta on
              maksullista. Jos lapsi osallistuu esiopetusta täydentävään
              toimintaan, perhe toimittaa tuloselvityksen bruttotuloistaan
              tuloselvityslomakkeella viimeistään kahden viikon kuluessa siitä,
              kun lapsi on aloittanut esiopetuksen.
            </P>
          )
        },
        links: (
          <P>
            Lisätietoa varhaiskasvatuksen asiakasmaksuista löydät{' '}
            <ExternalLink
              href="https://lempaala.fi/palvelut/kasvatus-ja-opetus/varhaiskasvatus/asiakasmaksut/"
              text="Lempäälän kunnan sivuilta"
              newTab
            />
          </P>
        )
      },
      additionalDetails: {
        dietInfo: (
          <>
            Erityisruokavaliosta huoltaja toimittaa varhaiskasvatus
            {preschoolEnabled ? ' tai esiopetuspaikkaan' : 'paikkaan'} lääkärin
            tai ravitsemusterapeutin täyttämän ja allekirjoittaman{' '}
            <ExternalLink
              href="https://lempaala.fi/palvelut/asuminen-ja-elinymparisto/siivous-ja-ruokapalvelu/erityisruokavaliot/"
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
    title: `Hakeminen varhaiskasvatukseen${
      preschoolEnabled ? ' ja esiopetukseen' : ''
    }`,
    summary: (
      <P width="800px">
        Lapsen huoltaja voi tehdä lapselleen hakemuksen varhaiskasvatukseen
        {preschoolEnabled ? ' ja esiopetukseen' : ''}. Huoltajan lasten tiedot
        haetaan tähän näkymään automaattisesti Väestötietojärjestelmästä.
      </P>
    )
  },
  children: {
    pageDescription:
      'Tällä sivulla näet lastesi varhaiskasvatukseen liittyvät yleiset tiedot.',
    vasu: {
      sharingVasuDisclaimer:
        'Lapsen vaihtaessa toiseen Lempäälän kunnan varhaiskasvatusyksikköön varhaiskasvatussuunnitelma ja muut varhaiskasvatuksen asiakirjat siirretään uuteen yksikköön (koskee myös Lempäälän ostopalveluna järjestämää varhaiskasvatusta). Siirrosta informoidaan huoltajaa etukäteen. Varhaiskasvatussuunnitelman luovuttamiseen ulkopuoliselle pyydämme huoltajalta luvan. Mikäli lapsi siirtyy yksityiseen tai toisen kunnan varhaiskasvatukseen, on varhaiskasvatussuunnitelma kuitenkin toimitettava uudelle varhaiskasvatuksen järjestäjälle myös ilman lupaa, jos tiedot ovat välttämättömiä lapsen varhaiskasvatuksen järjestämiseksi (varhaiskasvatuslaki 41 §:n 3 mom.). Lisäksi varhaiskasvatussuunnitelma on toimitettava esi- tai perusopetuksen järjestäjälle, jos se on välttämätöntä lapsen opetuksen järjestämiseksi (perusopetuslaki 41 §:n 4 mom.). Luovuttamisesta informoidaan huoltajaa etukäteen.'
    }
  },
  footer: {
    cityLabel: '© Lempäälän kunta',
    privacyPolicyLink: (
      <ExternalLink
        href="https://www.lempaala.fi/lempaala-tietoa/lempaala-talo/asiointi-ja-neuvonta/tietosuoja/tietosuojaselosteet/"
        text="Tietosuojaselosteet"
        newTab={true}
        data-qa="footer-policy-link"
      />
    ),
    sendFeedbackLink: (
      <ExternalLink
        href="https://lempaala.trimblefeedback.com/eFeedback/"
        text="Lähetä palautetta"
        newTab={true}
        data-qa="footer-feedback-link"
      />
    )
  },
  loginPage: {
    applying: {
      infoBullets: [
        `hakea lapsellesi varhaiskasvatus-${
          preschoolEnabled ? '  tai esiopetus' : ''
        }paikkaa tai tarkastella aiemmin tekemääsi hakemusta`,
        `tarkastella lapsesi varhaiskasvatukseen${
          preschoolEnabled ? ' ja esiopetukseen' : ''
        } liittyviä kuvia ja muita dokumentteja`,
        'ilmoittaa omat tai lapsesi tulotiedot',
        `hyväksyä lapsesi varhaiskasvatus-${
          preschoolEnabled ? '  tai esiopetus' : ''
        }paikan`,
        'irtisanoa lapsen varhaiskasvatuspaikan.'
      ]
    },
    login: {
      infoBoxText: (
        <>
          <P>
            Mikäli kirjautuminen tästä ei onnistu, katso ohjeet{' '}
            <a
              href="https://lempaala.fi/palvelut/kasvatus-ja-opetus/varhaiskasvatus/varhaiskasvatuksen-jarjestaminen/"
              target="_blank"
              rel="noreferrer"
            >
              eVaka | Lempäälän kunta
            </a>
            . Voit kirjautua myös käyttämällä vahvaa tunnistautumista.
          </P>
        </>
      ),
      paragraph: `Huoltajat, joiden lapsi on jo varhaiskasvatuksessa${
        preschoolEnabled ? ' tai esiopetuksessa' : ''
      }: hoida lapsesi päivittäisiä asioita kuten lue viestejä ja ilmoita lapsen läsnäoloajat ja poissaolot.`
    },
    title: `Lempäälän kunnan varhaiskasvatus${
      preschoolEnabled ? ' ja esiopetus' : ''
    }`
  },
  map: {
    mainInfo: `Tässä näkymässä voit hakea kartalta kaikki Lempäälän varhaiskasvatusyksiköt.${
      preschoolEnabled
        ? '  Esiopetusta järjestetään pääsääntöisesti kouluilla.'
        : ''
    }`,
    privateUnitInfo: <></>,
    serviceVoucherLink:
      'https://lempaala.fi/palvelut/kasvatus-ja-opetus/varhaiskasvatus/yksityiset-paivakodit/',
    searchPlaceholder: 'Esim. Peiponpellon päiväkoti'
  },
  decisions: {
    summary: (
      <P width="800px">
        Tälle sivulle saapuvat kaikki lasta koskevat päätökset.
      </P>
    ),
    applicationDecisions: {
      type: {
        PRESCHOOL_DAYCARE: 'täydentävästä varhaiskasvatuksesta'
      },
      confirmationInfo: {
        preschool:
          'Esiopetuspaikka ja/tai esiopetusta täydentävän toiminnan paikka tulee vastaanottaa tai hylätä viipymättä, viimeistään kahden viikon kuluessa päätöksen tiedoksisaannista. Mikäli olet hakenut sekä esiopetukseen että esiopetusta täydentävään toimintaan, saat molemmista erilliset päätökset.'
      },
      summary:
        'Päätöksessä ilmoitettu paikka tulee vastaanottaa tai hylätä viipymättä, viimeistään kahden viikon kuluessa päätöksen tiedoksisaannista.',
      warnings: {
        doubleRejectWarning: {
          text: 'Olet hylkäämässä tarjotun esiopetuspaikan. Täydentävän toiminnan paikka merkitään samalla hylätyksi.'
        }
      },
      response: {
        disabledInfo:
          'HUOM! Pystyt vastaanottamaan / hylkäämään esiopetusta täydentävän toiminnan paikan vasta sen jälkeen, kun olet vastaanottanut esiopetuspaikan.'
      }
    },
    assistanceDecisions: {
      decision: {
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
              Tiedoksisaantipäivää ei lueta määräaikaan. Jos määräajan viimeinen
              päivä on pyhäpäivä, itsenäisyyspäivä, vapunpäivä, joulu- tai
              juhannusaatto tai arkilauantai, saa tehtävän toimittaa
              ensimmäisenä arkipäivänä sen jälkeen.
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
            <H3>Oikaisuvaatimuksen muoto ja sisältö</H3>
            <P>
              Oikaisuvaatimus on tehtävä kirjallisesti. Myös sähköinen asiakirja
              täyttää vaatimuksen kirjallisesta muodosta.
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
              Jos oikaisuvaatimuksen tekijän puhevaltaa käyttää hänen laillinen
              edustajansa tai asiamiehensä tai jos oikaisuvaatimuksen laatijana
              on joku muu henkilö, oikaisuvaatimuksessa on ilmoitettava myös
              tämän nimi ja kotikunta.
            </P>
            <P noMargin>Oikaisuvaatimukseen on liitettävä</P>
            <ul>
              <li>
                päätös, johon haetaan oikaisua, alkuperäisenä tai jäljennöksenä
              </li>
              <li>
                todistus siitä, minä päivänä päätös on annettu tiedoksi, tai muu
                selvitys oikaisuvaatimusajan alkamisen ajankohdasta
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
        jurisdictionText:
          'Tämän päätöksen peruste tulee Lempäälän kunnan hallintosäännöstä: 3 luku Henkilöstöorganisaatio ja toimivalta, 3 § Palvelualueet, 3.1 Palvelualuejohtaja 3 momentti Palvelualuejohtajat voivat siirtää edellä mainittua toimivaltaansa ja 6 §:ssä kirjattua erityistoimivaltaansa alaisilleen viranhaltijoille, sivistysjohtajan subdelegointipäätös 6.3.2023 § 37 Toimivallan siirto varhaiskasvatuksen tehtäväalueen viranhaltijoille 1.1.2023 lukien.'
      }
    },
    assistancePreschoolDecisions: {
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
            Oikaisu tehdään Länsi- ja Sisä-Suomen aluehallintovirastolle (Vaasan
            päätoimipaikka).
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
          <H3>Oikaisuvaatimusaika</H3>
          <P>
            Oikaisuvaatimus on tehtävä 14 päivän kuluessa päätöksen
            tiedoksisaannista.
          </P>
          <H3>Tiedoksisaanti</H3>
          <P>
            Asianosaisen katsotaan saaneen päätöksestä tiedon, jollei muuta
            näytetä, 7 päivän kuluttua kirjeen lähettämisestä, 3 päivän kuluttua
            sähköpostin lähettämisestä, saantitodistuksen osoittamana aikana tai
            erilliseen tiedoksisaantitodistukseen merkittynä aikana.
            Tiedoksisaantipäivää ei lueta määräaikaan. Jos määräajan viimeinen
            päivä on pyhäpäivä, itsenäisyyspäivä, vapunpäivä, joulu- tai
            juhannusaatto tai arkilauantai, saa tehtävän toimittaa ensimmäisenä
            arkipäivänä sen jälkeen.
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
              päätös, johon haetaan oikaisua, alkuperäisenä tai jäljennöksenä
            </li>
            <li>
              todistus siitä, minä päivänä päätös on annettu tiedoksi, tai muu
              selvitys oikaisuvaatimusajan alkamisen ajankohdasta
            </li>
            <li>
              asiakirjat, joihin oikaisuvaatimuksen tekijä vetoaa
              oikaisuvaatimuksensa tueksi, jollei niitä ole jo aikaisemmin
              toimitettu viranomaiselle.
            </li>
          </ul>

          <P>
            Asiamiehen on liitettävä valituskirjelmään valtakirja, kuten
            oikeudenkäynnistä hallintoasioissa annetun lain (808/2019) 32 §:ssä
            säädetään
          </P>
          <H3>Oikaisuvaatimuksen toimittaminen</H3>
          <P>
            Oikaisuvaatimuskirjelmä on toimitettava oikaisuvaatimusajan kuluessa
            oikaisuvaatimusviranomaiselle. Oikaisuvaatimuskirjelmän tulee olla
            perillä oikaisuvaatimusajan viimeisenä päivänä ennen viraston
            aukiolon päättymistä. Oikaisuvaatimuksen lähettäminen postitse tai
            sähköisesti tapahtuu lähettäjän omalla vastuulla.
          </P>
        </>
      ),
      jurisdictionText:
        'Kasvatus- ja opetuslautakunnan päätös 20.6.2023 § 11 (Kasvatus- ja opetuslautakunnan toimivallan siirto viranhaltijoille), jonka 4 §:n mukaan varhaiskasvatuksen palvelupäällikkö päättää esiopetuksessa annettavasta tuesta ja tukipalveluista.',
      lawReference:
        'Laki viranomaisen toiminnan julkisuudesta 24 § 1 mom. 30 kohta'
    }
  },
  placement: {
    type: {
      PRESCHOOL_DAYCARE: 'Täydentävä varhaiskasvatus päiväkodissa',
      PRESCHOOL_CLUB: 'Esiopetuksen kerho koululla klo 7-17',
      PRESCHOOL_WITH_DAYCARE: 'Täydentävä varhaiskasvatus päiväkodissa'
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
          <a href="https://lempaala.fi/palvelut/kasvatus-ja-opetus/varhaiskasvatus/asiakasmaksut/">
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
            Jos tulosi ylittävät perhekoon mukaisen korkeimman maksun tulorajan,
            hyväksy korkein varhaiskasvatusmaksu. Tällöin sinun ei tarvitse
            selvittää tulojasi lainkaan.
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
              href="https://lempaala.fi/palvelut/kasvatus-ja-opetus/varhaiskasvatus/asiakasmaksut/"
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
      absenceTypes: {
        PLANNED_ABSENCE: 'Suunniteltu poissaolo'
      },
      selectChildrenInfo: 'Ilmoita tässä vain koko päivän poissaolot.'
    },
    absences: {
      PLANNED_ABSENCE: 'Suunniteltu poissaolo'
    },
    absentPlanned: 'Suunniteltu poissaolo'
  },
  accessibilityStatement: (
    <>
      <H1>Saavutettavuusseloste</H1>
      <P>
        Tämä saavutettavuusseloste koskee Lempäälän kunnan varhaiskasvatuksen
        eVaka-verkkopalvelua osoitteessa{' '}
        <a href="https://evaka.lempaala.fi">evaka.lempaala.fi</a>. Lempäälän
        kunta pyrkii takaamaan verkkopalvelun saavutettavuuden, parantamaan
        käyttäjäkokemusta jatkuvasti ja soveltamaan asianmukaisia
        saavutettavuusstandardeja.
      </P>
      <P>
        Palvelun saavutettavuuden on arvioinut palvelun kehitystiimi, ja seloste
        on laadittu 12.4.2022.
      </P>
      <H2>Palvelun vaatimustenmukaisuus</H2>
      <P>
        Verkkopalvelu täyttää lain asettamat kriittiset
        saavutettavuusvaatimukset WCAG v2.1 -tason AA mukaisesti. Palvelu ei ole
        vielä kaikilta osin vaatimusten mukainen.
      </P>
      <H2>Toimet saavutettavuuden tukemiseksi</H2>
      <P>
        Verkkopalvelun saavutettavuus varmistetaan muun muassa seuraavilla
        toimenpiteillä:
      </P>
      <ul>
        <li>
          Saavutettavuus huomioidaan alusta lähtien suunnitteluvaiheessa, mm.
          valitsemalla palvelun värit ja kirjaisinten koot saavutettavasti.
        </li>
        <li>
          Palvelun elementit on määritelty semantiikaltaan johdonmukaisesti.
        </li>
        <li>Palvelua testataan jatkuvasti ruudunlukijalla.</li>
        <li>
          Erilaiset käyttäjät testaavat palvelua ja antavat saavutettavuudesta
          palautetta.
        </li>
        <li>
          Sivuston saavutettavuudesta huolehditaan jatkuvalla valvonnalla
          tekniikan tai sisällön muuttuessa.
        </li>
      </ul>
      <P>
        Tätä selostetta päivitetään sivuston muutosten ja saavutettavuuden
        tarkistusten yhteydessä.
      </P>
      <H2>Tunnetut saavutettavuusongelmat</H2>
      <P>
        Käyttäjät saattavat edelleen kohdata sivustolla joitakin ongelmia.
        Seuraavassa on kuvaus tunnetuista saavutettavuusongelmista. Jos huomaat
        sivustolla ongelman, joka ei ole luettelossa, otathan meihin yhteyttä.
      </P>
      <ul>
        <li>
          Viestit-sivulla liikkuminen näppäimistöllä tai ruudunlukijalla vaatii
          vielä korjauksia siirtymien ja kohdistettavien elementtien osalta.
        </li>
        <li>
          Palvelun yksikkökartassa ei pysty liikkumaan
          näppäimistöllä/ruudunlukijalla, mutta yksikköjä voi selata samassa
          näkymässä olevalta listalta. Palvelussa käytetty kartta on kolmannen
          osapuolen tuottama.
        </li>
      </ul>
      <H2>Kolmannet osapuolet</H2>
      <P>
        Verkkopalvelussa käytetään seuraavia kolmannen osapuolen palveluita,
        joiden saavutettavuudesta emme voi vastata.
      </P>
      <ul>
        <li>Keycloak käyttäjän tunnistautumispalvelu</li>
        <li>Suomi.fi-tunnistautuminen</li>
        <li>Leaflet-karttapalvelu</li>
      </ul>
      <H2>Vaihtoehtoiset asiointitavat</H2>
      <P>
        <ExternalLink
          href="https://lempaala.fi/palvelut/kasvatus-ja-opetus/varhaiskasvatus/"
          text="Varhaiskasvatuksen ja esiopetuksen asiakaspalvelu"
        />
      </P>
      <H2>Anna palautetta</H2>
      <P>
        Jos huomaat saavutettavuuspuutteen verkkopalvelussamme, kerro siitä
        meille.{' '}
        <ExternalLink
          href="https://lempaala.fi/anna-palautetta/"
          text="Anna saavutettavuuspalautetta tällä verkkolomakkeella"
        />
      </P>
      <H2>Valvontaviranomainen</H2>
      <P>
        Jos huomaat sivustolla saavutettavuusongelmia, anna ensin palautetta
        meille sivuston ylläpitäjille. Vastauksessa voi mennä 14 päivää. Jos et
        ole tyytyväinen saamaasi vastaukseen, tai et saa vastausta lainkaan
        kahden viikon aikana, voit antaa palautteen Etelä-Suomen
        aluehallintovirastoon. Etelä-Suomen aluehallintoviraston sivulla
        kerrotaan tarkasti, miten valituksen voi tehdä, ja miten asia
        käsitellään.
      </P>

      <P>
        <strong>Valvontaviranomaisen yhteystiedot </strong>
        <br />
        Etelä-Suomen aluehallintovirasto <br />
        Saavutettavuuden valvonnan yksikkö
        <br />
        <ExternalLink
          href="www.saavutettavuusvaatimukset.fi"
          text="www.saavutettavuusvaatimukset.fi"
        />
        <br />
        <a href="mailto:saavutettavuus@avi.fi">saavutettavuus@avi.fi</a>
        <br />
        puhelinnumero vaihde 0295 016 000
        <br />
        Avoinna: ma-pe klo 8.00–16.15
      </P>
    </>
  )
}

export default fi
