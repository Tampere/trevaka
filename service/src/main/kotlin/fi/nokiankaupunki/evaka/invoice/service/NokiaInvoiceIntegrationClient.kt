// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.nokiankaupunki.evaka.invoice.service

import fi.espoo.evaka.invoicing.domain.InvoiceDetailed
import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient
import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient.SendResult
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

class NokiaInvoiceIntegrationClient(
    private val s3Sender: S3Sender,
    private val invoiceGenerator: ProEInvoiceGenerator,
) : InvoiceIntegrationClient {
    override fun send(invoices: List<InvoiceDetailed>): SendResult {
        val failedList = mutableListOf<InvoiceDetailed>()

        val (zeroSumInvoices, nonZeroSumInvoices) = invoices.partition { invoice -> invoice.totalPrice == 0 }
        val generatorResult = invoiceGenerator.generateInvoice(nonZeroSumInvoices)
        val proEinvoices = generatorResult.invoiceString
        val successList = generatorResult.sendResult.succeeded
        val manuallySentList = generatorResult.sendResult.manuallySent

        if (successList.isNotEmpty()) {
            s3Sender.send(proEinvoices)
            logger.info { "Successfully sent ${successList.size} invoices and created ${manuallySentList.size} manual invoice" }
        }

        return SendResult(successList + zeroSumInvoices, failedList, manuallySentList)
    }
}

fun interface StringInvoiceGenerator {
    data class InvoiceGeneratorResult(
        val sendResult: SendResult = SendResult(),
        val invoiceString: String = "",
    )

    fun generateInvoice(invoices: List<InvoiceDetailed>): InvoiceGeneratorResult
}
