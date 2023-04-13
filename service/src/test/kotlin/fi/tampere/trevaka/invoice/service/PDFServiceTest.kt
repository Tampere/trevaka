// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.invoice.service

import fi.espoo.evaka.invoicing.domain.DecisionIncome
import fi.espoo.evaka.invoicing.domain.EmployeeWithName
import fi.espoo.evaka.invoicing.domain.FeeAlteration
import fi.espoo.evaka.invoicing.domain.FeeAlterationWithEffect
import fi.espoo.evaka.invoicing.domain.FeeDecisionChildDetailed
import fi.espoo.evaka.invoicing.domain.FeeDecisionDetailed
import fi.espoo.evaka.invoicing.domain.FeeDecisionStatus
import fi.espoo.evaka.invoicing.domain.FeeDecisionThresholds
import fi.espoo.evaka.invoicing.domain.FeeDecisionType
import fi.espoo.evaka.invoicing.domain.IncomeEffect
import fi.espoo.evaka.invoicing.domain.PersonDetailed
import fi.espoo.evaka.invoicing.domain.UnitData
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionDetailed
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionPlacementDetailed
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionServiceNeed
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionStatus
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionType
import fi.espoo.evaka.invoicing.service.DocumentLang
import fi.espoo.evaka.invoicing.service.FeeDecisionPdfData
import fi.espoo.evaka.invoicing.service.VoucherValueDecisionPdfData
import fi.espoo.evaka.pdfgen.Page
import fi.espoo.evaka.pdfgen.PdfGenerator
import fi.espoo.evaka.pdfgen.Template
import fi.espoo.evaka.placement.PlacementType
import fi.espoo.evaka.setting.SettingType
import fi.espoo.evaka.shared.AreaId
import fi.espoo.evaka.shared.DaycareId
import fi.espoo.evaka.shared.EmployeeId
import fi.espoo.evaka.shared.FeeDecisionId
import fi.espoo.evaka.shared.PersonId
import fi.espoo.evaka.shared.VoucherValueDecisionId
import fi.espoo.evaka.shared.domain.DateRange
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.tampere.trevaka.AbstractIntegrationTest
import fi.tampere.trevaka.reportsPath
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.beans.factory.annotation.Autowired
import org.thymeleaf.context.Context
import java.io.FileOutputStream
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

private val settings = mapOf(
    SettingType.DECISION_MAKER_NAME to "Paula Palvelupäällikkö",
    SettingType.DECISION_MAKER_TITLE to "Asiakaspalvelupäällikkö",
)

