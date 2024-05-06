// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.lempaala.evaka.util

import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Component
class FinanceDateProvider {
    fun currentDate(): String {
        val invoiceIdFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        return LocalDate.now().format(invoiceIdFormatter)
    }

    fun previousMonth(): String {
        val previousMonth = LocalDate.now().minusMonths(1)
        val titleFormatter = DateTimeFormatter.ofPattern("MM.yyyy")
        return previousMonth.format(titleFormatter)
    }

    fun currentDateWithAbbreviatedYear(): String {
        val invoiceIdFormatter = DateTimeFormatter.ofPattern("yyMMdd")
        return LocalDate.now().format(invoiceIdFormatter)
    }

    fun previousMonthLastDate(): String {
        val previousMonthlastDate = YearMonth.now().minusMonths(1).atEndOfMonth()
        val invoiceIdFormatter = DateTimeFormatter.ofPattern("yyMMdd")
        return previousMonthlastDate.format(invoiceIdFormatter)
    }

    fun previousMonthYYMM(): String {
        val previousMonth = LocalDate.now().minusMonths(1)
        val titleFormatter = DateTimeFormatter.ofPattern("yyMM")
        return previousMonth.format(titleFormatter)
    }
}
