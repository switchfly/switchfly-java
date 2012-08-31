package com.switchfly.compress;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class AssetGroupManager {

    private final LinkedHashMap<String, AssetGroup> _groups = new LinkedHashMap<String, AssetGroup>();

    public AssetGroupManager() {
    }

    public AssetGroup createGroup(String name, AssetType type) {
        AssetGroup assetGroup = new AssetGroup();
        assetGroup.setType(type);
        assetGroup.setName(name);
        _groups.put(name + "-" + type, assetGroup);
        return assetGroup;
    }

    public List<AssetGroup> getGroups() {
        return new ArrayList<AssetGroup>(_groups.values());
    }

    public void addAsset(Asset asset) {
        AssetGroup group = _groups.get(asset.getGroup() + "-" + asset.getType());
        if (group == null) {
            throw new IllegalArgumentException("AssetGroup does not exist for asset " + asset);
        }
        group.addAsset(asset);
    }
}