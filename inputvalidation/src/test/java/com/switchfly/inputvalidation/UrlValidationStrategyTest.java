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

import com.switchfly.inputvalidation.exception.ValidatorException;
import org.junit.Test;

import static org.junit.Assert.*;

public class UrlValidationStrategyTest {
    @Test
    public void testValidate() throws Exception {
        UrlValidationStrategy validationStrategy = new UrlValidationStrategy();

        assertEquals("http://www.foo.com/to/page.cfm?a=1&b=2#bar", validationStrategy.validate("http://www.foo.com/to/page.cfm?a=1&b=2#bar"));
        assertEquals("https://www.foo.com/to/page.cfm?a=1&b=2#bar", validationStrategy.validate("https://www.foo.com/to/page.cfm?a=1&b=2#bar"));
        assertEquals("/to/page.cfm?a=1&b=2#bar", validationStrategy.validate("/to/page.cfm?a=1&b=2#bar"));
        assertValidationException("data%3atext%2fht%%206dl%3bbase64%2cPHdoc2N%20oZWNrPg%3d%3d", validationStrategy);
    }

    public static void assertValidationException(String content, ValidationStrategy<String> validationStrategy) {
        try {
            validationStrategy.validate(content);
            fail("content should not be valid.");
        } catch (ValidatorException e) {
            assertTrue(e instanceof ValidatorException);
        }
    }
}
