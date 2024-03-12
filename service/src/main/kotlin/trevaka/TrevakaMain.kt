// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@SpringBootApplication(
    scanBasePackages = ["trevaka", "fi.espoo.evaka"],
    exclude = [
        DataSourceAutoConfiguration::class,
        FlywayAutoConfiguration::class,
        SecurityAutoConfiguration::class,
        SecurityFilterAutoConfiguration::class,
        TransactionAutoConfiguration::class,
    ],
)
class TrevakaMain

fun main(args: Array<String>) {
    runApplication<TrevakaMain>(*args)
}

@Profile("tampere_evaka")
@Configuration
@ComponentScan("fi.tampere.trevaka")
@ConfigurationPropertiesScan(basePackages = ["fi.tampere.trevaka"])
class TampereComponentScan

@Profile("vesilahti_evaka")
@Configuration
@ComponentScan("fi.vesilahti.evaka")
@ConfigurationPropertiesScan(basePackages = ["fi.vesilahti.evaka"])
class VesilahtiComponentScan

@Profile("hameenkyro_evaka")
@Configuration
@ComponentScan("fi.hameenkyro.evaka")
@ConfigurationPropertiesScan(basePackages = ["fi.hameenkyro.evaka"])
class HameenkyroComponentScan
