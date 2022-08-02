// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.email

import fi.espoo.evaka.emailclient.IEmailMessageProvider
import fi.espoo.evaka.shared.AssistanceNeedDecisionId
import fi.espoo.evaka.shared.ChildId
import fi.espoo.evaka.shared.DecisionId
import fi.tampere.trevaka.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junitpioneer.jupiter.cartesian.ArgumentSets
import org.junitpioneer.jupiter.cartesian.CartesianTest
import org.reflections.ReflectionUtils.*
import org.springframework.beans.factory.annotation.Autowired
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
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
        assertNotContainEspooText(
            emailMessageProvider.getAssistanceNeedDecisionEmailText(
                ChildId(UUID.randomUUID()),
                AssistanceNeedDecisionId(UUID.randomUUID())
            )
        )
        assertNotContainEspooText(
            emailMessageProvider.getAssistanceNeedDecisionEmailHtml(
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

    @CartesianTest
    @CartesianTest.MethodFactory("getPreschoolMethods")
    fun getPreschoolMessagesThrowError(method: Method, withinApplicationPeriod: Boolean) {
        val exception = assertThrows<InvocationTargetException> {
            method.invoke(emailMessageProvider, withinApplicationPeriod)
        }
        Assertions.assertEquals(Error::class.java, exception.cause?.javaClass)
        Assertions.assertEquals("Preschool not in use!", exception.cause?.message)
    }

    companion object {
        @JvmStatic
        fun getPreschoolMethods(): ArgumentSets {
            val preschoolMethods = getAllMethods(
                    IEmailMessageProvider::class.java,
                    withPrefix("getPreschool"),
                    withParametersAssignableTo(Boolean::class.java),
                    withReturnType(String::class.java))
            return ArgumentSets.create()
                .argumentsForNextParameter(preschoolMethods)
                .argumentsForNextParameter(true, false)
        }
    }
}