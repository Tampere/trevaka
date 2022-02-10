package fi.tampere.trevaka.security

import fi.espoo.evaka.shared.security.PermittedRoleActions

class TamperePermittedRoleActions(private val defaults: PermittedRoleActions) : PermittedRoleActions by defaults {
}
