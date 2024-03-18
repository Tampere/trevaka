// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.lempaala.evaka.message.config

import fi.espoo.evaka.decision.DecisionSendAddress
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

internal const val PREFIX: String = "fi.lempaala.evaka.MessageProvider"

@Configuration
class MessageConfiguration {

    @Bean
    fun messageProvider(): IMessageProvider {
        val messageSource = YamlMessageSource(ClassPathResource("messages-lempaala.yaml"))
        return LempaalaMessageProvider(messageSource)
    }
}

internal class LempaalaMessageProvider(private val messageSource: MessageSource) : IMessageProvider {

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

    override fun getAssistanceNeedDecisionHeader(lang: MessageLanguage): String =
        messageSource.getMessage("$PREFIX.ASSISTANCE_NEED_DECISION_HEADER", null, resolveLocale(lang))

    override fun getAssistanceNeedDecisionContent(lang: MessageLanguage): String =
        messageSource.getMessage("$PREFIX.ASSISTANCE_NEED_DECISION_CONTENT", null, resolveLocale(lang))

    override fun getAssistanceNeedPreschoolDecisionHeader(lang: MessageLanguage): String =
        getAssistanceNeedDecisionHeader(lang)

    override fun getAssistanceNeedPreschoolDecisionContent(lang: MessageLanguage): String =
        getAssistanceNeedDecisionContent(lang)

    override fun getDefaultDecisionAddress(lang: MessageLanguage): DecisionSendAddress = when (lang) {
        MessageLanguage.FI, MessageLanguage.SV -> DecisionSendAddress(
            street = "PL 36",
            postalCode = "37501",
            postOffice = "Lempäälä",
            row1 = "Lempäälän kunta",
            row2 = "Varhaiskasvatuspalvelut",
            row3 = "",
        )
    }

    override fun getDefaultFinancialDecisionAddress(lang: MessageLanguage): DecisionSendAddress =
        getDefaultDecisionAddress(lang)

    private fun resolveLocale(lang: MessageLanguage): Locale {
        if (MessageLanguage.SV == lang) return resolveLocale(MessageLanguage.FI)
        return Locale.of(lang.name.lowercase())
    }
}

internal class YamlMessageSource(resource: Resource) : AbstractMessageSource() {

    private val properties: Properties = YamlPropertiesFactoryBean().apply {
        setResources(resource)
        afterPropertiesSet()
    }.`object`!!

    override fun resolveCode(code: String, locale: Locale): MessageFormat? =
        properties.getProperty("$code.${locale.language.lowercase()}")?.let { MessageFormat(it, locale) }
}
