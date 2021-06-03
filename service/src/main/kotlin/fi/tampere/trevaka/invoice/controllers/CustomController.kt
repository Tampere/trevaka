// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.invoice.controllers

import fi.espoo.evaka.invoicing.domain.InvoiceDetailed
import fi.espoo.evaka.invoicing.domain.InvoiceRowDetailed
import fi.espoo.evaka.invoicing.domain.InvoiceStatus
import fi.espoo.evaka.invoicing.domain.PersonData
import fi.espoo.evaka.invoicing.domain.Product
import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.UUID
import javax.servlet.http.HttpServletRequest

private val logger = KotlinLogging.logger { }

//FIXME: this controller is PUBLIC and for illustrating/testing customization principles only,
// remove at the latest after actual invoicing client is implemented
@Profile("trevaka")
@RestController
@RequestMapping("/public/tre-test")
class CustomController @Autowired constructor(private val integrationClient: InvoiceIntegrationClient) {

    @Value("\${spring.application.name}")
    lateinit var name: String

    @Autowired
    lateinit var env: Environment

    @GetMapping("/test")
    fun testMethod(request: HttpServletRequest): ResponseEntity<String> {
        logger.info("Test profile-based property override: {}", name)
        logger.info("Active profiles: {}", env.activeProfiles)
        return ResponseEntity.ok(integrationClient.sendBatch(listOf(validInvoice(5)), 5).toString())
    }
}

private fun validInvoice(agreementType: Int): InvoiceDetailed {
    val invoiceDate = LocalDate.now().withDayOfMonth(1)
    val periodDate = invoiceDate.minusMonths(1)
    val periodStart = periodDate.withDayOfMonth(1)
    val periodEnd = periodDate.withDayOfMonth(periodDate.lengthOfMonth())
    val dueDate = invoiceDate.withDayOfMonth(invoiceDate.lengthOfMonth())
    val headOfFamily = PersonData.Detailed(
        UUID.randomUUID(), LocalDate.of(1982, 3, 31), null,
        "Maija", "Meikäläinen",
        "310382-956D", "Meikäläisenkuja 6 B 7", "33730", "TAMPERE",
        null, null, null, null, restrictedDetailsEnabled = false
    )
    val invoiceRow1 = InvoiceRowDetailed(
        UUID.randomUUID(), PersonData.Detailed(
            UUID.randomUUID(), LocalDate.of(2018, 1, 1), null,
            "Matti", "Meikäläinen",
            null, null, null, null,
            null, null, null, null, restrictedDetailsEnabled = false
        ), 1, 243,
        periodStart,
        periodEnd,
        Product.DAYCARE, "131885", null, "kuvaus1"
    )
    val invoiceRow2 = InvoiceRowDetailed(
        UUID.randomUUID(), PersonData.Detailed(
            UUID.randomUUID(), LocalDate.of(2015, 11, 26), null,
            "Maiju", "Meikäläinen",
            null, null, null, null,
            null, null, null, null, restrictedDetailsEnabled = false
        ), 1, 482,
        periodStart,
        periodEnd,
        Product.PRESCHOOL_WITH_DAYCARE, "284823", null, "kuvaus2"
    )
    return InvoiceDetailed(
        UUID.randomUUID(), InvoiceStatus.WAITING_FOR_SENDING, periodStart, periodEnd,
        dueDate, invoiceDate,
        agreementType, headOfFamily, listOf(invoiceRow1, invoiceRow2), null, null, null
    )
}
