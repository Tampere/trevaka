// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka.security

import fi.espoo.evaka.shared.security.Action
import fi.espoo.evaka.shared.security.actionrule.ActionRuleMapping
import fi.espoo.evaka.shared.security.actionrule.ScopedActionRule
import fi.espoo.evaka.shared.security.actionrule.UnscopedActionRule

class TrevakaActionRuleMapping : ActionRuleMapping {
    override fun rulesOf(action: Action.UnscopedAction): Sequence<UnscopedActionRule> = when (action) {
        else -> action.defaultRules.asSequence()
    }

    override fun <T> rulesOf(action: Action.ScopedAction<in T>): Sequence<ScopedActionRule<in T>> = when (action) {
        else -> action.defaultRules.asSequence()
    }
}
