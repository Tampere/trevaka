// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.util

import org.apache.hc.core5.http.ConnectionReuseStrategy
import org.apache.hc.core5.http.HttpRequest
import org.apache.hc.core5.http.HttpRequestInterceptor
import org.apache.hc.core5.http.HttpResponse
import org.apache.hc.core5.http.message.BasicHeader
import org.apache.hc.core5.http.protocol.HttpContext
import java.nio.charset.StandardCharsets
import java.util.Base64

fun basicAuthInterceptor(username: String, password: String) = HttpRequestInterceptor { request, _, _ ->
    request.addHeader(BasicHeader("Authorization", "Basic ${encode("$username:$password")}"))
}

internal fun encode(data: String) = Base64.getEncoder().encodeToString(data.toByteArray(StandardCharsets.UTF_8))

class NoConnectionReuseStrategy : ConnectionReuseStrategy {
    override fun keepAlive(request: HttpRequest?, response: HttpResponse?, context: HttpContext?): Boolean {
        return false
    }

    companion object {
        val INSTANCE = NoConnectionReuseStrategy()
    }
}
