<!--
SPDX-FileCopyrightText: 2021 City of Tampere

SPDX-License-Identifier: LGPL-2.1-or-later
-->

# eVaka frontend customizations

* municipality directories must be linked under main eVaka as `frontend/src/lib-customizations/<municipality>` using symbolic or hard links. See `link.sh` for an example
* after linked, main eVaka build automatically uses these customizations instead of Espoo-specific ones when environment variable `EVAKA_CUSTOMIZATIONS=<municipality>` is used. This applies to all builds, including local dev server (`yarn dev`) and production builds (`yarn build`)
* no new library dependencies can be installed, because these files are basically included in the main eVaka `lib-customizations` builds as is, and don't form a separate package/project
* customizations can freely be split into multiple files, but everything must be reachable by following the import chain from the main `common.tsx` / `citizen.tsx` / `employee.tsx` / `employeeMobile.tsx` files

# Icons

See [eVaka documentation](https://github.com/espoon-voltti/evaka/blob/master/frontend/README.md#font-awesome-icon-library).
