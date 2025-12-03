// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.nokiankaupunki.evaka.archival

import fi.espoo.evaka.application.*
import fi.espoo.evaka.caseprocess.*
import fi.espoo.evaka.daycare.domain.ProviderType
import fi.espoo.evaka.decision.Decision
import fi.espoo.evaka.decision.DecisionStatus
import fi.espoo.evaka.decision.DecisionType
import fi.espoo.evaka.decision.DecisionUnit
import fi.espoo.evaka.document.ChildDocumentType
import fi.espoo.evaka.document.DocumentTemplate
import fi.espoo.evaka.document.DocumentTemplateContent
import fi.espoo.evaka.document.archival.ArchivalIntegrationClient
import fi.espoo.evaka.document.childdocument.ChildBasics
import fi.espoo.evaka.document.childdocument.ChildDocumentDetails
import fi.espoo.evaka.document.childdocument.DocumentContent
import fi.espoo.evaka.document.childdocument.DocumentStatus
import fi.espoo.evaka.identity.ExternalIdentifier
import fi.espoo.evaka.invoicing.domain.*
import fi.espoo.evaka.pis.service.PersonDTO
import fi.espoo.evaka.placement.PlacementType
import fi.espoo.evaka.s3.Document
import fi.espoo.evaka.s3.DocumentKey
import fi.espoo.evaka.shared.*
import fi.espoo.evaka.shared.auth.AuthenticatedUser
import fi.espoo.evaka.shared.dev.feeThresholds2020
import fi.espoo.evaka.shared.domain.*
import fi.espoo.evaka.shared.sftp.SftpClient
import fi.espoo.evaka.user.EvakaUser
import fi.espoo.evaka.user.EvakaUserType
import fi.nokiankaupunki.evaka.AbstractNokiaIntegrationTest
import fi.nokiankaupunki.evaka.NokiaProperties
import fi.tampere.trevaka.assistanceneed.decision.toPersonDetailed
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import java.math.BigDecimal
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class NokiaArchivalClientTest : AbstractNokiaIntegrationTest() {

    @Autowired
    private lateinit var archivalIntegrationClient: ArchivalIntegrationClient

    @Autowired
    private lateinit var nokiaProperties: NokiaProperties

    @Test
    fun uploadDecision() {
        val archival = nokiaProperties.archival ?: error("No archival configuration")
        val archiveId = archivalIntegrationClient.uploadDecisionToArchive(
            testCaseProcessApplication,
            testChildInfo,
            testDecisionDaycare,
            testDocumentDecisionDaycare,
            testEvakaUser,
        )
        assertEquals("static-sftp-response", archiveId)

        val sftpClient = SftpClient(archival.sftp.toSftpEnv())
        val metadata =
            sftpClient.getAsString("${archival.sftp.prefix}${testDecisionDaycare.id}.xml", StandardCharsets.UTF_8)

        val document =
            sftpClient.getAsString("${archival.sftp.prefix}${testDecisionDaycare.id}.txt", StandardCharsets.UTF_8)
        assertEquals(
            ClassPathResource("tweb-archival-client/decision-metadata.xml")
                .getContentAsString(StandardCharsets.UTF_8),
            metadata,
        )
        assertEquals(
            "vakapäätös tekstitiedostona",
            document,
        )
    }

    @Test
    fun uploadChildDocumentWithoutHistory() {
        assertThrows<IllegalStateException> {
            archivalIntegrationClient.uploadDecisionToArchive(
                noHistoryTestCaseProcessApplication,
                testChildInfo,
                testDecisionDaycare,
                testDocumentDecisionDaycare,
                testEvakaUser,
            )
        }
    }

    @Test
    fun uploadDecisionWithoutHistory() {
        assertThrows<IllegalStateException> {
            archivalIntegrationClient.uploadChildDocumentToArchive(
                testVasuDetails.id,
                emptyTestCaseProcessChildDocument,
                testChildInfo,
                testVasuDetails,
                testDocumentMetadataChildDocument,
                testDocumentChildDocument,
                testEvakaUser,
            )
        }
    }

    @Test
    fun uploadFeeDecisionToArchive() {
        val archival = nokiaProperties.archival ?: error("No archival configuration")
        val archiveId = archivalIntegrationClient.uploadFeeDecisionToArchive(
            testCaseProcessFeeDecision,
            testFeeDecision,
            testDocumentFeeDecision,
            testEvakaUser,
        )

        assertEquals("static-sftp-response", archiveId)

        val sftpClient = SftpClient(archival.sftp.toSftpEnv())
        val metadata =
            sftpClient.getAsString("${archival.sftp.prefix}${testFeeDecision.id}.xml", StandardCharsets.UTF_8)

        val document =
            sftpClient.getAsString("${archival.sftp.prefix}${testFeeDecision.id}.txt", StandardCharsets.UTF_8)
        assertEquals(
            ClassPathResource("tweb-archival-client/fee-decision-metadata.xml")
                .getContentAsString(StandardCharsets.UTF_8),
            metadata,
        )
        assertEquals(
            "maksupäätös tekstitiedostona",
            document,
        )
    }

    @Test
    fun uploadVoucherValueDecisionToArchive() {
        val archival = nokiaProperties.archival ?: error("No archival configuration")
        val archiveId = archivalIntegrationClient.uploadVoucherValueDecisionToArchive(
            testCaseProcessApplication,
            testVoucherValueDecision,
            testDocumentVoucherValueDecision,
            testEvakaUser,
        )

        assertEquals("static-sftp-response", archiveId)

        val sftpClient = SftpClient(archival.sftp.toSftpEnv())
        val metadata =
            sftpClient.getAsString("${archival.sftp.prefix}${testVoucherValueDecision.id}.xml", StandardCharsets.UTF_8)

        val document =
            sftpClient.getAsString("${archival.sftp.prefix}${testVoucherValueDecision.id}.txt", StandardCharsets.UTF_8)
        assertEquals(
            ClassPathResource("tweb-archival-client/voucher-decision-metadata.xml")
                .getContentAsString(StandardCharsets.UTF_8),
            metadata,
        )
        assertEquals(
            "arvopäätös tekstitiedostona",
            document,
        )
    }

    @Test
    fun uploadChildDocument() {
        val archival = nokiaProperties.archival ?: error("No archival configuration")
        val archiveId = archivalIntegrationClient.uploadChildDocumentToArchive(
            testVasuDetails.id,
            fullTestCaseProcessChildDocument,
            testChildInfo,
            testVasuDetails,
            testDocumentMetadataChildDocument,
            testDocumentChildDocument,
            testEvakaUser,
        )
        assertEquals("static-sftp-response", archiveId)

        val sftpClient = SftpClient(archival.sftp.toSftpEnv())
        val metadata =
            sftpClient.getAsString("${archival.sftp.prefix}${testVasuDetails.id}.xml", StandardCharsets.UTF_8)

        val document =
            sftpClient.getAsString("${archival.sftp.prefix}${testVasuDetails.id}.txt", StandardCharsets.UTF_8)
        assertEquals(
            ClassPathResource("tweb-archival-client/child-document-metadata.xml")
                .getContentAsString(StandardCharsets.UTF_8),
            metadata,
        )
        assertEquals(
            "vasu tekstitiedostona",
            document,
        )
    }

    @Test
    fun `uploadChildDocument without child ssn`() {
        assertThrows<IllegalStateException> {
            archivalIntegrationClient.uploadChildDocumentToArchive(
                testVasuDetails.id,
                fullTestCaseProcessChildDocument,
                testNoSsnChildInfo,
                testVasuDetails,
                testDocumentMetadataChildDocument,
                testDocumentChildDocument,
                testEvakaUser,
            )
        }
    }

    @Test
    fun `uploadFeeDecision without hof ssn`() {
        assertThrows<IllegalStateException> {
            archivalIntegrationClient.uploadFeeDecisionToArchive(
                testCaseProcessApplication,
                testFeeDecision.copy(headOfFamily = noSsnTestAdultInfo.toPersonDetailed()),
                testDocumentFeeDecision,
                testEvakaUser,
            )
        }
    }
}

