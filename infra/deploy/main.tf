# SPDX-FileCopyrightText: 2023-2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

terraform {
  required_version = ">= 1.9.0, < 1.10.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
  backend "s3" {
    // region & bucket are configured with command-line key/value pairs (-backend-config="region=x" -backend-config="bucket=y")
    key            = "apps.tfstate"
    encrypt        = true
    dynamodb_table = "terraform-locks"
  }
}

locals {
  default_tags = {
    Municipality = var.municipality
  }
}

provider "aws" {
  region = var.region

  default_tags {
    tags = local.default_tags
  }
}

data "terraform_remote_state" "base" {
  backend = "s3"

  config = {
    region = var.region
    bucket = var.terraform_bucket
    key    = "base.tfstate"
  }
}

data "aws_subnets" "private" {
  filter {
    name   = "vpc-id"
    values = [data.terraform_remote_state.base.outputs.vpc_id]
  }

  filter {
    name   = "map-public-ip-on-launch"
    values = ["false"]
  }
}

data "aws_lb_listener" "private" {
  arn = data.terraform_remote_state.base.outputs.private_alb_listener_arn
}

data "aws_lb" "private" {
  arn = data.aws_lb_listener.private.load_balancer_arn
}

locals {
  project      = var.municipality == "tampere" ? "trevaka" : "${var.municipality}-evaka"
  param_prefix = "/${local.project}-${var.environment}"
  frontend_url = "https://${data.terraform_remote_state.base.outputs.public_domain_name}"
}
