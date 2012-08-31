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
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

public class RequestParameter extends Parameter<String> {

    public RequestParameter(String name, String value) {
        super(name, value);
        validateWith(ValidationStrategy.DEFAULT_WEB_CONTENT_VALIDATION);
    }

    public boolean isBlank() {
        return StringUtils.isBlank(getValue());
    }

    @Override
    public boolean isEmpty() {
        return isBlank();
    }

    @Override
    public RequestParameter validateWith(ValidationStrategy<String> validationStrategy) {
        super.validateWith(validationStrategy);
        return this;
    }

    @Override
    public String toString() {
        return validate();
    }

    public String toString(String defaultValue) {
        return validate(defaultValue);
    }

    public Double toDouble() {
        String value = toString();
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new InvalidRequestParameterException(getName(), value);
        }
    }

    public Double toDouble(double defaultValue) {
        try {
            return toDouble();
        } catch (InvalidRequestParameterException e) {
            return defaultValue;
        } catch (MissingRequestParameterException e) {
            return defaultValue;
        }
    }

    public Long toLong() {
        String value = toString();
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new InvalidRequestParameterException(getName(), value);
        }
    }

    public Long toLong(long defaultValue) {
        try {
            return toLong();
        } catch (InvalidRequestParameterException e) {
            return defaultValue;
        } catch (MissingRequestParameterException e) {
            return defaultValue;
        }
    }

    public Integer toInteger() {
        String value = toString();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new InvalidRequestParameterException(getName(), value);
        }
    }

    public Integer toInteger(int defaultValue) {
        try {
            return toInteger();
        } catch (InvalidRequestParameterException e) {
            return defaultValue;
        } catch (MissingRequestParameterException e) {
            return defaultValue;
        }
    }

    public boolean toBoolean() {
        String value = toString();
        return BooleanUtils.toBoolean(value);
    }

    public boolean toBoolean(boolean defaultValue) {
        try {
            return toBoolean();
        } catch (InvalidRequestParameterException e) {
            return defaultValue;
        } catch (MissingRequestParameterException e) {
            return defaultValue;
        }
    }

    public <E extends Enum<E>> E toEnum(Class<E> type) {
        String value = toString();
        try {
            return Enum.valueOf(type, value.toUpperCase());
        } catch (Exception e) {
            throw new InvalidRequestParameterException(getName(), value);
        }
    }

    public <E extends Enum<E>> E toEnum(Class<E> type, E defaultValue) {
        try {
            return toEnum(type);
        } catch (InvalidRequestParameterException e) {
            return defaultValue;
        } catch (MissingRequestParameterException e) {
            return defaultValue;
        }
    }
}
