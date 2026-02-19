<!--
SPDX-FileCopyrightText: 2023-2026 Tampere region

SPDX-License-Identifier: LGPL-2.1-or-later
-->

# Deploy

## Usage

    terraform init -reconfigure
    terraform apply -var-file ./variables/<municipality>-<environment>-apps.tfvars

or with command line arguments:

    terraform init -reconfigure -backend-config="bucket=<terraform_bucket>"
    terraform apply -var-file ./variables/<municipality>-<environment>-apps.tfvars \
        -var apps_version=<git_commit_id> \
        -var region=<region> \
        -var terraform_bucket=<terraform_bucket> \
        -var utility_account_id=<utility_account_id>

You can also query current version after `terraform init` with `terraform output`.
