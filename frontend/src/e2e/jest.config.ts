// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import type { Config } from '@jest/types'

export const createConfig = (customizations: string): Config.InitialOptions => ({
  displayName: `e2e-playwright (${customizations})`,
  preset: 'ts-jest',
  testEnvironment: '../../../evaka/frontend/src/e2e-test/jest-environment',
  testRunner: 'jest-circus/runner',
  moduleNameMapper: {
    '^e2e/(.*)$': '<rootDir>/$1',
    '^@evaka/customizations/(.*)': `<rootDir>/../../${customizations}/$1`,
    '^Icons$': '<rootDir>/../../../evaka/frontend/src/lib-icons/fontawesome.d.ts',
    '\.(jpg|jpeg|png|svg)$': '<rootDir>/../../../evaka/frontend/assetsTransformer.js'
  }
});
