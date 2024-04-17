// SPDX-FileCopyrightText: 2023-2024 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.bi

import fi.espoo.evaka.shared.db.Database
import fi.espoo.evaka.shared.db.QuerySql
import kotlin.reflect.KClass

object BiQueries {
    val getAreas =
        csvQuery<BiArea> {
            sql(
                """
            SELECT id, name, created, updated, area_code, sub_cost_center, short_name
            FROM care_area
            """,
            )
        }

    val getPersons =
        csvQuery<BiPerson> {
            sql(
                """
            SELECT id, social_security_number, first_name, last_name, email, aad_object_id, language, date_of_birth, created, updated, street_address, postal_code, post_office, nationalities, restricted_details_enabled, restricted_details_end_date, phone, updated_from_vtj, invoicing_street_address, invoicing_postal_code, invoicing_post_office, invoice_recipient_name, date_of_death, residence_code, force_manual_fee_decisions, backup_phone, last_login, oph_person_oid, vtj_guardians_queried, vtj_dependants_queried, ssn_adding_disabled, preferred_name, duplicate_of, enabled_email_types
            FROM person
            """,
            )
        }

    val getApplications =
        csvQuery<BiApplication> {
            sql(
                """
            select id, created, updated, sentdate, duedate, guardian_id, child_id, checkedbyadmin, hidefromguardian, transferapplication, other_guardian_id, additionaldaycareapplication, status, origin, duedate_set_manually_at, service_worker_note, type, allow_other_guardian_access, document::text
            FROM application
        """,
            )
        }

    val getApplicationForms =
        csvQuery<BiApplicationForm> {
            sql(
                """
            select id, application_id, created, revision, document::text, updated, latest
            FROM application_form
        """,
            )
        }

    val getAssistanceActions =
        csvQuery<BiAssistanceAction> {
            sql(
                """
            select id, created, updated, updated_by, child_id, start_date, end_date, other_action, measures::text
            FROM assistance_action
        """,
            )
        }

    val getAssistanceActionOptions =
        csvQuery<BiAssistanceActionOption> {
            sql(
                """
            select id, created, updated, value, name_fi, display_order, description_fi
            FROM assistance_action_option
        """,
            )
        }

    val getAssistanceActionOptionRefs =
        csvQuery<BiAssistanceActionOptionRef> {
            sql(
                """
            select action_id, option_id, created
            FROM assistance_action_option_ref
        """,
            )
        }

    val getAssistanceBasisOptions =
        csvQuery<BiAssistanceBasisOption> {
            sql(
                """
            select id, created, updated, value, name_fi, description_fi, display_order
            FROM assistance_basis_option
        """,
            )
        }

    val getAssistanceBasisOptionRefs =
        csvQuery<BiAssistanceBasisOptionRef> {
            sql(
                """
            select need_id, option_id, created
            FROM assistance_basis_option_ref
        """,
            )
        }

    val getAssistanceFactors =
        csvQuery<BiAssistanceFactor> {
            sql(
                """
            select id, created, updated, child_id, modified, modified_by, valid_during::text, capacity_factor
            FROM assistance_factor
        """,
            )
        }

    val getAssistanceNeeds =
        csvQuery<BiAssistanceNeed> {
            sql(
                """
            select id, created, updated, updated_by, child_id, start_date, end_date, capacity_factor
            FROM assistance_need
        """,
            )
        }

    val getAssistanceNeedDecisions =
        csvQuery<BiAssistanceNeedDecision> {
            sql(
                """
            select id, created, updated, decision_number, child_id, language, decision_made, sent_for_decision, selected_unit, decision_maker_employee_id, decision_maker_title, preparer_1_employee_id, preparer_1_title, preparer_2_employee_id, preparer_2_title, pedagogical_motivation, structural_motivation_opt_smaller_group, structural_motivation_opt_special_group, structural_motivation_opt_small_group, structural_motivation_opt_group_assistant, structural_motivation_opt_child_assistant, structural_motivation_opt_additional_staff, structural_motivation_description, care_motivation, service_opt_consultation_special_ed, service_opt_part_time_special_ed, service_opt_full_time_special_ed, service_opt_interpretation_and_assistance_services, service_opt_special_aides, services_motivation, expert_responsibilities, guardians_heard_on, view_of_guardians, other_representative_heard, other_representative_details, motivation_for_decision, preparer_1_phone_number, preparer_2_phone_number, decision_maker_has_opened, document_key, unread_guardian_ids, assistance_levels, validity_period::text, status, annulment_reason
            FROM assistance_need_decision
        """,
            )
        }

