package fi.tampere.trevaka.invoice.controllers

import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@Profile("trevaka")
@RestController
@RequestMapping("/public/tre-test")
class CustomController @Autowired constructor(private val integrationClient: InvoiceIntegrationClient) {

    @GetMapping("/test")
    fun testMethod(request: HttpServletRequest): ResponseEntity<String> {
        return ResponseEntity.ok(integrationClient.sendBatch(emptyList(), 5).toString())
    }
}