# SPDX-FileCopyrightText: 2023-2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

resource "aws_route53_record" "internal" {
  count = var.public ? 0 : 1

  zone_id = var.internal_zone_id
  name    = local.internal_service_address
  type    = "A"

  alias {
    name                   = var.private_alb_dns_name
    zone_id                = var.private_alb_zone_id
    evaluate_target_health = false
  }
}
