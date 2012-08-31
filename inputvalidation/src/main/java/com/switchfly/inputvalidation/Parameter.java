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

import com.switchfly.inputvalidation.exception.InvalidRequestParameterException;
import com.switchfly.inputvalidation.exception.MissingRequestParameterException;
import com.switchfly.inputvalidation.exception.ValidatorException;

public class Parameter<E> {

    private final String _name;
    private final E _value;
    private final ValidationStrategy<String> _parameterNameValidationStrategy = new ParameterNameValidationStrategy();

    private ValidationStrategy<E> _validationStrategy = new ValidationStrategy<E>(null, null, null);

    public Parameter(String name, E value) {
        _name = name;
        _value = value;
    }

    protected E getValue() {
        return _value;
    }

    public String getName() {
        return _parameterNameValidationStrategy.validate(_name);
    }

    public Parameter<E> validateWith(ValidationStrategy<E> validationStrategy) {
        _validationStrategy = validationStrategy;
        return this;
    }

    public boolean isEmpty() {
        return _value == null;
    }

    public E validate() {
        if (isEmpty()) {
            throw new MissingRequestParameterException(getName());
        }
        try {
            return _validationStrategy.validate(_value);
        } catch (ValidatorException e) {
            throw new InvalidRequestParameterException(getName(), e.getSanitizedContent());
        }
    }

    public E validate(E defaultValue) {
        try {
            return validate();
        } catch (InvalidRequestParameterException e) {
            return defaultValue;
        } catch (MissingRequestParameterException e) {
            return defaultValue;
        }
    }
}
