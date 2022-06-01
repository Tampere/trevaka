// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka

import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.dev.runDevScript
import fi.tampere.trevaka.database.resetTampereDatabaseForE2ETests
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import redis.clients.jedis.JedisPool

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT,
    classes = [IntegrationTestConfiguration::class]
)
@AutoConfigureWireMock(port = 0)
abstract class AbstractIntegrationTest(private val resetDbBeforeEach: Boolean = true) {

    @Autowired
    private lateinit var jdbi: Jdbi

    @Autowired
    private lateinit var redisPool: JedisPool

    protected lateinit var db: Database.Connection

    @BeforeAll
    protected fun initializeJdbi() {
        db = Database(jdbi).connectWithManualLifecycle()
        db.transaction {
            it.runDevScript("reset-tampere-database-for-e2e-tests.sql")
            if (!resetDbBeforeEach) {
                it.resetTampereDatabaseForE2ETests()
            }
        }
    }

    @BeforeEach
    fun setup() {
        if (resetDbBeforeEach) {
            db.transaction {
                it.resetTampereDatabaseForE2ETests()
            }
        }
        redisPool.resource.use { it.flushDB() }
    }

    @AfterAll
    protected fun afterAll() {
        db.close()
    }

}
