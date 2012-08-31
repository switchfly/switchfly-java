package com.switchfly.compress.compressor;

import java.io.IOException;

public interface AssetCompressor {

    String compress(String source, String name) throws IOException;
}