private fun PersonDTO.toChildBasics() = ChildBasics(id, firstName, lastName, dateOfBirth)
private fun PersonDTO.toChildDetails() = ChildDetails(
    PersonBasics(firstName, lastName, identity.toString()),
    dateOfBirth,
    null,
    null,
    "FI",
    "fi",
    allergies = "",
    diet = "",
    false,
    "",
)

private fun PersonDTO.toGuardian() = Guardian(PersonBasics(firstName, lastName, identity.toString()), null, null, "", null)

private fun ChildDocumentDetails.toDocumentMetadata() = DocumentMetadata(
    documentId = template.id.raw,
    name = template.name,
    createdAtDate = null,
    createdAtTime = null,
    createdBy = null,
    confidential = template.confidentiality != null,
    confidentiality = template.confidentiality,
    downloadPath = "/employee/child-documents/$id/pdf",
    receivedBy = null,
    sfiDeliveries = emptyList(),
)

private val testChildInfo = PersonDTO(
    id = PersonId(UUID.randomUUID()),
    duplicateOf = null,
    identity = ExternalIdentifier.SSN.getInstance("081222A9859"),
    ssnAddingDisabled = false,
    firstName = "John",
    lastName = "Smith",
    preferredName = "John",
    email = null,
    phone = "",
    backupPhone = "",
    language = null,
    dateOfBirth = LocalDate.of(2022, 12, 8),
    dateOfDeath = null,
    streetAddress = "",
    postalCode = "",
    postOffice = "",
    residenceCode = "",
    municipalityOfResidence = "",
)

