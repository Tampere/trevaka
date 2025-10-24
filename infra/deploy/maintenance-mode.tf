# SPDX-FileCopyrightText: 2023-2025 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

resource "aws_lb_listener_rule" "maintenance_mode" {
  count = var.maintenance_mode_enabled ? 1 : 0

  listener_arn = data.terraform_remote_state.base.outputs.public_alb_listener_arn
  priority     = 99

  condition {
    path_pattern {
      values = ["/*"]
    }
  }

  action {
    type = "fixed-response"

    fixed_response {
      content_type = "text/html"
      message_body = "<html><body>Huoltokatko</body></html>"
      status_code  = "503"
    }
  }
}
