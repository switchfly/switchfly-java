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

public class AddressLineValidationStrategiesTest {
    @Test
    public void testClean() throws Exception {
        ValidationStrategy<String> validationStrategy = ValidationStrategy.ADDRESS_LINE.cleanStrategy();

        assertEquals("อ", validationStrategy.validate("อ"));
        assertEquals("อ", validationStrategy.validate("%E0%B8%AD"));
        assertEquals("อ", validationStrategy.validate("&#3629;"));
        assertEquals("P.O. Box 123", validationStrategy.validate("P.O. Box 123"));
        assertEquals("/P.O. Box 123", validationStrategy.validate("/P.O. Box 123"));
        assertEquals("\\P.O. Box 123", validationStrategy.validate("\\P.O. Box 123"));
        assertEquals("50 First St, Floor 7", validationStrategy.validate("50 First St, Floor 7"));
        assertEquals("50 First St, Floor 7", validationStrategy.validate("<b>50 First St</b>, <i>Floor 7</u><script>alert(hacked!);</script>"));
        assertEquals("50 First St, Floor 7", validationStrategy.validate("^50 First} St, ~Flo|or 7"));

        assertEquals("Ligue para o número no voucher com no mínimo 24 horas de antecedência para reconfirmar a sua atividade selecionada. " +
            "Horário para reconfirmação: de 8:00h às 18:00h - Segunda a Sexta, 8:00h às 12:00h - Sábado", validationStrategy.validate(
            "Ligue para o número no voucher com no mínimo 24 horas de antecedência para reconfirmar a sua atividade selecionada. " +
                "Horário para reconfirmação: de 8:00h às 18:00h - Segunda a Sexta, 8:00h às 12:00h - Sábado"));

        assertEquals("或憑證號碼調情是在事先不少於小時重申選擇", validationStrategy.validate("或憑證號碼調情是在事先不少於小時重申選擇"));

        assertEquals("-.0123456789aAbBcCdDeEfFgGhHiIjJkKlLmMnNñoOópPqQrRsStTuUvVwWxXyYzZ Ã# &",
            validationStrategy.validate("-.0123456789aAbBcCdDeEfFgGhHiIjJkKlLmMnNñoOópPqQrRsStTuUvVwWxXyYzZ Ã# &"));

        /**
         * We have disabled HTML output encoding to so that special characters, such as ê and ú, are not also encoded.
         * Unfortunately, without output encoding, many XSS attacks become *MUCH* more difficult to prevent.
         * If the following tests fail, then we may be vulnerable to XSS attacks.
         */
        assertEquals("", validationStrategy.validate("_"));
        assertEquals("", validationStrategy.validate("@"));
        assertEquals("", validationStrategy.validate("!"));
        assertEquals("", validationStrategy.validate("$"));
        assertEquals("", validationStrategy.validate("%"));
        assertEquals("", validationStrategy.validate("^"));
        assertEquals("", validationStrategy.validate("*"));
        assertEquals("", validationStrategy.validate("["));
        assertEquals("", validationStrategy.validate("]"));
        assertEquals("", validationStrategy.validate("<"));
        assertEquals("", validationStrategy.validate(">"));
        assertEquals("", validationStrategy.validate("="));
        assertEquals("", validationStrategy.validate("{"));
        assertEquals("", validationStrategy.validate("}"));
        assertEquals("", validationStrategy.validate("\""));
        assertEquals("", validationStrategy.validate("("));
        assertEquals("", validationStrategy.validate(")"));
        assertEquals("", validationStrategy.validate(";"));

        /**
         * HTML output encoding alone will not prevent HTML tag attribute attacks.  DO NOT allow these characters: ( ) = "
         */
        assertEquals("1 onmouseoveralert123123", validationStrategy.validate("1\" +onmouseover=alert(\"123123\") +"));
        assertEquals("1 onmouseoveralert123123", validationStrategy.validate("1\"+onmouseover=alert(\"123123\")+"));
        assertEquals("1' onmouseoveralert'123123'", validationStrategy.validate("1'+onmouseover=alert('123123')+"));
        assertEquals("1'onmouseoveralert'123123'", validationStrategy.validate("1'onmouseover=alert('123123')"));
        assertEquals("1'onmouseoveralert123123", validationStrategy.validate("1'onmouseover=alert(123123)"));
    }

