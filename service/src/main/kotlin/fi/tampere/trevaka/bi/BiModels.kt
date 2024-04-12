// SPDX-FileCopyrightText: 2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.bi

import fi.espoo.evaka.absence.AbsenceCategory
import fi.espoo.evaka.absence.AbsenceType
import fi.espoo.evaka.application.ApplicationOrigin
import fi.espoo.evaka.application.ApplicationStatus
import fi.espoo.evaka.application.ApplicationType
import fi.espoo.evaka.assistance.DaycareAssistanceLevel
import fi.espoo.evaka.assistance.OtherAssistanceMeasureType
import fi.espoo.evaka.assistance.PreschoolAssistanceLevel
import fi.espoo.evaka.assistanceneed.decision.AssistanceNeedDecisionStatus
import fi.espoo.evaka.attendance.StaffAttendanceType
import fi.espoo.evaka.daycare.CareType
import fi.espoo.evaka.daycare.domain.Language
import fi.espoo.evaka.daycare.domain.ProviderType
import fi.espoo.evaka.decision.DecisionStatus
import fi.espoo.evaka.decision.DecisionType
import fi.espoo.evaka.invoicing.domain.FeeAlterationType
import fi.espoo.evaka.invoicing.domain.FeeDecisionDifference
import fi.espoo.evaka.invoicing.domain.FeeDecisionStatus
import fi.espoo.evaka.invoicing.domain.FeeDecisionType
import fi.espoo.evaka.invoicing.domain.IncomeEffect
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionDifference
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionStatus
import fi.espoo.evaka.invoicing.domain.VoucherValueDecisionType
import fi.espoo.evaka.pis.service.CreateSource
import fi.espoo.evaka.pis.service.ModifySource
import fi.espoo.evaka.placement.PlacementType
import fi.espoo.evaka.serviceneed.ShiftCareType
import fi.espoo.evaka.shared.auth.UserRole
import fi.espoo.evaka.shared.domain.DateRange
import fi.espoo.evaka.shared.domain.HelsinkiDateTime
import fi.espoo.evaka.shared.domain.TimeRange
import fi.espoo.evaka.shared.security.PilotFeature
import fi.espoo.evaka.user.EvakaUserType
import fi.espoo.evaka.vasu.CurriculumType
import fi.espoo.evaka.vasu.VasuDocumentEventType
import fi.espoo.evaka.vasu.VasuLanguage
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class BiApplication(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val sentdate: LocalDate?,
    val duedate: LocalDate?,
    val guardian_id: UUID,
    val child_id: UUID,
    val checkedbyadmin: Boolean,
    val hidefromguardian: Boolean,
    val transferapplication: Boolean,
    val other_guardian_id: UUID?,
    val additionaldaycareapplication: Boolean,
    val status: ApplicationStatus,
    val origin: ApplicationOrigin,
    val duedate_set_manually_at: HelsinkiDateTime?,
    val service_worker_note: String,
    val type: ApplicationType,
    val allow_other_guardian_access: Boolean,
    val document: String?,
    val form_modified: HelsinkiDateTime?
)

data class BiArea(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val name: String,
    val area_code: Int?,
    val sub_cost_center: String?,
    val short_name: String
)

data class BiPerson(
    val id: UUID,
    val social_security_number: String?,
    val first_name: String,
    val last_name: String,
    val email: String?,
    val aad_object_id: UUID?,
    val language: String?,
    val date_of_birth: LocalDate,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val street_address: String,
    val postal_code: String,
    val post_office: String,
    val nationalities: List<String>,
    val restricted_details_enabled: Boolean,
    val restricted_details_end_date: LocalDate?,
    val phone: String,
    val updated_from_vtj: HelsinkiDateTime?,
    val invoicing_street_address: String,
    val invoicing_postal_code: String,
    val invoicing_post_office: String,
    val invoice_recipient_name: String,
    val date_of_death: LocalDate?,
    val residence_code: String,
    val force_manual_fee_decisions: Boolean,
    val backup_phone: String,
    val last_login: HelsinkiDateTime?,
    val freetext_vec: String? = "", // BI should have no need for this
    val oph_person_oid: String,
    val vtj_guardians_queried: HelsinkiDateTime?,
    val vtj_dependants_queried: HelsinkiDateTime?,
    val ssn_adding_disabled: Boolean,
    val preferred_name: String,
    val duplicate_of: UUID?,
    val enabled_email_types: List<String>?,
    val keycloak_email: String?
)

