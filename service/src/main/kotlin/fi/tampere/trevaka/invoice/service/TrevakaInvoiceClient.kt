package fi.tampere.trevaka.invoice.service

import com.fasterxml.jackson.databind.ObjectMapper
import fi.espoo.evaka.invoicing.domain.InvoiceDetailed
import fi.espoo.evaka.invoicing.integration.CommunityInvoiceBatch
import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient
import fi.espoo.evaka.invoicing.integration.createBatchExports
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class TrevakaInvoiceClient(private val objectMapper: ObjectMapper) : InvoiceIntegrationClient {
    private val sentBatches = mutableListOf<CommunityInvoiceBatch>()

    override fun sendBatch(invoices: List<InvoiceDetailed>, agreementType: Int): Boolean {
        val batch = createBatchExports(invoices, agreementType)
        logger.info("Trevaka mock invoice integration client got batch ${objectMapper.writeValueAsString(batch)}")
        sentBatches.add(batch)
        return true
    }
}

