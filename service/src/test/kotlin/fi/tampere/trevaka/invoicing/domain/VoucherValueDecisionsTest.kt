// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.invoicing.domain

import fi.espoo.evaka.invoicing.domain.VoucherValue
import fi.espoo.evaka.invoicing.domain.calculateVoucherValue
import fi.espoo.evaka.serviceneed.ServiceNeedOption
import fi.espoo.evaka.serviceneed.getServiceNeedOptions
import fi.espoo.evaka.shared.db.Database
import fi.tampere.trevaka.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.LocalDate

internal class VoucherValueDecisionsTest : AbstractIntegrationTest(resetDbBeforeEach = true) {

    @Autowired
    private lateinit var jdbi: Jdbi

    @Test
    fun calculateVoucherValuesAt20220413() {
        Database(jdbi).connect { dbc ->
            dbc.transaction { tx ->
                val date = LocalDate.of(2022, 4, 13)
                val voucherValue = tx.getVoucherValue(date)
                val serviceNeedOptions = tx.getServiceNeedOptions()
                assertVoucherValues(
                    voucherValue.baseValue,
                    serviceNeedOptions,
                    expectedValuesAt20220413.entries.associate { it.key to it.value.first }
                )
            }
        }
    }

    @Test
    fun calculateVoucherValuesUnderThreeYearOldsAt20220413() {
        Database(jdbi).connect { dbc ->
            dbc.transaction { tx ->
                val date = LocalDate.of(2022, 4, 13)
                val voucherValue = tx.getVoucherValue(date)
                val serviceNeedOptions = tx.getServiceNeedOptions()
                assertVoucherValues(
                    voucherValue.baseValueAgeUnderThree,
                    serviceNeedOptions,
                    expectedValuesAt20220413.entries.associate { it.key to it.value.second }
                )
            }
        }
    }

    internal fun assertVoucherValues(
        baseValue: Int,
        serviceNeedOptions: List<ServiceNeedOption>,
        expectedValues: Map<String, Int>
    ) {
        val values = serviceNeedOptions.associate { option ->
            option.nameFi to calculateVoucherValue(
                baseValue,
                BigDecimal("1.00"),
                option.voucherValueCoefficient
            )
        }
        assertThat(values).isEqualTo(expectedValues)
    }

}

private val expectedValuesAt20220413 = mapOf(
    // "service need option name fi" to Pair(expected value for 3-year-old, expected value for under 3-year-old)
    "Kokopäiväinen" to Pair(82600, 124700),
    "Kokopäiväinen, 10 pv sopimus" to Pair(49560, 74820),
    "Kokopäiväinen, 15 pv sopimus" to Pair(66080, 99760),
    "Osapäiväinen, max 5h päivässä" to Pair(49560, 74820),
    "Osapäiväinen, max 5h päivässä; 10 pv sopimus" to Pair(29736, 44892),
    "Osapäiväinen, max 5h päivässä; 15 pv sopimus" to Pair(39648, 59856),
    "Osapäiväinen, max 20h viikossa" to Pair(49560, 74820),
    "Tilapäinen varhaiskasvatus" to Pair(82600, 124700),
    "Esiopetusta täydentävä varhaiskasvatus - max 5 h päivässä" to Pair(41300, 62350),
    "Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä" to Pair(41300, 62350),
    "Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 10 pv sopimus" to Pair(24780, 37410),
    "Esiopetusta täydentävä varhaiskasvatus – max 5 h päivässä; 15 pv sopimus" to Pair(33040, 49880),
    "Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 10 pv sopimus" to Pair(24780, 37410),
    "Esiopetusta täydentävä varhaiskasvatus – yli 5 h päivässä; 15 pv sopimus" to Pair(33040, 49880),
    "Kerho" to Pair(0, 0),
    "Koululaisen vuorohoito" to Pair(82600, 124700),
)

private fun Database.Read.getVoucherValue(date: LocalDate): VoucherValue {
    return createQuery("SELECT * FROM voucher_value WHERE validity @> :date")
        .bind("date", date).mapTo(VoucherValue::class.java).first()
}
