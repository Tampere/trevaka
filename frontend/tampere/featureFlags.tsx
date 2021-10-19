{
  /*
SPDX-FileCopyrightText: 2021 City of Tampere

SPDX-License-Identifier: LGPL-2.1-or-later
*/
}

import { FeatureFlags } from 'lib-customizations/types'

const featureFlags: FeatureFlags = {
  daycareApplication: {
    serviceNeedOptionsEnabled: true,
    dailyTimesEnabled: false
  },
  groupsTableServiceNeedsEnabled: true,
  evakaLogin: true,
  financeBasicsPage: true,
  urgencyAttachmentsEnabled: false,
  preschoolEnabled: false,
  assistanceActionOtherEnabled: false,
  assistanceBasisOtherEnabled: false,
  experimental: {
    messageAttachments: true,
    vasu: true
  },
  pedagogicalDocumentsEnabled: true
}

export default featureFlags
