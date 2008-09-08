package com.fatwire.cs.rest.xom;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;

public class PublicationIdXom implements XOM<Long> {
	private final XPath xpathSelector = DocumentHelper
			.createXPath("/message/id");

	public Long toObject(final Document doc) throws XOMException {
		List nodes = xpathSelector.selectNodes(doc);
		if (nodes == null)
			throw new XOMException(
					"no id element returned from ContentServer for GetPubId");
		if (nodes.size()==0)
			throw new XOMException(
					"no id returned from ContentServer for GetPubId");

		Element idElement = (Element)nodes.get(0);

		return Long.parseLong(idElement.getStringValue());

	}

}
