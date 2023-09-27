// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.invoice.service

import fi.espoo.evaka.invoicing.domain.DecisionIncome
import fi.espoo.evaka.invoicing.domain.EmployeeWithName
import fi.espoo.evaka.invoicing.domain.FeeAlteration
import fi.espoo.evaka.invoicing.domain.FeeAlterationType
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
import fi.espoo.evaka.invoicing.domain.toFeeAlterationsWithEffects
import fi.espoo.evaka.invoicing.service.DocumentLang
import fi.espoo.evaka.invoicing.service.FeeDecisionPdfData
import fi.espoo.evaka.invoicing.service.VoucherValueDecisionPdfData
import fi.espoo.evaka.pdfgen.Page
import fi.espoo.evaka.pdfgen.PdfGenerator
import fi.espoo.evaka.pdfgen.Template
import fi.espoo.evaka.placement.PlacementType
import fi.espoo.evaka.setting.SettingType
import fi.espoo.evaka.shared.AreaId
import fi.espoo.evaka.shared.ChildId
import fi.espoo.evaka.shared.DaycareId
import fi.espoo.evaka.shared.EmployeeId
import fi.espoo.evaka.shared.FeeAlterationId
import fi.espoo.evaka.shared.FeeDecisionId
import fi.espoo.evaka.shared.PersonId
import fi.espoo.evaka.shared.ServiceNeedOptionId
import fi.espoo.evaka.shared.VoucherValueDecisionId
import fi.espoo.evaka.shared.domain.DateRange
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.tampere.trevaka.AbstractIntegrationTest
import fi.tampere.trevaka.reportsPath
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junitpioneer.jupiter.cartesian.CartesianTest
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

    @ParameterizedTest
    @EnumSource(PlacementType::class)
    fun generateFeeDecisionPdfFromPlacementType(placementType: PlacementType) {
        val decision = validFeeDecision(children = listOf(validFeeDecisionChild().copy(placementType = placementType)))

        val bytes = pdfGenerator.generateFeeDecisionPdf(FeeDecisionPdfData(decision, settings, DocumentLang.FI))

        val filepath = "$reportsPath/PDFServiceTest-fee-decision-placement-type-$placementType.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @CartesianTest
    fun generateFeeDecisionPdfFromDecisionType(
        @CartesianTest.Enum(
            PlacementType::class,
            mode = CartesianTest.Enum.Mode.INCLUDE,
            names = ["DAYCARE", "PRESCHOOL_CLUB"],
        ) placementType: PlacementType,
        @CartesianTest.Enum(
            FeeDecisionType::class,
            mode = CartesianTest.Enum.Mode.EXCLUDE,
            names = ["NORMAL"],
        ) decisionType: FeeDecisionType,
    ) {
        val decision = validFeeDecision(listOf(validFeeDecisionChild().copy(placementType = placementType))).copy(decisionType = decisionType)

        val bytes = pdfGenerator.generateFeeDecisionPdf(FeeDecisionPdfData(decision, settings, DocumentLang.FI))

        val filepath = "$reportsPath/PDFServiceTest-fee-decision-placement-type-$placementType-decision-type-$decisionType.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @ParameterizedTest
    @EnumSource(FeeDecisionType::class)
    fun generateFeeDecisionPdfWithPlacementTypeDaycareAndPreschoolClub(decisionType: FeeDecisionType) {
        val decision = validFeeDecision(
            children = listOf(
                validFeeDecisionChild(validChild().copy(firstName = "Matti")).copy(
                    placementType = PlacementType.DAYCARE,
                    serviceNeedDescriptionFi = "Kokopäiväinen",
                    serviceNeedDescriptionSv = "Kokopäiväinen (sv)",
                    baseFee = 29500,
                    fee = 29500,
                    finalFee = 29500,
                ),
                validFeeDecisionChild(validChild().copy(firstName = "Mikko")).copy(
                    placementType = PlacementType.PRESCHOOL_CLUB,
                    serviceNeedDescriptionFi = "Esiopetuksen kerho 1-3h päivässä",
                    serviceNeedDescriptionSv = "Esiopetuksen kerho 1-3h päivässä (sv)",
                    baseFee = 14000,
                    fee = 7000,
                    finalFee = 7000,
                ),
            ),
        ).copy(decisionType = decisionType)

        val bytes = pdfGenerator.generateFeeDecisionPdf(FeeDecisionPdfData(decision, settings, DocumentLang.FI))

        val filepath = "$reportsPath/PDFServiceTest-fee-decision-placement-type-DAYCARE-and-PRESCHOOL_CLUB-decision-type-$decisionType.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @ParameterizedTest
    @EnumSource(VoucherValueDecisionType::class)
    fun generateVoucherValueDecisionPdfFromDecisionType(decisionType: VoucherValueDecisionType) {
        val decision = validVoucherValueDecision().copy(decisionType = decisionType)
        val data = VoucherValueDecisionPdfData(decision, settings, DocumentLang.FI)

        val bytes = pdfGenerator.generateVoucherValueDecisionPdf(data)

        val filepath = "$reportsPath/PDFServiceTest-voucher-value-decision-type-$decisionType.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateFeeDecisionPdfWithHeadOfFamilyIncome() {
        val decision = validFeeDecision().copy(headOfFamilyIncome = testDecisionIncome)

        val bytes = pdfGenerator.generateFeeDecisionPdf(FeeDecisionPdfData(decision, settings, DocumentLang.FI))

        val filepath = "$reportsPath/PDFServiceTest-fee-decision-head-of-family-income.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateFeeDecisionPdfWithPartner() {
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
    fun generateFeeDecisionPdfWithPartnerIncome() {
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
    fun generateFeeDecisionPdfWithAllIncomes() {
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
    fun generateFeeDecisionPdfWithEmptyAddress() {
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
    fun generateFeeDecisionPdfWithFeeAlterations() {
        val decision =
            validFeeDecision(children = listOf(validFeeDecisionChild(feeAlterations = validFeeAlterations())))

        val bytes = pdfGenerator.generateFeeDecisionPdf(FeeDecisionPdfData(decision, settings, DocumentLang.FI))

        val filepath = "$reportsPath/PDFServiceTest-fee-decision-fee-alterations.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @ParameterizedTest
    @EnumSource(PlacementType::class)
    fun generateVoucherValueDecisionPdfFromPlacementType(placementType: PlacementType) {
        val decision =
            validVoucherValueDecision(placement = validVoucherValueDecisionPlacementDetailed(placementType = placementType))
        val data = VoucherValueDecisionPdfData(decision, settings, DocumentLang.FI)

        val bytes = pdfGenerator.generateVoucherValueDecisionPdf(data)

        val filepath = "$reportsPath/PDFServiceTest-voucher-value-decision-placement-type-$placementType.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateVoucherValueDecisionPdfWithHeadOfFamilyIncome() {
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
    fun generateVoucherValueDecisionPdfWithPartner() {
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
    fun generateVoucherValueDecisionPdfWithEmptyAddress() {
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
    fun generateVoucherValueDecisionPdfWithFeeAlterations() {
        val decision = validVoucherValueDecision(feeAlterations = validFeeAlterations())
        val data = VoucherValueDecisionPdfData(decision, settings, DocumentLang.FI)

        val bytes = pdfGenerator.generateVoucherValueDecisionPdf(data)

        val filepath = "$reportsPath/PDFServiceTest-voucher-value-decision-fee-alterations.pdf"
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

private fun validFeeDecision(children: List<FeeDecisionChildDetailed> = listOf(validFeeDecisionChild())) =
    FeeDecisionDetailed(
        id = FeeDecisionId(UUID.randomUUID()),
        children = children,
        validDuring = DateRange(
            LocalDate.now(),
            LocalDate.now().plusYears(1), // end is nullable but actually never is null for fee decisions
        ),
        status = FeeDecisionStatus.WAITING_FOR_SENDING,
        decisionNumber = 100200,
        decisionType = FeeDecisionType.NORMAL,
        headOfFamily = PersonDetailed(
            PersonId(UUID.randomUUID()), LocalDate.of(1982, 3, 31), null,
            "Maija", "Meikäläinen",
            "310382-956D", "Meikäläisenkuja 6 B 7", "33730", "TAMPERE",
            "", null, "", null, restrictedDetailsEnabled = false,
        ),
        partner = null,
        headOfFamilyIncome = null,
        partnerIncome = null,
        familySize = 1 + children.size,
        feeThresholds = FeeDecisionThresholds(
            minIncomeThreshold = 387400,
            maxIncomeThreshold = 662640,
            incomeMultiplier = BigDecimal("0.1070"),
            maxFee = 29500,
            minFee = 2800,
        ),
        documentKey = null,
        approvedBy = EmployeeWithName(EmployeeId(UUID.randomUUID()), "Markus", "Maksusihteeri"),
        approvedAt = HelsinkiDateTime.now(),
        sentAt = null,
        financeDecisionHandlerFirstName = null,
        financeDecisionHandlerLastName = null,
    )

private fun validFeeDecisionChild(
    child: PersonDetailed = validChild(),
    feeAlterations: List<FeeAlteration> = emptyList(),
) = FeeDecisionChildDetailed(
    child = child,
    placementType = PlacementType.DAYCARE,
    placementUnit = UnitData(
        id = DaycareId(UUID.randomUUID()),
        name = "Yksikkö 1",
        areaId = AreaId(UUID.randomUUID()),
        areaName = "Alue 1",
        language = "fi",
    ),
    serviceNeedOptionId = ServiceNeedOptionId(UUID.randomUUID()),
    serviceNeedFeeCoefficient = BigDecimal.ONE,
    serviceNeedDescriptionFi = "Kokopäiväinen",
    serviceNeedDescriptionSv = "Kokopäiväinen (sv)",
    serviceNeedMissing = false,
    baseFee = 29500,
    siblingDiscount = 0,
    fee = 29500,
    feeAlterations = toFeeAlterationsWithEffects(29500, feeAlterations),
    finalFee = 29500,
    childIncome = null,
)

private fun validChild() = PersonDetailed(
    PersonId(UUID.randomUUID()), LocalDate.of(2018, 1, 1), null,
    "Matti", "Meikäläinen",
    null, "", "", "",
    "", null, "", null, restrictedDetailsEnabled = false,
)

private fun validVoucherValueDecision(
    child: PersonDetailed = validChild(),
    placement: VoucherValueDecisionPlacementDetailed = validVoucherValueDecisionPlacementDetailed(
        PlacementType.DAYCARE,
    ),
    feeAlterations: List<FeeAlteration> = emptyList(),
) = VoucherValueDecisionDetailed(
    id = VoucherValueDecisionId(UUID.randomUUID()),
    validFrom = LocalDate.now(),
    validTo = LocalDate.now().plusYears(1), // validTo is nullable but actually never is null
    status = VoucherValueDecisionStatus.WAITING_FOR_SENDING,
    decisionNumber = 100200,
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
    familySize = 2,
    feeThresholds = FeeDecisionThresholds(
        minIncomeThreshold = 387400,
        maxIncomeThreshold = 662640,
        incomeMultiplier = BigDecimal("0.1070"),
        maxFee = 29500,
        minFee = 2800,
    ),
    child = child,
    placement = placement,
    serviceNeed = VoucherValueDecisionServiceNeed(
        feeCoefficient = BigDecimal.ONE,
        voucherValueCoefficient = BigDecimal.ONE,
        feeDescriptionFi = "Kokopäiväinen",
        feeDescriptionSv = "Kokopäiväinen (sv)",
        voucherValueDescriptionFi = "Kokopäiväinen",
        voucherValueDescriptionSv = "Kokopäiväinen (sv)",
        missing = false,
    ),
    baseCoPayment = 29500,
    siblingDiscount = 0,
    coPayment = 29500,
    feeAlterations = toFeeAlterationsWithEffects(29500, feeAlterations),
    finalCoPayment = 29500,
    baseValue = 130200,
    childAge = 1,
    assistanceNeedCoefficient = BigDecimal.ZERO,
    voucherValue = 130200,
    documentKey = null,
    approvedBy = EmployeeWithName(EmployeeId(UUID.randomUUID()), "Markus", "Maksusihteeri"),
    approvedAt = HelsinkiDateTime.now(),
    sentAt = null,
    created = HelsinkiDateTime.now(),
    financeDecisionHandlerFirstName = null,
    financeDecisionHandlerLastName = null,
)

private fun validVoucherValueDecisionPlacementDetailed(placementType: PlacementType) =
    VoucherValueDecisionPlacementDetailed(
        UnitData(
            DaycareId(UUID.randomUUID()),
            name = "Touhula Ylöjärvi",
            areaId = AreaId(UUID.randomUUID()),
            areaName = "Länsi",
            language = "fi",
        ),
        type = placementType,
    )

private fun validFeeAlterations(childId: ChildId = ChildId(UUID.randomUUID())) = FeeAlterationType.values().flatMap {
    listOf(
        FeeAlteration(
            id = FeeAlterationId(UUID.randomUUID()),
            personId = childId,
            type = it,
            amount = 5,
            isAbsolute = false,
            validFrom = LocalDate.now(),
            validTo = null,
            notes = "",
            updatedAt = null,
            updatedBy = null,
        ),
        FeeAlteration(
            id = FeeAlterationId(UUID.randomUUID()),
            personId = childId,
            type = it,
            amount = 50,
            isAbsolute = true,
            validFrom = LocalDate.now(),
            validTo = null,
            notes = "",
            updatedAt = null,
            updatedBy = null,
        ),
    )
}
