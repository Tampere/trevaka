// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.vesilahti.evaka

import org.springframework.test.context.ActiveProfiles
import trevaka.AbstractIntegrationTest

@ActiveProfiles(value = ["integration-test", "vesilahti_evaka"])
abstract class AbstractVesilahtiIntegrationTest : AbstractIntegrationTest()
