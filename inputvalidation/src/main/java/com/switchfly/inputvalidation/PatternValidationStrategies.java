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

import com.switchfly.inputvalidation.canonicalizer.HtmlAndQueryStringCanonicalizer;
import com.switchfly.inputvalidation.sanitizer.StripHtmlAndCleanPatternSanitizer;
import com.switchfly.inputvalidation.sanitizer.StripHtmlSanitizer;
import com.switchfly.inputvalidation.validator.PatternValidator;

public class PatternValidationStrategies implements ValidationStrategies {

    private final ValidationStrategy<String> _cleanStrategy;
    private final ValidationStrategy<String> _validateStrategy;

    public PatternValidationStrategies(String acceptedPattern) {
        _cleanStrategy =
            new ValidationStrategy<String>(new HtmlAndQueryStringCanonicalizer(), null, new StripHtmlAndCleanPatternSanitizer(acceptedPattern));
        _validateStrategy =
            new ValidationStrategy<String>(new HtmlAndQueryStringCanonicalizer(), new PatternValidator(acceptedPattern), new StripHtmlSanitizer());
    }

    @Override
    public ValidationStrategy<String> cleanStrategy() {
        return _cleanStrategy;
    }

    @Override
    public ValidationStrategy<String> validateStrategy() {
        return _validateStrategy;
    }
}
