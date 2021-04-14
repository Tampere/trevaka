package fi.tampere.trevaka.message

import fi.espoo.evaka.shared.message.IMessageProvider
import fi.espoo.evaka.shared.message.MessageLanguage
import fi.espoo.evaka.shared.message.MessageType
import fi.tampere.trevaka.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junitpioneer.jupiter.CartesianEnumSource
import org.junitpioneer.jupiter.CartesianProductTest
import org.springframework.beans.factory.annotation.Autowired

internal class MessageProviderTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var messageProvider: IMessageProvider

    @CartesianProductTest
    @CartesianEnumSource.CartesianEnumSources(
        value = [CartesianEnumSource(MessageType::class), CartesianEnumSource(MessageLanguage::class)]
    )
    fun `get works for every message type and language`(type: MessageType, lang: MessageLanguage) {
        assertThat(messageProvider.get(type, lang).also(::println))
            .isNotBlank
            .doesNotContainIgnoringCase("espoo")
            .doesNotContainIgnoringCase("esbo")
    }

}
