// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.vesilahti.evaka.invoice.service

import fi.vesilahti.evaka.SftpConnector
import fi.vesilahti.evaka.SftpProperties
import fi.vesilahti.evaka.SftpSender
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argThat
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

internal class InvoiceSenderTest {
    @Test
    fun `should send invoice`() {
        val path = "/some/path"
        val sftpProperties = SftpProperties("", path, "", "")
        val proEInvoice = "one"
        val sftpConnector = mock<SftpConnector>()
        val sftpSender = SftpSender(
            sftpProperties,
            sftpConnector,
        )

        sftpSender.send(proEInvoice)

        verify(sftpConnector).connect(sftpProperties.address, sftpProperties.username, sftpProperties.password)
        val fileNamePattern = """$path/proe-\d{8}-\d{6}.txt"""
        verify(sftpConnector).send(
            argThat { filePath -> filePath.matches(Regex(fileNamePattern)) },
            eq("one"),
        )
        verify(sftpConnector).disconnect()
    }
}
