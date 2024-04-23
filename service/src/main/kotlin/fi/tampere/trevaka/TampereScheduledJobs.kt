// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka

import fi.espoo.evaka.ScheduledJobsEnv
import fi.espoo.evaka.shared.async.AsyncJobRunner
import fi.espoo.evaka.shared.async.AsyncJobType
import fi.espoo.evaka.shared.async.removeUnclaimedJobs
import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.domain.EvakaClock
import fi.espoo.evaka.shared.job.JobSchedule
import fi.espoo.evaka.shared.job.ScheduledJobDefinition
import fi.espoo.evaka.shared.job.ScheduledJobSettings
import fi.tampere.trevaka.bi.BiTable
import fi.tampere.trevaka.export.ExportUnitsAclService
import mu.KotlinLogging
import java.time.LocalTime

enum class TampereScheduledJob(
    val fn: (TampereScheduledJobs, Database.Connection, EvakaClock) -> Unit,
    val defaultSettings: ScheduledJobSettings,
) {
    ExportUnitsAcl(
        TampereScheduledJobs::exportUnitsAcl,
        ScheduledJobSettings(enabled = false, schedule = JobSchedule.daily(LocalTime.of(0, 0))),
    ),
    PlanBiExportJobs(
        { jobs, db, clock -> jobs.planBiJobs(db, clock, BiTable.entries) },
        ScheduledJobSettings(enabled = false, schedule = JobSchedule.daily(LocalTime.of(12, 0))),
    ),
}

class TampereScheduledJobs(
    private val exportUnitsAclService: ExportUnitsAclService,
    private val asyncJobRunner: AsyncJobRunner<TampereAsyncJob>,
    env: ScheduledJobsEnv<TampereScheduledJob>,
) : JobSchedule {
    private val logger = KotlinLogging.logger {}

    override val jobs: List<ScheduledJobDefinition> =
        env.jobs.map {
            ScheduledJobDefinition(it.key, it.value) { db, clock -> it.key.fn(this, db, clock) }
        }

    fun exportUnitsAcl(db: Database.Connection, clock: EvakaClock) {
        db.transaction { tx -> exportUnitsAclService.exportUnitsAcl(tx, clock.now()) }
    }

    fun planBiJobs(db: Database.Connection, clock: EvakaClock, selectedTables: List<BiTable>?) {
        val tables = selectedTables ?: BiTable.entries
        logger.info("Planning BI jobs for ${tables.size} tables")
        db.transaction { tx ->
            tx.removeUnclaimedJobs(setOf(AsyncJobType(TampereAsyncJob.SendBiTable::class)))
            asyncJobRunner.plan(
                tx,
                tables.asSequence().map(TampereAsyncJob::SendBiTable),
                runAt = clock.now(),
                retryCount = 1,
            )
        }
    }
}
