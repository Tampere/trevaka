// SPDX-FileCopyrightText: 2023 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.vesilahti.evaka

import fi.espoo.evaka.daycare.domain.ProviderType
import fi.espoo.evaka.decision.DecisionType
import fi.espoo.evaka.invoicing.domain.FeeDecisionType
import fi.espoo.evaka.placement.PlacementType
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import org.springframework.test.context.ActiveProfiles
import software.amazon.awssdk.services.s3.S3Client
import trevaka.AbstractIntegrationTest
import java.util.stream.Stream

@ActiveProfiles(value = ["integration-test", "vesilahti_evaka"])
abstract class AbstractVesilahtiIntegrationTest : AbstractIntegrationTest() {
    protected fun supportedDecisionTypes(): Stream<DecisionType> = Stream.of(
        DecisionType.DAYCARE,
        DecisionType.PRESCHOOL_DAYCARE,
    )

    protected fun supportedProviderTypes(): Stream<ProviderType> = Stream.of(
        ProviderType.MUNICIPAL,
        ProviderType.PURCHASED,
        ProviderType.PRIVATE,
    )

    protected fun supportedPlacementTypes(): Stream<PlacementType> = Stream.of(
        PlacementType.DAYCARE,
        PlacementType.PRESCHOOL_DAYCARE_ONLY,
        PlacementType.TEMPORARY_DAYCARE,
        PlacementType.SCHOOL_SHIFT_CARE,
    )

    protected fun supportedFeeDecisionTypes(): Stream<FeeDecisionType> = Stream.of(
        FeeDecisionType.NORMAL,
        FeeDecisionType.RELIEF_REJECTED,
        FeeDecisionType.RELIEF_ACCEPTED,
    )
}

@Component
class VesilahtiIntegrationTestInit(private val client: S3Client, private val properties: VesilahtiProperties) : ApplicationListener<ContextRefreshedEvent> {
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        val existingBuckets = client.listBuckets().buckets().map { it.name() }
        properties.bucket.allBuckets()
            .filterNot { bucket -> existingBuckets.contains(bucket) }
            .forEach { bucket -> client.createBucket { it.bucket(bucket) } }
    }
}