data class BiAbsence(
    val id: UUID,
    val child_id: UUID,
    val date: LocalDate,
    val absence_type: AbsenceType,
    val modified_at: HelsinkiDateTime,
    val modified_by: UUID,
    val category: AbsenceCategory,
    val questionnaire_id: UUID?
)

data class BiApplicationForm(
    val id: UUID,
    val application_id: UUID,
    val created: HelsinkiDateTime,
    val revision: Long,
    val document: String,
    val updated: HelsinkiDateTime,
    val latest: Boolean
)

data class BiAssistanceAction(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val updated_by: UUID,
    val child_id: UUID,
    val start_date: LocalDate,
    val end_date: LocalDate,
    val other_action: String,
    val measures: List<String> // FIXME: no db enum type in kotlin, just string list?
)

data class BiAssistanceActionOption(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val value: String,
    val name_fi: String,
    val display_order: Int,
    val description_fi: String?
)

data class BiAssistanceActionOptionRef(
    val action_id: UUID,
    val option_id: UUID,
    val created: HelsinkiDateTime
)

data class BiAssistanceBasisOption(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val value: String,
    val name_fi: String,
    val display_order: Int,
    val description_fi: String?
)

data class BiAssistanceBasisOptionRef(
    val need_id: UUID,
    val option_id: UUID,
    val created: HelsinkiDateTime
)

data class BiAssistanceFactor(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val child_id: UUID,
    val modified: HelsinkiDateTime,
    val modified_by: UUID,
    val valid_during: DateRange,
    val capacity_factor: BigDecimal
)

data class BiAssistanceNeed(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val updated_by: UUID,
    val child_id: UUID,
    val start_date: LocalDate,
    val end_date: LocalDate,
    val capacity_factor: BigDecimal
)

data class BiAssistanceNeedDecision(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val decision_number: Long,
    val child_id: UUID,
    val language: String,
    val decision_made: LocalDate,
    val sent_for_decision: LocalDate,
    val selected_unit: UUID?,
    val decision_maker_employee_id: UUID?,
    val decision_maker_title: String?,
    val preparer_1_employee_id: UUID?,
    val preparer_1_title: String?,
    val preparer_2_employee_id: UUID?,
    val preparer_2_title: String?,
    val pedagogical_motivation: String?,
    val structural_motivation_opt_smaller_group: Boolean,
    val structural_motivation_opt_special_group: Boolean,
    val structural_motivation_opt_small_group: Boolean,
    val structural_motivation_opt_group_assistant: Boolean,
    val structural_motivation_opt_child_assistant: Boolean,
    val structural_motivation_opt_additional_staff: Boolean,
    val structural_motivation_description: String?,
    val care_motivation: String?,
    val service_opt_consultation_special_ed: Boolean,
    val service_opt_part_time_special_ed: Boolean,
    val service_opt_full_time_special_ed: Boolean,
    val service_opt_interpretation_and_assistance_services: Boolean,
    val service_opt_special_aides: Boolean,
    val services_motivation: String?,
    val expert_responsibilities: String?,
    val guardians_heard_on: LocalDate?,
    val view_of_guardians: String?,
    val other_representative_heard: Boolean,
    val other_representative_details: String?,
    val motivation_for_decision: String?,
    val preparer_1_phone_number: String?,
    val preparer_2_phone_number: String?,
    val decision_maker_has_opened: Boolean,
    val document_key: String?,
    val unread_guardian_ids: List<UUID>?,
    val assistance_levels: List<String>?,
    val validity_period: DateRange?,
    val status: AssistanceNeedDecisionStatus,
    val annulment_reason: String
)

