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
  employeeMobileChildAttendanceReservationEdit: true,
  employeeMobileStaffAttendanceEdit: true,
  employeeMobileConfirmedDaysReservations: true,
  assistanceNeedDecisionsLanguageSelect: false
}

const features: Features = {
  default: {
    ...prod
  },
  staging: {
    ...prod
  },
  prod
}

const featureFlags = features[env()]

export default featureFlags
