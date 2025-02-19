# SPDX-FileCopyrightText: 2023-2024 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

module "app_service" {
  source = "./modules/ecs_service"

  region = var.region

  project                   = local.project
  name                      = "service"
  municipality              = var.municipality
  environment               = var.environment
  image                     = "${var.utility_account_id}.dkr.ecr.${var.region}.amazonaws.com/trevaka/service:${local.service_version}"
  container_port            = 8888
  desired_count             = var.service_count
  task_cpu                  = var.service_task_cpu
  task_memory               = var.service_task_memory_mb
  health_check_grace_period = 60 * 10
  health_check_path         = "/health"

  wait_for_steady_state = true
  force_new_deployment  = var.force_new_deployment || var.service_force_new_deployment

  vpc_id                   = data.terraform_remote_state.base.outputs.vpc_id
  ecs_cluster_id           = data.terraform_remote_state.base.outputs.ecs_cluster_id
  internal_domain_name     = data.terraform_remote_state.base.outputs.internal_domain_name
  internal_zone_id         = data.terraform_remote_state.base.outputs.internal_zone_id
  public_alb_listener_arn  = data.terraform_remote_state.base.outputs.public_alb_listener_arn
  private_alb_listener_arn = data.terraform_remote_state.base.outputs.private_alb_listener_arn
  private_alb_dns_name     = data.aws_lb.private.dns_name
  private_alb_zone_id      = data.aws_lb.private.zone_id
  task_role_arn            = data.terraform_remote_state.base.outputs.ecs_tasks.evaka_service.task_role_arn
  execution_role_arn       = data.terraform_remote_state.base.outputs.ecs_tasks.evaka_service.execution_role_arn
  security_group_ids       = data.terraform_remote_state.base.outputs.ecs_tasks.evaka_service.security_group_ids
  private_subnet_ids       = data.aws_subnets.private.ids
  log_group_name           = data.terraform_remote_state.base.outputs.ecs_tasks.evaka_service.log_group_name

  lb_listener_rule_priority = 200

