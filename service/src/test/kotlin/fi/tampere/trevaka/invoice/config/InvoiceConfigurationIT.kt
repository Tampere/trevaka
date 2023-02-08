// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.invoice.config

import fi.espoo.evaka.invoicing.data.flatten
import fi.espoo.evaka.invoicing.data.invoiceQueryBase
import fi.espoo.evaka.invoicing.data.toInvoice
import fi.espoo.evaka.invoicing.data.upsertFeeDecisions
import fi.espoo.evaka.invoicing.domain.ChildWithDateOfBirth
import fi.espoo.evaka.invoicing.domain.DecisionIncome
import fi.espoo.evaka.invoicing.domain.FeeAlterationWithEffect
import fi.espoo.evaka.invoicing.domain.FeeDecision
import fi.espoo.evaka.invoicing.domain.FeeDecisionChild
import fi.espoo.evaka.invoicing.domain.FeeDecisionDifference
import fi.espoo.evaka.invoicing.domain.FeeDecisionPlacement
import fi.espoo.evaka.invoicing.domain.FeeDecisionServiceNeed
import fi.espoo.evaka.invoicing.domain.FeeDecisionStatus
import fi.espoo.evaka.invoicing.domain.FeeDecisionThresholds
import fi.espoo.evaka.invoicing.domain.FeeDecisionType
import fi.espoo.evaka.invoicing.domain.FeeThresholds
import fi.espoo.evaka.invoicing.domain.Invoice
import fi.espoo.evaka.invoicing.service.InvoiceGenerator
import fi.espoo.evaka.placement.PlacementType
import fi.espoo.evaka.serviceneed.ServiceNeedOption
import fi.espoo.evaka.shared.AreaId
import fi.espoo.evaka.shared.ChildId
import fi.espoo.evaka.shared.DaycareId
import fi.espoo.evaka.shared.EvakaUserId
import fi.espoo.evaka.shared.FeeDecisionId
import fi.espoo.evaka.shared.ParentshipId
import fi.espoo.evaka.shared.PersonId
import fi.espoo.evaka.shared.ServiceNeedOptionId
import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.dev.DevChild
import fi.espoo.evaka.shared.dev.DevDaycare
import fi.espoo.evaka.shared.dev.DevParentship
import fi.espoo.evaka.shared.dev.DevPerson
import fi.espoo.evaka.shared.dev.insertEvakaUser
import fi.espoo.evaka.shared.dev.insertTestChild
import fi.espoo.evaka.shared.dev.insertTestDaycare
import fi.espoo.evaka.shared.dev.insertTestFeeThresholds
import fi.espoo.evaka.shared.dev.insertTestParentship
import fi.espoo.evaka.shared.dev.insertTestPerson
import fi.espoo.evaka.shared.dev.insertTestPlacement
import fi.espoo.evaka.shared.domain.DateRange
import fi.espoo.evaka.shared.security.PilotFeature
import fi.espoo.evaka.user.EvakaUser
import fi.espoo.evaka.user.EvakaUserType
import fi.tampere.trevaka.AbstractIntegrationTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

internal class InvoiceConfigurationIT : AbstractIntegrationTest() {

    @Autowired
    private lateinit var generator: InvoiceGenerator

    private final val questionnaireId = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
    private final val evakaUserId = EvakaUserId(UUID.randomUUID())
    private final val placementPeriod = DateRange(LocalDate.of(2021, 8, 31), LocalDate.of(2022, 8, 31))

