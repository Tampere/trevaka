#!/usr/bin/env bash

# SPDX-FileCopyrightText: 2017-2026 City of Espoo
# SPDX-FileCopyrightText: 2026 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later

set -euo pipefail

# Generic function to prompt user for selection with arrow keys
select_option() {
    local prompt=$1
    shift
    local options=("$@")
    local selected=0
    local num_options=${#options[@]}

    # Function to draw menu
    draw_menu() {
        printf "%s\n" "$prompt" >&2
        for i in "${!options[@]}"; do
            if [ $i -eq $selected ]; then
                printf "→ %s\n" "${options[$i]}" >&2
            else
                printf "  %s\n" "${options[$i]}" >&2
            fi
        done
    }

    # Hide cursor
    tput civis >&2

    # Ensure cursor is shown on exit
    trap 'tput cnorm >&2' EXIT

    # Draw initial menu
    draw_menu

    while true; do
        # Read a single character
        read -rsn1 key </dev/tty

        # Handle arrow keys (they send multiple characters)
        if [ "$key" == $'\x1b' ]; then
            read -rsn2 key </dev/tty
            case $key in
                '[A') # Up arrow
                    ((selected--))
                    if [ $selected -lt 0 ]; then
                        selected=$((num_options - 1))
                    fi
                    # Redraw menu
                    for ((i=0; i<num_options+1; i++)); do
                        tput cuu1 >&2
                        tput el >&2
                    done
                    draw_menu
                    ;;
                '[B') # Down arrow
                    ((selected++))
                    if [ $selected -ge $num_options ]; then
                        selected=0
                    fi
                    # Redraw menu
                    for ((i=0; i<num_options+1; i++)); do
                        tput cuu1 >&2
                        tput el >&2
                    done
                    draw_menu
                    ;;
            esac
        elif [ "$key" == "" ]; then
            # Enter key pressed
            break
        fi
    done

    # Show cursor again
    tput cnorm >&2

    echo "${options[$selected]}"
}

# Function to prompt user for environment selection
select_environment() {
    select_option "Please select an environment (use ↑/↓ arrow keys and press Enter):" "test" "prod"
}

# Function to prompt user for municipality selection
select_municipality() {
    select_option "Please select a municipality (use ↑/↓ arrow keys and press Enter):" \
        "hameenkyro" "kangasala" "lempaala" "nokia" "orivesi" "pirkkala" "tampere" "vesilahti" "ylojarvi"
}

# Function to validate environment argument
validate_environment() {
    local env=$1
    if [[ "$env" != "test" && "$env" != "prod" ]]; then
        echo "Error: Invalid environment '$env'. Must be 'test' or 'prod'." >&2
        exit 1
    fi
}

# Function to validate municipality argument
validate_municipality() {
    local municipality=$1
    local valid_municipalities=("hameenkyro" "kangasala" "lempaala" "nokia" "orivesi" "pirkkala" "tampere" "vesilahti" "ylojarvi")

    for valid in "${valid_municipalities[@]}"; do
        if [[ "$municipality" == "$valid" ]]; then
            return 0
        fi
    done

    echo "Error: Invalid municipality '$municipality'." >&2
    echo "Valid municipalities are: ${valid_municipalities[*]}" >&2
    exit 1
}

# Main logic
ENVIRONMENT=""
MUNICIPALITY=""
LOCAL_PORT="15432"  # Default local port
QUERY_MODE=false
QUERY=""
QUERY_FILE=""
QUERY_TIMEOUT="5"  # Default timeout in seconds

