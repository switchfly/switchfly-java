/*
 * Copyright 2012 Switchfly
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.switchfly.inputvalidation;

import com.switchfly.inputvalidation.canonicalizer.Canonicalizer;
import com.switchfly.inputvalidation.exception.ValidatorException;
import com.switchfly.inputvalidation.sanitizer.Sanitizer;
import com.switchfly.inputvalidation.validator.Validator;
import org.apache.commons.lang.StringUtils;

public class ValidationStrategy<O> {

    public static final ValidationStrategy<String> DEFAULT_WEB_CONTENT_VALIDATION = new WebContentValidationStrategy();
    public static final ValidationStrategy<String> CLEAN_CURRENCY_CODE = new CurrencyCodeValidationStrategy();
    public static final ValidationStrategy<String> CLEAN_LOCALE_CODE = new LocaleCodeValidationStrategy();
    public static final ValidationStrategy<String> CLEAN_URL = new UrlCleanValidationStrategy();

    public static final ValidationStrategies COBRAND_NAME = ValidationStrategyBuilder.withPattern("[\\p{L}\\p{Nd}\\-_]+");
    public static final ValidationStrategies ADDRESS_LINE = ValidationStrategyBuilder.withPattern("[\\p{L}\\p{Nd}\\.\\-\\s#&',:\\/\\\\]+");
    public static final ValidationStrategies NAME = ValidationStrategyBuilder.withPattern("[\\p{L}\\p{Nd}_\\.\\-\\s~,'`]+");
    public static final ValidationStrategies PROPERTY_NAME = ValidationStrategyBuilder.withPattern("[\\w\\d\\-\\._]+");

    private final Canonicalizer<O> _canonicalizer;
    private final Validator<O> _validator;
    private final Sanitizer<O> _sanitizer;

    public ValidationStrategy(Canonicalizer<O> canonicalizer, Validator<O> validator, Sanitizer<O> sanitizer) {
        _canonicalizer = canonicalizer;
        _validator = validator;
        _sanitizer = sanitizer;
    }

    public final Canonicalizer<O> getCanonicalizer() {
        return _canonicalizer;
    }

    public final Validator<O> getValidator() {
        return _validator;
    }

    public final Sanitizer<O> getSanitizer() {
        return _sanitizer;
    }

    public final O validate(O content) {
        O canonicalized = null;
        try {
            canonicalized = _canonicalizer != null ? _canonicalizer.execute(content) : content;
        } catch (Exception e) {
            throw new ValidatorException("", "Failed to canonicalize content.");
        }
        if (_validator != null && !_validator.execute(canonicalized)) {
            throw StringUtils.isBlank(_validator.getErrorMessage()) ? new ValidatorException(canonicalized.toString()) :
                new ValidatorException(canonicalized.toString(), _validator.getErrorMessage());
        }
        try {
            return _sanitizer != null ? _sanitizer.execute(canonicalized) : canonicalized;
        } catch (Exception e) {
            throw new ValidatorException("", "Failed to sanitize content.");
        }
    }
}