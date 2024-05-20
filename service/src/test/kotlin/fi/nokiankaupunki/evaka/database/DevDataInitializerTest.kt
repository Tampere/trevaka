// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.nokiankaupunki.evaka.database

import fi.nokiankaupunki.evaka.AbstractNokiaIntegrationTest
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class DevDataInitializerTest : AbstractNokiaIntegrationTest() {

    @Autowired
    private lateinit var jdbi: Jdbi

    @Test
    fun init() {
        DevDataInitializer(jdbi)
    }
}
