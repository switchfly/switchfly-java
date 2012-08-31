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

package com.switchfly.inputvalidation.exception;

public class MissingRequestParameterException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String _parameterName;

    public MissingRequestParameterException(String parameterName) {
        super("Missing request parameter: " + parameterName + ".");
        _parameterName = parameterName;
    }

    public MissingRequestParameterException(String parameterName, String message) {
        super(message);
        _parameterName = parameterName;
    }

    public MissingRequestParameterException(String parameterName, String message, Throwable cause) {
        super(message, cause);
        _parameterName = parameterName;
    }

    public String getParameterName() {
        return _parameterName;
    }
}
