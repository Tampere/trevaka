package fi.tampere.trevaka.message

import fi.espoo.evaka.shared.message.IMessageProvider
import fi.espoo.evaka.shared.message.MessageLanguage
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.AbstractMessageSource
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import java.text.MessageFormat
import java.util.Locale
import java.util.Properties

@Configuration
class MessageConfiguration {

    @Bean
    fun messageProvider(): IMessageProvider {
        val messageSource = YamlMessageSource(ClassPathResource("messages.yaml"))
        return TrevakaMessageProvider(messageSource)
    }

}

internal class TrevakaMessageProvider(val messageSource: MessageSource) : IMessageProvider {
    fun get(type: MessageType, lang: MessageLanguage) =
        messageSource.getMessage("fi.tampere.trevaka.MessageProvider.$type", null, resolveLocale(lang))

    override fun getDecisionHeader(lang: MessageLanguage): String = get(MessageType.DECISION_HEADER, lang)

    override fun getDecisionContent(lang: MessageLanguage): String = get(MessageType.DECISION_CONTENT, lang)

    override fun getFeeDecisionHeader(lang: MessageLanguage): String = get(MessageType.FEE_DECISION_HEADER, lang)

    override fun getFeeDecisionContent(lang: MessageLanguage): String = get(MessageType.FEE_DECISION_CONTENT, lang)

    override fun getVoucherValueDecisionHeader(lang: MessageLanguage): String =
        get(MessageType.VOUCHER_VALUE_DECISION_HEADER, lang)

    override fun getVoucherValueDecisionContent(lang: MessageLanguage): String =
        get(MessageType.VOUCHER_VALUE_DECISION_CONTENT, lang)

    private fun resolveLocale(lang: MessageLanguage) = Locale(lang.name.toLowerCase())
}

enum class MessageType {
    DECISION_HEADER,
    DECISION_CONTENT,
    FEE_DECISION_HEADER,
    FEE_DECISION_CONTENT,
    VOUCHER_VALUE_DECISION_HEADER,
    VOUCHER_VALUE_DECISION_CONTENT,
}

internal class YamlMessageSource(resource: Resource) : AbstractMessageSource() {

    private val properties: Properties = YamlPropertiesFactoryBean().apply {
        setResources(resource)
        afterPropertiesSet()
    }.`object`!!

    override fun resolveCode(code: String, locale: Locale): MessageFormat? =
        properties.getProperty("$code.${locale.language.toLowerCase()}")?.let { MessageFormat(it, locale) }

}
