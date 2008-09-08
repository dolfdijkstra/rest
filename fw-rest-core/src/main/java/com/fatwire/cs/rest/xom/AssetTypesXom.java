package com.fatwire.cs.rest.xom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;

import com.fatwire.cs.rest.model.AssetType;

public class AssetTypesXom implements XOM<List<AssetType>> {

	private final XPath xpathSelector = DocumentHelper
			.createXPath("/assettypes/assettype");

	public AssetTypesXom() {
		super();
	}

	public List<AssetType> toObject(final Document doc) throws XOMException {
		final List<AssetType> l = new ArrayList<AssetType>();
		List nodes = xpathSelector.selectNodes(doc);
		for (final Iterator assetTypes = nodes.iterator(); assetTypes.hasNext();) {
			final Element p = (Element) assetTypes.next();
			final AssetType asset = new AssetType();
			asset.setName(p.elementText("name"));
			asset.setDescription(p.elementText("description"));
			final Iterator subTypes = p.elementIterator("subtype");
			if (subTypes != null) {
				while (subTypes.hasNext()) {
					final Element st = (Element) subTypes.next();
					asset.addSubType(st.getTextTrim());
				}
			}

			l.add(asset);
		}

		return l;
	}

}
