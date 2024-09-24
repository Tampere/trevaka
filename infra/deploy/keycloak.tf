# SPDX-FileCopyrightText: 2023-2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

module "app_auth" {
  source = "./modules/ecs_service"

  region = var.region

  project                   = local.project
  name                      = "auth"
  municipality              = var.municipality
  environment               = var.environment
  image                     = "${var.utility_account_id}.dkr.ecr.${var.region}.amazonaws.com/trevaka/keycloak:${local.keycloak_version}"
  container_ports           = [8080, 9000]
  internal_ports            = [7800, 7800 + 50000]
  desired_count             = var.auth_count
  task_cpu                  = var.auth_task_cpu
  task_memory               = var.auth_task_memory_mb
  health_check_grace_period = 600
  health_check_port         = 9000
  health_check_path         = "/auth/health"
  alb_include_public_host   = true
  alb_paths                 = ["/auth", "/auth/*"]

  wait_for_steady_state = true
  force_new_deployment  = var.force_new_deployment || var.auth_force_new_deployment

  vpc_id                   = data.terraform_remote_state.base.outputs.vpc_id
  ecs_cluster_id           = data.terraform_remote_state.base.outputs.ecs_cluster_id
  public_domain_name       = data.terraform_remote_state.base.outputs.public_domain_name
  public_alb_listener_arn  = data.terraform_remote_state.base.outputs.public_alb_listener_arn
  internal_domain_name     = data.terraform_remote_state.base.outputs.internal_domain_name
  internal_zone_id         = data.terraform_remote_state.base.outputs.internal_zone_id
  private_alb_listener_arn = data.terraform_remote_state.base.outputs.private_alb_listener_arn
  private_alb_dns_name     = data.aws_lb.private.dns_name
  private_alb_zone_id      = data.aws_lb.private.zone_id
  task_role_arn            = data.terraform_remote_state.base.outputs.ecs_tasks.keycloak.task_role_arn
  execution_role_arn       = data.terraform_remote_state.base.outputs.ecs_tasks.keycloak.execution_role_arn
  security_group_ids       = data.terraform_remote_state.base.outputs.ecs_tasks.keycloak.security_group_ids
  private_subnet_ids       = data.aws_subnets.private.ids
  log_group_name           = data.terraform_remote_state.base.outputs.ecs_tasks.keycloak.log_group_name

  service_discovery_service_arn = aws_service_discovery_service.auth.arn

  lb_listener_rule_priority = 300

  # https://www.keycloak.org/server/all-config

  secrets = {
    # database
    KC_DB_URL_HOST     = "${local.param_prefix}/auth/db/host"
    KC_DB_URL_PORT     = "${local.param_prefix}/auth/db/port"
    KC_DB_URL_DATABASE = "${local.param_prefix}/auth/db/name"
    KC_DB_USERNAME     = "${local.param_prefix}/auth/db/username"
    KC_DB_PASSWORD     = "${local.param_prefix}/auth/db/password"
  }

  env_vars = {
    JAVA_OPTS_APPEND = "-Djgroups.dns.query=${aws_service_discovery_service.auth.name}.${aws_service_discovery_private_dns_namespace.apps.name}"

    KEYCLOAK_ADMIN          = var.auth_admin_username
    KEYCLOAK_ADMIN_PASSWORD = var.auth_admin_password

    # cache
    KC_CACHE       = "ispn"
    KC_CACHE_STACK = "kubernetes"

    # hostname
    KC_HOSTNAME = "${local.frontend_url}/auth"

    # http(s)
    KC_HTTP_ENABLED = true

    # proxy
    KC_PROXY_HEADERS = "xforwarded"

    # logging
    KC_LOG                = "console"
    KC_LOG_CONSOLE_OUTPUT = "json"
  }
}

variable "keycloak_version" {
  type    = string
  default = ""
}

variable "auth_count" {
  description = "Desired count of auth ECS tasks"
  type        = string
  default     = 0
}

variable "auth_force_new_deployment" {
  type    = bool
  default = false
}

variable "auth_task_cpu" {
  type    = number
  default = 512
}

variable "auth_task_memory_mb" {
  type    = number
  default = 1024
}

variable "auth_internal_realm" {
  type    = string
  default = "employees"
}

variable "auth_enduser_realm" {
  type    = string
  default = "citizens"
}

variable "auth_admin_username" {
  type    = string
  default = null
}

variable "auth_admin_password" {
  type    = string
  default = null
}

locals {
  keycloak_version = var.keycloak_version != "" ? var.keycloak_version : var.apps_version
}

output "keycloak_version" {
  value = local.keycloak_version
}
