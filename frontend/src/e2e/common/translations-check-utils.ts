// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

import React from 'react'
import { isArray, toArray, isObject } from 'lodash'

type TranslationError = {
    key: string,
    value: string
}

type UnwantedText = {
    valueCheck: (value: string) => Boolean
}
const unwantedTexts: UnwantedText[] = [
    {
        valueCheck: (value) => new RegExp('espoo').test(value)
    },
    {
        valueCheck: (value) => new RegExp('liittyvä\\p{Letter}* varhais', 'u').test(value)
    }
]

const checkTampereTranslation = (key: string, value: string | null, errors: TranslationError[]) => {
    if (value !== null && value !== undefined) {
        unwantedTexts.forEach(unwantedText => {
            if (unwantedText.valueCheck(value.toLowerCase())) {
                let error = { key, value }
                errors.push(error);
            }
        })
    }
}

const checkTampereTranslationInner = (translationKeyAgg: string, object: any, errors: TranslationError[]) => {
    if (React.isValidElement(object)) {
        checkTampereTranslation(translationKeyAgg, JSON.stringify(object), errors);
    } else if (isObject(object)) {
        Object.entries(object).forEach(([key, value]) => {
            checkTampereTranslationInner(translationKeyAgg.concat('.', key), value, errors)
        });
    } else if (isArray(object)) {
        let valAsArray = toArray(object)
        for (let val in valAsArray) {
            checkTampereTranslationInner(translationKeyAgg, val, errors)
        }
    } else {
        checkTampereTranslation(translationKeyAgg, object, errors)
    }
}

export const checkTampereTranslations = (translationsFi: { [key: string]: any }): TranslationError[] => {
    let errors: TranslationError[] = []
    for (const key of Object.keys(translationsFi)) {
        checkTampereTranslationInner(key, translationsFi[key], errors)
    }
    return errors
}