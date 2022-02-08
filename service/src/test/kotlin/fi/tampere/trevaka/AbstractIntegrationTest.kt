// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka

import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.dev.runDevScript
import fi.tampere.trevaka.database.resetTampereDatabaseForE2ETests
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import redis.clients.jedis.JedisPool

@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT,
    classes = [IntegrationTestConfiguration::class]
)
@AutoConfigureWireMock(port = 0)
abstract class AbstractIntegrationTest {

    @Autowired
    private lateinit var jdbi: Jdbi

    @Autowired
    private lateinit var redisPool: JedisPool

    @BeforeEach
    fun setup() {
        Database(jdbi).connect { dbc ->
            dbc.transaction { tx ->
                tx.runDevScript("reset-tampere-database-for-e2e-tests.sql")
                tx.resetTampereDatabaseForE2ETests()
            }
        }
        redisPool.resource.use { it.flushDB() }
    }

}
