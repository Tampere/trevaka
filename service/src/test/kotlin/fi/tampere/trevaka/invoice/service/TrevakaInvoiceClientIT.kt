// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.invoice.service

import com.github.tomakehurst.wiremock.client.BasicCredentials
import com.github.tomakehurst.wiremock.client.WireMock.*
import fi.tampere.trevaka.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ws.soap.client.SoapFaultClientException

internal class TrevakaInvoiceClientIT : AbstractIntegrationTest() {

    @Autowired
    private lateinit var client: TrevakaInvoiceClient

    @Test
    fun send() {
        val invoice1 = validInvoice()
        stubFor(
            post(urlEqualTo("/mock/ipaas/salesOrder")).willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/soap+xml")
                    .withBodyFile("invoice-client/sales-order-response-ok.xml")
            )
        )

        assertThat(client.send(listOf(invoice1)))
            .returns(listOf(invoice1)) { it.succeeded }
            .returns(listOf()) { it.failed }

        verify(
            postRequestedFor(urlEqualTo("/mock/ipaas/salesOrder"))
                .withBasicAuth(BasicCredentials("user", "pass"))
                .withHeader(
                    "Content-Type",
                    equalTo("application/soap+xml; charset=utf-8;   \taction=\"http://www.tampere.fi/services/sapsd/salesorder/v1.0/SendSalesOrder\"")
                )
                .withoutHeader("SOAPAction")
        )
    }

    @Test
    fun sendWithApplicationFaultResponse() {
        val invoice1 = validInvoice()
        stubFor(
            post(urlEqualTo("/mock/ipaas/salesOrder")).willReturn(
                aResponse()
                    .withStatus(400)
                    .withHeader("Content-Type", "application/soap+xml")
                    .withBodyFile("invoice-client/sales-order-response-application-fault.xml")
            )
        )

        val thrown = catchThrowable { client.send(listOf(invoice1)) }

        assertThat(thrown).isInstanceOf(SoapFaultClientException::class.java)
        verify(
            postRequestedFor(urlEqualTo("/mock/ipaas/salesOrder"))
                .withBasicAuth(BasicCredentials("user", "pass"))
                .withHeader(
                    "Content-Type",
                    equalTo("application/soap+xml; charset=utf-8;   \taction=\"http://www.tampere.fi/services/sapsd/salesorder/v1.0/SendSalesOrder\"")
                )
                .withoutHeader("SOAPAction")
        )
    }

    @Test
    fun sendWithSystemFaultResponse() {
        val invoice1 = validInvoice()
        stubFor(
            post(urlEqualTo("/mock/ipaas/salesOrder")).willReturn(
                aResponse()
                    .withStatus(500)
                    .withHeader("Content-Type", "application/soap+xml")
                    .withBodyFile("invoice-client/sales-order-response-system-fault.xml")
            )
        )

        val thrown = catchThrowable { client.send(listOf(invoice1)) }

        assertThat(thrown).isInstanceOf(SoapFaultClientException::class.java)
        verify(
            postRequestedFor(urlEqualTo("/mock/ipaas/salesOrder"))
                .withBasicAuth(BasicCredentials("user", "pass"))
                .withHeader(
                    "Content-Type",
                    equalTo("application/soap+xml; charset=utf-8;   \taction=\"http://www.tampere.fi/services/sapsd/salesorder/v1.0/SendSalesOrder\"")
                )
                .withoutHeader("SOAPAction")
        )
    }

}
