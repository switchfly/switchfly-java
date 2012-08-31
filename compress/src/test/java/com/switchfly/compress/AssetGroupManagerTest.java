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
