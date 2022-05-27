// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka

import com.auth0.jwt.algorithms.Algorithm
import fi.espoo.evaka.BucketEnv
import fi.espoo.evaka.EvakaEnv
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import redis.clients.jedis.JedisPool
import redis.clients.jedis.Protocol
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.RSAPublicKeySpec

@TestConfiguration
class IntegrationTestConfiguration {

    @Bean
    fun redisPool(): JedisPool {
        // Use database 1 to avoid conflicts with normal development setup in database 0
        val database = 1
        return JedisPool(
            GenericObjectPoolConfig(),
            "localhost",
            6379,
            Protocol.DEFAULT_TIMEOUT,
            null,
            database
        )
    }

    @Bean
    fun s3Client(evakaEnv: EvakaEnv, bucketEnv: BucketEnv): S3Client {
        val client = S3Client.builder()
            .region(evakaEnv.awsRegion)
            .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
            .endpointOverride(bucketEnv.s3MockUrl)
            .credentialsProvider(
                StaticCredentialsProvider.create(AwsBasicCredentials.create("foo", "bar"))
            )
            .build()

        client.createBucket { it.bucket(bucketEnv.decisions) }
        client.createBucket { it.bucket(bucketEnv.feeDecisions) }
        client.createBucket { it.bucket(bucketEnv.voucherValueDecisions) }
        client.createBucket { it.bucket(bucketEnv.attachments) }
        client.createBucket { it.bucket(bucketEnv.data) }

        return client
    }

    @Bean
    fun jwtAlgorithm(): Algorithm {
        val kf = KeyFactory.getInstance("RSA")
        val spec = RSAPublicKeySpec(jwtPrivateKey.modulus, jwtPrivateKey.publicExponent)
        val jwtPublicKey = kf.generatePublic(spec) as RSAPublicKey
        return Algorithm.RSA256(jwtPublicKey, null)
    }

}
