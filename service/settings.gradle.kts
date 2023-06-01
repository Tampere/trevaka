// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

rootProject.name = "trevaka-service"
apply(from = "settings-dependabot-ignore.gradle.kts")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../evaka/service/gradle/libs.versions.toml"))
        }
    }
}
