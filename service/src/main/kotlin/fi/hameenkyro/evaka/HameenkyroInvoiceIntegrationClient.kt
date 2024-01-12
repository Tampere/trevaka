// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.hameenkyro.evaka

import fi.espoo.evaka.invoicing.domain.InvoiceDetailed
import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient

class HameenkyroInvoiceIntegrationClient : InvoiceIntegrationClient {
    override fun send(invoices: List<InvoiceDetailed>): InvoiceIntegrationClient.SendResult {
        TODO("Not yet implemented")
    }
}