  secrets = {
    # Dockerfile / entrypoint.sh
    DEPLOYMENT_BUCKET = "${local.param_prefix}/service/bucket/deployment"

    # WebPushEnv
    EVAKA_WEB_PUSH_VAPID_PRIVATE_KEY = "${local.param_prefix}/service/web-push/vapid-private-key"

    # DatabaseEnv
    EVAKA_DATABASE_URL             = "${local.param_prefix}/service/database/url"
    EVAKA_DATABASE_USERNAME        = "${local.param_prefix}/service/datasource/username"
    EVAKA_DATABASE_PASSWORD        = "${local.param_prefix}/service/datasource/password"
    EVAKA_DATABASE_FLYWAY_USERNAME = "${local.param_prefix}/service/flyway/username"
    EVAKA_DATABASE_FLYWAY_PASSWORD = "${local.param_prefix}/service/flyway/password"

    # BucketEnv
    EVAKA_BUCKET_DATA                    = "${local.param_prefix}/service/bucket/data"
    EVAKA_BUCKET_ATTACHMENTS             = "${local.param_prefix}/service/bucket/attachments"
    EVAKA_BUCKET_DECISIONS               = "${local.param_prefix}/service/bucket/daycaredecisions"
    EVAKA_BUCKET_FEE_DECISIONS           = "${local.param_prefix}/service/bucket/paymentdecisions"
    EVAKA_BUCKET_VOUCHER_VALUE_DECISIONS = "${local.param_prefix}/service/bucket/vouchervaluedecisions"

    # KoskiEnv
    EVAKA_INTEGRATION_KOSKI_USER   = var.evaka_integration_koski_enabled ? "${local.param_prefix}/service/koski/user" : null
    EVAKA_INTEGRATION_KOSKI_SECRET = var.evaka_integration_koski_enabled ? "${local.param_prefix}/service/koski/secret" : null

    # VardaEnv
    EVAKA_INTEGRATION_VARDA_BASIC_AUTH = "${local.param_prefix}/service/varda/basic-auth"

    # DvvModificationsEnv
    EVAKA_INTEGRATION_DVV_MODIFICATIONS_URL      = "${local.param_prefix}/service/dvv-modifications/url"
    EVAKA_INTEGRATION_DVV_MODIFICATIONS_USER_ID  = "${local.param_prefix}/service/dvv-modifications/userid"
    EVAKA_INTEGRATION_DVV_MODIFICATIONS_PASSWORD = "${local.param_prefix}/service/dvv-modifications/password"

    # VtjEnv
    EVAKA_INTEGRATION_VTJ_USERNAME = "${local.param_prefix}/service/vtj/username"
    EVAKA_INTEGRATION_VTJ_PASSWORD = "${local.param_prefix}/service/vtj/password"

    # VtjXroadEnv
    EVAKA_INTEGRATION_VTJ_XROAD_ADDRESS = "${local.param_prefix}/service/vtj/address"

    # SfiEnv
    EVAKA_INTEGRATION_SFI_KEY_STORE_PASSWORD = var.evaka_integration_sfi_enabled ? "${local.param_prefix}/message-service/keystore/password" : null
    EVAKA_INTEGRATION_SFI_REST_USERNAME      = var.evaka_integration_sfi_enabled ? "${local.param_prefix}/service/sfi/username" : null

    # SfiPrintingEnv
    EVAKA_INTEGRATION_SFI_PRINTING_BILLING_ID       = var.evaka_integration_sfi_enabled ? "${local.param_prefix}/service/sfi/printing/id" : null
    EVAKA_INTEGRATION_SFI_PRINTING_BILLING_PASSWORD = var.evaka_integration_sfi_enabled ? "${local.param_prefix}/service/sfi/printing/password" : null

    # SfiContactPersonEnv
    EVAKA_INTEGRATION_SFI_CONTACT_PERSON_NAME  = var.evaka_integration_sfi_enabled ? "${local.param_prefix}/service/sfi/contact/name" : null
    EVAKA_INTEGRATION_SFI_CONTACT_PERSON_PHONE = var.evaka_integration_sfi_enabled ? "${local.param_prefix}/service/sfi/contact/phone" : null
    EVAKA_INTEGRATION_SFI_CONTACT_PERSON_EMAIL = var.evaka_integration_sfi_enabled ? "${local.param_prefix}/service/sfi/contact/email" : null

    # JamixEnv
    EVAKA_INTEGRATION_JAMIX_USER     = local.jamix_enabled ? "${local.param_prefix}/service/jamix/user" : null
    EVAKA_INTEGRATION_JAMIX_PASSWORD = local.jamix_enabled ? "${local.param_prefix}/service/jamix/password" : null

    # TampereProperties
    TAMPERE_IPAAS_USERNAME = var.municipality == "tampere" ? "${local.param_prefix}/service/ipaas/username" : null
    TAMPERE_IPAAS_PASSWORD = var.municipality == "tampere" ? "${local.param_prefix}/service/ipaas/password" : null
    TAMPERE_BUCKET_EXPORT  = var.municipality == "tampere" ? "${local.param_prefix}/service/bucket/export" : null
    TAMPERE_INVOICE_URL    = var.municipality == "tampere" ? "${local.param_prefix}/service/invoice/url" : null
    TAMPERE_PAYMENT_URL    = var.municipality == "tampere" ? "${local.param_prefix}/service/payment/url" : null

    # VesilahtiProperties
    VESILAHTI_IPAAS_USERNAME = var.municipality == "vesilahti" ? "${local.param_prefix}/service/ipaas/username" : null
    VESILAHTI_IPAAS_PASSWORD = var.municipality == "vesilahti" ? "${local.param_prefix}/service/ipaas/password" : null
    VESILAHTI_BUCKET_EXPORT  = var.municipality == "vesilahti" ? "${local.param_prefix}/service/bucket/export" : null

    # HameenkyroProperties
    HAMEENKYRO_IPAAS_USERNAME = var.municipality == "hameenkyro" ? "${local.param_prefix}/service/ipaas/username" : null
    HAMEENKYRO_IPAAS_PASSWORD = var.municipality == "hameenkyro" ? "${local.param_prefix}/service/ipaas/password" : null
    HAMEENKYRO_BUCKET_EXPORT  = var.municipality == "hameenkyro" ? "${local.param_prefix}/service/bucket/export" : null

    # NokiaProperties
    NOKIA_IPAAS_USERNAME = var.municipality == "nokia" ? "${local.param_prefix}/service/ipaas/username" : null
    NOKIA_IPAAS_PASSWORD = var.municipality == "nokia" ? "${local.param_prefix}/service/ipaas/password" : null
    NOKIA_BUCKET_EXPORT  = var.municipality == "nokia" ? "${local.param_prefix}/service/bucket/export" : null

    # YlojarviProperties
    YLOJARVI_IPAAS_USERNAME = var.municipality == "ylojarvi" ? "${local.param_prefix}/service/ipaas/username" : null
    YLOJARVI_IPAAS_PASSWORD = var.municipality == "ylojarvi" ? "${local.param_prefix}/service/ipaas/password" : null
    YLOJARVI_BUCKET_EXPORT  = var.municipality == "ylojarvi" ? "${local.param_prefix}/service/bucket/export" : null

    # PirkkalaProperties
    PIRKKALA_IPAAS_USERNAME = var.municipality == "pirkkala" ? "${local.param_prefix}/service/ipaas/username" : null
    PIRKKALA_IPAAS_PASSWORD = var.municipality == "pirkkala" ? "${local.param_prefix}/service/ipaas/password" : null
    PIRKKALA_BUCKET_EXPORT  = var.municipality == "pirkkala" ? "${local.param_prefix}/service/bucket/export" : null

    # KangasalaProperties
    KANGASALA_IPAAS_USERNAME = var.municipality == "kangasala" ? "${local.param_prefix}/service/ipaas/username" : null
    KANGASALA_IPAAS_PASSWORD = var.municipality == "kangasala" ? "${local.param_prefix}/service/ipaas/password" : null
    KANGASALA_BUCKET_EXPORT  = var.municipality == "kangasala" ? "${local.param_prefix}/service/bucket/export" : null

    # LempaalaProperties
    LEMPAALA_IPAAS_USERNAME = var.municipality == "lempaala" ? "${local.param_prefix}/service/ipaas/username" : null
    LEMPAALA_IPAAS_PASSWORD = var.municipality == "lempaala" ? "${local.param_prefix}/service/ipaas/password" : null
    LEMPAALA_BUCKET_EXPORT  = var.municipality == "lempaala" ? "${local.param_prefix}/service/bucket/export" : null

    # OrivesiProperties
    ORIVESI_IPAAS_USERNAME = var.municipality == "orivesi" ? "${local.param_prefix}/service/ipaas/username" : null
    ORIVESI_IPAAS_PASSWORD = var.municipality == "orivesi" ? "${local.param_prefix}/service/ipaas/password" : null
    ORIVESI_BUCKET_EXPORT  = var.municipality == "orivesi" ? "${local.param_prefix}/service/bucket/export" : null
  }

