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
