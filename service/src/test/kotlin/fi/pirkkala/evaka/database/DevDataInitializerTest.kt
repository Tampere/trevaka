// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.pirkkala.evaka.database

import fi.pirkkala.evaka.AbstractPirkkalaIntegrationTest
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class DevDataInitializerTest : AbstractPirkkalaIntegrationTest() {

    @Autowired
    private lateinit var jdbi: Jdbi

    @Test
    fun init() {
        DevDataInitializer(jdbi)
    }
}
