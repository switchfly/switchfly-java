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
