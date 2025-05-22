// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import type { FeatureFlags } from 'lib-customizations/types'

const featureFlags: FeatureFlags = {
  environmentLabel: null,
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
  urgencyAttachments: true,
  preschool: true,
  preparatory: true,
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
  voucherValueSeparation: false,
  assistanceNeedDecisionsLanguageSelect: false,
  extendedPreschoolTerm: false,
  hideClubApplication: false,
  discussionReservations: true,
  jamixIntegration: true,
  serviceApplications: true,
  multiSelectDeparture: true,
  requireAttachments: true
}

export default featureFlags
