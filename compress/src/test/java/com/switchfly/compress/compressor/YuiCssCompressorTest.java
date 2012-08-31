package com.switchfly.compress.compressor;

import com.switchfly.compress.TestingUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class YuiCssCompressorTest {
    @Test
    public void testCompress() throws Exception {

        String uncompressed = TestingUtil.readFile(getClass(), "uncompressed.css.txt");
        String expected = TestingUtil.readFile(getClass(), "YuiCssCompressorTest_compressed.css.txt");

        YuiCssCompressor packager = new YuiCssCompressor();

        String compressed = packager.compress(uncompressed, "uncompressed.css.txt");
        assertEquals(expected, compressed);
    }
}
