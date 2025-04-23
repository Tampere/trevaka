// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.message

import fi.espoo.evaka.shared.domain.OfficialLanguage
import fi.espoo.evaka.shared.message.IMessageProvider
import fi.tampere.trevaka.AbstractTampereIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junitpioneer.jupiter.cartesian.ArgumentSets
import org.junitpioneer.jupiter.cartesian.CartesianTest
import org.springframework.beans.factory.annotation.Autowired
import java.lang.reflect.Method
import kotlin.reflect.full.functions
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.javaMethod

internal class MessageProviderTest : AbstractTampereIntegrationTest() {

    @Autowired
    private lateinit var messageProvider: IMessageProvider

    @CartesianTest
    @CartesianTest.MethodFactory("methodsWithLang")
    fun `get works for every message type and language`(method: Method, lang: OfficialLanguage) {
        assertThat(((method.invoke(messageProvider, lang)) as String).also(::println))
            .isNotBlank
            .doesNotContainIgnoringCase("espoo")
            .doesNotContainIgnoringCase("esbo")
    }

    companion object {
        @JvmStatic
        fun methodsWithLang(): ArgumentSets {
            val allMethods = IMessageProvider::class.functions
                .filter { it.valueParameters.map { param -> param.type.classifier } == listOf(OfficialLanguage::class) }
                .filter { it.returnType.classifier == String::class }
                .filterNot { it.name.startsWith("getPlacementTool") }
                .map { it.javaMethod }
            return ArgumentSets.create()
                .argumentsForNextParameter(allMethods)
                .argumentsForNextParameter(OfficialLanguage.entries)
        }
    }
}
