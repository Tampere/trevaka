# SPDX-FileCopyrightText: 2021-2023 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later

FROM alpine:3.20.2
ARG CACHE_BUST
RUN apk add --no-cache bash coreutils aws-cli
ADD export_cloudwatch_to_s3.sh export_cloudwatch_to_s3.sh
CMD ["./export_cloudwatch_to_s3.sh"]
