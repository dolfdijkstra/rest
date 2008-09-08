package com.fatwire.cs.rest.commands;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.XPath;

import com.fatwire.cs.rest.CommandException;
import com.fatwire.cs.rest.commands.GetPublications;

public class GetPublicationsTest extends AbstractCommandTest {

	public void testExecute() throws CommandException, DocumentException {
		if (!doIntegrationTests)
			return;

		GetPublications command = new GetPublications(client, location);

		byte[] xml = command.execute();
		Document doc = this.createDocument(xml);
		final XPath xpathSelector = DocumentHelper
				.createXPath("/message/publications/publication");

		List results = xpathSelector.selectNodes(doc);
		if (results.size() == 0)
			fail("no publications");

	}

}
