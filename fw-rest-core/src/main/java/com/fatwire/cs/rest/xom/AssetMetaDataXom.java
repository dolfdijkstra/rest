package com.fatwire.cs.rest.xom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;

import com.fatwire.cs.rest.model.AssetMetaData;

public class AssetMetaDataXom implements XOM<List<AssetMetaData>> {

	private final XPath xpathSelector = DocumentHelper
			.createXPath("/assets/assetdata");
    
	public AssetMetaDataXom() {
		super();
	}

	public List<AssetMetaData> toObject(final Document doc) throws XOMException {
		final List<AssetMetaData> l = new ArrayList<AssetMetaData>();
		final SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		List nodes = xpathSelector.selectNodes(doc);
		try {
			for (final Iterator assets = nodes.iterator(); assets.hasNext();) {
				final Element p = (Element) assets.next();
				final AssetMetaData asset = new AssetMetaData();
				asset.setAssetType(p.elementText("assettype"));
				asset.setId(Long.parseLong(p.elementText("id")));

				asset.setUpdatedDate(formatter.parse(p
						.elementText("updateddate")));
                asset.setCreatedBy(p.elementText("createdby"));
                asset.setCreatedDate(formatter.parse(p
                        .elementText("createddate")));
                asset.setDescription(p.elementText("description"));
                asset.setName(p.elementText("name"));
                asset.setStatus(p.elementText("status"));
                asset.setUpdatedBy(p.elementText("updatedby"));
				l.add(asset);
			}
		} catch (ParseException e) {
			throw new XOMException(e.getMessage(), e);
		}

		return l;
	}

}
