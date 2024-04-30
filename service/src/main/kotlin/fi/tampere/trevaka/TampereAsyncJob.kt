// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka

import fi.espoo.evaka.shared.async.AsyncJobPayload
import fi.espoo.evaka.shared.async.AsyncJobPool
import fi.espoo.evaka.shared.async.AsyncJobRunner
import fi.espoo.evaka.shared.auth.AuthenticatedUser
import fi.tampere.trevaka.bi.BiExportJob
import fi.tampere.trevaka.bi.BiTable

sealed interface TampereAsyncJob : AsyncJobPayload {
    data class SendBiTable(val table: BiTable) : TampereAsyncJob {
        override val user: AuthenticatedUser? = null
    }

    companion object {
        val pool =
            AsyncJobRunner.Pool(
                AsyncJobPool.Id(TampereAsyncJob::class, "tampere"),
                AsyncJobPool.Config(concurrency = 1),
                setOf(SendBiTable::class),
            )
    }
}

class TampereAsyncJobRegistration(
    runner: AsyncJobRunner<TampereAsyncJob>,
    biExportJob: BiExportJob,
) {
    init {
        biExportJob.let { runner.registerHandler(it::sendBiTable) }
    }
}
