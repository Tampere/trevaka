// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package trevaka.s3

import fi.hameenkyro.evaka.HameenkyroProperties
import fi.kangasala.evaka.KangasalaProperties
import fi.lempaala.evaka.LempaalaProperties
import fi.nokiankaupunki.evaka.NokiaProperties
import fi.orivesi.evaka.OrivesiProperties
import fi.pirkkala.evaka.PirkkalaProperties
import fi.tampere.trevaka.TampereProperties
import fi.vesilahti.evaka.VesilahtiProperties
import fi.ylojarvi.evaka.YlojarviProperties
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.s3.S3Client

@Component
@Profile("local", "integration-test")
class S3Initializer(
    client: S3Client,
    tampereProperties: TampereProperties?,
    vesilahtiProperties: VesilahtiProperties?,
    hameenkyroProperties: HameenkyroProperties?,
    nokiaProperties: NokiaProperties?,
    ylojarviProperties: YlojarviProperties?,
    pirkkalaProperties: PirkkalaProperties?,
    kangasalaProperties: KangasalaProperties?,
    lempaalaProperties: LempaalaProperties?,
    orivesiProperties: OrivesiProperties?,
) {
    init {
        tampereProperties?.let { createBucketsIfNeeded(client, it.bucket.allBuckets()) }
        vesilahtiProperties?.let { createBucketsIfNeeded(client, it.bucket.allBuckets()) }
        hameenkyroProperties?.let { createBucketsIfNeeded(client, it.bucket.allBuckets()) }
        nokiaProperties?.let { createBucketsIfNeeded(client, it.bucket.allBuckets()) }
        ylojarviProperties?.let { createBucketsIfNeeded(client, it.bucket.allBuckets()) }
        pirkkalaProperties?.let { createBucketsIfNeeded(client, it.bucket.allBuckets()) }
        kangasalaProperties?.let { createBucketsIfNeeded(client, it.bucket.allBuckets()) }
        lempaalaProperties?.let { createBucketsIfNeeded(client, it.bucket.allBuckets()) }
        orivesiProperties?.let { createBucketsIfNeeded(client, it.bucket.allBuckets()) }
    }
}

fun createBucketsIfNeeded(client: S3Client, allBuckets: List<String>) {
    val existingBuckets = client.listBuckets().buckets().map { it.name() }
    allBuckets
        .filterNot { bucket -> existingBuckets.contains(bucket) }
        .forEach { bucket -> client.createBucket { it.bucket(bucket) } }
}
