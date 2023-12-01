# SPDX-FileCopyrightText: 2023 Tampere region
#
# SPDX-License-Identifier: LGPL-2.1-or-later

/^(espooLogoAlt[[:blank:]]*=[[:blank:]]*).*/{
s//\1Vesilahden kunnan logo/g
w /dev/stdout
}
/^(doAcknowledgeResourcesPrivacyPolicyLink[[:blank:]]*=[[:blank:]]*).*/{
s//\1https:\/\/www.vesilahti.fi\/kunta-ja-hallinto\/asiointi\/tietosuoja-ja-selosteet\//g
w /dev/stdout
}
/^(doAcknowledgeResourcesyDataProtectionLink[[:blank:]]*=[[:blank:]]*).*/{
s//\1https:\/\/www.vesilahti.fi\/kunta-ja-hallinto\/asiointi\/tietosuoja-ja-selosteet\//g
w /dev/stdout
}
/^(doGiveFeedbackLink[[:blank:]]*=[[:blank:]]*).*/{
s//\1https:\/\/www.vesilahti.fi\/kunta-ja-hallinto\/osallistu-ja-vaikuta\/palaute\//g
w /dev/stdout
}