data class BiAssistanceNeedDecisionGuardian(
    val id: UUID,
    val created: HelsinkiDateTime,
    val assistance_need_decision_id: UUID,
    val person_id: UUID,
    val is_heard: Boolean,
    val details: String?
)

data class BiAssistanceNeedVoucherCoefficient(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val child_id: UUID,
    val validity_period: DateRange,
    val coefficient: BigDecimal
)

data class BiBackupCare(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val child_id: UUID,
    val unit_id: UUID,
    val group_id: UUID?,
    val start_date: LocalDate,
    val end_date: LocalDate
)

data class BiChild(
    val id: UUID,
    val allergies: String,
    val diet: String,
    val additionalinfo: String,
    val medication: String,
    val language_at_home: String,
    val language_at_home_details: String
)

data class BiChildAttendance(
    val id: UUID,
    val child_id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val unit_id: UUID,
    val date: LocalDate,
    val start_time: LocalTime,
    val end_time: LocalTime
)

data class BiCurriculumDocument(
    val id: UUID,
    val child_id: UUID,
    val basics: String,
    val template_id: UUID,
    val modified_at: HelsinkiDateTime
)

data class BiCurriculumDocumentEvent(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val curriculum_document_id: UUID,
    val event_type: VasuDocumentEventType,
    val created_by: UUID,
)

data class BiCurriculumTemplate(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val valid: DateRange,
    val type: CurriculumType,
    val language: VasuLanguage,
    val name: String,
    val content: String
)

data class BiDailyServiceTimeNotification(
    val id: UUID,
    val guardian_id: UUID,
    val daily_service_time_id: UUID,
    val date_from: LocalDate,
    val has_deleted_reservations: Boolean
)

data class BiDaycare(
    val id: UUID,
    val name: String,
    val type: List<CareType>,
    val care_area_id: UUID,
    val phone: String?,
    val url: String?,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val backup_location: String?,
    val language_emphasis_id: UUID?,
    val opening_date: LocalDate?,
    val closing_date: LocalDate?,
    val email: String?,
    val schedule: String?,
    val additional_info: String?,
    val cost_center: String?,
    val upload_to_varda: Boolean,
    val capacity: Int,
    val decision_daycare_name: String,
    val decision_preschool_name: String,
    val decision_handler: String,
    val decision_handler_address: String,
    val street_address: String,
    val postal_code: String,
    val post_office: String,
    val mailing_po_box: String?,
    val location: String, // FIXME: what type??
    val mailing_street_address: String?,
    val mailing_postal_code: String?,
    val mailing_post_office: String?,
    val invoiced_by_municipality: Boolean,
    val provider_type: ProviderType,
    val language: Language,
    val upload_to_koski: Boolean,
    val oph_unit_oid: String?,
    val oph_organizer_oid: String?,
    val ghost_unit: Boolean?,
    val daycare_apply_period: DateRange?,
    val preschool_apply_period: DateRange?,
    val club_apply_period: DateRange?,
    val finance_decision_handler: UUID?,
    val round_the_clock: Boolean,
    val enabled_pilot_features: List<PilotFeature>,
    val upload_children_to_varda: Boolean,
    val business_id: String,
    val iban: String,
    val provider_id: String,
    val operation_times: List<TimeRange>,
    val operation_days: List<Int>,
    val unit_manager_name: String,
    val unit_manager_phone: String,
    val unit_manager_email: String,
    val dw_cost_center: String?,
    val daily_preschool_time: TimeRange?,
    val daily_preparatory_time: TimeRange?
)

data class BiDaycareAssistance(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val child_id: UUID,
    val modified: HelsinkiDateTime,
    val modified_by: UUID,
    val valid_during: DateRange,
    val level: DaycareAssistanceLevel
)

