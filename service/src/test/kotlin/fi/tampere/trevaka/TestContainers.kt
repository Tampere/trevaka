// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait

class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres:13-alpine")

class RedisContainer(val password: String) : GenericContainer<RedisContainer>("redis:6.0.12-alpine") {
    val url = "localhost"
    private val port = 6379
    val ssl = false

    init {
        setWaitStrategy(Wait.defaultWaitStrategy())
        setCommand("redis-server --requirepass $password")
        addExposedPort(port)
    }
}

class S3Container : GenericContainer<S3Container>("adobe/s3mock:2.1.28") {
    private val port = 9090

    init {
        setWaitStrategy(Wait.forHttp("/"))
        addExposedPort(port)
    }

    fun getUrl() = "http://localhost:${getMappedPort(port)}"
}
