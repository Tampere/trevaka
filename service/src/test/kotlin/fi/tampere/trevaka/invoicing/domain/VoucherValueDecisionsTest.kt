// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.invoicing.domain

import fi.espoo.evaka.invoicing.domain.ServiceNeedOptionVoucherValue
import fi.espoo.evaka.invoicing.domain.VoucherValue
import fi.espoo.evaka.shared.ServiceNeedOptionVoucherValueId
import fi.espoo.evaka.shared.db.Database
import fi.tampere.trevaka.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal

internal class VoucherValueDecisionsTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var jdbi: Jdbi

    @Test
    fun `voucher value settings are in sync`() {
        val options = Database(jdbi).connect { dbc -> dbc.read { tx -> tx.getVoucherValues() } }

        assertThat(options).isNotEmpty
        options.forEach {
            assertVoucherValue(it.id, VoucherValue(it.baseValue, it.coefficient, it.value))
            assertVoucherValue(it.id, VoucherValue(it.baseValueUnder3y, it.coefficientUnder3y, it.valueUnder3y))
        }
    }

    private fun assertVoucherValue(id: ServiceNeedOptionVoucherValueId, voucherValue: VoucherValue) {
        val value = (BigDecimal(voucherValue.baseValue) * voucherValue.coefficient).toInt()
        assertThat(value)
            .withFailMessage("Voucher $id computed value is ${voucherValue.baseValue} * ${voucherValue.coefficient} = $value but expected ${voucherValue.value}")
            .isEqualTo(voucherValue.value)
    }
}

private fun Database.Read.getVoucherValues(): List<ServiceNeedOptionVoucherValue> = createQuery(
    """
        SELECT
            id,
            service_need_option_id,
            validity,
            base_value,
            coefficient,
            value,
            base_value_under_3y,
            coefficient_under_3y,
            value_under_3y
        FROM service_need_option_voucher_value
    """.trimIndent()
).mapTo(ServiceNeedOptionVoucherValue::class.java).list()
