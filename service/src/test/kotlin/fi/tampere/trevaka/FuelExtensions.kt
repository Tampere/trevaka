// SPDX-FileCopyrightText: 2017-2020 City of Espoo
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.extensions.jsonBody
import fi.espoo.evaka.shared.auth.AuthenticatedUser
import fi.espoo.evaka.shared.auth.applyToJwt
import org.springframework.core.io.Resource
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateCrtKey
import java.security.spec.PKCS8EncodedKeySpec
import java.time.Clock
import java.time.ZonedDateTime
import java.util.Base64
import java.util.Date

fun Request.jsonBody(resource: Resource, charset: Charset = StandardCharsets.UTF_8) =
    jsonBody(resourceAsString(resource, charset))

fun Request.asUser(user: AuthenticatedUser, clock: Clock? = Clock.systemDefaultZone()): Request {
    val now = ZonedDateTime.now(clock)
    val token = user.applyToJwt(
        JWT.create()
            .withKeyId("integration-test").withIssuer("integration-test")
            .withIssuedAt(Date.from(now.toInstant()))
            .withExpiresAt(Date.from(now.plusHours(12).toInstant()))
    ).sign(algorithm)
    return this.header("Authorization", "Bearer $token")
}

private val privateKeyText =
    """
MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCu2xNCpcwhgBrA
PynkAYdmJLt2FhEMh0DnTgGaJ+2W1RgUzHU/xyf/IaHR2x2CzTHWAjj49TTA1hs+
PVxTEKiRWmb11swIOMeNqS78rMOLQszwghJWHCERFJqJIdVqHOhBSRrrYyPj+LGY
33eys/GNxGJcHnaIJFYIdaH5lCfI3OcK2oUaexN6V4UnFS9EygYM+HVZG1b3SnNS
wv/zj8N03CGghUGiFRlACpUCy8f4pCiQ9FgQAwRx+AzvtyZ+ABQeATh1SCM8EAVv
wQf91Pm0pVK2wAs6KaSKZtV2AHf2tGrHv0aaccMWvqS4+p9EFoM6cAaNYwrw3+Di
185kG04tAgMBAAECggEAGsoz7D0sxmEzOAs1ZtqZHRw5oIEHAa1jU00PT8gYTci+
VMlymV/xD9TeTyHMJrM7lHdotTQUbgsfx2xtTci6mvKH3diYEBZq9JhcO5hYqWiF
r7+uAYVzx4MpniMR6J0fNIl312KsKAx9YVezpiyNsNPK4iREst0mVUt4kp2RbuOw
lODMhfCY8bpHnRYZ0PZATKsS467zmtvGg6n4kMrkNgUmLxmixu7tkQvFItObkGbu
Z2Fw76f7bbEna2h5QwqZJtjTBWu9fuoVE+yrutoUDZfcdytdnDQb+Iim3fTcfHoF
Uu4Ejvt9U7ND4WNYBnioNTDy0bU0JsvGFqFESs/YXQKBgQDeY1J077cF+RIOqMpX
y1ktbYqCld2jwBKRdh7cq6RrvuKAwOV8EZHgRjDtQilPkZ5EYSaLd18OpEnHaNul
kyq5Xf8c/pDIVJaE/DJxv+mdD3OEQfNIeQ4tTt9GQ9CSRmc4xUyrtMSuF1TEelyE
SR3yWQZ1rTbmitxRS4nYkOXBNwKBgQDJSKCueZ7IBYK4QyZvyF+AqluGvpraaDX8
cURyjWiGeMbcmmIM4uVeDAufK7je8ejh6OEkZnElQkG1g5jDzeZN1oV2mpGmQIqB
54rShnwAB1QsQNgpsMWXsobIxlkjOeM9w6TEiAEofzUPXnsxCeTXM21j1X0/Mbl/
4CLS63OtuwKBgQCm+nyXQMZzvahJSYNkmFLFQVKW6mAY68cFYWOa3WX/YLxBYJTu
q2KUux9RhjPugrALN8bxQRgmrkSXbaw+jlmuBV3gL2QWhyzdfV+C+U43l7psu/gn
mn2Cl44d/B1HN4WZossGwhxLe0kynY2J5jyOEzo+cWifgfQbNlSyP1nD0QKBgAP+
6hmslmdYHKk1xAySCLtQJnL9U6ukR/YT9Yzkm7+XpDL66Zlh47XDPXPSdWAPo1Tg
v59uXS6lKxLEYwL4EmtXJ90b4mpPe7BqfUoCm3GQs64RN3lUZgfF5oET1u8pFgbC
3IF+Ra+dHtuIP5/Ql3diMPi7Yzoe7ZTJSUewnuJDAoGBAIqL1Qk2teLOs5Ub2Wzk
S+k53NM9reKCWjbwy1KtsmrNjs1vQvjBptCO7u5E70/IUlfYzTCfXOvgevWUq7hz
xItTHbCFft9xr8yk62C7ruSrfl+CFwMHWq1lqrOfuN/kIWUlskIqy4yd1EvEqo8W
9OPpBjnpM+7RemDE7M9i9nsQ
    """.trimIndent()

private val algorithm: Algorithm by lazy {
    Algorithm.RSA256(null, jwtPrivateKey)
}

internal val jwtPrivateKey: RSAPrivateCrtKey by lazy {
    val kf = KeyFactory.getInstance("RSA")
    val spec = PKCS8EncodedKeySpec(Base64.getMimeDecoder().decode(privateKeyText))
    kf.generatePrivate(spec) as RSAPrivateCrtKey
}
