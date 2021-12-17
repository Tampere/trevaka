# SPDX-FileCopyrightText: 2021 City of Tampere
#
# SPDX-License-Identifier: LGPL-2.1-or-later

/^(espooLogoAlt[[:blank:]]*=[[:blank:]]*).*/{
s//\1Tampereen kaupungin logo/g
w /dev/stdout
}
/^(doAcknowledgeResourcesPrivacyPolicyLink[[:blank:]]*=[[:blank:]]*).*/{
s//\1https:\/\/www.tampere.fi\/tampereen-kaupunki\/yhteystiedot-ja-asiointi\/verkkoasiointi\/tietosuoja\/tietosuojaselosteet.html/g
w /dev/stdout
}
/^(doAcknowledgeResourcesyDataProtectionLink[[:blank:]]*=[[:blank:]]*).*/{
s//\1https:\/\/www.tampere.fi\/tampereen-kaupunki\/yhteystiedot-ja-asiointi\/verkkoasiointi\/tietosuoja\/tietosuojaselosteet.html/g
w /dev/stdout
}
/^(doGiveFeedbackLink[[:blank:]]*=[[:blank:]]*).*/{
s//\1https:\/\/www.tampere.fi\/palaute.html.stx/g
w /dev/stdout
}
