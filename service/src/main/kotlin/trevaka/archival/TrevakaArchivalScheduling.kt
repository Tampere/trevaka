// SPDX-FileCopyrightText: 2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka.archival

import fi.espoo.evaka.shared.ChildDocumentId
import fi.espoo.evaka.shared.DecisionId
import fi.espoo.evaka.shared.FeeDecisionId
import fi.espoo.evaka.shared.VoucherValueDecisionId
import fi.espoo.evaka.shared.async.AsyncJob
import fi.espoo.evaka.shared.async.AsyncJobRunner
import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.domain.EvakaClock
import fi.tampere.trevaka.ArchivalSchedule
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

fun planDocumentArchival(
    db: Database.Connection,
    clock: EvakaClock,
    asyncJobRunner: AsyncJobRunner<AsyncJob>,
    schedule: ArchivalSchedule,
) {
    if (schedule.dailyDocumentLimit < 1) error("Invalid archival limit configuration of ${schedule.dailyDocumentLimit}")

    db.transaction { tx ->
        val archivalPicks = pickIds(tx, clock, schedule)
        logger.info {
            "Scheduling archival for ${archivalPicks.planChildDocuments.size} plan child documents, ${archivalPicks.decisionChildDocuments.size} decision child documents, ${archivalPicks.decisions.size} decisions, ${archivalPicks.feeDecisions.size} fee decisions and ${archivalPicks.voucherValueDecisions.size} voucher value decisions, limit: ${schedule.dailyDocumentLimit})"
        }
        asyncJobRunner.plan(
            tx,
            archivalPicks.planChildDocuments.map { documentId ->
                AsyncJob.ArchiveChildDocument(user = null, documentId = documentId)
            } +
                archivalPicks.decisionChildDocuments.map { documentId ->
                    AsyncJob.ArchiveChildDocument(user = null, documentId = documentId)
                } +
                archivalPicks.decisions.map { documentId ->
                    AsyncJob.ArchiveDecision(user = null, decisionId = documentId)
                } +
                archivalPicks.feeDecisions.map { documentId ->
                    AsyncJob.ArchiveFeeDecision(user = null, feeDecisionId = documentId)
                } +
                archivalPicks.voucherValueDecisions.map { documentId ->
                    AsyncJob.ArchiveVoucherValueDecision(user = null, voucherValueDecisionId = documentId)
                },
            retryCount = 1, // Run once, no retries on failure
            runAt = clock.now(),
        )

        logger.info {
            "Successfully scheduled ${archivalPicks.totalCount()} archival jobs"
        }
    }
}

private fun pickIds(tx: Database.Transaction, clock: EvakaClock, schedule: ArchivalSchedule): ArchivalPicks {
    // select extra to pad out rough proportionality in extreme distributions
    val documentTypeLimit = ((schedule.dailyDocumentLimit / 5) * 1.5).toInt().coerceAtLeast(1)

    logger.info {
        "Planning document archival jobs, max limit: ${schedule.dailyDocumentLimit}"
    }

    val dau = clock.today().minusDays(schedule.documentPlanDelayDays)
    val planChildDocumentIds = tx.getChildPlanDocumentsEligibleForArchival(dau, documentTypeLimit)
    val decisionChildDocumentIds = tx.getChildDecisionDocumentsEligibleForArchival(clock.today().minusDays(schedule.documentDecisionDelayDays), documentTypeLimit)
    val decisionIds = tx.getDecisionsEligibleForArchival(clock.today().minusDays(schedule.decisionDelayDays), documentTypeLimit)
    val feeDecisionIds = tx.getFeeDecisionsEligibleForArchival(clock.today().minusDays(schedule.feeDecisionDelayDays), documentTypeLimit)
    val voucherValueDecisionIds = tx.getVoucherValueDecisionsEligibleForArchival(clock.today().minusDays(schedule.voucherDecisionDelayDays), documentTypeLimit)

    val archivalTargets = listOf(
        planChildDocumentIds,
        decisionChildDocumentIds,
        decisionIds,
        feeDecisionIds,
        voucherValueDecisionIds,
    )
    val sizes = archivalTargets.map { it.size }
    val totalAvailable = sizes.sum()
    val actualTargetCount = minOf(totalAvailable, schedule.dailyDocumentLimit.toInt())

    // rough proportional archival allocation guaranteed to be <= daily limit
    val proportional = sizes.map { size ->
        if (totalAvailable == 0) {
            0
        } else {
            ((size.toDouble() / totalAvailable) * actualTargetCount).toInt()
        }
    }

    return ArchivalPicks(
        planChildDocumentIds.take(proportional[0]),
        decisionChildDocumentIds.take(proportional[1]),
        decisionIds.take(proportional[2]),
        feeDecisionIds.take(proportional[3]),
        voucherValueDecisionIds.take(proportional[4]),
    )
}

data class ArchivalPicks(val planChildDocuments: List<ChildDocumentId>, val decisionChildDocuments: List<ChildDocumentId>, val decisions: List<DecisionId>, val feeDecisions: List<FeeDecisionId>, val voucherValueDecisions: List<VoucherValueDecisionId>) {
    fun totalCount(): Int = this.planChildDocuments.size + this.decisionChildDocuments.size + this.decisions.size + this.feeDecisions.size + this.voucherValueDecisions.size
}
