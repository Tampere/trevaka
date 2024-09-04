// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.payment

import fi.espoo.evaka.daycare.CareType
import fi.espoo.evaka.invoicing.domain.Payment
import fi.espoo.evaka.invoicing.domain.PaymentIntegrationClient
import fi.espoo.evaka.shared.db.Database
import fi.tampere.messages.ipaas.commontypes.v1.SimpleAcknowledgementResponseType
import fi.tampere.messages.sapfico.payableaccounting.v05.Invoice
import fi.tampere.messages.sapfico.payableaccounting.v05.PayableAccounting
import fi.tampere.messages.sapfico.payableaccounting.v05.PayableAccountingHeader
import fi.tampere.messages.sapfico.payableaccounting.v05.PayableAccountingLine
import fi.tampere.services.sapfico.payableaccounting.v1.SendPayableAccountingRequest
import fi.tampere.trevaka.PaymentProperties
import jakarta.xml.bind.JAXBIntrospector
import mu.KotlinLogging
import org.springframework.ws.client.core.WebServiceTemplate
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
        val request = SendPayableAccountingRequest().apply {
            payableAccounting = PayableAccounting().apply { invoice.addAll(payments.map(::toInvoice)) }
        }
        val response = webServiceTemplate.marshalSendAndReceive(
            properties.url,
            request,
            SoapActionCallback("http://www.tampere.fi/services/sapfico/payableaccounting/v1.0/SendPayableAccounting"),
        )
        when (val value = JAXBIntrospector.getValue(response)) {
            is SimpleAcknowledgementResponseType -> logger.info("Payment batch ended with status ${value.statusMessage}")
            else -> logger.warn("Unknown response in payment: $value")
        }
        return PaymentIntegrationClient.SendResult(succeeded = payments)
    }

    private fun toInvoice(payment: Payment): Invoice {
        val value = payment.amount.centsToEuros()
        return Invoice().apply {
            payableAccountingHeader = PayableAccountingHeader().apply {
                sapVendor = "_${payment.unit.providerId}" // TODO: remove prefix
                iban = payment.unit.iban?.filterNot { it.isWhitespace() }
                bic = "_" // TODO: remove
                customer = payment.unit.businessId
                organisation = 1310.toBigInteger()
                date = payment.period.end?.format()
                receiptType = "6F"
                debetKredit = "-"
                /**
                 * TODO: description = "Varhaiskasvatus" (reference & currency must also be set)
                 */
                billingDate = payment.paymentDate?.format()
                billNumber = payment.number?.toBigInteger()
                billValue = value
                basicDate = payment.dueDate?.format()
                interfaceID = "_383" // TODO: remove prefix
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
                    /**
                     * TODO: taxCode = "29" (transactionType, partnerCode & functionalArea must also be set)
                     */
                    debetKredit = "+"
                    this.value = value
                },
            )
        }
    }
}

private fun LocalDate.format(): String = format(DateTimeFormatter.ISO_LOCAL_DATE)
private fun Int.centsToEuros(): BigDecimal = BigDecimal(this).divide(BigDecimal(100), 2, RoundingMode.HALF_UP)
