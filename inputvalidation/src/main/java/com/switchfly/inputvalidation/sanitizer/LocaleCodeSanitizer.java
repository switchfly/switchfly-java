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

import java.util.Arrays;
import java.util.Locale;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class LocaleCodeSanitizer implements Sanitizer<String> {
    private static final Logger _logger = Logger.getLogger(CobrandNameSanitizer.class);

    @Override
    public String execute(String content) {
        if (StringUtils.isBlank(content)) {
            return content;
        }
        Locale locale;
        try {
            locale = LocaleUtils.toLocale(content);
        } catch (Exception e) {
            return getDefaultLocale(content);
        }
        if (!Arrays.asList(Locale.getAvailableLocales()).contains(locale)) {
            return getDefaultLocale(content);
        }
        return locale.toString();
    }

    private String getDefaultLocale(String content) {
        String sanitized = new HtmlSanitizer().execute(content);
        _logger.warn("Invalid locale code (" + sanitized + "). Setting locale code to \"en_US\".");
        return Locale.US.toString();
    }
}
