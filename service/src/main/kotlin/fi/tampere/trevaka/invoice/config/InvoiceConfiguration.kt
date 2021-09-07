// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.invoice.config

import fi.espoo.evaka.invoicing.domain.IncomeType
import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient
import fi.espoo.evaka.invoicing.service.IncomeTypesProvider
import fi.tampere.trevaka.TrevakaProperties
import fi.tampere.trevaka.invoice.service.TrevakaInvoiceClient
import fi.tampere.trevaka.util.basicAuthInterceptor
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import org.springframework.ws.client.core.WebServiceTemplate
import org.springframework.ws.soap.SoapVersion
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory
import org.springframework.ws.transport.http.HttpComponentsMessageSender
import org.springframework.ws.transport.http.HttpComponentsMessageSender.RemoveSoapHeadersInterceptor

const val WEB_SERVICE_TEMPLATE_INVOICE = "webServiceTemplateInvoice"
const val HTTP_CLIENT_INVOICE = "httpClientInvoice"

internal val SOAP_PACKAGES = arrayOf(
    "fi.tampere.messages.ipaas.commontypes.v1",
    "fi.tampere.messages.sapsd.salesorder.v11",
    "fi.tampere.services.sapsd.salesorder.v1",
)

@Profile("trevaka")
@Configuration
class InvoiceConfiguration {
    @Primary
    @Bean(name = ["trevakaInvoiceIntegrationClient"])
    fun invoiceIntegrationClient(
        @Qualifier(WEB_SERVICE_TEMPLATE_INVOICE) webServiceTemplate: WebServiceTemplate,
        properties: TrevakaProperties,
        environment: Environment,
    ): InvoiceIntegrationClient = TrevakaInvoiceClient(webServiceTemplate, properties.invoice, environment)

    @Bean(WEB_SERVICE_TEMPLATE_INVOICE)
    fun webServiceTemplate(
        @Qualifier(HTTP_CLIENT_INVOICE) httpClient: HttpClient,
        properties: TrevakaProperties
    ): WebServiceTemplate {
        val messageFactory = SaajSoapMessageFactory().apply {
            setSoapVersion(SoapVersion.SOAP_12)
            afterPropertiesSet()
        }
        val marshaller = Jaxb2Marshaller().apply {
            setPackagesToScan(*SOAP_PACKAGES)
            afterPropertiesSet()
        }
        return WebServiceTemplate(messageFactory).apply {
            this.marshaller = marshaller
            unmarshaller = marshaller
            setMessageSender(HttpComponentsMessageSender(httpClient))
        }
    }

    @Bean(HTTP_CLIENT_INVOICE)
    fun httpClient(properties: TrevakaProperties) = HttpClientBuilder.create()
        .addInterceptorFirst(RemoveSoapHeadersInterceptor())
        .addInterceptorFirst(basicAuthInterceptor(properties.ipaas.username, properties.ipaas.password))
        .build()

    @Bean
    fun incomeTypesProvider(): IncomeTypesProvider = TampereIncomeTypesProvider()

}

class TampereIncomeTypesProvider : IncomeTypesProvider {
    override fun get(): Map<String, IncomeType> {
        return linkedMapOf(
            "PENSION" to IncomeType("Eläke", 1, false, false),
            "MAIN_INCOME" to IncomeType("Palkkatulo", 1, true, false),
            "HOLIDAY_BONUS" to IncomeType("Lomaraha", 1, false, false),
            "ALIMONY" to IncomeType("Saadut elatusavut", 1, false, false),
            "PAID_ALIMONY" to IncomeType("Maksetut elatusavut", -1, false, false),
            "OTHER_INCOME" to IncomeType("Muu tulo", 1, true, false),
            "DAILY_ALLOWANCE" to IncomeType("Päiväraha", 1, false, false),
            "BUSINESS_INCOME" to IncomeType("Yritystoiminnan tulo", 1, false, false),
            "ADJUSTED_DAILY_ALLOWANCE" to IncomeType("Soviteltu päiväraha", 1, false, false),
            "PERKS" to IncomeType("Luontaisetu", 1, false, false),
            "HOME_CARE_ALLOWANCE" to IncomeType("Kotihoidontuki", 1, false, false),
            "RELATIVE_CARE_SUPPORT" to IncomeType("Omaishoidontuki", 1, false, false),
            "STUDENT_INCOME" to IncomeType("Opiskelijan tulot", 1, false, false),
            "GRANT" to IncomeType("Apuraha", 1, false, false),
            "STARTUP_GRANT" to IncomeType("Starttiraha", 1, false, false),
            "CAPITAL_INCOME" to IncomeType("Pääomatulo", 1, false, false),
            "RENTAL_INCOME" to IncomeType("Vuokratulot", 1, false, false)
        )
    }
}
