// SPDX-FileCopyrightText: 2021-2023 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.assistanceneed.decision

import fi.espoo.evaka.assistanceneed.decision.AssistanceNeedDecisionStatus
import fi.espoo.evaka.assistanceneed.preschooldecision.AssistanceNeedPreschoolDecision
import fi.espoo.evaka.assistanceneed.preschooldecision.AssistanceNeedPreschoolDecisionChild
import fi.espoo.evaka.assistanceneed.preschooldecision.AssistanceNeedPreschoolDecisionForm
import fi.espoo.evaka.assistanceneed.preschooldecision.AssistanceNeedPreschoolDecisionGuardian
import fi.espoo.evaka.assistanceneed.preschooldecision.AssistanceNeedPreschoolDecisionService
import fi.espoo.evaka.assistanceneed.preschooldecision.AssistanceNeedPreschoolDecisionType
import fi.espoo.evaka.shared.AssistanceNeedPreschoolDecisionGuardianId
import fi.espoo.evaka.shared.AssistanceNeedPreschoolDecisionId
import fi.espoo.evaka.shared.ChildId
import fi.espoo.evaka.shared.DaycareId
import fi.espoo.evaka.shared.EmployeeId
import fi.espoo.evaka.shared.PersonId
import fi.espoo.evaka.shared.domain.OfficialLanguage
import fi.tampere.trevaka.AbstractTampereIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.util.UUID

class PreschoolAssistanceNeedDecisionServiceTest : AbstractTampereIntegrationTest() {

    @Autowired
    private lateinit var preschoolAssistanceNeedDecisionService: AssistanceNeedPreschoolDecisionService

    @Test
    fun generatePdf() {
        val bytes =
            preschoolAssistanceNeedDecisionService.generatePdf(
                sentDate = LocalDate.of(2022, 9, 12),
                decision = validAssistanceNeedPreschoolDecision,
                validTo = LocalDate.of(2022, 12, 31),
            )

        val filename = "PreschoolAssistanceNeedDecisionServiceTest-preschool-assistance-need-decision.pdf"
        writeReportsFile(filename, bytes)
    }

    @Test
    fun generatePdfWithoutGuardian() {
        val decision =
            validAssistanceNeedPreschoolDecision.copy(
                form = validAssistanceNeedPreschoolDecisionForm.copy(
                    guardianInfo = emptySet(),
                    guardiansHeardOn = null,
                    viewOfGuardians = "",
                ),
            )

        val bytes =
            preschoolAssistanceNeedDecisionService.generatePdf(
                sentDate = LocalDate.of(2022, 9, 12),
                decision = decision,
                validTo = LocalDate.of(2022, 12, 31),
            )

        val filename = "PreschoolAssistanceNeedDecisionServiceTest-preschool-assistance-need-decision-without-guardian.pdf"
        writeReportsFile(filename, bytes)
    }

    @Test
    fun generatePdfWithFullPreparerSection() {
        val form =
            validAssistanceNeedPreschoolDecisionForm.copy(
                preparer1EmployeeId = EmployeeId(UUID.randomUUID()),
                preparer1PhoneNumber = "050987654",
                preparer1Title = "VEO",
                preparer2EmployeeId = EmployeeId(UUID.randomUUID()),
                preparer2PhoneNumber = "050456789",
                preparer2Title = "VEO",
            )

        val decision =
            validAssistanceNeedPreschoolDecision.copy(
                preparer1Name = "Vallu Valmistelija",
                preparer2Name = "Veikko Valmistelija",
                form = form,
            )

        val bytes =
            preschoolAssistanceNeedDecisionService.generatePdf(
                sentDate = LocalDate.of(2022, 9, 12),
                decision = decision,
                validTo = LocalDate.of(2022, 12, 31),
            )

        val filename = "PreschoolAssistanceNeedDecisionServiceTest-preschool-assistance-need-decision-with-prepared-by.pdf"
        writeReportsFile(filename, bytes)
    }
}

