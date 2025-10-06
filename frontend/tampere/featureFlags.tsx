{
  /*
SPDX-FileCopyrightText: 2021 City of Tampere

SPDX-License-Identifier: LGPL-2.1-or-later
*/
}

import type { FeatureFlags } from 'lib-customizations/types'

import type { Env } from './env'
import { env } from './env'

type Features = {
  default: FeatureFlags
} & Record<Env, FeatureFlags>

const prod: FeatureFlags = {
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
  preparatory: false,
  assistanceActionOther: false,
  financeDecisionHandlerSelect: true,
  feeDecisionPreschoolClubFilter: true,
  placementGuarantee: true,
  staffAttendanceTypes: true,
  intermittentShiftCare: true,
  citizenAttendanceSummary: true,
  noAbsenceType: true,
  voucherUnitPayments: true,
  voucherValueSeparation: false,
  assistanceNeedDecisionsLanguageSelect: false,
  extendedPreschoolTerm: false,
  hideOvertimeSelection: false,
  hideSicknessSelection: true,
  hideChildSicknessSelection: true,
  discussionReservations: true,
  aromiIntegration: true,
  serviceApplications: true,
  absenceApplications: true,
  multiSelectDeparture: true,
  requireAttachments: true,
  showCitizenApplicationPreschoolTerms: true,
  decisionChildDocumentTypes: true
}

const features: Features = {
  default: {
    ...prod,
    environmentLabel: 'Test',
    hideSicknessSelection: false,
    hideChildSicknessSelection: false,
    archiveIntegration: {
      decisions: true,
      childDocuments: true
    },
    citizenChildDocumentTypes: true
  },
  staging: {
    ...prod,
    environmentLabel: 'Staging'
  },
  prod
}

const featureFlags = features[env()]

export default featureFlags
