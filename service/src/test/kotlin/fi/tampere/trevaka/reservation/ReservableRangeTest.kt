// SPDX-FileCopyrightText: 2021-2023 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.reservation

import fi.espoo.evaka.reservations.getReservableRange
import fi.espoo.evaka.shared.FeatureConfig
import fi.espoo.evaka.shared.domain.FiniteDateRange
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.tampere.trevaka.AbstractTampereIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.time.LocalTime

class ReservableRangeTest : AbstractTampereIntegrationTest() {

    @Autowired
    private lateinit var featureConfig: FeatureConfig

    @Test
    fun `next week is reservable just before Tuesday`() {
        val instant = HelsinkiDateTime.of(LocalDate.of(2023, 7, 24), LocalTime.MAX) // Mon 23:59
        val range = getReservableRange(instant, featureConfig.citizenReservationThresholdHours)
        assertThat(range).isEqualTo(FiniteDateRange(LocalDate.of(2023, 7, 31), LocalDate.of(2024, 8, 31)))
    }

    @Test
    fun `next week is not reservable on Tuesday`() {
        val instant = HelsinkiDateTime.of(LocalDate.of(2023, 7, 25), LocalTime.MIN) // Tue 00:00
        val range = getReservableRange(instant, featureConfig.citizenReservationThresholdHours)
        assertThat(range).isEqualTo(FiniteDateRange(LocalDate.of(2023, 8, 7), LocalDate.of(2024, 8, 31)))
    }
}
