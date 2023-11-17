// SPDX-FileCopyrightText: 2021-2023 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("tampere_evaka")
@Configuration
@ComponentScan("fi.tampere.trevaka")
@ConfigurationPropertiesScan(basePackages = ["fi.tampere.trevaka"])
class TampereComponentScan
