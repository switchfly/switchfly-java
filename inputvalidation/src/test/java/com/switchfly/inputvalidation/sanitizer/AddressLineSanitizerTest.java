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

public class AddressLineSanitizerTest {
    @Test
    public void testExecute() throws Exception {
        AddressLineSanitizer sanitizer = new AddressLineSanitizer();

        assertEquals("E0B8AD", sanitizer.execute("%E0%B8%AD"));
        assertEquals("อ", sanitizer.execute("&#3629;"));
        assertEquals("P.O. Box 123", sanitizer.execute("P.O. Box 123"));
        assertEquals("/P.O. Box 123", sanitizer.execute("/P.O. Box 123"));
        assertEquals("\\P.O. Box 123", sanitizer.execute("\\P.O. Box 123"));
        assertEquals("50 First St, Floor 7", sanitizer.execute("50 First St, Floor 7"));
        assertEquals("50 First St, Floor 7", sanitizer.execute("<b>50 First St</b>, <i>Floor 7</u><script>alert(hacked!);</script>"));
        assertEquals("50 First St, Floor 7", sanitizer.execute("^50 First} St, ~Flo|or 7"));

        assertEquals("Ligue para o número no voucher com no mínimo 24 horas de antecedência para reconfirmar a sua atividade selecionada. " +
            "Horário para reconfirmação: de 8:00h às 18:00h - Segunda a Sexta, 8:00h às 12:00h - Sábado", sanitizer.execute(
            "Ligue para o número no voucher com no mínimo 24 horas de antecedência para reconfirmar a sua atividade selecionada. " +
                "Horário para reconfirmação: de 8:00h às 18:00h - Segunda a Sexta, 8:00h às 12:00h - Sábado"));

        assertEquals("-.0123456789aAbBcCdDeEfFgGhHiIjJkKlLmMnNñoOópPqQrRsStTuUvVwWxXyYzZ Ã# &",
            sanitizer.execute("-.0123456789aAbBcCdDeEfFgGhHiIjJkKlLmMnNñoOópPqQrRsStTuUvVwWxXyYzZ Ã# &"));

        /**
         * We have disabled HTML output encoding to so that special characters, such as ê and ú, are not also encoded.
         * Unfortunately, without output encoding, many XSS attacks become *MUCH* more difficult to prevent.
         * If the following tests fail, then we may be vulnerable to XSS attacks.
         */
        assertEquals("", sanitizer.execute("_"));
        assertEquals("", sanitizer.execute("@"));
        assertEquals("", sanitizer.execute("!"));
        assertEquals("", sanitizer.execute("$"));
        assertEquals("", sanitizer.execute("%"));
        assertEquals("", sanitizer.execute("^"));
        assertEquals("", sanitizer.execute("*"));
        assertEquals("", sanitizer.execute("["));
        assertEquals("", sanitizer.execute("]"));
        assertEquals("", sanitizer.execute("<"));
        assertEquals("", sanitizer.execute(">"));
        assertEquals("", sanitizer.execute("="));
        assertEquals("", sanitizer.execute("{"));
        assertEquals("", sanitizer.execute("}"));
        assertEquals("", sanitizer.execute("\""));
        assertEquals("", sanitizer.execute("("));
        assertEquals("", sanitizer.execute(")"));
        assertEquals("", sanitizer.execute(";"));

        /**
         * HTML output encoding alone will not prevent HTML tag attribute attacks.  DO NOT allow these characters: ( ) = "
         */
        assertEquals("1 onmouseoveralert123123 ", sanitizer.execute("1\" +onmouseover=alert(\"123123\") +"));
        assertEquals("1onmouseoveralert123123", sanitizer.execute("1\"+onmouseover=alert(\"123123\")+"));
        assertEquals("1'onmouseoveralert'123123'", sanitizer.execute("1'+onmouseover=alert('123123')+"));
        assertEquals("1'onmouseoveralert'123123'", sanitizer.execute("1'onmouseover=alert('123123')"));
        assertEquals("1'onmouseoveralert123123", sanitizer.execute("1'onmouseover=alert(123123)"));
    }
}
