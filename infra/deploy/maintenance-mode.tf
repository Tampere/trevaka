# SPDX-FileCopyrightText: 2023-2025 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

resource "aws_lb_listener_rule" "maintenance_mode" {
  listener_arn = data.terraform_remote_state.base.outputs.public_alb_listener_arn
  priority     = var.maintenance_mode_enabled ? 100 : 900

  condition {
    path_pattern {
      values = ["/*"]
    }
  }

  action {
    type = "fixed-response"

    fixed_response {
      content_type = "text/html"
      message_body = "<!DOCTYPE html><html><head><title>Huoltokatko | Service break</title><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><style>html,body,header{margin:0;padding:0}body{background-color:#eee}header,article{background-color:white;padding:1rem}article{margin:3rem auto;max-width:600px;padding:4rem 2rem}*{font-family:sans-serif;font-weight:300}h2,#v{font-weight:400}#v{font-size:1.2em;color:navy}#e{position:absolute;top:72px;left:calc(50% - 32px);font-weight:700;font-size:36pt;background:coral;width:64px;line-height:64px;text-align:center;border-radius:50%;border:8px solid #eee}</style></head><body><header><h1><span id=\"v\">evaka</span> | Varhaiskasvatus</h1></header><article><p id=\"e\">!</p><h2>Huoltokatko</h2><h2>Service break </h2><p>Pahoittelemme aiheutunutta haittaa.</p><p>We apologize for the inconvenience</p></article></body></html>"
      status_code  = "503"
    }
  }
}
