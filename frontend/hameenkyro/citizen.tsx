// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import React from 'react'

import ExternalLink from 'lib-components/atoms/ExternalLink'
import UnorderedList from 'lib-components/atoms/UnorderedList'
import { H1, H2, H3, P } from 'lib-components/typography'
import { Gap } from 'lib-components/white-space'
import { CitizenCustomizations } from 'lib-customizations/types'

import HameenkyroLogo from './HameenkyroLogo.png'
import featureFlags from './featureFlags'

const customerContactTextFi = function () {
  return (
    <ExternalLink
      href="https://www.hameenkyro.fi/kasvatus-ja-opetus/varhaiskasvatus/"
      text="varhaiskasvatuksen asiakaspalveluun"
    />
  )
}

const customerContactTextEn = function () {
  return (
    <ExternalLink
      href="https://www.hameenkyro.fi/kasvatus-ja-opetus/varhaiskasvatus/"
      text="customer service of the Early childhood education"
    />
  )
}

const customizations: CitizenCustomizations = {
  appConfig: {},
  langs: ['fi', 'en'],
  translations: {
    fi: {
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
              Huoltaja voi tehdä muutoksia hakemukseen verkkopalvelussa siihen
              asti, kun hakemus otetaan asiakaspalvelussa käsittelyyn. Tämän
              jälkeen muutokset tai hakemuksen peruminen on mahdollista
              ottamalla yhteyttä
              {customerContactTextFi()}.
            </P>
          ),
          duplicateWarning:
            'Lapsella on jo samantyyppinen, keskeneräinen hakemus. Palaa Hakemukset-näkymään ja muokkaa olemassa olevaa hakemusta tai ota yhteyttä Varhaiskasvatuksen asiakaspalveluun.',
          transferApplicationInfo: {
            DAYCARE:
              'Lapsella on jo paikka Hämeenkyrön varhaiskasvatuksessa. Tällä hakemuksella voit hakea siirtoa toiseen varhaiskasvatusta tarjoavaan yksikköön Hämeenkyröllä.',
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
                yhteydessä {customerContactTextFi()}.
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
                      Esiopetuspaikka määräytyy lapsen kotiosoitteen mukaan,
                      tulevan koulupolun mukaisesti. Ilmoittautuessa lapsi
                      ilmoitetaan koulupolun mukaiseen esiopetukseen tai
                      painotettuun esiopetukseen. Mikäli lapsi tarvitsee
                      säännöllisesti ilta- tai vuorohoitoa, hänet ilmoitetaan
                      ilta- tai vuorohoidon esiopetukseen.
                    </P>
                    <P>
                      Tieto tulevasta esiopetuspaikasta ilmoitetaan huoltajille
                      sähköisesti Suomi.fi-viestit palvelun kautta. Mikäli
                      huoltaja ei ole ottanut palvelua käyttöönsä, tieto
                      lähetetään hänelle kirjeitse.
                    </P>
                    <P>
                      Lisätietoa{' '}
                      <ExternalLink
                        href="https://www.hameenkyro.fi/kasvatus-ja-opetus/esi-ja-perusopetus/"
                        text="https://www.hameenkyro.fi/kasvatus-ja-opetus/esi-ja-perusopetus/"
                        newTab
                      />
                    </P>
                  </>
                )
              },
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
                    Varhaiskasvatushakemus tulee jättää viimeistään neljä
                    kuukautta ennen hoidon toivottua alkamisajankohtaa. Mikäli
                    varhaiskasvatuksen tarve johtuu työllistymisestä, opinnoista
                    tai koulutuksesta, eikä hoidon tarpeen ajankohtaa ole
                    pystynyt ennakoimaan, on varhaiskasvatuspaikkaa haettava
                    mahdollisimman pian - kuitenkin viimeistään kaksi viikkoa
                    ennen kuin lapsi tarvitsee hoitopaikan.
                  </P>
                  <P>
                    Kirjallinen päätös varhaiskasvatuspaikasta lähetetään
                    Suomi.fi-viestit -palveluun. Mikäli haluatte päätöksen
                    sähköisenä tiedoksiantona, teidän tulee ottaa
                    Suomi.fi-viestit -palvelu käyttöön. Palvelusta ja sen
                    käyttöönotosta saatte lisätietoa{' '}
                    <ExternalLink
                      text="https://www.suomi.fi/viestit"
                      href="https://www.suomi.fi/viestit"
                      newTab
                    />
                    . Mikäli ette ota Suomi.fi-viestit -palvelua käyttöön,
                    päätös lähetetään teille postitse.
                  </P>
                  <P fitted={true}>
                    * Tähdellä merkityt tiedot ovat pakollisia
                  </P>
                </>
              ),
              PRESCHOOL: (
                <>
                  <P>
                    Perusopetuslain (26 a §) mukaan lasten on oppivelvollisuuden
                    alkamista edeltävänä vuonna osallistuttava vuoden kestävään
                    esiopetukseen tai muuhun esiopetuksen tavoitteet
                    saavuttavaan toimintaan. Esiopetus on maksutonta.
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
                  <P fitted={true}>
                    * Tähdellä merkityt tiedot ovat pakollisia
                  </P>
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
                    . Mikäli ette ota Suomi.fi-viestit -palvelua käyttöön,
                    ilmoitus kerhopaikasta lähetetään teille postitse. Paikka
                    myönnetään yhdeksi toimintakaudeksi kerrallaan.
                  </P>
                  <P>
                    Kerhohakemus kohdistuu yhdelle kerhon toimintakaudelle.
                    Kyseisen kauden päättyessä hakemus poistetaan
                    järjestelmästä.
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
                    Toivottua aloituspäivää on mahdollista muuttaa myöhemmäksi
                    niin kauan, kun hakemusta ei ole otettu käsittelyyn. Tämän
                    jälkeen toivotun aloituspäivän muutokset tehdään ottamalla
                    yhteyttä {customerContactTextFi()}.
                  </>
                ),
                PRESCHOOL: null
              },
              info: {
                PRESCHOOL: [
                  'Lukuvuosi 2023–2024 alkaa keskiviikkona 9.8.2023 ja päättyy perjantaina 31.5.2024.'
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
                    työllistymisestä tai opiskelupaikan saamisesta, tulee
                    paikkaa hakea viimeistään kaksi viikkoa ennen kuin hoidon
                    tarve alkaa. Lisäksi huoltajan tulee ottaa yhteyttä
                    viipymättä {customerContactTextFi()}.
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
                'Tehostettua tai erityistä tukea annetaan lapselle heti tarpeen ilmettyä. Mikäli lapsella on olemassa tuen tarpeesta asiantuntijalausunto, tämä tulee ilmoittaa varhaiskasvatushakemuksella. Tukitoimet toteutuvat lapsen arjessa osana varhaiskasvatuksen toimintaa. Hämeenkyrön varhaiskasvatuksesta otetaan erikseen yhteyttä hakemuksen jättämisen jälkeen, jos lapsella on tuen tarve. Lisätiedot näkyvät varhaiskasvatuksen palveluohjauksessa ja varhaiskasvatuksen erityisopettajalle paikan järjestämiseksi.',
              CLUB: 'Jos lapsella on tuen tarve, Hämeenkyrön varhaiskasvatuksesta otetaan yhteyttä hakemuksen jättämisen jälkeen.',
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
                    täydentävään toimintaan aamuisin ja iltapäivisin.
                    Täydentävän toiminnan vaihtoehtoina ovat päiväkodeissa
                    annettava täydentävä varhaiskasvatus ja kouluilla annettava
                    esiopetuksen kerho.
                  </P>
                  <P>
                    Lisätietoa täydentävästä toiminnasta ja asiakasmaksuista{' '}
                    <ExternalLink
                      href="https://www.hameenkyro.fi/kasvatus-ja-opetus/esi-ja-perusopetus/iltapaivatoiminta/"
                      text="https://www.hameenkyro.fi/kasvatus-ja-opetus/esi-ja-perusopetus/iltapaivatoiminta/"
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
                tällä hakemuksella. Jos henkilötiedoissa on virheitä,
                päivitäthän tiedot{' '}
                <ExternalLink
                  text="Digi- ja Väestötietoviraston sivuilla"
                  href="https://dvv.fi/henkiloasiakkaat"
                  newTab
                />
                . Mikäli osoitteenne on muuttumassa, voit lisätä tulevan
                osoitteen erilliseen kohtaan hakemuksella; lisää tuleva osoite
                sekä lapselle että huoltajalle. Virallisena osoitetietoa
                pidetään vasta, kun se on päivittynyt väestötietojärjestelmään.
                Varhaiskasvatus-, palveluseteli- ja esiopetuspäätös sekä tieto
                avoimen varhaiskasvatuksen kerhopaikasta toimitetaan
                automaattisesti myös eri osoitteessa asuvalle väestötiedoista
                löytyvälle huoltajalle.
              </P>
            ),
            futureAddressInfo: `Hämeenkyrön varhaiskasvatuksessa ja esiopetuksessa virallisena osoitteena pidetään väestötiedoista saatavaa osoitetta. Osoite väestötiedoissa muuttuu hakijan tehdessä muuttoilmoituksen postiin tai maistraattiin.`
          },
          fee: {
            info: {
              DAYCARE: (
                <P>
                  Kunnallisen varhaiskasvatuksen asiakasmaksu ja palvelusetelin
                  omavastuuosuus perustuu varhaiskasvatuksen asiakasmaksuista
                  annettuun lakiin (Laki varhaiskasvatuksen asiakasmaksuista
                  (1503/2016)). Asiakasmaksu määräytyy perheen koon,
                  palveluntarpeen sekä bruttotulojen mukaan. Uusien asiakkaiden
                  tulee täyttää asiakasmaksulomake ja toimittaa tarvittavat
                  liitteet Varhaiskasvatuksen asiakasmaksuihin viimeistään
                  kuukauden kuluessa hoidon alkamisesta.
                </P>
              ),
              PRESCHOOL: (
                <P>
                  Esiopetus on maksutonta, mutta sitä täydentävä toiminta on
                  maksullista. Jos lapsi osallistuu esiopetusta täydentävään
                  toimintaan, perhe toimittaa tuloselvityksen bruttotuloistaan
                  tuloselvityslomakkeella viimeistään kahden viikon kuluessa
                  siitä, kun lapsi on aloittanut esiopetuksen.
                </P>
              )
            },
            links: (
              <P>
                Lisätietoa varhaiskasvatuksen asiakasmaksuista löydät{' '}
                <ExternalLink
                  href="https://www.hameenkyro.fi/kasvatus-ja-opetus/varhaiskasvatus/asiakasmaksut/"
                  text="Hämeenkyrön kunnan sivuilta"
                  newTab
                />
              </P>
            )
          },
          additionalDetails: {
            dietInfo: (
              <>
                Erityisruokavaliosta huoltaja toimittaa varhaiskasvatus tai
                esiopetuspaikkaan lääkärin tai ravitsemusterapeutin täyttämän ja
                allekirjoittaman{' '}
                <ExternalLink
                  href="https://www.hameenkyro.fi/kasvatus-ja-opetus/esi-ja-perusopetus/kouluruokailu-ja-ruokalista/"
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
        title: `Hakeminen varhaiskasvatukseen ja esiopetukseen`,
        summary: (
          <P width="800px">
            Lapsen huoltaja voi tehdä lapselleen hakemuksen varhaiskasvatukseen,
            esiopetukseen ja kerhoon. Huoltajan lasten tiedot haetaan tähän
            näkymään automaattisesti Väestötietojärjestelmästä.
          </P>
        )
      },
      children: {
        pageDescription:
          'Tällä sivulla näet lastesi varhaiskasvatukseen liittyvät yleiset tiedot.',
        vasu: {
          sharingVasuDisclaimer:
            'Lapsen vaihtaessa toiseen Hämeenkyrön kunnan varhaiskasvatusyksikköön varhaiskasvatussuunnitelma ja muut varhaiskasvatuksen asiakirjat siirretään uuteen yksikköön (koskee myös Hämeenkyrön ostopalveluna järjestämää varhaiskasvatusta). Siirrosta informoidaan huoltajaa etukäteen. Varhaiskasvatussuunnitelman luovuttamiseen ulkopuoliselle pyydämme huoltajalta luvan. Mikäli lapsi siirtyy yksityiseen tai toisen kunnan varhaiskasvatukseen, on varhaiskasvatussuunnitelma kuitenkin toimitettava uudelle varhaiskasvatuksen järjestäjälle myös ilman lupaa, jos tiedot ovat välttämättömiä lapsen varhaiskasvatuksen järjestämiseksi (varhaiskasvatuslaki 41 §:n 3 mom.). Lisäksi varhaiskasvatussuunnitelma on toimitettava esi- tai perusopetuksen järjestäjälle, jos se on välttämätöntä lapsen opetuksen järjestämiseksi (perusopetuslaki 41 §:n 4 mom.). Luovuttamisesta informoidaan huoltajaa etukäteen.'
        }
      },
      footer: {
        cityLabel: '© Hämeenkyrön kunta',
        privacyPolicyLink: (
          <ExternalLink
            href="https://www.hameenkyro.fi/kunta-ja-hallinto/asiointi/tietosuoja-ja-selosteet/"
            text="Tietosuojaselosteet"
            newTab={true}
            data-qa="footer-policy-link"
          />
        ),
        sendFeedbackLink: (
          <ExternalLink
            href="https://www.hameenkyro.fi/kunta-ja-hallinto/osallistu-ja-vaikuta/palaute/"
            text="Lähetä palautetta"
            newTab={true}
            data-qa="footer-feedback-link"
          />
        )
      },
      loginPage: {
        applying: {
          infoBullets: [
            `hakea lapsellesi varhaiskasvatus-, esiopetus tai kerhopaikkaa tai tarkastella aiemmin tekemääsi hakemusta`,
            `tarkastella lapsesi varhaiskasvatukseen ja esiopetukseen liittyviä kuvia ja muita dokumentteja`,
            'ilmoittaa omat tai lapsesi tulotiedot',
            `hyväksyä lapsesi varhaiskasvatus-, esiopetus- tai kerhopaikan`,
            'irtisanoa lapsen varhaiskasvatus- tai kerhopaikan.'
          ]
        },
        login: {
          infoBoxText: (
            <>
              <P>
                Mikäli kirjautuminen tästä ei onnistu, katso ohjeet{' '}
                <a
                  href="https://www.hameenkyro.fi/kasvatus-ja-opetus/varhaiskasvatus/"
                  target="_blank"
                  rel="noreferrer"
                >
                  eVaka | Hämeenkyrön kunta
                </a>
                . Voit kirjautua myös käyttämällä vahvaa tunnistautumista.
              </P>
            </>
          ),
          paragraph: `Huoltajat, joiden lapsi on jo varhaiskasvatuksessa tai esiopetuksessa: hoida lapsesi päivittäisiä asioita kuten lue viestejä ja ilmoita lapsen läsnäoloajat ja poissaolot.`
        },
        title: `Hämeenkyrön kunnan varhaiskasvatus ja esiopetus`
      },
      map: {
        mainInfo: `Tässä näkymässä voit hakea kartalta kaikki Hämeenkyrön varhaiskasvatusyksiköt sekä kerhot. Kartalta löytyvät myös seudulliset palveluseteliyksiköt ja -kerhot. Esiopetusta järjestetään pääsääntöisesti kouluilla.`,
        privateUnitInfo: <></>,
        serviceVoucherLink:
          'https://www.hameenkyro.fi/kasvatus-ja-opetus/varhaiskasvatus/kunnallinen-varhaiskasvatus/kunnalliset-paivakodit/',
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
                  Oikaisuvaatimuksen saa tehdä se, johon päätös on kohdistettu
                  tai jonka oikeuteen, velvollisuuteen tai etuun päätös
                  välittömästi vaikuttaa (asianosainen).
                </P>
                <H3>Oikaisuvaatimusaika</H3>
                <P>
                  Oikaisuvaatimus on tehtävä 30 päivän kuluessa päätöksen
                  tiedoksisaannista.
                </P>
                <H3>Tiedoksisaanti</H3>
                <P>
                  Asianosaisen katsotaan saaneen päätöksestä tiedon, jollei
                  muuta näytetä, seitsemän päivän kuluttua kirjeen
                  lähettämisestä tai saantitodistukseen tai
                  tiedoksiantotodistukseen merkittynä päivänä. Käytettäessä
                  tavallista sähköistä tiedoksiantoa asianosaisen katsotaan
                  saaneen päätöksestä tiedon, jollei muuta näytetä kolmantena
                  päivänä viestin lähettämisestä. Tiedoksisaantipäivää ei lueta
                  määräaikaan. Jos määräajan viimeinen päivä on pyhäpäivä,
                  itsenäisyyspäivä, vapunpäivä, joulu- tai juhannusaatto tai
                  arkilauantai, saa tehtävän toimittaa ensimmäisenä arkipäivänä
                  sen jälkeen.
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
                    miltä osin päätökseen haetaan oikaisua ja mitä oikaisua
                    siihen vaaditaan tehtäväksi
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
                  kuluessa oikaisuvaatimusviranomaiselle.
                  Oikaisuvaatimuskirjelmän tulee olla perillä
                  oikaisuvaatimusajan viimeisenä päivänä ennen viraston aukiolon
                  päättymistä. Oikaisuvaatimuksen lähettäminen postitse tai
                  sähköisesti tapahtuu lähettäjän omalla vastuulla.
                </P>
              </>
            ),
            jurisdictionText:
              'Delegointipäätös pohjautuu Sivistys- ja kulttuurilautakunnan toimivallan siirtämiseen viranhaltijoille 14.6.2022 § 116: Varhaiskasvatuksen palvelupäällikkö päättää varhaiskasvatuslain mukaisesta tuesta ja tukipalveluista yksityisessä varhaiskasvatuksessa (Varhaiskasvatuslaki 3 a luku) ja päiväkodin johtaja päättää varhaiskasvatuslain mukaisesta tuesta ja tukipalveluista kunnallisessa varhaiskasvatuksessa (Varhaiskasvatuslaki 3 a luku)'
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
              tuloselvityksiä ja muokata tai poistaa niitä kunnes viranomainen
              on käsitellyt tiedot. Lomakkeen käsittelyn jälkeen voit päivittää
              tulotietojasi toimittamalla uuden lomakkeen.
            </p>
            <p data-qa="income-description-p2">
              <strong>
                Molempien samassa taloudessa asuvien aikuisten tulee toimittaa
                omat erilliset tuloselvitykset.
              </strong>
            </p>
            <p data-qa="income-description-p3">
              Kunnallisen varhaiskasvatuksen asiakasmaksut määräytyvät
              prosenttiosuutena perheen bruttotuloista. Maksut vaihtelevat
              perheen koon ja tulojen sekä varhaiskasvatusajan mukaan.
            </p>
            <p data-qa="income-description-p4">
              <a href="https://www.hameenkyro.fi/kasvatus-ja-opetus/varhaiskasvatus/asiakasmaksut/">
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
              Asiakkaan on viipymättä ilmoitettava tulojen ja perhekoon
              muutoksista varhaiskasvutuksen asiakasmaksuihin. Viranomainen on
              tarvittaessa oikeutettu perimään varhaiskasvatusmaksuja myös
              takautuvasti.
            </P>
            <P>
              <strong>Huomioitavaa:</strong>
            </P>
            <Gap size="xs" />
            <UnorderedList data-qa="income-formDescription-ul">
              <li>
                Jos tulosi ylittävät perhekoon mukaisen korkeimman maksun
                tulorajan, hyväksy korkein varhaiskasvatusmaksu. Tällöin sinun
                ei tarvitse selvittää tulojasi lainkaan.
              </li>
              <li>
                Jos perheeseesi kuuluu toinen aikuinen, myös hänen on
                toimitettava tuloselvitys tunnistautumalla eVakaan omilla
                henkilötiedoillaan ja täyttämällä tämä lomake.
              </li>
              <li>
                Katso voimassaolevat tulorajat{' '}
                <a
                  target="_blank"
                  rel="noreferrer"
                  href="https://www.hameenkyro.fi/kasvatus-ja-opetus/varhaiskasvatus/asiakasmaksut/"
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
            PLANNED_ABSENCE: 'Sopimuksen mukainen poissaolo'
          },
          selectChildrenInfo: 'Ilmoita tässä vain koko päivän poissaolot.'
        },
        absences: {
          PLANNED_ABSENCE: 'Sopimuksen mukainen poissaolo'
        },
        absentPlanned: 'Sopimuksen mukainen poissaolo'
      },
      accessibilityStatement: (
        <>
          <H1>Saavutettavuusseloste</H1>
          <P>
            Tämä saavutettavuusseloste koskee Hämeenkyrön kunnan
            varhaiskasvatuksen eVaka-verkkopalvelua osoitteessa{' '}
            <a href="https://evaka.hameenkyro.fi">evaka.hameenkyro.fi</a>.
            Hämeenkyrön kunta pyrkii takaamaan verkkopalvelun saavutettavuuden,
            parantamaan käyttäjäkokemusta jatkuvasti ja soveltamaan
            asianmukaisia saavutettavuusstandardeja.
          </P>
          <P>
            Palvelun saavutettavuuden on arvioinut palvelun kehitystiimi, ja
            seloste on laadittu 12.4.2022.
          </P>
          <H2>Palvelun vaatimustenmukaisuus</H2>
          <P>
            Verkkopalvelu täyttää lain asettamat kriittiset
            saavutettavuusvaatimukset WCAG v2.1 -tason AA mukaisesti. Palvelu ei
            ole vielä kaikilta osin vaatimusten mukainen.
          </P>
          <H2>Toimet saavutettavuuden tukemiseksi</H2>
          <P>
            Verkkopalvelun saavutettavuus varmistetaan muun muassa seuraavilla
            toimenpiteillä:
          </P>
          <ul>
            <li>
              Saavutettavuus huomioidaan alusta lähtien suunnitteluvaiheessa,
              mm. valitsemalla palvelun värit ja kirjaisinten koot
              saavutettavasti.
            </li>
            <li>
              Palvelun elementit on määritelty semantiikaltaan johdonmukaisesti.
            </li>
            <li>Palvelua testataan jatkuvasti ruudunlukijalla.</li>
            <li>
              Erilaiset käyttäjät testaavat palvelua ja antavat
              saavutettavuudesta palautetta.
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
            Seuraavassa on kuvaus tunnetuista saavutettavuusongelmista. Jos
            huomaat sivustolla ongelman, joka ei ole luettelossa, otathan meihin
            yhteyttä.
          </P>
          <ul>
            <li>
              Viestit-sivulla liikkuminen näppäimistöllä tai ruudunlukijalla
              vaatii vielä korjauksia siirtymien ja kohdistettavien elementtien
              osalta.
            </li>
            <li>
              Palvelun yksikkökartassa ei pysty liikkumaan
              näppäimistöllä/ruudunlukijalla, mutta yksikköjä voi selata samassa
              näkymässä olevalta listalta. Palvelussa käytetty kartta on
              kolmannen osapuolen tuottama.
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
              href="https://www.hameenkyro.fi/kasvatus-ja-opetus/varhaiskasvatus/"
              text="Varhaiskasvatuksen ja esiopetuksen asiakaspalvelu"
            />
          </P>
          <H2>Anna palautetta</H2>
          <P>
            Jos huomaat saavutettavuuspuutteen verkkopalvelussamme, kerro siitä
            meille.{' '}
            <ExternalLink
              href="https://www.hameenkyro.fi/kunta-ja-hallinto/osallistu-ja-vaikuta/palaute/"
              text="Anna saavutettavuuspalautetta tällä verkkolomakkeella"
            />
          </P>
          <H2>Valvontaviranomainen</H2>
          <P>
            Jos huomaat sivustolla saavutettavuusongelmia, anna ensin palautetta
            meille sivuston ylläpitäjille. Vastauksessa voi mennä 14 päivää. Jos
            et ole tyytyväinen saamaasi vastaukseen, tai et saa vastausta
            lainkaan kahden viikon aikana, voit antaa palautteen Etelä-Suomen
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
    },
    sv: {},
    en: {
      applications: {
        creation: {
          daycareInfo:
            'An applicant for early childhood education applies for a place in a municipal day care centre or family day care, an outsourced service day care centre or a day care centre supported by a service voucher.',
          clubInfo:
            'With a club application one may apply for a place at municipal clubs or clubs supported by a service voucher.',
          preschoolLabel: 'Enrolment in pre-primary education',
          preschoolInfo:
            'There are four hours of free pre-school education per day. In addition, supplementary activities, for which a fee is charged, can be applied for the child. An application for supplementary activities can be made when enrolling for pre-school education or as a separate application once education has started. The unit for supplementary activities cannot be selected in the application as it is always determined by the preschool unit. If a place in supplementary activities is applied for later, the need for pre-primary education will also be entered in the application.',
          preschoolDaycareInfo:
            'Applying for supplementary activities for children who are enrolled / have been enrolled in preschool',
          applicationInfo: (
            <P>
              The custodian can make amendments to the application on the web up
              until the moment that the application is accepted for processing
              by the customer service. After this, amendments or cancellation of
              the application are possible by getting in contact with the
              {customerContactTextEn()}
            </P>
          ),
          duplicateWarning:
            'The child already has a similar unfinished application. Please return to the Applications view and complete the existing application or contact the customer service of the Early childhood education.',
          transferApplicationInfo: {
            DAYCARE:
              'The child already has a place in early childhood education in Hämeenkyrö. With this application you can apply for a transfer to another unit offering early childhood education in Hämeenkyrö.',
            PRESCHOOL:
              'The child already has a pre-school place. With this application, you can apply for activities that supplement preschool education or transfer to another unit that offers preschool education.'
          }
        },
        editor: {
          actions: {
            allowOtherGuardianAccess: (
              <span>
                I understand that the application will also be visible to the
                other guardian. If the other guardian should not be able to see
                this application, please contact {customerContactTextEn()}
              </span>
            )
          },
          verification: {
            serviceNeed: {
              connectedDaycare: {
                label: 'Activities supplementing pre-primary education',
                withConnectedDaycare:
                  'I am applying for a supplementary activity to pre-school education.'
              },
              startDate: {
                title: {
                  PRESCHOOL: 'Start of pre-primary education'
                }
              }
            }
          },
          unitPreference: {
            siblingBasis: {
              checkbox: {
                PRESCHOOL:
                  "It is our greatest wish that the child is allocated in pre-primary education to the same school as the child's older sibling."
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
                      The pre-school place is determined by the child&apos;s
                      home address, in accordance with the future school path.
                      At the time of enrolment, the child is enrolled in a
                      pre-school or weighted pre-school according to the school
                      path. If the child needs regular evening or shift care, he
                      or she is enrolled in evening or shift care in early
                      childhood education.
                    </P>
                    <P>
                      Information about the future pre-school place is
                      communicated to the custodians electronically via the
                      Suomi.fi message service. If the custodian has not
                      registered for the service, the information will be sent
                      to them by letter.
                    </P>
                    <P>
                      Further information{' '}
                      <ExternalLink
                        href="https://www.hameenkyro.fi/kasvatus-ja-opetus/esi-ja-perusopetus/"
                        text="https://www.hameenkyro.fi/kasvatus-ja-opetus/esi-ja-perusopetus/"
                        newTab
                      />
                    </P>
                  </>
                )
              },
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
                    submitted no later than four months prior to the desired
                    start date. If the need for early childhood education is due
                    to employment, studies or training, and it has not been
                    possible to anticipate the need for care, an early childhood
                    education place must be sought as soon as possible –
                    however, no later than two weeks before the child needs the
                    place.
                  </P>
                  <P>
                    A written decision on the early childhood education place
                    will be sent to the Suomi.fi Messages service. If you wish
                    to be notified of the decision electronically, you will need
                    to activate the Suomi.fi Messages service. Further
                    information on the service and its activation is available
                    at{' '}
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
              PRESCHOOL: (
                <>
                  <P>
                    In accordance with the Basic Education Act (Section 26 a §),
                    children must participate in pre-primary education or other
                    activities that achieve the goals of pre-primary education
                    in the year preceding the commencement of compulsory
                    education. Pre-primary education is free of charge.
                  </P>
                  <P>
                    The decisions will be sent to the{' '}
                    <a
                      href="https://www.suomi.fi/viestit"
                      target="_blank"
                      rel="noreferrer"
                    >
                      Suomi.fi Messages
                    </a>{' '}
                    service or by post, if the applicant does not use the
                    Suomi.fi Messages service.
                  </P>
                  <P fitted={true}>
                    * The information denoted with an asterisk is mandatory.
                  </P>
                </>
              ),
              CLUB: (
                <>
                  <P>
                    A place at a club can be applied for all year round. A
                    municipal place in a club or one supported with a service
                    voucher can be applied for with a club application. A
                    written confirmation of a place in a club will be sent to
                    the Suomi.fi Messages service. If you wish to have the
                    notice in electronic form, you must activate the Suomi.fi
                    Messages service. Further information on the service and its
                    activation is available at{' '}
                    <ExternalLink
                      text="https://www.suomi.fi/messages"
                      href="https://www.suomi.fi/messages"
                      newTab
                    />
                    . If you do not activate the suomi.fi/messages service, the
                    notice of the place at the club will be sent to you by post.
                    A place is granted for one administrative period at a time.
                  </P>
                  <P>
                    The club application is for one such period. Once the period
                    in question ends, the application is removed from the
                    system.
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
                    It is possible to postpone the preferred starting day as
                    long as the application has not been processed by the
                    customer service. After this, any desired amendments can be
                    made by contacting the {customerContactTextEn()}
                  </>
                ),
                PRESCHOOL: null
              },
              info: {
                PRESCHOOL: [
                  'The school year 2023–2024 starts on Wednesday 9.8.2023 and ends on Friday 31.5.2024'
                ]
              },
              label: {
                PRESCHOOL: 'The need starting from'
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
                    If the need for an early child education place is due to
                    sudden employment or obtaining a study place, the early
                    childhood education place must be sought no later than two
                    weeks before the need for care starts. Furthermore, the
                    custodian must make contact, without delay, with the{' '}
                    {customerContactTextEn()}
                  </P>
                )
              }
            },
            shiftCare: {
              instructions:
                "If a child needs evening or day care in addition to pre-school education, he or she must be enrolled in evening or day care pre-school education. In addition, the child must be enrolled in supplementary early childhood education and care, for more than 5 hours per day. The day care centres are open normally on weekdays, from 6 am to 6 pm. Evening care is for children who, due to their parents' work or studies leading to a degree, require regular care after 6 pm. Day care centres offering evening care open at 5.30 am if necessary and close at 10.30 pm at the latest. Some day care centres providing evening care are also open at weekends. Shift care is for children whose parents work shifts and whose care also includes nights.",
              message: {
                text: 'Evening and shift care is intended for those children who, due to the parents work or studies that lead to a qualification, require evening and shift care. In the case of the parents, an employer’s certificate of a need for evening or shift care due to shift work or study must be attached to the application.'
              },
              attachmentsMessage: {
                text: 'Evening and shift care is intended for those children who, due to the parents’ work or studies that lead to a qualification, require evening and shift care. In the case of the parents, an employer’s certificate of a need for evening or shift care due to shift work or study must be attached to the application.'
              }
            },
            assistanceNeedInstructions: {
              DAYCARE:
                'Intensified or special care is given to a child as soon as the need arises. If a child has received an expert opinion backing the need for support, this must be notified in the early childhood education application. The support measures are carried out in the child’s daily life as part of the early childhood educational activities. Hämeenkyrö’s early childhood education will separately be in contact after the application has been submitted, if the child has a need for support. The additional information given here will be visible to early childhood education service employees and special early childhood education teachers for the purposes of arranging an early childhood education placement.',
              CLUB: 'If the child has a need for support, the staff of Hämeenkyrö’s early childhood education will get in contact the application has been submitted.',
              PRESCHOOL:
                "Select this box on the application form, if your child needs support for his/her development and/or learning during the pre-school year. This support is provided in the child's everyday life as a part of pre-school activities. Some places in pre-school are reserved for children who need support. If your child has developmental and/or learning support needs, you will be contacted by the special needs teacher for early childhood education, so that your child's needs can be taken into account when allocating a pre-school place. The additional information given here will be visible to early childhood education service employees and special early childhood education teachers for the purposes of arranging an early childhood education placement."
            },
            partTime: {
              true: 'Part-time'
            },
            dailyTime: {
              label: {
                DAYCARE: 'Service options',
                PRESCHOOL: 'Activities supplementing pre-primary education'
              },
              connectedDaycare:
                'I am applying for a supplementary activity to pre-school education.',
              connectedDaycareInfo: (
                <>
                  <P>
                    Pre-school education lasts for four hours a day, generally
                    from 9 am to 1 pm. In addition, the child can participate in
                    paid supplementary activities in the mornings and in the
                    afternoons. Options for supplementary activities are
                    supplementary early childhood education in day care centres
                    and pre-school clubs in schools.
                  </P>
                  <P>
                    Further information about supplementary activities and
                    client fees{' '}
                    <ExternalLink
                      href="https://www.hameenkyro.fi/kasvatus-ja-opetus/esi-ja-perusopetus/iltapaivatoiminta/"
                      text="https://www.hameenkyro.fi/kasvatus-ja-opetus/esi-ja-perusopetus/iltapaivatoiminta/"
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
                The personal data have been retrieved from the population data
                and cannot be changed with this application. If there are any
                errors in the personal data, please update the information on
                the website of the{' '}
                <ExternalLink
                  text="Digital and Population Data Services Agency"
                  href="https://dvv.fi/en/individuals"
                  newTab
                />
                . If your address is about to change, you can add the future
                address in a separate box on the application form; add the
                future address both for the child and the custodian. The address
                information is only considered official once it has been updated
                in the population data system. Decisions on early childhood
                education and care, service vouchers, pre-school education and
                information on open early childhood education places are also
                automatically sent to the custodian at a different address found
                in the population data.
              </P>
            ),
            futureAddressInfo: `For early childhood education and pre-school education in Hämeenkyrö, the official address is the address obtained from population data. The address in the population data register changes when the applicant submits a notification of change of address to the post office or the local register office.`
          },
          fee: {
            info: {
              DAYCARE: (
                <P>
                  The client fees for municipal early childhood education and
                  the own deductible part of the service voucher are based on
                  the Act on Client Fees in Early Childhood Education and Care
                  (1503/2016). The client fee is determined by the size of the
                  family, the need for service as well the gross income. New
                  clients must fill in the client fee form and submit the
                  required appendices to the Client fees of Early childhood
                  education within a month from when the care started at the
                  latest.
                </P>
              ),
              PRESCHOOL: (
                <P>
                  TODO: Esiopetus on maksutonta, mutta sitä täydentävä toiminta
                  on maksullista. Jos lapsi osallistuu esiopetusta täydentävään
                  toimintaan, perhe toimittaa tuloselvityksen bruttotuloistaan
                  tuloselvityslomakkeella viimeistään kahden viikon kuluessa
                  siitä, kun lapsi on aloittanut esiopetuksen.
                </P>
              )
            },
            links: (
              <P>
                You will find further information on client fees for early
                childhood education on{' '}
                <ExternalLink
                  href="https://www.hameenkyro.fi/kasvatus-ja-opetus/varhaiskasvatus/asiakasmaksut/"
                  text="the website of the municipality of Hämeenkyrö"
                  newTab
                />
              </P>
            )
          },
          additionalDetails: {
            dietInfo: (
              <>
                In the case of a special diet, the custodian submits to the
                early childhood education or pre-school education centre a{' '}
                <ExternalLink
                  href="https://www.hameenkyro.fi/kasvatus-ja-opetus/esi-ja-perusopetus/kouluruokailu-ja-ruokalista/"
                  text="Notification of Special Diet form"
                  newTab
                />
                , completed and signed by a doctor or nutritionist, which is
                valid for a limited period of time.
              </>
            )
          }
        }
      },
      applicationsList: {
        title: `Applying for early childhood education and pre-primary education`,
        summary: (
          <P width="800px">
            The child&apos;s custodian can apply for early childhood education,
            pre-primary education and a club for the child. Information about
            the custodian&apos;s children is automatically retrieved from the
            Population data register for this view.
          </P>
        )
      },
      children: {
        pageDescription:
          "General information related to your children's early childhood education is displayed on this page.",
        vasu: {
          sharingLeopsDisclaimer:
            "When a child changes to another early childhood education unit in the Municipality of Hämeenkyrö, the early childhood education plan and other early childhood education documents are transferred to the new unit (also applies to early childhood education organized by Hämeenkyrö as a purchasing service). The guardian will be informed about the transfer in advance. We ask the guardian for permission to hand over the early childhood education plan to an outsider. However, if a child enters private or other municipality's early childhood education, the early childhood education plan must be submitted to the new early childhood education provider even without permission, if the information is necessary for arranging the child's early childhood education (section 41 (3) of the Early Childhood Education Act). In addition, the early childhood education plan must be submitted to the organizer of pre-primary or primary education if it is necessary for the organization of the child's education (section 41 (4) of the Basic Education Act). The guardian will be informed of the transfer in advance."
        }
      },
      footer: {
        cityLabel: '© Municipality of Hämeenkyrö',
        privacyPolicyLink: (
          <ExternalLink
            href="https://www.hameenkyro.fi/kunta-ja-hallinto/asiointi/tietosuoja-ja-selosteet/"
            text="Privacy Notices"
            newTab={true}
            data-qa="footer-policy-link"
          />
        ),
        sendFeedbackLink: (
          <ExternalLink
            href="https://www.hameenkyro.fi/kunta-ja-hallinto/osallistu-ja-vaikuta/palaute/"
            text="Give feedback"
            newTab={true}
            data-qa="footer-feedback-link"
          />
        )
      },
      loginPage: {
        applying: {
          infoBullets: [
            `apply for an early childhood, pre-primary or club place for your child or view a previous application`,
            `view pictures and other documents related to your child’s early childhood and pre-primary`,
            "report your or your child's income information",
            `accept your child's early childhood, pre-primary or club place`,
            "terminate your child's early childhood or club place."
          ]
        },
        login: {
          infoBoxText: (
            <>
              <P>
                If you are not able to log in here, see the instructions{' '}
                <a
                  href="https://www.hameenkyro.fi/kasvatus-ja-opetus/varhaiskasvatus/"
                  target="_blank"
                  rel="noreferrer"
                >
                  eVaka | Municipality of Hämeenkyrö
                </a>
                . You can also log in with strong authentication.
              </P>
            </>
          ),
          paragraph: `Guardians whose child is already in early childhood education or preschool: take care of your child's daily affairs, such as reading messages and reporting the child's attendance and absence times.`
        },
        title: `Municipality of Hämeenkyrö early childhood and pre-primary education`
      },
      map: {
        mainInfo: `In this view you can locate on the map all of Hämeenkyrö’s early childhood education units and clubs. Regional service voucher units and clubs can also be found on the map. Pre-primary education is mainly organized in schools.`,
        privateUnitInfo: <></>,
        serviceVoucherLink:
          'https://www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus/paivakodit.html#palvelusetelipaivakodit',
        searchPlaceholder: 'E.g. Amurin päiväkoti'
      },
      decisions: {
        summary: (
          <P width="800px">
            This page displays the received decisions regarding your child.
          </P>
        ),
        applicationDecisions: {
          type: {
            PRESCHOOL_DAYCARE: 'supplementary early childhood education',
            PRESCHOOL_CLUB: 'pre-school club'
          },
          confirmationInfo: {
            preschool:
              'The place of pre-primary education and/or the place of activities that supplement pre-primary education must be received or rejected without delay, no later than within two weeks of receiving notification of the decision. If you have applied for both pre-primary education and activities that supplement pre-primary education, you will receive separate decisions on both.'
          },
          summary:
            'The place indicated in the decision must be received or rejected without delay, no later than two weeks after the decision has been notified.',
          warnings: {
            doubleRejectWarning: {
              text: 'You are rejecting an offer on pre-primary placement. The activities supplementing pre-primary education placement will also be rejected.'
            }
          },
          response: {
            disabledInfo:
              'N.B! You will be able to receive / reject the place of activities that complement preschool education only after you have received a place in preschool education.'
          }
        }
      },
      placement: {
        type: {
          PRESCHOOL_DAYCARE:
            'Supplementary early childhood education in day care centre',
          PRESCHOOL_CLUB: 'Pre-school clubs in school from 7 am to 5 pm',
          PRESCHOOL_WITH_DAYCARE:
            'Supplementary early childhood education in day care centre'
        }
      },
      income: {
        description: (
          <>
            <p data-qa="income-description-p1">
              On this page, you can submit statements on your earnings that
              affect the early childhood education fee. You can also view, edit,
              or delete income statements that you have submitted until the
              authority has processed the information. After the form has been
              processed, you can update your income information by submitting a
              new form.
            </p>
            <p>
              <strong>
                Both adults living in the same household must submit their own
                separate income statements.
              </strong>
            </p>
            <p data-qa="income-description-p2">
              The client fees for municipal early childhood education are
              determined as a percentage of the family’s gross income. The fees
              vary according to family size, income and time in early childhood
              education.
            </p>
            <p>
              <a href="https://www.hameenkyro.fi/kasvatus-ja-opetus/varhaiskasvatus/asiakasmaksut/">
                Further information on client fees.
              </a>
            </p>
          </>
        ),
        formDescription: (
          <>
            <P data-qa="income-formDescription-p1">
              The income statement and its attachments must be submitted within
              a month from the beginning of early childhood education. In case
              of incomplete income information, the fee may be set at the
              highest fee.
            </P>
            <P>
              The client fee is charged from the first day of early education in
              accordance with the decision.
            </P>
            <P>
              The client must immediately inform the client fees for Early
              childhood education of changes in income and family size.{' '}
            </P>
            <P>
              <strong>To be noted:</strong>
            </P>
            <Gap size="xs" />
            <UnorderedList>
              <li>
                If your income exceeds the highest payment income threshold
                according to family size, accept the highest early childhood
                education fee. In this case, you do not need to submit an income
                statement.
              </li>
              <li>
                If there&apos;s another adult in your family, they must also
                submit an income statement by personally logging into eVaka and
                filling out this form.
              </li>
              <li>
                See current income thresholds{' '}
                <a
                  target="_blank"
                  rel="noreferrer"
                  href="https://www.hameenkyro.fi/kasvatus-ja-opetus/varhaiskasvatus/asiakasmaksut/"
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
            This accessibility statement applies to the Municipality of
            Hämeenkyrö’s early childhood education online service eVaka at{' '}
            <a href="https://evaka.hameenkyro.fi">evaka.hameenkyro.fi</a>. The
            Municipality of Hämeenkyrö endeavours to ensure the accessibility of
            the online service, continuously improve the user experience and
            apply appropriate accessibility standards.
          </P>
          <P>
            The accessibility of the service was assessed by the development
            team of the service, and this statement was drawn up on 12 April
            2022.
          </P>
          <H2>Compliance of the service</H2>
          <P>
            The online service complies with the statutory critical
            accessibility requirements in accordance with Level AA of the
            Accessibility Guidelines for the WCAG v2.1. The service is not yet
            fully compliant with the requirements.
          </P>
          <H2>Measures to support accessibility</H2>
          <P>
            The accessibility of the online service is ensured, among other
            things, by the following measures:
          </P>
          <ul>
            <li>
              Accessibility has been taken into account from the beginning of
              the design phase, for example, when choosing the colours and font
              sizes of the service.
            </li>
            <li>
              The service elements have been defined in consistently in terms of
              semantics.
            </li>
            <li>The service is continuously tested with a screen reader.</li>
            <li>
              Various users test the service and give feedback on its
              accessibility.
            </li>
            <li>
              When website technology or content changes, its accessibility is
              ensured through constant monitoring.
            </li>
          </ul>
          <P>
            This statement will be updated in conjunction with website changes
            and accessibility evaluations.
          </P>
          <H2>Known accessibility issues</H2>
          <P>
            Users may still encounter some issues on the website. The following
            contains a description of known accessibility issues. If you notice
            an issue on the site that is not listed, please contact us.
          </P>
          <ul>
            <li>
              Navigating on the Messages page using the keyboard or screen
              reader still requires revision for part of moving and targeted
              elements.
            </li>
            <li>
              The service’s unit map cannot be navigated using the
              keyboard/screen reader, but the units can be browsed on the list
              available in the same view. The map used in the service is
              produced by a third party.
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
            <ExternalLink
              href="https://www.hameenkyro.fi/kasvatus-ja-opetus/varhaiskasvatus/"
              text="Early childhood and pre-primary education customer service"
            />
          </P>
          <H2>Give feedback</H2>
          <P>
            If you notice an accessibility gap in our online service, please let
            us know! You can give us feedback using the{' '}
            <ExternalLink
              href="https://www.hameenkyro.fi/kunta-ja-hallinto/osallistu-ja-vaikuta/palaute/"
              text="Give accessibility feedback with this web form"
            />
          </P>
          <H2>Supervisory authority</H2>
          <P>
            If you notice any accessibility issues on the website, please send
            us, the site administrator, feedback first. It may take us up to 14
            days to reply. If you are not satisfied with the reply or you do not
            receive a reply within two weeks, you can give feedback to the
            Regional State Administrative Agency for Southern Finland. The
            website of the Regional State Administrative Agency for Southern
            Finland explains in detail how a complaint can be submitted, and how
            the matter will be processed.
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
      )
    }
  },
  cityLogo: {
    src: HameenkyroLogo,
    alt: 'Hämeenkyrö logo'
  },
  mapConfig: {
    center: [61.63827, 23.19625],
    initialZoom: 14,
    addressZoom: 14,
    searchAreaRect: {
      maxLatitude: 61.65501,
      minLatitude: 61.63087,
      maxLongitude: 23.21197,
      minLongitude: 23.17912
    },
    careTypeFilters: ['DAYCARE', 'PRESCHOOL', 'CLUB'],
    unitProviderTypeFilters: [
      'MUNICIPAL',
      'PURCHASED',
      'PRIVATE',
      'PRIVATE_SERVICE_VOUCHER'
    ]
  },
  featureFlags,
  getMaxPreferredUnits(type) {
    if (type === 'PRESCHOOL') {
      return 1
    }
    return 3
  }
}

export default customizations
