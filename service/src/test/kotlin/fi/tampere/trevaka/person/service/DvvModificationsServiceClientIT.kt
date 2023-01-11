// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.person.service

import com.github.tomakehurst.wiremock.client.BasicCredentials
import com.github.tomakehurst.wiremock.client.WireMock.*
import fi.espoo.evaka.dvv.DvvModificationsServiceClient
import fi.tampere.trevaka.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

internal class DvvModificationsServiceClientIT : AbstractIntegrationTest() {

    @Autowired
    lateinit var dvvModificationsServiceClient: DvvModificationsServiceClient

    @Test
    fun getFirstModificationToken() {
        stubFor(
            get(urlEqualTo("/mock/modifications/kirjausavain/2021-04-01")).willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBodyFile("modifications-client/first-modification-token-response-ok.json")
            )
        )

        val response = dvvModificationsServiceClient.getFirstModificationToken(LocalDate.of(2021, 4, 1))
        assertThat(response).returns(5446623423) { it?.latestModificationToken }

        verify(
            getRequestedFor(urlEqualTo("/mock/modifications/kirjausavain/2021-04-01"))
                .withBasicAuth(BasicCredentials("user", "pass"))
        )
    }

    @Test
    fun getModifications() {
        stubFor(
            post(urlEqualTo("/mock/modifications/muutokset")).willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBodyFile("modifications-client/modifications-response-ok.json")
            )
        )

        val response = dvvModificationsServiceClient.getModifications("5446623423", listOf())
        assertThat(response).returns("3494393") { it.viimeisinKirjausavain }

        verify(
            postRequestedFor(urlEqualTo("/mock/modifications/muutokset"))
                .withBasicAuth(BasicCredentials("user", "pass"))
        )
    }
}
