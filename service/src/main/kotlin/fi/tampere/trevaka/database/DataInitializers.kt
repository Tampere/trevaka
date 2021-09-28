package fi.tampere.trevaka.database

import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.dev.runDevScript

fun Database.Transaction.ensureTampereDevData() {
    if (createQuery("SELECT count(*) FROM daycare").mapTo(Int::class.java).first() == 0) {
        listOf("tampere-dev-data.sql").forEach { runDevScript(it) }
    }
}

fun Database.Transaction.resetTampereDatabaseForE2ETests() {
    execute("SELECT reset_tampere_database_for_e2e_tests()")
}
