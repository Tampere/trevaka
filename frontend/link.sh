#!/bin/bash

# SPDX-FileCopyrightText: 2021 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later

# Example how to link these customizations to evaka build
set -euo pipefail

EVAKA=../evaka/frontend

for MUNICIPALITY in tampere vesilahti hameenkyro ylojarvi pirkkala nokia kangasala lempaala
do
  CUSTOMIZATIONS="${EVAKA}"/src/lib-customizations/${MUNICIPALITY}
  if [ ! -e "${CUSTOMIZATIONS}" ]; then
    ln -v -s "$(readlink -f ./${MUNICIPALITY})" "${CUSTOMIZATIONS}"
  else
    echo "${CUSTOMIZATIONS} already exists: no linking was done"
  fi
done