    val getAssistanceNeedDecisionGuardians =
        csvQuery<BiAssistanceNeedDecisionGuardian> {
            sql(
                """
            select id, created, assistance_need_decision_id, person_id, is_heard, details
            FROM assistance_need_decision_guardian
        """,
            )
        }

    val getAssistanceNeedVoucherCoefficients =
        csvQuery<BiAssistanceNeedVoucherCoefficient> {
            sql(
                """
            select id, created, updated, child_id, validity_period::text, coefficient
            FROM assistance_need_voucher_coefficient
        """,
            )
        }
    val getBackupCares =
        csvQuery<BiBackupCare> {
            sql(
                """
            select id, created, updated, child_id, unit_id, group_id, start_date, end_date
            FROM backup_care
        """,
            )
        }

    val getChildren =
        csvQuery<BiChild> {
            sql(
                """
            select id, allergies, diet, additionalinfo, medication, language_at_home, language_at_home_details
            FROM child
        """,
            )
        }

    val getCurriculumDocuments =
        csvQuery<BiCurriculumDocument> {
            sql(
                """
            select id, created, updated, child_id, basics::text, template_id, modified_at
            FROM curriculum_document
        """,
            )
        }

    val getCurriculumDocumentEvents =
        csvQuery<BiCurriculumDocumentEvent> {
            sql(
                """
            select id, created, updated, curriculum_document_id, event_type, created_by
            FROM curriculum_document_event
        """,
            )
        }

    val getCurriculumTemplates =
        csvQuery<BiCurriculumTemplate> {
            sql(
                """
            select id, created, updated, valid::text, type, language, name, content::text
            FROM curriculum_template
        """,
            )
        }

    val getDailyServiceTimeNotifications =
        csvQuery<BiDailyServiceTimeNotification> {
            sql(
                """
            select id, guardian_id, daily_service_time_id, date_from, has_deleted_reservations
            FROM daily_service_time_notification
        """,
            )
        }

    val getDaycares =
        csvQuery<BiDaycare> {
            sql(
                """
            select id, name, type, care_area_id, phone, url, created, updated, backup_location, language_emphasis_id, opening_date, closing_date, email, schedule, additional_info, cost_center, upload_to_varda, capacity, decision_daycare_name, decision_preschool_name, decision_handler, decision_handler_address, street_address, postal_code, post_office, mailing_po_box, location::text, mailing_street_address, mailing_postal_code, mailing_post_office, invoiced_by_municipality, provider_type, language, upload_to_koski, oph_unit_oid, oph_organizer_oid, ghost_unit, daycare_apply_period, preschool_apply_period, club_apply_period, finance_decision_handler, round_the_clock, enabled_pilot_features, upload_children_to_varda, business_id, iban, provider_id, operation_times, unit_manager_name, unit_manager_phone, unit_manager_email, dw_cost_center
            FROM daycare
        """,
            )
        }

    val getDaycareAssistances =
        csvQuery<BiDaycareAssistance> {
            sql(
                """
            select id, created, updated, child_id, modified, modified_by, valid_during::text, level
            FROM daycare_assistance
        """,
            )
        }

    val getDaycareCaretakers =
        csvQuery<BiDaycareCaretaker> {
            sql(
                """
            select id, created, updated, group_id, amount, start_date, end_date
            FROM daycare_caretaker
        """,
            )
        }

    val getDaycareGroups =
        csvQuery<BiDaycareGroup> {
            sql(
                """
            select id, daycare_id, name, start_date, end_date
            FROM daycare_group
        """,
            )
        }

    val getDaycareGroupPlacements =
        csvQuery<BiDaycareGroupPlacement> {
            sql(
                """
            select id, created, updated, daycare_placement_id, daycare_group_id, start_date, end_date
            FROM daycare_group_placement
        """,
            )
        }

    val getDecisions =
        csvQuery<BiDecision> {
            sql(
                """
            select id, number, created, updated, created_by, sent_date, unit_id, application_id, type, start_date, end_date, status, requested_start_date, resolved, resolved_by, planned, pending_decision_emails_sent_count, pending_decision_email_sent, document_key, other_guardian_document_key
            FROM decision
        """,
            )
        }

    val getEmployees =
        csvQuery<BiEmployee> {
            sql(
                """
            select id, active, first_name, last_name, email, created, updated, roles, external_id, last_login, employee_number, preferred_first_name, temporary_in_unit_id
            FROM employee
        """,
            )
        }

    val getEvakaUsers =
        csvQuery<BiEvakaUser> {
            sql(
                """
            select id, type, citizen_id, employee_id, mobile_device_id, name
            FROM evaka_user
        """,
            )
        }

