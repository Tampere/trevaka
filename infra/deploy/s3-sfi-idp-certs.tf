# SPDX-FileCopyrightText: 2026 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

variable "sfi_idp_certificate_years" {
  description = "List of SFI IDP certificate years to load from SSM and publish to S3 (e.g. [2024, 2026])"
  type        = list(number)
  default     = []
}

locals {
  sfi_idp_cert_years           = toset([for y in var.sfi_idp_certificate_years : tostring(y)])
  sfi_idp_cert_container_paths = [for y in sort(keys(aws_s3_object.sfi-idp-certificate)) : "/home/evaka/s3/sfi-idp-certificate-${y}.pem"]
}

data "aws_ssm_parameter" "sfi-idp-certificate" {
  for_each = local.sfi_idp_cert_years
  name     = "/${local.project}-${var.environment}/suomifi/idp-certificate-${each.key}"
}

resource "aws_s3_object" "sfi-idp-certificate" {
  for_each = local.sfi_idp_cert_years
  bucket   = "${local.project}-${var.environment}-deployment"
  key      = "api-gw/sfi-idp-certificate-${each.key}.pem"
  content  = data.aws_ssm_parameter.sfi-idp-certificate[each.key].value
}
