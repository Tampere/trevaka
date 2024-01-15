// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.vesilahti.evaka

import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import org.springframework.test.context.ActiveProfiles
import software.amazon.awssdk.services.s3.S3Client
import trevaka.AbstractIntegrationTest

@ActiveProfiles(value = ["integration-test", "vesilahti_evaka"])
abstract class AbstractVesilahtiIntegrationTest : AbstractIntegrationTest()

@Component
class VesilahtiIntegrationTestInit(private val client: S3Client, private val properties: VesilahtiProperties) : ApplicationListener<ContextRefreshedEvent> {
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        val existingBuckets = client.listBuckets().buckets().map { it.name() }
        properties.bucket.allBuckets()
            .filterNot { bucket -> existingBuckets.contains(bucket) }
            .forEach { bucket -> client.createBucket { it.bucket(bucket) } }
    }
}
