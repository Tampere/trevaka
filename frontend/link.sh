#!/bin/bash

# SPDX-FileCopyrightText: 2021 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later

# Example how to link these customizations to evaka build
set -euo pipefail

EVAKA=../evaka/frontend
CUSTOMIZATIONS="${EVAKA}"/src/lib-customizations/tampere

if [ ! -e "${CUSTOMIZATIONS}" ]; then
  ln -v -s $(readlink -f ./tampere) "${CUSTOMIZATIONS}"
else
  echo "${CUSTOMIZATIONS}" already exists: no linking was done
fi
