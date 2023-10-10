// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.database

import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.dev.runDevScript

fun Database.Transaction.ensureTampereDevData() {
    if (createQuery("SELECT count(*) FROM daycare").mapTo<Int>().exactlyOne() == 0) {
        listOf("tampere-dev-data.sql").forEach { runDevScript(it) }
    }
}

fun Database.Transaction.resetTampereDatabaseForE2ETests() {
    execute("SELECT reset_tampere_database_for_e2e_tests()")
}