# Parse command-line options
while getopts "e:m:p:q:f:t:h" opt; do
    case $opt in
        e)
            validate_environment "$OPTARG"
            ENVIRONMENT="$OPTARG"
            ;;
        m)
            validate_municipality "$OPTARG"
            MUNICIPALITY="$OPTARG"
            ;;
        p)
            # Validate that the port is a number
            if ! [[ "$OPTARG" =~ ^[0-9]+$ ]]; then
                echo "Error: Port number must be a positive integer." >&2
                exit 1
            fi
            LOCAL_PORT="$OPTARG"
            ;;
        q)
            QUERY_MODE=true
            QUERY="$OPTARG"
            ;;
        f)
            QUERY_MODE=true
            QUERY_FILE="$OPTARG"
            ;;
        t)
            if ! [[ "$OPTARG" =~ ^[0-9]+$ ]]; then
                echo "Error: Timeout must be a positive integer." >&2
                exit 1
            fi
            QUERY_TIMEOUT="$OPTARG"
            ;;
        h)
            echo "Usage: $0 [-e test|prod] [-m municipality] [-p port] [-q query | -f query_file] [-t timeout]"
            echo ""
            echo "Port forwarding mode:"
            echo "  -e: Specify environment (test or prod)"
            echo "  -m: Specify municipality (hameenkyro, kangasala, lempaala, nokia, orivesi, pirkkala, tampere, vesilahti, ylojarvi)"
            echo "  -p: Specify local port number (default: 15432)"
            echo ""
            echo "Query mode:"
            echo "  -q: Execute SQL query across municipalities"
            echo "  -f: Execute SQL query from file (must end with .sql)"
            echo "  -t: Query timeout in seconds (default: 5)"
            echo "  -m: Run query on specific municipality (if omitted, runs on all municipalities)"
            echo ""
            echo "  -h: Show this help message"
            echo ""
            echo "If -e or -m are not provided in port forwarding mode, you will be prompted to select them interactively."
            exit 0
            ;;
        \?)
            echo "Error: Invalid option -$OPTARG" >&2
            echo "Usage: $0 [-e test|prod] [-m municipality] [-p port] [-q query | -f query_file] [-t timeout]" >&2
            exit 1
            ;;
        :)
            echo "Error: Option -$OPTARG requires an argument." >&2
            exit 1
            ;;
    esac
done

# Validate query mode options
if [ "$QUERY_MODE" = true ]; then
    if [ -n "$QUERY" ] && [ -n "$QUERY_FILE" ]; then
        echo "Error: Cannot specify both -q and -f options." >&2
        exit 1
    fi

    if [ -z "$QUERY" ] && [ -z "$QUERY_FILE" ]; then
        echo "Error: Query mode requires either -q or -f option." >&2
        exit 1
    fi

    if [ -n "$QUERY_FILE" ]; then
        if [ ! -f "$QUERY_FILE" ]; then
            echo "Error: Query file '$QUERY_FILE' does not exist." >&2
            exit 1
        fi
        if [[ ! "$QUERY_FILE" =~ \.sql$ ]]; then
            echo "Error: Query file must end with .sql extension." >&2
            exit 1
        fi
        QUERY=$(cat "$QUERY_FILE")
    fi

    # Check for psql dependency
    if ! command -v psql &> /dev/null; then
        echo "Error: psql is not installed. Please install PostgreSQL client." >&2
        exit 1
    fi

    # Check if port is available
    if lsof -Pi :${LOCAL_PORT} -sTCP:LISTEN -t >/dev/null 2>&1; then
        echo "Error: Port ${LOCAL_PORT} is already in use." >&2
        exit 1
    fi
fi

# If environment not set via flag, prompt user
if [ -z "$ENVIRONMENT" ]; then
    ENVIRONMENT=$(select_environment)
fi

# If municipality not set via flag, prompt user (only in port-forward mode)
if [ -z "$MUNICIPALITY" ] && [ "$QUERY_MODE" = false ]; then
    MUNICIPALITY=$(select_municipality)
fi

# Construct AWS profile name based on environment
AWS_PROFILE="trevaka-${ENVIRONMENT}"

# Fetch AWS resources upfront (they return data for all municipalities)
echo "Fetching EC2 instances from AWS (profile: ${AWS_PROFILE})..." >&2

# Execute AWS CLI command to get EC2 instances
EC2_INSTANCES=$(aws --profile="${AWS_PROFILE}" ec2 describe-instances --query "Reservations[*].Instances[*].{
    InstanceId: InstanceId,
    Municipality: Tags[?Key=='Municipality'].Value | [0]
}" --output json 2>&1)

# Check if AWS command was successful
if [ $? -ne 0 ]; then
    echo "Error: Failed to fetch EC2 instances from AWS." >&2
    echo "$EC2_INSTANCES" >&2
    exit 1
