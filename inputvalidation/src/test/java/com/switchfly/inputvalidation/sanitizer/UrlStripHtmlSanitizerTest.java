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
import static org.junit.Assert.fail;

public class UrlStripHtmlSanitizerTest {

    private final UrlStripHtmlSanitizer _sanitizer = new UrlStripHtmlSanitizer();

    @Test
    public void testExecute() throws Exception {
        assertEquals("http://www.foo.com/", _sanitizer.execute("http://www.foo.com"));

        assertEquals("http://www.foo.com/path/page.cfm", _sanitizer.execute("http://www.foo.com/path/page.cfm"));

        assertEquals("http://www.foo.com/path/page.cfm?a=1&num_rooms=1&children=0&int=VA&b=2#foo",
            _sanitizer.execute("http://www.foo.com/path/page.cfm?a=1&num_rooms=1&children=0&int=VA&b=2#foo"));

        assertEquals("/path/page.cfm?a=1&num_rooms=1&children=0&int=VA&b=2",
            _sanitizer.execute("/path/page.cfm?a=1&num_rooms=1&children=0&int=VA&b=2"));

        assertEquals("/?a=1&num_rooms=1&children=0&int=VA&b=2", _sanitizer.execute("?a=1&num_rooms=1&children=0&int=VA&b=2"));

        assertEquals("/foo", _sanitizer.execute("foo"));

        assertEquals(
            "https://travel.americanexpress.co.uk/travel/itinerary_clear_process.cfm?redirect_url=%2Ftravel%2Farc.cfm%3Ftab%3Dact&nav=default",
            _sanitizer
                .execute("https://travel.americanexpress.co.uk/travel/itinerary_clear_process.cfm?redirect_url=/travel/arc.cfm?tab=act&nav=default"));

        try {
            _sanitizer.execute("<script>alert('hacked!');</script>");
            fail("Should throw URISyntaxException");
        } catch (Exception e) {
            assertEquals("java.net.URISyntaxException: Illegal character in path at index 0: <script>alert('hacked!');</script>", e.getMessage());
        }
    }

    @Test
    public void testExecuteWithXSS() throws Exception {
        assertEquals("http://www.foo.com/path/page.cfm?a=1&num_rooms=1&children=0&int=VA&b=2&d=", _sanitizer.execute(
            "http://www.foo.com/path/page.cfm?a=1&num_rooms=1&children=0&int=VA&b=2&d=%3C%2Fscript%3E%3Cscript%3Ealert%281%29%3C%2Fscript%3E"));
    }
}
