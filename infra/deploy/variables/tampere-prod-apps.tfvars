# SPDX-FileCopyrightText: 2023-2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

# common
municipality = "tampere"
environment  = "prod"

# datadog
datadog_enabled = true

# proxy
proxy_count = 3

# gateway
apigw_count = 4

apigw_service_key                = "apigw-to-service.key"
apigw_suomifi_identification_key = "apigw-to-suomifi-identification.key"
apigw_ad_key                     = "apigw-to-ad.key"
apigw_auth_certificate           = "apigw-to-suomifi-identification.crt"
apigw_auth_citizens_key          = "apigw-to-auth-citizens.key"
apigw_auth_employees_key         = "apigw-to-auth-employees.key"

# internal-gw
titania_enabled            = true
ad_saml_entrypoint_url     = "https://login.microsoftonline.com/dde5dc12-bd3c-4c06-85cc-34361efe9ad4/saml2"
ad_saml_public_cert        = ["TreVaka_Tuotanto.pem"]
ad_saml_external_id_prefix = "tampere-ad"

# service
service_count                           = 2
service_task_cpu                        = 1024
service_task_memory_mb                  = 4096
service_heap_size_mb                    = 2048
finance_decision_min_date               = "2023-03-01"
fee_decision_days_in_advance            = 123
voucher_value_decision_days_in_advance  = 123
varda_integration_api_url               = "https://backend.prod.varda.opintopolku.fi/api"
vtj_enabled                             = true
vtj_xroad_client_instance               = "FI"
vtj_xroad_client_membercode             = "0211675-2"
vtj_xroad_client_subsystemcode          = "treVaka"
vtj_xroad_service_instance              = "FI"
email_name                              = "Tampereen varhaiskasvatus"
dvv_modifications_service_xroadclientid = "FI/MUN/0211675-2/treVaka"
dvv_mutp_update_enabled                 = true
evaka_integration_sfi_enabled           = true

evaka_job_cancel_outdated_transfer_applications_enabled = true
evaka_job_freeze_voucher_value_reports_enabled          = true
evaka_job_freeze_voucher_value_reports_cron             = "0 30 23 L * ?"
evaka_job_remove_old_draft_applications_enabled         = true
send_pending_decision_reminder_emails_enabled           = true
evaka_job_inactive_people_cleanup_enabled               = true
evaka_job_inactive_employees_role_reset_enabled         = true
evaka_job_send_missing_reservation_reminders_enabled    = true
evaka_job_send_outdated_income_notifications_enabled    = true

# service: koski
evaka_integration_koski_enabled                = true
koski_integration_api_url                      = "https://koski.opintopolku.fi/koski/api"
evaka_integration_koski_municipality_caller_id = "tampereevaka"

# service: varda
varda_integration_enabled   = true
evaka_job_varda_update_cron = "0 0 5 * * 1,2,3,4,5"

# service: sfi message
sfi_msg_service_address      = "https://pr0.integraatiopalvelu.fi/Asiointitili/ViranomaispalvelutWSInterface"
sfi_msg_authority_identifier = "tampere_ws_evaka"
sfi_msg_service_identifier   = "tampere_ws_evaka"
sfi_msg_certificate_cn       = "Viestit"
sfi_msg_enable_printing      = true
sfi_msg_printing_provider    = "Posti"

# service: TampereProperties
tampere_job_export_units_acl_enabled = true

tampere_job_plan_bi_export_jobs_enabled = true
tampere_job_plan_bi_export_jobs_cron    = "0 0 1 * * *"

# auth
auth_count = 1

auth_task_cpu       = 1024
auth_task_memory_mb = 2048
