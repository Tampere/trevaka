package fi.tampere.trevaka.email

import fi.espoo.evaka.emailclient.IEmailMessageProvider
import fi.tampere.trevaka.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junitpioneer.jupiter.CartesianProductTest
import org.reflections.ReflectionUtils.*
import org.springframework.beans.factory.annotation.Autowired
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

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
    }

    private fun assertNotContainEspooText(message: String) {
        assertThat(message.also(::println))
            .isNotBlank
            .doesNotContainIgnoringCase("espoo")
            .doesNotContainIgnoringCase("esbo")
    }

    @CartesianProductTest(factory = "getPreschoolMethods")
    fun getPreschoolMessagesThrowError(method: Method, withinApplicationPeriod: Boolean) {
        val exception = assertThrows<InvocationTargetException> {
            method.invoke(emailMessageProvider, withinApplicationPeriod)
        }
        Assertions.assertEquals(Error::class.java, exception.cause?.javaClass)
        Assertions.assertEquals("Preschool not in use!", exception.cause?.message)
    }

    companion object {
        @JvmStatic
        fun getPreschoolMethods(): CartesianProductTest.Sets {
            val preschoolMethods = getAllMethods(
                    IEmailMessageProvider::class.java,
                    withPrefix("getPreschool"),
                    withParametersAssignableTo(Boolean::class.java),
                    withReturnType(String::class.java))
            return CartesianProductTest.Sets()
                    .addAll(preschoolMethods)
                    .add(true, false)
        }
    }
}