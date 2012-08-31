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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UrlValidatorTest {
    @Test
    public void testExecute() throws Exception {

        UrlValidator validator = new UrlValidator();

        assertEquals(false, validator.execute(""));
        assertEquals(false, validator.execute(" "));
        assertEquals(false, validator.execute(null));
        assertEquals(true, validator.execute("http://www.foo.com/to/page.cfm?a=1&b=2#bar"));
        assertEquals(true, validator.execute("https://www.foo.com/to/page.cfm?a=1&b=2#bar"));
        assertEquals(true, validator.execute("/to/page.cfm?a=1&b=2#bar"));
        assertEquals(true, validator.execute("/to/page.cfm?a=%00%foo&b=2#bar"));
        assertEquals(false, validator.execute("/to/page.cfm?a=ü&b=2#bar"));
        assertEquals(false, validator.execute("https://www.foo.com/to/page.cfm?a=ü&b=2#bar"));
        assertEquals(false, validator.execute("https://www.foo.com/to/page.cfm?a=ü&b=2#bar"));
    }
}
