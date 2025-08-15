// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.nokiankaupunki.evaka.security

import fi.espoo.evaka.shared.auth.UserRole
import fi.espoo.evaka.shared.security.Action
import fi.espoo.evaka.shared.security.actionrule.ActionRuleMapping
import fi.espoo.evaka.shared.security.actionrule.HasGlobalRole
import fi.espoo.evaka.shared.security.actionrule.HasUnitRole
import fi.espoo.evaka.shared.security.actionrule.ScopedActionRule
import fi.espoo.evaka.shared.security.actionrule.UnscopedActionRule

class NokiaActionRuleMapping(private val commonRules: ActionRuleMapping) : ActionRuleMapping {
    override fun rulesOf(action: Action.UnscopedAction): Sequence<UnscopedActionRule> = when (action) {
        Action.Global.SETTINGS_PAGE,
        Action.Global.UPDATE_SETTINGS,
        ->
            action.defaultRules.asSequence() + sequenceOf(HasGlobalRole(UserRole.SERVICE_WORKER))
        else -> commonRules.rulesOf(action)
    }

    override fun <T> rulesOf(action: Action.ScopedAction<in T>): Sequence<ScopedActionRule<in T>> = when (action) {
        Action.Unit.READ_EXCEEDED_SERVICE_NEEDS_REPORT ->
            @Suppress("UNCHECKED_CAST")
            action.defaultRules.asSequence() + sequenceOf(
                HasGlobalRole(UserRole.SERVICE_WORKER, UserRole.FINANCE_ADMIN) as ScopedActionRule<in T>,
            ) + sequenceOf(
                HasUnitRole(UserRole.UNIT_SUPERVISOR, UserRole.EARLY_CHILDHOOD_EDUCATION_SECRETARY).inUnit() as ScopedActionRule<in T>,
            )
        else -> commonRules.rulesOf(action)
    }
}
