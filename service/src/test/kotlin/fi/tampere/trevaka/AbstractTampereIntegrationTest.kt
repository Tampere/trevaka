// SPDX-FileCopyrightText: 2021-2023 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka

import org.springframework.test.context.ActiveProfiles
import trevaka.AbstractIntegrationTest

@ActiveProfiles(value = ["integration-test", "tampere_evaka"])
abstract class AbstractTampereIntegrationTest : AbstractIntegrationTest()
