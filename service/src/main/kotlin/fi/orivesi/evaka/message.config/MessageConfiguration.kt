// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.orivesi.evaka.message.config

import fi.espoo.evaka.decision.DecisionSendAddress
import fi.espoo.evaka.shared.domain.OfficialLanguage
import fi.espoo.evaka.shared.message.IMessageProvider
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

internal const val PREFIX: String = "fi.orivesi.evaka.MessageProvider"

@Configuration
class MessageConfiguration {

    @Bean
    fun messageProvider(): IMessageProvider {
        val messageSource = YamlMessageSource(ClassPathResource("messages-orivesi.yaml"))
        return OrivesiMessageProvider(messageSource)
    }
}

internal class OrivesiMessageProvider(private val messageSource: MessageSource) : IMessageProvider {

    override fun getDecisionHeader(lang: OfficialLanguage): String = messageSource.getMessage("$PREFIX.DECISION_HEADER", null, resolveLocale(lang))

    override fun getDecisionContent(lang: OfficialLanguage): String = messageSource.getMessage("$PREFIX.DECISION_CONTENT", null, resolveLocale(lang))

    override fun getFeeDecisionHeader(lang: OfficialLanguage): String = messageSource.getMessage("$PREFIX.FEE_DECISION_HEADER", null, resolveLocale(lang))

    override fun getFeeDecisionContent(lang: OfficialLanguage): String = messageSource.getMessage("$PREFIX.FEE_DECISION_CONTENT", null, resolveLocale(lang))

    override fun getVoucherValueDecisionHeader(lang: OfficialLanguage): String = messageSource.getMessage("$PREFIX.VOUCHER_VALUE_DECISION_HEADER", null, resolveLocale(lang))

    override fun getVoucherValueDecisionContent(lang: OfficialLanguage): String = messageSource.getMessage("$PREFIX.VOUCHER_VALUE_DECISION_CONTENT", null, resolveLocale(lang))

    override fun getAssistanceNeedDecisionHeader(lang: OfficialLanguage): String = messageSource.getMessage("$PREFIX.ASSISTANCE_NEED_DECISION_HEADER", null, resolveLocale(lang))

    override fun getAssistanceNeedDecisionContent(lang: OfficialLanguage): String = messageSource.getMessage("$PREFIX.ASSISTANCE_NEED_DECISION_CONTENT", null, resolveLocale(lang))

    override fun getAssistanceNeedPreschoolDecisionHeader(lang: OfficialLanguage): String = getAssistanceNeedDecisionHeader(lang)

    override fun getAssistanceNeedPreschoolDecisionContent(lang: OfficialLanguage): String = getAssistanceNeedDecisionContent(lang)

    override fun getDefaultDecisionAddress(lang: OfficialLanguage): DecisionSendAddress = when (lang) {
        OfficialLanguage.FI, OfficialLanguage.SV -> DecisionSendAddress(
            street = "PL 7",
            postalCode = "35301",
            postOffice = "Orivesi",
            row1 = "Varhaiskasvatuspalvelut",
            row2 = "",
            row3 = "",
        )
    }

    override fun getDefaultFinancialDecisionAddress(lang: OfficialLanguage): DecisionSendAddress = getDefaultDecisionAddress(lang)

    override fun getPlacementToolHeader(lang: OfficialLanguage): String = "Esitäytetty hakemus esiopetukseen"

    override fun getPlacementToolContent(lang: OfficialLanguage): String = """
Olemme tehneet lapsellenne esitäytetyn hakemuksen esiopetukseen. Hakemus on tehty lapsen oppilaaksiottoalueen mukaiseen esiopetusyksikköön.

Mikäli haluatte hakeutua muuhun kuin lapsellenne osoitettuun paikkaan, voitte muokata hakemusta eVakassa.

Jos taas hyväksytte osoitetun esiopetuspaikan, teidän ei tarvitse tehdä mitään.

In English:

We have made a pre-filled application for preschool education for your child. The application has been submitted to the pre-school unit according to the child's pupil enrollment area.

If you want to apply for a place other than the one assigned to your child, you can edit the application in eVaka.

If you accept the assigned pre-school place, you don't have to do anything.
    """.trimIndent()

    private fun resolveLocale(lang: OfficialLanguage): Locale {
        if (OfficialLanguage.SV == lang) return resolveLocale(OfficialLanguage.FI)
        return Locale.of(lang.name.lowercase())
    }
}

internal class YamlMessageSource(resource: Resource) : AbstractMessageSource() {

    private val properties: Properties = YamlPropertiesFactoryBean().apply {
        setResources(resource)
        afterPropertiesSet()
    }.`object`!!

    override fun resolveCode(code: String, locale: Locale): MessageFormat? = properties.getProperty("$code.${locale.language.lowercase()}")?.let { MessageFormat(it, locale) }
}
