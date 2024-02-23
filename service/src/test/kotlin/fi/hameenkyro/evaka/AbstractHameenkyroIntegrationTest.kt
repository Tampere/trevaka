// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.hameenkyro.evaka

import fi.espoo.evaka.daycare.domain.ProviderType
import fi.espoo.evaka.decision.DecisionType
import fi.espoo.evaka.invoicing.domain.FeeDecisionType
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionType
import fi.espoo.evaka.placement.PlacementType
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import org.springframework.test.context.ActiveProfiles
import software.amazon.awssdk.services.s3.S3Client
import trevaka.AbstractIntegrationTest
import java.util.stream.Stream

@ActiveProfiles(value = ["integration-test", "hameenkyro_evaka"])
abstract class AbstractHameenkyroIntegrationTest : AbstractIntegrationTest() {
    protected fun supportedDecisionTypes(): Stream<DecisionType> = Stream.of(
        DecisionType.DAYCARE,
        DecisionType.PRESCHOOL,
        DecisionType.PRESCHOOL_DAYCARE,
        DecisionType.PREPARATORY_EDUCATION,
    )

    protected fun supportedProviderTypes(): Stream<ProviderType> = Stream.of(
        ProviderType.MUNICIPAL,
        ProviderType.PURCHASED,
        ProviderType.PRIVATE,
        ProviderType.PRIVATE_SERVICE_VOUCHER,
    )

    protected fun supportedPlacementTypes(): Stream<PlacementType> = Stream.of(
        PlacementType.DAYCARE,
        PlacementType.PRESCHOOL,
        PlacementType.PREPARATORY_DAYCARE,
        PlacementType.PRESCHOOL_DAYCARE_ONLY,
        PlacementType.TEMPORARY_DAYCARE,
        PlacementType.SCHOOL_SHIFT_CARE,
    )

    protected fun supportedFeeDecisionTypes(): Stream<FeeDecisionType> = Stream.of(
        FeeDecisionType.NORMAL,
        FeeDecisionType.RELIEF_REJECTED,
        FeeDecisionType.RELIEF_ACCEPTED,
    )

    protected fun supportedVoucherValueDecisionTypes(): Stream<VoucherValueDecisionType> = Stream.of(
        VoucherValueDecisionType.NORMAL,
        VoucherValueDecisionType.RELIEF_REJECTED,
        VoucherValueDecisionType.RELIEF_ACCEPTED,
    )
}

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