private val testNoSsnChildInfo = PersonDTO(
    id = PersonId(UUID.randomUUID()),
    duplicateOf = null,
    identity = ExternalIdentifier.NoID,
    ssnAddingDisabled = false,
    firstName = "John",
    lastName = "Smith",
    preferredName = "John",
    email = null,
    phone = "",
    backupPhone = "",
    language = null,
    dateOfBirth = LocalDate.of(2022, 12, 8),
    dateOfDeath = null,
    streetAddress = "",
    postalCode = "",
    postOffice = "",
    residenceCode = "",
    municipalityOfResidence = "",
)

private val testAdultInfo = PersonDTO(
    id = PersonId(UUID.randomUUID()),
    duplicateOf = null,
    identity = ExternalIdentifier.SSN.getInstance("020998-958R"),
    ssnAddingDisabled = false,
    firstName = "Jane",
    lastName = "Smith",
    preferredName = "Jane",
    email = null,
    phone = "",
    backupPhone = "",
    language = null,
    dateOfBirth = LocalDate.of(1998, 9, 2),
    dateOfDeath = null,
    streetAddress = "",
    postalCode = "",
    postOffice = "",
    residenceCode = "",
    municipalityOfResidence = "",
)

private val noSsnTestAdultInfo = testAdultInfo.copy(identity = ExternalIdentifier.NoID)

