// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
    mavenCentral()
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}
