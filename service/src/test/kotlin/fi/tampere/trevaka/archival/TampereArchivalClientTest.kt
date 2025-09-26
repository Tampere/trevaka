// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.archival

import com.github.tomakehurst.wiremock.client.BasicCredentials
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.containing
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.equalToXml
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.github.tomakehurst.wiremock.matching.MultipartValuePatternBuilder
import fi.espoo.evaka.application.ApplicationDetails
import fi.espoo.evaka.application.ApplicationForm
import fi.espoo.evaka.application.ApplicationOrigin
import fi.espoo.evaka.application.ApplicationStatus
import fi.espoo.evaka.application.ApplicationType
import fi.espoo.evaka.application.ChildDetails
import fi.espoo.evaka.application.Guardian
import fi.espoo.evaka.application.PersonBasics
import fi.espoo.evaka.application.Preferences
import fi.espoo.evaka.caseprocess.CaseProcess
import fi.espoo.evaka.caseprocess.DocumentConfidentiality
import fi.espoo.evaka.caseprocess.DocumentMetadata
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
import fi.espoo.evaka.pis.service.PersonDTO
import fi.espoo.evaka.placement.PlacementType
import fi.espoo.evaka.s3.Document
import fi.espoo.evaka.s3.DocumentKey
import fi.espoo.evaka.shared.ApplicationId
import fi.espoo.evaka.shared.CaseProcessId
import fi.espoo.evaka.shared.ChildDocumentId
import fi.espoo.evaka.shared.DaycareId
import fi.espoo.evaka.shared.DecisionId
import fi.espoo.evaka.shared.DocumentTemplateId
import fi.espoo.evaka.shared.PersonId
import fi.espoo.evaka.shared.auth.AuthenticatedUser
import fi.espoo.evaka.shared.domain.DateRange
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.espoo.evaka.shared.domain.OfficialLanguage
import fi.espoo.evaka.shared.domain.UiLanguage
import fi.espoo.evaka.user.EvakaUser
import fi.espoo.evaka.user.EvakaUserType
import fi.tampere.trevaka.AbstractTampereIntegrationTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class TampereArchivalClientTest : AbstractTampereIntegrationTest() {

    @Autowired
    private lateinit var archivalIntegrationClient: ArchivalIntegrationClient

    @Test
    fun uploadDecision() {
        stubFor(
            post(urlEqualTo("/mock/frends/archival/records/add")).willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/xml")
                    .withBodyFile("archival-client/archival-response-success.xml"),
            ),
        )

        val archiveId = archivalIntegrationClient.uploadDecisionToArchive(
            testCaseProcessApplication,
            testChildInfo,
            testDecisionDaycare,
            testDocumentDecisionDaycare,
            testEvakaUser,
        )
        assertEquals("archive-record-id-1", archiveId)

        verify(
            postRequestedFor(urlEqualTo("/mock/frends/archival/records/add"))
                .withBasicAuth(BasicCredentials("frends-user", "frends-pass"))
                .withHeader("Content-Type", containing("multipart/form-data; boundary="))
                .withoutHeader("X-API-key")
                .withoutHeader("X-API-transactionid") // only set when running AsyncJob
                .withRequestBodyPart(
                    MultipartValuePatternBuilder()
                        .withHeader("Content-Disposition", equalTo("form-data; name=\"xml\""))
                        .withHeader("Content-Type", equalTo("application/xml; charset=utf-8"))
                        .withBody(equalToXml(ClassPathResource("archival-client/archival-post-record-request-decision-daycare.xml").getContentAsString(Charsets.UTF_8)))
                        .build(),
                )
                .withRequestBodyPart(
                    MultipartValuePatternBuilder()
                        .withHeader("Content-Disposition", equalTo("form-data; name=\"content\"; filename=\"${testDecisionDaycare.id}\""))
                        .withHeader("Content-Type", equalTo("text/plain"))
                        .withBody(equalTo("vakapäätös tekstitiedostona"))
                        .build(),
                ),
        )
    }

    @Test
    fun uploadChildDocument() {
        stubFor(
            post(urlEqualTo("/mock/frends/archival/records/add")).willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/xml")
                    .withBodyFile("archival-client/archival-response-success.xml"),
            ),
        )

        val archiveId = archivalIntegrationClient.uploadChildDocumentToArchive(
            testVasuDetails.id,
            testCaseProcessChildDocument,
            testChildInfo,
            testVasuDetails,
            testDocumentMetadataChildDocument,
            testDocumentChildDocument,
            testEvakaUser,
        )
        assertEquals("archive-record-id-1", archiveId)

        verify(
            postRequestedFor(urlEqualTo("/mock/frends/archival/records/add"))
                .withBasicAuth(BasicCredentials("frends-user", "frends-pass"))
                .withHeader("Content-Type", containing("multipart/form-data; boundary="))
                .withoutHeader("X-API-key")
                .withoutHeader("X-API-transactionid") // only set when running AsyncJob
                .withRequestBodyPart(
                    MultipartValuePatternBuilder()
                        .withHeader("Content-Disposition", equalTo("form-data; name=\"xml\""))
                        .withHeader("Content-Type", equalTo("application/xml; charset=utf-8"))
                        .withBody(equalToXml(ClassPathResource("archival-client/archival-post-record-request-child-document.xml").getContentAsString(Charsets.UTF_8)))
                        .build(),
                )
                .withRequestBodyPart(
                    MultipartValuePatternBuilder()
                        .withHeader("Content-Disposition", equalTo("form-data; name=\"content\"; filename=\"${testVasuDetails.id}\""))
                        .withHeader("Content-Type", equalTo("text/plain"))
                        .withBody(equalTo("vasu tekstitiedostona"))
                        .build(),
                ),
        )
    }

    @Test
    fun `uploadChildDocument with unexpected response body`() {
        stubFor(
            post(urlEqualTo("/mock/frends/archival/records/add")).willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/xml")
                    .withBodyFile("archival-client/archival-response-unexpected.xml"),
            ),
        )

        assertNull(
            archivalIntegrationClient.uploadChildDocumentToArchive(
                testVasuDetails.id,
                testCaseProcessChildDocument,
                testChildInfo,
                testVasuDetails,
                testDocumentMetadataChildDocument,
                testDocumentChildDocument,
                testEvakaUser,
            ),
        )

        verify(
            postRequestedFor(urlEqualTo("/mock/frends/archival/records/add"))
                .withBasicAuth(BasicCredentials("frends-user", "frends-pass"))
                .withHeader("Content-Type", containing("multipart/form-data; boundary="))
                .withoutHeader("X-API-key")
                .withoutHeader("X-API-transactionid") // only set when running AsyncJob
                .withRequestBodyPart(
                    MultipartValuePatternBuilder()
                        .withHeader("Content-Disposition", equalTo("form-data; name=\"xml\""))
                        .withHeader("Content-Type", equalTo("application/xml; charset=utf-8"))
                        .withBody(equalToXml(ClassPathResource("archival-client/archival-post-record-request-child-document.xml").getContentAsString(Charsets.UTF_8)))
                        .build(),
                )
                .withRequestBodyPart(
                    MultipartValuePatternBuilder()
                        .withHeader("Content-Disposition", equalTo("form-data; name=\"content\"; filename=\"${testVasuDetails.id}\""))
                        .withHeader("Content-Type", equalTo("text/plain"))
                        .withBody(equalTo("vasu tekstitiedostona"))
                        .build(),
                ),
        )
    }

    @Test
    fun `error invalid apikey`() {
        stubFor(
            post(urlEqualTo("/mock/frends/archival/records/add")).willReturn(
                aResponse()
                    .withStatus(401)
                    .withHeader("Content-Type", "application/xml")
                    .withBodyFile("archival-client/archival-response-invalid-apikey.xml"),
            ),
        )

        val exception = assertThrows<IllegalStateException> {
            archivalIntegrationClient.uploadChildDocumentToArchive(
                testVasuDetails.id,
                testCaseProcessChildDocument,
                testChildInfo,
                testVasuDetails,
                testDocumentMetadataChildDocument,
                testDocumentChildDocument,
                testEvakaUser,
            )
        }
        assertEquals(
            "Unsuccessfully post record (status=401), response body: Error(errorCode=invalid_apikey, errorSummary=API-avaimella ei ole oikeutta kirjoittaa kansioon)",
            exception.message,
        )

        verify(
            postRequestedFor(urlEqualTo("/mock/frends/archival/records/add")),
        )
    }
}

