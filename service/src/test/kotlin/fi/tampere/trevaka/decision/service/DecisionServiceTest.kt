// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.decision.service

import fi.espoo.evaka.application.ServiceNeed
import fi.espoo.evaka.application.ServiceNeedOption
import fi.espoo.evaka.daycare.domain.ProviderType
import fi.espoo.evaka.daycare.service.DaycareManager
import fi.espoo.evaka.decision.Decision
import fi.espoo.evaka.decision.DecisionStatus
import fi.espoo.evaka.decision.DecisionType
import fi.espoo.evaka.decision.DecisionUnit
import fi.espoo.evaka.decision.createDecisionPdf
import fi.espoo.evaka.identity.ExternalIdentifier
import fi.espoo.evaka.pdfgen.PdfGenerator
import fi.espoo.evaka.pis.service.PersonDTO
import fi.espoo.evaka.setting.SettingType
import fi.espoo.evaka.shared.ApplicationId
import fi.espoo.evaka.shared.ChildId
import fi.espoo.evaka.shared.DaycareId
import fi.espoo.evaka.shared.DecisionId
import fi.espoo.evaka.shared.PersonId
import fi.espoo.evaka.shared.ServiceNeedOptionId
import fi.espoo.evaka.shared.domain.OfficialLanguage
import fi.espoo.evaka.shared.template.ITemplateProvider
import fi.tampere.trevaka.AbstractTampereIntegrationTest
import org.junit.jupiter.api.Test
import org.junitpioneer.jupiter.cartesian.CartesianTest
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.util.UUID

private val settings = mapOf(
    SettingType.DECISION_MAKER_NAME to "Paula Palvelupäällikkö",
    SettingType.DECISION_MAKER_TITLE to "Asiakaspalvelupäällikkö",
)

class DecisionServiceTest : AbstractTampereIntegrationTest() {

    @Autowired
    private lateinit var templateProvider: ITemplateProvider

    @Autowired
    private lateinit var pdfGenerator: PdfGenerator

    @CartesianTest
    fun createDecisionPdf(
        @CartesianTest.Enum(
            value = DecisionType::class,
            names = ["PREPARATORY_EDUCATION"],
            mode = CartesianTest.Enum.Mode.EXCLUDE,
        ) decisionType: DecisionType,
        @CartesianTest.Enum(
            value = ProviderType::class,
            names = ["MUNICIPAL", "PRIVATE_SERVICE_VOUCHER"],
            mode = CartesianTest.Enum.Mode.INCLUDE,
        ) providerType: ProviderType,
        @CartesianTest.Values(booleans = [false, true]) isTransferApplication: Boolean,
    ) {
        val bytes = createDecisionPdf(
            templateProvider,
            pdfGenerator,
            settings,
            validDecision(decisionType, validDecisionUnit(providerType)),
            child = validChild(),
            isTransferApplication = isTransferApplication,
            serviceNeed = when (decisionType) {
                DecisionType.CLUB -> null
                else -> ServiceNeed(
                    startTime = "08:00",
                    endTime = "16:00",
                    shiftCare = false,
                    partTime = false,
                    ServiceNeedOption(
                        ServiceNeedOptionId(UUID.randomUUID()),
                        "Palveluntarve 1",
                        "Palveluntarve 1",
                        "Palveluntarve 1",
                        null,
                    ),
                )
            },
            lang = OfficialLanguage.FI,
            DaycareManager("Päivi Päiväkodinjohtaja", "paivi.paivakodinjohtaja@example.com", "0451231234"),
        )

        val filename = "DecisionServiceTest-$decisionType-$providerType${if (isTransferApplication) "-transfer" else ""}.pdf"
        writeReportsFile(filename, bytes)
    }

    @Test
    fun createDecisionPdfWithoutServiceNeed() {
        val bytes = createDecisionPdf(
            templateProvider,
            pdfGenerator,
            settings,
            validDecision(DecisionType.DAYCARE, validDecisionUnit(ProviderType.MUNICIPAL)),
            child = validChild(),
            isTransferApplication = false,
            ServiceNeed(
                startTime = "08:00",
                endTime = "16:00",
                shiftCare = false,
                partTime = false,
                serviceNeedOption = null,
            ),
            lang = OfficialLanguage.FI,
            DaycareManager("Päivi Päiväkodinjohtaja", "paivi.paivakodinjohtaja@example.com", "0451231234"),
        )

        val filename = "DecisionServiceTest-DAYCARE-without-service-need-option.pdf"
        writeReportsFile(filename, bytes)
    }

