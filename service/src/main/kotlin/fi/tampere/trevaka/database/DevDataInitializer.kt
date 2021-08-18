package fi.tampere.trevaka.database

import fi.espoo.evaka.shared.db.Database
import org.jdbi.v3.core.Jdbi
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("local")
class DevDataInitializer(jdbi: Jdbi) {
    init {
        Database(jdbi).transaction { tx ->
            tx.ensureTampereDevData()
        }
    }
}
