// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import { FeatureFlags } from 'lib-customizations/types'

const featureFlags: FeatureFlags = {
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
  assistanceNeedDecisionsLanguageSelect: false
}

export default featureFlags
