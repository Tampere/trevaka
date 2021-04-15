# treVaka
treVaka – ERP for early childhood education in Tampere

This repository contains the code for customizing, configuring and extending the Espoo eVaka ERP for use in Tampere early education.

## Submodules
treVaka utilizes the [eVaka-repository](https://github.com/espoon-voltti/evaka) as its submodule. When cloning the repository use `--recurse-submodules` or manually initialize and update the submodule after cloning with `git submodule update --init`.


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

**NOTE:** The tool has no concept for "no license", so currently it will
always fail for the following files:

- Logos of the city of Tampere

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