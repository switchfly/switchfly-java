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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CobrandNameSanitizerTest {

    @Test
    public void testExecute() throws Exception {

        CobrandNameSanitizer sanitizer = new CobrandNameSanitizer();

        assertEquals("Abc-12_3", sanitizer.execute("Abc-12_3"));
        assertEquals("", sanitizer.execute(""));
        assertEquals(" ", sanitizer.execute(" "));
        assertEquals(null, sanitizer.execute(null));
        assertEquals("default", sanitizer.execute("Abc- 12_3"));
        assertEquals("default", sanitizer.execute("Abc-12_3!"));
        assertEquals("default", sanitizer.execute("Abc-12_3'"));
        assertEquals("default", sanitizer.execute("Abc-12_3\""));
        assertEquals("default", sanitizer.execute("<script>"));
    }
}
