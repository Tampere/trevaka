// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka

import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.RSAKeyProvider
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration
import java.net.URI
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

@TestConfiguration
class IntegrationTestConfiguration {

    @Bean
    fun s3Client(environment: Environment): S3Client {
        val s3MockUrl = environment.getRequiredProperty("fi.espoo.voltti.s3mock.url")
        val region = environment.getRequiredProperty("aws.region")
        val client = S3Client.builder()
            .region(Region.of(region))
            .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
            .endpointOverride(URI.create(s3MockUrl))
            .credentialsProvider(
                StaticCredentialsProvider.create(AwsBasicCredentials.create("foo", "bar"))
            )
            .build()

        client.createBucket { it.bucket(environment.getRequiredProperty("fi.espoo.voltti.document.bucket.daycaredecision")) }
        client.createBucket { it.bucket(environment.getRequiredProperty("fi.espoo.voltti.document.bucket.paymentdecision")) }
        client.createBucket { it.bucket(environment.getRequiredProperty("fi.espoo.voltti.document.bucket.vouchervaluedecision")) }
        client.createBucket { it.bucket(environment.getRequiredProperty("fi.espoo.voltti.document.bucket.attachments")) }
        client.createBucket { it.bucket(environment.getRequiredProperty("fi.espoo.voltti.document.bucket.data")) }

        return client
    }

    @Bean
    fun jwtAlgorithm(): Algorithm {
        return Algorithm.RSA256(JwtKeys(privateKeyId = null, privateKey = null, publicKeys = mapOf()))
    }

}

internal class JwtKeys(
    private val privateKeyId: String?,
    private val privateKey: RSAPrivateKey?,
    private val publicKeys: Map<String, RSAPublicKey>
) : RSAKeyProvider {
    override fun getPrivateKeyId(): String? = privateKeyId
    override fun getPrivateKey(): RSAPrivateKey? = privateKey
    override fun getPublicKeyById(keyId: String): RSAPublicKey? = publicKeys[keyId]
}
