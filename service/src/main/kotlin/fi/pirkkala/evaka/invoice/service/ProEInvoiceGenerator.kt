// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.pirkkala.evaka.invoice.service

import fi.espoo.evaka.invoicing.domain.InvoiceDetailed
import fi.espoo.evaka.invoicing.domain.InvoiceRowDetailed
import fi.espoo.evaka.invoicing.domain.PersonDetailed
import fi.espoo.evaka.invoicing.integration.InvoiceIntegrationClient
import fi.pirkkala.evaka.invoice.config.Product
import fi.pirkkala.evaka.util.FieldType
import fi.pirkkala.evaka.util.FinanceDateProvider
import org.springframework.stereotype.Component
import java.lang.Math.abs
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val restrictedStreetAddress = "Varhaiskasvatus, PL 1001"
private val restrictedPostCode = "33961"
private val restrictedPostOffice = "Pirkkala"

@Component
class ProEInvoiceGenerator(private val invoiceChecker: InvoiceChecker, val financeDateProvider: FinanceDateProvider) : StringInvoiceGenerator {

    fun generateInvoiceTitle(): String {
        val previousMonth = financeDateProvider.previousMonth()
        return "Varhaiskasvatus " + previousMonth
    }

    fun gatherInvoiceData(invoiceDetailed: InvoiceDetailed): InvoiceData {
        val invoiceData = InvoiceData()

        val invoiceDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

        invoiceData.setAlphanumericValue(InvoiceFieldName.NOT_USED, "")
        invoiceData.setAlphanumericValue(InvoiceFieldName.GAP_IN_SPEC, "")

        // we have previously made sure the head of family has an SSN but the compiler doesn't realize it
        invoiceData.setAlphanumericValue(InvoiceFieldName.INVOICE_IDENTIFIER, invoiceDetailed.headOfFamily.ssn ?: "")
        invoiceData.setAlphanumericValue(InvoiceFieldName.HEADER_ROW_CODE, "L")
        val clientName = invoiceDetailed.headOfFamily.lastName + " " + invoiceDetailed.headOfFamily.firstName
        invoiceData.setAlphanumericValue(
            InvoiceFieldName.CLIENT_NAME1,
            clientName.substring(0, Math.min(50, clientName.length)),
        )
        invoiceData.setAlphanumericValue(
            InvoiceFieldName.CLIENT_NAME2,
            if (clientName.length > 50) clientName.substring(50, Math.min(100, clientName.length)) else "",
        )
        invoiceData.setAlphanumericValue(InvoiceFieldName.STREET_ADDRESS, invoiceDetailed.headOfFamily.streetAddress)
        invoiceData.setAlphanumericValue(
            InvoiceFieldName.POSTAL_ADDRESS,
            invoiceDetailed.headOfFamily.postalCode + " " + invoiceDetailed.headOfFamily.postOffice,
        )
        invoiceData.setAlphanumericValue(InvoiceFieldName.CLIENT_CONTACT, "")
        // 1 = Finnish
        invoiceData.setAlphanumericValue(InvoiceFieldName.LANGUAGE_CODE, "1")
        invoiceData.setAlphanumericValue(InvoiceFieldName.REMINDER_CODE, "")
        // K = print a normal invoice
        invoiceData.setAlphanumericValue(InvoiceFieldName.PRINTING_METHOD, "K")
        invoiceData.setAlphanumericValue(
            InvoiceFieldName.INVOICE_DATE,
            invoiceDetailed.invoiceDate.format(invoiceDateFormatter),
        )
        invoiceData.setAlphanumericValue(
            InvoiceFieldName.DUE_DATE,
            invoiceDetailed.dueDate.format(invoiceDateFormatter),
        )
        invoiceData.setAlphanumericValue(
            InvoiceFieldName.ACCOUNTING_DATE,
            invoiceDetailed.sentAt?.toLocalDateTime()?.format(invoiceDateFormatter) ?: LocalDate.now()
                .format(invoiceDateFormatter),
        )
        invoiceData.setAlphanumericValue(InvoiceFieldName.PRINTING_DATE, "")
        invoiceData.setAlphanumericValue(InvoiceFieldName.CREDIT_NOTE_INVOICE_NUMBER, "")
        invoiceData.setAlphanumericValue(
            InvoiceFieldName.INVOICE_NUMBER,
            if (invoiceDetailed.number != null) invoiceDetailed.number.toString() else "",
        )
        invoiceData.setAlphanumericValue(InvoiceFieldName.REFERENCE_NUMBER, "")
        invoiceData.setAlphanumericValue(InvoiceFieldName.PARTNER_CODE, "1000")
        invoiceData.setAlphanumericValue(InvoiceFieldName.CURRENCY, "")
        invoiceData.setAlphanumericValue(InvoiceFieldName.INVOICE_TYPE, "")
        // what should we put here?
        invoiceData.setAlphanumericValue(InvoiceFieldName.DESCRIPTION, generateInvoiceTitle())
        invoiceData.setAlphanumericValue(InvoiceFieldName.SECURITY_DENIAL, "")
        invoiceData.setAlphanumericValue(InvoiceFieldName.CONTRACT_NUMBER, "")
        invoiceData.setAlphanumericValue(InvoiceFieldName.COUNTRY, "")
        invoiceData.setAlphanumericValue(InvoiceFieldName.SSN, "")
        invoiceData.setAlphanumericValue(InvoiceFieldName.RF_REFERENCE, "")

        invoiceData.setAlphanumericValue(InvoiceFieldName.CODEBTOR_ROW_CODE, "Y")

        val codebtor = invoiceDetailed.codebtor
        if (codebtor != null) {
            invoiceData.setAlphanumericValue(InvoiceFieldName.CODEBTOR_IDENTIFIER, codebtor.ssn ?: "")
            invoiceData.setAlphanumericValue(
                InvoiceFieldName.CODEBTOR_NAME,
                codebtor.lastName + " " + codebtor.firstName,
            )
            invoiceData.setAlphanumericValue(InvoiceFieldName.CODEBTOR_STREET_ADDRESS, codebtor.streetAddress)
            invoiceData.setAlphanumericValue(
                InvoiceFieldName.CODEBTOR_POSTAL_ADDRESS,
                codebtor.postalCode + " " + codebtor.postOffice,
            )
        } else {
            invoiceData.setAlphanumericValue(InvoiceFieldName.CODEBTOR_IDENTIFIER, "")
            invoiceData.setAlphanumericValue(InvoiceFieldName.CODEBTOR_NAME, "")
            invoiceData.setAlphanumericValue(InvoiceFieldName.CODEBTOR_STREET_ADDRESS, "")
            invoiceData.setAlphanumericValue(InvoiceFieldName.CODEBTOR_POSTAL_ADDRESS, "")
        }
        invoiceData.setAlphanumericValue(InvoiceFieldName.CODEBTOR_LANGUAGE_CODE, "1")
        invoiceData.setAlphanumericValue(InvoiceFieldName.CODEBTOR_PARTNER_CODE, "")
        invoiceData.setAlphanumericValue(InvoiceFieldName.CODEBTOR_COUNTRY, "")

        val sortedRows = invoiceDetailed.rows.sortedBy { row -> row.child.firstName }

        val rowsPerChild: MutableMap<String, List<InvoiceData>> = mutableMapOf()
        var currentChild = ""
        var childRows: MutableList<InvoiceData> = mutableListOf()

        sortedRows.forEach {
            if (it.child.firstName != currentChild) {
                currentChild = it.child.firstName
                childRows = mutableListOf()
                rowsPerChild.put(currentChild, childRows)
            }

            val invoiceRowData = InvoiceData()

            // we have previously made sure the head of family has an SSN but the compiler doesn't realize it
            invoiceRowData.setAlphanumericValue(
                InvoiceFieldName.INVOICE_IDENTIFIER,
                invoiceDetailed.headOfFamily.ssn ?: "",
            )
            invoiceRowData.setAlphanumericValue(InvoiceFieldName.TEXT_ROW_CODE, "3")
            invoiceRowData.setAlphanumericValue(
                InvoiceFieldName.CHILD_NAME,
                it.child.lastName + " " + it.child.firstName,
            )
            val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            invoiceRowData.setAlphanumericValue(
                InvoiceFieldName.TIME_PERIOD,
                it.periodStart.format(dateFormatter) + " - " + it.periodEnd.format(dateFormatter),
            )

            invoiceRowData.setAlphanumericValue(InvoiceFieldName.DETAIL_ROW_CODE, "1")
            invoiceRowData.setAlphanumericValue(InvoiceFieldName.PRODUCT_NAME, Product.valueOf(it.product.value).nameFi)
            invoiceRowData.setAlphanumericValue(InvoiceFieldName.PRICE_SIGN, "")
            invoiceRowData.setNumericValue(InvoiceFieldName.UNIT_PRICE, abs(it.unitPrice))
            invoiceRowData.setAlphanumericValue(InvoiceFieldName.UNIT, "KPL")
            // empty value is interpreted as a plus sign
            invoiceRowData.setAlphanumericValue(InvoiceFieldName.AMOUNT_SIGN, if (it.unitPrice < 0) "-" else "")
            invoiceRowData.setNumericValue(InvoiceFieldName.AMOUNT, it.amount)
            invoiceRowData.setAlphanumericValue(InvoiceFieldName.VAT_CODE, "00")
            invoiceRowData.setAlphanumericValue(InvoiceFieldName.VAT_ACCOUNT, "")
            invoiceRowData.setAlphanumericValue(InvoiceFieldName.BRUTTO_NETTO, "")
            invoiceRowData.setAlphanumericValue(InvoiceFieldName.DEBIT_ACCOUNTING, "")
            invoiceRowData.setAlphanumericValue(InvoiceFieldName.CREDIT_ACCOUNTING, getCreditAccounting(it))

            invoiceRowData.setAlphanumericValue(InvoiceFieldName.DESCRIPTION, it.description)

            childRows.add(invoiceRowData)
        }

        invoiceData.setChildRowMap(rowsPerChild)

        return invoiceData
    }