data class BiDaycareCaretaker(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val group_id: UUID,
    val amount: BigDecimal,
    val start_date: LocalDate,
    val end_date: LocalDate
)

data class BiDaycareGroup(
    val id: UUID,
    val daycare_id: UUID,
    val name: String,
    val start_date: LocalDate,
    val end_date: LocalDate?
)

data class BiDaycareGroupPlacement(
    val id: UUID,
    val created: HelsinkiDateTime?,
    val updated: HelsinkiDateTime?,
    val daycare_placement_id: UUID,
    val daycare_group_id: UUID,
    val start_date: LocalDate,
    val end_date: LocalDate
)

data class BiDecision(
    val id: UUID,
    val number: Int,
    val created: HelsinkiDateTime?,
    val updated: HelsinkiDateTime?,
    val created_by: UUID,
    val sent_date: LocalDate?,
    val unit_id: UUID,
    val application_id: UUID,
    val type: DecisionType,
    val start_date: LocalDate,
    val end_date: LocalDate,
    val status: DecisionStatus,
    val requested_start_date: LocalDate?,
    val resolved: HelsinkiDateTime?,
    val resolved_by: UUID?,
    val planned: Boolean,
    val pending_decision_emails_sent_count: Int?,
    val pending_decision_email_sent: HelsinkiDateTime?,
    val document_key: String?,
    val other_guardian_document_key: String?
)

data class BiEmployee(
    val id: UUID,
    val first_name: String,
    val last_name: String,
    val email: String?,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val roles: List<UserRole>,
    val external_id: String?,
    val last_login: HelsinkiDateTime,
    val employee_number: String?,
    val preferred_first_name: String?,
    val temporary_in_unit_id: UUID?,
    val active: Boolean
)

data class BiEvakaUser(
    val id: UUID,
    val type: EvakaUserType,
    val citizen_id: UUID?,
    val employee_id: UUID?,
    val mobile_device_id: UUID?,
    val name: String
)

data class BiFeeAlteration(
    val id: UUID,
    val person_id: UUID,
    val type: FeeAlterationType,
    val amount: Int,
    val is_absolute: Boolean,
    val valid_from: LocalDate,
    val valid_to: LocalDate?,
    val notes: String,
    val updated_at: HelsinkiDateTime,
    val updated_by: UUID
)

data class BiFeeDecision(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val status: FeeDecisionStatus,
    val valid_during: DateRange,
    val decision_type: FeeDecisionType,
    val head_of_family_id: UUID,
    val head_of_family_income: String?, // JSON
    val partner_id: UUID?,
    val partner_income: String?, // JSON
    val family_size: Int,
    val fee_thresholds: String, // JSON
    val decision_number: Long?,
    val document_key: String?,
    val approved_at: HelsinkiDateTime?,
    val approved_by_id: UUID?,
    val decision_handler_id: UUID?,
    val sent_at: HelsinkiDateTime?,
    val cancelled_at: HelsinkiDateTime?,
    val total_fee: Int,
    val difference: List<FeeDecisionDifference>
)

data class BiFeeDecisionChild(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val fee_decision_id: UUID,
    val child_id: UUID,
    val child_date_of_birth: LocalDate,
    val sibling_discount: Int,
    val placement_unit_id: UUID,
    val placement_type: PlacementType,
    val service_need_fee_coefficient: BigDecimal,
    val service_need_description_fi: String,
    val service_need_description_sv: String,
    val base_fee: Int,
    val fee: Int,
    val fee_alterations: String, // JSON
    val final_fee: Int,
    val service_need_missing: Boolean,
    val service_need_contract_days_per_month: Int?,
    val child_income: String?, // JSON
    val service_need_option_id: UUID?,
)

