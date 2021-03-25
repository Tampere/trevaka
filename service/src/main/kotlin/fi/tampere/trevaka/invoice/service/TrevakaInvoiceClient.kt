package fi.tampere.trevaka.invoice.service

import fi.espoo.evaka.invoicing.domain.InvoiceDetailed
import fi.espoo.evaka.invoicing.domain.InvoiceRowDetailed
import fi.espoo.evaka.invoicing.domain.Product
import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient
import fi.tampere.messages.ipaas.commontypes.v1.FaultType
import fi.tampere.messages.ipaas.commontypes.v1.SimpleAcknowledgementResponseType
import fi.tampere.messages.sapsd.salesorder.v11.*
import fi.tampere.services.sapsd.salesorder.v1.SendSalesOrderRequest
import fi.tampere.trevaka.InvoiceProperties
import mu.KotlinLogging
import org.springframework.ws.client.core.WebServiceTemplate
import org.springframework.ws.soap.client.SoapFaultClientException
import org.springframework.ws.soap.client.core.SoapActionCallback
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import javax.xml.bind.JAXBIntrospector
import javax.xml.datatype.DatatypeFactory
import javax.xml.datatype.XMLGregorianCalendar

private val logger = KotlinLogging.logger {}

class TrevakaInvoiceClient(val webServiceTemplate: WebServiceTemplate, val properties: InvoiceProperties) :
    InvoiceIntegrationClient {

    val dateFormatter: DateTimeFormatter =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale("fi"))

    override fun sendBatch(invoices: List<InvoiceDetailed>, agreementType: Int): Boolean {
        logger.info("Invoice batch started (agreementType=${agreementType})")
        try {
            val request = toRequest(invoices)
            val response = webServiceTemplate.marshalSendAndReceive(
                "${webServiceTemplate.defaultUri}/salesOrder", request,
                SoapActionCallback("http://www.tampere.fi/services/sapsd/salesorder/v1.0/SendSalesOrder")
            )
            when (val value = JAXBIntrospector.getValue(response)) {
                is SimpleAcknowledgementResponseType -> logger.info("Invoice batch ended with status ${value.statusMessage}")
                else -> logger.warn("Unknown response in invoice: $value")
            }
        } catch (e: SoapFaultClientException) {
            when (val faultDetail = unmarshalFaultDetail(e)) {
                is FaultType -> logger.error("Fault in invoice: ${faultDetail.errorCode}. Message: ${faultDetail.errorMessage}")
                else -> logger.error("Unknown fault in invoice: $faultDetail", e)
            }
            return false
        }
        return true
    }

    private fun toRequest(invoices: List<InvoiceDetailed>): SendSalesOrderRequest {
        return SendSalesOrderRequest().apply {
            salesOrder = SalesOrder().apply {
                header.addAll(invoices.map(this@TrevakaInvoiceClient::toHeader))
            }
        }
    }

    private fun toHeader(invoice: InvoiceDetailed): Header {
        return Header().apply {
            customer = P1PartnerType().apply {
                person = Person().apply {
                    ssn = invoice.headOfFamily.ssn
                    personName = PersonName().apply {
                        firstNames = invoice.headOfFamily.firstName
                        surName = invoice.headOfFamily.lastName
                    }
                }
                address = Address().apply {
                    street = invoice.headOfFamily.streetAddress
                    town = invoice.headOfFamily.postOffice
                    postCode = invoice.headOfFamily.postalCode
                }
            }
            paymentTerm = properties.paymentTerm
            dueDate = localDateToXMLGregorianCalendar(invoice.dueDate)
            billingDate = localDateToXMLGregorianCalendar(invoice.invoiceDate)
            salesOrganisation = properties.salesOrganisation
            distributionChannel = properties.distributionChannel
            division = properties.division
            salesOrderType = properties.salesOrderType
            interfaceID = properties.interfaceID
            items = toItems(invoice.rows)
        }
    }

    private fun toItems(rows: List<InvoiceRowDetailed>): Items {
        return Items().apply {
            item.addAll(rows.map(this@TrevakaInvoiceClient::toItem))
        }
    }

    private fun toItem(it: InvoiceRowDetailed): Item {
        return Item().apply {
            description = it.description
            profitCenter = it.costCenter
            material = productToMaterial(it.product)
            rowAmount = it.price()
            unitPrice = it.unitPrice
            quantity = it.amount.toFloat().toString()
            plant = properties.plant
            text.addAll(listOf(Text().apply {
                textRow.addAll(
                    listOf(
                        "${it.child.lastName} ${it.child.firstName}",
                        "${it.periodStart.format(dateFormatter)} - ${it.periodEnd.format(dateFormatter)}"
                    )
                )
            }))
        }
    }

    private fun localDateToXMLGregorianCalendar(localDate: LocalDate): XMLGregorianCalendar =
        DatatypeFactory.newInstance().newXMLGregorianCalendar(localDate.toString())

    private fun productToMaterial(product: Product): String = when (product) {
        Product.DAYCARE -> "500218"
        Product.DAYCARE_DISCOUNT -> throw UnsupportedOperationException("Unsupported product $product")
        Product.DAYCARE_INCREASE -> throw UnsupportedOperationException("Unsupported product $product")
        Product.PRESCHOOL_WITH_DAYCARE -> "500220"
        Product.PRESCHOOL_WITH_DAYCARE_DISCOUNT -> throw UnsupportedOperationException("Unsupported product $product")
        Product.PRESCHOOL_WITH_DAYCARE_INCREASE -> throw UnsupportedOperationException("Unsupported product $product")
        Product.TEMPORARY_CARE -> throw UnsupportedOperationException("Unsupported product $product")
        Product.SICK_LEAVE_100 -> "500248"
        Product.SICK_LEAVE_50 -> "500283"
        Product.ABSENCE -> throw UnsupportedOperationException("Unsupported product $product")
        Product.FREE_OF_CHARGE -> "500156"
    }

    private fun unmarshalFaultDetail(exception: SoapFaultClientException): Any? {
        try {
            val detailEntries = exception.soapFault?.faultDetail?.detailEntries
            return when (detailEntries?.hasNext()) {
                true -> webServiceTemplate.unmarshaller.unmarshal(detailEntries.next().source)
                else -> null
            }
        } catch (e: Exception) {
            logger.error("Unable to unmarshal fault detail", e)
            return null
        }
    }

}
