// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.invoice.config

import fi.espoo.evaka.invoicing.domain.FeeAlterationType
import fi.espoo.evaka.invoicing.domain.IncomeCoefficient
import fi.espoo.evaka.invoicing.domain.IncomeType
import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient
import fi.espoo.evaka.invoicing.service.DefaultInvoiceNumberProvider
import fi.espoo.evaka.invoicing.service.IncomeCoefficientMultiplierProvider
import fi.espoo.evaka.invoicing.service.IncomeTypesProvider
import fi.espoo.evaka.invoicing.service.InvoiceGenerationLogicChooser
import fi.espoo.evaka.invoicing.service.InvoiceNumberProvider
import fi.espoo.evaka.invoicing.service.InvoiceProductProvider
import fi.espoo.evaka.invoicing.service.ProductKey
import fi.espoo.evaka.invoicing.service.ProductWithName
import fi.espoo.evaka.placement.PlacementType
import fi.espoo.evaka.shared.ChildId
import fi.espoo.evaka.shared.db.Database
import fi.tampere.trevaka.SummertimeAbsenceProperties
import fi.tampere.trevaka.TampereProperties
import fi.tampere.trevaka.invoice.service.TampereInvoiceClient
import org.apache.hc.client5.http.classic.HttpClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import org.springframework.ws.client.core.WebServiceTemplate
import org.springframework.ws.soap.SoapVersion
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory
import org.springframework.ws.transport.http.HttpComponents5MessageSender
import trevaka.ipaas.newIpaasHttpClient
import java.math.BigDecimal
import java.time.YearMonth

const val WEB_SERVICE_TEMPLATE_INVOICE = "webServiceTemplateInvoice"
const val HTTP_CLIENT_INVOICE = "httpClientInvoice"

internal val SOAP_PACKAGES = arrayOf(
    "fi.tampere.messages.ipaas.commontypes.v1",
    "fi.tampere.messages.sapsd.salesorder.v11",
    "fi.tampere.services.sapsd.salesorder.v1",
)

@Configuration
class InvoiceConfiguration {
    @Primary
    @Bean
    fun invoiceIntegrationClient(
        @Qualifier(WEB_SERVICE_TEMPLATE_INVOICE) webServiceTemplate: WebServiceTemplate,
        properties: TampereProperties,
    ): InvoiceIntegrationClient = TampereInvoiceClient(webServiceTemplate, properties.invoice)

    @Bean(WEB_SERVICE_TEMPLATE_INVOICE)
    fun webServiceTemplate(
        @Qualifier(HTTP_CLIENT_INVOICE) httpClient: HttpClient,
        properties: TampereProperties,
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
            setMessageSender(HttpComponents5MessageSender(httpClient))
        }
    }

    @Bean(HTTP_CLIENT_INVOICE)
    fun httpClient(properties: TampereProperties) = newIpaasHttpClient(properties.ipaas)

    @Bean
    fun incomeTypesProvider(): IncomeTypesProvider = TampereIncomeTypesProvider()

    @Bean
    fun incomeCoefficientMultiplierProvider(): IncomeCoefficientMultiplierProvider = TampereIncomeCoefficientMultiplierProvider()

    @Bean
    fun invoiceProductProvider(): InvoiceProductProvider = TampereInvoiceProductProvider()

    @Bean
    fun invoiceGenerationLogicChooser(properties: TampereProperties): InvoiceGenerationLogicChooser = TampereInvoiceGeneratorLogicChooser(properties.summertimeAbsence)

    @Bean
    fun invoiceNumberProvider(): InvoiceNumberProvider = DefaultInvoiceNumberProvider(5000000000)
}

