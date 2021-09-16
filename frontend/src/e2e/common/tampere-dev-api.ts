// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import axios from 'axios'
import { DevApiError } from 'e2e-test-common/dev-api'
import config from 'e2e-test-common/config'

export const devClient = axios.create({
    baseURL: config.devApiGwUrl + '/tampere'
})

export async function resetDatabaseForE2ETests(): Promise<void> {
    try {
        await devClient.post(`/reset-tampere-db-for-e2e-tests`, null)
    } catch (e) {
        throw new DevApiError(e)
    }
}