    @BeforeEach
    fun insertBaseData() {
        db.transaction { tx ->
            tx.createUpdate(
                """
                INSERT INTO holiday_period_questionnaire(id, type, absence_type, requires_strong_auth, active, title, description, description_link, condition_continuous_placement, period_options, period_option_label)
                VALUES (:questionnaireId, 'FIXED_PERIOD', 'FREE_ABSENCE', false, daterange('2022-04-13', '2022-04-29'), '{"fi": "title"}', '{"fi": "description"}', '{"fi": "link"}', daterange('2021-08-31', '2022-06-30'), array[daterange('2022-06-06', '2022-07-31', '[]'), daterange('2022-06-13', '2022-08-07', '[]'), daterange('2022-06-20', '2022-08-14', '[]'), daterange('2022-06-27', '2022-08-21', '[]'), daterange('2022-07-04', '2022-08-28', '[]')], '{"fi": "period option label"}')
                """
            ).bind("questionnaireId", questionnaireId).execute()
            tx.insertTestFeeThresholds(
                FeeThresholds(
                    validDuring = DateRange(LocalDate.of(2000, 1, 1), null),
                    minIncomeThreshold2 = 210200,
                    minIncomeThreshold3 = 271300,
                    minIncomeThreshold4 = 308000,
                    minIncomeThreshold5 = 344700,
                    minIncomeThreshold6 = 381300,
                    maxIncomeThreshold2 = 479900,
                    maxIncomeThreshold3 = 541000,
                    maxIncomeThreshold4 = 577700,
                    maxIncomeThreshold5 = 614400,
                    maxIncomeThreshold6 = 651000,
                    incomeMultiplier2 = BigDecimal("0.1070"),
                    incomeMultiplier3 = BigDecimal("0.1070"),
                    incomeMultiplier4 = BigDecimal("0.1070"),
                    incomeMultiplier5 = BigDecimal("0.1070"),
                    incomeMultiplier6 = BigDecimal("0.1070"),
                    incomeThresholdIncrease6Plus = 14200,
                    siblingDiscount2 = BigDecimal("0.5"),
                    siblingDiscount2Plus = BigDecimal("0.8"),
                    maxFee = 28900,
                    minFee = 2700,
                    temporaryFee = 2900,
                    temporaryFeePartDay = 1500,
                    temporaryFeeSibling = 1500,
                    temporaryFeeSiblingPartDay = 800
                )
            )
            tx.insertTestDaycare(testDaycare)
            tx.insertTestPerson(testChild)
            tx.insertTestChild(DevChild(testChild.id))
            tx.insertTestPerson(testAdult)
            tx.insertTestParentship(testParentship)
            tx.insertEvakaUser(EvakaUser(evakaUserId, "integration-test", EvakaUserType.UNKNOWN))
        }
        val decisions = listOf(
            createFeeDecisionFixture(
                FeeDecisionStatus.SENT,
                FeeDecisionType.NORMAL,
                placementPeriod,
                testAdult.id,
                listOf(
                    createFeeDecisionChildFixture(
                        childId = testChild.id,
                        dateOfBirth = testChild.dateOfBirth,
                        placementUnitId = testDaycare.id,
                        placementType = PlacementType.DAYCARE,
                        serviceNeed = snDaycareFullDay.toFeeDecisionServiceNeed(),
                        baseFee = 28900,
                        fee = 28900,
                        feeAlterations = listOf()
                    )
                )
            )
        )
        insertDecisionsAndPlacements(decisions)
    }

    @Test
    fun test8WeeksReserved() {

        db.transaction { tx ->
            tx.createUpdate(
                """
                INSERT INTO absence(child_id, date, absence_type, modified_by, category, questionnaire_id)
                VALUES (:childId, generate_series('2022-06-06', '2022-07-31', interval '1 day')::date, 'FREE_ABSENCE', :evakaUserId, 'BILLABLE', :questionnaireId)
                """
            ).bind("childId", testChild.id).bind("evakaUserId", evakaUserId).bind("questionnaireId", questionnaireId)
                .execute()
        }

        val june = DateRange(LocalDate.of(2022, 6, 1), LocalDate.of(2022, 6, 30))
        db.transaction { generator.createAndStoreAllDraftInvoices(it, june) }
        val result = db.read { getAllInvoices(it) }
        Assertions.assertEquals(0, result.size)
    }

