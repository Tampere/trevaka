// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka.async

import fi.espoo.evaka.pis.service.FridgeFamilyService
import fi.espoo.evaka.shared.PersonId
import fi.espoo.evaka.shared.async.AsyncJobPayload
import fi.espoo.evaka.shared.async.AsyncJobPool
import fi.espoo.evaka.shared.async.AsyncJobRunner
import fi.espoo.evaka.shared.auth.AuthenticatedUser
import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.domain.EvakaClock
import org.springframework.stereotype.Component

sealed interface TrevakaAsyncJob : AsyncJobPayload {
    data class UpdateGuardianOrChildFromVtj(val personId: PersonId) : TrevakaAsyncJob {
        override val user: AuthenticatedUser? = null
    }

    companion object {
        val pool =
            AsyncJobRunner.Pool(
                AsyncJobPool.Id(TrevakaAsyncJob::class, "trevaka"),
                AsyncJobPool.Config(concurrency = 1),
                setOf(UpdateGuardianOrChildFromVtj::class),
            )
    }
}

class TrevakaAsyncJobRegistration(
    runner: AsyncJobRunner<TrevakaAsyncJob>,
    updateGuardianOrChildFromVtjJob: UpdateGuardianOrChildFromVtjJob,
) {
    init {
        updateGuardianOrChildFromVtjJob.let { runner.registerHandler(it::updateGuardianOrChildFromVtj) }
    }
}

@Component
class UpdateGuardianOrChildFromVtjJob(private val fridgeFamilyService: FridgeFamilyService) {
    fun updateGuardianOrChildFromVtj(
        db: Database.Connection,
        clock: EvakaClock,
        msg: TrevakaAsyncJob.UpdateGuardianOrChildFromVtj,
    ) {
        fridgeFamilyService.updateGuardianOrChildFromVtj(db, AuthenticatedUser.SystemInternalUser, clock, msg.personId)
    }
}
