// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka.time

import fi.espoo.evaka.shared.domain.EvakaClock
import fi.espoo.evaka.shared.domain.RealEvakaClock
import org.springframework.stereotype.Service

@Service
class ClockService {
    fun clock(): EvakaClock = RealEvakaClock()
}
