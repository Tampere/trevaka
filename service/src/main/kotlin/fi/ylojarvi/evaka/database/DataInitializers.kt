// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.ylojarvi.evaka.database

import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.dev.runDevScript

fun Database.Transaction.ensureYlojarviDevData() {
    if (createQuery { sql("SELECT count(*) FROM daycare") }.mapTo<Int>().exactlyOne() == 0) {
        listOf("ylojarvi-dev-data.sql").forEach { runDevScript(it) }
    }
}