  env_vars = {
    # Dockerfile / entrypoint.sh
    VOLTTI_ENV = var.environment
    JAVA_OPTS  = "-Djava.security.egd=file:/dev/./urandom -Djdk.tls.client.protocols=TLSv1.2,TLSv1.3 -XX:MaxMetaspaceSize=256m -XX:ReservedCodeCacheSize=256m -Xms512m -Xmx${var.service_heap_size_mb}m"

    DD_PROFILING_ENABLED = var.datadog_enabled ? true : null
    DD_AGENT_HOST        = var.datadog_enabled ? module.app_datadog_agent.internal_service_address : null
    DD_TRACE_AGENT_PORT  = var.datadog_enabled ? 80 : null
    DD_SERVICE           = var.datadog_enabled ? "evaka-service" : null
    DD_VERSION           = var.datadog_enabled ? local.service_version : null

    # Spring
    SPRING_APPLICATION_JSON = length(var.service_logging_levels) > 0 ? jsonencode({ "logging.level" : var.service_logging_levels }) : null
    SPRING_PROFILES_ACTIVE  = join(",", ["production", "${var.municipality}_evaka"])

    # EvakaEnv
    EVAKA_INTEGRATION_KOSKI_ENABLED              = var.evaka_integration_koski_enabled
    EVAKA_INTEGRATION_SFI_ENABLED                = var.evaka_integration_sfi_enabled
    EVAKA_INTEGRATION_VTJ_ENABLED                = var.vtj_enabled
    EVAKA_ASYNC_JOB_RUNNER_DISABLE_RUNNER        = var.evaka_async_job_runner_disable_runner
    EVAKA_FRONTEND_BASE_URL_FI                   = local.frontend_url
    EVAKA_FRONTEND_BASE_URL_SV                   = local.frontend_url
    EVAKA_FEE_DECISION_MIN_DATE                  = var.finance_decision_min_date
    EVAKA_FEE_DECISION_DAYS_IN_ADVANCE           = var.fee_decision_days_in_advance
    EVAKA_VOUCHER_VALUE_DECISION_DAYS_IN_ADVANCE = var.voucher_value_decision_days_in_advance
    EVAKA_PASSWORD_BLACKLIST_DIRECTORY           = "/opt/password-blacklists"

    EVAKA_NOT_FOR_PROD_FORCE_UNPUBLISH_DOCUMENT_TEMPLATE_ENABLED = contains(["dev", "test"], var.environment) ? true : null

    # JwtEnv
    EVAKA_JWT_PUBLIC_KEYS_URL = "file:///home/ubuntu/s3/jwks.json"

    # WebPushEnv
    EVAKA_WEB_PUSH_ENABLED = true

    # EmailEnv
    EVAKA_EMAIL_ENABLED                                = true
    EVAKA_EMAIL_WHITELIST                              = var.email_allowlist
    EVAKA_EMAIL_SENDER_ADDRESS                         = "noreply@${data.terraform_remote_state.base.outputs.public_domain_name}"
    EVAKA_EMAIL_SENDER_NAME_FI                         = var.email_name
    EVAKA_EMAIL_SENDER_NAME_SV                         = var.email_name
    EVAKA_EMAIL_SUBJECT_POSTFIX                        = var.email_subject_postfix
    EVAKA_EMAIL_APPLICATION_RECEIVED_SENDER_ADDRESS_FI = "noreply@${data.terraform_remote_state.base.outputs.public_domain_name}"
    EVAKA_EMAIL_APPLICATION_RECEIVED_SENDER_ADDRESS_SV = "noreply@${data.terraform_remote_state.base.outputs.public_domain_name}"
    EVAKA_EMAIL_APPLICATION_RECEIVED_SENDER_NAME_FI    = var.email_name
    EVAKA_EMAIL_APPLICATION_RECEIVED_SENDER_NAME_SV    = var.email_name

    # KoskiEnv
    EVAKA_INTEGRATION_KOSKI_URL                    = var.evaka_integration_koski_enabled ? var.koski_integration_api_url : null
    EVAKA_INTEGRATION_KOSKI_SOURCE_SYSTEM          = var.evaka_integration_koski_enabled ? var.koski_source_system : null
    EVAKA_INTEGRATION_KOSKI_MUNICIPALITY_CALLER_ID = var.evaka_integration_koski_enabled ? var.evaka_integration_koski_municipality_caller_id : null

    # VardaEnv
    EVAKA_INTEGRATION_VARDA_URL           = var.varda_integration_api_url
    EVAKA_INTEGRATION_VARDA_SOURCE_SYSTEM = var.varda_integration_source_system
    EVAKA_INTEGRATION_VARDA_START_DATE    = var.evaka_integration_varda_start_date
    EVAKA_INTEGRATION_VARDA_END_DATE      = var.evaka_integration_varda_end_date

    # DvvModificationsEnv
    EVAKA_INTEGRATION_DVV_MODIFICATIONS_XROAD_CLIENT_ID = var.dvv_modifications_service_xroadclientid

    # VtjXroadClientEnv
    EVAKA_INTEGRATION_VTJ_XROAD_CLIENT_INSTANCE       = var.vtj_xroad_client_instance
    EVAKA_INTEGRATION_VTJ_XROAD_CLIENT_MEMBER_CLASS   = "MUN"
    EVAKA_INTEGRATION_VTJ_XROAD_CLIENT_MEMBER_CODE    = var.vtj_xroad_client_membercode
    EVAKA_INTEGRATION_VTJ_XROAD_CLIENT_SUBSYSTEM_CODE = var.vtj_xroad_client_subsystemcode

    # VtjXroadServiceEnv
    EVAKA_INTEGRATION_VTJ_XROAD_SERVICE_INSTANCE        = var.vtj_xroad_service_instance
    EVAKA_INTEGRATION_VTJ_XROAD_SERVICE_MEMBER_CLASS    = "GOV"
    EVAKA_INTEGRATION_VTJ_XROAD_SERVICE_MEMBER_CODE     = "0245437-2"
    EVAKA_INTEGRATION_VTJ_XROAD_SERVICE_SUBSYSTEM_CODE  = "VTJkysely"
    EVAKA_INTEGRATION_VTJ_XROAD_SERVICE_SERVICE_CODE    = "HenkilonTunnusKysely"
    EVAKA_INTEGRATION_VTJ_XROAD_SERVICE_SERVICE_VERSION = "v1"

    # SfiEnv
    EVAKA_INTEGRATION_SFI_ADDRESS                 = var.evaka_integration_sfi_enabled ? var.sfi_msg_service_address : null
    EVAKA_INTEGRATION_SFI_TRUST_STORE_LOCATION    = var.evaka_integration_sfi_enabled ? "file:///opt/java/openjdk/lib/security/cacerts" : null
    EVAKA_INTEGRATION_SFI_KEY_STORE_LOCATION      = var.evaka_integration_sfi_enabled ? "file:///home/ubuntu/s3/keystore.p12" : null
    EVAKA_INTEGRATION_SFI_SIGNING_KEY_ALIAS       = var.evaka_integration_sfi_enabled ? var.sfi_msg_keystore_alias : null
    EVAKA_INTEGRATION_SFI_AUTHORITY_IDENTIFIER    = var.evaka_integration_sfi_enabled ? var.sfi_msg_authority_identifier : null
    EVAKA_INTEGRATION_SFI_SERVICE_IDENTIFIER      = var.evaka_integration_sfi_enabled ? var.sfi_msg_service_identifier : null
    EVAKA_INTEGRATION_SFI_CERTIFICATE_COMMON_NAME = var.evaka_integration_sfi_enabled ? var.sfi_msg_certificate_cn : null
    EVAKA_INTEGRATION_SFI_PRINTING_ENABLED        = var.evaka_integration_sfi_enabled ? var.sfi_msg_enable_printing : null
    EVAKA_INTEGRATION_SFI_REST_ENABLED            = var.evaka_integration_sfi_enabled ? true : null
    EVAKA_INTEGRATION_SFI_REST_ADDRESS            = var.evaka_integration_sfi_enabled ? var.environment == "prod" ? "https://api.messages.suomi.fi" : "https://api.messages-qa.suomi.fi" : null
    EVAKA_INTEGRATION_SFI_REST_PASSWORD_SSM_NAME  = var.evaka_integration_sfi_enabled ? "${local.param_prefix}/service/sfi/password" : null

    # SfiPrintingEnv
    EVAKA_INTEGRATION_SFI_PRINTING_FORCE_PRINT_FOR_ELECTRONIC_USER = var.evaka_integration_sfi_enabled ? var.sfi_msg_force_print_for_electronic_user : null
    EVAKA_INTEGRATION_SFI_PRINTING_PROVIDER                        = var.evaka_integration_sfi_enabled ? var.sfi_msg_printing_provider : null

    # JamixEnv
    EVAKA_INTEGRATION_JAMIX_ENABLED = local.jamix_enabled
    EVAKA_INTEGRATION_JAMIX_URL     = local.jamix_enabled ? "https://fi.jamix.cloud/japi/pirnet/" : null

    # ScheduledJobsEnv
    EVAKA_JOB_CANCEL_OUTDATED_TRANSFER_APPLICATIONS_ENABLED      = var.evaka_job_cancel_outdated_transfer_applications_enabled
    EVAKA_JOB_DVV_UPDATE_ENABLED                                 = var.dvv_mutp_update_enabled
    EVAKA_JOB_DVV_UPDATE_CRON                                    = var.dvv_mutp_update_cron
    EVAKA_JOB_END_OF_DAY_ATTENDANCE_UPKEEP_ENABLED               = var.evaka_job_end_of_day_attendance_upkeep_enabled
    EVAKA_JOB_END_OF_DAY_STAFF_ATTENDANCE_UPKEEP_ENABLED         = var.evaka_job_end_of_day_staff_attendance_upkeep_enabled
    EVAKA_JOB_END_OF_DAY_RESERVATION_UPKEEP_ENABLED              = var.evaka_job_end_of_day_reservation_upkeep_enabled
    EVAKA_JOB_FREEZE_VOUCHER_VALUE_REPORTS_ENABLED               = var.evaka_job_freeze_voucher_value_reports_enabled
    EVAKA_JOB_FREEZE_VOUCHER_VALUE_REPORTS_CRON                  = var.evaka_job_freeze_voucher_value_reports_cron
    EVAKA_JOB_KOSKI_UPDATE_ENABLED                               = var.evaka_integration_koski_enabled
    EVAKA_JOB_REMOVE_OLD_ASYNC_JOBS_ENABLED                      = var.evaka_job_remove_old_async_jobs_enabled
    EVAKA_JOB_REMOVE_OLD_DAYCARE_DAILY_NOTES_ENABLED             = var.evaka_job_remove_old_daycare_daily_notes_enabled
    EVAKA_JOB_REMOVE_OLD_DRAFT_APPLICATIONS_ENABLED              = var.evaka_job_remove_old_draft_applications_enabled
    EVAKA_JOB_SEND_JAMIX_ORDERS_ENABLED                          = var.jamix_orders_enabled
    EVAKA_JOB_SYNC_JAMIX_DIETS_ENABLED                           = var.jamix_diets_enabled
    EVAKA_JOB_SEND_PENDING_DECISION_REMINDER_EMAILS_ENABLED      = var.send_pending_decision_reminder_emails_enabled
    EVAKA_JOB_VARDA_UPDATE_ENABLED                               = var.varda_integration_enabled
    EVAKA_JOB_VARDA_UPDATE_CRON                                  = var.evaka_job_varda_update_cron
    EVAKA_JOB_INACTIVE_PEOPLE_CLEANUP_ENABLED                    = var.evaka_job_inactive_people_cleanup_enabled
    EVAKA_JOB_INACTIVE_EMPLOYEES_ROLE_RESET_ENABLED              = var.evaka_job_inactive_employees_role_reset_enabled
    EVAKA_JOB_SEND_MISSING_RESERVATION_REMINDERS_ENABLED         = var.evaka_job_send_missing_reservation_reminders_enabled
    EVAKA_JOB_SEND_MISSING_RESERVATION_REMINDERS_CRON            = var.evaka_job_send_missing_reservation_reminders_cron
    EVAKA_JOB_SEND_MISSING_HOLIDAY_RESERVATION_REMINDERS_ENABLED = var.evaka_job_send_missing_holiday_reservation_reminders_enabled
    EVAKA_JOB_SEND_OUTDATED_INCOME_NOTIFICATIONS_ENABLED         = var.evaka_job_send_outdated_income_notifications_enabled

    EVAKA_JOB_END_ACTIVE_DAYCARE_ASSISTANCE_DECISIONS_ENABLED                = var.evaka_job_end_active_daycare_assistance_decisions_enabled
    EVAKA_JOB_END_ACTIVE_PRESCHOOL_ASSISTANCE_DECISIONS_ENABLED              = var.evaka_job_end_active_preschool_assistance_decisions_enabled
    EVAKA_JOB_END_ASSISTANCE_FACTORS_WHICH_BELONG_TO_PAST_PLACEMENTS_ENABLED = var.evaka_job_end_assistance_factors_which_belong_to_past_placements_enabled

    # tampereScheduledJobEnv
    TAMPERE_JOB_EXPORT_UNITS_ACL_ENABLED    = var.municipality == "tampere" ? var.tampere_job_export_units_acl_enabled : null
    TAMPERE_JOB_PLAN_BI_EXPORT_JOBS_ENABLED = var.municipality == "tampere" ? var.tampere_job_plan_bi_export_jobs_enabled : null
    TAMPERE_JOB_PLAN_BI_EXPORT_JOBS_CRON    = var.municipality == "tampere" ? var.tampere_job_plan_bi_export_jobs_cron : null

    # TampereProperties
    TAMPERE_SUMMERTIME_ABSENCE_FREE_MONTH = var.municipality == "tampere" ? var.tampere_summertime_absence_free_month : null
    TAMPERE_BI_EXPORT_PREFIX              = var.municipality == "tampere" ? "reporting" : null
  }
}

