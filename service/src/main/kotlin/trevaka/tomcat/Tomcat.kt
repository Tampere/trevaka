// SPDX-FileCopyrightText: 2021-2023 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka.tomcat

import fi.espoo.evaka.logging.defaultAccessLoggingValve
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.core.env.Environment

fun tomcatAccessLoggingCustomizer(env: Environment) = WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    it.addContextValves(defaultAccessLoggingValve(env))
}
