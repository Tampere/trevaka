# SPDX-FileCopyrightText: 2023-2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

# external

variable "region" {
  type = string
}

variable "terraform_bucket" {
  type = string
}

variable "utility_account_id" {
  type = string
}

variable "apps_version" {
  type = string
}

variable "force_new_deployment" {
  type    = bool
  default = false
}

variable "maintenance_mode_bucket" {
  type    = string
  default = null
}

# internal

variable "municipality" {
  type = string
}

variable "environment" {
  type = string
}
