// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.vesilahti.evaka

import com.jcraft.jsch.SftpException
import java.text.SimpleDateFormat
import java.util.*

class SftpSender(val sftpProperties: SftpProperties, val sftpConnector: SftpConnector) {
    @Throws(SftpException::class)
    fun send(content: String) {
        val path = sftpProperties.path
        val fileName = SimpleDateFormat("'proe-'yyyyMMdd-hhmmss'.txt'").format(Date())
        val filepath = "$path/$fileName"

        sftpConnector.connect(sftpProperties.address, sftpProperties.username, sftpProperties.password)

        sftpConnector.send(filepath, content)

        sftpConnector.disconnect()
    }
}
