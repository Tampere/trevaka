# SPDX-FileCopyrightText: 2023-2026 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

"""
usage: uv run infra/update-sfi-idp-certs.py [dev] [test] [prod]

Downloads all Suomi.fi Tunnistus SAML IDP signing certificates from SAML metadata
and uploads each to S3 for every municipality in the given environment(s).

S3 path per municipality: s3://{project}-{env}-deployment/api-gw/sfi-idp-certificate-{year}.pem
where project is "trevaka" for tampere and "{municipality}-evaka" for others.

By default updates all environments. Pass environment name(s) to restrict:

  uv run infra/update-sfi-idp-certs.py test
  uv run infra/update-sfi-idp-certs.py prod
"""

import sys
import urllib.request
import xml.etree.ElementTree as ET

import boto3
import click
from cryptography import x509

SAML_NS = "{urn:oasis:names:tc:SAML:2.0:metadata}"
DS_NS = "{http://www.w3.org/2000/09/xmldsig#}"
ENTITY_DESCRIPTOR_TAG = f"{SAML_NS}EntityDescriptor"

PEM_HEADER = "-----BEGIN CERTIFICATE-----\n"
PEM_FOOTER = "\n-----END CERTIFICATE-----"

ALL_MUNICIPALITIES = [
    "tampere",
    "hameenkyro",
    "kangasala",
    "lempaala",
    "nokia",
    "orivesi",
    "pirkkala",
    "vesilahti",
    "ylojarvi",
]

TEST_METADATA = {
    "metadata_url": "https://static.apro.tunnistus.fi/static/metadata/idp-metadata.xml",
    "entity_id": "https://testi.apro.tunnistus.fi/idp1",
}

PROD_METADATA = {
    "metadata_url": "https://tunnistus.suomi.fi/static/metadata/idp-metadata.xml",
    "entity_id": "https://tunnistautuminen.suomi.fi/idp1",
}

# Each environment: AWS profile, municipalities in scope, and which IDP metadata to use
ENVIRONMENTS: dict[str, dict] = {
    "dev": {
        "profile": "trevaka-dev",
        "env_name": "dev",
        "municipalities": ["tampere"],
        **TEST_METADATA,
    },
    "test": {
        "profile": "trevaka-test",
        "env_name": "test",
        "municipalities": ALL_MUNICIPALITIES,
        **TEST_METADATA,
    },
    "prod": {
        "profile": "trevaka-prod",
        "env_name": "prod",
        "municipalities": ALL_MUNICIPALITIES,
        **PROD_METADATA,
    },
}


def project_name(municipality: str) -> str:
    return "trevaka" if municipality == "tampere" else f"{municipality}-evaka"


def fetch_metadata(url: str) -> ET.Element:
    response = urllib.request.urlopen(url).read()
    return ET.fromstring(response)


def find_entity(tree: ET.Element, entity_id: str) -> ET.Element:
    if tree.tag == ENTITY_DESCRIPTOR_TAG:
        if tree.get("entityID") == entity_id:
            return tree
        raise SystemExit(
            f"ERROR: metadata has a single entity ({tree.get('entityID')}), not {entity_id!r}"
        )

    for entity in tree.findall(ENTITY_DESCRIPTOR_TAG):
        if entity.get("entityID") == entity_id:
            return entity

    available = [e.get("entityID") for e in tree.findall(ENTITY_DESCRIPTOR_TAG)]
    raise SystemExit(
        f"ERROR: entity {entity_id!r} not found.\nAvailable entities:\n"
        + "\n".join(f"  {e}" for e in available)
    )


def extract_signing_certs(entity: ET.Element) -> list[tuple[str, int]]:
    """Return list of (pem_content, issuance_year) for all signing certs in the entity."""
    xpath = (
        f"./{SAML_NS}IDPSSODescriptor"
        f"/{SAML_NS}KeyDescriptor[@use='signing']"
        f"//{DS_NS}X509Certificate"
    )

    results = []
    for cert_element in entity.findall(xpath):
        cert_b64 = cert_element.text
        if not cert_b64:
            continue
        cert_b64 = cert_b64.strip()
        pem = f"{PEM_HEADER}{cert_b64}{PEM_FOOTER}"
        cert = x509.load_pem_x509_certificate(pem.encode("utf-8"))
        year = cert.not_valid_before_utc.year
        results.append((pem, year))

    return results


def write_cert(profile: str, bucket: str, s3_key: str, pem: str, dry_run: bool) -> None:
    s3_url = f"s3://{bucket}/{s3_key}"
    if dry_run:
        print(f"[dry-run] {profile}  →  {s3_url}")
        return

    try:
        session = boto3.Session(profile_name=profile)
        s3 = session.client("s3")
        s3.put_object(
            Bucket=bucket,
            Key=s3_key,
            Body=pem.encode("utf-8"),
            ContentType="application/x-pem-file",
        )
        print(f"OK    {profile}  →  {s3_url}")
    except Exception as e:
        print(f"FAIL  {profile}  →  {s3_url}: {e}", file=sys.stderr)


@click.command()
@click.argument(
    "environments",
    nargs=-1,
    type=click.Choice(list(ENVIRONMENTS.keys())),
)
@click.option(
    "--dry-run", is_flag=True, help="Print what would be done without writing to S3"
)
def main(environments: tuple[str, ...], dry_run: bool) -> None:
    """Download SFI IDP certs from SAML metadata and store in S3."""

    if not environments:
        environments = tuple(ENVIRONMENTS.keys())

    for env in environments:
        cfg = ENVIRONMENTS[env]

        print(f"[{env}] Fetching metadata from {cfg['metadata_url']} ...")
        tree = fetch_metadata(cfg["metadata_url"])
        entity = find_entity(tree, cfg["entity_id"])
        certs = extract_signing_certs(entity)

        if not certs:
            raise SystemExit(f"ERROR: No signing certificates found in {env} metadata")

        print(f"[{env}] Found {len(certs)} signing certificate(s):")
        for pem, year in certs:
            cert = x509.load_pem_x509_certificate(pem.encode("utf-8"))
            print(
                f"  {year}: notBefore={cert.not_valid_before_utc.date()}"
                f"  notAfter={cert.not_valid_after_utc.date()}"
            )
        print()

        for municipality in cfg["municipalities"]:
            project = project_name(municipality)
            for pem, year in certs:
                bucket = f"{project}-{cfg['env_name']}-deployment"
                s3_key = f"api-gw/sfi-idp-certificate-{year}.pem"
                write_cert(cfg["profile"], bucket, s3_key, pem, dry_run)


if __name__ == "__main__":
    main()
