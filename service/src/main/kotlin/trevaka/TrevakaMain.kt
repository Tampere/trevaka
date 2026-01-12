// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.boot.transaction.autoconfigure.TransactionAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@SpringBootApplication(
    scanBasePackages = ["trevaka", "fi.espoo.evaka"],
    exclude = [
        DataSourceAutoConfiguration::class,
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
@ConfigurationPropertiesScan(basePackages = ["trevaka", "fi.tampere.trevaka"])
class TampereComponentScan

@Profile("vesilahti_evaka")
@Configuration
@ComponentScan("fi.vesilahti.evaka")
@ConfigurationPropertiesScan(basePackages = ["trevaka", "fi.vesilahti.evaka"])
class VesilahtiComponentScan

@Profile("hameenkyro_evaka")
@Configuration
@ComponentScan("fi.hameenkyro.evaka")
@ConfigurationPropertiesScan(basePackages = ["trevaka", "fi.hameenkyro.evaka"])
class HameenkyroComponentScan

@Profile("ylojarvi_evaka")
@Configuration
@ComponentScan("fi.ylojarvi.evaka")
@ConfigurationPropertiesScan(basePackages = ["trevaka", "fi.ylojarvi.evaka"])
class YlojarviComponentScan

@Profile("pirkkala_evaka")
@Configuration
@ComponentScan("fi.pirkkala.evaka")
@ConfigurationPropertiesScan(basePackages = ["trevaka", "fi.pirkkala.evaka"])
class PirkkalaComponentScan

@Profile("nokia_evaka")
@Configuration
@ComponentScan("fi.nokiankaupunki.evaka")
@ConfigurationPropertiesScan(basePackages = ["trevaka", "fi.nokiankaupunki.evaka"])
class NokiaComponentScan

@Profile("kangasala_evaka")
@Configuration
@ComponentScan("fi.kangasala.evaka")
@ConfigurationPropertiesScan(basePackages = ["trevaka", "fi.kangasala.evaka"])
class KangasalaComponentScan

@Profile("lempaala_evaka")
@Configuration
@ComponentScan("fi.lempaala.evaka")
@ConfigurationPropertiesScan(basePackages = ["trevaka", "fi.lempaala.evaka"])
class LempaalaComponentScan

@Profile("orivesi_evaka")
@Configuration
@ComponentScan("fi.orivesi.evaka")
@ConfigurationPropertiesScan(basePackages = ["trevaka", "fi.orivesi.evaka"])
class OrivesiComponentScan
