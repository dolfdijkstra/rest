package com.fatwire.cs.rest.xom;

import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;

import com.fatwire.cs.rest.xom.PublicationIdXom;
import com.fatwire.cs.rest.xom.XOMException;

public class PublicationIdXomTest extends AbstractXomTest {

	public void testToObject() throws DocumentException, IOException,
			XOMException {
		PublicationIdXom xom = new PublicationIdXom();
		Document doc = readTestDocument("publication.xml");
		long l = xom.toObject(doc);
		assertEquals(1112198287026L, l);

	}

}