internal class PDFServiceTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var pdfGenerator: PdfGenerator

    @Test
    fun render() {
        pdfGenerator.render(Page(Template("test"), Context()))
    }

    @Test
    fun generateFeeDecisionPdf() {
        val decision = validFeeDecision()

        val bytes = pdfGenerator.generateFeeDecisionPdf(FeeDecisionPdfData(decision, settings, DocumentLang.FI))

        val filepath = "$reportsPath/PDFServiceTest-fee-decision.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateFeeDecisionPdfWithIncome() {
        val decision = validFeeDecision().copy(headOfFamilyIncome = testDecisionIncome)

        val bytes = pdfGenerator.generateFeeDecisionPdf(FeeDecisionPdfData(decision, settings, DocumentLang.FI))

        val filepath = "$reportsPath/PDFServiceTest-fee-decision-head-of-family-income.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @ParameterizedTest
    @EnumSource(FeeDecisionType::class)
    fun generateFeeDecisionPdfType(decisionType: FeeDecisionType) {
        val decision = validFeeDecision().copy(decisionType = decisionType)

        val bytes = pdfGenerator.generateFeeDecisionPdf(FeeDecisionPdfData(decision, settings, DocumentLang.FI))

        val filepath = "$reportsPath/PDFServiceTest-fee-decision-type-$decisionType.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateFeeDecisionPdfPartner() {
        val decision = validFeeDecision().copy(
            partner = PersonDetailed(
                PersonId(UUID.randomUUID()), LocalDate.of(1980, 6, 14), null,
                "Mikko", "Meikäläinen",
                "140680-9239", "", "", "",
                "", null, "", null, restrictedDetailsEnabled = false,
            ),
            partnerIsCodebtor = true,
        )

        val bytes = pdfGenerator.generateFeeDecisionPdf(FeeDecisionPdfData(decision, settings, DocumentLang.FI))

        val filepath = "$reportsPath/PDFServiceTest-fee-decision-partner.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateFeeDecisionPdfPartnerIncome() {
        val decision = validFeeDecision().copy(
            headOfFamilyIncome = validDecisionIncome(314100),
            partner = PersonDetailed(
                PersonId(UUID.randomUUID()), LocalDate.of(1980, 6, 14), null,
                "Mikko", "Meikäläinen",
                "140680-9239", "", "", "",
                "", null, "", null, restrictedDetailsEnabled = false,
            ),
            partnerIncome = validDecisionIncome(214000),
            partnerIsCodebtor = true,
        )

        val bytes = pdfGenerator.generateFeeDecisionPdf(FeeDecisionPdfData(decision, settings, DocumentLang.FI))

        val filepath = "$reportsPath/PDFServiceTest-fee-decision-partner-income.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateFeeDecisionPdfChildIncome() {
        val decision = validFeeDecision().copy(
            children = listOf(validFeeDecisionChild().copy(childIncome = testDecisionIncome)),
        )

        val bytes = pdfGenerator.generateFeeDecisionPdf(FeeDecisionPdfData(decision, settings, DocumentLang.FI))

        val filepath = "$reportsPath/PDFServiceTest-fee-decision-child-income.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateFeeDecisionPdfIncomes() {
        val decision = validFeeDecision().copy(
            headOfFamilyIncome = validDecisionIncome(income = 300000),
            partner = PersonDetailed(
                PersonId(UUID.randomUUID()), LocalDate.of(1982, 6, 25), null,
                "Mikko", "Meikäläinen",
                "250682-983U", "Meikäläisenkuja 6 B 7", "33730", "TAMPERE",
                "", null, "", null, restrictedDetailsEnabled = false,
            ),
            partnerIncome = validDecisionIncome(income = 200000),
            children = listOf(
                validFeeDecisionChild().copy(
                    child = PersonDetailed(
                        PersonId(UUID.randomUUID()), LocalDate.of(2018, 1, 1), null,
                        "Matti", "Meikäläinen",
                        null, "", "", "",
                        "", null, "", null, restrictedDetailsEnabled = false,
                    ),
                    childIncome = validDecisionIncome(income = 10000),
                ),
                validFeeDecisionChild().copy(
                    child = PersonDetailed(
                        PersonId(UUID.randomUUID()), LocalDate.of(2018, 1, 1), null,
                        "Marko", "Meikäläinen",
                        null, "", "", "",
                        "", null, "", null, restrictedDetailsEnabled = false,
                    ),
                ),
                validFeeDecisionChild().copy(
                    child = PersonDetailed(
                        PersonId(UUID.randomUUID()), LocalDate.of(2018, 1, 1), null,
                        "Miia", "Meikäläinen",
                        null, "", "", "",
                        "", null, "", null, restrictedDetailsEnabled = false,
                    ),
                    childIncome = validDecisionIncome(income = 25000),
                ),
            ),
        )

        val bytes = pdfGenerator.generateFeeDecisionPdf(FeeDecisionPdfData(decision, settings, DocumentLang.FI))

        val filepath = "$reportsPath/PDFServiceTest-fee-decision-incomes.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateFeeDecisionPdfValidTo() {
        val validTo = LocalDate.now().minusDays(1)
        val validFrom = validTo.minusYears(1)
        val decision = validFeeDecision().copy(validDuring = DateRange(validFrom, validTo))

        val bytes = pdfGenerator.generateFeeDecisionPdf(FeeDecisionPdfData(decision, settings, DocumentLang.FI))

        val filepath = "$reportsPath/PDFServiceTest-fee-decision-valid-to.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateFeeDecisionPdfEmptyAddress() {
        val decision = validFeeDecision().copy(
            headOfFamily = PersonDetailed(
                PersonId(UUID.randomUUID()), LocalDate.of(1982, 3, 31), null,
                "Maija", "Meikäläinen",
                "310382-956D", "", "", "",
                "", null, "", null, restrictedDetailsEnabled = false,
            ),
        )

        val bytes = pdfGenerator.generateFeeDecisionPdf(FeeDecisionPdfData(decision, settings, DocumentLang.FI))

        val filepath = "$reportsPath/PDFServiceTest-fee-decision-empty-address.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateVoucherValueDecisionPdf() {
        val decision = validVoucherValueDecision()
        val data = VoucherValueDecisionPdfData(decision, settings, DocumentLang.FI)

        val bytes = pdfGenerator.generateVoucherValueDecisionPdf(data)

        val filepath = "$reportsPath/PDFServiceTest-voucher-value-decision.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateVoucherValueDecisionPdfWithIncome() {
        val decision = validVoucherValueDecision().copy(headOfFamilyIncome = testDecisionIncome)
        val data = VoucherValueDecisionPdfData(decision, settings, DocumentLang.FI)

        val bytes = pdfGenerator.generateVoucherValueDecisionPdf(data)

        val filepath = "$reportsPath/PDFServiceTest-voucher-value-decision-head-of-family-income.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateVoucherValueDecisionPdfWithChildIncome() {
        val decision = validVoucherValueDecision().copy(childIncome = testDecisionIncome)
        val data = VoucherValueDecisionPdfData(decision, settings, DocumentLang.FI)

        val bytes = pdfGenerator.generateVoucherValueDecisionPdf(data)

        val filepath = "$reportsPath/PDFServiceTest-voucher-value-decision-child-income.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateVoucherValueDecisionPdfPartner() {
        val decision = validVoucherValueDecision().copy(
            partner = PersonDetailed(
                PersonId(UUID.randomUUID()), LocalDate.of(1980, 6, 14), null,
                "Mikko", "Meikäläinen",
                "140680-9239", "", "", "",
                "", null, "", null, restrictedDetailsEnabled = false,
            ),
            partnerIsCodebtor = true,
        )
        val data = VoucherValueDecisionPdfData(decision, settings, DocumentLang.FI)

        val bytes = pdfGenerator.generateVoucherValueDecisionPdf(data)

        val filepath = "$reportsPath/PDFServiceTest-voucher-value-decision-partner.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateVoucherValueDecisionPdfValidTo() {
        val validTo = LocalDate.now().minusDays(1)
        val validFrom = validTo.minusYears(1)
        val decision = validVoucherValueDecision().copy(validFrom = validFrom, validTo = validTo)
        val data = VoucherValueDecisionPdfData(decision, settings, DocumentLang.FI)

        val bytes = pdfGenerator.generateVoucherValueDecisionPdf(data)

        val filepath = "$reportsPath/PDFServiceTest-voucher-value-decision-valid-to.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateVoucherValueDecisionPdfEmptyAddress() {
        val decision = validVoucherValueDecision().copy(
            headOfFamily = PersonDetailed(
                PersonId(UUID.randomUUID()), LocalDate.of(1982, 3, 31), null,
                "Maija", "Meikäläinen",
                "310382-956D", "", "", "",
                "", null, "", null, restrictedDetailsEnabled = false,
            ),
        )
        val data = VoucherValueDecisionPdfData(decision, settings, DocumentLang.FI)

        val bytes = pdfGenerator.generateVoucherValueDecisionPdf(data)

        val filepath = "$reportsPath/PDFServiceTest-voucher-value-decision-empty-address.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateReliefAcceptedVoucherValueDecisionPdfValidTo() {
        val decision = validVoucherValueDecision().copy(
            validTo = LocalDate.now().plusYears(1),
            decisionType = VoucherValueDecisionType.RELIEF_ACCEPTED,
            feeAlterations = listOf(
                FeeAlterationWithEffect(FeeAlteration.Type.RELIEF, 50, false, -10800),
            ),
        )
        val data = VoucherValueDecisionPdfData(decision, settings, DocumentLang.FI)

        val bytes = pdfGenerator.generateVoucherValueDecisionPdf(data)

        val filepath = "$reportsPath/PDFServiceTest-relief-accepted-voucher-value-decision-valid-to.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateReliefPartlyAcceptedVoucherValueDecisionPdfValidTo() {
        val decision = validVoucherValueDecision().copy(
            validTo = LocalDate.now().plusYears(1),
            decisionType = VoucherValueDecisionType.RELIEF_PARTLY_ACCEPTED,
            feeAlterations = listOf(
                FeeAlterationWithEffect(FeeAlteration.Type.RELIEF, 50, false, -100),
            ),
        )
        val data = VoucherValueDecisionPdfData(decision, settings, DocumentLang.FI)

        val bytes = pdfGenerator.generateVoucherValueDecisionPdf(data)

        val filepath = "$reportsPath/PDFServiceTest-relief-partly-accepted-voucher-value-decision-valid-to.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateReliefRejectedVoucherValueDecisionPdfValidTo() {
        val decision = validVoucherValueDecision().copy(
            validTo = LocalDate.now().plusYears(1),
            decisionType = VoucherValueDecisionType.RELIEF_REJECTED,
        )
        val data = VoucherValueDecisionPdfData(decision, settings, DocumentLang.FI)

        val bytes = pdfGenerator.generateVoucherValueDecisionPdf(data)

        val filepath = "$reportsPath/PDFServiceTest-relief-rejected-voucher-value-decision-valid-to.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }
}

private val testDecisionIncome = DecisionIncome(
    effect = IncomeEffect.INCOME,
    data = mapOf("MAIN_INCOME" to 314100),
    totalIncome = 314100,
    totalExpenses = 0,
    total = 314100,
    worksAtECHA = false,
)

private fun validDecisionIncome(income: Int = 314100) = DecisionIncome(
    effect = IncomeEffect.INCOME,
    data = mapOf("MAIN_INCOME" to income),
    totalIncome = income,
    totalExpenses = 0,
    total = income,
    worksAtECHA = false,
)

private fun validFeeDecision() = FeeDecisionDetailed(
    FeeDecisionId(UUID.randomUUID()),
    children = listOf(validFeeDecisionChild()),
    validDuring = DateRange(
        LocalDate.now(),
        LocalDate.now().plusYears(1), // end is nullable but actually never is null for fee decisions
    ),
    FeeDecisionStatus.WAITING_FOR_SENDING,
    decisionNumber = null,
    FeeDecisionType.NORMAL,
    headOfFamily = PersonDetailed(
        PersonId(UUID.randomUUID()), LocalDate.of(1982, 3, 31), null,
        "Maija", "Meikäläinen",
        "310382-956D", "Meikäläisenkuja 6 B 7", "33730", "TAMPERE",
        "", null, "", null, restrictedDetailsEnabled = false,
    ),
    partner = null,
    headOfFamilyIncome = null,
    partnerIncome = null,
    familySize = 1,
    FeeDecisionThresholds(
        minIncomeThreshold = 1,
        maxIncomeThreshold = 2,
        incomeMultiplier = BigDecimal.ONE,
        maxFee = 1,
        minFee = 1,
    ),
    documentKey = null,
    approvedBy = EmployeeWithName(EmployeeId(UUID.randomUUID()), "Markus", "Maksusihteeri"),
    approvedAt = HelsinkiDateTime.now(),
    sentAt = null,
    financeDecisionHandlerFirstName = null,
    financeDecisionHandlerLastName = null,
)

private fun validFeeDecisionChild() = FeeDecisionChildDetailed(
    child = PersonDetailed(
        PersonId(UUID.randomUUID()), LocalDate.of(2018, 1, 1), null,
        "Matti", "Meikäläinen",
        null, "", "", "",
        "", null, "", null, restrictedDetailsEnabled = false,
    ),
    placementType = PlacementType.DAYCARE,
    placementUnit = UnitData(
        DaycareId(UUID.randomUUID()),
        name = "Yksikkö 1",
        areaId = AreaId(UUID.randomUUID()),
        areaName = "Alue 1",
        language = "fi",
    ),
    serviceNeedFeeCoefficient = BigDecimal.ONE,
    serviceNeedDescriptionFi = "Palveluntarve 1",
    serviceNeedDescriptionSv = "Palveluntarve 1 (sv)",
    serviceNeedMissing = false,
    baseFee = 1,
    siblingDiscount = 1,
    fee = 1,
    feeAlterations = listOf(
        FeeAlterationWithEffect(FeeAlteration.Type.RELIEF, 50, false, -10800),
    ),
    finalFee = 1,
    childIncome = null,
)

private fun validVoucherValueDecision() = VoucherValueDecisionDetailed(
    VoucherValueDecisionId(UUID.randomUUID()),
    LocalDate.now(),
    LocalDate.now().plusYears(1), // validTo is nullable but actually never is null
    VoucherValueDecisionStatus.WAITING_FOR_SENDING,
    decisionNumber = null,
    decisionType = VoucherValueDecisionType.NORMAL,
    headOfFamily = PersonDetailed(
        PersonId(UUID.randomUUID()), LocalDate.of(1982, 3, 31), null,
        "Maija", "Meikäläinen",
        "310382-956D", "Meikäläisenkuja 6 B 7", "33730", "TAMPERE",
        "", null, "", null, restrictedDetailsEnabled = false,
    ),
    partner = null,
    headOfFamilyIncome = null,
    partnerIncome = null,
    childIncome = null,
    familySize = 1,
    FeeDecisionThresholds(
        minIncomeThreshold = 1,
        maxIncomeThreshold = 2,
        incomeMultiplier = BigDecimal.ONE,
        maxFee = 1,
        minFee = 1,
    ),
    PersonDetailed(
        PersonId(UUID.randomUUID()), LocalDate.of(2018, 1, 1), null,
        "Matti", "Meikäläinen",
        null, "", "", "",
        "", null, "", null, restrictedDetailsEnabled = false,
    ),
    VoucherValueDecisionPlacementDetailed(
        UnitData(
            DaycareId(UUID.randomUUID()),
            name = "Vuoreksen kerho",
            areaId = AreaId(UUID.randomUUID()),
            areaName = "Etelä",
            language = "fi",
        ),
        type = PlacementType.DAYCARE,
    ),
    VoucherValueDecisionServiceNeed(
        feeCoefficient = BigDecimal.ONE,
        voucherValueCoefficient = BigDecimal.ONE,
        feeDescriptionFi = "eka",
        feeDescriptionSv = "toka",
        voucherValueDescriptionFi = "kolmas",
        voucherValueDescriptionSv = "neljäs",
        missing = false,
    ),
    baseCoPayment = 1,
    siblingDiscount = 1,
    coPayment = 1,
    feeAlterations = emptyList(),
    finalCoPayment = 1,
    baseValue = 1,
    childAge = 1,
    assistanceNeedCoefficient = BigDecimal.ONE,
    voucherValue = 1,
    documentKey = null,
    approvedBy = EmployeeWithName(EmployeeId(UUID.randomUUID()), "Markus", "Maksusihteeri"),
    approvedAt = HelsinkiDateTime.now(),
    sentAt = null,
    created = HelsinkiDateTime.now(),
    financeDecisionHandlerFirstName = null,
    financeDecisionHandlerLastName = null,
)
