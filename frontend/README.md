<!--
SPDX-FileCopyrightText: 2021 City of Tampere

SPDX-License-Identifier: LGPL-2.1-or-later
-->

# eVaka frontend customizations PoC

* the `tampere` directory must be linked under main eVaka as `frontend/src/lib-customizations/tampere` using symbolic or hard links. See `link.sh` for an example
* after linked, main eVaka build automatically uses these customizations instead of Espoo-specific ones when environment variable `EVAKA_CUSTOMIZATIONS=tampere` is used. This applies to all builds, including local dev server (`yarn dev`) and production builds (`yarn build`)
* no new library dependencies can be installed, because these files are basically included in the main eVaka `lib-customizations` builds as is, and don't form a separate package/project
* customizations can freely be split into multiple files, but everything must be reachable by following the import chain from the main `citizen.tsx` / `employee.tsx` files
* `employee-mobile-frontend` is not included in this PoC, but it can be added using the same mechanism
