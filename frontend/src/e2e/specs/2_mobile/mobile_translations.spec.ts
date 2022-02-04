// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import { checkTampereTranslations } from '../../common/translations-check-utils'
import { translations } from 'lib-customizations/employeeMobile'

describe('Mobile translations', () => {
  test('fi', async () => {
    const errors = checkTampereTranslations(translations.fi)
    expect(errors).toEqual([])
  })
})
