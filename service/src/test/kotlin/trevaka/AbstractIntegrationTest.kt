// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka

import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.dev.runDevScript
import fi.espoo.evaka.shared.noopTracer
import fi.espoo.evaka.vtjclient.service.persondetails.MockPersonDetailsService
import fi.tampere.trevaka.database.resetTampereDatabaseForE2ETests
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import java.io.FileOutputStream
import java.nio.file.Paths

private val reportsPath: String = "${Paths.get("build").toAbsolutePath()}/reports"

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
    classes = [TrevakaMain::class, IntegrationTestConfiguration::class],
)
@AutoConfigureWireMock(port = 0)
abstract class AbstractIntegrationTest {

    @Autowired
    private lateinit var jdbi: Jdbi

    protected lateinit var db: Database.Connection

    @BeforeAll
    protected fun initializeJdbi() {
        db = Database(jdbi, noopTracer()).connectWithManualLifecycle()
        db.transaction { tx -> tx.runDevScript("reset-tampere-database-for-e2e-tests.sql") }
    }

    @BeforeEach
    fun setup() {
        db.transaction { tx -> tx.resetTampereDatabaseForE2ETests() }
        MockPersonDetailsService.reset()
    }

    @AfterAll
    protected fun afterAll() {
        db.close()
    }

    protected fun writeReportsFile(filename: String, bytes: ByteArray) {
        val filepath = "$reportsPath/$filename"
        FileOutputStream(filepath).use { it.write(bytes) }
    }
}
