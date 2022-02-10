package fi.tampere.trevaka.security

import fi.espoo.evaka.shared.auth.UserRole
import fi.espoo.evaka.shared.security.Action
import fi.espoo.evaka.shared.security.PermittedRoleActions
import java.util.EnumSet

class TamperePermittedRoleActions(private val defaults: PermittedRoleActions) : PermittedRoleActions by defaults {
    override fun childActions(role: UserRole): Set<Action.Child> {
        val added = addedChildActionsByRole[role] ?: emptySet()
        return defaults.childActions(role) + added
    }
}

private val addedChildActionsByRole: Map<UserRole, Set<Action.Child>> = mapOf(
    UserRole.STAFF to EnumSet.of(
        Action.Child.READ_ASSISTANCE_NEED,
        Action.Child.READ_ASSISTANCE_ACTION
    )
)
