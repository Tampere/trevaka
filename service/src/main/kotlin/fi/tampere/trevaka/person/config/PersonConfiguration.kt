// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.person.config

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.extensions.authentication
import fi.espoo.evaka.dvv.DvvModificationRequestCustomizer
import fi.tampere.trevaka.TrevakaProperties
import fi.tampere.trevaka.util.NoConnectionReuseStrategy
import fi.tampere.trevaka.util.basicAuthInterceptor
import org.apache.hc.client5.http.classic.HttpClient
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.ws.transport.WebServiceMessageSender
import org.springframework.ws.transport.http.HttpComponents5MessageSender
import org.springframework.ws.transport.http.HttpComponents5MessageSender.RemoveSoapHeadersInterceptor

const val HTTP_CLIENT_PERSON = "httpClientPerson"

@Configuration
class PersonConfiguration {

    /**
     * Custom [WebServiceMessageSender] for [fi.espoo.evaka.vtjclient.config.XroadSoapClientConfig.wsTemplate].
     */
    @Bean
    fun webServiceMessageSender(@Qualifier(HTTP_CLIENT_PERSON) httpClient: HttpClient): WebServiceMessageSender {
        return HttpComponents5MessageSender(httpClient)
    }

    @Bean(HTTP_CLIENT_PERSON)
    fun httpClient(properties: TrevakaProperties) = HttpClientBuilder.create()
        .addRequestInterceptorFirst(RemoveSoapHeadersInterceptor())
        .addRequestInterceptorFirst(basicAuthInterceptor(properties.ipaas.username, properties.ipaas.password))
        .setConnectionReuseStrategy(NoConnectionReuseStrategy.INSTANCE) // fix random "connection reset" errors
        .build()

    /**
     * Custom [FuelManager] for [fi.espoo.evaka.dvv.DvvModificationsServiceClient].
     */
    @Bean
    fun fuelManager(properties: TrevakaProperties) = FuelManager()

    @Bean
    fun basicAuthCustomizer(properties: TrevakaProperties) = DvvModificationRequestCustomizer { request ->
        request.authentication().basic(properties.ipaas.username, properties.ipaas.password)
    }
}
