package com.fatwire.cs.rest.xom;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;

import com.fatwire.cs.rest.model.AssetMetaData;

public class AssetMetaDataXomTest extends AbstractXomTest {

    public void testToObject() throws DocumentException, IOException,
            XOMException, ParseException {
        AssetMetaDataXom xom = new AssetMetaDataXom();
        Document doc = readTestDocument("assetdata.xml");
        List<AssetMetaData> assets = xom.toObject(doc);

        assertEquals(2, assets.size());

        AssetMetaData amd = assets.get(0);
        assertEquals("Template", amd.getAssetType());
        assertEquals(1121304726674L, amd.getId());
        final SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date date = formatter.parse("2008-07-28 15:16:57");
        assertEquals(date, amd.getLastUpdatedDate());

        amd = assets.get(1);
        assertEquals("Generates the top header, given the site and the current page's c and cid." ,amd.getDescription());

    }

}
