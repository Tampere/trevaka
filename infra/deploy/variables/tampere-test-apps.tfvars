# SPDX-FileCopyrightText: 2023-2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

frontend_version = "7a60d22deae8a851642cbf139d69b3df500cd6c0"
apigw_version    = "7a60d22deae8a851642cbf139d69b3df500cd6c0"
service_version  = "7a60d22deae8a851642cbf139d69b3df500cd6c0"

# common
municipality = "tampere"
environment  = "test"

# proxy
proxy_count = 1

# gateway
apigw_count = 1

apigw_service_key                = "apigw-to-service.key"
apigw_suomifi_identification_key = "apigw-to-suomifi-identification.key"
apigw_ad_key                     = "apigw-to-ad.key"

# internal-gw
ad_saml_entrypoint_url     = "https://login.microsoftonline.com/dde5dc12-bd3c-4c06-85cc-34361efe9ad4/saml2"
ad_saml_public_cert        = ["tamperead-internal-staging.pem"]
ad_saml_external_id_prefix = "tampere-ad"

# service
service_count                                 = 1
service_task_cpu                              = 1024
service_task_memory_mb                        = 4096
service_heap_size_mb                          = 2048
fee_decision_days_in_advance                  = 123
voucher_value_decision_days_in_advance        = 123
varda_integration_api_url                     = ""
vtj_enabled                                   = true
vtj_xroad_client_instance                     = "FI-TEST"
vtj_xroad_client_membercode                   = "0211675-2"
vtj_xroad_client_subsystemcode                = "treVaka"
vtj_xroad_service_instance                    = "FI-TEST"
email_allowlist                               = "^(.+)@tampere.fi$,^(.+)@ssrf.fi$"
email_name                                    = "Tampereen varhaiskasvatus"
email_subject_postfix                         = "staging"
dvv_modifications_service_xroadclientid       = "FI-TEST/MUN/0211675-2/treVaka"
dvv_mutp_update_enabled                       = false
send_pending_decision_reminder_emails_enabled = true
evaka_integration_sfi_enabled                 = false

evaka_job_freeze_voucher_value_reports_cron          = "0 30 23 L * ?"
evaka_job_inactive_employees_role_reset_enabled      = false
evaka_job_send_missing_reservation_reminders_enabled = true
evaka_job_send_outdated_income_notifications_enabled = true

# service: sfi message
sfi_msg_service_identifier = "tampere_ws_evaka"
sfi_msg_enable_printing    = false
sfi_msg_printing_provider  = "Posti"

# service: TampereProperties
