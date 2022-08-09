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
    citizenShiftCareAbsenceEnabled: false,
    daycareApplication: {
      dailyTimesEnabled: false
    },
    groupsTableServiceNeedsEnabled: true,
    evakaLogin: true,
    financeBasicsPage: true,
    urgencyAttachmentsEnabled: false,
    preschoolEnabled: true,
    assistanceActionOtherEnabled: false,
    experimental: {
      messageAttachments: true,
      personalDetailsPage: true,
      mobileMessages: true,
      assistanceNeedDecisions: true,
      staffAttendanceTypes: true
    },
    adminSettingsEnabled: true
  },
  staging: {
    citizenShiftCareAbsenceEnabled: false,
    daycareApplication: {
      dailyTimesEnabled: false
    },
    groupsTableServiceNeedsEnabled: true,
    evakaLogin: true,
    financeBasicsPage: true,
    urgencyAttachmentsEnabled: false,
    preschoolEnabled: false,
    assistanceActionOtherEnabled: false,
    experimental: {
      messageAttachments: true,
      personalDetailsPage: true,
      mobileMessages: true,
      assistanceNeedDecisions: false,
      staffAttendanceTypes: false
    },
    adminSettingsEnabled: true
  },
  prod: {
    citizenShiftCareAbsenceEnabled: false,
    daycareApplication: {
      dailyTimesEnabled: false
    },
    groupsTableServiceNeedsEnabled: true,
    evakaLogin: true,
    financeBasicsPage: true,
    urgencyAttachmentsEnabled: false,
    preschoolEnabled: false,
    assistanceActionOtherEnabled: false,
    experimental: {
      messageAttachments: true,
      personalDetailsPage: true,
      mobileMessages: true,
      assistanceNeedDecisions: false,
      staffAttendanceTypes: false
    },
    adminSettingsEnabled: true
  }
}

const featureFlags = features[env()]

export default featureFlags
