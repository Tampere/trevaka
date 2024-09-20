// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.reports

import fi.espoo.evaka.reports.freezeVoucherValueReportRows
import fi.espoo.evaka.shared.auth.AuthenticatedUser
import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.domain.EvakaClock
import fi.espoo.evaka.shared.domain.Forbidden
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

private val logger = KotlinLogging.logger { }

private const val TAKEN_AT_DAY_OF_MONTH = 25

@RestController
@RequestMapping("/reports")
class ReportsController() {

    @GetMapping("/freeze-voucher-value-report-rows")
    fun freezeVoucherValueReportRows(
        db: Database,
        user: AuthenticatedUser,
        clock: EvakaClock,
    ) {
        if (!user.isAdmin) throw Forbidden()
        logger.info { "Freeze all voucher value report rows" }
        db.connect { dbc ->
            dbc.transaction { tx ->
                val firstDate = tx.createQuery { sql("SELECT min(valid_from) FROM voucher_value_decision") }
                    .mapTo<LocalDate>()
                    .exactlyOneOrNull() ?: return@transaction
                val firstYearMonth = YearMonth.of(firstDate.year, firstDate.month)
                val now = clock.today()
                val lastDate = now.minusMonths(if (now.dayOfMonth >= TAKEN_AT_DAY_OF_MONTH) 0 else 1)
                val lastYearMonth = YearMonth.of(lastDate.year, lastDate.month)
                var yearMonth = firstYearMonth
                while (yearMonth <= lastYearMonth) {
                    freezeVoucherValueReportRows(tx, yearMonth)
                    yearMonth = yearMonth.plusMonths(1)
                }
            }
        }
    }

    @GetMapping("/freeze-voucher-value-report-rows/{year}/{month}")
    fun freezeVoucherValueReportRows(
        db: Database,
        user: AuthenticatedUser,
        @PathVariable year: Int,
        @PathVariable month: Int,
    ) {
        if (!user.isAdmin) throw Forbidden()
        db.connect { dbc ->
            dbc.transaction { tx ->
                freezeVoucherValueReportRows(tx, YearMonth.of(year, month))
            }
        }
    }

    internal fun freezeVoucherValueReportRows(
        tx: Database.Transaction,
        yearMonth: YearMonth,
    ) {
        logger.info { "Freeze voucher value report rows $yearMonth" }
        val takenAt = HelsinkiDateTime.of(yearMonth.atDay(TAKEN_AT_DAY_OF_MONTH), LocalTime.MIN)
        freezeVoucherValueReportRows(tx, yearMonth.year, yearMonth.monthValue, takenAt)
    }
}