variable "service_version" {
  default = ""
}

variable "service_count" {
  description = "Desired count of service ECS tasks"
  type        = number
  default     = 0
}

variable "service_force_new_deployment" {
  type    = bool
  default = false
}

variable "service_task_cpu" {
  type    = number
  default = 256
}

variable "service_task_memory_mb" {
  type    = number
  default = 1024
}

variable "service_heap_size_mb" {
  description = "service heap size (Xmx) in megabytes"
  type        = number
  default     = 512
}

variable "finance_decision_min_date" {
  type    = string
  default = null
}

variable "koski_integration_api_url" {
  description = "Koski integration API URL"
  type        = string
  default     = ""
}

variable "koski_source_system" {
  description = "Koski integration source system ID"
  type        = string
  default     = "evakaespoo" # https://koski.opintopolku.fi/koski/api/koodisto/lahdejarjestelma/latest
}

variable "evaka_integration_koski_municipality_caller_id" {
  description = "Koski integration municipalicy caller id"
  type        = string
  default     = ""
}

variable "varda_integration_api_url" {
  description = "Varda integration API URL"
  type        = string
  default     = ""
}

variable "varda_integration_source_system" {
  type    = string
  default = "31" # https://virkailija.opintopolku.fi/varda/julkinen/koodistot/vardalahdejarjestelma
}

