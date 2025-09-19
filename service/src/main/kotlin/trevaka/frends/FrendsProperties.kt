// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka.frends

import okhttp3.Credentials
import okhttp3.Interceptor

data class FrendsProperties(
    val username: String,
    val password: String,
)

fun basicAuthInterceptor(properties: FrendsProperties) = Interceptor { chain ->
    chain.proceed(
        chain.request()
            .newBuilder()
            .header("Authorization", Credentials.basic(properties.username, properties.password, Charsets.UTF_8))
            .build(),
    )
}
