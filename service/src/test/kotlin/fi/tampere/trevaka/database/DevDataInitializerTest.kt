// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.database

import fi.tampere.trevaka.AbstractIntegrationTest
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class DevDataInitializerTest : AbstractIntegrationTest(resetDbBeforeEach = true) {

    @Autowired
    private lateinit var jdbi: Jdbi

    @Test
    fun init() {
        DevDataInitializer(jdbi)
    }

}
