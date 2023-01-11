// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.assistanceneed.decision

import fi.espoo.evaka.assistanceneed.decision.AssistanceLevel
import fi.espoo.evaka.assistanceneed.decision.AssistanceNeedDecision
import fi.espoo.evaka.assistanceneed.decision.AssistanceNeedDecisionChild
import fi.espoo.evaka.assistanceneed.decision.AssistanceNeedDecisionEmployee
import fi.espoo.evaka.assistanceneed.decision.AssistanceNeedDecisionLanguage
import fi.espoo.evaka.assistanceneed.decision.AssistanceNeedDecisionMaker
import fi.espoo.evaka.assistanceneed.decision.AssistanceNeedDecisionService
import fi.espoo.evaka.assistanceneed.decision.AssistanceNeedDecisionStatus
import fi.espoo.evaka.assistanceneed.decision.ServiceOptions
import fi.espoo.evaka.assistanceneed.decision.StructuralMotivationOptions
import fi.espoo.evaka.assistanceneed.decision.UnitInfo
import fi.espoo.evaka.decision.DecisionSendAddress
import fi.espoo.evaka.identity.ExternalIdentifier
import fi.espoo.evaka.invoicing.domain.PersonDetailed
import fi.espoo.evaka.pis.service.PersonDTO
import fi.espoo.evaka.shared.AssistanceNeedDecisionId
import fi.espoo.evaka.shared.ChildId
import fi.espoo.evaka.shared.DaycareId
import fi.espoo.evaka.shared.EmployeeId
import fi.espoo.evaka.shared.PersonId
import fi.espoo.evaka.shared.domain.DateRange
import fi.tampere.trevaka.AbstractIntegrationTest
import fi.tampere.trevaka.reportsPath
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.io.FileOutputStream
import java.time.LocalDate
import java.util.UUID

class AssistanceNeedDecisionServiceTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var assistanceNeedDecisionService: AssistanceNeedDecisionService

    @Test
    fun generatePdf() {
        val decision = validAssistanceNeedDecision
        val headOfFamily = validPersonDTO

        val bytes = assistanceNeedDecisionService.generatePdf(
            sentDate = LocalDate.of(2022, 9, 12),
            decision = decision,
            sendAddress = DecisionSendAddress.fromPerson(headOfFamily.toPersonDetailed()),
            guardian = headOfFamily
        )

        val filepath = "$reportsPath/AssistanceNeedDecisionServiceTest-assistance-need-decision.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generatePdfWithoutdGuardian() {
        val decision = validAssistanceNeedDecision

        val bytes = assistanceNeedDecisionService.generatePdf(
            sentDate = LocalDate.of(2022, 9, 12),
            decision = decision,
            sendAddress = null,
            guardian = null
        )

        val filepath =
            "$reportsPath/AssistanceNeedDecisionServiceTest-assistance-need-decision-without-guardian.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generatePdfWithPreparedBy() {
        val decision = validAssistanceNeedDecision.copy(
            preparedBy1 = AssistanceNeedDecisionEmployee(
                employeeId = EmployeeId(UUID.randomUUID()),
                title = "Palvelupäällikkö",
                name = "Vallu Valmistelija",
                phoneNumber = "0501234567"
            ),
            preparedBy2 = AssistanceNeedDecisionEmployee(
                employeeId = EmployeeId(UUID.randomUUID()),
                title = "Palvelupäällikkö",
                name = "Valle Valmistelija",
                phoneNumber = "0507654321"
            ),
        )
        val headOfFamily = validPersonDTO

        val bytes = assistanceNeedDecisionService.generatePdf(
            sentDate = LocalDate.of(2022, 9, 12),
            decision = decision,
            sendAddress = DecisionSendAddress.fromPerson(headOfFamily.toPersonDetailed()),
            guardian = headOfFamily
        )

        val filepath =
            "$reportsPath/AssistanceNeedDecisionServiceTest-assistance-need-decision-with-prepared-by.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }

    @Test
    fun generatePdfWithEmptyPreparedBy() {
        val preparedBy = AssistanceNeedDecisionEmployee(
            employeeId = null,
            title = null,
            name = null,
            phoneNumber = null,
        )
        val decision = validAssistanceNeedDecision.copy(
            preparedBy1 = preparedBy,
            preparedBy2 = preparedBy,
        )
        val headOfFamily = validPersonDTO

        val bytes = assistanceNeedDecisionService.generatePdf(
            sentDate = LocalDate.of(2022, 9, 12),
            decision = decision,
            sendAddress = DecisionSendAddress.fromPerson(headOfFamily.toPersonDetailed()),
            guardian = headOfFamily
        )

        val filepath =
            "$reportsPath/AssistanceNeedDecisionServiceTest-assistance-need-decision-with-empty-prepared-by.pdf"
        FileOutputStream(filepath).use { it.write(bytes) }
    }
}

