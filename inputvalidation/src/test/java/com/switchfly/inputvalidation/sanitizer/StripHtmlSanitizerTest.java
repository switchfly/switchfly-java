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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Whitelist;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StripHtmlSanitizerTest {

    private static final String URL = "http://www.foo.com?a=1&num_rooms=1&children=0&int=VA&b=2";

    @Test
    public void testExecute() throws Exception {

        StripHtmlSanitizer sanitizer = new StripHtmlSanitizer();

        String html = IOUtils.toString(getClass().getResourceAsStream("StripHtmlSanitizerTest_dirty.html"));

        assertEquals("SPG Flights FAQs Home Frequently Asked Questions", sanitizer.execute(html));
    }

    @Test
    public void testStringEscapeUtilsUnescapeHtml() throws Exception {
        // org.apache.commons.lang.StringEscapeUtils;
        assertEquals(URL, StringEscapeUtils.unescapeHtml(URL));
    }

    /**
     * Fails: unescapes &num, &chi, and &int to #, χ, and ∫ respectively
     * Expected :http://www.foo.com?a=1&num_rooms=1&children=0&int=VA&b=2
     * Actual   :http://www.foo.com?a=1#_rooms=1&children=0∫=VA&b=2
     */
    @Test
    @Ignore
    public void testSanitizeUrl() throws Exception {
        StripHtmlSanitizer sanitizer = new StripHtmlSanitizer();
        assertEquals(URL, sanitizer.execute(URL));
    }

    /**
     * Fails: unescapes &num, &chi, and &int to #, χ, and ∫ respectively
     * Expected :http://www.foo.com?a=1&num_rooms=1&children=0&int=VA&b=2
     * Actual   :http://www.foo.com?a=1#_rooms=1χldren=0∫=VA&amp;b=2
     */
    @Test
    @Ignore
    public void testJsoupClean() throws Exception {
        String html = "<a href=\"" + URL + "\">" + URL + "</a>";
        assertEquals(URL, Jsoup.clean(html, Whitelist.none()));
    }

    /**
     * Fails: unescapes &num, &chi, and &int to #, χ, and ∫ respectively
     * Expected :http://www.foo.com?a=1&num_rooms=1&children=0&int=VA&b=2
     * Actual   :http://www.foo.com?a=1#_rooms=1&children=0∫=VA&b=2
     */
    @Test
    @Ignore
    public void testJsoupTextNodeCreateFromEncoded() throws Exception {
        assertEquals(URL, TextNode.createFromEncoded(URL, null).text());
    }
}
