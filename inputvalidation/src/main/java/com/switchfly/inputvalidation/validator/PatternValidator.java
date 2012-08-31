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

package com.switchfly.inputvalidation.validator;

import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

public class PatternValidator extends Validator<String> {
    public static final int FLAGS = Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE;
    private final Pattern _pattern;

    public PatternValidator(String regex) {
        _pattern = Pattern.compile(regex, FLAGS);
    }

    public PatternValidator(Pattern pattern) {
        _pattern = pattern;
    }

    @Override
    public boolean execute(String content) {
        return StringUtils.isBlank(content) || _pattern.matcher(content).matches();
    }
}
