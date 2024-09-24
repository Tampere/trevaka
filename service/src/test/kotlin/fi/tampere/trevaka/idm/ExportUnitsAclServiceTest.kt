// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.idm

import fi.espoo.evaka.shared.AreaId
import fi.espoo.evaka.shared.DaycareId
import fi.espoo.evaka.shared.auth.UserRole
import fi.espoo.evaka.shared.auth.insertDaycareAclRow
import fi.espoo.evaka.shared.dev.DevDaycare
import fi.espoo.evaka.shared.dev.DevEmployee
import fi.espoo.evaka.shared.dev.insert
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.tampere.trevaka.AbstractTampereIntegrationTest
import fi.tampere.trevaka.export.ExportUnitsAclService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class ExportUnitsAclServiceTest : AbstractTampereIntegrationTest() {

    @Autowired
    private lateinit var exportUnitsAclService: ExportUnitsAclService

    @Autowired
    private lateinit var s3Client: S3Client

    @Test
    fun exportUnitsAcl() {
        val timestamp = HelsinkiDateTime.of(LocalDate.of(2023, 12, 14), LocalTime.of(16, 33))
        db.transaction { tx ->
            val unitId = tx.insert(
                DevDaycare(
                    id = DaycareId(UUID.fromString("4e042a2e-f2d3-11ec-b2d6-1bf5942d79f4")),
                    name = "Sammon koulun esiopetus",
                    areaId = AreaId(UUID.fromString("6529f5a2-9777-11eb-ba89-cfcda122ed3b")),
                ),
            )
            val employeeId = tx.insert(
                DevEmployee(
                    firstName = "Leena",
                    lastName = "Testi",
                    email = "leena.testi@tampere.fi",
                    employeeNumber = "356751",
                ),
            )
            tx.insertDaycareAclRow(
                daycareId = unitId,
                employeeId = employeeId,
                role = UserRole.UNIT_SUPERVISOR,
            )
        }

        val (bucket, key) = db.transaction { tx -> exportUnitsAclService.exportUnitsAcl(tx, timestamp) }

        val request = GetObjectRequest.builder().bucket(bucket).key(key).build()
        val (data, contentType) = s3Client.getObject(request).use {
            it.readAllBytes().toString(StandardCharsets.UTF_8) to it.response().contentType()
        }
        assertEquals(EXPECTED, data)
        assertEquals("text/csv", contentType)
    }
}

private const val EXPECTED = """unit_id;unit_name;first_name;last_name;email;employee_number
4e042a2e-f2d3-11ec-b2d6-1bf5942d79f4;Sammon koulun esiopetus;Leena;Testi;leena.testi@tampere.fi;356751
"""
