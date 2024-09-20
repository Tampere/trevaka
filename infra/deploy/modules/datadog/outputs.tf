# SPDX-FileCopyrightText: 2023-2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

output "internal_service_address" {
  value = aws_route53_record.internal.name
}
