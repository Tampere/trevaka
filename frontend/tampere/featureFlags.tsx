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

const prod: FeatureFlags = {
  citizenShiftCareAbsence: false,
  daycareApplication: {
    dailyTimes: false,
    serviceNeedOption: true
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
  feeDecisionPreschoolClubFilter: true,
  placementGuarantee: true,
  staffAttendanceTypes: true,
  personDuplicate: true,
  intermittentShiftCare: true,
  citizenAttendanceSummary: true,
  noAbsenceType: true,
  voucherUnitPayments: false,
  assistanceNeedDecisionsLanguageSelect: false,
  extendedPreschoolTerm: false,
  automaticFixedScheduleAbsences: false
}

const features: Features = {
  default: {
    ...prod,
    voucherUnitPayments: true,
    discussionReservations: true,
    automaticFixedScheduleAbsences: true
  },
  staging: {
    ...prod
  },
  prod
}

const featureFlags = features[env()]

export default featureFlags
