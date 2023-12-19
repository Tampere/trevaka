// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka

import com.auth0.jwt.algorithms.Algorithm
import fi.espoo.evaka.BucketEnv
import fi.espoo.evaka.EvakaEnv
import fi.tampere.trevaka.TampereProperties
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.RSAPublicKeySpec

@TestConfiguration
class IntegrationTestConfiguration {

    @Bean
    fun s3Client(
        evakaEnv: EvakaEnv,
        bucketEnv: BucketEnv,
        tampereProperties: TampereProperties?,
    ): S3Client {
        val client = S3Client.builder()
            .region(evakaEnv.awsRegion)
            .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
            .endpointOverride(bucketEnv.s3MockUrl)
            .credentialsProvider(
                StaticCredentialsProvider.create(AwsBasicCredentials.create("foo", "bar")),
            )
            .build()

        createBucketsIfNeeded(client, bucketEnv.allBuckets())
        tampereProperties?.let { createBucketsIfNeeded(client, it.bucket.allBuckets()) }

        return client
    }

    private fun createBucketsIfNeeded(client: S3Client, allBuckets: List<String>) {
        val existingBuckets = client.listBuckets().buckets().map { it.name() }
        allBuckets
            .filterNot { bucket -> existingBuckets.contains(bucket) }
            .forEach { bucket -> client.createBucket { it.bucket(bucket) } }
    }

    @Bean
    fun s3Presigner(evakaEnv: EvakaEnv, bucketEnv: BucketEnv): S3Presigner =
        S3Presigner.builder()
            .region(evakaEnv.awsRegion)
            .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
            .endpointOverride(bucketEnv.s3MockUrl)
            .credentialsProvider(
                StaticCredentialsProvider.create(AwsBasicCredentials.create("foo", "bar")),
            )
            .build()

    @Bean
    fun jwtAlgorithm(): Algorithm {
        val kf = KeyFactory.getInstance("RSA")
        val spec = RSAPublicKeySpec(jwtPrivateKey.modulus, jwtPrivateKey.publicExponent)
        val jwtPublicKey = kf.generatePublic(spec) as RSAPublicKey
        return Algorithm.RSA256(jwtPublicKey, null)
    }
}
