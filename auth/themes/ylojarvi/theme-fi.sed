# SPDX-FileCopyrightText: 2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

/^(espooLogoAlt[[:blank:]]*=[[:blank:]]*).*/{
s//\1Ylöjärven kaupungin logo/g
w /dev/stdout
}
