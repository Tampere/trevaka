#!/bin/bash
# Example how to link these customizations to evaka build
set -euo pipefail

EVAKA=../evaka/frontend
CUSTOMIZATIONS="${EVAKA}"/src/lib-customizations/tampere

if [ ! -e "${CUSTOMIZATIONS}" ]; then
  ln -v -s $(readlink -f ./tampere) "${CUSTOMIZATIONS}"
else
  echo "${CUSTOMIZATIONS}" already exists: no linking was done
fi
