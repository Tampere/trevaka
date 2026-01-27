#!/usr/bin/env bash

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

# Parse command-line options
while getopts "e:m:p:h" opt; do
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
        h)
            echo "Usage: $0 [-e test|prod] [-m municipality] [-p port]"
            echo "  -e: Specify environment (test or prod)"
            echo "  -m: Specify municipality (hameenkyro, kangasala, lempaala, nokia, orivesi, pirkkala, tampere, vesilahti, ylojarvi)"
            echo "  -p: Specify local port number (default: 15432)"
            echo "  -h: Show this help message"
            echo ""
            echo "If -e or -m are not provided, you will be prompted to select them interactively."
            exit 0
            ;;
        \?)
            echo "Error: Invalid option -$OPTARG" >&2
            echo "Usage: $0 [-e test|prod] [-m municipality] [-p port]" >&2
            exit 1
            ;;
        :)
            echo "Error: Option -$OPTARG requires an argument." >&2
            exit 1
            ;;
    esac
done

# If environment not set via flag, prompt user
if [ -z "$ENVIRONMENT" ]; then
    ENVIRONMENT=$(select_environment)
fi

# If municipality not set via flag, prompt user
if [ -z "$MUNICIPALITY" ]; then
    MUNICIPALITY=$(select_municipality)
fi

# Construct AWS profile name based on environment
AWS_PROFILE="trevaka-${ENVIRONMENT}"

echo "Fetching EC2 instances from AWS (profile: ${AWS_PROFILE})..." >&2

# Execute AWS CLI command to get EC2 instances
AWS_OUTPUT=$(aws --profile="${AWS_PROFILE}" ec2 describe-instances --query "Reservations[*].Instances[*].{
    InstanceId: InstanceId,
    Municipality: Tags[?Key=='Municipality'].Value | [0]
}" --output json 2>&1)

# Check if AWS command was successful
if [ $? -ne 0 ]; then
    echo "Error: Failed to fetch EC2 instances from AWS." >&2
    echo "$AWS_OUTPUT" >&2
    exit 1
fi

# Parse JSON output to find the InstanceId for the matching municipality
INSTANCE_ID=$(echo "$AWS_OUTPUT" | jq -r --arg municipality "$MUNICIPALITY" '
    .[][] | select(.Municipality == $municipality) | .InstanceId
' | head -n 1)

# Check if an instance was found
if [ -z "$INSTANCE_ID" ]; then
    echo "Error: No EC2 instance found for municipality '$MUNICIPALITY' in environment '$ENVIRONMENT'." >&2
    exit 1
fi

echo "Fetching RDS instance information..." >&2

# Construct DB instance identifier
DB_INSTANCE_IDENTIFIER="${MUNICIPALITY}-evaka-${ENVIRONMENT}"

# Execute AWS CLI command to get RDS endpoint
RDS_HOST=$(aws --profile="${AWS_PROFILE}" rds describe-db-instances \
    --db-instance-identifier "${DB_INSTANCE_IDENTIFIER}" \
    --query "DBInstances[0].Endpoint.Address" \
    --output text 2>&1)

# Check if AWS command was successful
if [ $? -ne 0 ]; then
    echo "Error: Failed to fetch RDS instance information." >&2
    echo "$RDS_HOST" >&2
    exit 1
fi

# Check if RDS host was found
if [ -z "$RDS_HOST" ] || [ "$RDS_HOST" == "None" ]; then
    echo "Error: No RDS instance found with identifier '${DB_INSTANCE_IDENTIFIER}'." >&2
    exit 1
fi

echo "Fetching database credentials from AWS Secrets Manager..." >&2

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

# Parse JSON output to find the secret name for the matching municipality
SECRET_ID=$(echo "$SECRETS_LIST" | jq -r --arg municipality "$MUNICIPALITY" '
    .[] | select(.Municipality == $municipality) | .Name
' | head -n 1)

# Check if a secret was found
if [ -z "$SECRET_ID" ]; then
    echo "Error: No secret found for municipality '$MUNICIPALITY' in environment '$ENVIRONMENT'." >&2
    exit 1
fi

# Fetch the secret value
SECRET_VALUE=$(aws --profile="${AWS_PROFILE}" secretsmanager get-secret-value \
    --secret-id "$SECRET_ID" \
    --output json 2>&1)

# Check if AWS command was successful
if [ $? -ne 0 ]; then
    echo "Error: Failed to fetch secret value from AWS Secrets Manager." >&2
    echo "$SECRET_VALUE" >&2
    exit 1
fi

# Parse the SecretString to extract username and password
DB_USERNAME=$(echo "$SECRET_VALUE" | jq -r '.SecretString | fromjson | .username')
DB_PASSWORD=$(echo "$SECRET_VALUE" | jq -r '.SecretString | fromjson | .password')

# Check if credentials were successfully extracted
if [ -z "$DB_USERNAME" ] || [ -z "$DB_PASSWORD" ]; then
    echo "Error: Failed to extract database credentials from secret." >&2
    exit 1
fi

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