fi

echo "Fetching secrets from AWS Secrets Manager..." >&2

# List secrets and find the one matching the municipality
SECRETS_LIST=$(aws secretsmanager --profile="${AWS_PROFILE}" list-secrets \
    --query "SecretList[*].{Name:Name, Municipality:Tags[?Key=='Municipality'].Value | [0]}" \
    --output json 2>&1)

# Check if AWS command was successful
if [ $? -ne 0 ]; then
    echo "Error: Failed to list secrets from AWS Secrets Manager." >&2
    echo "$SECRETS_LIST" >&2
    exit 1
fi

# Helper function to get instance ID for a municipality
get_instance_id() {
    local municipality=$1
    echo "$EC2_INSTANCES" | jq -r --arg municipality "$municipality" '
        .[][] | select(.Municipality == $municipality) | .InstanceId
    ' | head -n 1
}

# Helper function to get secret ID for a municipality
get_secret_id() {
    local municipality=$1
    echo "$SECRETS_LIST" | jq -r --arg municipality "$municipality" '
        .[] | select(.Municipality == $municipality) | .Name
    ' | head -n 1
}

# Helper function to get RDS host for a municipality
get_rds_host() {
    local municipality=$1
    local db_instance_identifier="${municipality}-evaka-${ENVIRONMENT}"

    aws --profile="${AWS_PROFILE}" rds describe-db-instances \
        --db-instance-identifier "${db_instance_identifier}" \
        --query "DBInstances[0].Endpoint.Address" \
        --output text 2>&1
}

# Helper function to get database credentials for a municipality
get_db_credentials() {
    local secret_id=$1

    local secret_value=$(aws --profile="${AWS_PROFILE}" secretsmanager get-secret-value \
        --secret-id "$secret_id" \
        --output json 2>&1)

    if [ $? -ne 0 ]; then
        echo "Error: Failed to fetch secret value from AWS Secrets Manager." >&2
        return 1
    fi

    local username=$(echo "$secret_value" | jq -r '.SecretString | fromjson | .username')
    local password=$(echo "$secret_value" | jq -r '.SecretString | fromjson | .password')

    echo "${username}:${password}"
}

# Unified cleanup function to kill SSM process and wait for port release
cleanup_port() {
    local pid=${1:-}
    local max_wait=${2:-15}

    # Kill the specific SSM process if provided
    if [ -n "$pid" ] && kill -0 "$pid" 2>/dev/null; then
        kill "$pid" 2>/dev/null || true
        sleep 0.5
        kill -9 "$pid" 2>/dev/null || true
    fi

    # Kill any session-manager-plugin processes for this port
    pkill -9 -f "session-manager-plugin.*${LOCAL_PORT}" 2>/dev/null || true

    # Wait for port to be released
    local wait_count=0
    while lsof -Pi :${LOCAL_PORT} -sTCP:LISTEN -t >/dev/null 2>&1 && [ $wait_count -lt $max_wait ]; do
        # After 5 seconds, aggressively kill everything on the port
        if [ $wait_count -eq 5 ]; then
            lsof -ti:${LOCAL_PORT} | xargs -r kill -9 2>/dev/null || true
            pkill -9 -f "session-manager-plugin" 2>/dev/null || true
        fi
        sleep 1
        ((wait_count++))
    done

    sleep 1
}

# Flag to track if we're terminating
TERMINATING=false

# Cleanup function for graceful shutdown on script exit
cleanup_on_exit() {
    cleanup_port "" 5
}

# Signal handler for SIGINT/SIGTERM
handle_signal() {
    TERMINATING=true
    echo "" >&2
    echo "Interrupted. Cleaning up..." >&2
    cleanup_port "" 5
    exit 130
}

# Set up trap handlers for graceful shutdown
trap cleanup_on_exit EXIT
trap handle_signal SIGINT SIGTERM

# Function to wait for port to become available
wait_for_port() {
    local port=$1
    local max_attempts=10
    local attempt=0

    while [ $attempt -lt $max_attempts ]; do
        if nc -z localhost "$port" 2>/dev/null; then
            return 0
        fi
        sleep 2
        ((attempt++))
    done

    return 1
}

