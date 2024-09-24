# SPDX-FileCopyrightText: 2023-2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

resource "aws_ecs_service" "service" {
  name                              = "${local.project}-${local.name}"
  cluster                           = var.ecs_cluster_id
  task_definition                   = aws_ecs_task_definition.service.arn
  desired_count                     = var.desired_count
  health_check_grace_period_seconds = 300
  launch_type                       = "FARGATE"
  propagate_tags                    = "SERVICE"
  enable_ecs_managed_tags           = true
  platform_version                  = "1.4.0"
  wait_for_steady_state             = true
  force_new_deployment              = var.force_new_deployment

  load_balancer {
    target_group_arn = aws_lb_target_group.service.arn
    container_name   = "${local.project}-${local.name}-${var.environment}"
    container_port   = 8126
  }

  network_configuration {
    assign_public_ip = false
    security_groups  = var.security_group_ids
    subnets          = var.private_subnet_ids
  }
}

resource "aws_ecs_task_definition" "service" {
  family                   = "${local.project}-${local.name}-${var.environment}"
  task_role_arn            = var.task_role_arn
  execution_role_arn       = var.execution_role_arn
  cpu                      = 256
  memory                   = 512
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  tags                     = {}
  container_definitions = jsonencode([
    {
      name  = "${local.project}-${local.name}-${var.environment}"
      image = var.image
      portMappings = [
        { hostPort : 8126, containerPort : 8126, protocol : "tcp" },
        { hostPort : 5555, containerPort : 5555, protocol : "tcp" },
      ]
      environment = [for name, value in local.environment : { name : name, value : tostring(value) } if value != null]
      essential   = true
      secrets     = [for name, arn in local.secrets : { name : name, valueFrom : arn } if arn != null]
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

locals {
  # https://github.com/DataDog/datadog-agent/blob/main/pkg/config/config_template.yaml

  environment = {
    DD_ENV          = var.environment
    DD_HOSTNAME     = "${local.project}-${var.environment}"
    DD_SITE         = "datadoghq.eu"
    DD_APM_ENABLED  = true
    DD_LOGS_ENABLED = false
    ECS_FARGATE     = true
    DD_HEALTH_PORT  = 5555

    DD_LOG_LEVEL            = "info"
    DD_LOG_FORMAT_JSON      = true
    DD_LOG_TO_CONSOLE       = true
    DD_DISABLE_FILE_LOGGING = true
    DD_LOG_FORMAT_RFC3339   = true
  }

  secrets = {
    DD_API_KEY = "/${local.project}-${var.environment}/datadog/apikey"
  }
}
