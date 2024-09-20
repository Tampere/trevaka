# SPDX-FileCopyrightText: 2023-2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

resource "aws_lb_target_group" "service" {
  port                 = var.container_ports[0]
  protocol             = var.container_protocol
  vpc_id               = var.vpc_id
  deregistration_delay = 60
  target_type          = "ip"

  tags = {
    Name = "${var.municipality}-evaka-${var.environment}-${var.name}"
  }

  lifecycle {
    create_before_destroy = true
  }

  health_check {
    healthy_threshold   = var.health_check_healthy_threshold
    unhealthy_threshold = var.health_check_unhealthy_threshold
    timeout             = var.health_check_timeout
    port                = var.health_check_port
    path                = var.health_check_path
    interval            = var.health_check_interval
    matcher             = var.health_check_codes
    protocol            = var.container_protocol
  }
}

resource "aws_lb_listener_rule" "service" {
  listener_arn = var.public ? var.public_alb_listener_arn : var.private_alb_listener_arn
  priority     = var.lb_listener_rule_priority

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.service.arn
  }

  dynamic "condition" {
    for_each = var.public ? [] : [1]
    content {
      host_header {
        values = concat(
          [local.internal_service_address],
          var.alb_include_public_host ? [var.public_domain_name] : [],
        )
      }
    }
  }

  condition {
    path_pattern {
      values = var.alb_paths
    }
  }
}
