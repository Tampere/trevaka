// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.titania

import java.time.LocalDate
import java.time.LocalTime

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
                            employeeId = "176716",
                            name = "ANKKA IINES",
                            TitaniaWorkingTimeEvents(
                                event = listOf(
                                    TitaniaWorkingTimeEvent(
                                        date = LocalDate.of(2010, 1, 1),
                                        code = "A",
                                        beginTime = LocalTime.of(7, 0),
                                        endTime = LocalTime.of(15, 0),
                                        placement = TitaniaCodeName("VV", "Vuorovastaava"),
                                        administrativeUnit = TitaniaCodeName("110", "Vastuuyksikkö"),
                                        operativeUnit = TitaniaCodeName("110A", "Toimipiste"),
                                        project = TitaniaCodeName("P2000", "Projekti 2000")
                                    ),
                                    TitaniaWorkingTimeEvent(
                                        date = LocalDate.of(2010, 1, 2),
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
                            employeeId = "176716",
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
