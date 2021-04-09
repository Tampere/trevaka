package fi.tampere.trevaka

import com.amazonaws.ClientConfigurationFactory
import com.amazonaws.Protocol
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.RSAKeyProvider
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

@TestConfiguration
class IntegrationTestConfiguration {

    @Bean
    fun s3Client(environment: Environment): AmazonS3 {
        val s3MockUrl = environment.getRequiredProperty("fi.espoo.voltti.s3mock.url")
        val region = environment.getRequiredProperty("aws.region")
        val client = AmazonS3ClientBuilder
            .standard()
            .enablePathStyleAccess()
            .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(s3MockUrl, region))
            .withClientConfiguration(ClientConfigurationFactory().config.withProtocol(Protocol.HTTP))
            .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials("foo", "bar")))
            .build()

        client.createBucket(environment.getRequiredProperty("fi.espoo.voltti.document.bucket.clubdecision"))
        client.createBucket(environment.getRequiredProperty("fi.espoo.voltti.document.bucket.daycaredecision"))
        client.createBucket(environment.getRequiredProperty("fi.espoo.voltti.document.bucket.paymentdecision"))
        client.createBucket(environment.getRequiredProperty("fi.espoo.voltti.document.bucket.vouchervaluedecision"))
        client.createBucket(environment.getRequiredProperty("fi.espoo.voltti.document.bucket.attachments"))

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
