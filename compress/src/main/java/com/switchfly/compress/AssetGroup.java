package com.switchfly.compress;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

public class AssetGroup {

    private final LinkedHashSet<Asset> _assets = new LinkedHashSet<Asset>();

    private String _name;
    private String _url;
    private AssetType _type;

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getUrl() {
        return _url;
    }

    public void setUrl(String url) {
        _url = url;
    }

    public AssetType getType() {
        return _type;
    }

    public void setType(AssetType type) {
        _type = type;
    }

    public void addAsset(Asset asset) {
        if (_type != asset.getType()) {
            throw new IllegalArgumentException("Invalid asset type: " + toString() + ", " + asset);
        }
        _assets.add(asset);
    }

    public List<Asset> getAssets() {
        return new ArrayList<Asset>(_assets);
    }

    public String getExtension() {
        return _type != null ? _type.getExtension() : null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
            append("name", getName()).
            append("type", getType()).
            toString();
    }
}
