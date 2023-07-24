<!--
SPDX-FileCopyrightText: 2021 City of Tampere

SPDX-License-Identifier: LGPL-2.1-or-later
-->

# treVaka
treVaka aka eVaka Tampere – ERP for early childhood education in Tampere

This repository contains the code for customizing, configuring and extending the Espoo eVaka ERP for use in Tampere early education.

## Checkout

treVaka utilizes the [eVaka](https://github.com/espoon-voltti/evaka) as its submodule. When cloning the repository use `--recurse-submodules` or manually initialize and update the submodule after cloning with `git submodule update --init`.

Frontend customizations [must be linked](frontend/README.md) under eVaka-repository:

    cd frontend
    ./link.sh

## Getting treVaka dev environment up and running

### Prerequisites - needed software and tools
See [eVaka README](evaka/compose/README.md#Dependencies)

### Starting treVaka dev environment
1. `cd compose`
2. `docker-compose -f docker-compose-dbs.yml up -d --build`
3. `pm2 start` (starts all apps)
4. Open browser: http://localhost:9099/

## Running treVaka frontend tests

### e2e ([Playwright](https://playwright.dev/))

1. Start treVaka dev environment
2. `cd frontend`
3. `yarn e2e-playwright`


### For WSL users:

Install a tool for running X Window System, eg. [GWSL](https://www.microsoft.com/en-us/p/gwsl/9nl6kd1h33v3#activetab=pivot:overviewtab).

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
./bin/add-license-headers.sh --lint-only

# See also:
./bin/add-license-headers.sh --help
```

### Automatically add licensing headers

To **attempt** automatically adding licensing headers to all source files, run:

```sh
./bin/add-license-headers.sh
```

**NOTE:** The script uses the [reuse CLI tool](https://git.fsfe.org/reuse/tool),
which has limited capability in recognizing file types but will give some
helpful output in those cases, like:

```sh
$ ./bin/add-license-headers.sh
usage: reuse addheader [-h] [--copyright COPYRIGHT] [--license LICENSE]
                       [--year YEAR]
                       [--style {applescript,aspx,bibtex,c,css,haskell,html,jinja,jsx,lisp,m4,ml,python,tex}]
                       [--template TEMPLATE] [--exclude-year] [--single-line]
                       [--multi-line] [--explicit-license]
                       [--skip-unrecognised]
                       path [path ...]
reuse addheader: error: 'frontend/packages/employee-frontend/src/components/voucher-value-decision/VoucherValueDecisionActionBar.tsx' does not have a recognised file extension, please use --style, --explicit-license or --skip-unrecognised
```