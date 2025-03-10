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
  personDuplicate: true,
  intermittentShiftCare: true,
  citizenAttendanceSummary: true,
  noAbsenceType: true,
  voucherUnitPayments: true,
  voucherValueSeparation: false,
  assistanceNeedDecisionsLanguageSelect: false,
  extendedPreschoolTerm: false,
  discussionReservations: true,
  serviceApplications: true,
  multiSelectDeparture: true,
  requireAttachments: true
}

const features: Features = {
  default: {
    ...prod,
    environmentLabel: 'Test',
    aromiIntegration: true
  },
  staging: {
    ...prod,
    environmentLabel: 'Staging'
  },
  prod
}

const featureFlags = features[env()]

export default featureFlags
