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

@SuppressWarnings("TooBroadScope")
public class AssetGroupManagerTest {

    @Test()
    public void testAddAsset() throws Exception {

        AssetGroupManager assetGroupManager = new AssetGroupManager();

        AssetGroup fooGroup = assetGroupManager.createGroup("foo", AssetType.JAVASCRIPT);

        Asset asset1 = new Asset();
        asset1.setType(AssetType.JAVASCRIPT);
        asset1.setGroup("foo");

        assetGroupManager.addAsset(asset1);

        assertEquals(asset1, fooGroup.getAssets().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAssetNoGroup() throws Exception {

        AssetGroupManager assetGroupManager = new AssetGroupManager();

        assetGroupManager.createGroup("foo", AssetType.JAVASCRIPT);

        Asset asset1 = new Asset();
        asset1.setGroup("bar");

        assetGroupManager.addAsset(asset1);
    }
}
