package com.fatwire.cs.rest.xom;

import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;

import com.fatwire.cs.rest.model.Publication;
import com.fatwire.cs.rest.xom.PublicationXom;
import com.fatwire.cs.rest.xom.XOMException;

public class PublicationXomTest extends AbstractXomTest {

	public void testToObject() throws DocumentException, IOException,
			XOMException {
		PublicationXom xom = new PublicationXom();
		Document doc = readTestDocument("publications.xml");
		List<Publication> pubs = xom.toObject(doc);
		assertEquals(1,pubs.size());
		for (Publication pub : pubs) {
			assertEquals(1112198287026L, pub.getId());
			assertEquals("FirstSiteII", pub.getName());
			assertEquals("FirstSite Mark II", pub.getDescription());

		}

	}

}
