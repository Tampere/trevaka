{
  /*
SPDX-FileCopyrightText: 2021 City of Tampere

SPDX-License-Identifier: LGPL-2.1-or-later
*/
}

import React from 'react'

import { EmployeeCustomizations } from 'lib-customizations/types'

import TampereLogo from './city-logo.svg'
import { env } from './env'
import featureFlags from './featureFlags'

const customizations: EmployeeCustomizations = {
  appConfig: {},
  translations: {
    fi: {
      // override translations here
      application: {
        serviceNeed: {
          connectedLabel: 'Täydentävä varhaiskasvatus',
          connectedValue: 'Haen myös täydentävää varhaiskasvatusta'
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
          PRESCHOOL_DAYCARE: 'Esiopetus & täydentävä',
          PREPARATORY_DAYCARE: 'Valmistava & täydentävä',
          DAYCARE_ONLY: 'Myöhemmin haettu täydentävä'
        }
      },
      absences: {
        absenceCategories: {
          NONBILLABLE:
            'Ei laskuteta eVakasta (käytetään vain kerhotoiminnassa sekä koululaisen vuorohoidossa)',
          BILLABLE: 'Varhaiskasvatus'
        },
        absenceTypes: {
          PLANNED_ABSENCE: 'Sopimuksen mukainen poissaolo',
          FORCE_MAJEURE: 'Hyvityspäivä',
          FREE_ABSENCE: 'Kesäajan maksuton poissaolo'
        },
        absenceTypesShort: {
          PLANNED_ABSENCE: 'Sopim, muk.',
          FORCE_MAJEURE: 'Hyvitys'
        },
        absenceTypeInfo: {
          NO_ABSENCE: 'Lapsi läsnä varhaiskasvatuksessa.',
          OTHER_ABSENCE:
            'Käytetään tapauksissa, kun lapsi on poissa varhaiskasvatuksesta huoltajan ilmoituksesta.',
          UNKNOWN_ABSENCE:
            'Käytetään tapauksissa, kun lapsen poissaolosta ei tule huoltajalta mitään ilmoitusta.',
          PLANNED_ABSENCE:
            'Käytetään etukäteen ilmoitetuista poissaoloista, kun asiakkalla käytössä 10 pv/kk tai 15 pv/kk sopimuspäivät. Ilta- ja vuorohoitoyksiköissä kaikille sopimuspäiväläisille (ml. päiväryhmät), on laitettava merkintä myös viikonlopuille.',
          SICKLEAVE: 'Lapsi sairaana.',
          TEMPORARY_RELOCATION:
            'Käytetään tapauksissa, kun lapselle on tehty varasijoitus toiseen yksikköön.',
          PARENTLEAVE:
            'Käytetään tapauksissa, kun lapsi on poissa varhaiskasvatuksesta isyysvapaan ajan.',
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
            PLANNED_ABSENCE: 'Sopimuksen mukainen poissaolo',
            FORCE_MAJEURE: 'Hyvityspäivä',
            FREE_ABSENCE: 'Kesäajan maksuton poissaolo'
          }
        }
      },
      common: {
        careTypeLabels: {
          preschool: 'Esiopetusta täydentävä toiminta'
        },
        types: {
          PRESCHOOL_DAYCARE: 'Täydentävä varhaiskasvatus',
          PREPARATORY_DAYCARE: 'Täydentävä varhaiskasvatus'
        }
      },
      childInformation: {
        assistanceNeed: {
          fields: {
            /* eslint-disable */
            capacityFactorInfo: (
              <ol style={{ margin: '0', padding: '0 1em' }}>
                <li>
                  Kaupungin päiväkodeissa kerroin merkitään integroidussa
                  varhaiskasvatusryhmässä oleville tehostettua tai erityistä
                  tukea tarvitseville lapsille ja missä tahansa ryhmässä
                  kotoutumisen tukea saaville lapsille. Kertoimen tallentaa
                  varhaiskasvatuksen erityisopettaja.
                </li>
                <li>
                  Mikäli ostopalvelu- tai palvelusetelipäiväkodissa olevalla
                  lapsella on tehostetun tai erityisen tuen tarve, voidaan
                  hänelle määritellä tuen kerroin. Päätöksen kertoimesta tekee
                  varhaiskasvatusjohtaja, varhaiskasvatuksen erityisopettajan
                  esityksen perusteella. Kertoimen tallentaa varhaiskasvatuksen
                  asiakaspalvelu.
                </li>
              </ol>
            ) as any,
            /* eslint-enable */
            bases: 'Tuen tarve'
          }
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
        vasu: {
          title: 'Varhaiskasvatussuunnitelma'
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
        preschoolDaycare: 'Täydentävä varhaiskasvatus'
      },
      footer: {
        cityLabel: 'Tampereen kaupunki',
        linkLabel: 'Tampereen varhaiskasvatus',
        linkHref:
          'https://www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus.html'
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
        loginAD: 'Tampere AD'
      },
      placement: {
        type: {
          DAYCARE: 'Varhaiskasvatus',
          DAYCARE_PART_TIME: 'Osapäiväinen varhaiskasvatus',
          TEMPORARY_DAYCARE: 'Tilapäinen kokopäiväinen varhaiskasvatus',
          PRESCHOOL_DAYCARE: 'Esiopetusta täydentävä varhaiskasvatus',
          CLUB: 'Kerho',
          SCHOOL_SHIFT_CARE: 'Koululaisten vuorohoito',
          PRESCHOOL_WITH_DAYCARE: 'Esiopetus ja täydentävä varhaiskasvatus',
          PREPARATORY_WITH_DAYCARE:
            'Valmistava opetus ja täydentävä varhaiskasvatus',
          PREPARATORY_DAYCARE: 'Valmistava opetus ja täydentävä varhaiskasvatus'
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
          email: 'etunimi.sukunimi@tampere.fi',
          url: 'esim. https://www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus/paivakodit/alueesi-paivakodit/keskusta/amurinpaivakoti.html',
          streetAddress: 'Kadun nimi esim. Suokatu 10',
          decisionCustomization: {
            name: 'esim. Amurin päiväkoti'
          }
        },
        field: {
          decisionCustomization: {
            handler: ['Palveluohjaus', 'Varhaiskasvatusyksikön johtaja']
          }
        }
      },
      welcomePage: {
        text: 'Olet kirjautunut sisään eVaka Tampere -palveluun. Käyttäjätunnuksellesi ei ole vielä annettu oikeuksia, jotka mahdollistavat palvelun käytön. Tarvittavat käyttöoikeudet saat omalta esimieheltäsi.'
      },
      invoices: {
        buttons: {
          individualSendAlertText: ''
        }
      },
      reports: {
        decisions: {
          preschoolDaycare: 'Esiopetus+täydentävä päätöksiä',
          preparatoryDaycare: 'Valmistava+täydentävä päätöksiä'
        },
        placementSketching: {
          connected: 'Täydentävä'
        }
      }
    },
    sv: {
      // override translations here
      application: {
        serviceNeed: {
          connectedLabel: 'Täydentävä varhaiskasvatus',
          connectedValue: 'Haen myös täydentävää varhaiskasvatusta'
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
          PRESCHOOL_DAYCARE: 'Esiopetus & täydentävä',
          PREPARATORY_DAYCARE: 'Valmistava & täydentävä',
          DAYCARE_ONLY: 'Myöhemmin haettu täydentävä'
        }
      },
      absences: {
        absenceCategories: {
          NONBILLABLE: 'Ei laskuteta eVakasta',
          BILLABLE: 'Varhaiskasvatus'
        },
        absenceTypes: {
          PLANNED_ABSENCE: 'Sopimuksen mukainen poissaolo',
          FORCE_MAJEURE: 'Hyvityspäivä',
          FREE_ABSENCE: 'Kesäajan maksuton poissaolo'
        },
        absenceTypesShort: {
          PLANNED_ABSENCE: 'Sopim, muk.',
          FORCE_MAJEURE: 'Hyvitys'
        },
        absenceTypeInfo: {
          NO_ABSENCE: 'Lapsi läsnä varhaiskasvatuksessa.',
          OTHER_ABSENCE:
            'Käytetään tapauksissa, kun lapsi on poissa varhaiskasvatuksesta huoltajan ilmoituksesta.',
          UNKNOWN_ABSENCE:
            'Käytetään tapauksissa, kun lapsen poissaolosta ei tule huoltajalta mitään ilmoitusta.',
          PLANNED_ABSENCE:
            'Käytetään etukäteen ilmoitetuista poissaoloista, kun asiakkalla käytössä 10 pv/kk tai 15 pv/kk sopimuspäivät. Ilta- ja vuorohoitoyksiköissä kaikille sopimuspäiväläisille (ml. päiväryhmät), on laitettava merkintä myös viikonlopuille.',
          SICKLEAVE: 'Lapsi sairaana.',
          TEMPORARY_RELOCATION:
            'Käytetään tapauksissa, kun lapselle on tehty varasijoitus toiseen yksikköön.',
          PARENTLEAVE:
            'Käytetään tapauksissa, kun lapsi on poissa varhaiskasvatuksesta isyysvapaan ajan.',
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
            PLANNED_ABSENCE: 'Sopimuksen mukainen poissaolo',
            FORCE_MAJEURE: 'Hyvityspäivä',
            FREE_ABSENCE: 'Kesäajan maksuton poissaolo'
          }
        }
      },
      common: {
        careTypeLabels: {
          preschool: 'Esiopetusta täydentävä toiminta'
        },
        types: {
          PRESCHOOL_DAYCARE: 'Täydentävä varhaiskasvatus',
          PREPARATORY_DAYCARE: 'Täydentävä varhaiskasvatus'
        }
      },
      childInformation: {
        assistanceNeed: {
          fields: {
            /* eslint-disable */
            capacityFactorInfo: (
              <ol style={{ margin: '0', padding: '0 1em' }}>
                <li>
                  Kaupungin päiväkodeissa kerroin merkitään integroidussa
                  varhaiskasvatusryhmässä oleville tehostettua tai erityistä
                  tukea tarvitseville lapsille ja missä tahansa ryhmässä
                  kotoutumisen tukea saaville lapsille. Kertoimen tallentaa
                  varhaiskasvatuksen erityisopettaja.
                </li>
                <li>
                  Mikäli ostopalvelu- tai palvelusetelipäiväkodissa olevalla
                  lapsella on tehostetun tai erityisen tuen tarve, voidaan
                  hänelle määritellä tuen kerroin. Päätöksen kertoimesta tekee
                  varhaiskasvatusjohtaja, varhaiskasvatuksen erityisopettajan
                  esityksen perusteella. Kertoimen tallentaa varhaiskasvatuksen
                  asiakaspalvelu.
                </li>
              </ol>
            ) as any,
            /* eslint-enable */
            bases: 'Tuen tarve'
          }
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
        vasu: {
          title: 'Varhaiskasvatussuunnitelma'
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
        preschoolDaycare: 'Täydentävä varhaiskasvatus'
      },
      footer: {
        cityLabel: 'Tampereen kaupunki',
        linkLabel: 'Tampereen varhaiskasvatus',
        linkHref:
          'https://www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus.html'
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
        loginAD: 'Tampere AD'
      },
      placement: {
        type: {
          DAYCARE: 'Varhaiskasvatus',
          DAYCARE_PART_TIME: 'Osapäiväinen varhaiskasvatus',
          TEMPORARY_DAYCARE: 'Tilapäinen kokopäiväinen varhaiskasvatus',
          PRESCHOOL_DAYCARE: 'Esiopetusta täydentävä varhaiskasvatus',
          CLUB: 'Kerho',
          SCHOOL_SHIFT_CARE: 'Koululaisten vuorohoito',
          PRESCHOOL_WITH_DAYCARE: 'Esiopetus ja täydentävä varhaiskasvatus',
          PREPARATORY_WITH_DAYCARE:
            'Valmistava opetus ja täydentävä varhaiskasvatus',
          PREPARATORY_DAYCARE: 'Valmistava opetus ja täydentävä varhaiskasvatus'
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
          email: 'etunimi.sukunimi@tampere.fi',
          url: 'esim. https://www.tampere.fi/varhaiskasvatus-ja-koulutus/varhaiskasvatus/paivakodit/alueesi-paivakodit/keskusta/amurinpaivakoti.html',
          streetAddress: 'Kadun nimi esim. Suokatu 10',
          decisionCustomization: {
            name: 'esim. Amurin päiväkoti'
          }
        },
        field: {
          decisionCustomization: {
            handler: ['Palveluohjaus', 'Varhaiskasvatusyksikön johtaja']
          }
        }
      },
      welcomePage: {
        text: 'Olet kirjautunut sisään eVaka Tampere -palveluun. Käyttäjätunnuksellesi ei ole vielä annettu oikeuksia, jotka mahdollistavat palvelun käytön. Tarvittavat käyttöoikeudet saat omalta esimieheltäsi.'
      },
      invoices: {
        buttons: {
          individualSendAlertText: ''
        }
      },
      reports: {
        decisions: {
          preschoolDaycare: 'Esiopetus+täydentävä päätöksiä',
          preparatoryDaycare: 'Valmistava+täydentävä päätöksiä'
        },
        placementSketching: {
          connected: 'Täydentävä'
        }
      }
    }
  },
  vasuTranslations: {
    FI: {},
    SV: {}
  },
  cityLogo: {
    src: TampereLogo,
    alt: 'Tampere logo'
  },
  featureFlags: { ...featureFlags, preschoolEnabled: true },
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
  assistanceMeasures: [],
  placementTypes: [
    'DAYCARE',
    'DAYCARE_PART_TIME',
    'TEMPORARY_DAYCARE',
    ...(env() === 'default' ? (['PRESCHOOL'] as const) : []),
    'PRESCHOOL_DAYCARE',
    'CLUB',
    'SCHOOL_SHIFT_CARE'
  ],
  placementPlanRejectReasons: ['REASON_1', 'REASON_2', 'REASON_3', 'OTHER'],
  unitProviderTypes: [
    'MUNICIPAL',
    'PURCHASED',
    'PRIVATE',
    'PRIVATE_SERVICE_VOUCHER'
  ],
  voucherValueDecisionTypes: ['NORMAL', 'RELIEF_ACCEPTED', 'RELIEF_REJECTED']
}

export default customizations