variable "evaka_integration_varda_start_date" {
  type    = string
  default = null
}

variable "evaka_integration_varda_end_date" {
  type    = string
  default = null
}

variable "vtj_enabled" {
  type    = bool
  default = false
}

variable "evaka_async_job_runner_disable_runner" {
  type    = bool
  default = null
}

variable "vtj_xroad_client_instance" {
  type = string
}

variable "vtj_xroad_client_membercode" {
  type = string
}

variable "vtj_xroad_client_subsystemcode" {
  type = string
}

variable "vtj_xroad_service_instance" {
  type = string
}

variable "fee_decision_days_in_advance" {
  type    = number
  default = null
}

variable "voucher_value_decision_days_in_advance" {
  type    = number
  default = null
}

variable "evaka_integration_koski_enabled" {
  type    = bool
  default = false
}

variable "evaka_integration_sfi_enabled" {
  type = bool
}

variable "tampere_summertime_absence_free_month" {
  type    = string
  default = null
}

variable "tampere_job_export_units_acl_enabled" {
  type    = bool
  default = false
}

variable "tampere_job_plan_bi_export_jobs_enabled" {
  type    = bool
  default = false
}

variable "tampere_job_plan_bi_export_jobs_cron" {
  description = "Spring-style cron-expression. Service default = daily @ 1 am"
  type        = string
  default     = null
}

