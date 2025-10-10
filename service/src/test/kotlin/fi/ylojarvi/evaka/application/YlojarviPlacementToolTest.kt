// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.ylojarvi.evaka.application

import fi.espoo.evaka.EvakaEnv
import fi.ylojarvi.evaka.AbstractYlojarviIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import trevaka.assertPlacementToolServiceNeedOptionIdExists

class YlojarviPlacementToolTest : AbstractYlojarviIntegrationTest() {
    @Autowired
    private lateinit var evakaEnv: EvakaEnv

    @Test
    fun `placement tool service need option id exists`() {
        assertPlacementToolServiceNeedOptionIdExists(db, evakaEnv, "Täydentävä varhaiskasvatus 51–85 tuntia / kuukausi")
    }
}
