{
  /*
SPDX-FileCopyrightText: 2021 City of Tampere

SPDX-License-Identifier: LGPL-2.1-or-later
*/
}

import { FeatureFlags } from 'lib-customizations/types'

const featureFlags: FeatureFlags = {
  childIncomeEnabled: true,
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
    realtimeStaffAttendance: true,
    personalDetailsPage: true,
    mobileMessages: true,
    placementTermination: true
  },
  adminSettingsEnabled: true
}

export default featureFlags