    @Test
    public void testValidate() throws Exception {
        ValidationStrategy<String> validationStrategy = ValidationStrategy.ADDRESS_LINE.validateStrategy();

        assertEquals("อ", validationStrategy.validate("อ"));
        assertEquals("อ", validationStrategy.validate("%E0%B8%AD"));
        assertEquals("อ", validationStrategy.validate("&#3629;"));
        assertEquals("P.O. Box 123", validationStrategy.validate("P.O. Box 123"));
        assertEquals("/P.O. Box 123", validationStrategy.validate("/P.O. Box 123"));
        assertEquals("\\P.O. Box 123", validationStrategy.validate("\\P.O. Box 123"));
        assertEquals("50 First St, Floor 7", validationStrategy.validate("50 First St, Floor 7"));

        assertEquals("Ligue para o número no voucher com no mínimo 24 horas de antecedência para reconfirmar a sua atividade selecionada. " +
            "Horário para reconfirmação: de 8:00h às 18:00h - Segunda a Sexta, 8:00h às 12:00h - Sábado", validationStrategy.validate(
            "Ligue para o número no voucher com no mínimo 24 horas de antecedência para reconfirmar a sua atividade selecionada. " +
                "Horário para reconfirmação: de 8:00h às 18:00h - Segunda a Sexta, 8:00h às 12:00h - Sábado"));

        assertEquals("或憑證號碼調情是在事先不少於小時重申選擇", validationStrategy.validate("或憑證號碼調情是在事先不少於小時重申選擇"));

        assertEquals("-.0123456789aAbBcCdDeEfFgGhHiIjJkKlLmMnNñoOópPqQrRsStTuUvVwWxXyYzZ Ã# &",
            validationStrategy.validate("-.0123456789aAbBcCdDeEfFgGhHiIjJkKlLmMnNñoOópPqQrRsStTuUvVwWxXyYzZ Ã# &"));

        assertValidationException("<b>50 First St</b>, <i>Floor 7</u><script>alert(hacked!);</script>", validationStrategy);
        assertValidationException("^50 First} St, ~Flo|or 7", validationStrategy);

        assertValidationException("<h3>title</h3><p>blah blah blah.</p>", validationStrategy);

        assertValidationException("_", validationStrategy);
        assertValidationException("@", validationStrategy);
        assertValidationException("!", validationStrategy);
        assertValidationException("$", validationStrategy);
        assertValidationException("%", validationStrategy);
        assertValidationException("^", validationStrategy);
        assertValidationException("*", validationStrategy);
        assertValidationException("[", validationStrategy);
        assertValidationException("]", validationStrategy);

        /**
         * We have disabled HTML output encoding to so that special characters, such as ê and ú, are not also encoded.
         * Unfortunately, without output encoding, many XSS attacks become *MUCH* more difficult to prevent.
         * If the following tests fail, then we may be vulnerable to XSS attacks.
         */
        assertValidationException("<", validationStrategy);
        assertValidationException(">", validationStrategy);
        assertValidationException("=", validationStrategy);
        assertValidationException("{", validationStrategy);
        assertValidationException("}", validationStrategy);
        assertValidationException("\"", validationStrategy);
        assertValidationException("(", validationStrategy);
        assertValidationException(")", validationStrategy);
        assertValidationException(";", validationStrategy);

        /**
         * HTML output encoding alone will not prevent HTML tag attribute attacks.  DO NOT allow these characters: ( ) = "
         */
        assertValidationException("1\" +onmouseover=alert(\"123123\") +", validationStrategy);
        assertValidationException("1\"+onmouseover=alert(\"123123\")+", validationStrategy);
        assertValidationException("1'+onmouseover=alert('123123')+", validationStrategy);
        assertValidationException("1'onmouseover=alert('123123')", validationStrategy);
        assertValidationException("1'onmouseover=alert(123123)", validationStrategy);
    }

    public static void assertValidationException(String html, ValidationStrategy<String> validationStrategy) {
        try {
            validationStrategy.validate(html);
            fail("content should not be valid.");
        } catch (ValidatorException e) {
            assertTrue(e instanceof ValidatorException);
        }
    }
}
