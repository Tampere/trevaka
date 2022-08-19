# SPDX-FileCopyrightText: 2021 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later
#
# Scavenged from evaka integration testing and e2e compose files

ARG PLAYWRIGHT_VERSION=v1.25.0

FROM mcr.microsoft.com/playwright:${PLAYWRIGHT_VERSION}-focal

RUN rm /etc/apt/sources.list.d/nodesource.list

RUN curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.38.0/install.sh | bash \
 && . "$HOME/.nvm/nvm.sh" \
 && nvm install 16.13

RUN curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | apt-key add - \
 && echo "deb https://dl.yarnpkg.com/debian/ stable main" | tee /etc/apt/sources.list.d/yarn.list \
 && apt-get update \
 && DEBIAN_FRONTEND=noninteractive apt-get --yes install --no-install-recommends libgbm1 yarn='1.22.*' sudo \
 && rm -rf $HOME/.cache/pip /var/lib/apt/lists/*



COPY ./bin/run-e2e-tests.sh /bin/

CMD ["/bin/run-e2e-tests.sh"]
