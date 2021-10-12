// SPDX-FileCopyrightText: 2021 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import { translations } from 'lib-customizations/citizen'
import React from 'react'
import { isArray, toArray, isObject } from 'lodash'

const checkTampereTranslation = (value: string, errors: string[]) => {
    if (value.toLowerCase().includes('espoo')) {
        console.error(value)
        errors.push(value);
    }
}

const checkTampereTranslationInner = (object: any, errors: string[]) => {
    if (React.isValidElement(object)) {
        checkTampereTranslation(JSON.stringify(object), errors);
    } else if (isObject(object)) {
        Object.keys(object).forEach(key => {
            checkTampereTranslationInner(object[key], errors)
        });
    } else if (isArray(object)) {
        let valAsArray = toArray(object)
        for (let val in valAsArray) {
            checkTampereTranslationInner(val, errors)
        }
    } else {
        checkTampereTranslation(object, errors)
    }
}

const checkTampereTranslations = (translationsFi: { [key: string]: any }): string[] => {
    let errors: string[] = [];
    for (const key of Object.keys(translationsFi)) {
        checkTampereTranslationInner(translationsFi[key], errors)
    };
    return errors;
}

describe('Citizen translations', () => {
  test('fi', async () => {
    const errors = checkTampereTranslations(translations.fi)
    expect(errors).toEqual([])
  })
})
