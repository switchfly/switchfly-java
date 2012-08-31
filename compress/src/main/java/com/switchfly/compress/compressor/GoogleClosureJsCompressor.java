package com.switchfly.compress.compressor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import com.google.javascript.jscomp.CommandLineRunner;
import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSSourceFile;

public class GoogleClosureJsCompressor implements AssetCompressor {
    @Override
    public String compress(String source, String name) throws IOException {
        Compiler compiler = new Compiler();

        CompilerOptions options = new CompilerOptions();

        CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);

        List<JSSourceFile> defaultExterns = CommandLineRunner.getDefaultExterns();

        JSSourceFile input = JSSourceFile.fromCode(name, source);

        compiler.compile(defaultExterns, Arrays.asList(input), options);

        return compiler.toSource();
    }
}
