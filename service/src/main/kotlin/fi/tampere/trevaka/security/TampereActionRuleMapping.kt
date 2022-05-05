// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.security

import fi.espoo.evaka.shared.auth.UserRole
import fi.espoo.evaka.shared.security.Action
import fi.espoo.evaka.shared.security.actionrule.ActionRuleMapping
import fi.espoo.evaka.shared.security.actionrule.HasUnitRole
import fi.espoo.evaka.shared.security.actionrule.ScopedActionRule
import fi.espoo.evaka.shared.security.actionrule.StaticActionRule

class TampereActionRuleMapping : ActionRuleMapping {
    override fun rulesOf(action: Action.StaticAction): Sequence<StaticActionRule> = action.defaultRules.asSequence()
    override fun <T> rulesOf(action: Action.ScopedAction<in T>): Sequence<ScopedActionRule<in T>> = when (action) {
        Action.BackupCare.UPDATE,
        Action.BackupCare.DELETE -> {
            @Suppress("UNCHECKED_CAST")
            action.defaultRules.asSequence() + sequenceOf(
                HasUnitRole(UserRole.STAFF).inPlacementUnitOfChildOfBackupCare() as ScopedActionRule<in T>
            )
        }
        Action.Child.CREATE_BACKUP_CARE,
        Action.Child.READ_ASSISTANCE_ACTION,
        Action.Child.READ_ASSISTANCE_NEED -> {
            @Suppress("UNCHECKED_CAST")
            action.defaultRules.asSequence() + sequenceOf(
                HasUnitRole(UserRole.STAFF).inPlacementUnitOfChild() as ScopedActionRule<in T>
            )
        }
        else -> action.defaultRules.asSequence()
    }
}
