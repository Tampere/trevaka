package fi.tampere.trevaka.invoice.service

import fi.espoo.evaka.invoicing.service.PDFService
import fi.espoo.evaka.invoicing.service.Page
import fi.espoo.evaka.invoicing.service.Template
import fi.tampere.trevaka.AbstractIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.thymeleaf.context.Context

internal class PDFServiceTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var pdfService: PDFService

    @Test
    fun render() {
        pdfService.render(Page(Template("test"), Context()))
    }

}
