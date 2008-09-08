package com.fatwire.cs.rest.xom;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;

import com.fatwire.cs.rest.model.AssetType;
import com.fatwire.cs.rest.xom.AssetTypesXom;
import com.fatwire.cs.rest.xom.XOMException;

public class AssetTypesXomTest extends AbstractXomTest {

	public void testToObject() throws DocumentException, IOException,
			XOMException, ParseException {
		AssetTypesXom xom = new AssetTypesXom();
		Document doc = readTestDocument("assettypes.xml");
		List<AssetType> assets = xom.toObject(doc);

		assertEquals(43, assets.size());
			AssetType assetType = assets.get(1);
			assertEquals("Content_C", assetType.getName());
			assertEquals("Content", assetType.getDescription());
			assertEquals(3, assetType.getSubTypes().size());
	}

}
