#!/bin/node

// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

// usage: ./transform-users.js > ../src/main/resources/users.json

const fs = require("fs");
const data = fs.readFileSync("../../../evaka/service/src/main/resources/mock-vtj-data.json", "utf8");
const password = "mock";

const transformed = JSON.parse(data).reduce((prev, item) => ({
    ...prev,
    [item.socialSecurityNumber]: {
        "@class": "org.apereo.cas.adaptors.generic.CasUserAccount",
        password,
        "attributes": {
            "@class" : "java.util.LinkedHashMap",
            nationalIdentificationNumber:  ["java.util.List", [item.socialSecurityNumber]],
            sn: ["java.util.List", [item.lastName]],
            givenName: ["java.util.List", [item.firstNames.split(" ")[0]]],
        },
    },
}), {"@class": "java.util.LinkedHashMap"});

console.log(JSON.stringify(transformed, null, 2));
