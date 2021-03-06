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

package com.switchfly.compress.cli.task;

import java.io.IOException;
import com.switchfly.compress.cli.CompressCLI;
import com.switchfly.compress.cli.exceptions.CompressorException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class CompressorTask extends Task {
    private CompressCLI _compressCLI;

    public CompressorTask() {
        _compressCLI = new CompressCLI();
    }

    public void setIsListOnly(boolean isListOnly) {
        _compressCLI.setIsListOnly(isListOnly);
    }

    public void setIsVerbose(boolean isVerbose) {
        _compressCLI.setIsVerbose(isVerbose);
    }

    public void setLineBreakPos(int breakPos) {
        _compressCLI.setLineBreakPos(breakPos);
    }

    public void setObfuscate(boolean obfuscate) {
        _compressCLI.setObfuscate(obfuscate);
    }

    public void setDisableMicroOptimizations(boolean disableMicroOptimizations) {
        _compressCLI.setDisableMicroOptimizations(disableMicroOptimizations);
    }

    public void setPreserveSemicolons(boolean preserveSemicolons) {
        _compressCLI.setPreserveUnecessarySemicolons(preserveSemicolons);
    }

    public void setOutputPath(String outputPath) {
        _compressCLI.setOutputPath(outputPath);
    }

    public void setConfigurationPath(String configurationPath) throws IOException, CompressorException {
        _compressCLI.setConfigurationFromFile(configurationPath);
    }

    public void setBaseInputPath(String baseInputPath) {
        _compressCLI.setBaseInputPath(baseInputPath);
    }

    public void setJsURIFragment(String fragment) {
        _compressCLI.setJsURIFragment(fragment);
    }

    public void setCssURIFragment(String fragment) {
        _compressCLI.setCssURIFragment(fragment);
    }

    public void execute() {
        try {
            _compressCLI.execute();
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }
}
