#!/bin/bash

# SPDX-FileCopyrightText: 2017-2020 City of Espoo
#
# SPDX-License-Identifier: LGPL-2.1-or-later

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DATABASE" <<EOSQL
    CREATE ROLE evaka_it WITH SUPERUSER LOGIN PASSWORD 'evaka_it';
    CREATE DATABASE "evaka_tampere_local";
    CREATE DATABASE "evaka_tampere_it" OWNER evaka_it;
    CREATE DATABASE "evaka_vesilahti_local";
    CREATE DATABASE "evaka_vesilahti_it" OWNER evaka_it;
    CREATE DATABASE "evaka_hameenkyro_local";
    CREATE DATABASE "evaka_hameenkyro_it" OWNER evaka_it;
    CREATE DATABASE "evaka_ylojarvi_local";
    CREATE DATABASE "evaka_ylojarvi_it" OWNER evaka_it;
    CREATE DATABASE "evaka_pirkkala_local";
    CREATE DATABASE "evaka_pirkkala_it" OWNER evaka_it;
    CREATE DATABASE "evaka_nokia_local";
    CREATE DATABASE "evaka_nokia_it" OWNER evaka_it;
    CREATE DATABASE "evaka_kangasala_local";
    CREATE DATABASE "evaka_kangasala_it" OWNER evaka_it;

    -- Migration role to manage the migrations.
    CREATE ROLE "evaka_migration_role_local";
    GRANT ALL PRIVILEGES ON DATABASE "evaka_tampere_local" TO "evaka_migration_role_local" WITH GRANT OPTION;
    GRANT ALL PRIVILEGES ON DATABASE "evaka_tampere_it" TO "evaka_migration_role_local" WITH GRANT OPTION;
    GRANT ALL PRIVILEGES ON DATABASE "evaka_vesilahti_local" TO "evaka_migration_role_local" WITH GRANT OPTION;
    GRANT ALL PRIVILEGES ON DATABASE "evaka_vesilahti_it" TO "evaka_migration_role_local" WITH GRANT OPTION;
    GRANT ALL PRIVILEGES ON DATABASE "evaka_hameenkyro_local" TO "evaka_migration_role_local" WITH GRANT OPTION;
    GRANT ALL PRIVILEGES ON DATABASE "evaka_hameenkyro_it" TO "evaka_migration_role_local" WITH GRANT OPTION;
    GRANT ALL PRIVILEGES ON DATABASE "evaka_ylojarvi_local" TO "evaka_migration_role_local" WITH GRANT OPTION;
    GRANT ALL PRIVILEGES ON DATABASE "evaka_ylojarvi_it" TO "evaka_migration_role_local" WITH GRANT OPTION;
    GRANT ALL PRIVILEGES ON DATABASE "evaka_pirkkala_local" TO "evaka_migration_role_local" WITH GRANT OPTION;
    GRANT ALL PRIVILEGES ON DATABASE "evaka_pirkkala_it" TO "evaka_migration_role_local" WITH GRANT OPTION;
    GRANT ALL PRIVILEGES ON DATABASE "evaka_nokia_local" TO "evaka_migration_role_local" WITH GRANT OPTION;
    GRANT ALL PRIVILEGES ON DATABASE "evaka_nokia_it" TO "evaka_migration_role_local" WITH GRANT OPTION;
    GRANT ALL PRIVILEGES ON DATABASE "evaka_kangasala_local" TO "evaka_migration_role_local" WITH GRANT OPTION;
    GRANT ALL PRIVILEGES ON DATABASE "evaka_kangasala_it" TO "evaka_migration_role_local" WITH GRANT OPTION;

    -- (App) user-level role to connect to the database with least required privileges.
    CREATE ROLE "evaka_application_role_local";

    -- Migration login user
    CREATE ROLE "evaka_migration_local" WITH LOGIN PASSWORD 'flyway'
      IN ROLE "evaka_migration_role_local";

    -- App login user
    CREATE ROLE "evaka_application_local" WITH LOGIN PASSWORD 'app'
      IN ROLE "evaka_application_role_local";
EOSQL

# Tampere

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname evaka_tampere_local <<EOSQL
    GRANT ALL ON SCHEMA "public" TO "evaka_migration_role_local";

    -- DevDataInitializer creates a few helper functions
    GRANT CREATE ON SCHEMA "public" TO "evaka_application_local";
EOSQL

PGPASSWORD=flyway psql -v ON_ERROR_STOP=1 --username evaka_migration_local --dbname evaka_tampere_local <<EOSQL
    -- The reset_database function, used in e2e tests, truncates tables and resets sequences
    ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT TRUNCATE ON TABLES TO "evaka_application_local";
    ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT UPDATE ON SEQUENCES TO "evaka_application_local";
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname evaka_tampere_it <<EOSQL
    GRANT ALL ON SCHEMA "public" TO "evaka_migration_role_local";
EOSQL

