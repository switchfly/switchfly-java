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

public class NameValidationStrategiesTest {

    @Test
    public void testClean() throws Exception {
        ValidationStrategy<String> validationStrategy = ValidationStrategy.NAME.cleanStrategy();

        assertEquals("Pamela`", validationStrategy.validate("Pamela`"));
        assertEquals("William Luke Bunselmeyer", validationStrategy.validate("William Luke Bunselmeyer"));

        assertEquals("或憑證號碼調情是在事先不少於小時重申選擇", validationStrategy.validate("或憑證號碼調情是在事先不少於小時重申選擇"));

        assertEquals("_-.0123456789aAbBcCdDeEfFgGhHiIjJkKlLmMnNñoOópPqQrRsStTuUvVwWxXyYzZ Ã",
            validationStrategy.validate("_-.0123456789aAbBcCdDeEfFgGhHiIjJkKlLmMnNñoOópPqQrRsStTuUvVwWxXyYzZ Ã"));

        assertEquals("title blah blah blah.", validationStrategy.validate("<h3>title</h3><p>blah blah blah.</p>"));

        assertEquals("", validationStrategy.validate("@"));
        assertEquals("", validationStrategy.validate(";"));
        assertEquals("", validationStrategy.validate("!"));
        assertEquals("", validationStrategy.validate("/"));
        assertEquals("", validationStrategy.validate("("));
        assertEquals("", validationStrategy.validate(")"));
        assertEquals("", validationStrategy.validate("$"));
        assertEquals("", validationStrategy.validate("&"));
        assertEquals("", validationStrategy.validate("%"));
        assertEquals("", validationStrategy.validate("#"));
        assertEquals("", validationStrategy.validate("<"));
        assertEquals("", validationStrategy.validate(">"));
        assertEquals("", validationStrategy.validate("^"));
        assertEquals("", validationStrategy.validate("*"));
        assertEquals("", validationStrategy.validate("="));
        assertEquals("", validationStrategy.validate("{"));
        assertEquals("", validationStrategy.validate("}"));
        assertEquals("", validationStrategy.validate("["));
        assertEquals("", validationStrategy.validate("]"));
        assertEquals("", validationStrategy.validate("\""));

        assertEquals("1 onmouseoveralert123123", validationStrategy.validate("1\" +onmouseover=alert(\"123123\") +"));
        assertEquals("1 onmouseoveralert123123", validationStrategy.validate("1\"+onmouseover=alert(\"123123\")+"));
        assertEquals("1' onmouseoveralert'123123'", validationStrategy.validate("1'+onmouseover=alert('123123')+"));
        assertEquals("1'onmouseoveralert'123123'", validationStrategy.validate("1'onmouseover=alert('123123')"));
        assertEquals("1'onmouseoveralert123123", validationStrategy.validate("1'onmouseover=alert(123123)"));
    }

    @Test
    public void testValidate() {
        ValidationStrategy<String> validationStrategy = ValidationStrategy.NAME.validateStrategy();

        assertEquals("Pamela`", validationStrategy.validate("Pamela`"));
        assertEquals("William Luke Bunselmeyer", validationStrategy.validate("William Luke Bunselmeyer"));

        assertEquals("_-.0123456789aAbBcCdDeEfFgGhHiIjJkKlLmMnNñoOópPqQrRsStTuUvVwWxXyYzZ Ã",
            validationStrategy.validate("_-.0123456789aAbBcCdDeEfFgGhHiIjJkKlLmMnNñoOópPqQrRsStTuUvVwWxXyYzZ Ã"));

        assertEquals("或憑證號碼調情是在事先不少於小時重申選擇", validationStrategy.validate("或憑證號碼調情是在事先不少於小時重申選擇"));

        assertValidationException("<h3>title</h3><p>blah blah blah.</p>", validationStrategy);

        assertValidationException("@", validationStrategy);
        assertValidationException(";", validationStrategy);
        assertValidationException("!", validationStrategy);
        assertValidationException("/", validationStrategy);
        assertValidationException("(", validationStrategy);
        assertValidationException(")", validationStrategy);
        assertValidationException("$", validationStrategy);
        assertValidationException("&", validationStrategy);
        assertValidationException("%", validationStrategy);
        assertValidationException("#", validationStrategy);
        assertValidationException("<", validationStrategy);
        assertValidationException(">", validationStrategy);
        assertValidationException("^", validationStrategy);
        assertValidationException("*", validationStrategy);
        assertValidationException("=", validationStrategy);
        assertValidationException("{", validationStrategy);
        assertValidationException("}", validationStrategy);
        assertValidationException("[", validationStrategy);
        assertValidationException("]", validationStrategy);
        assertValidationException("\"", validationStrategy);

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
