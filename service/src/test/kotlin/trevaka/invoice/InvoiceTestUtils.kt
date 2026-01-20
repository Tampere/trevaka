// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka.invoice

import fi.espoo.evaka.identity.ExternalIdentifier
import fi.espoo.evaka.invoicing.domain.PersonDetailed
import fi.espoo.evaka.pis.service.PersonDTO

fun expectedRowLength(lastPos: Int, lastLength: Int) = lastPos + lastLength - 1

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
