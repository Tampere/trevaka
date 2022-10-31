// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.titania

import fi.espoo.evaka.pis.NewEmployee
import java.time.LocalDate
import java.time.LocalTime

internal val testEmployee = NewEmployee(
    firstName = "Test",
    lastName = "Person",
    email = "test.person@espoo.fi",
    externalId = null,
    employeeNumber = null,
    roles = setOf(),
)

// titania/titania-update-request-valid-example-data.json
internal val titaniaUpdateRequestValidExampleData = UpdateWorkingTimeEventsRequest(
    organisation = TitaniaCode(code = "88888", name = "Virtuaaliorganisaatio"),
    period = TitaniaPeriod(
        beginDate = LocalDate.of(2011, 1, 3),
        endDate = LocalDate.of(2011, 1, 23)
    ),
    schedulingUnit = listOf(
        TitaniaSchedulingUnit(
            code = "E1100", name = "Suunnittelupiste A", occupation = listOf(
                TitaniaOccupation(
                    code = "15510", name = "Kodinhoitaja", person = listOf(
                        TitaniaPerson(
                            employeeId = "00176716",
                            name = "ANKKA IINES",
                            TitaniaWorkingTimeEvents(
                                event = listOf(
                                    TitaniaWorkingTimeEvent(
                                        date = LocalDate.of(2011, 1, 3),
                                        code = "A",
                                        beginTime = LocalTime.of(7, 0),
                                        endTime = LocalTime.of(0, 0),
                                        placement = TitaniaCodeName("VV", "Vuorovastaava"),
                                        administrativeUnit = TitaniaCodeName("110", "Vastuuyksikkö"),
                                        operativeUnit = TitaniaCodeName("110A", "Toimipiste"),
                                        project = TitaniaCodeName("P2000", "Projekti 2000")
                                    ),
                                    TitaniaWorkingTimeEvent(
                                        date = LocalDate.of(2011, 1, 4),
                                        code = "V",
                                        description = "Vapaapaiva"
                                    )
                                )
                            ),
                            payrollItems = TitaniaPayrollItems(
                                item = listOf(
                                    TitaniaPayrollItem(
                                        code = "1010",
                                        type = TitaniaPayrollItemType.THIS,
                                        name = "Saldo",
                                        value = "90",
                                        unit = TitaniaPayrollItemUnit.MINUTE
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    )
)

internal val titaniaUpdateRequestValidMinimalData = UpdateWorkingTimeEventsRequest(
    period = TitaniaPeriod(
        beginDate = LocalDate.of(2011, 1, 3),
        endDate = LocalDate.of(2011, 1, 23)
    ),
    schedulingUnit = listOf(
        TitaniaSchedulingUnit(
            code = "E1100", occupation = listOf(
                TitaniaOccupation(
                    code = "15510", name = "Kodinhoitaja", person = listOf(
                        TitaniaPerson(
                            employeeId = "00176716",
                            name = "ANKKA IINES",
                            actualWorkingTimeEvents = TitaniaWorkingTimeEvents(
                                event = listOf(
                                    TitaniaWorkingTimeEvent(
                                        date = LocalDate.of(2010, 1, 1),
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    )
)

// titania/titania-get-request-valid-example-data.json
internal val titaniaGetRequestValidExampleData = GetStampedWorkingTimeEventsRequest(
    organisation = TitaniaCode(
        code = "88888",
        name = "Virtuaaliorganisaatio"
    ),
    period = TitaniaPeriod(
        beginDate = LocalDate.of(2014, 3, 3),
        endDate = LocalDate.of(2014, 3, 23)
    ),
    schedulingUnit = listOf(
        TitaniaStampedUnitRequest(
            code = "E1100",
            name = "Suunnittelupiste A",
            person = listOf(
                TitaniaStampedPersonRequest(
                    employeeId = "00177111",
                    name = "ANKKA IINES"
                ),
                TitaniaStampedPersonRequest(
                    employeeId = "00255145",
                    name = "HOPO HESSU"
                )
            )
        )
    )
)

// titania/titania-get-response-valid-example-data.json
internal val titaniaGetResponseValidExampleData = GetStampedWorkingTimeEventsResponse(
    schedulingUnit = listOf(
        TitaniaStampedUnitResponse(
            code = "E1100",
            name = "Suunnittelupiste A",
            person = listOf(
                TitaniaStampedPersonResponse(
                    employeeId = "00177111",
                    name = "ANKKA IINES",
                    stampedWorkingTimeEvents = TitaniaStampedWorkingTimeEvents(
                        event = listOf(
                            TitaniaStampedWorkingTimeEvent(
                                date = LocalDate.of(2014, 3, 3),
                                beginTime = LocalTime.of(7, 0),
                                endTime = LocalTime.of(15, 0),
                            ),
                            TitaniaStampedWorkingTimeEvent(
                                date = LocalDate.of(2014, 3, 4),
                                beginTime = LocalTime.of(6, 30),
                                endTime = LocalTime.of(12, 0),
                                beginReasonCode = "YT",
                            ),
                        )
                    )
                ),
                TitaniaStampedPersonResponse(
                    employeeId = "00255145",
                    name = "HOPO HESSU",
                    stampedWorkingTimeEvents = TitaniaStampedWorkingTimeEvents(
                        event = listOf(
                            TitaniaStampedWorkingTimeEvent(
                                date = LocalDate.of(2014, 3, 3),
                                beginTime = LocalTime.of(7, 0),
                                endTime = LocalTime.of(11, 0),
                            ),
                            TitaniaStampedWorkingTimeEvent(
                                date = LocalDate.of(2014, 3, 3),
                                beginTime = LocalTime.of(12, 5),
                                endTime = LocalTime.of(16, 10),
                            ),
                            TitaniaStampedWorkingTimeEvent(
                                date = LocalDate.of(2014, 3, 4),
                                beginTime = LocalTime.of(10, 15),
                                beginReasonCode = "TA",
                                endTime = LocalTime.of(17, 15),
                            ),
                        )
                    )
                )
            )
        ),
    )
)