class TampereIncomeTypesProvider : IncomeTypesProvider {
    override fun get(): Map<String, IncomeType> = linkedMapOf(
        "MAIN_INCOME" to IncomeType("Palkkatulo", 1, false, false),
        "HOLIDAY_BONUS" to IncomeType("Lomaraha", 1, false, false),
        "PERKS" to IncomeType("Luontaisetu", 1, false, false),
        "DAILY_ALLOWANCE" to IncomeType("Päiväraha", 1, true, false),
        "HOME_CARE_ALLOWANCE" to IncomeType("Kotihoidontuki", 1, false, false),
        "PENSION" to IncomeType("Eläke", 1, false, false),
        "RELATIVE_CARE_SUPPORT" to IncomeType("Omaishoidontuki", 1, false, false),
        "STUDENT_INCOME" to IncomeType("Opiskelijan tulot", 1, false, false),
        "GRANT" to IncomeType("Apuraha", 1, false, false),
        "STARTUP_GRANT" to IncomeType("Starttiraha", 1, true, false),
        "BUSINESS_INCOME" to IncomeType("Yritystoiminnan tulo", 1, false, false),
        "CAPITAL_INCOME" to IncomeType("Pääomatulo", 1, false, false),
        "RENTAL_INCOME" to IncomeType("Vuokratulot", 1, false, false),
        "PAID_ALIMONY" to IncomeType("Maksetut elatusavut", -1, false, false),
        "ALIMONY" to IncomeType("Saadut elatusavut", 1, false, false),
        "OTHER_INCOME" to IncomeType("Muu tulo", 1, true, false),
        "ADJUSTED_DAILY_ALLOWANCE" to IncomeType("Soviteltu päiväraha", 1, true, false),
    )
}

class TampereIncomeCoefficientMultiplierProvider : IncomeCoefficientMultiplierProvider {
    override fun multiplier(coefficient: IncomeCoefficient): BigDecimal = when (coefficient) {
        IncomeCoefficient.MONTHLY_WITH_HOLIDAY_BONUS -> BigDecimal("1.0417")
        IncomeCoefficient.MONTHLY_NO_HOLIDAY_BONUS -> BigDecimal("1.0000")
        IncomeCoefficient.BI_WEEKLY_WITH_HOLIDAY_BONUS -> BigDecimal("2.2323")
        IncomeCoefficient.BI_WEEKLY_NO_HOLIDAY_BONUS -> BigDecimal("2.1429")
        IncomeCoefficient.DAILY_ALLOWANCE_21_5 -> BigDecimal("21.5")
        IncomeCoefficient.DAILY_ALLOWANCE_25 -> BigDecimal("25")
        IncomeCoefficient.YEARLY -> BigDecimal("0.0833")
    }
}

class TampereInvoiceProductProvider : InvoiceProductProvider {

    override val products = Product.entries.map { ProductWithName(it.key, it.nameFi) }
    override val dailyRefund = Product.FREE_OF_CHARGE.key
    override val partMonthSickLeave = Product.SICK_LEAVE_50.key
    override val fullMonthSickLeave = Product.SICK_LEAVE_100.key
    override val fullMonthAbsence = Product.ABSENCE.key
    override val contractSurplusDay = Product.OVER_CONTRACT.key

    override fun mapToProduct(placementType: PlacementType): ProductKey {
        val product = when (placementType) {
            PlacementType.DAYCARE,
            PlacementType.DAYCARE_PART_TIME,
            PlacementType.DAYCARE_FIVE_YEAR_OLDS,
            PlacementType.DAYCARE_PART_TIME_FIVE_YEAR_OLDS,
            ->
                Product.DAYCARE
            PlacementType.PRESCHOOL_DAYCARE,
            PlacementType.PRESCHOOL_DAYCARE_ONLY,
            ->
                Product.PRESCHOOL_WITH_DAYCARE
            PlacementType.PRESCHOOL_CLUB ->
                Product.PRESCHOOL_WITH_CLUB
            PlacementType.PREPARATORY_DAYCARE,
            PlacementType.PREPARATORY_DAYCARE_ONLY,
            ->
                Product.PRESCHOOL_WITH_DAYCARE
            PlacementType.TEMPORARY_DAYCARE,
            ->
                Product.TEMPORARY_CARE
            PlacementType.TEMPORARY_DAYCARE_PART_DAY,
            ->
                Product.SUMMER_CLUB
            PlacementType.SCHOOL_SHIFT_CARE ->
                Product.SCHOOL_SHIFT_CARE
            PlacementType.PRESCHOOL,
            PlacementType.PREPARATORY,
            PlacementType.CLUB,
            ->
                error("No product mapping found for placement type $placementType")
        }
        return product.key
    }

