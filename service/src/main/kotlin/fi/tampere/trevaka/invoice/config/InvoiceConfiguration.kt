package fi.tampere.trevaka.invoice.config

import com.fasterxml.jackson.databind.ObjectMapper
import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient
import fi.tampere.trevaka.invoice.service.TrevakaInvoiceClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment

@Profile("trevaka")
@Configuration
class InvoiceConfiguration {
    @Primary
    @Bean(name = ["trevakaInvoiceIntegrationClient"])
    fun invoiceIntegrationClient(env: Environment, objectMapper: ObjectMapper): InvoiceIntegrationClient =
        TrevakaInvoiceClient(objectMapper)
}