// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.ylojarvi.evaka.invoice.service

import fi.espoo.evaka.invoicing.domain.InvoiceDetailed
import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient
import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient.SendResult
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class YlojarviInvoiceIntegrationClient(
    private val s3Sender: S3Sender,
    private val invoiceGenerator: ProEInvoiceGenerator,
) : InvoiceIntegrationClient {
    override fun send(invoices: List<InvoiceDetailed>): SendResult {
        val failedList = mutableListOf<InvoiceDetailed>()

        val generatorResult = invoiceGenerator.generateInvoice(invoices)
        val proEinvoices = generatorResult.invoiceString
        val successList = generatorResult.sendResult.succeeded
        val manuallySentList = generatorResult.sendResult.manuallySent

        if (successList.isNotEmpty()) {
            s3Sender.send(proEinvoices)
            logger.info { "Successfully sent ${successList.size} invoices and created ${manuallySentList.size} manual invoice" }
        }

        return SendResult(successList, failedList, manuallySentList)
    }
}

fun interface StringInvoiceGenerator {
    data class InvoiceGeneratorResult(
        val sendResult: SendResult = SendResult(),
        val invoiceString: String = "",
    )

    fun generateInvoice(invoices: List<InvoiceDetailed>): InvoiceGeneratorResult
}
