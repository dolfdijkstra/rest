package com.fatwire.cs.rest.commands;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.XPath;

import com.fatwire.cs.rest.CommandException;
import com.fatwire.cs.rest.commands.GetAssetData;

public class GetAssetDataTest extends AbstractCommandTest {

    public void testExecuteMethodCSElement() throws CommandException,
            DocumentException {
        if (!doIntegrationTests)
            return;

        GetAssetData command = new GetAssetData(client, location);
        command.setPublication(this.target_Site);
        command.setAssetType("CSElement");

        byte[] xml = command.execute();

        Document doc = this.createDocument(xml);
        final XPath xpathSelector = DocumentHelper
                .createXPath("/message/assets/assetdata");

        List results = xpathSelector.selectNodes(doc);
        if (results.size() == 0)
            fail("no assetData returned");

    }

    public void testExecuteMethodTemplate() throws CommandException,
            DocumentException {
        if (!doIntegrationTests)
            return;

        GetAssetData command = new GetAssetData(client, location);
        command.setPublication(this.target_Site);
        command.setAssetType("Template");

        byte[] xml = command.execute();
        Document doc = this.createDocument(xml);
        final XPath xpathSelector = DocumentHelper
                .createXPath("/message/assets/assetdata");

        List results = xpathSelector.selectNodes(doc);
        if (results.size() == 0)
            fail("no asset returned");

    }

}
