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

public class CleanPatternSanitizerTest {
    @Test
    public void testExecuteWithCharacterClass() throws Exception {
        CleanPatternSanitizer sanitizer = new CleanPatternSanitizer("[\\p{L}\\p{Nd}\\.\\-\\s#&',:\\/\\\\]+");
        assertEquals("P.O. Box 123", sanitizer.execute("P.O. Box 123"));
        assertEquals("/P.O. Box 123", sanitizer.execute("/P.O. Box 123"));
        assertEquals("\\P.O. Box 123", sanitizer.execute("\\P.O. Box 123"));
        assertEquals("b50 First St/b, iFloor 7/uscriptalerthacked/script",
            sanitizer.execute("<b>50 First St</b>, <i>Floor 7</u><script>alert(hacked!);</script>"));
        assertEquals("50 First St, Floor 7", sanitizer.execute("^50 First} St, ~Flo|or 7"));
    }

    @Test
    public void testExecuteWithPattern() throws Exception {
        CleanPatternSanitizer sanitizer = new CleanPatternSanitizer("[\\d]{4}-[\\d]{2}-[\\d]{2}");
        assertEquals("2012-06-28", sanitizer.execute("2012-06-28"));
        assertEquals("2012-06-282012-06-28", sanitizer.execute("2012-06-28 2012-06-28"));
        assertEquals("2012-06-28", sanitizer.execute("yadda yadda 2012-06-28"));
        assertEquals("", sanitizer.execute("^50 First} St, ~Flo|or 7"));
    }
}
