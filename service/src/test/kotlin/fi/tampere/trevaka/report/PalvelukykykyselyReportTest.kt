// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.report

import fi.espoo.evaka.daycare.CareType
import fi.espoo.evaka.placement.PlacementType
import fi.espoo.evaka.shared.AreaId
import fi.espoo.evaka.shared.dev.DevDaycare
import fi.espoo.evaka.shared.dev.DevFridgeChild
import fi.espoo.evaka.shared.dev.DevFridgePartnership
import fi.espoo.evaka.shared.dev.DevPerson
import fi.espoo.evaka.shared.dev.DevPersonType
import fi.espoo.evaka.shared.dev.DevPlacement
import fi.espoo.evaka.shared.dev.insert
import fi.tampere.trevaka.AbstractTampereIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class PalvelukykykyselyReportTest : AbstractTampereIntegrationTest() {

    @Test
    fun `palvelukykykysely report returns correct values`() {
        val childUrl = "https://varhaiskasvatus.tampere.fi/employee/child-information/"
        val adultUrl = "https://varhaiskasvatus.tampere.fi/employee/profile/"

        val areaId = db.read { tx ->
            tx.createQuery { sql("select id from care_area order by name limit 1") }.exactlyOne<AreaId>()
        }
        val unit = DevDaycare(name = "Yksikkö", areaId = areaId, type = setOf(CareType.CENTRE, CareType.PRESCHOOL))
        val child1 = DevPerson(
            firstName = "Pauliina",
            lastName = "Päiväkotilainen",
        )
        val child2 = DevPerson(
            firstName = "Taneli",
            lastName = "Täydentävä",
        )

        val child3 = DevPerson(
            firstName = "Eemeli",
            lastName = "Esioppilas",
        )

        val parent1 = DevPerson(
            firstName = "Veikko",
            lastName = "Vanhempi",
            email = "veikon@säpo",
        )

        val parent2 = DevPerson(
            firstName = "Petunia",
            lastName = "Puoliso",
            email = "petunian@säpo",
        )

        db.transaction { tx ->
            tx.insert(unit)
            tx.insert(
                child1,
                DevPersonType.CHILD,
            )
            tx.insert(
                child2,
                DevPersonType.CHILD,
            )
            tx.insert(
                child3,
                DevPersonType.CHILD,
            )

            tx.insert(parent1, DevPersonType.ADULT)
            tx.insert(parent2, DevPersonType.ADULT)

            tx.insert(
                DevFridgeChild(
                    childId = child1.id,
                    headOfChild = parent1.id,
                    startDate = LocalDate.of(2025, 1, 1),
                    endDate = LocalDate.of(2025, 12, 1),
                ),
            )
            tx.insert(
                DevFridgeChild(
                    childId = child2.id,
                    headOfChild = parent1.id,
                    startDate = LocalDate.of(2025, 1, 1),
                    endDate = LocalDate.of(2025, 12, 1),
                ),
            )
            tx.insert(
                DevFridgeChild(
                    childId = child3.id,
                    headOfChild = parent1.id,
                    startDate = LocalDate.of(2025, 1, 1),
                    endDate = LocalDate.of(2025, 12, 1),
                ),
            )

            tx.insert(
                DevFridgePartnership(
                    first = parent1.id,
                    second = parent2.id,
                    startDate = LocalDate.of(2025, 1, 1),
                    endDate = LocalDate.of(2025, 12, 1),
                ),
            )

            tx.insert(
                DevPlacement(
                    childId = child1.id,
                    unitId = unit.id,
                    startDate = LocalDate.of(2025, 1, 1),
                    endDate = LocalDate.of(2025, 12, 1),
                    type = PlacementType.DAYCARE,
                ),
            )
            tx.insert(
                DevPlacement(
                    childId = child2.id,
                    unitId = unit.id,
                    startDate = LocalDate.of(2025, 1, 1),
                    endDate = LocalDate.of(2025, 12, 1),
                    type = PlacementType.PRESCHOOL_DAYCARE,
                ),
            )
            tx.insert(
                DevPlacement(
                    childId = child3.id,
                    unitId = unit.id,
                    startDate = LocalDate.of(2025, 1, 1),
                    endDate = LocalDate.of(2025, 12, 1),
                    type = PlacementType.PRESCHOOL,
                ),
            )
        }

        val rows = db.read { tx ->
            tx.createQuery { sql("SELECT * FROM palvelukykykysely") }.toList<PalvelukykykyselyRow>()
        }

        assertThat(rows)
            .containsExactlyInAnyOrder(
                PalvelukykykyselyRow(
                    childUrl = "$childUrl${child1.id}",
                    childFirstName = child1.firstName,
                    childLastName = child1.lastName,
                    placementUnit = unit.name,
                    placementType = PlacementType.DAYCARE,
                    placementStartDate = LocalDate.of(2025, 1, 1),
                    headUrl = "$adultUrl${parent1.id}",
                    headFirstName = parent1.firstName,
                    headLastName = parent1.lastName,
                    headEmail = parent1.email ?: "",
                    partnerUrl = "$adultUrl${parent2.id}",
                    partnerFirstName = parent2.firstName,
                    partnerLastName = parent2.lastName,
                    partnerEmail = parent2.email ?: "",
                ),
            )
    }
}

private data class PalvelukykykyselyRow(
    val childUrl: String,
    val childFirstName: String,
    val childLastName: String,
    val placementUnit: String,
    val placementType: PlacementType,
    val placementStartDate: LocalDate,
    val headUrl: String,
    val headLastName: String,
    val headFirstName: String,
    val headEmail: String,
    val partnerUrl: String,
    val partnerLastName: String,
    val partnerFirstName: String,
    val partnerEmail: String,
)
