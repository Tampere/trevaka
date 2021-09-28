CREATE OR REPLACE FUNCTION reset_tampere_database_for_e2e_tests() RETURNS void AS $$
BEGIN
EXECUTE (
    SELECT 'TRUNCATE TABLE ' || string_agg(quote_ident(table_name), ', ') || ' CASCADE'
    FROM information_schema.tables
    WHERE table_schema = 'public'
      AND table_type = 'BASE TABLE'
      AND table_name not in ('flyway_schema_history', 'service_need_option', 'club_term', 'preschool_term')
);
EXECUTE (
    SELECT 'SELECT ' || string_agg(format('setval(%L, %L, false)', sequence_name, start_value), ', ')
    FROM information_schema.sequences
    WHERE sequence_schema = 'public'
);
END $$ LANGUAGE plpgsql;