variable "email_allowlist" {
  type    = string
  default = null
}

variable "email_name" {
  description = "Email sender name"
  type        = string
}

variable "email_subject_postfix" {
  description = "Email subject postfix (for test environments)"
  type        = string
  default     = null
}

variable "dvv_modifications_service_xroadclientid" {
  description = "Enable DVV mutp api xroad client id"
  type        = string
  default     = ""
}

variable "evaka_job_cancel_outdated_transfer_applications_enabled" {
  type    = bool
  default = false
}

variable "dvv_mutp_update_enabled" {
  type    = bool
  default = false
}

variable "dvv_mutp_update_cron" {
  description = "Spring-style cron-expression. Service default = daily @ 4 am"
  type        = string
  default     = null
}

variable "evaka_job_end_active_daycare_assistance_decisions_enabled" {
  type    = bool
  default = true
}

variable "evaka_job_end_active_preschool_assistance_decisions_enabled" {
  type    = bool
  default = true
}

variable "evaka_job_end_assistance_factors_which_belong_to_past_placements_enabled" {
  type    = bool
  default = true
}

variable "evaka_job_end_of_day_attendance_upkeep_enabled" {
  type    = bool
  default = true
}

variable "evaka_job_end_of_day_staff_attendance_upkeep_enabled" {
  type    = bool
  default = true
}

