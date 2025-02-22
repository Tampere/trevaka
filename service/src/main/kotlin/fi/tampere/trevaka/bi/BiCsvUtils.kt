// SPDX-FileCopyrightText: 2017-2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.bi

import fi.espoo.evaka.shared.domain.DateRange
import fi.espoo.evaka.shared.domain.TimeRange
import org.unbescape.csv.CsvEscape
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

val CSV_CHARSET = Charsets.UTF_8
const val CSV_FIELD_SEPARATOR = ","
const val CSV_RECORD_SEPARATOR = "\r\n"

fun convertToCsv(value: Any?): String = when (value) {
    null -> ""
    is Number -> value.toString()
    is String -> value
    is Boolean -> if (value) "t" else "f"
    is UUID -> value.toString()
    is LocalDate -> value.format(DateTimeFormatter.ISO_LOCAL_DATE)
    is List<*> -> "{${value.joinToString(",") { if (it == null) "NULL" else convertToCsv(it) }}}"
    is Enum<*> -> value.name
    is TimeRange -> "\"(${convertToCsv(value.start.inner)},${convertToCsv(value.end.inner)})\""
    is DateRange -> "[${convertToCsv(value.start)},${convertToCsv(value.end?.plusDays(1))})"
    is LocalTime -> value.format(DateTimeFormatter.ISO_LOCAL_TIME)
    else -> error("Unsupported CSV field type ${value.javaClass}")
}

fun <T : Any> toCsvRecords(
    converter: (value: Any?) -> String,
    clazz: KClass<T>,
    values: Sequence<T>,
): Sequence<String> {
    check(clazz.isData)
    val props = clazz.declaredMemberProperties.toList()
    val header = props.joinToString(CSV_FIELD_SEPARATOR, postfix = CSV_RECORD_SEPARATOR) { it.name }
    return sequenceOf(header) +
        values.map { record ->
            props.joinToString(CSV_FIELD_SEPARATOR, postfix = CSV_RECORD_SEPARATOR) {
                CsvEscape.escapeCsv(converter(it.get(record)))
            }
        }
}
