// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.person.service

import com.github.tomakehurst.wiremock.client.BasicCredentials
import com.github.tomakehurst.wiremock.client.WireMock.*
import fi.espoo.evaka.identity.ExternalIdentifier
import fi.espoo.evaka.shared.auth.AuthenticatedUser
import fi.espoo.evaka.vtjclient.service.persondetails.IPersonDetailsService
import fi.tampere.trevaka.AbstractTampereIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class PersonDetailsServiceIT : AbstractTampereIntegrationTest() {

    @Autowired
    lateinit var personDetailsService: IPersonDetailsService

    @Test
    fun getBasicDetailsFor() {
        stubFor(
            post(urlEqualTo("/mock/vtj")).willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "text/xml")
                    .withBodyFile("person-client/henkilotunnuskysely-response-ok.xml")
                    .withTransformers("response-template")
                    .withTransformerParameter("ssn", "070644-937X"),
            ),
        )

        val person = personDetailsService.getBasicDetailsFor(
            IPersonDetailsService.DetailsQuery(
                AuthenticatedUser.SystemInternalUser.evakaUserId,
                ExternalIdentifier.SSN.getInstance("070644-937X"),
            ),
        )

        assertThat(person).returns("070644-937X", { it.socialSecurityNumber })
        verify(
            postRequestedFor(urlEqualTo("/mock/vtj"))
                .withBasicAuth(BasicCredentials("user", "pass"))
                .withHeader("Content-Type", equalTo("text/xml; charset=UTF-8"))
                .withHeader("SOAPAction", equalTo("\"\"")),
        )
    }
}
