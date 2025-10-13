// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka

import fi.espoo.evaka.EvakaEnv
import fi.espoo.evaka.placement.PlacementType
import fi.espoo.evaka.serviceneed.findServiceNeedOptionById
import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.domain.DateRange
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import java.time.LocalDate
import java.time.Month

fun assertPlacementToolServiceNeedOptionIdExists(db: Database.Connection, evakaEnv: EvakaEnv, nameFi: String) {
    val serviceNeedOptionId = evakaEnv.placementToolServiceNeedOptionId
        ?: error("Service need option id is not configured")
    val serviceNeedOption = db.read { tx -> tx.findServiceNeedOptionById(serviceNeedOptionId) }
        ?: error("Service need option id $serviceNeedOptionId not found")

    assertThat(serviceNeedOption)
        .returns(PlacementType.PRESCHOOL_DAYCARE) { it.validPlacementType }
        .returns(false) { it.defaultOption }
        .returns(nameFi) { it.nameFi }

    val nextFirstOfAugust = LocalDate.now().withDayOfMonth(1)
        .let { (if (it.month >= Month.AUGUST) it.plusYears(1) else it).withMonth(8) }
    assertTrue(DateRange(serviceNeedOption.validFrom, serviceNeedOption.validTo).includes(nextFirstOfAugust))
}
