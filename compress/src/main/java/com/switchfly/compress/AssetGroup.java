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