    @Test
    fun createDecisionPdfWithoutSettings() {
        val bytes = createDecisionPdf(
            templateProvider,
            pdfGenerator,
            mapOf(),
            validDecision(DecisionType.DAYCARE, validDecisionUnit(ProviderType.MUNICIPAL)),
            child = validChild(),
            isTransferApplication = false,
            serviceNeed = ServiceNeed(
                startTime = "08:00",
                endTime = "16:00",
                shiftCare = false,
                partTime = false,
                ServiceNeedOption(
                    ServiceNeedOptionId(UUID.randomUUID()),
                    "Palveluntarve 1",
                    "Palveluntarve 1",
                    "Palveluntarve 1",
                    null,
                ),
            ),
            lang = OfficialLanguage.FI,
            DaycareManager("Päivi Päiväkodinjohtaja", "paivi.paivakodinjohtaja@example.com", "0451231234"),
        )

        val filename = "DecisionServiceTest-DAYCARE-without-settings.pdf"
        writeReportsFile(filename, bytes)
    }

    @Test
    fun createRestrictedDetailsEnabledPdf() {
        val bytes = createDecisionPdf(
            templateProvider,
            pdfGenerator,
            settings,
            validDecision(DecisionType.DAYCARE, validDecisionUnit(ProviderType.MUNICIPAL)),
            child = validChild(true),
            isTransferApplication = false,
            serviceNeed = ServiceNeed(
                startTime = "08:00",
                endTime = "16:00",
                shiftCare = false,
                partTime = false,
                ServiceNeedOption(
                    ServiceNeedOptionId(UUID.randomUUID()),
                    "Palveluntarve 1",
                    "Palveluntarve 1",
                    "Palveluntarve 1",
                    null,
                ),
            ),
            lang = OfficialLanguage.FI,
            DaycareManager("Päivi Päiväkodinjohtaja", "paivi.paivakodinjohtaja@example.com", "0451231234"),
        )

        val filename = "DecisionServiceTest-DAYCARE-restricted-details-enabled.pdf"
        writeReportsFile(filename, bytes)
    }
}

fun validDecision(type: DecisionType, decisionUnit: DecisionUnit) = Decision(
    DecisionId(UUID.randomUUID()),
    createdBy = "Päivi Päiväkodinjohtaja",
    type,
    startDate = LocalDate.now(),
    endDate = LocalDate.now().plusMonths(3),
    decisionUnit,
    applicationId = ApplicationId(UUID.randomUUID()),
    childId = ChildId(UUID.randomUUID()),
    childName = "Matti",
    documentKey = null,
    decisionNumber = 1,
    sentDate = LocalDate.now(),
    DecisionStatus.ACCEPTED,
    requestedStartDate = null,
    resolved = null,
    resolvedByName = null,
    documentContainsContactInfo = false,
)

fun validDecisionUnit(providerType: ProviderType) = DecisionUnit(
    DaycareId(UUID.randomUUID()),
    name = "Vuoreksen kerho",
    daycareDecisionName = "Vuoreksen kerho",
    preschoolDecisionName = "Vuoreksen kerho",
    manager = null,
    streetAddress = "Rautiolanrinne 2",
    postalCode = "33870",
    postOffice = "Tampere",
    phone = "+35850 1234564",
    decisionHandler = "Vuoreksen kerho",
    decisionHandlerAddress = "Rautiolanrinne 2, 33870 Tampere",
    providerType,
)

fun validChild(restrictedDetailsEnabled: Boolean = false) = PersonDTO(
    PersonId(UUID.randomUUID()),
    null,
    ExternalIdentifier.SSN.getInstance("010115A9532"),
    ssnAddingDisabled = false,
    firstName = "Matti",
    lastName = "Meikäläinen",
    preferredName = "Matti",
    email = null,
    phone = "",
    backupPhone = "",
    language = null,
    dateOfBirth = LocalDate.of(2015, 1, 1),
    streetAddress = "Kokinpellonraitti 3",
    postalCode = "33870",
    postOffice = "Tampere",
    residenceCode = "",
    restrictedDetailsEnabled = restrictedDetailsEnabled,
)
