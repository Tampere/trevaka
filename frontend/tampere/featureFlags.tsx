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
      citizenAttendanceSummary: true
    }
  },
  staging: {
    citizenShiftCareAbsence: false,
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
      intermittentShiftCare: true,
      citizenAttendanceSummary: true
    }
  },
  prod: {
    citizenShiftCareAbsence: false,
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
      intermittentShiftCare: true,
      citizenAttendanceSummary: true
    }
  }
}

const featureFlags = features[env()]

export default featureFlags
