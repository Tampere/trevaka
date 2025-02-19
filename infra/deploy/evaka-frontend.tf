# SPDX-FileCopyrightText: 2023-2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

module "app_proxy" {
  source = "./modules/ecs_service"

  region = var.region

  project         = local.project
  name            = var.municipality == "tampere" ? "proxy" : "frontend"
  municipality    = var.municipality
  environment     = var.environment
  image           = "${var.utility_account_id}.dkr.ecr.${var.region}.amazonaws.com/trevaka/frontend/${var.municipality}:${local.frontend_version}"
  container_ports = [8080]
  desired_count   = var.proxy_count
  task_cpu        = 256
  task_memory     = 512
  public          = true

  wait_for_steady_state = true
  force_new_deployment  = var.force_new_deployment || var.proxy_force_new_deployment

  vpc_id                   = data.terraform_remote_state.base.outputs.vpc_id
  ecs_cluster_id           = data.terraform_remote_state.base.outputs.ecs_cluster_id
  public_domain_name       = data.terraform_remote_state.base.outputs.public_domain_name
  public_alb_listener_arn  = data.terraform_remote_state.base.outputs.public_alb_listener_arn
  internal_domain_name     = data.terraform_remote_state.base.outputs.internal_domain_name
  internal_zone_id         = data.terraform_remote_state.base.outputs.internal_zone_id
  private_alb_listener_arn = data.terraform_remote_state.base.outputs.private_alb_listener_arn
  private_alb_dns_name     = data.aws_lb.private.dns_name
  private_alb_zone_id      = data.aws_lb.private.zone_id
  task_role_arn            = data.terraform_remote_state.base.outputs.ecs_tasks.evaka_frontend.task_role_arn
  execution_role_arn       = data.terraform_remote_state.base.outputs.ecs_tasks.evaka_frontend.execution_role_arn
  security_group_ids       = data.terraform_remote_state.base.outputs.ecs_tasks.evaka_frontend.security_group_ids
  private_subnet_ids       = data.aws_subnets.private.ids
  log_group_name           = data.terraform_remote_state.base.outputs.ecs_tasks.evaka_frontend.log_group_name

  lb_listener_rule_priority = 100

  secrets = {
    RATE_LIMIT_CIDR_WHITELIST = "${local.param_prefix}/frontend/rate-limit-cidr-allowlist"
    STATIC_FILES_ENDPOINT_URL = "${local.param_prefix}/frontend/bucket/static-website-url"
  }

  env_vars = {
    ENDUSER_GW_URL  = "http://${module.app_apigw.internal_service_address}"
    INTERNAL_GW_URL = "http://${module.app_apigw.internal_service_address}"
    NGINX_ENV       = var.environment

    DD_PROFILING_ENABLED = var.datadog_enabled ? true : null
    DD_AGENT_HOST        = var.datadog_enabled ? module.app_datadog_agent.internal_service_address : null
    DD_AGENT_PORT        = var.datadog_enabled ? 80 : null
  }
}

variable "frontend_version" {
  default = ""
}

variable "proxy_count" {
  description = "Desired count of evaka-proxy ECS tasks"
  type        = number
  default     = 0
}

variable "proxy_force_new_deployment" {
  type    = bool
  default = false
}

locals {
  frontend_version = var.frontend_version != "" ? var.frontend_version : var.apps_version
}

output "frontend_version" {
  value = local.frontend_version
}
