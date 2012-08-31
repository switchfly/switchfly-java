package com.switchfly.compress.cli.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

public interface IConfiguration {

    void loadFile(String path) throws IOException;

    void loadDocument(String document);

    void loadStream(InputStream stream);

    Set<String> getJsPackages();

    Set<String> getCSSPackages();

    List<String> getFilesForJsPackage(String jsPackage);

    List<String> getFilesForCssPackage(String cssPackage);
}
