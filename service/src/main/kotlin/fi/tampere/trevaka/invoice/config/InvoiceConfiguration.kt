// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.invoice.config

import fi.espoo.evaka.invoicing.domain.FeeAlteration
import fi.espoo.evaka.invoicing.domain.IncomeType
import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient
import fi.espoo.evaka.invoicing.service.IncomeTypesProvider
import fi.espoo.evaka.invoicing.service.InvoiceGenerationLogic
import fi.espoo.evaka.invoicing.service.InvoiceGenerationLogicChooser
import fi.espoo.evaka.invoicing.service.InvoiceProductProvider
import fi.espoo.evaka.invoicing.service.ProductKey
import fi.espoo.evaka.invoicing.service.ProductWithName
import fi.espoo.evaka.placement.PlacementType
import fi.espoo.evaka.shared.ChildId
import fi.espoo.evaka.shared.db.Database
import fi.tampere.trevaka.SummertimeAbsenceProperties
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
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import org.springframework.ws.client.core.WebServiceTemplate
import org.springframework.ws.soap.SoapVersion
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory
import org.springframework.ws.transport.http.HttpComponentsMessageSender
import org.springframework.ws.transport.http.HttpComponentsMessageSender.RemoveSoapHeadersInterceptor
import java.time.Month

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
        properties: TrevakaProperties
    ): InvoiceIntegrationClient = TrevakaInvoiceClient(webServiceTemplate, properties.invoice)

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

    @Bean
    fun invoiceProductProvider(): InvoiceProductProvider = TampereInvoiceProductProvider()

    @Bean
    fun invoiceGenerationLogicChooser(properties: TrevakaProperties) : InvoiceGenerationLogicChooser = TampereInvoiceGeneratorLogicChooser(properties.summertimeAbsenceProperties)
}

class TampereIncomeTypesProvider : IncomeTypesProvider {
    override fun get(): Map<String, IncomeType> {
        return linkedMapOf(
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
}

class TampereInvoiceProductProvider : InvoiceProductProvider {

    override val products = Product.values().map { ProductWithName(it.key, it.nameFi) }
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
            PlacementType.DAYCARE_PART_TIME_FIVE_YEAR_OLDS ->
                Product.DAYCARE
            PlacementType.PRESCHOOL_DAYCARE ->
                Product.PRESCHOOL_WITH_DAYCARE
            PlacementType.PREPARATORY_DAYCARE ->
                Product.PRESCHOOL_WITH_DAYCARE
            PlacementType.TEMPORARY_DAYCARE,
            PlacementType.TEMPORARY_DAYCARE_PART_DAY ->
                Product.TEMPORARY_CARE
            PlacementType.SCHOOL_SHIFT_CARE ->
                Product.SCHOOL_SHIFT_CARE
            PlacementType.PRESCHOOL,
            PlacementType.PREPARATORY,
            PlacementType.CLUB ->
                error("No product mapping found for placement type $placementType")
        }
        return product.key
    }

    override fun mapToFeeAlterationProduct(productKey: ProductKey, feeAlterationType: FeeAlteration.Type): ProductKey {
        val product = when (findProduct(productKey) to feeAlterationType) {
            Product.DAYCARE to FeeAlteration.Type.DISCOUNT,
            Product.DAYCARE to FeeAlteration.Type.RELIEF ->
                Product.DAYCARE_DISCOUNT
            Product.PRESCHOOL_WITH_DAYCARE to FeeAlteration.Type.DISCOUNT,
            Product.PRESCHOOL_WITH_DAYCARE to FeeAlteration.Type.RELIEF ->
                Product.PRESCHOOL_WITH_DAYCARE_DISCOUNT
            Product.DAYCARE to FeeAlteration.Type.INCREASE,
            Product.PRESCHOOL_WITH_DAYCARE to FeeAlteration.Type.INCREASE ->
                Product.CORRECTION
            else ->
                error("No product mapping found for product + fee alteration type combo ($productKey + $feeAlterationType)")
        }
        return product.key
    }

}

