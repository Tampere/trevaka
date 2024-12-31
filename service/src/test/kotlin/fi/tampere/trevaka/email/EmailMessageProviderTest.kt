// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.email

import fi.espoo.evaka.daycare.domain.Language
import fi.espoo.evaka.emailclient.CalendarEventNotificationData
import fi.espoo.evaka.emailclient.EmailContent
import fi.espoo.evaka.emailclient.IEmailMessageProvider
import fi.espoo.evaka.emailclient.MessageThreadData
import fi.espoo.evaka.invoicing.domain.FinanceDecisionType
import fi.espoo.evaka.invoicing.service.IncomeNotificationType
import fi.espoo.evaka.messaging.AccountType
import fi.espoo.evaka.messaging.MessageType
import fi.espoo.evaka.shared.ChildId
import fi.espoo.evaka.shared.HtmlSafe
import fi.espoo.evaka.shared.MessageThreadId
import fi.espoo.evaka.shared.domain.FiniteDateRange
import fi.tampere.trevaka.AbstractTampereIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.util.UUID
import java.util.stream.Stream

internal class EmailMessageProviderTest : AbstractTampereIntegrationTest() {

    @Autowired
    private lateinit var emailMessageProvider: IEmailMessageProvider

    @ParameterizedTest(name = "{0}")
    @MethodSource("contents")
    fun testContentDoNotContainEspooText(name: String, content: EmailContent) {
        assertNotContainEspooText(content.subject)
        assertNotContainEspooText(content.text)
        assertNotContainEspooText(content.html)
    }

    fun contents(): Stream<Arguments> = listOf(
        Arguments.of(
            "daycareApplicationReceived",
            emailMessageProvider.daycareApplicationReceived(Language.fi),
        ),
        Arguments.of(
            "clubApplicationReceived",
            emailMessageProvider.clubApplicationReceived(Language.fi),
        ),
        Arguments.of(
            "pendingDecisionNotification",
            emailMessageProvider.pendingDecisionNotification(Language.fi),
        ),
        Arguments.of(
            "preschoolApplicationReceivedWithinApplicationPeriodTrue",
            emailMessageProvider.preschoolApplicationReceived(Language.fi, true),
        ),
        Arguments.of(
            "preschoolApplicationReceivedWithinApplicationPeriodFalse",
            emailMessageProvider.preschoolApplicationReceived(Language.fi, false),
        ),
        Arguments.of(
            "assistanceNeedDecisionNotification",
            emailMessageProvider.assistanceNeedDecisionNotification(Language.fi),
        ),
        Arguments.of(
            "assistanceNeedPreschoolDecisionNotification",
            emailMessageProvider.assistanceNeedPreschoolDecisionNotification(Language.fi),
        ),
        Arguments.of(
            "missingReservationsNotification",
            emailMessageProvider.missingReservationsNotification(
                Language.fi,
                LocalDate.of(2023, 2, 13).let { FiniteDateRange(it, it.plusDays(6)) },
            ),
        ),
        Arguments.of(
            "messageNotification",
            emailMessageProvider.messageNotification(
                Language.fi,
                MessageThreadData(
                    id = MessageThreadId(UUID.randomUUID()),
                    type = MessageType.MESSAGE,
                    title = HtmlSafe("Ensi viikolla uimaan"),
                    urgent = false,
                    sensitive = false,
                    senderName = HtmlSafe("Kaisa Kasvattaja"),
                    senderType = AccountType.PERSONAL,
                    isCopy = false,
                ),
            ),
        ),
        Arguments.of(
            "pedagogicalDocumentNotification",
            emailMessageProvider.pedagogicalDocumentNotification(Language.fi, ChildId(UUID.randomUUID())),
        ),
        Arguments.of(
            "incomeNotification",
            emailMessageProvider.incomeNotification(IncomeNotificationType.INITIAL_EMAIL, Language.fi),
        ),
        Arguments.of(
            "incomeNotification",
            emailMessageProvider.incomeNotification(IncomeNotificationType.REMINDER_EMAIL, Language.fi),
        ),
        Arguments.of(
            "incomeNotification",
            emailMessageProvider.incomeNotification(IncomeNotificationType.EXPIRED_EMAIL, Language.fi),
        ),
        Arguments.of(
            "incomeNotification",
            emailMessageProvider.incomeNotification(IncomeNotificationType.NEW_CUSTOMER, Language.fi),
        ),
        Arguments.of(
            "calendarEventNotification",
            emailMessageProvider.calendarEventNotification(
                Language.fi,
                listOf(
                    CalendarEventNotificationData(HtmlSafe("Tapahtuma 1"), FiniteDateRange(LocalDate.of(2023, 8, 21), LocalDate.of(2023, 8, 21))),
                    CalendarEventNotificationData(HtmlSafe("Tapahtuma 2"), FiniteDateRange(LocalDate.of(2023, 8, 22), LocalDate.of(2023, 8, 23))),
                ),
            ),
        ),
        Arguments.of(
            "financeDecisionNotification FEE_DECISION",
            emailMessageProvider.financeDecisionNotification(
                FinanceDecisionType.FEE_DECISION,
            ),
        ),
        Arguments.of(
            "financeDecisionNotification VOUCHER_VALUE_DECISION",
            emailMessageProvider.financeDecisionNotification(
                FinanceDecisionType.VOUCHER_VALUE_DECISION,
            ),
        ),
    )
        .stream()

    private fun assertNotContainEspooText(message: String) {
        assertThat(message.also(::println))
            .isNotBlank
            .doesNotContainIgnoringCase("espoo")
            .doesNotContainIgnoringCase("esbo")
    }
}
