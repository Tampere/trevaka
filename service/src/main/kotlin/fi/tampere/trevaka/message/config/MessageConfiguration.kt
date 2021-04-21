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

internal const val PREFIX: String = "fi.tampere.trevaka.MessageProvider"

@Configuration
class MessageConfiguration {

    @Bean
    fun messageProvider(): IMessageProvider {
        val messageSource = YamlMessageSource(ClassPathResource("messages.yaml"))
        return TrevakaMessageProvider(messageSource)
    }

}

internal class TrevakaMessageProvider(val messageSource: MessageSource) : IMessageProvider {
    override fun getDecisionHeader(lang: MessageLanguage): String =
        messageSource.getMessage("$PREFIX.DECISION_HEADER", null, resolveLocale(lang))

    override fun getDecisionContent(lang: MessageLanguage): String =
        messageSource.getMessage("$PREFIX.DECISION_CONTENT", null, resolveLocale(lang))

    override fun getFeeDecisionHeader(lang: MessageLanguage): String =
        messageSource.getMessage("$PREFIX.FEE_DECISION_HEADER", null, resolveLocale(lang))

    override fun getFeeDecisionContent(lang: MessageLanguage): String =
        messageSource.getMessage("$PREFIX.FEE_DECISION_CONTENT", null, resolveLocale(lang))

    override fun getVoucherValueDecisionHeader(lang: MessageLanguage): String =
        messageSource.getMessage("$PREFIX.VOUCHER_VALUE_DECISION_HEADER", null, resolveLocale(lang))

    override fun getVoucherValueDecisionContent(lang: MessageLanguage): String =
        messageSource.getMessage("$PREFIX.VOUCHER_VALUE_DECISION_CONTENT", null, resolveLocale(lang))

    private fun resolveLocale(lang: MessageLanguage) = Locale(lang.name.toLowerCase())
}

internal class YamlMessageSource(resource: Resource) : AbstractMessageSource() {

    private val properties: Properties = YamlPropertiesFactoryBean().apply {
        setResources(resource)
        afterPropertiesSet()
    }.`object`!!

    override fun resolveCode(code: String, locale: Locale): MessageFormat? =
        properties.getProperty("$code.${locale.language.toLowerCase()}")?.let { MessageFormat(it, locale) }

}
