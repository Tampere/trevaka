// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.email

import fi.espoo.evaka.emailclient.IEmailMessageProvider
import fi.espoo.evaka.shared.AssistanceNeedDecisionId
import fi.espoo.evaka.shared.ChildId
import fi.tampere.trevaka.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

internal class EmailMessageProviderTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var emailMessageProvider: IEmailMessageProvider

    @Test
    fun testNonPreschoolMessagesDoNotContainEspooText() {
        assertNotContainEspooText(emailMessageProvider.getDaycareApplicationReceivedEmailText())
        assertNotContainEspooText(emailMessageProvider.getDaycareApplicationReceivedEmailHtml())
        assertNotContainEspooText(emailMessageProvider.getClubApplicationReceivedEmailText())
        assertNotContainEspooText(emailMessageProvider.getClubApplicationReceivedEmailHtml())
        assertNotContainEspooText(emailMessageProvider.getPendingDecisionEmailText())
        assertNotContainEspooText(emailMessageProvider.getPendingDecisionEmailHtml())
        assertNotContainEspooText(emailMessageProvider.getPreschoolApplicationReceivedEmailHtml(true))
        assertNotContainEspooText(emailMessageProvider.getPreschoolApplicationReceivedEmailText(true))
        assertNotContainEspooText(emailMessageProvider.getPreschoolApplicationReceivedEmailHtml(false))
        assertNotContainEspooText(emailMessageProvider.getPreschoolApplicationReceivedEmailText(false))
        assertNotContainEspooText(
            emailMessageProvider.getDecisionEmailText(
                ChildId(UUID.randomUUID()),
                AssistanceNeedDecisionId(UUID.randomUUID())
            )
        )
        assertNotContainEspooText(
            emailMessageProvider.getDecisionEmailHtml(
                ChildId(UUID.randomUUID()),
                AssistanceNeedDecisionId(UUID.randomUUID())
            )
        )
    }

    private fun assertNotContainEspooText(message: String) {
        assertThat(message.also(::println))
            .isNotBlank
            .doesNotContainIgnoringCase("espoo")
            .doesNotContainIgnoringCase("esbo")
    }
}