private val testApplicationDaycare = ApplicationDetails(
    id = ApplicationId(UUID.randomUUID()),
    type = ApplicationType.DAYCARE,
    form = ApplicationForm(
        child = testChildInfo.toChildDetails(),
        guardian = testAdultInfo.toGuardian(),
        secondGuardian = null,
        otherPartner = null,
        otherChildren = emptyList(),
        preferences = Preferences(
            preferredUnits = emptyList(),
            preferredStartDate = null,
            connectedDaycarePreferredStartDate = null,
            serviceNeed = null,
            siblingBasis = null,
            preparatory = false,
            urgent = false,
        ),
        maxFeeAccepted = false,
        otherInfo = "",
        clubDetails = null,
    ),
    status = ApplicationStatus.ACTIVE,
    origin = ApplicationOrigin.ELECTRONIC,
    childId = testChildInfo.id,
    guardianId = testAdultInfo.id,
    hasOtherGuardian = false,
    otherGuardianLivesInSameAddress = false,
    childRestricted = false,
    guardianRestricted = false,
    guardianDateOfDeath = null,
    checkedByAdmin = false,
    confidential = false,
    createdAt = HelsinkiDateTime.of(LocalDate.of(2022, 1, 1), LocalTime.of(8, 55)),
    createdBy = null,
    modifiedAt = HelsinkiDateTime.of(LocalDate.of(2022, 1, 2), LocalTime.of(11, 12)),
    modifiedBy = null,
    sentDate = LocalDate.of(2022, 1, 3),
    dueDate = LocalDate.of(2022, 5, 3),
    dueDateSetManuallyAt = null,
    transferApplication = false,
    additionalDaycareApplication = false,
    hideFromGuardian = false,
    allowOtherGuardianAccess = false,
    attachments = emptyList(),
)

private val testDecisionDaycare = Decision(
    id = DecisionId(UUID.fromString("c74c1dad-f448-41ce-83af-e37d0c095286")),
    createdBy = "todo",
    type = DecisionType.DAYCARE,
    startDate = LocalDate.of(2022, 2, 1),
    endDate = LocalDate.of(2022, 7, 31),
    unit = DecisionUnit(
        id = DaycareId(UUID.randomUUID()),
        name = "asd",
        daycareDecisionName = "asd",
        preschoolDecisionName = "asd",
        manager = null,
        streetAddress = "",
        postalCode = "",
        postOffice = "",
        phone = null,
        decisionHandler = "",
        decisionHandlerAddress = "",
        providerType = ProviderType.MUNICIPAL,
    ),
    applicationId = testApplicationDaycare.id,
    childId = testChildInfo.id,
    childName = "${testChildInfo.lastName} ${testChildInfo.firstName}",
    documentKey = null,
    decisionNumber = 2398437,
    sentDate = LocalDate.of(2022, 1, 8),
    status = DecisionStatus.ACCEPTED,
    requestedStartDate = null,
    resolved = null,
    resolvedByName = null,
    documentContainsContactInfo = false,
    archivedAt = null,
)

private val testDocumentDecisionDaycare = Document(
    DocumentKey.Decision(testDecisionDaycare.id, testDecisionDaycare.type, OfficialLanguage.FI).value,
    "vakapäätös tekstitiedostona".toByteArray(Charsets.UTF_8),
    "text/plain",
)

private val testFeeDecision = FeeDecisionDetailed(
    id = FeeDecisionId(UUID.fromString("385132a2-4be4-4b52-a3a6-08f27a1270e1")),
    children = emptyList(),
    validDuring = FiniteDateRange(LocalDate.of(2022, 2, 1), LocalDate.of(2022, 7, 31)),
    status = FeeDecisionStatus.SENT,
    decisionType = FeeDecisionType.NORMAL,
    headOfFamily = testAdultInfo.toPersonDetailed(),
    partner = null,
    headOfFamilyIncome = null,
    partnerIncome = null,
    familySize = 2,
    approvedAt = HelsinkiDateTime.of(LocalDate.of(2020, 1, 15), LocalTime.of(14, 43)),
    sentAt = HelsinkiDateTime.of(LocalDate.of(2020, 1, 5), LocalTime.of(8, 27)),
    feeThresholds = feeThresholds2020.getFeeDecisionThresholds(2),
    financeDecisionHandlerFirstName = "",
    financeDecisionHandlerLastName = "",
    documentContainsContactInfo = false,
    archivedAt = null,
)

