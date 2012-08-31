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

import com.switchfly.compress.AssetPackageMode;
import com.switchfly.inputvalidation.exception.InvalidRequestParameterException;
import com.switchfly.inputvalidation.exception.MissingRequestParameterException;
import com.switchfly.inputvalidation.validator.Validator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RequestParameterTest {

    private static final ValidationStrategy<String> ALWAYS_INVALID_VALIDATION = new ValidationStrategy<String>(null, new Validator<String>() {
        @Override
        public boolean execute(String content) {
            return false;
        }
    }, null);

    @Test
    public void testGetName() throws Exception {
        assertEquals("Abc-12_4", new RequestParameter("Abc-12_4", null).getName());
        assertEquals("Abc-12_4_script", new RequestParameter("?Ab = c-1&2_4_!\"'<script>", null).getName());
        assertEquals(null, new RequestParameter(null, null).getName());
        assertEquals(" ", new RequestParameter(" ", null).getName());
        assertEquals("", new RequestParameter("", null).getName());
    }

    @Test
    public void testToStringWithValidValues() throws Exception {
        assertEquals("foo", new RequestParameter(null, "foo").toString());
    }

    @Test
    public void testToStringWithDefaultValues() throws Exception {
        assertEquals("foo", new RequestParameter(null, "foo").toString("bar"));
        assertEquals("bar", new RequestParameter(null, "").toString("bar"));
        assertEquals("bar", new RequestParameter(null, null).toString("bar"));
        assertEquals("bar", new RequestParameter(null, "foo").validateWith(ALWAYS_INVALID_VALIDATION).toString("bar"));
    }

    @Test
    public void testToStringInvalidValues() throws Exception {
        try {
            new RequestParameter("foo", "").toString();
        } catch (MissingRequestParameterException e) {
            assertEquals("foo", e.getParameterName());
        }

        try {
            new RequestParameter("foo", null).toString();
        } catch (MissingRequestParameterException e) {
            assertEquals("foo", e.getParameterName());
        }

        try {
            new RequestParameter("foo", "bar").validateWith(ALWAYS_INVALID_VALIDATION).toString();
        } catch (InvalidRequestParameterException e) {
            assertEquals("foo", e.getParameterName());
            assertEquals("bar", e.getParameterValue());
        }
    }

    @Test
    public void testToIntegerWithValidValues() throws Exception {
        assertEquals(Integer.valueOf(1), new RequestParameter(null, "1").toInteger());
        assertEquals(Integer.valueOf(-1), new RequestParameter(null, "-1").toInteger());
    }

    @Test
    public void testToIntegerWithDefaultValues() throws Exception {
        assertEquals(Integer.valueOf(2), new RequestParameter(null, "2").toInteger(1));
        assertEquals(Integer.valueOf(1), new RequestParameter(null, "foo").toInteger(1));
        assertEquals(Integer.valueOf(1), new RequestParameter(null, "").toInteger(1));
        assertEquals(Integer.valueOf(1), new RequestParameter(null, null).toInteger(1));
        assertEquals(Integer.valueOf(1), new RequestParameter(null, "2").validateWith(ALWAYS_INVALID_VALIDATION).toInteger(1));
    }

    @Test
    public void testToIntegerWithInValidValues() throws Exception {
        try {
            new RequestParameter("foo", "").toInteger();
        } catch (MissingRequestParameterException e) {
            assertEquals("foo", e.getParameterName());
        }

        try {
            new RequestParameter("foo", null).toInteger();
        } catch (MissingRequestParameterException e) {
            assertEquals("foo", e.getParameterName());
        }

        try {
            new RequestParameter("foo", "1").validateWith(ALWAYS_INVALID_VALIDATION).toInteger();
        } catch (InvalidRequestParameterException e) {
            assertEquals("foo", e.getParameterName());
            assertEquals("1", e.getParameterValue());
        }
    }

    @Test
    public void testToLongerWithValidValues() throws Exception {
        assertEquals(Long.valueOf(1), new RequestParameter(null, "1").toLong());
        assertEquals(Long.valueOf(-1), new RequestParameter(null, "-1").toLong());
    }

    @Test
    public void testToLongerWithDefaultValues() throws Exception {
        assertEquals(Long.valueOf(2), new RequestParameter(null, "2").toLong(1));
        assertEquals(Long.valueOf(1), new RequestParameter(null, "foo").toLong(1));
        assertEquals(Long.valueOf(1), new RequestParameter(null, "").toLong(1));
        assertEquals(Long.valueOf(1), new RequestParameter(null, null).toLong(1));
        assertEquals(Long.valueOf(1), new RequestParameter(null, "2").validateWith(ALWAYS_INVALID_VALIDATION).toLong(1));
    }

    @Test
    public void testToLongerInvalidValues() throws Exception {
        try {
            new RequestParameter("foo", "").toLong();
        } catch (MissingRequestParameterException e) {
            assertEquals("foo", e.getParameterName());
        }

        try {
            new RequestParameter("foo", null).toLong();
        } catch (MissingRequestParameterException e) {
            assertEquals("foo", e.getParameterName());
        }

        try {
            new RequestParameter("foo", "1").validateWith(ALWAYS_INVALID_VALIDATION).toLong();
        } catch (InvalidRequestParameterException e) {
            assertEquals("foo", e.getParameterName());
            assertEquals("1", e.getParameterValue());
        }
    }

    @Test
    public void testToDouble() throws Exception {
        assertEquals(Double.valueOf(0.1), new RequestParameter(null, "0.1").toDouble());
        assertEquals(Double.valueOf(-0.1), new RequestParameter(null, "-0.1").toDouble());

        assertEquals(Double.valueOf(0.1), new RequestParameter(null, "foo").toDouble(0.1));
        assertEquals(Double.valueOf(0.1), new RequestParameter(null, "").toDouble(0.1));
        assertEquals(Double.valueOf(0.1), new RequestParameter(null, null).toDouble(0.1));

        try {
            new RequestParameter("foo", "").toDouble();
        } catch (MissingRequestParameterException e) {
            assertEquals("foo", e.getParameterName());
        }

        try {
            new RequestParameter("foo", null).toDouble();
        } catch (MissingRequestParameterException e) {
            assertEquals("foo", e.getParameterName());
        }

        try {
            new RequestParameter("foo", "0.1").validateWith(ALWAYS_INVALID_VALIDATION).toDouble();
        } catch (InvalidRequestParameterException e) {
            assertEquals("foo", e.getParameterName());
            assertEquals("0.1", e.getParameterValue());
        }
    }

    @Test
    public void testToDoubleWithValidValues() throws Exception {
        assertEquals(Double.valueOf(0.1), new RequestParameter(null, "0.1").toDouble());
        assertEquals(Double.valueOf(-0.1), new RequestParameter(null, "-0.1").toDouble());
    }

    @Test
    public void testToDoubleWithDefaultValues() throws Exception {
        assertEquals(Double.valueOf(0.2), new RequestParameter(null, "0.2").toDouble(0.1));
        assertEquals(Double.valueOf(0.1), new RequestParameter(null, "foo").toDouble(0.1));
        assertEquals(Double.valueOf(0.1), new RequestParameter(null, "").toDouble(0.1));
        assertEquals(Double.valueOf(0.1), new RequestParameter(null, null).toDouble(0.1));
        assertEquals(Double.valueOf(0.1), new RequestParameter(null, "0.2").validateWith(ALWAYS_INVALID_VALIDATION).toDouble(0.1));
    }

    @Test
    public void testToDoubleWithInvalidValues() throws Exception {
        try {
            new RequestParameter("foo", "").toDouble();
        } catch (MissingRequestParameterException e) {
            assertEquals("foo", e.getParameterName());
        }

        try {
            new RequestParameter("foo", null).toDouble();
        } catch (MissingRequestParameterException e) {
            assertEquals("foo", e.getParameterName());
        }

        try {
            new RequestParameter("foo", "0.1").validateWith(ALWAYS_INVALID_VALIDATION).toDouble();
        } catch (InvalidRequestParameterException e) {
            assertEquals("foo", e.getParameterName());
            assertEquals("0.1", e.getParameterValue());
        }
    }

    @Test
    public void testToBooleanWithValidValues() throws Exception {
        assertEquals(true, new RequestParameter(null, "true").toBoolean());
        assertEquals(true, new RequestParameter(null, "TRUE").toBoolean());
        assertEquals(true, new RequestParameter(null, "yes").toBoolean());
        assertEquals(true, new RequestParameter(null, "YES").toBoolean());
        assertEquals(false, new RequestParameter(null, "false").toBoolean());
        assertEquals(false, new RequestParameter(null, "no").toBoolean());
        assertEquals(false, new RequestParameter(null, "1").toBoolean());
        assertEquals(false, new RequestParameter(null, "0").toBoolean());
        assertEquals(false, new RequestParameter(null, "foo").toBoolean());
    }

    @Test
    public void testToBooleanWithDefaultValues() throws Exception {
        assertEquals(true, new RequestParameter(null, "true").toBoolean(false));
        assertEquals(false, new RequestParameter(null, "foo").toBoolean(true));
        assertEquals(true, new RequestParameter(null, "true").toBoolean(false));
        assertEquals(true, new RequestParameter(null, "").toBoolean(true));
        assertEquals(true, new RequestParameter(null, null).toBoolean(true));
        assertEquals(false, new RequestParameter(null, "true").validateWith(ALWAYS_INVALID_VALIDATION).toBoolean(false));
    }

    @Test
    public void testToBooleanWithInvalidValues() throws Exception {
        try {
            new RequestParameter("foo", "").toBoolean();
        } catch (MissingRequestParameterException e) {
            assertEquals("foo", e.getParameterName());
        }

        try {
            new RequestParameter("foo", null).toBoolean();
        } catch (MissingRequestParameterException e) {
            assertEquals("foo", e.getParameterName());
        }

        try {
            new RequestParameter("foo", "true").validateWith(ALWAYS_INVALID_VALIDATION).toBoolean();
        } catch (InvalidRequestParameterException e) {
            assertEquals("foo", e.getParameterName());
            assertEquals("true", e.getParameterValue());
        }
    }

    @Test
    public void testToEnumWithValidValues() throws Exception {
        assertEquals(AssetPackageMode.PRODUCTION, new RequestParameter(null, "PRODUCTION").toEnum(AssetPackageMode.class));
        assertEquals(AssetPackageMode.PRODUCTION, new RequestParameter(null, "production").toEnum(AssetPackageMode.class));
        assertEquals(AssetPackageMode.PRODUCTION, new RequestParameter(null, "Production").toEnum(AssetPackageMode.class));
    }

    @Test
    public void testToEnumWithDefaultValues() throws Exception {
        assertEquals(AssetPackageMode.DEBUG, new RequestParameter(null, "debug").toEnum(AssetPackageMode.class, AssetPackageMode.PRODUCTION));
        assertEquals(AssetPackageMode.PRODUCTION, new RequestParameter(null, "").toEnum(AssetPackageMode.class, AssetPackageMode.PRODUCTION));
        assertEquals(AssetPackageMode.PRODUCTION, new RequestParameter(null, null).toEnum(AssetPackageMode.class, AssetPackageMode.PRODUCTION));
        assertEquals(AssetPackageMode.PRODUCTION, new RequestParameter(null, "foo").toEnum(AssetPackageMode.class, AssetPackageMode.PRODUCTION));
        assertEquals(AssetPackageMode.PRODUCTION,
            new RequestParameter(null, "debug").validateWith(ALWAYS_INVALID_VALIDATION).toEnum(AssetPackageMode.class, AssetPackageMode.PRODUCTION));
    }

    @Test
    public void testToEnumWithInvalidValues() throws Exception {
        try {
            new RequestParameter("mode", null).toEnum(AssetPackageMode.class);
        } catch (MissingRequestParameterException e) {
            assertEquals("mode", e.getParameterName());
        }

        try {
            new RequestParameter("mode", "").toEnum(AssetPackageMode.class);
        } catch (MissingRequestParameterException e) {
            assertEquals("mode", e.getParameterName());
        }

        try {
            new RequestParameter("mode", "foo").toEnum(AssetPackageMode.class);
        } catch (InvalidRequestParameterException e) {
            assertEquals("mode", e.getParameterName());
            assertEquals("foo", e.getParameterValue());
        }

        try {
            new RequestParameter("mode", "debug").validateWith(ALWAYS_INVALID_VALIDATION).toEnum(AssetPackageMode.class);
        } catch (InvalidRequestParameterException e) {
            assertEquals("mode", e.getParameterName());
            assertEquals("debug", e.getParameterValue());
        }
    }
}