private val validAssistanceNeedPreschoolDecisionForm =
    AssistanceNeedPreschoolDecisionForm(
        language = OfficialLanguage.FI,
        type = AssistanceNeedPreschoolDecisionType.NEW,
        validFrom = LocalDate.of(2022, 8, 2),
        validTo = null,
        extendedCompulsoryEducation = true,
        extendedCompulsoryEducationInfo = "Pidennetyn oppivelvollisuuden infosisältö",
        grantedAssistanceService = true,
        grantedInterpretationService = true,
        grantedAssistiveDevices = true,
        grantedServicesBasis = "Myönnettyjen palveluiden perusteiden sisältö",
        selectedUnit = DaycareId(UUID.randomUUID()),
        primaryGroup = "Ensisijainen ryhmä tähän",
        decisionBasis = "Päätöksentekoperuste tähän",
        basisDocumentPedagogicalReport = true,
        basisDocumentPsychologistStatement = true,
        basisDocumentSocialReport = true,
        basisDocumentDoctorStatement = true,
        basisDocumentOtherOrMissing = true,
        basisDocumentOtherOrMissingInfo =
        "Muun perustedokumentaation tai sen puutteen teksti tähän",
        basisDocumentsInfo = "Perustedokumentaation infoteksti tähän",
        guardiansHeardOn = LocalDate.of(2022, 8, 2),
        guardianInfo =
        setOf(
            AssistanceNeedPreschoolDecisionGuardian(
                id = AssistanceNeedPreschoolDecisionGuardianId(UUID.randomUUID()),
                personId = PersonId(UUID.randomUUID()),
                name = "Testaaja Huoltaja",
                isHeard = true,
                details = "Huoltajayksityiskohtia tähän",
            ),
        ),
        otherRepresentativeHeard = false,
        otherRepresentativeDetails = "",
        viewOfGuardians = "Huoltajien näkemys tähän",
        preparer1EmployeeId = EmployeeId(UUID.randomUUID()),
        preparer1Title = "worker",
        preparer1PhoneNumber = "01020405060",
        preparer2EmployeeId = null,
        preparer2Title = "",
        preparer2PhoneNumber = "",
        decisionMakerEmployeeId = EmployeeId(UUID.randomUUID()),
        decisionMakerTitle = "Päätöksentekijän titteli",
        basisDocumentDoctorStatementDate = LocalDate.of(2022, 8, 2),
        basisDocumentPedagogicalReportDate = LocalDate.of(2022, 8, 2),
        basisDocumentPsychologistStatementDate = LocalDate.of(2022, 8, 2),
        basisDocumentSocialReportDate = LocalDate.of(2022, 8, 2),
    )

val validAssistanceNeedPreschoolDecision =
    AssistanceNeedPreschoolDecision(
        id = AssistanceNeedPreschoolDecisionId(UUID.randomUUID()),
        decisionNumber = 125632424,
        child =
        AssistanceNeedPreschoolDecisionChild(
            id = ChildId(UUID.randomUUID()),
            name = "Matti Meikäläinen",
            dateOfBirth = LocalDate.of(2020, 1, 5),
        ),
        status = AssistanceNeedDecisionStatus.ACCEPTED,
        form = validAssistanceNeedPreschoolDecisionForm,
        decisionMade = LocalDate.of(2022, 7, 1),
        sentForDecision = LocalDate.of(2022, 5, 12),
        annulmentReason = "",
        hasDocument = false,
        decisionMakerHasOpened = true,
        decisionMakerName = "Make Maker",
        preparer1Name = "Valmistelija 1",
        preparer2Name = "Valmistelija 2",
        unitName = "Kelokujan lapsiparkki",
        unitPostalCode = "22222",
        unitPostOffice = "Parkkila",
        unitStreetAddress = "Kelokuja 122 G",
    )
