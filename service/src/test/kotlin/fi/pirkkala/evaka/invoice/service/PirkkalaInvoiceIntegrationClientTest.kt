// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.pirkkala.evaka.invoice.service

import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.espoo.evaka.shared.domain.MockEvakaClock
import fi.pirkkala.evaka.AbstractPirkkalaIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalTime

class PirkkalaInvoiceIntegrationClientTest : AbstractPirkkalaIntegrationTest() {
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
        val data = getS3Object(properties.bucket.export, "invoices/604_53_202102011234.dat")
            .use { it.readAllBytes().toString(StandardCharsets.ISO_8859_1) }
        assertEquals(
            """310382-956DL  Meikäläinen Matti                                                                                   Meikäläisenkuja 6 B 7         90100 OULU                                                                                                                              1   K202102042021030620220505                    1                            1000         53   Varhaiskasvatus 01.2021                                                                                                                                                                                                                                                                                      
310382-956D3Meikäläinen Maiju                                                                                                     
310382-956D301.01.2021 - 31.01.2021                                                                                               
310382-956D1Esiopetusta täydentävä varhaiskasvatus   000004820000KPL 00000001000000                                                                                                                         325730010002627                                             00000048200
310382-956D3kuvaus2                                                                                                               
310382-956D3Meikäläinen Matti                                                                                                     
310382-956D301.01.2021 - 31.01.2021                                                                                               
310382-956D1Varhaiskasvatus                          000002430000KPL 00000001000000                                                                                                                         325730010002627                                             00000024300
310382-956D3kuvaus1                                                                                                               
310382-956D301.01.2021 - 31.01.2021                                                                                               
310382-956D1Varhaiskasvatus                          000002500000KPL 00000001000000                                                                                                                         325730010002627                                             00000025000
310382-956D301.01.2021 - 31.01.2021                                                                                               
310382-956D1Hyvityspäivä                             000002500000KPL-00000001000000                                                                                                                         325730010002627                                            -00000025000
310382-956D3kuvaus4                                                                                                               
""",
            data,
        )
    }
}
