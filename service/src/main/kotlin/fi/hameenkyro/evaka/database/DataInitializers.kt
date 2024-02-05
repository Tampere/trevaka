// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.hameenkyro.evaka.database

import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.dev.runDevScript

fun Database.Transaction.ensureHameenkyroDevData() {
    if (createQuery("SELECT count(*) FROM daycare").mapTo<Int>().exactlyOne() == 0) {
        listOf("hameenkyro-dev-data.sql").forEach { runDevScript(it) }
    }
}