variable "evaka_job_end_of_day_reservation_upkeep_enabled" {
  type    = bool
  default = true
}

variable "evaka_job_freeze_voucher_value_reports_enabled" {
  type    = bool
  default = true
}

variable "evaka_job_freeze_voucher_value_reports_cron" {
  description = "Spring-style cron-expression. Service default = '0 0 0 25 * ?' -> monthly on 25th"
  type        = string
  default     = null
}

variable "evaka_job_remove_old_async_jobs_enabled" {
  type    = bool
  default = true
}

variable "evaka_job_remove_old_daycare_daily_notes_enabled" {
  type    = bool
  default = true
}

variable "evaka_job_remove_old_draft_applications_enabled" {
  type    = bool
  default = false
}

variable "send_pending_decision_reminder_emails_enabled" {
  type    = bool
  default = false
}

variable "varda_integration_enabled" {
  type    = bool
  default = false
}

variable "evaka_job_varda_update_cron" {
  description = "Spring-style cron-expression. Service default = '0 0 23 * * 1,2,3,4,5' -> mon - fri @ 23 pm"
  type        = string
  default     = null
}

variable "evaka_job_inactive_people_cleanup_enabled" {
  type    = bool
  default = false
}

variable "evaka_job_inactive_employees_role_reset_enabled" {
  type    = bool
  default = false
}