data class BiFeeThresholds(
    val id: UUID,
    val valid_during: DateRange,
    val min_income_threshold_2: Int,
    val min_income_threshold_3: Int,
    val min_income_threshold_4: Int,
    val min_income_threshold_5: Int,
    val min_income_threshold_6: Int,
    val income_multiplier_2: BigDecimal,
    val income_multiplier_3: BigDecimal,
    val income_multiplier_4: BigDecimal,
    val income_multiplier_5: BigDecimal,
    val income_multiplier_6: BigDecimal,
    val max_income_threshold_2: Int,
    val max_income_threshold_3: Int,
    val max_income_threshold_4: Int,
    val max_income_threshold_5: Int,
    val max_income_threshold_6: Int,
    val income_threshold_increase_6_plus: Int,
    val sibling_discount_2: BigDecimal,
    val sibling_discount_2_plus: BigDecimal,
    val max_fee: Int,
    val min_fee: Int,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val temporary_fee: Int,
    val temporary_fee_part_day: Int,
    val temporary_fee_sibling: Int,
    val temporary_fee_sibling_part_day: Int
)

data class BiFridgeChild(
    val id: UUID,
    val child_id: UUID,
    val head_of_child: UUID,
    val start_date: LocalDate,
    val end_date: LocalDate,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val conflict: Boolean
)

data class BiFridgePartner(
    val partnership_id: UUID,
    val indx: Int,
    val person_id: UUID,
    val start_date: LocalDate,
    val end_date: LocalDate?,
    val created_at: HelsinkiDateTime,
    val updated: HelsinkiDateTime?,
    val conflict: Boolean,
    val other_indx: Int,
    val create_source: CreateSource?,
    val created_by: UUID?,
    val modify_source: ModifySource?,
    val modified_at: HelsinkiDateTime?,
    val modified_by: UUID?,
    val created_from_application: UUID?
)

data class BiGuardian(val guardian_id: UUID, val child_id: UUID, val created: HelsinkiDateTime?)

data class BiGuardianBlocklist(
    val guardian_id: UUID,
    val child_id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime
)

data class BiHolidayPeriod(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val period: DateRange,
    val reservation_deadline: LocalDate
)

data class BiHolidayQuestionnaireAnswer(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val modified_by: UUID?,
    val questionnaire_id: UUID?,
    val child_id: UUID?,
    val fixed_period: DateRange
)

data class BiIncome(
    val id: UUID,
    val person_id: UUID,
    val data: String, // JSON
    val valid_from: LocalDate,
    val valid_to: LocalDate?,
    val notes: String,
    val updated_at: HelsinkiDateTime,
    val effect: IncomeEffect,
    val is_entrepreneur: Boolean,
    val works_at_echa: Boolean,
    val application_id: UUID,
    val updated_by: UUID
)

data class BiOtherAssistanceMeasure(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val child_id: UUID,
    val modified: HelsinkiDateTime,
    val modified_by: UUID,
    val valid_during: DateRange,
    val type: OtherAssistanceMeasureType
)

data class BiPlacement(
    val id: UUID,
    val created: HelsinkiDateTime?,
    val updated: HelsinkiDateTime?,
    val type: PlacementType,
    val child_id: UUID,
    val unit_id: UUID,
    val start_date: LocalDate,
    val end_date: LocalDate,
    val termination_requested_date: LocalDate?,
    val terminated_by: UUID?,
    val place_guarantee: Boolean
)

data class BiPreschoolAssistance(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val child_id: UUID,
    val modified: HelsinkiDateTime,
    val modified_by: HelsinkiDateTime,
    val valid_during: DateRange,
    val level: PreschoolAssistanceLevel
)

data class BiServiceNeed(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val option_id: UUID,
    val placement_id: UUID,
    val start_date: LocalDate,
    val end_date: LocalDate,
    val confirmed_by: UUID?,
    val confirmed_at: HelsinkiDateTime?,
    val shift_care: ShiftCareType
)