private val testDocumentFeeDecision = Document(
    DocumentKey.FeeDecision(testFeeDecision.id, OfficialLanguage.FI).value,
    "maksupäätös tekstitiedostona".toByteArray(Charsets.UTF_8),
    "text/plain",
)

private val testVoucherValueDecision = VoucherValueDecisionDetailed(
    id = VoucherValueDecisionId(UUID.fromString("11479434-12da-4c59-a1e1-d9cd58c203a2")),
    validFrom = LocalDate.of(2022, 2, 1),
    validTo = LocalDate.of(2022, 7, 31),
    status = VoucherValueDecisionStatus.SENT,
    decisionType = VoucherValueDecisionType.NORMAL,
    headOfFamily = testAdultInfo.toPersonDetailed(),
    partner = null,
    headOfFamilyIncome = null,
    partnerIncome = null,
    childIncome = null,
    familySize = 2,
    feeThresholds = feeThresholds2020.getFeeDecisionThresholds(2),
    child = testChildInfo.toPersonDetailed(),
    placement = VoucherValueDecisionPlacementDetailed(
        unit = UnitData(
            id = DaycareId(UUID.randomUUID()),
            name = "test daycare",
            areaId = AreaId(UUID.randomUUID()),
            areaName = "test area",
            language = "fi",
        ),
        type = PlacementType.DAYCARE,
    ),
    serviceNeed = VoucherValueDecisionServiceNeed(
        feeCoefficient = BigDecimal.ZERO,
        voucherValueCoefficient = BigDecimal.ZERO,
        feeDescriptionFi = "",
        feeDescriptionSv = "",
        voucherValueDescriptionFi = "",
        voucherValueDescriptionSv = "",
        missing = false,
    ),
    baseCoPayment = 0,
    siblingDiscount = 0,
    coPayment = 0,
    feeAlterations = emptyList(),
    finalCoPayment = 0,
    baseValue = 0,
    childAge = 0,
    assistanceNeedCoefficient = BigDecimal.ZERO,
    voucherValue = 0,
    approvedAt = HelsinkiDateTime.of(LocalDate.of(2020, 1, 15), LocalTime.of(14, 43)),
    sentAt = HelsinkiDateTime.of(LocalDate.of(2020, 1, 5), LocalTime.of(8, 27)),
    financeDecisionHandlerFirstName = "",
    financeDecisionHandlerLastName = "",
    documentContainsContactInfo = false,
    archivedAt = null,
)

private val testDocumentVoucherValueDecision = Document(
    DocumentKey.VoucherValueDecision(testVoucherValueDecision.id).value,
    "arvopäätös tekstitiedostona".toByteArray(Charsets.UTF_8),
    "text/plain",
)

private val testVasuDetails = ChildDocumentDetails(
    id = ChildDocumentId(UUID.fromString("8554e2a5-29bb-4e3c-9aca-59c4995c1d86")),
    status = DocumentStatus.COMPLETED,
    publishedAt = HelsinkiDateTime.of(LocalDate.of(2025, 5, 12), LocalTime.of(8, 45)),
    archivedAt = null,
    pdfAvailable = true,
    content = DocumentContent(answers = emptyList()),
    publishedContent = DocumentContent(answers = emptyList()),
    child = testChildInfo.toChildBasics(),
    template = DocumentTemplate(
        id = DocumentTemplateId(UUID.randomUUID()),
        name = "Varhaiskasvatussuunnitelma 2024-2025",
        type = ChildDocumentType.VASU,
        placementTypes = setOf(PlacementType.DAYCARE, PlacementType.DAYCARE_PART_TIME),
        language = UiLanguage.FI,
        confidentiality = DocumentConfidentiality(durationYears = 100, basis = "Varhaiskasvatuslaki 40 § 3 mom."),
        legalBasis = "Varhaiskasvatuslaki (540/2018) 40§:n 3 mom.",
        validity = DateRange(LocalDate.of(2024, 8, 1), LocalDate.of(2025, 7, 31)),
        published = true,
        processDefinitionNumber = "04.01.00.11",
        archiveDurationMonths = 1440,
        archiveExternally = true,
        endDecisionWhenUnitChanges = false,
        content = DocumentTemplateContent(sections = emptyList()),
    ),
    decisionMaker = null,
    decision = null,
)