    private fun getCreditAccounting(it: InvoiceRowDetailed): String {
        val tili = "3257"
        val alv = "300"
        val kumppani = "1000"
        val kustannuspaikka = it.costCenter
        return "$tili$alv$kumppani$kustannuspaikka"
    }

    fun generateRow(fields: List<InvoiceField>, invoiceData: InvoiceData): String {
        var result = ""

        fields.forEach {
            when (it.fieldType) {
                FieldType.ALPHANUMERIC -> {
                    val value = invoiceData.getAlphanumericValue(it.field) ?: ""
                    result += value.take(it.length).padEnd(it.length)
                }
                FieldType.NUMERIC -> {
                    val value = invoiceData.getNumericValue(it.field) ?: 0
                    val stringValue = value.toString().padStart(it.length, '0')
                    // all Evaka values seem to be Int so we can just pad
                    // the decimal part with the correct number of zeroes
                    result += stringValue.padEnd(it.length + it.decimals, '0')
                }
                FieldType.MONETARY -> {
                    val value = invoiceData.getNumericValue(it.field) ?: 0
                    // if the value is non-zero it has been multiplied by 100 to already contain two decimals
                    val decimals = if (value == 0) it.decimals else it.decimals - 2
                    val length = if (value == 0) it.length else it.length + 2
                    val stringValue = value.toString().padStart(length, '0')
                    result += stringValue.padEnd(length + decimals, '0')
                }
            }
        }

        result = result + "\n"

        return result
    }

