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
                  AND cd.status = 'COMPLETED'
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
                WHERE NOT EXISTS (select from placement pla where cd.child_id = pla.child_id AND pla.end_date > ${bind(eligibleDate)})
                  AND dt.archive_externally = true
                  AND cd.archived_at IS NULL
                  AND dt.type = 'OTHER_DECISION'
                  AND cd.status = 'COMPLETED'
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
                WHERE d.status in ('ACCEPTED','REJECTED')
                  AND d.type <> 'CLUB' 
                  AND d.archived_at IS NULL
                  And d.resolved <= ${bind(eligibleDate)}
                ORDER BY d.resolved
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
                  AND fd.status in ('ANNULLED','SENT')
                  AND fd.archived_at IS NULL
                ORDER BY fd.approved_at
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
                  AND vvd.status in ('ANNULLED','SENT')
                  AND vvd.archived_at IS NULL
                ORDER BY vvd.approved_at
                LIMIT $limit
                """,
    )
}
    .toList<VoucherValueDecisionId>()
