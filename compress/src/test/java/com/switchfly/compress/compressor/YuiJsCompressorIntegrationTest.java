package com.switchfly.compress.compressor;

import com.switchfly.compress.TestingUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class YuiJsCompressorIntegrationTest {

    @Test
    public void testCompress() throws Exception {
        YuiJsCompressor packager = new YuiJsCompressor();

        String source = TestingUtil.readFile(getClass(), "uncompressed.js.txt");
        String expected = TestingUtil.readFile(getClass(), "YuiJsCompressorTest_compressed.js.txt");

        String compressed = packager.compress(source, "uncompressed.js.txt");

        assertEquals(expected, compressed);
    }
}