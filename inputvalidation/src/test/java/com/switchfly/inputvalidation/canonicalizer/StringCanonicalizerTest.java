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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringCanonicalizerTest {

    /**
     * Test composed and decomposed chacters with Normalizer.Form.NFC
     * LATIN CAPITAL LETTER A WITH ACUTE: http://www.fileformat.info/info/unicode/char/00c1/index.htm
     * LATIN SMALL LETTER O WITH DIAERESIS: http://www.fileformat.info/info/unicode/char/00f6/index.htm
     * LATIN SMALL LETTER A WITH ACUTE: http://www.fileformat.info/info/unicode/char/00e1/index.htm
     */

    @Test
    public void testExecute() throws Exception {
        StringCanonicalizer canonicalizer = new StringCanonicalizer();

        assertEquals("\u00C1", canonicalizer.execute("A\u0301"));
        assertEquals("\u00F6", canonicalizer.execute("o\u0308"));
        assertEquals("\u00E1", canonicalizer.execute("a\u0301"));
        assertEquals(" ", canonicalizer.execute(" "));
        assertEquals("", canonicalizer.execute(""));
        assertEquals(null, canonicalizer.execute(null));
        assertEquals("<script", canonicalizer.execute("\u003Cscript"));
        assertEquals("&lt;script", canonicalizer.execute("&lt;script"));
        assertEquals("&#x3C;&#x73;&#x63;&#x72;&#x69;&#x70;&#x74;", canonicalizer.execute("&#x3C;&#x73;&#x63;&#x72;&#x69;&#x70;&#x74;"));
        assertEquals("%3C%73%63%72%69%70%74", canonicalizer.execute("%3C%73%63%72%69%70%74"));
    }
}
