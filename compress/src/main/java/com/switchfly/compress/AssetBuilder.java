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

public class AssetBuilder {

    public Asset create(Element element) {
        if (!element.hasAttr("src") && !element.hasAttr("href") && !element.hasAttr("data-package")) {
            return null;
        }
        Asset asset = new Asset();
        asset.setType(AssetType.valueOf(element));
        if (element.hasAttr("src")) {
            asset.setUrl(element.attr("src"));
        }
        if (element.hasAttr("href")) {
            asset.setUrl(element.attr("href"));
        }
        asset.setGroup(element.attr("data-package"));
        return asset;
    }
}
