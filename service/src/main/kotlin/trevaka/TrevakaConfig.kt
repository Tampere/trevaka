// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.ws.transport.WebServiceMessageSender
import org.springframework.ws.transport.http.SimpleHttpComponents5MessageSender
import trevaka.frends.dvvModificationRequestCustomizer
import trevaka.frends.newFrendsHttpClient

@Configuration
class TrevakaConfig {

    /**
     * Custom [WebServiceMessageSender] for [fi.espoo.evaka.vtjclient.config.XroadSoapClientConfig.wsTemplate].
     */
    @Bean
    fun webServiceMessageSender(trevakaProperties: TrevakaProperties): WebServiceMessageSender = SimpleHttpComponents5MessageSender(
        newFrendsHttpClient(trevakaProperties.vtjKyselyApiKey),
    )

    @Bean
    fun basicAuthCustomizer(trevakaProperties: TrevakaProperties) = dvvModificationRequestCustomizer(trevakaProperties.vtjMutpaApiKey)
}