private val validAssistanceNeedDecision = AssistanceNeedDecision(
    id = AssistanceNeedDecisionId(UUID.randomUUID()),
    decisionNumber = 125632424,
    child = AssistanceNeedDecisionChild(
        id = ChildId(UUID.randomUUID()),
        name = "Matti Meikäläinen",
        dateOfBirth = LocalDate.of(2020, 1, 5)
    ),
    validityPeriod = DateRange(LocalDate.of(2022, 8, 2), LocalDate.of(2022, 12, 31)),
    status = AssistanceNeedDecisionStatus.ACCEPTED,
    language = AssistanceNeedDecisionLanguage.FI,
    decisionMade = LocalDate.of(2022, 7, 1),
    sentForDecision = LocalDate.of(2022, 5, 12),
    selectedUnit = UnitInfo(
        id = DaycareId(UUID.randomUUID()),
        name = "Amurin päiväkoti",
        streetAddress = "Amurinpolku 1",
        postalCode = "33100",
        postOffice = "Tampere"
    ),
    preparedBy1 = null,
    preparedBy2 = null,
    decisionMaker = AssistanceNeedDecisionMaker(
        employeeId = EmployeeId(UUID.randomUUID()),
        title = "Asiakaspalvelupäällikkö",
        name = "Paula Palvelupäällikkö"
    ),
    pedagogicalMotivation = null,
    structuralMotivationOptions = StructuralMotivationOptions(
        smallerGroup = false,
        specialGroup = false,
        smallGroup = false,
        groupAssistant = false,
        childAssistant = false,
        additionalStaff = false
    ),
    structuralMotivationDescription = null,
    careMotivation = null,
    serviceOptions = ServiceOptions(
        consultationSpecialEd = false,
        partTimeSpecialEd = false,
        fullTimeSpecialEd = false,
        interpretationAndAssistanceServices = false,
        specialAides = false
    ),
    servicesMotivation = null,
    expertResponsibilities = null,
    guardiansHeardOn = null,
    guardianInfo = emptySet(),
    viewOfGuardians = null,
    otherRepresentativeHeard = false,
    otherRepresentativeDetails = null,
    assistanceLevels = setOf(AssistanceLevel.ENHANCED_ASSISTANCE),
    motivationForDecision = null,
    hasDocument = false
)

private val validPersonDTO = PersonDTO(
    id = PersonId(UUID.randomUUID()),
    identity = ExternalIdentifier.SSN.getInstance("310382-956D"),
    ssnAddingDisabled = false,
    firstName = "Maija",
    lastName = "Meikäläinen",
    preferredName = "Maija",
    email = null,
    phone = "",
    backupPhone = "",
    language = null,
    dateOfBirth = LocalDate.of(1982, 3, 31),
    dateOfDeath = null,
    streetAddress = "Meikäläisenkuja 6 B 7",
    postalCode = "33730",
    postOffice = "TAMPERE",
    residenceCode = ""
)

fun PersonDTO.toPersonDetailed() = PersonDetailed(
    id = this.id,
    dateOfBirth = this.dateOfBirth,
    dateOfDeath = this.dateOfDeath,
    firstName = this.firstName,
    lastName = this.lastName,
    ssn = this.identity.let {
        when (it) {
            is ExternalIdentifier.SSN -> it.toString()
            is ExternalIdentifier.NoID -> null
        }
    },
    streetAddress = this.streetAddress,
    postalCode = this.postalCode,
    postOffice = this.postOffice,
    residenceCode = this.residenceCode,
    email = this.email,
    phone = this.phone,
    language = this.language,
    invoiceRecipientName = this.invoiceRecipientName,
    invoicingStreetAddress = this.invoicingStreetAddress,
    invoicingPostalCode = this.invoicingPostalCode,
    invoicingPostOffice = this.invoicingPostOffice,
    restrictedDetailsEnabled = this.restrictedDetailsEnabled,
    forceManualFeeDecisions = this.forceManualFeeDecisions,
)
