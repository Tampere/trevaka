# SPDX-FileCopyrightText: 2023-2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

resource "aws_lb_target_group" "service" {
  port                 = 8126
  protocol             = "HTTP"
  vpc_id               = var.vpc_id
  deregistration_delay = 60
  target_type          = "ip"

  health_check {
    healthy_threshold   = 2
    unhealthy_threshold = 3
    timeout             = 5
    port                = 5555
    path                = "/"
    interval            = 60
    matcher             = "200"
    protocol            = "HTTP"
  }

  tags = {
    Name = "${var.municipality}-evaka-${var.environment}-${local.name}"
  }

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_lb_listener_rule" "service" {
  listener_arn = var.private_alb_listener_arn
  priority     = 400

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.service.arn
  }

  condition {
    host_header {
      values = [aws_route53_record.internal.name]
    }
  }

  condition {
    path_pattern {
      values = ["/*"]
    }
  }
}
