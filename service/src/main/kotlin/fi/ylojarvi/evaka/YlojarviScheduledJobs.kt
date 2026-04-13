// SPDX-FileCopyrightText: 2026 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.ylojarvi.evaka

import fi.espoo.evaka.OphEnv
import fi.espoo.evaka.ScheduledJobsEnv
import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.domain.EvakaClock
import fi.espoo.evaka.shared.job.JobSchedule
import fi.espoo.evaka.shared.job.ScheduledJobDefinition
import fi.espoo.evaka.shared.job.ScheduledJobSettings
import trevaka.export.ChildDocumentTransferType
import trevaka.export.exportChildDocumentsViaSftp

enum class YlojarviScheduledJob(
    val fn: (YlojarviScheduledJobs, Database.Connection, EvakaClock) -> Unit,
    val defaultSettings: ScheduledJobSettings,
) {
    ExportPreschoolToPrimaryChildDocuments(
        YlojarviScheduledJobs::exportPreschoolToPrimaryChildDocuments,
        ScheduledJobSettings(enabled = false, schedule = JobSchedule.cron("0 0 0 1 8 ?")),
    ),
}

class YlojarviScheduledJobs(
    private val properties: YlojarviProperties,
    private val ophEnv: OphEnv,
    env: ScheduledJobsEnv<YlojarviScheduledJob>,
) : JobSchedule {

    override val jobs: List<ScheduledJobDefinition> =
        env.jobs.map {
            ScheduledJobDefinition(it.key, it.value) { db, clock -> it.key.fn(this, db, clock) }
        }

    fun exportPreschoolToPrimaryChildDocuments(db: Database.Connection, clock: EvakaClock) {
        val primus = properties.primus ?: error("Primus not configured")
        exportChildDocumentsViaSftp(db, clock, ophEnv.municipalityCode, primus, ChildDocumentTransferType.PRESCHOOL_TO_PRIMARY)
    }
}
