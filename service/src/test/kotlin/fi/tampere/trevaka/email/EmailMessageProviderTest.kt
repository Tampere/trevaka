// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.email

import fi.espoo.evaka.daycare.domain.Language
import fi.espoo.evaka.emailclient.IEmailMessageProvider
import fi.espoo.evaka.shared.AssistanceNeedDecisionId
import fi.espoo.evaka.shared.ChildId
import fi.espoo.evaka.shared.domain.FiniteDateRange
import fi.tampere.trevaka.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.util.UUID
import java.util.stream.Stream

internal class EmailMessageProviderTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var emailMessageProvider: IEmailMessageProvider

    @ParameterizedTest(name = "{0}")
    @MethodSource("contents")
    fun testContentDoNotContainEspooText(name: String, content: String) {
        assertNotContainEspooText(content)
    }

    fun contents(): Stream<Arguments> = listOf(
        Arguments.of(
            "getDaycareApplicationReceivedEmailText",
            emailMessageProvider.getDaycareApplicationReceivedEmailText()
        ),
        Arguments.of(
            "getDaycareApplicationReceivedEmailHtml",
            emailMessageProvider.getDaycareApplicationReceivedEmailHtml()
        ),
        Arguments.of(
            "getClubApplicationReceivedEmailText",
            emailMessageProvider.getClubApplicationReceivedEmailText()
        ),
        Arguments.of(
            "getClubApplicationReceivedEmailHtml",
            emailMessageProvider.getClubApplicationReceivedEmailHtml()
        ),
        Arguments.of(
            "getPendingDecisionEmailText",
            emailMessageProvider.getPendingDecisionEmailText()
        ),
        Arguments.of(
            "getPendingDecisionEmailHtml",
            emailMessageProvider.getPendingDecisionEmailHtml()
        ),
        Arguments.of(
            "getPreschoolApplicationReceivedEmailHtmlWithinApplicationPeriodTrue",
            emailMessageProvider.getPreschoolApplicationReceivedEmailHtml(true)
        ),
        Arguments.of(
            "getPreschoolApplicationReceivedEmailTextWithinApplicationPeriodTrue",
            emailMessageProvider.getPreschoolApplicationReceivedEmailText(true)
        ),
        Arguments.of(
            "getPreschoolApplicationReceivedEmailHtmlWithinApplicationPeriodFalse",
            emailMessageProvider.getPreschoolApplicationReceivedEmailHtml(false)
        ),
        Arguments.of(
            "getPreschoolApplicationReceivedEmailTextWithinApplicationPeriodFalse",
            emailMessageProvider.getPreschoolApplicationReceivedEmailText(false)
        ),
        Arguments.of(
            "getDecisionEmailText",
            emailMessageProvider.getDecisionEmailText(
                ChildId(UUID.randomUUID()), AssistanceNeedDecisionId(UUID.randomUUID())
            )
        ),
        Arguments.of(
            "getDecisionEmailHtml",
            emailMessageProvider.getDecisionEmailHtml(
                ChildId(UUID.randomUUID()), AssistanceNeedDecisionId(UUID.randomUUID())
            )
        ),
        Arguments.of(
            "missingReservationsNotificationText",
            emailMessageProvider.missingReservationsNotification(
                Language.fi,
                LocalDate.of(2023, 2, 13).let { FiniteDateRange(it, it.plusDays(6)) }
            ).text
        ),
        Arguments.of(
            "missingReservationsNotificationHtml",
            emailMessageProvider.missingReservationsNotification(
                Language.fi,
                LocalDate.of(2023, 2, 13).let { FiniteDateRange(it, it.plusDays(6)) }
            ).html
        )
    )
        .stream()

    private fun assertNotContainEspooText(message: String) {
        assertThat(message.also(::println))
            .isNotBlank
            .doesNotContainIgnoringCase("espoo")
            .doesNotContainIgnoringCase("esbo")
    }
}
