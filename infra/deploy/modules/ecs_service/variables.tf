# SPDX-FileCopyrightText: 2023-2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

variable "region" {
  description = "The AWS region"
}

variable "vpc_id" {
  type = string
}

variable "ecs_cluster_id" {
  type = string
}

variable "public_domain_name" {
  type = string
}

variable "internal_domain_name" {
  type = string
}

variable "internal_zone_id" {
  type = string
}

variable "public_alb_listener_arn" {
  type = string
}

variable "private_alb_listener_arn" {
  type = string
}

variable "private_alb_dns_name" {
  type = string
}

variable "private_alb_zone_id" {
  type = string
}

# Stack specific configuration

variable "project" {
  type = string
}

variable "name" {
  description = "The name of the service (used also for AWS billing). Can only contain characters that are alphanumeric characters or hyphens(-)"
}

variable "municipality" {
  type = string
}

variable "environment" {
  type = string
}

variable "image" {
  type = string
}

variable "public" {
  description = "Is this private or public service?"
  default     = false
}

variable "alb_include_public_host" {
  type    = bool
  default = false
}

variable "alb_paths" {
  description = "What path and/or host_headers will be routed from ALB to this service"
  default     = ["/*"]
}

variable "env_vars" {
  description = "Environment variables for the container"
  type        = map(string)
  default     = {}
}

variable "secrets" {
  description = "Map of secrets to expose to the container, from Parameter Store. Key is the environment variable name, value is the ARN of the secret (see example)."
  type        = map(string)
  default     = {}
}

variable "health_check_port" {
  type    = number
  default = null
}

variable "health_check_path" {
  description = "PATH for lb health check"
  default     = "/health"
}

variable "health_check_codes" {
  description = "OK HTTP codes for health check"
  default     = "200"
}

variable "health_check_healthy_threshold" {
  description = "How many healthy checks to declare service healthy"
  default     = 2
}

variable "health_check_unhealthy_threshold" {
  description = "How many failed"
  default     = 3
}

variable "health_check_interval" {
  description = "Interval for healt checking"
  default     = 60
}

variable "health_check_timeout" {
  description = "Timeout for the check"
  default     = 5
}

variable "health_check_grace_period" {
  description = "Service definition health check grace period (seconds). Service scheduler ignores ELB health checks for a pre-defined time period after a task has been instantiated"
  default     = 300
}

variable "wait_for_steady_state" {
  type    = bool
  default = false
}

variable "force_new_deployment" {
  type    = bool
  default = false
}

variable "container_ports" {
  description = "Port where docker is listening"
  default     = [8080]
}

variable "container_protocol" {
  description = "Protocol is docker listening"
  default     = "HTTP"
}

variable "internal_ports" {
  description = "Additional ports which are exposed to service itself"
  type        = list(number)
  default     = []
}

variable "desired_count" {
  description = "How many containers should we run"
  default     = 1
}

variable "task_cpu" {
  description = "The number of CPU units used by ALL of the containers in a task."
  type        = number
}

variable "task_memory" {
  description = "Task-level memory limit used by ALL of the containers in a task."
  type        = number
}

variable "task_role_arn" {
  type = string
}

variable "execution_role_arn" {
  type = string
}

variable "security_group_ids" {
  type = list(string)
}

variable "private_subnet_ids" {
  type = list(string)
}

variable "log_group_name" {
  type = string
}

variable "service_discovery_service_arn" {
  type    = string
  default = null
}

variable "lb_listener_rule_priority" {
  type = number
}

locals {
  internal_service_address = "${var.project}-${var.name}.${var.internal_domain_name}"
}
