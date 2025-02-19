# SPDX-FileCopyrightText: 2023-2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

resource "aws_ecs_service" "service" {
  name                              = "${var.project}-${var.name}"
  cluster                           = var.ecs_cluster_id
  task_definition                   = aws_ecs_task_definition.service.arn
  desired_count                     = var.desired_count
  health_check_grace_period_seconds = var.health_check_grace_period
  launch_type                       = "FARGATE"
  propagate_tags                    = "SERVICE"
  enable_ecs_managed_tags           = true
  platform_version                  = "1.4.0"
  wait_for_steady_state             = var.wait_for_steady_state
  force_new_deployment              = var.force_new_deployment

  load_balancer {
    target_group_arn = aws_lb_target_group.service.arn
    container_name   = "${var.project}-${var.name}-${var.environment}"
    container_port   = var.container_port
  }

  network_configuration {
    assign_public_ip = false
    security_groups  = var.security_group_ids
    subnets          = var.private_subnet_ids
  }

  timeouts {
    update = "${var.health_check_grace_period + 60 * 2}s"
  }
}

resource "aws_ecs_task_definition" "service" {
  family                   = "${var.project}-${var.name}-${var.environment}"
  task_role_arn            = var.task_role_arn
  execution_role_arn       = var.execution_role_arn
  cpu                      = var.task_cpu
  memory                   = var.task_memory
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"] # Helps with early validation for Fargate
  tags                     = {}
  container_definitions = jsonencode([
    {
      name         = "${var.project}-${var.name}-${var.environment}"
      image        = var.image
      portMappings = [{ hostPort : var.container_port, containerPort : var.container_port, protocol : "tcp" }]
      environment  = [for name, value in var.env_vars : { name : name, value : value } if value != null]
      essential    = true
      secrets      = [for name, arn in var.secrets : { name : name, valueFrom : arn } if arn != null]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          awslogs-group         = var.log_group_name
          awslogs-region        = var.region
          awslogs-stream-prefix = var.environment
        }
      }
      cpu            = 0
      mountPoints    = []
      volumesFrom    = []
      systemControls = []
    }
  ])
}
