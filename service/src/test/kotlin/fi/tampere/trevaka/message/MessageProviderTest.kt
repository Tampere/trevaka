// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.message

import fi.espoo.evaka.shared.message.IMessageProvider
import fi.espoo.evaka.shared.message.MessageLanguage
import fi.tampere.trevaka.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junitpioneer.jupiter.cartesian.ArgumentSets
import org.junitpioneer.jupiter.cartesian.CartesianTest
import org.reflections.ReflectionUtils.*
import org.springframework.beans.factory.annotation.Autowired
import java.lang.reflect.Method

internal class MessageProviderTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var messageProvider: IMessageProvider

    @CartesianTest
    @CartesianTest.MethodFactory("methodsWithLang")
    fun `get works for every message type and language`(method: Method, lang: MessageLanguage) {
        assertThat(((method.invoke(messageProvider, lang)) as String).also(::println))
            .isNotBlank
            .doesNotContainIgnoringCase("espoo")
            .doesNotContainIgnoringCase("esbo")
    }

    companion object {
        @JvmStatic
        fun methodsWithLang(): ArgumentSets {
            val allMethods = getAllMethods(
                IMessageProvider::class.java,
                withParametersAssignableTo(MessageLanguage::class.java), withReturnType(String::class.java)
            )
            return ArgumentSets.create()
                .argumentsForNextParameter(allMethods)
                .argumentsForNextParameter(MessageLanguage.values().toList())
        }
    }

}
