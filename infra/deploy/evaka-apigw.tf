# SPDX-FileCopyrightText: 2023-2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

moved {
  from = module.app_apigw.aws_route53_record.internal[0]
  to   = aws_route53_record.evaka_apigw
}

resource "aws_route53_record" "evaka_apigw" {
  zone_id = data.terraform_remote_state.base.outputs.internal_zone_id
  name    = "${local.project}-apigw.${data.terraform_remote_state.base.outputs.internal_domain_name}"
  type    = "A"

  alias {
    name                   = data.aws_lb.private.dns_name
    zone_id                = data.aws_lb.private.zone_id
    evaluate_target_health = false
  }
}

module "app_apigw" {
  source = "./modules/ecs_service"

  region = var.region

  project        = local.project
  name           = "apigw"
  municipality   = var.municipality
  environment    = var.environment
  image          = "${var.utility_account_id}.dkr.ecr.${var.region}.amazonaws.com/trevaka/api-gateway:${local.apigw_version}"
  container_port = 3000
  desired_count  = var.apigw_count
  task_cpu       = 256
  task_memory    = 512
  host_headers   = [aws_route53_record.evaka_apigw.name]

  wait_for_steady_state = true
  force_new_deployment  = var.force_new_deployment || var.apigw_force_new_deployment

  vpc_id             = data.terraform_remote_state.base.outputs.vpc_id
  ecs_cluster_id     = data.terraform_remote_state.base.outputs.ecs_cluster_id
  alb_listener_arn   = data.terraform_remote_state.base.outputs.private_alb_listener_arn
  task_role_arn      = data.terraform_remote_state.base.outputs.ecs_tasks.evaka_apigw.task_role_arn
  execution_role_arn = data.terraform_remote_state.base.outputs.ecs_tasks.evaka_apigw.execution_role_arn
  security_group_ids = data.terraform_remote_state.base.outputs.ecs_tasks.evaka_apigw.security_group_ids
  private_subnet_ids = data.aws_subnets.private.ids
  log_group_name     = data.terraform_remote_state.base.outputs.ecs_tasks.evaka_apigw.log_group_name

  lb_listener_rule_priority = 100

  secrets = {
    DEPLOYMENT_BUCKET = "${local.param_prefix}/apigw/bucket/deployment"

    REDIS_HOST            = "${local.param_prefix}/apigw/redis/host"
    REDIS_PORT            = "${local.param_prefix}/apigw/redis/port"
    REDIS_TLS_SERVER_NAME = "${local.param_prefix}/apigw/redis/tls-name"
    REDIS_PASSWORD        = "${local.param_prefix}/gateway/redis/password"

    # enduser
    CITIZEN_COOKIE_SECRET = "${local.param_prefix}/enduser-gw/cookie/secret"
    DIGITRANSIT_API_KEY   = "${local.param_prefix}/enduser-gw/digitransit/api-key"

    # internal
    EMPLOYEE_COOKIE_SECRET = "${local.param_prefix}/internal-gw/cookie/secret"
    EVAKA_TITANIA_USERNAME = var.titania_enabled ? "${local.param_prefix}/internal-gw/titania/username" : null
    EVAKA_TITANIA_PASSWORD = var.titania_enabled ? "${local.param_prefix}/internal-gw/titania/password" : null
  }

  env_vars = {
    AWS_REGION        = var.region
    VOLTTI_ENV        = var.environment
    HTTP_PORT         = 3000
    EVAKA_BASE_URL    = local.frontend_url
    EVAKA_SERVICE_URL = "http://${aws_route53_record.evaka_service.name}"
    ENABLE_DEV_API    = false
    AD_MOCK           = local.apigw_ad_mock

    JWT_PRIVATE_KEY = "/home/evaka/s3/${coalesce(var.apigw_service_key, "apigw.key")}"
    JWT_KID         = "${local.project}-apigw"

    # enduser
    SFI_MODE              = var.environment == "prod" ? "prod" : "test"
    SFI_SAML_CALLBACK_URL = "${local.frontend_url}/api/application/auth/saml/login/callback"
    SFI_SAML_ISSUER       = "${local.frontend_url}/api/application/auth/saml/"
    SFI_SAML_PRIVATE_CERT = "/home/evaka/s3/${coalesce(var.apigw_suomifi_identification_key, "apigw.key")}"

    CITIZEN_WEAK_LOGIN_RATE_LIMIT = 5

    DIGITRANSIT_API_URL = "https://api.digitransit.fi"

    # internal
    AD_SAML_CALLBACK_URL       = !local.apigw_ad_mock ? "${local.frontend_url}/api/internal/auth/saml/login/callback" : null
    AD_SAML_ENTRYPOINT_URL     = !local.apigw_ad_mock ? var.ad_saml_entrypoint_url : null
    AD_SAML_LOGOUT_URL         = !local.apigw_ad_mock ? coalesce(var.ad_saml_logout_url, var.ad_saml_entrypoint_url) : null
    AD_SAML_ISSUER             = !local.apigw_ad_mock ? "${local.frontend_url}/api/internal/auth/saml/" : null
    AD_SAML_PUBLIC_CERT        = !local.apigw_ad_mock ? join(",", formatlist("/home/evaka/s3/%s", var.ad_saml_public_cert)) : null
    AD_SAML_PRIVATE_CERT       = !local.apigw_ad_mock ? "/home/evaka/s3/${coalesce(var.apigw_ad_key, "apigw.key")}" : null
    AD_SAML_EXTERNAL_ID_PREFIX = !local.apigw_ad_mock ? var.ad_saml_external_id_prefix : null

    EMPLOYEE_SESSION_TIMEOUT_MINUTES = "60"
  }
}

variable "apigw_version" {
  default = ""
}

variable "apigw_count" {
  description = "Desired count of apigw ECS tasks"
  type        = number
  default     = 0
}

variable "apigw_force_new_deployment" {
  type    = bool
  default = false
}

variable "apigw_service_key" {
  type    = string
  default = null
}

variable "apigw_suomifi_identification_key" {
  type    = string
  default = null
}

variable "apigw_ad_key" {
  type    = string
  default = null
}

variable "titania_enabled" {
  type    = bool
  default = false
}

variable "ad_saml_entrypoint_url" {
  type    = string
  default = null
}

variable "ad_saml_logout_url" {
  type    = string
  default = null
}

variable "ad_saml_public_cert" {
  type    = list(string)
  default = []
}

variable "ad_saml_external_id_prefix" {
  type    = string
  default = "ad"
}

locals {
  apigw_version = var.apigw_version != "" ? var.apigw_version : var.apps_version
  apigw_ad_mock = var.ad_saml_entrypoint_url == null
}

output "apigw_version" {
  value = local.apigw_version
}
