// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.pirkkala.evaka.invoice.service

import fi.pirkkala.evaka.BucketProperties
import fi.pirkkala.evaka.InvoiceProperties
import fi.pirkkala.evaka.PirkkalaProperties
import fi.pirkkala.evaka.util.FinanceDateProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.KArgumentCaptor
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import trevaka.ipaas.IpaasProperties

val testProperties = PirkkalaProperties(
    IpaasProperties("user", "pass"),
    InvoiceProperties("604", "53"),
    BucketProperties("trevaka-export-it"),
)

class PirkkalaInvoiceIntegrationClientTest {

    private lateinit var pirkkalaInvoiceIntegrationClient: PirkkalaInvoiceIntegrationClient

    private lateinit var s3SenderMock: S3Sender
    private lateinit var s3SenderArgumentCaptor: KArgumentCaptor<String>
    private lateinit var financeDateProviderMock: FinanceDateProvider

    @BeforeEach
    fun setup() {
        s3SenderMock = mock()
        s3SenderArgumentCaptor = argumentCaptor<String>()
        financeDateProviderMock = mock {
            on { previousMonth() } doReturn "01.2021"
        }
        pirkkalaInvoiceIntegrationClient = PirkkalaInvoiceIntegrationClient(
            s3SenderMock,
            ProEInvoiceGenerator(InvoiceChecker(), financeDateProviderMock, testProperties),
        )
    }

    @Test
    fun `valid invoice with invoice number`() {
        val invoices = listOf(validInvoice().copy(number = 1))

        pirkkalaInvoiceIntegrationClient.send(invoices)

        verify(s3SenderMock).send(s3SenderArgumentCaptor.capture())
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
310382-956D1Hyvityspäivä                             000002500000KPL-00000001000000                                                                                                                         325730010002627                                             –0000025000
310382-956D3kuvaus4                                                                                                               
""",
            s3SenderArgumentCaptor.firstValue,
        )
    }
}
