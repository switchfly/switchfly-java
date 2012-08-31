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

package com.switchfly.inputvalidation.canonicalizer;

import java.text.Normalizer;
import org.apache.commons.lang.StringUtils;

/**
 * Canonicalize content to decomposed Unicode.
 * Do not use to canonicalize content rendered in a web browser
 * because content is not HTML or URL decoded.
 */
public class StringCanonicalizer implements Canonicalizer<String> {

    @Override
    public String execute(String content) {
        if (StringUtils.isBlank(content)) {
            return content;
        }
        try {
            return Normalizer.normalize(content, Normalizer.Form.NFC);
        } catch (Exception e) {
            throw new IllegalArgumentException("Canonicalization error", e);
        }
    }
}