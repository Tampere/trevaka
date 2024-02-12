// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.hameenkyro.evaka.message.config

import fi.espoo.evaka.shared.message.IMessageProvider
import fi.espoo.evaka.shared.message.MessageLanguage
import fi.hameenkyro.evaka.AbstractHameenkyroIntegrationTest
import org.assertj.core.api.Assertions
import org.junitpioneer.jupiter.cartesian.ArgumentSets
import org.junitpioneer.jupiter.cartesian.CartesianTest
import org.reflections.ReflectionUtils.getAllMethods
import org.reflections.ReflectionUtils.withParametersAssignableTo
import org.reflections.ReflectionUtils.withReturnType
import org.springframework.beans.factory.annotation.Autowired
import java.lang.reflect.Method

class HameenkyroMessageProviderTest : AbstractHameenkyroIntegrationTest() {

    @Autowired
    private lateinit var messageProvider: IMessageProvider

    @CartesianTest
    @CartesianTest.MethodFactory("methodsWithLang")
    fun `get works for every message type and language`(method: Method, lang: MessageLanguage) {
        Assertions.assertThat(((method.invoke(messageProvider, lang)) as String).also(::println))
            .isNotBlank
            .doesNotContainIgnoringCase("espoo")
            .doesNotContainIgnoringCase("esbo")
    }

    companion object {
        @JvmStatic
        fun methodsWithLang(): ArgumentSets {
            val allMethods = getAllMethods(
                IMessageProvider::class.java,
                withParametersAssignableTo(MessageLanguage::class.java),
                withReturnType(String::class.java),
            )
            return ArgumentSets.create()
                .argumentsForNextParameter(allMethods)
                .argumentsForNextParameter(MessageLanguage.entries)
        }
    }
}
