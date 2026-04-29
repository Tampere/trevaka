# SPDX-FileCopyrightText: 2023-2026 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

variable "sfi_idp_certificate_years" {
  description = "List of SFI IDP certificate years present in S3 (e.g. [2024, 2026])"
  type        = list(number)
  default     = []
}

locals {
  sfi_idp_cert_years           = toset([for y in var.sfi_idp_certificate_years : tostring(y)])
  sfi_idp_cert_container_paths = [for y in sort(local.sfi_idp_cert_years) : "/home/evaka/s3/sfi-idp-certificate-${y}.pem"]
}
