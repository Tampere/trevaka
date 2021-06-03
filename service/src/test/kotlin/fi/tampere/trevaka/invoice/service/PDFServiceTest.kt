// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.invoice.service

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
import fi.espoo.evaka.invoicing.service.DocumentLang
import fi.espoo.evaka.invoicing.service.FeeDecisionPdfData
import fi.espoo.evaka.invoicing.service.PDFService
import fi.espoo.evaka.invoicing.service.Page
import fi.espoo.evaka.invoicing.service.Template
import fi.espoo.evaka.invoicing.service.VoucherValueDecisionPdfData
import fi.espoo.evaka.placement.PlacementType
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

internal class PDFServiceTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var pdfService: PDFService

    @Test
    fun render() {
        pdfService.render(Page(Template("test"), Context()))
    }

    @Test
    fun generateFeeDecisionPdf() {
        val bytes = pdfService.generateFeeDecisionPdf(
            FeeDecisionPdfData(
                FeeDecisionDetailed(
                    UUID.randomUUID(),
                    children = emptyList(),
                    validDuring = DateRange(LocalDate.now(), null),
                    FeeDecisionStatus.WAITING_FOR_SENDING,
                    decisionNumber = null,
                    FeeDecisionType.NORMAL,
                    headOfFamily = PersonData.Detailed(
                        UUID.randomUUID(), LocalDate.of(1982, 3, 31), null,
                        "Maija", "Meikäläinen",
                        "310382-956D", "Meikäläisenkuja 6 B 7", "33730", "TAMPERE",
                        null, null, null, null, restrictedDetailsEnabled = false
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
                    approvedBy = null,
                    approvedAt = null,
                    sentAt = null,
                    financeDecisionHandlerFirstName = null,
                    financeDecisionHandlerLastName = null
                ), "fi"
            )
        )

        val filepath = "${Paths.get("build").toAbsolutePath()}/reports/PDFServiceTest-fee-decision.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generateVoucherValueDecisionPdf() {
        val bytes = pdfService.generateVoucherValueDecisionPdf(
            VoucherValueDecisionPdfData(
                VoucherValueDecisionDetailed(
                    UUID.randomUUID(),
                    LocalDate.now(),
                    validTo = null,
                    VoucherValueDecisionStatus.WAITING_FOR_SENDING,
                    decisionNumber = null,
                    PersonData.Detailed(
                        UUID.randomUUID(), LocalDate.of(1982, 3, 31), null,
                        "Maija", "Meikäläinen",
                        "310382-956D", "Meikäläisenkuja 6 B 7", "33730", "TAMPERE",
                        null, null, null, null, restrictedDetailsEnabled = false
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
                    PersonData.Detailed(
                        UUID.randomUUID(), LocalDate.of(2018, 1, 1), null,
                        "Matti", "Meikäläinen",
                        null, null, null, null,
                        null, null, null, null, restrictedDetailsEnabled = false
                    ),
                    VoucherValueDecisionPlacementDetailed(
                        UnitData.Detailed(
                            UUID.randomUUID(),
                            name = "Vuoreksen kerho",
                            areaId = UUID.randomUUID(),
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
                    voucherValue = 1,
                    documentKey = null,
                    approvedBy = null,
                    approvedAt = null,
                    sentAt = null,
                    created = Instant.now(),
                    financeDecisionHandlerFirstName = null,
                    financeDecisionHandlerLastName = null
                ),
                DocumentLang.fi
            )
        )

        val filepath = "${Paths.get("build").toAbsolutePath()}/reports/PDFServiceTest-voucher-value-decision.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

}