variable "evaka_job_send_missing_reservation_reminders_enabled" {
  type    = bool
  default = false
}

variable "evaka_job_send_missing_reservation_reminders_cron" {
  description = "Spring-style cron-expression. Service default = '0 0 18 * * 0' -> Sunday 18:00"
  type        = string
  default     = null
}

variable "evaka_job_send_missing_holiday_reservation_reminders_enabled" {
  type    = bool
  default = false
}

variable "evaka_job_send_outdated_income_notifications_enabled" {
  type    = bool
  default = false
}

variable "sfi_msg_service_address" {
  type = string
}

variable "sfi_msg_authority_identifier" {
  type = string
}

variable "sfi_msg_service_identifier" {
  type = string
}

variable "sfi_msg_certificate_cn" {
  type = string
}

variable "sfi_msg_enable_printing" {
  type = bool
}

variable "sfi_msg_force_print_for_electronic_user" {
  type    = bool
  default = false
}

variable "sfi_msg_printing_provider" {
  type = string
}

variable "sfi_msg_keystore_alias" {
  type    = string
  default = "signing-key"
}

variable "jamix_orders_enabled" {
  type    = bool
  default = false
}

variable "jamix_diets_enabled" {
  type    = bool
  default = false
}

variable "service_logging_levels" {
  type    = map(string)
  default = {}
}

locals {
  service_version = var.service_version != "" ? var.service_version : var.apps_version
  jamix_enabled   = var.jamix_orders_enabled || var.jamix_diets_enabled
}

output "service_version" {
  value = local.service_version
}
