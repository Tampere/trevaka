# SPDX-FileCopyrightText: 2023-2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

# common
municipality = "lempaala"
environment  = "prod"

# datadog
datadog_enabled = false

# proxy
proxy_count = 2

# gateway
apigw_count = 2

# internal-gw
titania_enabled            = true
ad_saml_entrypoint_url     = "https://login.microsoftonline.com/dde5dc12-bd3c-4c06-85cc-34361efe9ad4/saml2"
ad_saml_public_cert        = ["lempaala-prod-ad-2024-04.crt"]
ad_saml_external_id_prefix = "lempaala-ad"

# service
service_count                           = 2
service_task_cpu                        = 1024
service_task_memory_mb                  = 4096
service_heap_size_mb                    = 2048
finance_decision_min_date               = "2024-05-01"
fee_decision_days_in_advance            = 123
voucher_value_decision_days_in_advance  = 123
varda_integration_api_url               = "https://backend.prod.varda.opintopolku.fi/api"
evaka_integration_varda_start_date      = "2024-05-01"
vtj_enabled                             = true
vtj_xroad_client_instance               = "FI"
vtj_xroad_client_membercode             = "0150783-1"
vtj_xroad_client_subsystemcode          = "lempaalaevaka"
vtj_xroad_service_instance              = "FI"
email_name                              = "Lempäälän varhaiskasvatus"
dvv_modifications_service_xroadclientid = "FI/MUN/0150783-1/lempaalaevaka"
dvv_mutp_update_enabled                 = true
evaka_integration_sfi_enabled           = true

evaka_job_cancel_outdated_transfer_applications_enabled = false
evaka_job_freeze_voucher_value_reports_enabled          = true
evaka_job_remove_old_draft_applications_enabled         = true
send_pending_decision_reminder_emails_enabled           = true
evaka_job_inactive_people_cleanup_enabled               = true
evaka_job_inactive_employees_role_reset_enabled         = true
evaka_job_send_missing_reservation_reminders_enabled    = true
evaka_job_send_missing_reservation_reminders_cron       = "0 35 9 * * 4"
evaka_job_send_outdated_income_notifications_enabled    = true

# service: koski
evaka_integration_koski_enabled                = false
koski_integration_api_url                      = "https://koski.opintopolku.fi/koski/api"
evaka_integration_koski_municipality_caller_id = "lempaalaevaka"

# service: varda
varda_integration_enabled = true

# service: sfi message
sfi_msg_service_address      = "https://pr0.integraatiopalvelu.fi/Asiointitili/ViranomaispalvelutWSInterface"
sfi_msg_authority_identifier = "lempaala_ws_evaka"
sfi_msg_service_identifier   = "lempaala_ws_evaka"
sfi_msg_certificate_cn       = "eVaka Lempäälä"
sfi_msg_enable_printing      = true
sfi_msg_printing_provider    = "Posti"

# service: jamix
jamix_orders_enabled = true
jamix_diets_enabled  = true

# service: LempaalaProperties

# auth
auth_count = 1

auth_task_cpu       = 1024
auth_task_memory_mb = 2048
