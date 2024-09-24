# SPDX-FileCopyrightText: 2023-2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

resource "aws_service_discovery_private_dns_namespace" "apps" {
  name        = "${local.project}-${var.environment}"
  description = "Service discovery namespace for apps"
  vpc         = data.terraform_remote_state.base.outputs.vpc_id
}

resource "aws_service_discovery_service" "auth" {
  name = "auth"

  dns_config {
    namespace_id = aws_service_discovery_private_dns_namespace.apps.id

    dns_records {
      ttl  = 10
      type = "A"
    }

    routing_policy = "MULTIVALUE"
  }

  health_check_custom_config {
    failure_threshold = 1
  }
}
