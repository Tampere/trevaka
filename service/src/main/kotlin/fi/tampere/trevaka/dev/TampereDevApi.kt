// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.dev

import fi.espoo.evaka.ExcludeCodeGen
import fi.espoo.evaka.shared.async.AsyncJob
import fi.espoo.evaka.shared.async.AsyncJobRunner
import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.domain.EvakaClock
import fi.espoo.evaka.shared.domain.RealEvakaClock
import fi.tampere.trevaka.database.resetTampereDatabaseForE2ETests
import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@Profile("enable_dev_api")
@RestController
@RequestMapping("/dev-api/tampere")
@ExcludeCodeGen
class TampereDevApi(
    private val asyncJobRunner: AsyncJobRunner<AsyncJob>
) {
    @GetMapping
    fun healthCheck(): ResponseEntity<Unit> {
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/reset-tampere-db-for-e2e-tests")
    fun resetTampereDatabaseForE2ETests(db: Database, clock: EvakaClock = RealEvakaClock()): ResponseEntity<Unit> {
        // Run async jobs before database reset to avoid database locks/deadlocks
        asyncJobRunner.runPendingJobsSync(clock)
        asyncJobRunner.waitUntilNoRunningJobs(timeout = Duration.ofSeconds(20))

        db.connect { c -> c.transaction { tx -> tx.resetTampereDatabaseForE2ETests() } }
        return ResponseEntity.noContent().build()
    }
}
