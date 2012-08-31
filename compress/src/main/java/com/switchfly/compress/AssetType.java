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