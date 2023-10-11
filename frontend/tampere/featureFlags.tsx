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
    placementGuarantee: true,
    assistanceNeedPreschoolDecisions: true,
    staffAttendanceTypes: true,
    personDuplicate: true,
    childDocuments: true,
    intermittentShiftCare: true,
    citizenAttendanceSummary: true,
    feeDecisionIgnoredStatus: true,
    hojks: true,
    noAbsenceType: true,
    voucherUnitPayments: false,
    assistanceNeedDecisionsLanguageSelect: false
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
    placementGuarantee: true,
    assistanceNeedPreschoolDecisions: true,
    staffAttendanceTypes: true,
    personDuplicate: true,
    childDocuments: true,
    intermittentShiftCare: true,
    citizenAttendanceSummary: true,
    hojks: true,
    noAbsenceType: true,
    voucherUnitPayments: false,
    assistanceNeedDecisionsLanguageSelect: false
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
    placementGuarantee: true,
    assistanceNeedPreschoolDecisions: true,
    staffAttendanceTypes: true,
    personDuplicate: true,
    childDocuments: true,
    intermittentShiftCare: true,
    citizenAttendanceSummary: true,
    hojks: true,
    noAbsenceType: true,
    voucherUnitPayments: false,
    assistanceNeedDecisionsLanguageSelect: false
  }
}

const featureFlags = features[env()]

export default featureFlags
