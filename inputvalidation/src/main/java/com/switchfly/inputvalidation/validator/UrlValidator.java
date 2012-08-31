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

import org.apache.commons.lang.StringUtils;

public class UrlValidator extends Validator<String> {

    @Override
    public boolean execute(String content) {
        if (StringUtils.isBlank(content)) {
            return false;
        }
        String[] schemes = {"http", "https"};
        org.apache.commons.validator.UrlValidator urlValidator =
            new org.apache.commons.validator.UrlValidator(schemes, org.apache.commons.validator.UrlValidator.ALLOW_2_SLASHES);

        if (content.startsWith("http")) {
            return urlValidator.isValid(content);
        }

        return execute("http://www.mock.com/" + content);
    }
}