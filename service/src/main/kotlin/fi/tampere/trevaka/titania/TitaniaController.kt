// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.titania

import fi.espoo.evaka.shared.auth.AuthenticatedUser
import fi.espoo.evaka.shared.auth.AuthenticatedUserType
import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.domain.Forbidden
import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/integration/titania")
class TitaniaController(private val titaniaService: TitaniaService) {

    @PutMapping("/working-time-events")
    fun updateWorkingTimeEvents(
        @RequestBody request: UpdateWorkingTimeEventsRequest,
        user: AuthenticatedUser,
        db: Database,
    ): UpdateWorkingTimeEventsResponse {
        if (user.type != AuthenticatedUserType.integration) throw Forbidden()
        return db.connect { dbc -> dbc.transaction { tx -> titaniaService.updateWorkingTimeEvents(tx, request) } }
    }

    @PostMapping("/stamped-working-time-events")
    fun getStampedWorkingTimeEvents(
        @RequestBody request: GetStampedWorkingTimeEventsRequest,
        user: AuthenticatedUser,
        db: Database,
    ): GetStampedWorkingTimeEventsResponse {
        if (user.type != AuthenticatedUserType.integration) throw Forbidden()
        return db.connect { dbc -> dbc.read { tx -> titaniaService.getStampedWorkingTimeEvents(tx, request) } }
    }
}

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class TitaniaExceptionHandlers() {

    @ExceptionHandler(TitaniaException::class)
    fun titaniaExceptionHandler(ex: TitaniaException, req: HttpServletRequest): ResponseEntity<TitaniaErrorResponse> {
        val message = "${ex.status.name} (${ex.message})"
        when (ex.status.series()) {
            HttpStatus.Series.CLIENT_ERROR -> logger.warn { message }
            HttpStatus.Series.SERVER_ERROR -> logger.error { message }
            else -> logger.info { message }
        }
        return ResponseEntity
            .status(ex.status)
            .body(TitaniaErrorResponse(faultactor = req.requestURI, detail = ex.detail))
    }
}
