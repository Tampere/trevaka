// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.vesilahti.evaka.invoice.config

import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient
import fi.espoo.evaka.invoicing.service.IncomeCoefficientMultiplierProvider
import fi.espoo.evaka.invoicing.service.IncomeTypesProvider
import fi.espoo.evaka.invoicing.service.InvoiceProductProvider
import fi.espoo.evaka.invoicing.service.ProductKey
import fi.vesilahti.evaka.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import software.amazon.awssdk.services.s3.S3Client

@Configuration
class InvoiceConfiguration {
    @Primary
    @Bean(name = ["vesilahtiInvoiceIntegrationClient"])
    fun invoiceIntegrationClient(
        properties: VesilahtiProperties,
        invoiceGenerator: ProEInvoiceGenerator,
        s3Client: S3Client,
    ): InvoiceIntegrationClient {
        val s3Sender = S3Sender(s3Client, properties)
        return VesilahtiInvoiceIntegrationClient(s3Sender, invoiceGenerator)
    }

    @Bean
    fun incomeTypesProvider(): IncomeTypesProvider = VesilahtiIncomeTypesProvider()

    @Bean
    fun incomeCoefficientMultiplierProvider(): IncomeCoefficientMultiplierProvider = VesilahtiIncomeCoefficientMultiplierProvider()

    @Bean
    fun invoiceProductProvider(): InvoiceProductProvider = VesilahtiInvoiceProductProvider()
}

enum class Product(val nameFi: String, val code: String) {
    DAYCARE("Varhaiskasvatus", ""),
    DAYCARE_DISCOUNT("Alennus", ""),
    PRESCHOOL_WITH_DAYCARE("Esiopetusta täydentävä varhaiskasvatus", ""),
    TEMPORARY_CARE("Tilapäinen varhaiskasvatus", ""),
    SICK_LEAVE_50("Sairaspoissaolovähennys 50 %", ""),
    SICK_LEAVE_100("Sairaspoissaolovähennys 100 %", ""),
    ABSENCE("Poissaolovähennys 50%", ""),
    FREE_OF_CHARGE("Maksuton päivä", ""),
    CORRECTION("Oikaisu", ""),
    FREE_MONTH("Poissaolovähennys 100 %", ""),
    OVER_CONTRACT("Sovittujen päivien ylitys", ""),
    PRESCHOOL_DAYCARE_CORRECTION("Kokoaikainen varhaiskasvatus", ""),
    ;

    val key = ProductKey(this.name)
}
