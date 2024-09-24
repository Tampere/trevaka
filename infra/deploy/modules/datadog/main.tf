# SPDX-FileCopyrightText: 2023-2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

locals {
  name    = var.municipality == "tampere" ? "datadog-agent" : "datadog"
  project = var.municipality == "tampere" ? "trevaka" : "${var.municipality}-evaka"
}
