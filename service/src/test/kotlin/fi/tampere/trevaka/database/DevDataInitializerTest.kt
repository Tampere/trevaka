package fi.tampere.trevaka.database

import fi.espoo.evaka.shared.db.Database
import fi.tampere.trevaka.AbstractIntegrationTest
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class DevDataInitializerTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var jdbi: Jdbi

    @AfterEach
    fun cleanup() {
        Database(jdbi).connect { db -> db.transaction { tx -> tx.resetTampereDatabaseForE2ETests() } }
    }

    @Test
    fun init() {
        DevDataInitializer(jdbi)
    }

}
