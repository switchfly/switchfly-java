package com.switchfly.compress.compressor;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import com.yahoo.platform.yui.compressor.CssCompressor;

public class YuiCssCompressor implements AssetCompressor {
    @Override
    public String compress(String source, String name) throws IOException {
        StringReader input = new StringReader(source);

        CssCompressor compressor = new CssCompressor(input);

        StringWriter out = new StringWriter();

        compressor.compress(out, -1);

        return out.toString();
    }
}
