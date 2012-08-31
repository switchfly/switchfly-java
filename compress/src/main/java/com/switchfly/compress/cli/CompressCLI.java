package com.switchfly.compress.cli;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Set;
import com.switchfly.compress.cli.configuration.IConfiguration;
import com.switchfly.compress.cli.configuration.YamlConfiguration;
import com.switchfly.compress.cli.exceptions.CompressorException;
import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import jargs.gnu.CmdLineParser;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

/* Usage of this tool is:
	-y ${PROJ.JS_COMMON_SRC}/asset_packages.yml	: configuration
	-o ${PROJ.JS_COMMON_TARGET}/packages		: output location
	-i ${PROJ.JS_COMMON_SRC}/			: input location (to resolve contents of yml)
	-u ${js_common_assets_url}			: the JS URI fragment (/js)
	-c ${js_common_assets_url}			: the CSS URI fragment (/css)
	-L						: include for Package(List-only). Leave out for compression
 */

public class CompressCLI {

    private String _baseInputPath;
    private IConfiguration _config;
    private String _cssURIFragment = "";    //JS loader will use this URI fragment to resolve CSS files
    private boolean _disableMicroOptimizations = false;
    private boolean _isListOnly = false;    //true will NOT compress
    private boolean _isVerbose = false;
    private boolean _isDebug = false;
    private String _jsURIFragment = "";     //JS loader will use this URI fragment to resolve JS files
    private int _lineBreakPos = -1;         //insert a line break at this pos. -1 = disable, 0 = after every statement
    private boolean _obfuscate = true;
    private String _outputPath;
    private boolean _preserveUnecessarySemicolons = true;

    private String _jsTemplate = "(function(){" + "var loader = function(name, files, asset_url){" + "var _files = files.split(',');" +
        "var require = function(libraryName) {" + "if (libraryName.search(/\\.js$/i) > 0) {" +
        "document.write('<script type=\"text/javascript\" src=\"'+libraryName+'\"></script>');" +
        "} else if (libraryName.search(/\\.css$/i) > 0 ) {" +
        "document.write('<link rel=\"stylesheet\" type=\"text/css\" href=\"'+libraryName+'\">');" + "}};" + "var include_files = function(path){" +
        "var root = (path.slice(0,2) === '//' || path.slice(0,4) === 'http')? '' : location.protocol + '//' + location.host;" +
        "if( path.charAt(path.length-1) !== '/') {" + "path+= '/';}" + "for(var i = 0, len = _files.length; i < len; i++){" +
        "require(root + path + _files[i]);}};" + "if ( asset_url.length === 0 ) { return;}" + "include_files( asset_url );};" +
        "loader('#{package_name}', '#{package_files}', '#{assets_url}');})();";

