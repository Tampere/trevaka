package fi.tampere.trevaka

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication(
    scanBasePackages = ["fi.tampere.trevaka", "fi.espoo.evaka"],
    exclude = [
        DataSourceAutoConfiguration::class,
        FlywayAutoConfiguration::class,
        SecurityAutoConfiguration::class,
        SecurityFilterAutoConfiguration::class,
        TransactionAutoConfiguration::class
    ]
)
class TrevakaMain

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {

    val profiles = mutableListOf<String>()

    System.getenv("VOLTTI_ENV")?.let { envString ->
        val envList = envString.split(",").map { it.trim() }
        for (env in envList) {
            when (env) {
                "dev", "test" -> profiles.add("enables_dev_api")
                "trevaka" -> profiles.add("trevaka")
            }
        }
    }

    logger.info("Active profiles: {}", profiles.toTypedArray())
    SpringApplicationBuilder()
        .sources(TrevakaMain::class.java)
        .profiles(*profiles.toTypedArray())
        .run(*args)
}
