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
  urgencyAttachmentsEnabled: false,
  preschoolEnabled: false,
  assistanceActionOtherEnabled: false
}

export default featureFlags