data class BiServiceNeedOption(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val name_fi: String,
    val valid_placement_type: PlacementType,
    val fee_coefficient: BigDecimal,
    val occupancy_coefficient: BigDecimal,
    val part_day: Boolean,
    val part_week: Boolean,
    val daycare_hours_per_week: Int,
    val default_option: Boolean,
    val fee_description_fi: String,
    val fee_description_sv: String,
    val voucher_value_description_fi: String,
    val voucher_value_description_sv: String,
    val display_order: Int?,
    val name_sv: String,
    val name_en: String,
    val active: Boolean,
    val contract_days_per_month: Int?,
    val occupancy_coefficient_under_3y: BigDecimal,
    val show_for_citizen: Boolean,
    val realized_occupancy_coefficient: BigDecimal,
    val realized_occupancy_coefficient_under_3y: BigDecimal,
    val daycare_hours_per_month: Int?
)

data class BiServiceNeedOptionVoucherValue(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val service_need_option_id: UUID,
    val validity: DateRange,
    val base_value: Int,
    val coefficient: BigDecimal,
    val value: Int,
    val base_value_under_3y: Int,
    val coefficient_under_3y: BigDecimal,
    val value_under_3y: Int
)

data class BiStaffAttendance(
    val id: UUID,
    val group_id: UUID,
    val date: LocalDate,
    val count: BigDecimal,
    val created: HelsinkiDateTime?,
    val count_other: BigDecimal,
    val updated: HelsinkiDateTime
)

data class BiStaffAttendanceExternal(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val name: String,
    val group_id: UUID,
    val arrived: HelsinkiDateTime,
    val departed: HelsinkiDateTime?,
    val occupancy_coefficient: BigDecimal,
    val departed_automatically: Boolean
)

data class BiStaffAttendancePlan(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime,
    val employee_id: UUID,
    val type: StaffAttendanceType,
    val start_time: HelsinkiDateTime,
    val end_time: HelsinkiDateTime,
    val description: String?
)

data class BiStaffOccupancyCoefficient(
    val id: UUID,
    val created: HelsinkiDateTime,
    val updated: HelsinkiDateTime?,
    val employee_id: UUID,
    val daycare_id: UUID,
    val coefficient: BigDecimal
)

data class BiVoucherValueDecision(
    val id: UUID,
    val status: VoucherValueDecisionStatus,
    val valid_from: LocalDate?,
    val valid_to: LocalDate?,
    val decision_number: Long?,
    val head_of_family_id: UUID,
    val partner_id: UUID?,
    val head_of_family_income: String?, // JSON
    val partner_income: String?, // JSON
    val family_size: Int,
    val fee_thresholds: String, // JSON
    val document_key: String?,
    val created: HelsinkiDateTime?,
    val approved_by: UUID?,
    val approved_at: HelsinkiDateTime?,
    val sent_at: HelsinkiDateTime?,
    val cancelled_at: HelsinkiDateTime?,
    val decision_handler: UUID?,
    val child_id: UUID,
    val child_date_of_birth: LocalDate,
    val base_co_payment: Int,
    val sibling_discount: Int,
    val placement_unit_id: UUID?,
    val placement_type: PlacementType?,
    val co_payment: Int,
    val fee_alterations: String, // JSON
    val base_value: Int,
    val voucher_value: Int,
    val final_co_payment: Int,
    val service_need_fee_coefficient: BigDecimal?,
    val service_need_voucher_value_coefficient: BigDecimal?,
    val service_need_fee_description_fi: String,
    val service_need_fee_description_sv: String,
    val service_need_voucher_value_description_fi: String?,
    val service_need_voucher_value_description_sv: String?,
    val updated: HelsinkiDateTime,
    val assistance_need_coefficient: BigDecimal,
    val decision_type: VoucherValueDecisionType,
    val annulled_at: HelsinkiDateTime?,
    val validity_updated_at: HelsinkiDateTime?,
    val child_income: String?, // JSON
    val difference: List<VoucherValueDecisionDifference>,
    val service_need_missing: Boolean
)
