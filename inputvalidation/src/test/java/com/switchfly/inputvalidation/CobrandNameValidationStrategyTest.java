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
import org.apache.commons.collections.Closure;
import org.junit.Test;

import static org.junit.Assert.*;

public class CobrandNameValidationStrategyTest {

    @Test
    public void testCleanStrategy() throws Exception {
        assertEquals("", ValidationStrategy.COBRAND_NAME.cleanStrategy().validate(""));
        assertEquals(" ", ValidationStrategy.COBRAND_NAME.cleanStrategy().validate(" "));
        assertEquals(null, ValidationStrategy.COBRAND_NAME.cleanStrategy().validate(null));
        assertEquals("foo-Bar_123üaàé", ValidationStrategy.COBRAND_NAME.cleanStrategy().validate("foo-Bar_123üaàé"));
        assertEquals("etcpasswd", ValidationStrategy.COBRAND_NAME.cleanStrategy().validate("../../../../../etc/passwd%00"));
        assertEquals("etcpasswd00foo", ValidationStrategy.COBRAND_NAME.cleanStrategy().validate("../../../../../etc/passwd%00%foo"));
        assertEquals("default00", ValidationStrategy.COBRAND_NAME.cleanStrategy().validate("default%00%<script>alert('hacked')</script>"));
    }

    @Test
    public void testValidateStrategy() throws Exception {
        assertEquals("", ValidationStrategy.COBRAND_NAME.validateStrategy().validate(""));
        assertEquals(" ", ValidationStrategy.COBRAND_NAME.validateStrategy().validate(" "));
        assertEquals(null, ValidationStrategy.COBRAND_NAME.validateStrategy().validate(null));
        assertEquals("foo-Bar_123", ValidationStrategy.COBRAND_NAME.validateStrategy().validate("foo-Bar_123"));

        assertThrowException(ValidatorException.class, new Closure() {
            @Override
            public void execute(Object input) {
                ValidationStrategy.COBRAND_NAME.validateStrategy().validate("../../../../../etc/passwd%00");
            }
        });

        assertThrowException(ValidatorException.class, new Closure() {
            @Override
            public void execute(Object input) {
                ValidationStrategy.COBRAND_NAME.validateStrategy().validate("../../../../../etc/passwd%00");
            }
        });

        assertThrowException(ValidatorException.class, new Closure() {
            @Override
            public void execute(Object input) {
                ValidationStrategy.COBRAND_NAME.validateStrategy().validate("../../../../../etc/passwd%00foo");
            }
        });

        assertThrowException(ValidatorException.class, new Closure() {
            @Override
            public void execute(Object input) {
                ValidationStrategy.COBRAND_NAME.validateStrategy().validate("default%00%<script>alert('hacked')</script>");
            }
        });
    }

    public static void assertThrowException(Class<? extends Throwable> exceptionClass, Closure closure) {
        try {
            closure.execute(null);
            fail("Should throw " + exceptionClass.getName());
        } catch (Exception e) {
            assertTrue(exceptionClass.isAssignableFrom(e.getClass()));
        }
    }
}