    @Test
    fun test7WeeksReserved() {

        db.transaction { tx ->
            tx.createUpdate(
                """
                INSERT INTO absence(child_id, date, absence_type, modified_by, category, questionnaire_id)
                VALUES (:childId, generate_series('2022-06-13', '2022-07-31', interval '1 day')::date, 'FREE_ABSENCE', :evakaUserId, 'BILLABLE', :questionnaireId)
                """
            ).bind("childId", testChild.id).bind("evakaUserId", evakaUserId).bind("questionnaireId", questionnaireId)
                .execute()
        }

        val june = DateRange(LocalDate.of(2022, 6, 1), LocalDate.of(2022, 6, 30))
        db.transaction { generator.createAndStoreAllDraftInvoices(it, june) }
        val result = db.read { getAllInvoices(it) }
        Assertions.assertEquals(1, result.size)
    }

    private final val testChild = DevPerson(
        id = ChildId(UUID.randomUUID()),
        dateOfBirth = LocalDate.of(2017, 6, 1),
        ssn = "010617A123U",
        firstName = "Ricky",
        lastName = "Doe",
        streetAddress = "Kamreerintie 2",
        postalCode = "02770",
        postOffice = "Espoo",
        restrictedDetailsEnabled = false
    )

    private final val testAdult = DevPerson(
        id = PersonId(UUID.randomUUID()),
        dateOfBirth = LocalDate.of(1980, 1, 1),
        ssn = "010180-1232",
        firstName = "John",
        lastName = "Doe",
        streetAddress = "Kamreerintie 2",
        postalCode = "02770",
        postOffice = "Espoo",
        restrictedDetailsEnabled = false
    )

    private final val testParentship = DevParentship(
        id = ParentshipId(UUID.randomUUID()),
        headOfChildId = testAdult.id,
        childId = testChild.id,
        startDate = testChild.dateOfBirth,
        endDate = testChild.dateOfBirth.plusYears(18).minusDays(1)
    )

    private final val defaultMunicipalOrganizerOid = "1.2.246.562.10.888888888888"

    private final val testDaycare = DevDaycare(
        id = DaycareId(UUID.randomUUID()),
        name = "Test Daycare",
        areaId = AreaId(UUID.fromString("6529e31e-9777-11eb-ba88-33a923255570")), // Etelä
        ophOrganizerOid = defaultMunicipalOrganizerOid,
        enabledPilotFeatures = setOf(PilotFeature.MESSAGING, PilotFeature.MOBILE, PilotFeature.RESERVATIONS, PilotFeature.PLACEMENT_TERMINATION)
    )

    private final val snDaycareFullDay = ServiceNeedOption(
        id = ServiceNeedOptionId(UUID.randomUUID()),
        nameFi = "Kokopäiväinen",
        nameSv = "Kokopäiväinen",
        nameEn = "Kokopäiväinen",
        validPlacementType = PlacementType.DAYCARE,
        defaultOption = false,
        feeCoefficient = BigDecimal("1.00"),
        occupancyCoefficient = BigDecimal("1.00"),
        occupancyCoefficientUnder3y = BigDecimal("1.75"),
        realizedOccupancyCoefficient = BigDecimal("1.00"),
        realizedOccupancyCoefficientUnder3y = BigDecimal("1.75"),
        daycareHoursPerWeek = 35,
        contractDaysPerMonth = null,
        partDay = false,
        partWeek = false,
        feeDescriptionFi = "",
        feeDescriptionSv = "",
        voucherValueDescriptionFi = "",
        voucherValueDescriptionSv = "",
        active = true
    )

    private val getAllInvoices: (Database.Read) -> List<Invoice> = { r ->
        r.createQuery(
            """
            $invoiceQueryBase
            ORDER BY invoice.id, row.idx
            """.trimIndent()
        )
            .map(toInvoice)
            .list()
            .let(::flatten)
            .shuffled() // randomize order to expose assumptions
    }

