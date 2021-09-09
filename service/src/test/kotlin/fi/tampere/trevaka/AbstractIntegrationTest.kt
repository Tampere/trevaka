// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

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
            registry.add("evaka.database.url", postgres::getJdbcUrl)
            registry.add("evaka.database.username", postgres::getUsername)
            registry.add("evaka.database.password", postgres::getPassword)
            registry.add("evaka.database.flyway.username", postgres::getUsername)
            registry.add("evaka.database.flyway.password", postgres::getPassword)
            registry.add("evaka.redis.url", redis::url)
            registry.add("evaka.redis.port", redis::getFirstMappedPort)
            registry.add("evaka.redis.password", redis::password)
            registry.add("evaka.redis.use_ssl", redis::ssl)
            registry.add("evaka.s3mock.url", s3::getUrl)
        }
    }

}
