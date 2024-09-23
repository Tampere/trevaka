# SPDX-FileCopyrightText: 2023-2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

# common
municipality = "tampere"
environment  = "dev"

# datadog
datadog_enabled = false

# proxy
proxy_count = 1

# gateway
apigw_count = 1

apigw_service_key                = "apigw-to-service.key"
apigw_suomifi_identification_key = "apigw-to-suomifi-identification.key"
apigw_ad_key                     = "apigw-to-ad.key"
apigw_auth_certificate           = "apigw-to-suomifi-identification.crt"
apigw_auth_citizens_key          = "apigw-to-auth-citizens.key"
apigw_auth_employees_key         = "apigw-to-auth-employees.key"

# enduser-gw
enduser_gw_auth_saml_issuer = "evaka-customer"

# internal-gw
titania_enabled            = true
ad_saml_external_id_prefix = "tampere-ad"

evaka_saml_issuer = "evaka"

# service
service_count                                 = 1
fee_decision_days_in_advance                  = 123
voucher_value_decision_days_in_advance        = 123
varda_integration_api_url                     = "https://backend.qa.varda.opintopolku.fi/api"
vtj_enabled                                   = true
vtj_xroad_client_instance                     = "FI-TEST"
vtj_xroad_client_membercode                   = "0211675-2"
vtj_xroad_client_subsystemcode                = "treVaka"
vtj_xroad_service_instance                    = "FI-TEST"
email_allowlist                               = "^(.+)@tampere.fi$"
email_name                                    = "Tampereen varhaiskasvatus"
email_subject_postfix                         = "test"
dvv_modifications_service_xroadclientid       = "FI-TEST/MUN/0211675-2/treVaka"
dvv_mutp_update_enabled                       = false
send_pending_decision_reminder_emails_enabled = true
evaka_integration_sfi_enabled                 = true

evaka_job_inactive_employees_role_reset_enabled      = false
evaka_job_send_missing_reservation_reminders_enabled = true
evaka_job_send_outdated_income_notifications_enabled = true

# service: koski
evaka_integration_koski_enabled                = true
koski_integration_api_url                      = "https://koski.testiopintopolku.fi/koski/api"
evaka_integration_koski_municipality_caller_id = "tampereevaka"

# service: sfi message
sfi_msg_service_address      = "https://qat.integraatiopalvelu.fi/Asiointitili/ViranomaispalvelutWSInterfaceNonSigned"
sfi_msg_authority_identifier = "tampere_ws_evaka"
sfi_msg_service_identifier   = "tampere_ws_evaka"
sfi_msg_certificate_cn       = "Viestit"
sfi_msg_enable_printing      = false
sfi_msg_printing_provider    = "Posti"
sfi_msg_keystore_alias       = "viestit"

# service: TampereProperties
tampere_job_export_units_acl_enabled = true

tampere_job_plan_bi_export_jobs_enabled = true
tampere_job_plan_bi_export_jobs_cron    = "0 0 1 * * *"

# auth
auth_count          = 1
auth_internal_realm = "tampere"

service_logging_levels = {
  "fi.espoo.evaka.titania" : "DEBUG"
}