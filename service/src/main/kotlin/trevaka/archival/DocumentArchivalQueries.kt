// SPDX-FileCopyrightText: 2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka.archival

import fi.espoo.evaka.shared.ChildDocumentId
import fi.espoo.evaka.shared.DecisionId
import fi.espoo.evaka.shared.FeeDecisionId
import fi.espoo.evaka.shared.VoucherValueDecisionId
import fi.espoo.evaka.shared.db.Database
import java.time.LocalDate

fun Database.Read.getChildPlanDocumentsEligibleForArchival(
    eligibleDate: LocalDate,
    limit: Int,
): List<ChildDocumentId> = createQuery {
    sql(
        """
                SELECT cd.id
                FROM child_document cd
                JOIN document_template dt ON cd.template_id = dt.id
                -- upper is exclusive
                WHERE upper(dt.validity) - interval '1 day' <= ${bind(eligibleDate)}
                  AND dt.archive_externally = true
                  AND cd.archived_at IS NULL
                  AND dt.type <> 'OTHER_DECISION'
                ORDER BY cd.created
                LIMIT $limit
                """,
    )
}
    .toList<ChildDocumentId>()

fun Database.Read.getChildDecisionDocumentsEligibleForArchival(
    eligibleDate: LocalDate,
    limit: Int,
): List<ChildDocumentId> = createQuery {
    sql(
        """
                SELECT cd.id
                FROM child_document cd
                JOIN document_template dt ON cd.template_id = dt.id
                JOIN placement pl ON pl.child_id = cd.child_id AND cd.published_at BETWEEN pl.start_date AND pl.end_date
                WHERE pl.end_date <= ${bind(eligibleDate)}
                  AND dt.archive_externally = true
                  AND cd.archived_at IS NULL
                  AND dt.type = 'OTHER_DECISION'
                ORDER BY cd.created
                LIMIT $limit
                """,
    )
}
    .toList<ChildDocumentId>()

fun Database.Read.getDecisionsEligibleForArchival(
    eligibleDate: LocalDate,
    limit: Int,
): List<DecisionId> = createQuery {
    sql(
        """
                SELECT d.id
                FROM decision d
                WHERE d.status = 'ACCEPTED'
                  AND d.archived_at IS NULL
                  And d.sent_date <= ${bind(eligibleDate)}
                ORDER BY d.created
                LIMIT $limit
                """,
    )
}
    .toList<DecisionId>()

fun Database.Read.getFeeDecisionsEligibleForArchival(
    eligibleDate: LocalDate,
    limit: Int,
): List<FeeDecisionId> = createQuery {
    sql(
        """
                SELECT fd.id
                FROM fee_decision fd
                WHERE fd.approved_at <= ${bind(eligibleDate)}
                  AND fd.status = 'SENT'
                  AND fd.archived_at IS NULL
                ORDER BY fd.created
                LIMIT $limit
                """,
    )
}
    .toList<FeeDecisionId>()

fun Database.Read.getVoucherValueDecisionsEligibleForArchival(
    eligibleDate: LocalDate,
    limit: Int,
): List<VoucherValueDecisionId> = createQuery {
    sql(
        """
                SELECT vvd.id
                FROM voucher_value_decision vvd                
                WHERE vvd.approved_at <= ${bind(eligibleDate)}
                  AND vvd.status = 'SENT'
                  AND vvd.archived_at IS NULL
                ORDER BY vvd.created
                LIMIT $limit
                """,
    )
}
    .toList<VoucherValueDecisionId>()
