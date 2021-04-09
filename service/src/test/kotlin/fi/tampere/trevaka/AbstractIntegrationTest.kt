package fi.tampere.trevaka

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT,
    classes = [IntegrationTestConfiguration::class]
)
@AutoConfigureWireMock(port = 0)
abstract class AbstractIntegrationTest {

    companion object {
        private val postgres = PostgresContainer().apply { start() }
        private val redis = RedisContainer("testpw").apply { start() }
        private val s3 = S3Container().apply { start() }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            // see application-integration-test.yml for static properties
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("flyway.username", postgres::getUsername)
            registry.add("flyway.password", postgres::getPassword)
            registry.add("redis.url", redis::url)
            registry.add("redis.port", redis::getFirstMappedPort)
            registry.add("redis.password", redis::password)
            registry.add("redis.ssl", redis::ssl)
            registry.add("fi.espoo.voltti.s3mock.url", s3::getUrl)
        }
    }

}
