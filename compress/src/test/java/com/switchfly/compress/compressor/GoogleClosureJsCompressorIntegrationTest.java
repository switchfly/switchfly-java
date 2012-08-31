package com.switchfly.compress.compressor;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.switchfly.compress.TestingUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GoogleClosureJsCompressorIntegrationTest {

    @Test
    public void testCompress() throws Exception {
        GoogleClosureJsCompressor packager = new GoogleClosureJsCompressor();
        // Disable logging for js compressor
        Logger.getLogger("com.google.javascript.jscomp").setLevel(Level.OFF);

        String source = TestingUtil.readFile(getClass(), "uncompressed.js.txt");
        String expected = TestingUtil.readFile(getClass(), "GoogleClosureJsCompressorTest_compressed.js.txt");

        String compressed = packager.compress(source, "uncompressed.js.txt");

        assertEquals(expected, compressed);
    }
}
