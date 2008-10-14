package com.fatwire.cs.rest.xom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;

import com.fatwire.cs.rest.model.Publication;

public class PublicationXom implements XOM<List<Publication>> {
	private final XPath xpathSelector = DocumentHelper
	.createXPath("/publications/publication");


	public List<Publication> toObject(final Document doc) throws XOMException {
		final List<Publication> l = new ArrayList<Publication>();
		List nodes = xpathSelector.selectNodes(doc);

		for (final Iterator pubs = nodes.iterator(); pubs.hasNext();) {
			final Element p = (Element) pubs.next();
			final Publication publication = new Publication();
			publication.setName(p.elementText("name"));
			publication.setDescription(p.elementText("description"));
			publication.setPubroot(p.elementText("pubroot"));
			publication.setPreview(p.elementText("cs_preview"));
			publication.setPrefix(p.elementText("cs_prefix"));
			publication.setPreviewAsset(p.elementText("cs_previewasset"));
		     
		        
		        
		        

			final String v = p.elementText("id");
			publication.setId(Long.parseLong(v));
			l.add(publication);
		}

		return l;
	}

}
