# SPDX-FileCopyrightText: 2023 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

/^(espooLogoAlt[[:blank:]]*=[[:blank:]]*).*/{
s//\1Hämeenkyrön kunnan logo/g
w /dev/stdout
}
