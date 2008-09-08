package com.fatwire.cs.rest.commands;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.XPath;

import com.fatwire.cs.rest.CommandException;
import com.fatwire.cs.rest.commands.GetAsset;
import com.fatwire.cs.rest.commands.SetAsset;

public class SetAssetTest extends AbstractCommandTest {

	public void testExecuteTemplate() throws CommandException,
			DocumentException {
		if (!doIntegrationTests)
			return;

		GetAsset command = new GetAsset(client, location);

		command.setAssetType("Template");
		command.setAssetId(this.target_Template);//

		byte[] asset = command.execute();
		assertTrue(asset != null && asset.length > 20);
		SetAsset setCommand = new SetAsset(client, location);

		setCommand.setAssetType("Template");
		setCommand.setAssetId(this.target_Template);//
		setCommand.setAssetData(asset);
		setCommand.setPublication(this.target_Site);
		byte[] xml = setCommand.execute();
		Document doc = this.createDocument(xml);
		final XPath xpathSelector = DocumentHelper
				.createXPath("/message/response");

		List results = xpathSelector.selectNodes(doc);
		if (results.size() == 0){
            System.out.println(doc.asXML());
			fail("asset not saved");
        }

	}
}
