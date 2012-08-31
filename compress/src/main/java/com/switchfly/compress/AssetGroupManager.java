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