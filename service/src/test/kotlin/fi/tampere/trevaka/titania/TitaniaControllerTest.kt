// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.titania

import fi.espoo.evaka.shared.auth.AuthenticatedUser
import fi.tampere.trevaka.AbstractIntegrationTest
import fi.tampere.trevaka.asUser
import fi.tampere.trevaka.jsonBody
import fi.tampere.trevaka.resourceAsString
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import org.skyscreamer.jsonassert.JSONAssert.assertEquals
import org.skyscreamer.jsonassert.JSONCompareMode
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus

internal class TitaniaControllerTest : AbstractIntegrationTest() {

    @MockBean
    private lateinit var titaniaService: TitaniaService

    @Test
    fun `put working time events with valid example data should respond 200 and deserialize input correctly`() {
        whenever(titaniaService.updateWorkingTimeEvents(any(), any()))
            .thenReturn(UpdateWorkingTimeEventsResponse.ok())

        val (_, res, _) = http.put("/integration/titania/working-time-events")
            .asUser(AuthenticatedUser.Integration)
            .jsonBody(ClassPathResource("titania/titania-update-request-valid-example-data.json"))
            .response()

        assertThat(res).returns(200) { it.statusCode }
        verify(titaniaService).updateWorkingTimeEvents(any(), eq(titaniaUpdateRequestValidExampleData))
    }

    @Test
    fun `put working time events with system user should respond 403`() {
        whenever(titaniaService.updateWorkingTimeEvents(any(), any()))
            .thenReturn(UpdateWorkingTimeEventsResponse.ok())

        val (_, res, _) = http.put("/integration/titania/working-time-events")
            .asUser(AuthenticatedUser.SystemInternalUser)
            .jsonBody(ClassPathResource("titania/titania-update-request-valid-example-data.json"))
            .response()

        assertThat(res).returns(403) { it.statusCode }
        verifyNoInteractions(titaniaService)
    }

    @Test
    fun `put working time events without employee id should respond 400`() {
        whenever(titaniaService.updateWorkingTimeEvents(any(), any()))
            .thenReturn(UpdateWorkingTimeEventsResponse.ok())

        val (_, res, _) = http.put("/integration/titania/working-time-events")
            .asUser(AuthenticatedUser.Integration)
            .jsonBody(ClassPathResource("titania/titania-update-request-without-employee-id.json"))
            .response()

        assertThat(res).returns(400) { it.statusCode }
        verifyNoInteractions(titaniaService)
    }

    @Test
    fun `put working time events with titania exception should respond 400`() {
        whenever(titaniaService.updateWorkingTimeEvents(any(), any()))
            .thenThrow(
                TitaniaException(
                    HttpStatus.BAD_REQUEST,
                    listOf(
                        TitaniaErrorDetail(TitaniaError.INVALID_EMPLOYEE_NUMBER, "Error description"),
                        TitaniaErrorDetail(TitaniaError.INVALID_EMPLOYEE_NUMBER, "Error description"),
                    )
                )
            )

        val (_, res, _) = http.put("/integration/titania/working-time-events")
            .asUser(AuthenticatedUser.Integration)
            .jsonBody(ClassPathResource("titania/titania-update-request-valid-example-data.json"))
            .response()

        assertThat(res).returns(400) { it.statusCode }
        assertEquals(
            """{
    "faultcode": "Server",
    "faultstring": "multiple",
    "faultactor": "/integration/titania/working-time-events",
    "detail": [
        {
            "errorcode": "101",
            "message": "Error description"
        },
        {
            "errorcode": "101",
            "message": "Error description"
        }
    ]
}""",
            res.body().asString("application/json"), JSONCompareMode.STRICT
        )

        verify(titaniaService).updateWorkingTimeEvents(any(), eq(titaniaUpdateRequestValidExampleData))
    }

    @Test
    fun `get stamped working time events with valid example data should respond 200 and deserialize input correctly`() {
        whenever(titaniaService.getStampedWorkingTimeEvents(any(), any()))
            .thenReturn(titaniaGetResponseValidExampleData)

        val (_, res, _) = http.post("/integration/titania/stamped-working-time-events")
            .asUser(AuthenticatedUser.Integration)
            .jsonBody(ClassPathResource("titania/titania-get-request-valid-example-data.json"))
            .response()

        assertThat(res).returns(200) { it.statusCode }
        assertEquals(
            resourceAsString(ClassPathResource("titania/titania-get-response-valid-example-data.json")),
            res.body().asString("application/json"),
            JSONCompareMode.STRICT
        )
        verify(titaniaService).getStampedWorkingTimeEvents(any(), eq(titaniaGetRequestValidExampleData))
    }

    @Test
    fun `get stamped working time events with system user should respond 403`() {
        whenever(titaniaService.getStampedWorkingTimeEvents(any(), any()))
            .thenReturn(titaniaGetResponseValidExampleData)

        val (_, res, _) = http.post("/integration/titania/stamped-working-time-events")
            .asUser(AuthenticatedUser.SystemInternalUser)
            .jsonBody(ClassPathResource("titania/titania-get-request-valid-example-data.json"))
            .response()

        assertThat(res).returns(403) { it.statusCode }
        verifyNoInteractions(titaniaService)
    }

    @Test
    fun `get stamped working time events without employee id should respond 400`() {
        whenever(titaniaService.getStampedWorkingTimeEvents(any(), any()))
            .thenReturn(titaniaGetResponseValidExampleData)

        val (_, res, _) = http.post("/integration/titania/stamped-working-time-events")
            .asUser(AuthenticatedUser.Integration)
            .jsonBody(ClassPathResource("titania/titania-get-request-without-employee-id.json"))
            .response()

        assertThat(res).returns(400) { it.statusCode }
        verifyNoInteractions(titaniaService)
    }
}
