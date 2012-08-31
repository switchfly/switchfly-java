package com.switchfly.compress;

import org.jsoup.nodes.Element;

public enum AssetType {
    CSS("css", "link", "href", "text/css"),
    JAVASCRIPT("js", "script", "src", "text/javascript"),;

    private final String _extension;
    private final String _tagName;
    private final String _urlAttribute;
    private final String _contentType;

    private AssetType(String extension, String tagName, String urlAttribute, String contentType) {
        _extension = extension;
        _tagName = tagName;
        _urlAttribute = urlAttribute;
        _contentType = contentType;
    }

    public String getExtension() {
        return _extension;
    }

    public String getTagName() {
        return _tagName;
    }

    public String getUrlAttribute() {
        return _urlAttribute;
    }

    public String getContentType() {
        return _contentType;
    }

    public boolean isType(Element element) {
        return getTagName().equalsIgnoreCase(element.tagName()) && getContentType().equalsIgnoreCase(element.attr("type"));
    }

    public static AssetType valueOf(Element element) {
        if (JAVASCRIPT.isType(element)) {
            return JAVASCRIPT;
        } else if (CSS.isType(element)) {
            return CSS;
        } else {
            return null;
        }
    }
}