    val getFeeAlterations =
        csvQuery<BiFeeAlteration> {
            sql(
                """
            select id, person_id, type, amount, is_absolute, valid_from, valid_to, notes, updated_at, updated_by
            FROM fee_alteration
        """,
            )
        }
    val getFeeDecisions =
        csvQuery<BiFeeDecision> {
            sql(
                """
            select id, created, updated, status, valid_during::text, decision_type, head_of_family_id, head_of_family_income::text, partner_id, partner_income::text, family_size, fee_thresholds::text, decision_number, document_key, approved_at, approved_by_id, decision_handler_id, sent_at, cancelled_at, total_fee, difference
            FROM fee_decision
        """,
            )
        }

    val getFeeDecisionChildren =
        csvQuery<BiFeeDecisionChild> {
            sql(
                """
            select id, created, updated, fee_decision_id, child_id, child_date_of_birth, sibling_discount, placement_unit_id, placement_type, service_need_fee_coefficient, service_need_description_fi, service_need_description_sv, base_fee, fee, fee_alterations, final_fee, service_need_missing, service_need_contract_days_per_month, child_income, service_need_option_id
            FROM fee_decision_child
        """,
            )
        }

    val getFeeThresholds =
        csvQuery<BiFeeThresholds> {
            sql(
                """
            select id, valid_during::text, min_income_threshold_2, min_income_threshold_3, min_income_threshold_4, min_income_threshold_5, min_income_threshold_6, income_multiplier_2, income_multiplier_3, income_multiplier_4, income_multiplier_5, income_multiplier_6, max_income_threshold_2, max_income_threshold_3, max_income_threshold_4, max_income_threshold_5, max_income_threshold_6, income_threshold_increase_6_plus, sibling_discount_2, sibling_discount_2_plus, max_fee, min_fee, created, updated, temporary_fee, temporary_fee_part_day, temporary_fee_sibling, temporary_fee_sibling_part_day
            FROM fee_thresholds
        """,
            )
        }

    val getFridgeChildren =
        csvQuery<BiFridgeChild> {
            sql(
                """
            select id, child_id, head_of_child, start_date, end_date, created, updated, conflict
            FROM fridge_child
        """,
            )
        }

    val getFridgePartners =
        csvQuery<BiFridgePartner> {
            sql(
                """
            select partnership_id, indx, person_id, start_date, end_date, created_at, updated, conflict, other_indx, create_source, created_by, modify_source, modified_at, modified_by, created_from_application
            FROM fridge_partner
        """,
            )
        }

    val getGuardians =
        csvQuery<BiGuardian> {
            sql(
                """
            select guardian_id, child_id, created
            FROM guardian
        """,
            )
        }

    val getGuardianBlockLists =
        csvQuery<BiGuardianBlocklist> {
            sql(
                """
            select guardian_id, child_id, created, updated
            FROM guardian_blocklist
        """,
            )
        }

    val getHolidayPeriods =
        csvQuery<BiHolidayPeriod> {
            sql(
                """
            select id, created, updated, period::text, reservation_deadline
            FROM holiday_period
        """,
            )
        }

    val getHolidayQuestionnaireAnswers =
        csvQuery<BiHolidayQuestionnaireAnswer> {
            sql(
                """
            select id, created, updated, modified_by, questionnaire_id, child_id, fixed_period::text
            FROM holiday_questionnaire_answer
        """,
            )
        }

    val getIncomes =
        csvQuery<BiIncome> {
            sql(
                """
            select id, person_id, data::text, valid_from, valid_to, notes, updated_at, effect, is_entrepreneur, works_at_echa, application_id, updated_by
            FROM income
        """,
            )
        }

    val getOtherAssistanceMeasures =
        csvQuery<BiOtherAssistanceMeasure> {
            sql(
                """
            select id, created, updated, child_id, modified, modified_by, valid_during::text, type
            FROM other_assistance_measure
        """,
            )
        }

    val getPlacements =
        csvQuery<BiPlacement> {
            sql(
                """
            select id, created, updated, type, child_id, unit_id, start_date, end_date, termination_requested_date, terminated_by, place_guarantee
            FROM placement
        """,
            )
        }

    val getPreschoolAssistances =
        csvQuery<BiPreschoolAssistance> {
            sql(
                """
            select id, created, updated, child_id, modified, modified_by, valid_during::text, level
            FROM preschool_assistance
        """,
            )
        }

    val getServiceNeeds =
        csvQuery<BiServiceNeed> {
            sql(
                """
            select id, created, updated, option_id, placement_id, start_date, end_date, confirmed_by, confirmed_at, shift_care
            FROM service_need
        """,
            )
        }

