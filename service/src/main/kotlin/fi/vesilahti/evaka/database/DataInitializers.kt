// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.vesilahti.evaka.database

import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.dev.runDevScript

fun Database.Transaction.ensureVesilahtiDevData() {
    if (createQuery("SELECT count(*) FROM daycare").mapTo<Int>().exactlyOne() == 0) {
        listOf("vesilahti-dev-data.sql").forEach { runDevScript(it) }
    }
}
