<!--
SPDX-FileCopyrightText: 2021 City of Tampere

SPDX-License-Identifier: LGPL-2.1-or-later
-->

# treVaka
treVaka aka eVaka Tampere – ERP for early childhood education in Tampere

This repository contains the code for customizing, configuring and extending the Espoo eVaka ERP for use in Tampere early education.

## Checkout

treVaka utilizes the [eVaka](https://github.com/espoon-voltti/evaka) as its submodule. When cloning the repository use `--recurse-submodules` or manually initialize and update the submodule after cloning with `git submodule update --init`.

## Getting treVaka dev environment up and running

### Prerequisites - needed software and tools
See [eVaka README](evaka/compose/README.md#Dependencies)

Frontend customizations [must be linked](frontend/README.md) under eVaka-repository:

    cd frontend
    ./link.sh

It is also recommended to exclude folders under `frontend` so that files don't show twice after linking,
e.g. Intellij IDEA: select all folders under `frontend` -> Mark Directory as -> Excluded.

### Starting treVaka dev environment
1. `docker-compose up -d --build`
2. `pm2 start` or `mise start` (starts all apps)
3. Open browser: http://localhost:9099/

You can also start other supported configurations with `--env` command line parameter, e.g.:

```sh
pm2 stop all
pm2 start --env vesilahti
pm2 stop all
pm2 start # starts vesilahti configurations from previously used --env parameter
pm2 delete all
pm2 start # starts tampere configurations (default)
```

or with `mise`: `mise start --env vesilahti`.

## Running treVaka frontend tests

### e2e ([Playwright](https://playwright.dev/))

1. Start treVaka dev environment
2. `cd e2e`
3. `yarn e2e-playwright`


### For WSL users:

Install a tool for running X Window System, eg. [GWSL](https://www.microsoft.com/en-us/p/gwsl/9nl6kd1h33v3#activetab=pivot:overviewtab).

## Local test environment

It is also possible start test environment locally with just Docker Compose:

1. `docker compose --profile evaka up --build`
1. Open browser: http://localhost:9099/

You can also start other supported configurations with `EVAKA_CUSTOMIZATIONS` environment variable, e.g.:

It is also possible to set instance counts, e.g.: `docker compose --profile evaka up --scale apigw=4 --scale service=2`

```sh
EVAKA_CUSTOMIZATIONS=vesilahti docker compose --profile evaka up --build
```

## Connecting to Databases

The `infra/bin/trevaka-db.sh` script provides secure access to municipality databases in test and production environments through AWS Systems Manager (SSM) port forwarding.

### Prerequisites

- AWS CLI configured with appropriate profiles (`trevaka-test`, `trevaka-prod`)
- AWS SSM Session Manager plugin installed
- PostgreSQL client (`psql`) - only required for query mode
- `jq`, `lsof`, `nc` command-line tools

Before using the script, authenticate with AWS SSO:

```sh
aws sso login --sso-session tre-sso
```

### Port Forwarding Mode

Establish a persistent database connection for interactive use:

```sh
# Interactive mode - prompts for environment and municipality
./infra/bin/trevaka-db.sh

# Specify environment and municipality
./infra/bin/trevaka-db.sh -e test -m tampere

# Use custom local port
./infra/bin/trevaka-db.sh -e prod -m kangasala -p 5433
```

Once connected, use your preferred database client to connect to `localhost:15432` (or your specified port). The database password is automatically copied to your clipboard.

**Connection details:**
- Host: `localhost`
- Port: `15432` (default)
- Database: `service`
- Username: (displayed on connection)

### Query Mode

Execute read-only SQL queries across one or all municipalities:

```sh
# Query all municipalities in test environment
./infra/bin/trevaka-db.sh -e test -q "SELECT COUNT(*) FROM person;"

# Query specific municipality
./infra/bin/trevaka-db.sh -e prod -m tampere -q "SELECT version();"

# Execute query from file (must end with .sql)
./infra/bin/trevaka-db.sh -e test -f check-constraint.sql

# Increase query timeout (default: 5 seconds)
./infra/bin/trevaka-db.sh -e test -t 30 -q "SELECT COUNT(*) FROM application;"
```

**Query mode features:**
- Read-only transactions prevent accidental data modifications
- Configurable query timeout (default: 5 seconds)
- Automatic port cleanup between municipalities
- Continues on errors, shows summary at the end
- Press Ctrl+C to interrupt and clean up gracefully

### Options

```
-e <environment>  Specify environment (test or prod)
-m <municipality> Specify municipality
-p <port>         Specify local port number (default: 15432)
-q <query>        Execute SQL query (enables query mode)
-f <file>         Execute SQL query from file (enables query mode)
-t <timeout>      Query timeout in seconds (default: 5)
-h                Show help message
```

## License

treVaka is published under **LGPL-2.1-or-later** license. Please refer to
[LICENSE](LICENSE) for further details.

### Bulk-licensing

Bulk-licensing is applied to certain directories that will never contain
anything but binary-like files (e.g. certificates) with
[a DEP5 file](./.reuse/dep5) (see
[docs](https://reuse.software/faq/#bulk-license)).

### Check licensing compliance

This repository targets [REUSE](https://reuse.software/) compliance by utilizing
the [reuse CLI tool](https://git.fsfe.org/reuse/tool).

To check that the repository is compliant (e.g. before submitting a pull
request), run:

```sh
./add-license-headers.sh --lint-only

# See also:
./add-license-headers.sh --help
```

### Automatically add licensing headers

To **attempt** automatically adding licensing headers to all source files, run:

```sh
./add-license-headers.sh
```

**NOTE:** The script uses the [reuse CLI tool](https://git.fsfe.org/reuse/tool),
which has limited capability in recognizing file types but will give some
helpful output in those cases, like:

```sh
$ ./add-license-headers.sh
usage: reuse addheader [-h] [--copyright COPYRIGHT] [--license LICENSE]
                       [--year YEAR]
                       [--style {applescript,aspx,bibtex,c,css,haskell,html,jinja,jsx,lisp,m4,ml,python,tex}]
                       [--template TEMPLATE] [--exclude-year] [--single-line]
                       [--multi-line] [--explicit-license]
                       [--skip-unrecognised]
                       path [path ...]
reuse addheader: error: 'frontend/packages/employee-frontend/src/components/voucher-value-decision/VoucherValueDecisionActionBar.tsx' does not have a recognised file extension, please use --style, --explicit-license or --skip-unrecognised
```