// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.titania

import fi.espoo.evaka.shared.auth.AuthenticatedUser
import fi.tampere.trevaka.AbstractIntegrationTest
import fi.tampere.trevaka.asUser
import fi.tampere.trevaka.jsonBody
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.io.ClassPathResource

internal class TitaniaControllerTest : AbstractIntegrationTest() {

    @MockBean
    private lateinit var titaniaService: TitaniaService

    @Test
    fun `put working time events with valid example data should respond 200 and deserialize input correctly`() {
        whenever(titaniaService.updateWorkingTimeEvents(any())).thenReturn(UpdateWorkingTimeEventsResponse.ok())

        val (_, res, _) = http.put("/integration/titania/workingTimeEvents")
            .asUser(AuthenticatedUser.Integration)
            .jsonBody(ClassPathResource("titania/titania-update-request-valid-example-data.json"))
            .response()

        assertThat(res).returns(200) { it.statusCode }
        verify(titaniaService).updateWorkingTimeEvents(titaniaUpdateRequestValidExampleData)
    }

    @Test
    fun `put working time events with system user should respond 403`() {
        whenever(titaniaService.updateWorkingTimeEvents(any())).thenReturn(UpdateWorkingTimeEventsResponse.ok())

        val (_, res, _) = http.put("/integration/titania/workingTimeEvents")
            .asUser(AuthenticatedUser.SystemInternalUser)
            .jsonBody(ClassPathResource("titania/titania-update-request-valid-example-data.json"))
            .response()

        assertThat(res).returns(403) { it.statusCode }
        verifyNoInteractions(titaniaService)
    }

    @Test
    fun `put working time events without employee id should respond 400`() {
        whenever(titaniaService.updateWorkingTimeEvents(any())).thenReturn(UpdateWorkingTimeEventsResponse.ok())

        val (_, res, _) = http.put("/integration/titania/workingTimeEvents")
            .asUser(AuthenticatedUser.Integration)
            .jsonBody(ClassPathResource("titania/titania-update-request-without-employee-id.json"))
            .response()

        assertThat(res).returns(400) { it.statusCode }
        verifyNoInteractions(titaniaService)
    }

}
