# SPDX-FileCopyrightText: 2023-2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

variable "municipality" {
  type = string
}

variable "environment" {
  type = string
}

variable "image" {
  type = string
}

variable "desired_count" {
  type = number
}

variable "force_new_deployment" {
  type = bool
}

variable "vpc_id" {
  type = string
}

variable "ecs_cluster_id" {
  type = string
}

variable "internal_domain_name" {
  type = string
}

variable "internal_zone_id" {
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

variable "region" {
  type = string
}
