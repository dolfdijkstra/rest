package com.fatwire.cs.rest.sample;

import java.io.File;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.fatwire.cs.rest.model.Asset;

public class AssetComparatorTest extends TestCase {

    public void XXXtestCompareSameDoc() {
        AssetComparator comparator = new AssetComparator();

        File originalFile = new File("/tmp/.cside/CSElement/1232656189986.xml");
        File newAssetFile = new File("/tmp/.cside/CSElement/1232656189986.xml");

        try {
            Asset original = DomUtils.createAsset(originalFile);
            Asset asset = DomUtils.createAsset(newAssetFile);// xom.toObject(doc2);
            int val = comparator.compare(asset, original);
            Assert.assertTrue("Asset should be the same", val == 0);

        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

    }

    public void testCompare() {

        AssetComparator comparator = new AssetComparator();

        File originalFile = new File("/tmp/.cside/CSElement/1232656189986.xml");
        File newAssetFile = new File("/tmp/.cside/CSElement/1232656189986A.xml");
        try {
            Asset original = DomUtils.createAsset(originalFile);
            Asset asset = DomUtils.createAsset(newAssetFile);// xom.toObject(doc2);
            int val = comparator.compare(asset, original);
            Assert.assertTrue("Asset should not be the same", val != 0);

        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

    }

}