fun findProduct(key: ProductKey) = Product.values().find { it.key == key }
    ?: error("Product with key $key not found")

enum class Product(val nameFi: String, val code: String) {
    DAYCARE("Varhaiskasvatus", "500218"),
    DAYCARE_DISCOUNT("Alennus - Varhaiskasvatus", "500687"),
    PRESCHOOL_WITH_DAYCARE("Esiopetusta täydentävä varhaiskasvatus", "500220"),
    PRESCHOOL_WITH_DAYCARE_DISCOUNT("Alennus - Esiopetusta täydentävä varhaiskasvatus", "509565"),
    TEMPORARY_CARE("Tilapäinen varhaiskasvatus", "500576"),
    SCHOOL_SHIFT_CARE("Koululaisen vuorohoito", "500949"),
    SICK_LEAVE_50("Laskuun vaikuttava poissaolo 50%", "500283"),
    SICK_LEAVE_100("Laskuun vaikuttava poissaolo 100%", "500248"),
    ABSENCE("Poissaolovähennys 50%", "500210"),
    FREE_OF_CHARGE("Hyvityspäivä", "503696"),
    CORRECTION("Oikaisu", "500177"),
    FREE_MONTH("Maksuton kuukausi", "500156"),
    OVER_CONTRACT("Sopimuksen ylitys", "500538"),
    UNANNOUNCED_ABSENCE("Ilmoittamaton päivystysajan poissaolo", "507292");

    val key = ProductKey(this.name)
}

class TampereInvoiceGeneratorLogicChooser(
    private val summertimeAbsenceProperties: SummertimeAbsenceProperties
) : InvoiceGenerationLogicChooser {

    override fun logicForMonth(tx: Database.Read, year: Int, month: Month, childId: ChildId): InvoiceGenerationLogic {
        return when {
            month == summertimeAbsenceProperties.freeMonth && tx.hasFreeSummerAbsence(childId, year) -> InvoiceGenerationLogic.Free
            else -> InvoiceGenerationLogic.Default
        }
    }

}

fun Database.Read.hasFreeSummerAbsence(childId: ChildId, year: Int): Boolean {
    // language=SQL
    val sql =
        """
        SELECT EXISTS(
            SELECT 1
            FROM holiday_period_questionnaire hpq
            WHERE
                hpq.absence_type = 'FREE_ABSENCE'
                AND date_part('year', upper(hpq.active) - interval '1 day') = :year
                AND EXISTS(
                    SELECT count(a.id), period
                    FROM
                        absence a
                        JOIN placement pl ON pl.child_id = a.child_id AND daterange(pl.start_date, pl.end_date, '[]') @> a.date
                        JOIN daycare dc ON dc.id = pl.unit_id,
                        unnest(hpq.period_options) period
                    WHERE
                        a.absence_type = 'FREE_ABSENCE'
                        AND a.child_id = :childId
                        AND period @> a.date
                        AND a.date NOT IN (SELECT h.date FROM holiday h)
                        AND date_part('isodow', a.date) = ANY(dc.operation_days)
                    GROUP BY period
                    HAVING count(a.id) >= (
                        SELECT count(*)
                        FROM
                            placement pl
                            JOIN daycare dc ON dc.id = pl.unit_id,
                            generate_series(lower(period), upper(period) - interval '1 day', interval '1 day') period_date
                        WHERE
                        pl.child_id = :childId
                        AND period_date NOT IN (SELECT h.date FROM holiday h)
                        AND daterange(pl.start_date, pl.end_date, '[]') @> period_date::date
                        AND date_part('isodow', period_date) = ANY(dc.operation_days)
                    )
                )
        )
        """.trimIndent()

    return createQuery(sql)
        .bind("year", year)
        .bind("childId", childId)
        .mapTo(Boolean::class.java)
        .one()
}