    private final val testFeeThresholds = FeeThresholds(
        validDuring = DateRange(LocalDate.of(2000, 1, 1), null),
        maxFee = 28900,
        minFee = 2700,
        minIncomeThreshold2 = 210200,
        minIncomeThreshold3 = 271300,
        minIncomeThreshold4 = 308000,
        minIncomeThreshold5 = 344700,
        minIncomeThreshold6 = 381300,
        maxIncomeThreshold2 = 479900,
        maxIncomeThreshold3 = 541000,
        maxIncomeThreshold4 = 577700,
        maxIncomeThreshold5 = 614400,
        maxIncomeThreshold6 = 651000,
        incomeMultiplier2 = BigDecimal("0.1070"),
        incomeMultiplier3 = BigDecimal("0.1070"),
        incomeMultiplier4 = BigDecimal("0.1070"),
        incomeMultiplier5 = BigDecimal("0.1070"),
        incomeMultiplier6 = BigDecimal("0.1070"),
        incomeThresholdIncrease6Plus = 14200,
        siblingDiscount2 = BigDecimal("0.5000"),
        siblingDiscount2Plus = BigDecimal("0.8000"),
        temporaryFee = 2900,
        temporaryFeePartDay = 1500,
        temporaryFeeSibling = 1500,
        temporaryFeeSiblingPartDay = 800
    )

    private fun createFeeDecisionFixture(
        status: FeeDecisionStatus,
        decisionType: FeeDecisionType,
        period: DateRange,
        headOfFamilyId: PersonId,
        children: List<FeeDecisionChild>,
        partnerId: PersonId? = null,
        feeThresholds: FeeDecisionThresholds = testFeeThresholds.getFeeDecisionThresholds(children.size + 1),
        difference: Set<FeeDecisionDifference> = emptySet(),
        headOfFamilyIncome: DecisionIncome? = null
    ) = FeeDecision(
        id = FeeDecisionId(UUID.randomUUID()),
        status = status,
        decisionType = decisionType,
        validDuring = period,
        headOfFamilyId = headOfFamilyId,
        partnerId = partnerId,
        headOfFamilyIncome = headOfFamilyIncome,
        partnerIncome = null,
        familySize = children.size + 1,
        feeThresholds = feeThresholds,
        difference = difference,
        children = children,
    )

    private fun ServiceNeedOption.toFeeDecisionServiceNeed() = FeeDecisionServiceNeed(
        feeCoefficient = this.feeCoefficient,
        contractDaysPerMonth = this.contractDaysPerMonth,
        descriptionFi = this.feeDescriptionFi,
        descriptionSv = this.feeDescriptionSv,
        missing = this.defaultOption
    )

    private fun createFeeDecisionChildFixture(
        childId: ChildId,
        dateOfBirth: LocalDate,
        placementUnitId: DaycareId,
        placementType: PlacementType,
        serviceNeed: FeeDecisionServiceNeed,
        baseFee: Int = 28900,
        siblingDiscount: Int = 0,
        fee: Int = 28900,
        feeAlterations: List<FeeAlterationWithEffect> = listOf(),
    ) = FeeDecisionChild(
        child = ChildWithDateOfBirth(id = childId, dateOfBirth = dateOfBirth),
        placement = FeeDecisionPlacement(placementUnitId, placementType),
        serviceNeed = serviceNeed,
        baseFee = baseFee,
        siblingDiscount = siblingDiscount,
        fee = fee,
        feeAlterations = feeAlterations,
        finalFee = fee + feeAlterations.sumOf { it.effect },
        childIncome = null
    )

    private fun insertDecisionsAndPlacements(feeDecisions: List<FeeDecision>) = db.transaction { tx ->
        tx.upsertFeeDecisions(feeDecisions)
        feeDecisions.forEach { decision ->
            decision.children.forEach { part ->
                tx.insertTestPlacement(
                    childId = part.child.id,
                    unitId = part.placement.unitId,
                    startDate = decision.validFrom,
                    endDate = decision.validTo!!,
                    type = part.placement.type
                )
            }
        }
    }
}