# Vesilahti

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname evaka_vesilahti_local <<EOSQL
    GRANT ALL ON SCHEMA "public" TO "evaka_migration_role_local";

    -- DevDataInitializer creates a few helper functions
    GRANT CREATE ON SCHEMA "public" TO "evaka_application_local";
EOSQL

PGPASSWORD=flyway psql -v ON_ERROR_STOP=1 --username evaka_migration_local --dbname evaka_vesilahti_local <<EOSQL
    -- The reset_database function, used in e2e tests, truncates tables and resets sequences
    ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT TRUNCATE ON TABLES TO "evaka_application_local";
    ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT UPDATE ON SEQUENCES TO "evaka_application_local";
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname evaka_vesilahti_it <<EOSQL
    GRANT ALL ON SCHEMA "public" TO "evaka_migration_role_local";
EOSQL

# Hämeenkyrö

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname evaka_hameenkyro_local <<EOSQL
    GRANT ALL ON SCHEMA "public" TO "evaka_migration_role_local";

    -- DevDataInitializer creates a few helper functions
    GRANT CREATE ON SCHEMA "public" TO "evaka_application_local";
EOSQL

PGPASSWORD=flyway psql -v ON_ERROR_STOP=1 --username evaka_migration_local --dbname evaka_hameenkyro_local <<EOSQL
    -- The reset_database function, used in e2e tests, truncates tables and resets sequences
    ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT TRUNCATE ON TABLES TO "evaka_application_local";
    ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT UPDATE ON SEQUENCES TO "evaka_application_local";
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname evaka_hameenkyro_it <<EOSQL
    GRANT ALL ON SCHEMA "public" TO "evaka_migration_role_local";
EOSQL

# Ylöjärvi

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname evaka_ylojarvi_local <<EOSQL
    GRANT ALL ON SCHEMA "public" TO "evaka_migration_role_local";

    -- DevDataInitializer creates a few helper functions
    GRANT CREATE ON SCHEMA "public" TO "evaka_application_local";
EOSQL

PGPASSWORD=flyway psql -v ON_ERROR_STOP=1 --username evaka_migration_local --dbname evaka_ylojarvi_local <<EOSQL
    -- The reset_database function, used in e2e tests, truncates tables and resets sequences
    ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT TRUNCATE ON TABLES TO "evaka_application_local";
    ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT UPDATE ON SEQUENCES TO "evaka_application_local";
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname evaka_ylojarvi_it <<EOSQL
    GRANT ALL ON SCHEMA "public" TO "evaka_migration_role_local";
EOSQL

# Pirkkala

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname evaka_pirkkala_local <<EOSQL
    GRANT ALL ON SCHEMA "public" TO "evaka_migration_role_local";

    -- DevDataInitializer creates a few helper functions
    GRANT CREATE ON SCHEMA "public" TO "evaka_application_local";
EOSQL

PGPASSWORD=flyway psql -v ON_ERROR_STOP=1 --username evaka_migration_local --dbname evaka_pirkkala_local <<EOSQL
    -- The reset_database function, used in e2e tests, truncates tables and resets sequences
    ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT TRUNCATE ON TABLES TO "evaka_application_local";
    ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT UPDATE ON SEQUENCES TO "evaka_application_local";
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname evaka_pirkkala_it <<EOSQL
    GRANT ALL ON SCHEMA "public" TO "evaka_migration_role_local";
EOSQL

# Nokia

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname evaka_nokia_local <<EOSQL
    GRANT ALL ON SCHEMA "public" TO "evaka_migration_role_local";

    -- DevDataInitializer creates a few helper functions
    GRANT CREATE ON SCHEMA "public" TO "evaka_application_local";
EOSQL

PGPASSWORD=flyway psql -v ON_ERROR_STOP=1 --username evaka_migration_local --dbname evaka_nokia_local <<EOSQL
    -- The reset_database function, used in e2e tests, truncates tables and resets sequences
    ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT TRUNCATE ON TABLES TO "evaka_application_local";
    ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT UPDATE ON SEQUENCES TO "evaka_application_local";
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname evaka_nokia_it <<EOSQL
    GRANT ALL ON SCHEMA "public" TO "evaka_migration_role_local";
EOSQL

# Kangasala

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname evaka_kangasala_local <<EOSQL
    GRANT ALL ON SCHEMA "public" TO "evaka_migration_role_local";

    -- DevDataInitializer creates a few helper functions
    GRANT CREATE ON SCHEMA "public" TO "evaka_application_local";
EOSQL

PGPASSWORD=flyway psql -v ON_ERROR_STOP=1 --username evaka_migration_local --dbname evaka_kangasala_local <<EOSQL
    -- The reset_database function, used in e2e tests, truncates tables and resets sequences
    ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT TRUNCATE ON TABLES TO "evaka_application_local";
    ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT UPDATE ON SEQUENCES TO "evaka_application_local";
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname evaka_kangasala_it <<EOSQL
    GRANT ALL ON SCHEMA "public" TO "evaka_migration_role_local";
EOSQL