# Function to execute query on a municipality
execute_query_on_municipality() {
    local municipality=$1
    local query=$2
    local timeout=$3

    echo "" >&2
    echo "==================== ${municipality^^} ====================" >&2

    # Get resources for this municipality
    local instance_id=$(get_instance_id "$municipality")
    if [ -z "$instance_id" ]; then
        echo "Error: No EC2 instance found for municipality '$municipality'" >&2
        return 1
    fi

    local rds_host=$(get_rds_host "$municipality")
    if [ $? -ne 0 ] || [ -z "$rds_host" ] || [ "$rds_host" == "None" ]; then
        echo "Error: No RDS instance found for municipality '$municipality'" >&2
        return 1
    fi

    local secret_id=$(get_secret_id "$municipality")
    if [ -z "$secret_id" ]; then
        echo "Error: No secret found for municipality '$municipality'" >&2
        return 1
    fi

    local credentials=$(get_db_credentials "$secret_id")
    if [ $? -ne 0 ] || [ -z "$credentials" ]; then
        echo "Error: Failed to retrieve credentials for municipality '$municipality'" >&2
        return 1
    fi

    local db_username="${credentials%%:*}"
    local db_password="${credentials#*:}"

    # Ensure port is free before starting new SSM session
    if lsof -Pi :${LOCAL_PORT} -sTCP:LISTEN -t >/dev/null 2>&1; then
        echo "Warning: Port $LOCAL_PORT still in use, cleaning up..." >&2
        cleanup_port "" 10
        if lsof -Pi :${LOCAL_PORT} -sTCP:LISTEN -t >/dev/null 2>&1; then
            echo "Error: Port $LOCAL_PORT is still in use after cleanup" >&2
            return 1
        fi
    fi

    # Start SSM port forwarding in background
    aws --profile="${AWS_PROFILE}" ssm start-session \
        --target "$instance_id" \
        --document-name AWS-StartPortForwardingSessionToRemoteHost \
        --parameters "{\"host\": [\"$rds_host\"], \"portNumber\": [\"5432\"], \"localPortNumber\": [\"$LOCAL_PORT\"]}" \
        >/dev/null 2>&1 &

    local ssm_pid=$!

    # Wait for port to become available
    if ! wait_for_port "$LOCAL_PORT"; then
        echo "Error: Timeout waiting for port $LOCAL_PORT to become available" >&2
        cleanup_port "$ssm_pid" 10
        return 1
    fi

    # Execute query with read-only session and timeout
    # Use -q flag to suppress SET command output, redirect stderr to capture errors
    PGPASSWORD="$db_password" psql -q -h localhost -p "$LOCAL_PORT" -U "$db_username" -d service \
        -c "SET SESSION CHARACTERISTICS AS TRANSACTION READ ONLY; SET statement_timeout = '${timeout}s';" \
        -c "$query" 2>&1

    local query_exit_code=$?

    # Cleanup SSM session and wait for port to be released
    cleanup_port "$ssm_pid"

    if [ $query_exit_code -eq 0 ]; then
        return 0
    else
        echo "✗ Query failed" >&2
        return 1
    fi
}

