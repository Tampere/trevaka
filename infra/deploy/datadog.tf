# SPDX-FileCopyrightText: 2023-2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

module "app_datadog_agent" {
  source = "./modules/datadog"

  municipality         = var.municipality
  environment          = var.environment
  image                = "${var.utility_account_id}.dkr.ecr.${var.region}.amazonaws.com/trevaka/datadog:${local.datadog_version}"
  desired_count        = var.datadog_enabled ? 1 : 0
  force_new_deployment = var.force_new_deployment || var.datadog_force_new_deployment

  vpc_id                   = data.terraform_remote_state.base.outputs.vpc_id
  ecs_cluster_id           = data.terraform_remote_state.base.outputs.ecs_cluster_id
  internal_domain_name     = data.terraform_remote_state.base.outputs.internal_domain_name
  internal_zone_id         = data.terraform_remote_state.base.outputs.internal_zone_id
  private_alb_listener_arn = data.terraform_remote_state.base.outputs.private_alb_listener_arn
  private_alb_dns_name     = data.aws_lb.private.dns_name
  private_alb_zone_id      = data.aws_lb.private.zone_id
  task_role_arn            = data.terraform_remote_state.base.outputs.ecs_tasks.datadog.task_role_arn
  execution_role_arn       = data.terraform_remote_state.base.outputs.ecs_tasks.datadog.execution_role_arn
  security_group_ids       = data.terraform_remote_state.base.outputs.ecs_tasks.datadog.security_group_ids
  private_subnet_ids       = data.aws_subnets.private.ids
  log_group_name           = data.terraform_remote_state.base.outputs.ecs_tasks.datadog.log_group_name
  region                   = var.region
}

variable "datadog_version" {
  type    = string
  default = ""
}

variable "datadog_force_new_deployment" {
  type    = bool
  default = false
}

locals {
  datadog_version = var.datadog_version != "" ? var.datadog_version : var.apps_version
}

output "datadog_version" {
  value = local.datadog_version
}
