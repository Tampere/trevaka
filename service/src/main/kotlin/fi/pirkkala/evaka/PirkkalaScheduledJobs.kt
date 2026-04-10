// SPDX-FileCopyrightText: 2026 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.pirkkala.evaka

import fi.espoo.evaka.OphEnv
import fi.espoo.evaka.ScheduledJobsEnv
import fi.espoo.evaka.shared.async.AsyncJob
import fi.espoo.evaka.shared.async.AsyncJobRunner
import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.domain.EvakaClock
import fi.espoo.evaka.shared.job.JobSchedule
import fi.espoo.evaka.shared.job.ScheduledJobDefinition
import fi.espoo.evaka.shared.job.ScheduledJobSettings
import trevaka.archival.planDocumentArchival
import trevaka.export.exportPreschoolChildDocumentsViaSftp
import java.time.LocalTime

enum class PirkkalaScheduledJob(
    val fn: (PirkkalaScheduledJobs, Database.Connection, EvakaClock) -> Unit,
    val defaultSettings: ScheduledJobSettings,
) {
    ExportPreschoolChildDocuments(
        PirkkalaScheduledJobs::exportPreschoolChildDocuments,
        ScheduledJobSettings(enabled = false, schedule = JobSchedule.cron("0 0 0 1 8 ?")),
    ),
    PlanDocumentArchival(
        PirkkalaScheduledJobs::archiveEligibleDocuments,
        ScheduledJobSettings(enabled = false, schedule = JobSchedule.daily(LocalTime.of(20, 0))),
    ),
}

class PirkkalaScheduledJobs(
    private val coreAsyncJobRunner: AsyncJobRunner<AsyncJob>,
    private val properties: PirkkalaProperties,
    private val ophEnv: OphEnv,
    env: ScheduledJobsEnv<PirkkalaScheduledJob>,
) : JobSchedule {

    override val jobs: List<ScheduledJobDefinition> =
        env.jobs.map {
            ScheduledJobDefinition(it.key, it.value) { db, clock -> it.key.fn(this, db, clock) }
        }

    fun exportPreschoolChildDocuments(db: Database.Connection, clock: EvakaClock) {
        val primus = properties.primus ?: error("Primus not configured")
        exportPreschoolChildDocumentsViaSftp(db, clock, ophEnv.municipalityCode, primus)
    }

    fun archiveEligibleDocuments(db: Database.Connection, clock: EvakaClock) {
        planDocumentArchival(
            db,
            clock,
            coreAsyncJobRunner,
            properties.archival?.schedule ?: error("No archival configuration available"),
        )
    }
}
