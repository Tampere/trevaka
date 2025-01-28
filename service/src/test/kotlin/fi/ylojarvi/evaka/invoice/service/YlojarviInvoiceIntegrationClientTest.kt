// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.ylojarvi.evaka.invoice.service

import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.espoo.evaka.shared.domain.MockEvakaClock
import fi.ylojarvi.evaka.AbstractYlojarviIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalTime

class YlojarviInvoiceIntegrationClientTest : AbstractYlojarviIntegrationTest() {
    @Autowired
    private lateinit var invoiceIntegrationClient: InvoiceIntegrationClient

    @Test
    fun `valid invoice with invoice number`() {
        whenever(clockService.clock()).thenReturn(
            MockEvakaClock(
                HelsinkiDateTime.of(
                    LocalDate.of(2021, 2, 1),
                    LocalTime.of(12, 34),
                ),
            ),
        )
        val invoices = listOf(validInvoice().copy(number = 1))

        val result = invoiceIntegrationClient.send(invoices)

        assertThat(result)
            .returns(invoices) { it.succeeded }
            .returns(emptyList()) { it.failed }
            .returns(emptyList()) { it.manuallySent }
        val data = getS3Object(properties.bucket.export, "invoices/980_53_202102011234.dat")
            .use { it.readAllBytes().toString(StandardCharsets.ISO_8859_1) }
        assertEquals(
            """310382-956DL10Meikäläinen Matti                                                                                   Meikäläisenkuja 6 B 7         90100 OULU                                                                                                                             01 N0K20210204202103062022050500000000            1                           N1000           000Varhaiskasvatus 01.2021                                                                                                                                                                                                                                                                       
310382-956D3Meikäläinen Maiju                                                                                                                                                                  
310382-956D301.01.2021 - 31.01.2021                                                                                                                                                            
310382-956D1Esiopetusta täydentävä varhaiskasvatus   000004820000kpl 00000001000000                                                            0                                                            325730010002627 484                                                    
310382-956D3kuvaus2                                                     
310382-956D3Meikäläinen Matti                                                                                                                                                                  
310382-956D301.01.2021 - 31.01.2021                                                                                                                                                            
310382-956D1Varhaiskasvatus                          000002430000kpl 00000001000000                                                            0                                                            325730010002627 485                                                    
310382-956D3kuvaus1                                                     
310382-956D301.01.2021 - 31.01.2021                                                                                                                                                            
310382-956D1Varhaiskasvatus                          000002500000kpl 00000001000000                                                            0                                                            325730010002627 483                                                    
310382-956D301.01.2021 - 31.01.2021                                                                                                                                                            
310382-956D1Hyvityspäivä                             000002500000kpl-00000001000000                                                            0                                                            325730010002627 483                                                    
310382-956D3kuvaus4                                                     
""",
            data,
        )
    }
}
