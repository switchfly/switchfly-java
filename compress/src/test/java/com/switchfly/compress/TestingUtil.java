package com.switchfly.compress;

import java.io.IOException;
import org.apache.commons.io.IOUtils;

import static org.junit.Assert.assertEquals;

public class TestingUtil {

    public static String readFile(Class clazz, String resource) throws IOException {
        return IOUtils.toString(clazz.getResourceAsStream(resource));
    }

    public static void assertEqualsIgnoreWhitespace(String expected, String actual) {
        if (normalizeWhiteSpace(expected).equals(normalizeWhiteSpace(actual))) {
            return;
        }
        assertEquals(expected, actual);
    }

    // Trim and replace any white space between characters, newlines, carriage returns, etc. with one space " \t ab c" => " ab c"
    // Good for maintaining space for XML documents, if you used equalsIgnoreWhiteSpace it would make "a href" => "ahref"
    public static String normalizeWhiteSpace(String s) {
        return s.replaceAll("\\s+", "").trim();
    }
}
