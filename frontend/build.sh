#!/bin/bash

# SPDX-FileCopyrightText: 2021 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later

# Example build using customizations. This could be just a script in package.json or something else
set -euo pipefail

EVAKA=../evaka/frontend

export EVAKA_CUSTOMIZATIONS=tampere

cd "${EVAKA}" && yarn build
