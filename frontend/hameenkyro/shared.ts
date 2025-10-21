// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import type { CustomizableStaffAttendanceType } from '../types'

import type { Env } from './env'
import { env } from './env'

interface SharedCustomizations {
  additionalStaffAttendanceTypes: CustomizableStaffAttendanceType[]
}

const prod: SharedCustomizations = {
  additionalStaffAttendanceTypes: [
    'OTHER_WORK',
    'TRAINING',
    'OVERTIME',
    'JUSTIFIED_CHANGE'
  ]
}

const sharedCustomizationsByEnv: { default: SharedCustomizations } & Record<
  Env,
  SharedCustomizations
> = {
  default: {
    ...prod,
    additionalStaffAttendanceTypes: [
      ...prod.additionalStaffAttendanceTypes,
      'SICKNESS',
      'CHILD_SICKNESS'
    ]
  },
  prod
}

const sharedCustomizations = sharedCustomizationsByEnv[env()]

export default sharedCustomizations
