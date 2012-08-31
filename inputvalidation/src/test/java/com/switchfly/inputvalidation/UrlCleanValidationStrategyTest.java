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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UrlCleanValidationStrategyTest {

    private final ValidationStrategy<String> _validationStrategy = new UrlCleanValidationStrategy();

    @Test
    public void testValidate() throws Exception {
        assertEquals("http://www.foo.com/bar.cfm?a=1&b=2&c=3&num_rooms=3&int=4&children=5",
            _validationStrategy.validate("http://www.foo.com/bar.cfm?a=1&b=2&c=3&num_rooms=3&int=4&children=5"));

        assertEquals("http://www.foo.com/bar.cfm?a=1&b=2&c=3&d=",
            _validationStrategy.validate("http://www.foo.com/bar.cfm?a=1&b=2&c=3&d=%3C%2Fscript%3E%3Cscript%3Ealert%281%29%3C%2Fscript%3E"));

        assertEquals("http://www.foo.com/bar.cfm?a=1&b=2&c=3&d=%2Ftravel%2Farc.cfm&e=11",
            _validationStrategy.validate("http://www.foo.com/bar.cfm?a=1&b=2&c=3&d=/travel/arc.cfm&e=11"));

        assertEquals("http://www.foo.com/bar.cfm?a=1&b=2&c=3&d=%2Ftravel%2Farc.cfm%3Fnav%3Dfoo%26tab%3Daa&e=11",
            _validationStrategy.validate("http://www.foo.com/bar.cfm?a=1&b=2&c=3&d=%2Ftravel%2Farc.cfm%3Fnav%3Dfoo%26tab%3Daa&e=11"));
    }
}