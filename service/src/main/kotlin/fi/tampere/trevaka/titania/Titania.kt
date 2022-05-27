// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.titania

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import java.time.LocalTime

// from updateWorkingTimeEvents.wsdl, version 1.2 25.8.2020

data class TitaniaCode(
    val code: String,
    val name: String? = null,
)

data class TitaniaCodeName(
    val code: String,
    val name: String,
)

data class TitaniaPeriod(
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    val beginDate: LocalDate,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    val endDate: LocalDate,
)

enum class TitaniaPayrollItemType {
    /** Edelliseltä jaksolta siirtyvää */
    PREVIOUS,

    /** Kuluva jakso */
    THIS,

    /** Seuraavalle siirtyvää */
    NEXT,

    /** Aikanakorvattavat (aikahyvitys) */
    TIME,

    /** Rahana korvattavat */
    MONEY,
}

enum class TitaniaPayrollItemUnit {
    /** Minuutit */
    MINUTE,

    /** Kappaleet */
    QUANTITY,
}

data class UpdateWorkingTimeEventsRequest(
    val organisation: TitaniaCode? = null,
    val period: TitaniaPeriod,
    val schedulingUnit: List<TitaniaSchedulingUnit>,
)

data class TitaniaSchedulingUnit(
    val code: String,
    val name: String? = null,
    val occupation: List<TitaniaOccupation>,
)

data class TitaniaOccupation(
    val code: String,
    val name: String,
    val person: List<TitaniaPerson>,
)

// also includes ssn, but we cannot use it so just drop it
data class TitaniaPerson(
    val employeeId: String, // optional in the schema, but required for us
    val name: String,
    val actualWorkingTimeEvents: TitaniaWorkingTimeEvents,
    val payrollItems: TitaniaPayrollItems? = null,
)

data class TitaniaWorkingTimeEvents(
    val event: List<TitaniaWorkingTimeEvent>,
)

data class TitaniaWorkingTimeEvent(
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    val date: LocalDate,
    val code: String? = null,
    val description: String? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HHmm")
    val beginTime: LocalTime? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HHmm")
    val endTime: LocalTime? = null,
    val administrativeUnit: TitaniaCodeName? = null,
    val placement: TitaniaCodeName? = null,
    val operativeUnit: TitaniaCodeName? = null,
    val project: TitaniaCodeName? = null,
    val eventType: TitaniaCodeName? = null,
    val eventKind: TitaniaCodeName? = null,
)

data class TitaniaPayrollItems(
    val item: List<TitaniaPayrollItem>,
)

data class TitaniaPayrollItem(
    val code: String,
    val type: TitaniaPayrollItemType,
    val name: String? = null,
    val value: String,
    val unit: TitaniaPayrollItemUnit,
)

data class UpdateWorkingTimeEventsResponse(
    val message: String
) {
    companion object {
        fun ok() = UpdateWorkingTimeEventsResponse("OK")
    }
}
