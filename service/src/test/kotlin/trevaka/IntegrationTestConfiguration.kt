// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka

import com.auth0.jwt.algorithms.Algorithm
import fi.espoo.evaka.BucketEnv
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import trevaka.s3.createBucketsIfNeeded
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPublicKey

@TestConfiguration
class IntegrationTestConfiguration {

    @Bean
    fun s3Client(bucketEnv: BucketEnv): S3Client {
        val client =
            S3Client.builder()
                .region(Region.EU_WEST_1)
                .serviceConfiguration(
                    S3Configuration.builder().pathStyleAccessEnabled(true).build(),
                )
                .endpointOverride(bucketEnv.s3MockUrl)
                .credentialsProvider(
                    StaticCredentialsProvider.create(AwsBasicCredentials.create("foo", "bar")),
                )
                .build()

        createBucketsIfNeeded(client, bucketEnv.allBuckets())

        return client
    }

    @Bean
    @Profile("tampere_evaka")
    fun testS3AsyncClient(bucketEnv: BucketEnv): S3AsyncClient = S3AsyncClient.crtBuilder()
        .region(Region.EU_WEST_1)
        .forcePathStyle(true)
        .endpointOverride(bucketEnv.s3MockUrl)
        .checksumValidationEnabled(false)
        .credentialsProvider(
            StaticCredentialsProvider.create(AwsBasicCredentials.create("foo", "bar")),
        )
        .build()

    @Bean
    fun s3Presigner(bucketEnv: BucketEnv): S3Presigner =
        S3Presigner.builder()
            .region(Region.EU_WEST_1)
            .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
            .endpointOverride(bucketEnv.s3MockUrl)
            .credentialsProvider(
                StaticCredentialsProvider.create(AwsBasicCredentials.create("foo", "bar")),
            )
            .build()

    @Bean
    fun jwtAlgorithm(): Algorithm {
        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(2048)
        val keyPair = generator.generateKeyPair()
        val jwtPublicKey = keyPair.public as RSAPublicKey
        return Algorithm.RSA256(jwtPublicKey, null)
    }
}
