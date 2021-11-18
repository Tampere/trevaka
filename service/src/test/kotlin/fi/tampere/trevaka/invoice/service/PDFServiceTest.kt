// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.invoice.service

import fi.espoo.evaka.invoicing.domain.FeeAlteration
import fi.espoo.evaka.invoicing.domain.FeeAlterationWithEffect
import fi.espoo.evaka.invoicing.domain.FeeDecisionChildDetailed
import fi.espoo.evaka.invoicing.domain.FeeDecisionDetailed
import fi.espoo.evaka.invoicing.domain.FeeDecisionStatus
import fi.espoo.evaka.invoicing.domain.FeeDecisionThresholds
import fi.espoo.evaka.invoicing.domain.FeeDecisionType
import fi.espoo.evaka.invoicing.domain.PersonData
import fi.espoo.evaka.invoicing.domain.UnitData
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionDetailed
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionPlacementDetailed
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionServiceNeed
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionStatus
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionType
import fi.espoo.evaka.invoicing.service.DocumentLang
import fi.espoo.evaka.invoicing.service.FeeDecisionPdfData
import fi.espoo.evaka.invoicing.service.PDFService
import fi.espoo.evaka.invoicing.service.Page
import fi.espoo.evaka.invoicing.service.Template
import fi.espoo.evaka.invoicing.service.VoucherValueDecisionPdfData
import fi.espoo.evaka.placement.PlacementType
import fi.espoo.evaka.shared.AreaId
import fi.espoo.evaka.shared.DaycareId
import fi.espoo.evaka.shared.FeeDecisionId
import fi.espoo.evaka.shared.VoucherValueDecisionId
import fi.espoo.evaka.shared.domain.DateRange
import fi.tampere.trevaka.AbstractIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.thymeleaf.context.Context
import java.io.FileOutputStream
import java.math.BigDecimal
import java.nio.file.Paths
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

private val reportsPath: String = "${Paths.get("build").toAbsolutePath()}/reports"

internal class PDFServiceTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var pdfService: PDFService

    @Test
    fun render() {
        pdfService.render(Page(Template("test"), Context()))
    }

    @Test
    fun generateFeeDecisionPdf() {
        val bytes = pdfService.generateFeeDecisionPdf(validFeeDecisionPdfData())

        val filepath = "${reportsPath}/PDFServiceTest-fee-decision.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateFeeDecisionPdfValidTo() {
        val validTo = LocalDate.now().plusYears(1)
        val bytes = pdfService.generateFeeDecisionPdf(validFeeDecisionPdfData(validTo))

        val filepath = "${reportsPath}/PDFServiceTest-fee-decision-valid-to.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateFeeDecisionPdfEmptyAddress() {
        val bytes = pdfService.generateFeeDecisionPdf(
            validFeeDecisionPdfData(
                headOfFamily = PersonData.Detailed(
                    UUID.randomUUID(), LocalDate.of(1982, 3, 31), null,
                    "Maija", "Meikäläinen",
                    "310382-956D", "", "", "",
                    "", null, "", null, restrictedDetailsEnabled = false
                )
            )
        )

        val filepath = "${reportsPath}/PDFServiceTest-fee-decision-empty-address.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateVoucherValueDecisionPdf() {
        val bytes = pdfService.generateVoucherValueDecisionPdf(validVoucherValueDecisionPdfData())

        val filepath = "${reportsPath}/PDFServiceTest-voucher-value-decision.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateVoucherValueDecisionPdfValidTo() {
        val validTo = LocalDate.now().plusYears(1)
        val bytes = pdfService.generateVoucherValueDecisionPdf(validVoucherValueDecisionPdfData(validTo))

        val filepath = "${reportsPath}/PDFServiceTest-voucher-value-decision-valid-to.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateVoucherValueDecisionPdfEmptyAddress() {
        val bytes = pdfService.generateVoucherValueDecisionPdf(
            validVoucherValueDecisionPdfData(
                headOfFamily = PersonData.Detailed(
                    UUID.randomUUID(), LocalDate.of(1982, 3, 31), null,
                    "Maija", "Meikäläinen",
                    "310382-956D", "", "", "",
                    "", null, "", null, restrictedDetailsEnabled = false
                )
            )
        )

        val filepath = "${reportsPath}/PDFServiceTest-voucher-value-decision-empty-address.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

}

