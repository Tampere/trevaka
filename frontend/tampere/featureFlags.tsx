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
    groupsTableServiceNeeds: true,
    urgencyAttachments: false,
    preschool: true,
    assistanceActionOther: false,
    experimental: {
      leops: true,
      assistanceNeedDecisions: true,
      staffAttendanceTypes: true,
      fosterParents: true
    }
  },
  staging: {
    citizenShiftCareAbsence: false,
    daycareApplication: {
      dailyTimes: false
    },
    groupsTableServiceNeeds: true,
    urgencyAttachments: false,
    preschool: true,
    assistanceActionOther: false,
    experimental: {
      assistanceNeedDecisions: true,
      staffAttendanceTypes: false,
      fosterParents: true
    }
  },
  prod: {
    citizenShiftCareAbsence: false,
    daycareApplication: {
      dailyTimes: false
    },
    groupsTableServiceNeeds: true,
    urgencyAttachments: false,
    preschool: false,
    assistanceActionOther: false,
    experimental: {
      assistanceNeedDecisions: true,
      staffAttendanceTypes: false,
      fosterParents: true
    }
  }
}

const featureFlags = features[env()]

export default featureFlags
