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

export const checkTampereTranslations = (translationsFi: { [key: string]: any }): string[] => {
    let errors: string[] = []
    for (const key of Object.keys(translationsFi)) {
        checkTampereTranslationInner(translationsFi[key], errors)
    }
    return errors
}