private val emptyTestCaseProcessChildDocument = null

private val testDocumentMetadataChildDocument = testVasuDetails.toDocumentMetadata()

private val testDocumentChildDocument = Document(
    DocumentKey.ChildDocument(testVasuDetails.id).value,
    "vasu tekstitiedostona".toByteArray(Charsets.UTF_8),
    "text/plain",
)

private val testEvakaUser = EvakaUser(AuthenticatedUser.SystemInternalUser.evakaUserId, "eVaka", EvakaUserType.SYSTEM)

private val testPreparerUser = EvakaUser(
    id = EvakaUserId(UUID.randomUUID()),
    name = "Esko Esivalmistelija",
    EvakaUserType.EMPLOYEE,
)

private val testDeciderUser = EvakaUser(
    id = EvakaUserId(UUID.randomUUID()),
    name = "Pänü Päättäjä",
    EvakaUserType.EMPLOYEE,
)

private val fullCaseProcessHistory =
    listOf(
        CaseProcessHistoryRow(
            rowIndex = 1,
            state = CaseProcessState.INITIAL,
            enteredAt = HelsinkiDateTime.of(LocalDate.of(2025, 5, 12), LocalTime.of(8, 45)),
            enteredBy = testPreparerUser,
        ),
        CaseProcessHistoryRow(
            rowIndex = 2,
            state = CaseProcessState.PREPARATION,
            enteredAt = HelsinkiDateTime.of(LocalDate.of(2025, 5, 12), LocalTime.of(8, 45)),
            enteredBy = testPreparerUser,
        ),
        CaseProcessHistoryRow(
            rowIndex = 3,
            state = CaseProcessState.DECIDING,
            enteredAt = HelsinkiDateTime.of(LocalDate.of(2025, 5, 12), LocalTime.of(8, 55)),
            enteredBy = testDeciderUser,
        ),
        CaseProcessHistoryRow(
            rowIndex = 4,
            state = CaseProcessState.COMPLETED,
            enteredAt = HelsinkiDateTime.of(LocalDate.of(2025, 5, 12), LocalTime.of(8, 55)),
            enteredBy = testEvakaUser,
        ),
    )

private val testCaseProcessApplication = CaseProcess(
    id = CaseProcessId(UUID.randomUUID()),
    caseIdentifier = "1/04.01.00.11/2025",
    processDefinitionNumber = "04.01.00.11",
    year = 2025,
    number = 1,
    organization = "Nokian kaupunki, varhaiskasvatus ja esiopetus",
    archiveDurationMonths = 10 * 12,
    migrated = false,
    history = fullCaseProcessHistory,
)

private val testCaseProcessFeeDecision = CaseProcess(
    id = CaseProcessId(UUID.randomUUID()),
    caseIdentifier = "1/04.01.00.12/2025",
    processDefinitionNumber = "04.01.00.12",
    year = 2025,
    number = 1,
    organization = "Nokian kaupunki, varhaiskasvatus ja esiopetus",
    archiveDurationMonths = 10 * 12,
    migrated = false,
    history = fullCaseProcessHistory,
)

private val noHistoryTestCaseProcessApplication =
    testCaseProcessApplication.copy(history = emptyList())

private val fullTestCaseProcessChildDocument = CaseProcess(
    id = CaseProcessId(UUID.randomUUID()),
    caseIdentifier = "1/04.01.00.11/2025",
    processDefinitionNumber = "04.01.00.11",
    year = 2025,
    number = 1,
    organization = "Varhaiskasvatustoiminta",
    archiveDurationMonths = 12 * 10,
    migrated = false,
    history = testCaseProcessApplication.history,
)
