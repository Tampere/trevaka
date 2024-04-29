// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.nokiankaupunki.evaka.database

import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.vtjclient.service.persondetails.MockPersonDetailsService
import fi.espoo.evaka.vtjclient.service.persondetails.legacyMockVtjDataset
import io.opentracing.noop.NoopTracerFactory
import org.jdbi.v3.core.Jdbi
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("local")
class DevDataInitializer(jdbi: Jdbi) {
    init {
        Database(jdbi, NoopTracerFactory.create()).connect { db ->
            db.transaction { tx ->
                tx.ensureNokiaDevData()
            }
        }
        MockPersonDetailsService.add(legacyMockVtjDataset())
    }
}
