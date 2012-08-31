package com.switchfly.compress.cli.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.yaml.snakeyaml.Yaml;

public class YamlConfiguration implements IConfiguration {

    private Map<String, List<String>> _cssMap = new HashMap<String, List<String>>();
    private Map<String, List<String>> _jsMap = new HashMap<String, List<String>>();

    @Override
    public Set<String> getCSSPackages() {
        return _cssMap.keySet();
    }

    @Override
    public List<String> getFilesForCssPackage(String cssPackage) {
        if (_cssMap.containsKey(cssPackage)) {
            return _cssMap.get(cssPackage);
        }
        return Collections.emptyList();
    }

    @Override
    public Set<String> getJsPackages() {
        return _jsMap.keySet();
    }

    @Override
    public List<String> getFilesForJsPackage(String jsPackage) {
        if (_jsMap.containsKey(jsPackage)) {
            return _jsMap.get(jsPackage);
        }
        return Collections.emptyList();
    }

    @Override
    public void loadFile(String path) throws IOException {
        InputStream input = null;
        try {
            input = new FileInputStream(new File(path));
            loadStream(input);
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    @Override
    public void loadDocument(String document) {
        Yaml yaml = new Yaml();
        processData(yaml.load(document));
    }

    @Override
    public void loadStream(InputStream stream) {
        Yaml yaml = new Yaml();
        processData(yaml.load(stream));
    }

    private boolean isDocumentValid(Object document) {
        if (!(document instanceof Map)) {
            return false;
        }

        //NOTE: we dont attempt to resolve element types. If file format is not as expected we let the app blow up
        return true;
    }

    @SuppressWarnings("unchecked")
    private void processData(Object document) {

        if (!isDocumentValid(document)) {
            return;
        }

        Map<String, List<LinkedHashMap<String, Vector<String>>>> data = (Map<String, List<LinkedHashMap<String, Vector<String>>>>) document;

        if (data.containsKey("javascript")) {
            populateFileMapFromStructure(_jsMap, data.get("javascript"), ".js");
        }

        if (data.containsKey("stylesheet")) {
            populateFileMapFromStructure(_cssMap, data.get("stylesheet"), ".css");
        }
    }

    private void populateFileMapFromStructure(Map<String, List<String>> map, List<LinkedHashMap<String, Vector<String>>> value,
        String fileExtension) {

        for (LinkedHashMap<String, Vector<String>> fileSet : value) {
            for (String key : fileSet.keySet()) {
                ArrayList<String> files = new ArrayList<String>();
                List<String> valueFiles = fileSet.get(key);
                for (String aFile : valueFiles) {
                    files.add(aFile + fileExtension);
                }
                map.put(key, files);
            }
        }
    }
}
