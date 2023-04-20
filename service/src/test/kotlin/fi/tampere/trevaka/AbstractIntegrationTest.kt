// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka

import com.github.kittinunf.fuel.core.FuelManager
import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.dev.runDevScript
import fi.tampere.trevaka.database.resetTampereDatabaseForE2ETests
import io.opentracing.noop.NoopTracerFactory
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.core.io.Resource
import org.springframework.util.StreamUtils
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Paths
import java.util.function.Function

val reportsPath: String = "${Paths.get("build").toAbsolutePath()}/reports"

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT,
    classes = [IntegrationTestConfiguration::class],
)
@AutoConfigureWireMock(port = 0)
abstract class AbstractIntegrationTest(private val resetDbBeforeEach: Boolean = true) {

    @LocalServerPort
    var httpPort: Int = 0

    @Autowired
    private lateinit var jdbi: Jdbi

    @Autowired
    protected lateinit var http: FuelManager

    protected lateinit var db: Database.Connection

    @BeforeAll
    protected fun initializeJdbi() {
        db = Database(jdbi, NoopTracerFactory.create()).connectWithManualLifecycle()
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
        http.basePath = "http://localhost:$httpPort/"
    }

    @AfterAll
    protected fun afterAll() {
        db.close()
    }

    protected fun <T> runInTransaction(function: Function<Database.Transaction, T>) =
        db.transaction { tx -> function.apply(tx) }
}

fun resourceAsString(resource: Resource, charset: Charset = StandardCharsets.UTF_8) =
    resource.inputStream.use { StreamUtils.copyToString(it, charset) }
