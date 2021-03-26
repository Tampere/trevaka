package fi.tampere.trevaka.invoice.service

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.BasicCredentials
import com.github.tomakehurst.wiremock.client.WireMock.*
import fi.tampere.trevaka.InvoiceProperties
import fi.tampere.trevaka.IpaasProperties
import fi.tampere.trevaka.TrevakaProperties
import fi.tampere.trevaka.invoice.config.InvoiceConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.lanwen.wiremock.ext.WiremockResolver

@ExtendWith(WiremockResolver::class)
internal class TrevakaInvoiceClientIT {

    private lateinit var client: TrevakaInvoiceClient

    @BeforeEach
    fun setup(@WiremockResolver.Wiremock server: WireMockServer) {
        val properties = TrevakaProperties(
            IpaasProperties("http://localhost:${server.port()}", "user", "pass"),
            InvoiceProperties()
        )
        val configuration = InvoiceConfiguration()
        val webServiceTemplate = configuration.webServiceTemplate(configuration.httpClient(properties), properties)
        client = configuration.invoiceIntegrationClient(webServiceTemplate, properties) as TrevakaInvoiceClient
        configureFor("localhost", server.port())
    }

    @Test
    fun sendBatch() {
        stubFor(
            post(urlEqualTo("/salesOrder")).willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/soap+xml")
                    .withBodyFile("invoice-client/sales-order-response-ok.xml")
            )
        )

        assertThat(client.sendBatch(listOf(), 1)).isTrue()

        verify(
            postRequestedFor(urlEqualTo("/salesOrder"))
                .withBasicAuth(BasicCredentials("user", "pass"))
                .withHeader(
                    "Content-Type",
                    equalTo("application/soap+xml; charset=utf-8;   \taction=\"http://www.tampere.fi/services/sapsd/salesorder/v1.0/SendSalesOrder\"")
                )
                .withoutHeader("SOAPAction")
        )
    }

    @Test
    fun sendBatchWithApplicationFaultResponse() {
        stubFor(
            post(urlEqualTo("/salesOrder")).willReturn(
                aResponse()
                    .withStatus(400)
                    .withHeader("Content-Type", "application/soap+xml")
                    .withBodyFile("invoice-client/sales-order-response-application-fault.xml")
            )
        )

        assertThat(client.sendBatch(listOf(), 1)).isFalse()

        verify(
            postRequestedFor(urlEqualTo("/salesOrder"))
                .withBasicAuth(BasicCredentials("user", "pass"))
                .withHeader(
                    "Content-Type",
                    equalTo("application/soap+xml; charset=utf-8;   \taction=\"http://www.tampere.fi/services/sapsd/salesorder/v1.0/SendSalesOrder\"")
                )
                .withoutHeader("SOAPAction")
        )
    }

    @Test
    fun sendBatchWithSystemFaultResponse() {
        stubFor(
            post(urlEqualTo("/salesOrder")).willReturn(
                aResponse()
                    .withStatus(500)
                    .withHeader("Content-Type", "application/soap+xml")
                    .withBodyFile("invoice-client/sales-order-response-system-fault.xml")
            )
        )

        assertThat(client.sendBatch(listOf(), 1)).isFalse()

        verify(
            postRequestedFor(urlEqualTo("/salesOrder"))
                .withBasicAuth(BasicCredentials("user", "pass"))
                .withHeader(
                    "Content-Type",
                    equalTo("application/soap+xml; charset=utf-8;   \taction=\"http://www.tampere.fi/services/sapsd/salesorder/v1.0/SendSalesOrder\"")
                )
                .withoutHeader("SOAPAction")
        )
    }

}
