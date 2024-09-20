// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.lempaala.evaka.pdfgen

import fi.espoo.evaka.application.ServiceNeed
import fi.espoo.evaka.application.ServiceNeedOption
import fi.espoo.evaka.assistanceneed.decision.AssistanceNeedDecisionService
import fi.espoo.evaka.assistanceneed.preschooldecision.AssistanceNeedPreschoolDecisionService
import fi.espoo.evaka.daycare.domain.ProviderType
import fi.espoo.evaka.daycare.service.DaycareManager
import fi.espoo.evaka.decision.DecisionType
import fi.espoo.evaka.invoicing.domain.FeeDecisionType
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionType
import fi.espoo.evaka.invoicing.service.FeeDecisionPdfData
import fi.espoo.evaka.invoicing.service.VoucherValueDecisionPdfData
import fi.espoo.evaka.pdfgen.PdfGenerator
import fi.espoo.evaka.placement.PlacementType
import fi.espoo.evaka.setting.SettingType
import fi.espoo.evaka.shared.ServiceNeedOptionId
import fi.espoo.evaka.shared.domain.OfficialLanguage
import fi.espoo.evaka.shared.template.ITemplateProvider
import fi.lempaala.evaka.AbstractLempaalaIntegrationTest
import fi.tampere.trevaka.assistanceneed.decision.validAssistanceNeedDecision
import fi.tampere.trevaka.assistanceneed.decision.validAssistanceNeedPreschoolDecision
import fi.tampere.trevaka.decision.service.validChild
import fi.tampere.trevaka.decision.service.validDecision
import fi.tampere.trevaka.decision.service.validDecisionUnit
import fi.tampere.trevaka.invoice.service.validFeeDecision
import fi.tampere.trevaka.invoice.service.validFeeDecisionChild
import fi.tampere.trevaka.invoice.service.validVoucherValueDecision
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junitpioneer.jupiter.cartesian.ArgumentSets
import org.junitpioneer.jupiter.cartesian.CartesianTest
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.util.UUID

private val settings = emptyMap<SettingType, String>()

class LempaalaPdfGeneratorTest : AbstractLempaalaIntegrationTest() {

    @Autowired
    private lateinit var templateProvider: ITemplateProvider

    @Autowired
    private lateinit var pdfGenerator: PdfGenerator

    @Autowired
    private lateinit var daycareAssistanceDecisionService: AssistanceNeedDecisionService

    @Autowired
    private lateinit var preschoolAssistanceDecisionService: AssistanceNeedPreschoolDecisionService

    private fun createDecisionPdfValues() = ArgumentSets.argumentsForFirstParameter(supportedDecisionTypes())
        .argumentsForNextParameter(supportedProviderTypes())
        .argumentsForNextParameter(listOf(false, true))

    @CartesianTest
    @CartesianTest.MethodFactory("createDecisionPdfValues")
    fun createDecisionPdf(decisionType: DecisionType, providerType: ProviderType, isTransferApplication: Boolean) {
        val bytes = fi.espoo.evaka.decision.createDecisionPdf(
            templateProvider,
            pdfGenerator,
            settings,
            validDecision(decisionType, validDecisionUnit(providerType)),
            child = validChild(),
            isTransferApplication = isTransferApplication,
            serviceNeed = when (decisionType) {
                DecisionType.CLUB -> null
                else -> ServiceNeed(
                    startTime = "08:00",
                    endTime = "16:00",
                    shiftCare = false,
                    partTime = false,
                    ServiceNeedOption(
                        ServiceNeedOptionId(UUID.randomUUID()),
                        "Palveluntarve 1",
                        "Palveluntarve 1",
                        "Palveluntarve 1",
                        null,
                    ),
                )
            },
            lang = OfficialLanguage.FI,
            DaycareManager("Päivi Päiväkodinjohtaja", "paivi.paivakodinjohtaja@example.com", "0451231234"),
        )

        val filename = "lempaala-decision-$decisionType-$providerType${if (isTransferApplication) "-transfer" else ""}.pdf"
        writeReportsFile(filename, bytes)
    }

    @ParameterizedTest
    @MethodSource("supportedPlacementTypes")
    fun generateFeeDecisionPdf(placementType: PlacementType) {
        val decision = validFeeDecision(children = listOf(validFeeDecisionChild().copy(placementType = placementType)))

        val bytes = pdfGenerator.generateFeeDecisionPdf(FeeDecisionPdfData(decision, settings, OfficialLanguage.FI))

        val filename = "lempaala-fee-decision-with-placement-type-$placementType.pdf"
        writeReportsFile(filename, bytes)
    }

    @ParameterizedTest
    @MethodSource("supportedFeeDecisionTypes")
    fun generateFeeDecisionPdf(decisionType: FeeDecisionType) {
        val decision = validFeeDecision().copy(decisionType = decisionType)

        val bytes = pdfGenerator.generateFeeDecisionPdf(FeeDecisionPdfData(decision, settings, OfficialLanguage.FI))

        val filename = "lempaala-fee-decision-with-decision-type-$decisionType.pdf"
        writeReportsFile(filename, bytes)
    }

    @ParameterizedTest
    @MethodSource("supportedVoucherValueDecisionTypes")
    fun generateVoucherValueDecisionPdf(decisionType: VoucherValueDecisionType) {
        val decision = validVoucherValueDecision().copy(decisionType = decisionType)

        val bytes = pdfGenerator.generateVoucherValueDecisionPdf(VoucherValueDecisionPdfData(decision, settings, OfficialLanguage.FI))

        val filename = "lempaala-voucher-value-decision-with-decision-type-$decisionType.pdf"
        writeReportsFile(filename, bytes)
    }

    @Test
    fun daycareAssistanceDecisionGeneratePdf() {
        val decision = validAssistanceNeedDecision

        val bytes = daycareAssistanceDecisionService.generatePdf(
            sentDate = LocalDate.of(2022, 9, 12),
            decision = decision,
        )

        val filename = "lempaala-daycare-assistance-decision.pdf"
        writeReportsFile(filename, bytes)
    }

    @Test
    fun preschoolAssistanceDecisionGeneratePdf() {
        val decision = validAssistanceNeedPreschoolDecision

        val bytes = preschoolAssistanceDecisionService.generatePdf(
            sentDate = LocalDate.of(2022, 9, 12),
            decision = decision,
            validTo = null,
        )

        val filename = "lempaala-preschool-assistance-decision.pdf"
        writeReportsFile(filename, bytes)
    }
}
