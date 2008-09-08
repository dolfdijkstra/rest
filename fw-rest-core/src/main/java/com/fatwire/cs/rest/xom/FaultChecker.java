package com.fatwire.cs.rest.xom;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

import com.fatwire.cs.rest.remote.RemoteFaultException;
import com.fatwire.cs.rest.remote.RestServiceException;

public class FaultChecker {
	private final XPath xpathSelector = DocumentHelper
			.createXPath("/message/fault");

	public void checkForFaultMessage(byte[] xml) throws RestServiceException {
		final SAXReader xmlReader = new SAXReader();
		Document doc;
		try {
			doc = xmlReader.read(new ByteArrayInputStream(xml));
		} catch (DocumentException e) {
			throw new RestServiceException(e.getMessage(), e);
		}
		checkForFaultMessage(doc);
	}

	public void checkForFaultMessage(Document doc) throws RemoteFaultException {

		List results = xpathSelector.selectNodes(doc);
		if (results.size() == 0)
			return;
		Element fault = (Element) results.get(0);
		if (fault == null)
			throw new RemoteFaultException("-22000",
					"fault details was not in xml", "unknown");
		throw new RemoteFaultException(fault.attributeValue("code"), fault
				.attributeValue("string"), fault.attributeValue("actor"));

	}

}