private fun validFeeDecisionPdfData(
    validTo: LocalDate? = null,
    headOfFamily: PersonData.Detailed = PersonData.Detailed(
        UUID.randomUUID(), LocalDate.of(1982, 3, 31), null,
        "Maija", "Meikäläinen",
        "310382-956D", "Meikäläisenkuja 6 B 7", "33730", "TAMPERE",
        "", null, "", null, restrictedDetailsEnabled = false
    )
): FeeDecisionPdfData {
    return FeeDecisionPdfData(
        FeeDecisionDetailed(
            FeeDecisionId(UUID.randomUUID()),
            children = listOf(
                FeeDecisionChildDetailed(
                    child = PersonData.Detailed(
                        UUID.randomUUID(), LocalDate.of(2018, 1, 1), null,
                        "Matti", "Meikäläinen",
                        null, "", "", "",
                        "", null, "", null, restrictedDetailsEnabled = false
                    ),
                    placementType = PlacementType.DAYCARE,
                    placementUnit = UnitData.Detailed(
                        DaycareId(UUID.randomUUID()),
                        name = "Yksikkö 1",
                        areaId = AreaId(UUID.randomUUID()),
                        areaName = "Alue 1",
                        language = "fi"
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
                    finalFee = 1
                )
            ),
            validDuring = DateRange(LocalDate.now(), validTo),
            FeeDecisionStatus.WAITING_FOR_SENDING,
            decisionNumber = null,
            FeeDecisionType.NORMAL,
            headOfFamily = headOfFamily,
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
            approvedBy = PersonData.WithName(UUID.randomUUID(), "Markus", "Maksusihteeri"),
            approvedAt = Instant.now(),
            sentAt = null,
            financeDecisionHandlerFirstName = null,
            financeDecisionHandlerLastName = null
        ), "fi"
    )
}

private fun validVoucherValueDecisionPdfData(
    validTo: LocalDate? = null,
    headOfFamily: PersonData.Detailed = PersonData.Detailed(
        UUID.randomUUID(), LocalDate.of(1982, 3, 31), null,
        "Maija", "Meikäläinen",
        "310382-956D", "Meikäläisenkuja 6 B 7", "33730", "TAMPERE",
        "", null, "", null, restrictedDetailsEnabled = false
    )
): VoucherValueDecisionPdfData {
    return VoucherValueDecisionPdfData(
        VoucherValueDecisionDetailed(
            VoucherValueDecisionId(UUID.randomUUID()),
            LocalDate.now(),
            validTo,
            VoucherValueDecisionStatus.WAITING_FOR_SENDING,
            decisionNumber = null,
            decisionType = VoucherValueDecisionType.NORMAL,
            headOfFamily = headOfFamily,
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
            PersonData.Detailed(
                UUID.randomUUID(), LocalDate.of(2018, 1, 1), null,
                "Matti", "Meikäläinen",
                null, "", "", "",
                "", null, "", null, restrictedDetailsEnabled = false
            ),
            VoucherValueDecisionPlacementDetailed(
                UnitData.Detailed(
                    DaycareId(UUID.randomUUID()),
                    name = "Vuoreksen kerho",
                    areaId = AreaId(UUID.randomUUID()),
                    areaName = "Etelä",
                    language = "fi"
                ),
                type = PlacementType.DAYCARE
            ),
            VoucherValueDecisionServiceNeed(
                feeCoefficient = BigDecimal.ONE,
                voucherValueCoefficient = BigDecimal.ONE,
                feeDescriptionFi = "eka",
                feeDescriptionSv = "toka",
                voucherValueDescriptionFi = "kolmas",
                voucherValueDescriptionSv = "neljäs"
            ),
            baseCoPayment = 1,
            siblingDiscount = 1,
            coPayment = 1,
            emptyList(),
            finalCoPayment = 1,
            baseValue = 1,
            childAge = 1,
            ageCoefficient = BigDecimal.ONE,
            capacityFactor = BigDecimal.ONE,
            voucherValue = 1,
            documentKey = null,
            approvedBy = PersonData.WithName(UUID.randomUUID(), "Markus", "Maksusihteeri"),
            approvedAt = Instant.now(),
            sentAt = null,
            created = Instant.now(),
            financeDecisionHandlerFirstName = null,
            financeDecisionHandlerLastName = null
        ),
        DocumentLang.fi
    )
}
