// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

const path = require('path')

const defaults = {
  autorestart: false
}

module.exports = {
  apps: [{
    name: 'trevaka-apigw',
    script: 'yarn && yarn clean && yarn dev',
    cwd: path.resolve(__dirname, 'evaka/apigw'),
    env: {
      EVAKA_TITANIA_USERNAME: 'titania',
      EVAKA_TITANIA_PASSWORD: 'titania',
      EVAKA_CUSTOMER_SAML_ENTRYPOINT: 'http://localhost:8080/auth/realms/citizens/protocol/saml',
      EVAKA_CUSTOMER_SAML_ISSUER: 'trevaka',
      EVAKA_SAML_ENTRYPOINT: 'http://localhost:8080/auth/realms/employees/protocol/saml',
      EVAKA_SAML_ISSUER: 'trevaka',
      AD_SAML_EXTERNAL_ID_PREFIX: 'tampere-ad'
    },
    ...defaults
  }, {
    name: 'trevaka-frontend',
    script: 'yarn && yarn clean && yarn dev',
    cwd: path.resolve(__dirname, 'evaka/frontend'),
    env: {
      'EVAKA_CUSTOMIZATIONS': 'tampere',
      'ICONS': process.env.ICONS
    },
    env_tampere: {
      // default
    },
    env_vesilahti: {
      'EVAKA_CUSTOMIZATIONS': 'vesilahti',
    },
    env_hameenkyro: {
      'EVAKA_CUSTOMIZATIONS': 'hameenkyro',
    },
    env_ylojarvi: {
      'EVAKA_CUSTOMIZATIONS': 'ylojarvi',
    },
    ...defaults
  }, {
    name: 'trevaka-service',
    script: `${__dirname}/evaka/compose/run-after-db.sh`,
    args: './gradlew --no-daemon bootRun',
    cwd: path.resolve(__dirname, 'service'),
    env: {
      SPRING_PROFILES_ACTIVE: 'tampere_evaka',
    },
    env_tampere: {
      // default
    },
    env_vesilahti: {
      SPRING_PROFILES_ACTIVE: 'vesilahti_evaka',
    },
    env_hameenkyro: {
      SPRING_PROFILES_ACTIVE: 'hameenkyro_evaka',
    },
    env_ylojarvi: {
      SPRING_PROFILES_ACTIVE: 'ylojarvi_evaka',
    },
    ...defaults
  },
  ],
}
