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
    financeDecisionHandlerSelect: true,
    childDiscussion: false,
    feeDecisionPreschoolClubFilter: true,
    experimental: {
      leops: true,
      assistanceNeedDecisions: true,
      assistanceNeedPreschoolDecisions: true,
      staffAttendanceTypes: true,
      fosterParents: true,
      serviceWorkerMessaging: true,
      personDuplicate: true,
      childDocuments: true,
      intermittentShiftCare: true,
      citizenEmailNotificationSettings: true,
      citizenAttendanceSummary: true
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
    financeDecisionHandlerSelect: true,
    childDiscussion: false,
    feeDecisionPreschoolClubFilter: true,
    experimental: {
      leops: true,
      assistanceNeedDecisions: true,
      staffAttendanceTypes: true,
      fosterParents: true,
      serviceWorkerMessaging: true,
      personDuplicate: true,
      intermittentShiftCare: true
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
    financeDecisionHandlerSelect: true,
    childDiscussion: false,
    feeDecisionPreschoolClubFilter: true,
    experimental: {
      leops: true,
      assistanceNeedDecisions: true,
      staffAttendanceTypes: true,
      fosterParents: true,
      serviceWorkerMessaging: true,
      personDuplicate: true,
      intermittentShiftCare: true
    }
  }
}

const featureFlags = features[env()]

export default featureFlags