private fun PersonDTO.toChildBasics() = ChildBasics(id, firstName, lastName, dateOfBirth)
private fun PersonDTO.toChildDetails() = ChildDetails(PersonBasics(firstName, lastName, identity.toString()), dateOfBirth, null, null, "FI", "fi", allergies = "", diet = "", false, "")
private fun PersonDTO.toGuardian() = Guardian(PersonBasics(firstName, lastName, identity.toString()), null, null, "", null)
private fun ChildDocumentDetails.toDocumentMetadata() = DocumentMetadata(
    documentId = template.id.raw,
    name = template.name,
    createdAt = null,
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

private val testCaseProcessApplication = CaseProcess(
    id = CaseProcessId(UUID.randomUUID()),
    caseIdentifier = "1/12.06.01.17/2025",
    processDefinitionNumber = "12.06.01.17",
    year = 2025,
    number = 1,
    organization = "Tampereen kaupunki, varhaiskasvatus ja esiopetus",
    archiveDurationMonths = 10 * 12,
    migrated = false,
    history = emptyList(),
)

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
        processDefinitionNumber = "12.06.01.11",
        archiveDurationMonths = 1440,
        archiveExternally = true,
        endDecisionWhenUnitChanges = false,
        content = DocumentTemplateContent(sections = emptyList()),
    ),
    decisionMaker = null,
    decision = null,
)

private val testCaseProcessChildDocument = null

private val testDocumentMetadataChildDocument = testVasuDetails.toDocumentMetadata()

private val testDocumentChildDocument = Document(
    DocumentKey.ChildDocument(testVasuDetails.id).value,
    "vasu tekstitiedostona".toByteArray(Charsets.UTF_8),
    "text/plain",
)

private val testEvakaUser = EvakaUser(AuthenticatedUser.SystemInternalUser.evakaUserId, "eVaka", EvakaUserType.SYSTEM)
