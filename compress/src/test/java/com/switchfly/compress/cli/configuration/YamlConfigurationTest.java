package com.switchfly.compress.cli.configuration;

import java.io.FileNotFoundException;
import org.junit.Test;

import static org.junit.Assert.*;

public class YamlConfigurationTest {

    @Test
    public void testLoadConfig() {
        YamlConfiguration config = new YamlConfiguration();

        try {
            config.loadFile("NonExistent file");
            fail("Should fail on noexistent file.");
        } catch (Exception e) {
            assertTrue((e instanceof FileNotFoundException));
        }

        StringBuilder document = new StringBuilder();
        document.append("");
        document.append("---\n");
        document.append("javascript:\n");
        document.append("- externals:\n");
        document.append("  - log4javascript/log4javascript-1.3.1/log4javascript_uncompressed\n");
        document.append("- gmaps:\n");
        document.append("  - gmaps/labeledmarker\n");
        document.append("  - gmaps/extinfowindow\n");
        document.append("  - ezrez/js/ezrez-ui-gmap\n");
        document.append("- ezrez-lib-all:\n");
        document.append("  - ezrez/js/ezrez\n");
        document.append("  - ezrez/js/ezrez-util\n");
        document.append("  - ezrez/js/ezrez-ui\n");
        document.append("  - ezrez/js/ezrez-ui-gmap\n");
        document.append("  - ezrez/js/ezrez-app\n");
        document.append("  - ezrez/js/ezrez-webapi\n");
        document.append("  - ezrez/js/include\n");
        document.append("  - ezrez/js/show_details\n");
        document.append("  - ezrez/js/show_layer\n");
        document.append("stylesheet:\n");
        document.append("- externals-webapp:\n");
        document.append("  - yui/2.5.1/build/container/assets/container\n");
        document.append("  - yui/2.5.1/build/calendar/assets/skins/sam/calendar\n");
        document.append("  - gmaps/extinfowindow_default\n");
        document.append("  - prototype/protomultiselect/facebook\n");
        document.append("- ezrez-base:\n");
        document.append("  - ezrez/css/ezReset\n");
        document.append("  - ezrez/css/ezBase\n");
        document.append("  - ezrez/css/ez3col\n");
        document.append("  - ezrez/css/calendar\n");
        document.append("  - ezrez/css/lightbox\n");
        document.append("  - ezrez/css/dialog\n");
        document.append("  - ezrez/css/dualSlider\n");
        document.append("  - ezrez/css/detailPricingMatrix\n");

        config.loadDocument(document.toString());

        assertEquals(2, config.getCSSPackages().size());
        assertNotNull(config.getFilesForCssPackage("externals-webapp"));
        assertEquals(0, config.getFilesForCssPackage("externals-webapp2").size());
        assertTrue(config.getFilesForCssPackage("externals-webapp").contains("gmaps/extinfowindow_default.css"));

        assertEquals(3, config.getJsPackages().size());
        assertNotNull(config.getFilesForJsPackage("ezrez-lib-all"));
        assertEquals(0, config.getFilesForJsPackage("ezrez-lib-all2").size());
        assertTrue(config.getFilesForJsPackage("ezrez-lib-all").contains("ezrez/js/ezrez-webapi.js"));
    }
}
