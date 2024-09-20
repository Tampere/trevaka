// SPDX-FileCopyrightText: 2021-2023 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka.ipaas

import com.github.kittinunf.fuel.core.extensions.authentication
import fi.espoo.evaka.dvv.DvvModificationRequestCustomizer
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.core5.http.ConnectionReuseStrategy
import org.apache.hc.core5.http.HttpRequest
import org.apache.hc.core5.http.HttpRequestInterceptor
import org.apache.hc.core5.http.HttpResponse
import org.apache.hc.core5.http.message.BasicHeader
import org.apache.hc.core5.http.protocol.HttpContext
import org.springframework.ws.transport.http.HttpComponents5MessageSender
import java.nio.charset.StandardCharsets
import java.util.Base64

data class IpaasProperties(
    val username: String,
    val password: String,
)

fun newIpaasHttpClient(properties: IpaasProperties) = HttpClientBuilder.create()
    .addRequestInterceptorFirst(HttpComponents5MessageSender.RemoveSoapHeadersInterceptor())
    .addRequestInterceptorFirst(basicAuthInterceptor(properties.username, properties.password))
    .setConnectionReuseStrategy(NoConnectionReuseStrategy.INSTANCE) // fix random "connection reset" errors
    .build()

fun dvvModificationRequestCustomizer(properties: IpaasProperties) = DvvModificationRequestCustomizer { request ->
    request.authentication().basic(properties.username, properties.password)
}

private fun basicAuthInterceptor(username: String, password: String) = HttpRequestInterceptor { request, _, _ ->
    request.addHeader(BasicHeader("Authorization", "Basic ${encode("$username:$password")}"))
}

private fun encode(data: String) = Base64.getEncoder().encodeToString(data.toByteArray(StandardCharsets.UTF_8))

private class NoConnectionReuseStrategy : ConnectionReuseStrategy {
    override fun keepAlive(request: HttpRequest?, response: HttpResponse?, context: HttpContext?): Boolean = false

    companion object {
        val INSTANCE = NoConnectionReuseStrategy()
    }
}
