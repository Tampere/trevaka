// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.payment

import com.github.tomakehurst.wiremock.client.BasicCredentials
import com.github.tomakehurst.wiremock.client.WireMock.*
import fi.tampere.trevaka.AbstractTampereIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class TamperePaymentClientIT : AbstractTampereIntegrationTest() {

    @Autowired
    private lateinit var client: TamperePaymentClient

    @Test
    fun send() {
        val payment1 = testPayment
        stubFor(
            post(urlEqualTo("/mock/ipaas/payableAccounting")).willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/soap+xml")
                    .withBodyFile("payment-client/payable-accounting.xml"),
            ),
        )

        assertThat(db.read { tx -> client.send(listOf(payment1), tx) })
            .returns(listOf(payment1)) { it.succeeded }
            .returns(listOf()) { it.failed }

        verify(
            postRequestedFor(urlEqualTo("/mock/ipaas/payableAccounting"))
                .withBasicAuth(BasicCredentials("user", "pass"))
                .withHeader(
                    "Content-Type",
                    equalTo("application/soap+xml; charset=utf-8; action=\"http://www.tampere.fi/services/sapfico/payableaccounting/v1.0/SendPayableAccounting\""),
                )
                .withoutHeader("SOAPAction"),
        )
    }
}
