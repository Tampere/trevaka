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
  discussionReservations: true,
  aromiIntegration: true,
  serviceApplications: true,
  multiSelectDeparture: true,
  requireAttachments: true,
  showCitizenApplicationPreschoolTerms: true
}

const features: Features = {
  default: {
    ...prod,
    environmentLabel: 'Test',
    absenceApplications: true,
    citizenChildDocumentTypes: true,
    decisionChildDocumentTypes: true
  },
  staging: {
    ...prod,
    environmentLabel: 'Staging'
  },
  prod
}

const featureFlags = features[env()]

export default featureFlags