    override fun mapToFeeAlterationProduct(productKey: ProductKey, feeAlterationType: FeeAlterationType): ProductKey {
        val product = when (findProduct(productKey) to feeAlterationType) {
            Product.DAYCARE to FeeAlterationType.DISCOUNT,
            Product.DAYCARE to FeeAlterationType.RELIEF,
            ->
                Product.DAYCARE_DISCOUNT
            Product.PRESCHOOL_WITH_DAYCARE to FeeAlterationType.DISCOUNT,
            Product.PRESCHOOL_WITH_DAYCARE to FeeAlterationType.RELIEF,
            ->
                Product.PRESCHOOL_WITH_DAYCARE_DISCOUNT
            Product.PRESCHOOL_WITH_CLUB to FeeAlterationType.DISCOUNT,
            Product.PRESCHOOL_WITH_CLUB to FeeAlterationType.RELIEF,
            ->
                Product.PRESCHOOL_WITH_CLUB_DISCOUNT
            Product.DAYCARE to FeeAlterationType.INCREASE,
            Product.PRESCHOOL_WITH_DAYCARE to FeeAlterationType.INCREASE,
            Product.PRESCHOOL_WITH_CLUB to FeeAlterationType.INCREASE,
            ->
                Product.CORRECTION
            else ->
                error("No product mapping found for product + fee alteration type combo ($productKey + $feeAlterationType)")
        }
        return product.key
    }
}

fun findProduct(key: ProductKey) = Product.entries.find { it.key == key }
    ?: error("Product with key $key not found")

enum class Product(val nameFi: String, val code: String, val internalOrder: String? = null) {
    DAYCARE("Varhaiskasvatus", "500218"),
    DAYCARE_DISCOUNT("Alennus - Varhaiskasvatus", "500687"),
    PRESCHOOL_WITH_DAYCARE("Esiopetusta täydentävä varhaiskasvatus", "500220"),
    PRESCHOOL_WITH_DAYCARE_DISCOUNT("Alennus - Esiopetusta täydentävä varhaiskasvatus", "509565"),
    PRESCHOOL_WITH_CLUB("Esiopetuksen kerho", "503745"),
    PRESCHOOL_WITH_CLUB_DISCOUNT("Alennus - Esiopetuksen kerhotoiminta", "509787"),
    TEMPORARY_CARE("Tilapäinen varhaiskasvatus", "500576"),
    SUMMER_CLUB("Kesäkerho", "500061", "23461"),
    SCHOOL_SHIFT_CARE("Koululaisen vuorohoito", "500949"),
    SICK_LEAVE_50("Laskuun vaikuttava poissaolo 50%", "500283"),
    SICK_LEAVE_100("Laskuun vaikuttava poissaolo 100%", "500248"),
    ABSENCE("Poissaolovähennys 50%", "500210"),
    FREE_OF_CHARGE("Hyvityspäivä", "503696"),
    CORRECTION("Oikaisu", "500177"),
    FREE_MONTH("Maksuton kuukausi", "500156"),
    OVER_CONTRACT("Sopimuksen ylitys", "500538"),
    UNANNOUNCED_ABSENCE("Ilmoittamaton päivystysajan poissaolo", "507292"),
    ;

    val key = ProductKey(this.name)
}

class TampereInvoiceGeneratorLogicChooser(
    private val properties: SummertimeAbsenceProperties,
) : InvoiceGenerationLogicChooser {

    override fun getFreeChildren(tx: Database.Read, month: YearMonth, childIds: Set<ChildId>): Set<ChildId> = when {
        month.month == properties.freeMonth -> throw UnsupportedOperationException("Not implemented yet")
        else -> emptySet()
    }
}
