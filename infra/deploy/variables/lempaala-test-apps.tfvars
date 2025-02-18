# SPDX-FileCopyrightText: 2023-2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

# common
municipality = "lempaala"
environment  = "test"

# datadog
datadog_enabled = false

# proxy
proxy_count = 1

# gateway
apigw_count = 1

# internal-gw
ad_saml_entrypoint_url     = "https://login.microsoftonline.com/dde5dc12-bd3c-4c06-85cc-34361efe9ad4/saml2"
ad_saml_public_cert        = ["lempaala-test-ad-2024-04.crt"]
ad_saml_external_id_prefix = "lempaala-ad"

# service
service_count                                 = 1
fee_decision_days_in_advance                  = 123
voucher_value_decision_days_in_advance        = 123
varda_integration_api_url                     = ""
vtj_enabled                                   = true
vtj_xroad_client_instance                     = "FI-TEST"
vtj_xroad_client_membercode                   = "0150783-1"
vtj_xroad_client_subsystemcode                = "lempaalaevaka"
vtj_xroad_service_instance                    = "FI-TEST"
email_allowlist                               = "^(.+)@lempaala.fi$"
email_name                                    = "Lempäälän varhaiskasvatus"
email_subject_postfix                         = "test"
dvv_modifications_service_xroadclientid       = "FI-TEST/MUN/0150783-1/lempaalaevaka"
dvv_mutp_update_enabled                       = false
send_pending_decision_reminder_emails_enabled = true
evaka_integration_sfi_enabled                 = true
evaka_integeration_sfi_rest_enabled           = true

evaka_job_inactive_employees_role_reset_enabled      = false
evaka_job_send_missing_reservation_reminders_enabled = true
evaka_job_send_outdated_income_notifications_enabled = true

# service: sfi message
sfi_msg_service_address      = "https://qat.integraatiopalvelu.fi/Asiointitili/ViranomaispalvelutWSInterfaceNonSigned"
sfi_msg_authority_identifier = "lempaala_ws_evaka"
sfi_msg_service_identifier   = "lempaala_ws_evaka"
sfi_msg_certificate_cn       = "eVaka Lempäälä"
sfi_msg_enable_printing      = false
sfi_msg_printing_provider    = "Posti"

# service: LempaalaProperties

# auth
auth_count   = 1
auth_enabled = false
