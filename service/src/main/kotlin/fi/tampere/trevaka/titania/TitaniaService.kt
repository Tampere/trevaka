// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.titania

import org.springframework.stereotype.Service

@Service
class TitaniaService {

    fun updateWorkingTimeEvents(request: UpdateWorkingTimeEventsRequest): UpdateWorkingTimeEventsResponse {
        println(request)
        return UpdateWorkingTimeEventsResponse.ok()
    }

}
