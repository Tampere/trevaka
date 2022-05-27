// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.titania

import fi.espoo.evaka.shared.auth.AuthenticatedUser
import fi.espoo.evaka.shared.auth.AuthenticatedUserType
import fi.espoo.evaka.shared.domain.Forbidden
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/integration/titania")
class TitaniaController(private val titaniaService: TitaniaService) {

    @PutMapping("/working-time-events")
    fun updateWorkingTimeEvents(
        @RequestBody request: UpdateWorkingTimeEventsRequest,
        user: AuthenticatedUser
    ): UpdateWorkingTimeEventsResponse {
        if (user.type != AuthenticatedUserType.integration) throw Forbidden()
        return titaniaService.updateWorkingTimeEvents(request)
    }

}