    public static void main(String[] args) {

        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option pathToCompressedOutput = parser.addStringOption('o', "output");
        CmdLineParser.Option pathToConfiguration = parser.addStringOption('y', "config");
        CmdLineParser.Option pathToInput = parser.addStringOption('i', "input");
        CmdLineParser.Option jsURIFragment = parser.addStringOption('u', "js-uri");
        CmdLineParser.Option cssURIFragment = parser.addStringOption('c', "css-uri");
        CmdLineParser.Option isList = parser.addBooleanOption('L', "list-only");
        CmdLineParser.Option isVerbose = parser.addBooleanOption('v', "verbose");
        CmdLineParser.Option isDebug = parser.addBooleanOption('d', "debug");

        try {
            parser.parse(args);
        } catch (CmdLineParser.OptionException e) {
            System.err.println(e.getMessage());
            printUsage();
            System.exit(1);
        }

        CompressCLI compressCLI = new CompressCLI();
        try {
            compressCLI.setOutputPath((String) parser.getOptionValue(pathToCompressedOutput));
            compressCLI.setConfigurationFromFile((String) parser.getOptionValue(pathToConfiguration));
            compressCLI.setBaseInputPath((String) parser.getOptionValue(pathToInput));
            compressCLI.setJsURIFragment((String) parser.getOptionValue(jsURIFragment));
            compressCLI.setCssURIFragment((String) parser.getOptionValue(cssURIFragment));
            compressCLI.setIsListOnly((Boolean) parser.getOptionValue(isList, Boolean.FALSE));
            compressCLI.setIsVerbose((Boolean) parser.getOptionValue(isVerbose, Boolean.FALSE));
            compressCLI.setIsDebug((Boolean) parser.getOptionValue(isDebug, Boolean.FALSE));

            compressCLI.execute();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void printUsage() {
        System.err.println(
            "Usage: Compressor [{-L}] [{-v}] [{-d}] [-y path-to-configuration ] [-o output] [-i input] [-u js-assets-uri] [-c css-assets-uri]");
    }

    public void setIsListOnly(boolean isListOnly) {
        this._isListOnly = isListOnly;
    }

    public void setIsVerbose(boolean isVerbose) {
        this._isVerbose = isVerbose;
    }

    public void setIsDebug(boolean isDebug) {
        this._isDebug = isDebug;
    }

    public void setLineBreakPos(int breakPos) {
        _lineBreakPos = breakPos;
    }

    public void setObfuscate(boolean obfuscate) {
        _obfuscate = obfuscate;
    }

    public void setDisableMicroOptimizations(boolean disableMicroOptimizations) {
        _disableMicroOptimizations = disableMicroOptimizations;
    }

    public void setPreserveUnecessarySemicolons(boolean preserveUnecessarySemicolons) {
        _preserveUnecessarySemicolons = preserveUnecessarySemicolons;
    }

    public void setOutputPath(String outputPath) {
        _outputPath = outputPath;
    }

    public void setConfigurationFromFile(String configurationPath) throws CompressorException, IOException {
        _config = new YamlConfiguration();
        _config.loadFile(configurationPath);
    }

    public void setConfigurationFromStream(InputStream in) {
        _config = new YamlConfiguration();
        _config.loadStream(in);
    }

    public void setConfigurationFromString(String in) {
        _config = new YamlConfiguration();
        _config.loadDocument(in);
    }

    public void setBaseInputPath(String baseInputPath) {
        _baseInputPath = baseInputPath;
    }

    public void setJsURIFragment(String fragment) {
        _jsURIFragment = fragment;
    }

    public void setCssURIFragment(String fragment) {
        _cssURIFragment = fragment;
    }

    public void execute() throws CompressorException, IOException {

        if (_config == null) {
            throw new CompressorException("No configuration specified.");
        }

        if (_outputPath == null) {
            throw new CompressorException("No path to output location.");
        }

        File outputDir = new File(_outputPath);
        if (outputDir.exists() && !outputDir.isDirectory()) {
            throw new CompressorException("Output is not a directory: " + _outputPath);
        }

        if (_baseInputPath == null) {
            throw new CompressorException("No path to input location.");
        }

        File inputDir = new File(_baseInputPath);
        if (inputDir.exists() && !inputDir.isDirectory()) {
            throw new CompressorException("Input is not a directory: " + _baseInputPath);
        }

        Set<String> jsPackages = _config.getJsPackages();
        for (String pkg : jsPackages) {
            if (_isListOnly) {
                processList(pkg, _config.getFilesForJsPackage(pkg), inputDir, outputDir, true);
            } else {
                processCompress(pkg, _config.getFilesForJsPackage(pkg), inputDir, outputDir, true);
            }
        }

        Set<String> cssPackages = _config.getCSSPackages();
        for (String pkg : cssPackages) {
            if (_isListOnly) {
                processList(pkg, _config.getFilesForCssPackage(pkg), inputDir, outputDir, false);
            } else {
                processCompress(pkg, _config.getFilesForCssPackage(pkg), inputDir, outputDir, false);
            }
        }
    }

    protected void processCompress(String packageNamePrefix, List<String> files, File inputDir, File baseOutputDir, boolean isJs) throws IOException {
        if (files.size() <= 0) {
            return;
        }

        File outputDir = new File(baseOutputDir, "css");
        if (isJs) {
            outputDir = new File(baseOutputDir, "js");
        }

        if (!outputDir.exists()) {
            outputDir.mkdirs();
            outputDir.deleteOnExit();
        }

        File target = new File(outputDir, packageNamePrefix + "-pkg.tmp");

        StringBuffer buffer = new StringBuffer();
        boolean first = true;
        for (String path : files) {
            File file = new File(inputDir, path);
            if (file.exists()) {
                if (!first) {
                    buffer.append(",");
                }
                first = false;
                buffer.append(path);
                try {
                    copyfile(file, target, !_isListOnly);
                } catch (IOException e) {
                    System.err.println("Unable to copy file: " + file.getAbsolutePath());
                    throw e;
                }
            }
        }

        String ext = ".js";
        String assetUri = _jsURIFragment;
        if (!isJs) {
            ext = ".css";
            assetUri = _cssURIFragment;
        }

        File processedTarget = new File(outputDir, packageNamePrefix + "-pkg" + ext);

        minifyAndObfuscateFile(target, processedTarget, isJs);

        String pkgContents = getContentsForJsPackage(packageNamePrefix + "-pkg-list", buffer.toString(), assetUri);

        File pkg = new File(outputDir, packageNamePrefix + "-pkg-list.js");

        writePackageFile(pkg, pkgContents);
        target.delete();
    }

    /**
     * Wrapper around the YUI Compressor to minify & obfuscate JS/CSS files
     *
     * @param input  - File Input file to minify & obfuscate
     * @param output - File Output
     * @param isJs   - boolean, is the file a JS file
     */
    public void minifyAndObfuscateFile(File input, File output, boolean isJs) throws EvaluatorException, IOException {

        final String fileName = input.getCanonicalPath();
        InputStreamReader in = new InputStreamReader(new FileInputStream(input), "UTF-8");
        OutputStreamWriter out = null;
        try {
            if (isJs) {

                JavaScriptCompressor compressor = new JavaScriptCompressor(in, new ErrorReporter() {

                    public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
                        if (line < 0) {
                            System.err.println("\n[WARNING] " + fileName + ':' + message);
                        } else {
                            System.err.println("\n[WARNING] " + fileName + ':' + line + ':' + lineOffset + ':' + message);
                        }
                    }

                    public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
                        if (line < 0) {
                            System.err.println("\n[ERROR] " + fileName + ':' + message);
                        } else {
                            System.err.println("\n[ERROR] " + fileName + ':' + line + ':' + lineOffset + ':' + message);
                        }
                    }

                    public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
                        error(message, sourceName, line, lineSource, lineOffset);
                        return new EvaluatorException(message);
                    }
                });

                in.close();
                in = null;
                out = new OutputStreamWriter(new FileOutputStream(output), "UTF-8");
                compressor.compress(out, _lineBreakPos, _obfuscate, _isDebug, _preserveUnecessarySemicolons, _disableMicroOptimizations);
            } else {
                CssCompressor compressor = new com.yahoo.platform.yui.compressor.CssCompressor(in);
                in.close();
                in = null;
                out = new OutputStreamWriter(new FileOutputStream(output), "UTF-8");
                compressor.compress(out, _lineBreakPos);
            }
        } finally {
            if (in != null) {
                in.close();
            }

            if (out != null) {
                out.close();
            }
        }
    }

    private void processList(String packageNamePrefix, List<String> files, File inputDir, File baseOutputDir, boolean isJs) throws IOException {

        if (_isVerbose) {
            System.out.println("List-Only: Processing package: " + packageNamePrefix);
        }

        if (files.size() <= 0) {
            return;
        }

        File outputDir = new File(baseOutputDir, "css");
        if (isJs) {
            outputDir = new File(baseOutputDir, "js");
        }

        if (!outputDir.exists()) {
            outputDir.mkdirs();
            outputDir.deleteOnExit();
        }

        StringBuffer fileList = new StringBuffer();
        boolean isFirst = true;
        for (String filename : files) {
            File file = new File(inputDir, filename);
            if (file.exists()) {
                if (!isFirst) {
                    fileList.append(",");
                }
                isFirst = false;
                fileList.append(filename);
            }
        }

        String assetUri = _jsURIFragment;
        if (!isJs) {
            assetUri = _cssURIFragment;
        }

        String pkgContents = getContentsForJsPackage(packageNamePrefix + "-pkg-list", fileList.toString(), assetUri);

        File pkg = new File(outputDir, packageNamePrefix + "-pkg-list.js");
        writePackageFile(pkg, pkgContents);
    }

    private void writePackageFile(File pkg, String contents) throws IOException {
        BufferedWriter out = null;
        FileWriter fstream = null;
        try {
            fstream = new FileWriter(pkg);
            out = new BufferedWriter(fstream);
            out.write(contents);
            out.flush();
            out.close();
            fstream.close();
        } catch (IOException e) {
            System.err.println("Unable to write file: " + pkg.getAbsolutePath());
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }

            if (fstream != null) {
                fstream.close();
            }
        }
    }

    private void copyfile(File source, File target, boolean append) throws IOException {

        if (_isVerbose) {
            System.out.println("Processing file: " + source.getAbsolutePath() + " to: " + target.getAbsolutePath());
        }

        if (!target.exists()) {
            target.getParentFile().mkdirs();
        }

        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(source);

            out = new FileOutputStream(target, append);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage() + " in the specified directory.");
        } finally {
            if (in != null) {
                in.close();
            }

            if (out != null) {
                out.close();
            }
        }
    }

    private String getContentsForJsPackage(String packageName, String fileJsArray, String assetUri) {
        String contents = _jsTemplate.replace("#{package_name}", packageName);
        contents = contents.replace("#{package_files}", fileJsArray);
        contents = contents.replace("#{assets_url}", assetUri);

        return contents;
    }
}
