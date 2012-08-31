package com.switchfly.compress.cli;

import java.io.File;
import java.io.FileInputStream;
import com.switchfly.compress.TestingUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CompressCLITest {

    private String _configuration;
    private File _inputLocation;
    private File _outputLocation;
    private File _outputFile;

    @Before
    public void setUp() throws Exception {

        _inputLocation = new File(getClass().getResource("asset_packages.yml").getFile()).getParentFile();

        _outputLocation = File.createTempFile("output_", "_TEST");
        if (_outputLocation.exists()) {
            _outputLocation.delete();
        }
        _outputLocation.mkdir();

        _outputFile = File.createTempFile("output_", "_compressed");
        FileUtils.deleteQuietly(_outputFile);

        _configuration = FileUtils.readFileToString(new File(getClass().getResource("asset_packages.yml").getFile()));
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.deleteDirectory(_outputLocation);
        FileUtils.deleteQuietly(_outputFile);
    }

    @Test
    public void testListOnly() throws Exception {

        CompressCLI compressCLI = new CompressCLI();

        compressCLI.setIsListOnly(true);
        compressCLI.setOutputPath(_outputLocation.getAbsolutePath());
        compressCLI.setConfigurationFromString(_configuration);
        compressCLI.setBaseInputPath(_inputLocation.getAbsolutePath());
        compressCLI.setJsURIFragment("/js");
        compressCLI.setCssURIFragment("/css");

        compressCLI.execute();

        File cssDir = new File(_outputLocation, "css");
        assertTrue("Did not create CSS dir for compressed files.", cssDir.exists());
        assertEquals(2, cssDir.listFiles().length);

        File jsDir = new File(_outputLocation, "js");
        assertTrue("Did not create JS dir for compressed files.", jsDir.exists());
        assertEquals(2, jsDir.listFiles().length);
    }

    @Test
    public void testCompress() throws Exception {

        CompressCLI compressCLI = new CompressCLI();
        compressCLI.setIsListOnly(false);
        compressCLI.setIsVerbose(false);
        compressCLI.setIsDebug(false);
        compressCLI.setLineBreakPos(-1);
        compressCLI.setObfuscate(true);
        compressCLI.setDisableMicroOptimizations(false);
        compressCLI.setPreserveUnecessarySemicolons(true);
        compressCLI.setOutputPath(_outputLocation.getAbsolutePath());
        compressCLI.setConfigurationFromString(_configuration);
        compressCLI.setBaseInputPath(_inputLocation.getAbsolutePath());
        compressCLI.setJsURIFragment("/js");
        compressCLI.setCssURIFragment("/css");

        compressCLI.execute();

        File cssDir = new File(_outputLocation, "css");
        assertTrue(cssDir.exists());
        assertEquals(4, cssDir.listFiles().length);

        File jsDir = new File(_outputLocation, "js");
        assertTrue(jsDir.exists());
        assertEquals(4, jsDir.listFiles().length);
    }

    @Test
    public void testMinifyAndObfuscateCss() throws Exception {
        File input = new File(getClass().getResource("input.css").getFile());

        CompressCLI compressCLI = new CompressCLI();
        compressCLI.minifyAndObfuscateFile(input, _outputFile, false);

        assertTrue(_outputFile.exists());

        String actual = IOUtils.toString(new FileInputStream(_outputFile));
        String expected = IOUtils.toString(getClass().getResourceAsStream("inputMinified.css")).trim();
        TestingUtil.assertEqualsIgnoreWhitespace(expected, actual.trim());
    }

    @Test
    public void testMinifyAndObfuscateJs() throws Exception {
        File input = new File(getClass().getResource("input.js").getFile());

        CompressCLI compressCLI = new CompressCLI();
        compressCLI.minifyAndObfuscateFile(input, _outputFile, true);

        assertTrue(_outputFile.exists());

        String actual = IOUtils.toString(new FileInputStream(_outputFile));
        String expected = IOUtils.toString(getClass().getResourceAsStream("inputMinified.js")).trim();

        TestingUtil.assertEqualsIgnoreWhitespace(expected, actual.trim());
    }
}
