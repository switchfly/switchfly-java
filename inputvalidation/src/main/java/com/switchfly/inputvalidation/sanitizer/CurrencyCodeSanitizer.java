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

import java.util.Currency;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class CurrencyCodeSanitizer implements Sanitizer<String> {
    private static final Logger _logger = Logger.getLogger(CobrandNameSanitizer.class);

    @Override
    public String execute(String content) {
        if (StringUtils.isBlank(content)) {
            return content;
        }
        try {
            return Currency.getInstance(StringUtils.upperCase(content)).toString();
        } catch (Exception e) {
            HtmlSanitizer htmlSanitizer = new HtmlSanitizer();
            String sanitized = htmlSanitizer.execute(content);
            _logger.warn("Invalid currency code (" + sanitized + "). Setting currency code to \"USD\".");
            return "USD";
        }
    }
}