    fun formatInvoice(invoiceData: InvoiceData): String {
        var result = generateRow(headerRowFields, invoiceData)

        if (invoiceData.getAlphanumericValue(InvoiceFieldName.CODEBTOR_IDENTIFIER) != "") {
            result += generateRow(codebtorRowFields, invoiceData)
        }

        val rowsPerChild = invoiceData.getChildRowMap()
        rowsPerChild.forEach { row ->
            result += generateRow(childHeaderRowFields, row.value[0])
            row.value.forEach { data ->
                result += generateRow(rowHeaderRowFields, data)
                result += generateRow(detailRowFields, data)

                if (data.getAlphanumericValue(InvoiceFieldName.DESCRIPTION) != "") {
                    result += generateRow(descriptionRowFields, data)
                }
            }
        }

        return result
    }

    override fun generateInvoice(invoices: List<InvoiceDetailed>): StringInvoiceGenerator.InvoiceGeneratorResult {
        var invoiceString = ""
        val successList = mutableListOf<InvoiceDetailed>()
        val failedList = mutableListOf<InvoiceDetailed>()
        val manuallySentList = mutableListOf<InvoiceDetailed>()

        val (manuallySent, succeeded) = invoices.partition { invoice -> invoiceChecker.shouldSendManually(invoice) }
        manuallySentList.addAll(manuallySent)

        succeeded.forEach { invoice ->
            val invoiceWithCorrectedData = invoice.copy(
                headOfFamily = handlePerson(invoice.headOfFamily),
                codebtor = invoice.codebtor?.let { handlePerson(it) },
            )
            val invoiceData = gatherInvoiceData(invoiceWithCorrectedData)
            invoiceString += formatInvoice(invoiceData)
            successList.add(invoiceWithCorrectedData)
        }

        return StringInvoiceGenerator.InvoiceGeneratorResult(
            InvoiceIntegrationClient.SendResult(
                successList,
                failedList,
                manuallySentList,
            ),
            invoiceString,
        )
    }
}

internal fun handlePerson(person: PersonDetailed): PersonDetailed {
    val (lastName, firstName) =
        if (person.invoiceRecipientName.isNotBlank()) {
            person.invoiceRecipientName.trim() to ""
        } else {
            person.lastName.trim() to person.firstName.trim()
        }
    val (streetAddress, postalCode, postOffice) = when (person.restrictedDetailsEnabled) {
        true -> Triple(
            restrictedStreetAddress,
            restrictedPostCode,
            restrictedPostOffice,
        )
        false -> if (hasInvoicingAddress(person)) {
            Triple(
                person.invoicingStreetAddress.trim(),
                person.invoicingPostalCode.trim(),
                person.invoicingPostOffice.trim(),
            )
        } else {
            Triple(
                person.streetAddress.trim(),
                person.postalCode.trim(),
                person.postOffice.trim(),
            )
        }
    }
    return person.copy(
        lastName = lastName,
        firstName = firstName,
        streetAddress = streetAddress,
        postalCode = postalCode,
        postOffice = postOffice,
    )
}

internal fun hasInvoicingAddress(person: PersonDetailed): Boolean = person.invoicingStreetAddress.isNotBlank() &&
    person.invoicingPostalCode.isNotBlank() &&
    person.invoicingPostOffice.isNotBlank()
