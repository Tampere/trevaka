#!/bin/bash

# SPDX-FileCopyrightText: 2023-2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

set -euo pipefail

(cd "$(dirname "$0")/../evaka/frontend"; \
  yarn eslint --max-warnings 0 "$@" \
    src/lib-customizations/tampere/. \
    src/lib-customizations/vesilahti/. \
    src/lib-customizations/hameenkyro/. \
    src/lib-customizations/nokia/. \
    src/lib-customizations/ylojarvi/. \
    src/lib-customizations/pirkkala/. \
    src/lib-customizations/kangasala/. \
    src/lib-customizations/lempaala/. \
    src/lib-customizations/orivesi/.
)
