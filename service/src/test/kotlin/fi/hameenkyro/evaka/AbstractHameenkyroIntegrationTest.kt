// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.hameenkyro.evaka

import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import org.springframework.test.context.ActiveProfiles
import software.amazon.awssdk.services.s3.S3Client
import trevaka.AbstractIntegrationTest

@ActiveProfiles(value = ["integration-test", "hameenkyro_evaka"])
abstract class AbstractHameenkyroIntegrationTest : AbstractIntegrationTest()

@Component
class HameenkyroIntegrationTestInit(private val client: S3Client, private val properties: HameenkyroProperties) :
    ApplicationListener<ContextRefreshedEvent> {
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        val existingBuckets = client.listBuckets().buckets().map { it.name() }
        properties.bucket.allBuckets()
            .filterNot { bucket -> existingBuckets.contains(bucket) }
            .forEach { bucket -> client.createBucket { it.bucket(bucket) } }
    }
}