    val getServiceNeedOptions =
        csvQuery<BiServiceNeedOption> {
            sql(
                """
            select id, created, updated, name_fi, valid_placement_type, fee_coefficient, occupancy_coefficient, part_day, part_week, daycare_hours_per_week, default_option, fee_description_fi, fee_description_sv, voucher_value_description_fi, voucher_value_description_sv, display_order, name_sv, name_en, active, contract_days_per_month, occupancy_coefficient_under_3y, show_for_citizen, realized_occupancy_coefficient, realized_occupancy_coefficient_under_3y
            FROM service_need_option
        """,
            )
        }

    val getServiceNeedOptionVoucherValues =
        csvQuery<BiServiceNeedOptionVoucherValue> {
            sql(
                """
            select id, created, updated, service_need_option_id, validity::text, base_value, coefficient, value, base_value_under_3y, coefficient_under_3y, value_under_3y
            FROM service_need_option_voucher_value
        """,
            )
        }

    val getStaffAttendance =
        csvQuery<BiStaffAttendance> {
            sql(
                """
            select id, group_id, date, count, created, count_other, updated
            FROM staff_attendance
        """,
            )
        }

    val getStaffAttendanceExternals =
        csvQuery<BiStaffAttendanceExternal> {
            sql(
                """
            select id, created, updated, name, group_id, arrived, departed, occupancy_coefficient, departed_automatically
            FROM staff_attendance_external
        """,
            )
        }

    val getStaffAttendancePlans =
        csvQuery<BiStaffAttendancePlan> {
            sql(
                """
            select id, created, updated, employee_id, type, start_time, end_time, description
            FROM staff_attendance_plan
        """,
            )
        }

    val getStaffOccupancyCoefficients =
        csvQuery<BiStaffOccupancyCoefficient> {
            sql(
                """
            select id, created, updated, employee_id, daycare_id, coefficient
            FROM staff_occupancy_coefficient
        """,
            )
        }

    val getVoucherValueDecisions =
        csvQuery<BiVoucherValueDecision> {
            sql(
                """
            select id, status, valid_from, valid_to, decision_number, head_of_family_id, partner_id, head_of_family_income::text, partner_income::text, family_size, fee_thresholds::text, document_key, created, approved_by, approved_at, sent_at, cancelled_at, decision_handler, child_id, child_date_of_birth, base_co_payment, sibling_discount, placement_unit_id, placement_type, co_payment, fee_alterations::text, base_value, voucher_value, final_co_payment, service_need_fee_coefficient, service_need_voucher_value_coefficient, service_need_fee_description_fi, service_need_fee_description_sv, service_need_voucher_value_description_fi, service_need_voucher_value_description_sv, updated, assistance_need_coefficient, decision_type, annulled_at, validity_updated_at, child_income::text, difference, service_need_missing
            FROM voucher_value_decision
        """,
            )
        }

    // delta queries
    val getChildAttendanceDelta =
        csvQuery<BiChildAttendance> {
            sql(
                """
            select id, child_id, created, updated, unit_id, date, start_time, end_time
            FROM child_attendance
            WHERE updated >= (current_date AT TIME ZONE 'Europe/Helsinki' - interval '60 days')::date
        """,
            )
        }

    val getAbsencesDelta =
        csvQuery<BiAbsence> {
            sql(
                """
            SELECT id, child_id, date, absence_type, modified_at, modified_by, category, questionnaire_id 
            FROM absence
            WHERE modified_at >= (current_date AT TIME ZONE 'Europe/Helsinki' - interval '60 days')::date
            """,
            )
        }

    interface CsvQuery {
        operator fun <R> invoke(tx: Database.Read, useResults: (records: Sequence<String>) -> R): R
    }

    class StreamingCsvQuery<T : Any>(
        private val clazz: KClass<T>,
        private val query: (Database.Read) -> Database.Result<T>,
    ) : CsvQuery {
        override operator fun <R> invoke(
            tx: Database.Read,
            useResults: (records: Sequence<String>) -> R,
        ): R =
            query(tx).useSequence { rows ->
                useResults(toCsvRecords(::convertToCsv, clazz, rows))
            }
    }

    private const val QUERY_STREAM_CHUNK_SIZE = 10_000

    private inline fun <reified T : Any> csvQuery(
        crossinline f: QuerySql.Builder.() -> QuerySql,
    ): CsvQuery =
        StreamingCsvQuery(T::class) { tx ->
            tx.createQuery { f() }.setFetchSize(QUERY_STREAM_CHUNK_SIZE).mapTo<T>()
        }
}
