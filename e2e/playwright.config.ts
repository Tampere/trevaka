// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import { dirname, join } from 'node:path'
import { fileURLToPath } from 'node:url'

import type { PlaywrightTestConfig } from '@playwright/test'

const e2eDir = dirname(fileURLToPath(import.meta.url))
const baseURL = process.env.BASE_URL ?? 'http://localhost:9099'
const isCI = process.env.CI === 'true' || process.env.CI === '1'
const isHeaded = process.env.HEADED === 'true' || process.env.HEADED === '1'

const config: PlaywrightTestConfig = {
  testDir: './src/e2e/specs',
  testMatch: '**/*.spec.ts',
  outputDir: join(e2eDir, 'test-results', 'artifacts'),
  fullyParallel: true,
  timeout: isHeaded ? 1_000_000_000 : 60_000,
  workers: 1,
  retries: isCI ? 2 : 0,
  reporter: [
    ['list'],
    ['junit', { outputFile: join(e2eDir, 'test-results', 'junit.xml') }]
  ],
  use: {
    baseURL,
    headless: !isHeaded,
    ignoreHTTPSErrors: true,
    trace: 'retain-on-failure',
    screenshot: 'only-on-failure',
    locale: 'fi-FI',
    timezoneId: 'Europe/Helsinki'
  },
  projects: [
    {
      name: 'chromium',
      use: { browserName: 'chromium' }
    }
  ],
  tsconfig: join(e2eDir, 'tsconfig.json')
}

export default config
