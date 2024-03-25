// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.kangasala.evaka

import fi.espoo.evaka.daycare.domain.ProviderType
import fi.espoo.evaka.decision.DecisionType
import fi.espoo.evaka.invoicing.domain.FeeDecisionType
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionType
import fi.espoo.evaka.placement.PlacementType
import org.springframework.test.context.ActiveProfiles
import trevaka.AbstractIntegrationTest
import java.util.stream.Stream

@ActiveProfiles(value = ["integration-test", "kangasala_evaka"])
abstract class AbstractKangasalaIntegrationTest : AbstractIntegrationTest() {
    protected fun supportedDecisionTypes(): Stream<DecisionType> = Stream.of(
        DecisionType.CLUB,
        DecisionType.DAYCARE,
        DecisionType.PRESCHOOL,
        DecisionType.PRESCHOOL_DAYCARE,
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
