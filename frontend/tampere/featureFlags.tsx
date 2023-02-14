{
  /*
SPDX-FileCopyrightText: 2021 City of Tampere

SPDX-License-Identifier: LGPL-2.1-or-later
*/
}

import { FeatureFlags } from 'lib-customizations/types'

import { env, Env } from './env'

type Features = {
  default: FeatureFlags
} & {
  [k in Env]: FeatureFlags
}

const features: Features = {
  default: {
    citizenShiftCareAbsence: false,
    citizenContractDayAbsence: true,
    daycareApplication: {
      dailyTimes: false
    },
    preschoolApplication: {
      connectedDaycarePreferredStartDate: true,
      serviceNeedOption: true
    },
    decisionDraftMultipleUnits: true,
    urgencyAttachments: false,
    preschool: true,
    preparatory: false,
    assistanceActionOther: false,
    experimental: {
      leops: true,
      assistanceNeedDecisions: true,
      staffAttendanceTypes: true,
      fosterParents: true,
      serviceWorkerMessaging: true,
      personDuplicate: true
    }
  },
  staging: {
    citizenShiftCareAbsence: false,
    citizenContractDayAbsence: true,
    daycareApplication: {
      dailyTimes: false
    },
    preschoolApplication: {
      connectedDaycarePreferredStartDate: true,
      serviceNeedOption: true
    },
    decisionDraftMultipleUnits: true,
    urgencyAttachments: false,
    preschool: true,
    preparatory: false,
    assistanceActionOther: false,
    experimental: {
      assistanceNeedDecisions: true,
      staffAttendanceTypes: true,
      fosterParents: true
    }
  },
  prod: {
    citizenShiftCareAbsence: false,
    citizenContractDayAbsence: true,
    daycareApplication: {
      dailyTimes: false
    },
    preschoolApplication: {
      connectedDaycarePreferredStartDate: true,
      serviceNeedOption: true
    },
    decisionDraftMultipleUnits: true,
    urgencyAttachments: false,
    preschool: true,
    preparatory: false,
    assistanceActionOther: false,
    experimental: {
      assistanceNeedDecisions: true,
      staffAttendanceTypes: true,
      fosterParents: true
    }
  }
}

const featureFlags = features[env()]

export default featureFlags
