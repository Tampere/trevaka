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
import trevaka.ipaas.IpaasProperties
import trevaka.ipaas.newIpaasHttpClient
import trevaka.ipaas.dvvModificationRequestCustomizer as ipaasDvvModificationRequestCustomizer

@Configuration
class TrevakaConfig {

    /**
     * Custom [WebServiceMessageSender] for [fi.espoo.evaka.vtjclient.config.XroadSoapClientConfig.wsTemplate].
     */
    @Bean
    fun webServiceMessageSender(trevakaProperties: TrevakaProperties, ipaasProperties: IpaasProperties): WebServiceMessageSender = if (trevakaProperties.enabledFeatures.frendsVtjKysely) {
        SimpleHttpComponents5MessageSender(
            newFrendsHttpClient(trevakaProperties.vtjKyselyApiKey ?: error("Vtj kysely api key not set (TREVAKA_VTJ_KYSELY_API_KEY)")),
        )
    } else {
        SimpleHttpComponents5MessageSender(newIpaasHttpClient(ipaasProperties))
    }

    @Bean
    fun basicAuthCustomizer(trevakaProperties: TrevakaProperties, ipaasProperties: IpaasProperties) = if (trevakaProperties.enabledFeatures.frendsVtjMutpa) {
        dvvModificationRequestCustomizer(trevakaProperties.vtjMutpaApiKey ?: error("Vtj mutpa api key not set (TREVAKA_VTJ_MUTPA_API_KEY)"))
    } else {
        ipaasDvvModificationRequestCustomizer(ipaasProperties)
    }
}
