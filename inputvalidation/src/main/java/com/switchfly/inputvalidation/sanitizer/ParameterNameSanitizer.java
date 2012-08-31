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

package com.switchfly.inputvalidation.sanitizer;

import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

public class ParameterNameSanitizer implements Sanitizer<String> {

    private static final Pattern PATTERN = Pattern.compile("[^\\w_-]");

    @Override
    public String execute(String content) {
        if (StringUtils.isBlank(content)) {
            return content;
        }
        return PATTERN.matcher(content).replaceAll("");
    }
}
