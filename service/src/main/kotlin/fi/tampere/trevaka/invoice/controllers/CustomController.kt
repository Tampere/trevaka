// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.invoice.controllers

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
        return ResponseEntity.ok(integrationClient.send(emptyList()).toString())
    }
}