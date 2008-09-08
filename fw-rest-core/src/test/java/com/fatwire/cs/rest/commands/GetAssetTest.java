package com.fatwire.cs.rest.commands;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.XPath;

import com.fatwire.cs.rest.CommandException;
import com.fatwire.cs.rest.commands.GetAsset;

public class GetAssetTest extends AbstractCommandTest {

	public void testExecuteCSElement() throws CommandException,
			DocumentException {
		if (!doIntegrationTests)
			return;

		final GetAsset command = new GetAsset(client, location);

		command.setAssetType("CSElement");
		command.setAssetId(this.target_CSElement);

		final byte[] asset = command.execute();
		
		final Document doc = createDocument(asset);
		final XPath xpathSelector = DocumentHelper
				.createXPath("/document/asset");

		final List results = xpathSelector.selectNodes(doc);
		if (results.size() == 0) {
			fail("no asset returned");
		}

	}

	public void testExecuteTemplate() throws CommandException,
			DocumentException {
		if (!doIntegrationTests)
			return;

		final GetAsset command = new GetAsset(client, location);

		command.setAssetType("Template");
		command.setAssetId(this.target_Template);

		final byte[] asset = command.execute();
		final Document doc = createDocument(asset);
		final XPath xpathSelector = DocumentHelper
				.createXPath("/document/asset");

		final List results = xpathSelector.selectNodes(doc);
		if (results.size() == 0) {
			fail("no asset returned");
		}
	}

}
