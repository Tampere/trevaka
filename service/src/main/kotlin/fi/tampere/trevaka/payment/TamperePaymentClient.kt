// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.payment

import fi.espoo.evaka.daycare.CareType
import fi.espoo.evaka.invoicing.domain.Payment
import fi.espoo.evaka.invoicing.domain.PaymentIntegrationClient
import fi.espoo.evaka.shared.db.Database
import fi.tampere.messages.ipaas.commontypes.v1.FaultType
import fi.tampere.trevaka.PaymentProperties
import generated.Invoice
import generated.PayableAccounting
import generated.PayableAccountingHeader
import generated.PayableAccountingLine
import jakarta.xml.bind.JAXBIntrospector
import mu.KotlinLogging
import org.springframework.ws.client.core.WebServiceTemplate
import org.springframework.ws.soap.client.SoapFaultClientException
import org.springframework.ws.soap.client.core.SoapActionCallback
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val logger = KotlinLogging.logger {}

class TamperePaymentClient(
    private val webServiceTemplate: WebServiceTemplate,
    private val properties: PaymentProperties,
) : PaymentIntegrationClient {
    override fun send(payments: List<Payment>, tx: Database.Read): PaymentIntegrationClient.SendResult {
        try {
            val payableAccounting = PayableAccounting().apply { invoice.addAll(payments.map(::toInvoice)) }
            val response = webServiceTemplate.marshalSendAndReceive(
                properties.url,
                payableAccounting,
                SoapActionCallback("http://www.tampere.fi/services/sapfico/payableaccounting/v1.0/SendPayableAccounting"),
            )
            when (val value = JAXBIntrospector.getValue(response)) {
                else -> logger.warn("Unknown response in payment: $value")
            }
        } catch (e: SoapFaultClientException) {
            when (val faultDetail = unmarshalFaultDetail(e)) {
                is FaultType -> logger.error("Fault in payment: ${faultDetail.errorCode}. Message: ${faultDetail.errorMessage}. Details: ${faultDetail.detailMessage}")
                else -> logger.error("Unknown fault in payment: $faultDetail", e)
            }
            throw e
        }
        return PaymentIntegrationClient.SendResult(succeeded = payments)
    }

    private fun toInvoice(payment: Payment): Invoice {
        val value = payment.amount.centsToEuros()
        return Invoice().apply {
            payableAccountingHeader = PayableAccountingHeader().apply {
                sapVendor = payment.unit.providerId
                iban = payment.unit.iban
                customer = payment.unit.businessId
                organisation = 1310.toBigInteger()
                date = payment.period.end?.format()
                receiptType = "6F"
                debetKredit = "-"
                description = "Varhaiskasvatus"
                billingDate = payment.paymentDate?.format()
                billNumber = payment.number?.toBigInteger()
                billValue = value
                basicDate = payment.dueDate?.format()
                interfaceID = "383"
            }
            payableAccountingLine.add(
                PayableAccountingLine().apply {
                    account = "440100"
                    costCenter = "131111"
                    profitCenter = "131111"
                    internalOrder = with(payment.unit.careType) {
                        when {
                            contains(CareType.CLUB) -> "25275"
                            else -> "20285"
                        }
                    }
                    taxCode = "29"
                    debetKredit = "+"
                    this.value = value
                },
            )
        }
    }

    private fun unmarshalFaultDetail(exception: SoapFaultClientException): Any? {
        return try {
            val detailEntries = exception.soapFault?.faultDetail?.detailEntries
            when (detailEntries?.hasNext()) {
                true -> webServiceTemplate.unmarshaller.unmarshal(detailEntries.next().source)
                else -> null
            }
        } catch (e: Exception) {
            logger.error("Unable to unmarshal fault detail", e)
            null
        }
    }
}

private fun LocalDate.format(): String = format(DateTimeFormatter.ISO_LOCAL_DATE)
private fun Int.centsToEuros(): BigDecimal = BigDecimal(this).divide(BigDecimal(100), 2, RoundingMode.HALF_UP)
