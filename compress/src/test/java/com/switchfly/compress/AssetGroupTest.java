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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AssetGroupTest {

    @Test
    public void testGetExtension() throws Exception {
        AssetGroup cssGroup = new AssetGroup();
        assertEquals(null, cssGroup.getExtension());

        cssGroup.setType(AssetType.CSS);
        assertEquals("css", cssGroup.getExtension());
    }

    @Test
    public void testAddAsset() throws Exception {
        Asset asset1 = new Asset();
        asset1.setUrl("aaaa");

        Asset asset2 = new Asset();
        asset2.setUrl("bbb");

        Asset asset3 = new Asset();
        asset3.setUrl("bbb");

        AssetGroup assetGroup = new AssetGroup();
        assetGroup.addAsset(asset1);
        assetGroup.addAsset(asset2);
        assetGroup.addAsset(asset3);

        assertEquals(2, assetGroup.getAssets().size());
        assertEquals(asset1, assetGroup.getAssets().get(0));
        assertEquals(asset2, assetGroup.getAssets().get(1));
    }

    @Test()
    public void testAddValidAsset() throws Exception {
        Asset asset = new Asset();
        asset.setType(AssetType.JAVASCRIPT);

        AssetGroup assetGroup = new AssetGroup();
        assetGroup.setType(AssetType.JAVASCRIPT);
        assetGroup.addAsset(asset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidAsset() throws Exception {
        Asset asset = new Asset();
        asset.setType(AssetType.JAVASCRIPT);

        AssetGroup assetGroup = new AssetGroup();
        assetGroup.setType(AssetType.CSS);
        assetGroup.addAsset(asset);
    }
}
