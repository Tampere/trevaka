// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.person.config

import fi.tampere.trevaka.TampereProperties
import org.apache.hc.client5.http.classic.HttpClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.ws.transport.WebServiceMessageSender
import org.springframework.ws.transport.http.HttpComponents5MessageSender
import trevaka.ipaas.dvvModificationRequestCustomizer
import trevaka.ipaas.newIpaasHttpClient

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
    fun httpClient(properties: TampereProperties) = newIpaasHttpClient(properties.ipaas)

    @Bean
    fun basicAuthCustomizer(properties: TampereProperties) = dvvModificationRequestCustomizer(properties.ipaas)
}
