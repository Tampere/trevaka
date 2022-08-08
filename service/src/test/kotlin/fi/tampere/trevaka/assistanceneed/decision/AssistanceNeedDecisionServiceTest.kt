package fi.tampere.trevaka.assistanceneed.decision

import fi.espoo.evaka.assistanceneed.decision.AssistanceLevel
import fi.espoo.evaka.assistanceneed.decision.AssistanceNeedDecision
import fi.espoo.evaka.assistanceneed.decision.AssistanceNeedDecisionChild
import fi.espoo.evaka.assistanceneed.decision.AssistanceNeedDecisionLanguage
import fi.espoo.evaka.assistanceneed.decision.AssistanceNeedDecisionMaker
import fi.espoo.evaka.assistanceneed.decision.AssistanceNeedDecisionService
import fi.espoo.evaka.assistanceneed.decision.AssistanceNeedDecisionStatus
import fi.espoo.evaka.assistanceneed.decision.ServiceOptions
import fi.espoo.evaka.assistanceneed.decision.StructuralMotivationOptions
import fi.espoo.evaka.assistanceneed.decision.UnitInfo
import fi.espoo.evaka.shared.AssistanceNeedDecisionId
import fi.espoo.evaka.shared.ChildId
import fi.espoo.evaka.shared.DaycareId
import fi.espoo.evaka.shared.EmployeeId
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

        val bytes = assistanceNeedDecisionService.generatePdf(decision)

        val filepath = "${reportsPath}/AssistanceNeedDecisionServiceTest-assistance-need-decision.pdf"
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