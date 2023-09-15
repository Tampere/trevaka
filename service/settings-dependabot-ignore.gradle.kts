// SPDX-FileCopyrightText: 2021-2023 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

includeBuild("../evaka/service") {
    dependencySubstitution {
        substitute(module("evaka:evaka-bom")).using(project(":evaka-bom"))
        substitute(module("evaka:evaka-service")).using(project(":"))
    }
}