# Query mode execution
if [ "$QUERY_MODE" = true ]; then
    # Determine target municipalities
    if [ -n "$MUNICIPALITY" ]; then
        MUNICIPALITIES=("$MUNICIPALITY")
    else
        MUNICIPALITIES=("hameenkyro" "kangasala" "lempaala" "nokia" "orivesi" "pirkkala" "tampere" "vesilahti" "ylojarvi")
    fi

    echo "" >&2
    echo "Executing query on ${#MUNICIPALITIES[@]} municipality/municipalities..." >&2

    # Track results
    declare -a SUCCESSFUL=()
    declare -a FAILED=()

    # Execute query on each municipality
    for municipality in "${MUNICIPALITIES[@]}"; do
        # Check if we received a termination signal
        if [ "$TERMINATING" = true ]; then
            break
        fi

        if execute_query_on_municipality "$municipality" "$QUERY" "$QUERY_TIMEOUT"; then
            SUCCESSFUL+=("$municipality")
        else
            FAILED+=("$municipality")
        fi
    done

    # Display summary
    echo "" >&2
    echo "==================== SUMMARY ====================" >&2
    echo "Successful: ${#SUCCESSFUL[@]}/${#MUNICIPALITIES[@]}" >&2
    if [ ${#SUCCESSFUL[@]} -gt 0 ]; then
        echo "  ✓ ${SUCCESSFUL[*]}" >&2
    fi
    if [ ${#FAILED[@]} -gt 0 ]; then
        echo "  ✗ ${FAILED[*]}" >&2
    fi
    echo "=================================================" >&2

    exit 0
fi

# Port forwarding mode (original functionality)
# Parse JSON output to find the InstanceId for the matching municipality
INSTANCE_ID=$(get_instance_id "$MUNICIPALITY")

# Check if an instance was found
if [ -z "$INSTANCE_ID" ]; then
    echo "Error: No EC2 instance found for municipality '$MUNICIPALITY' in environment '$ENVIRONMENT'." >&2
    exit 1
fi

echo "Fetching RDS instance information..." >&2

# Get RDS host for the municipality
RDS_HOST=$(get_rds_host "$MUNICIPALITY")

# Check if AWS command was successful
if [ $? -ne 0 ]; then
    echo "Error: Failed to fetch RDS instance information." >&2
    echo "$RDS_HOST" >&2
    exit 1
fi

# Check if RDS host was found
if [ -z "$RDS_HOST" ] || [ "$RDS_HOST" == "None" ]; then
    echo "Error: No RDS instance found for municipality '${MUNICIPALITY}'." >&2
    exit 1
fi

echo "Fetching database credentials from AWS Secrets Manager..." >&2

# Get secret ID for the municipality
SECRET_ID=$(get_secret_id "$MUNICIPALITY")

# Check if a secret was found
if [ -z "$SECRET_ID" ]; then
    echo "Error: No secret found for municipality '$MUNICIPALITY' in environment '$ENVIRONMENT'." >&2
    exit 1
fi

# Get credentials
CREDENTIALS=$(get_db_credentials "$SECRET_ID")

# Check if credentials were successfully extracted
if [ $? -ne 0 ] || [ -z "$CREDENTIALS" ]; then
    echo "Error: Failed to extract database credentials from secret." >&2
    exit 1
fi

DB_USERNAME="${CREDENTIALS%%:*}"
DB_PASSWORD="${CREDENTIALS#*:}"

# Copy password to clipboard
if command -v pbcopy &> /dev/null; then
    echo -n "$DB_PASSWORD" | pbcopy
    CLIPBOARD_STATUS="✓ Password copied to clipboard"
elif command -v xclip &> /dev/null; then
    echo -n "$DB_PASSWORD" | xclip -selection clipboard
    CLIPBOARD_STATUS="✓ Password copied to clipboard"
elif command -v xsel &> /dev/null; then
    echo -n "$DB_PASSWORD" | xsel --clipboard --input
    CLIPBOARD_STATUS="✓ Password copied to clipboard"
else
    CLIPBOARD_STATUS="⚠ Clipboard utility not found (password not copied)"
fi

# Display gathered information
echo "============================================" >&2
echo "Connection Details:" >&2
echo "============================================" >&2
echo "Environment:      $ENVIRONMENT" >&2
echo "Municipality:     $MUNICIPALITY" >&2
echo "Instance ID:      $INSTANCE_ID" >&2
echo "RDS Host:         $RDS_HOST" >&2
echo "DB Username:      $DB_USERNAME" >&2
echo "DB Password:      ********** (redacted)" >&2
echo "Local Port:       $LOCAL_PORT" >&2
echo "" >&2
echo "$CLIPBOARD_STATUS" >&2
echo "============================================" >&2
echo "" >&2
echo "Starting SSM port forwarding session..." >&2
echo "" >&2

# Start SSM port forwarding session
aws --profile="${AWS_PROFILE}" ssm start-session \
    --target "$INSTANCE_ID" \
    --document-name AWS-StartPortForwardingSessionToRemoteHost \
    --parameters "{\"host\": [\"$RDS_HOST\"], \"portNumber\": [\"5432\"], \"localPortNumber\": [\"$LOCAL_PORT\"]}"
