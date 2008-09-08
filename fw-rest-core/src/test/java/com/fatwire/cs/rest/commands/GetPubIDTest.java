package com.fatwire.cs.rest.commands;

import java.net.MalformedURLException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.XPath;

import com.fatwire.cs.rest.CommandException;
import com.fatwire.cs.rest.HttpCommandException;
import com.fatwire.cs.rest.commands.GetPubID;
import com.fatwire.cs.rest.remote.RemoteFaultException;
import com.fatwire.cs.rest.remote.RestServiceException;
import com.fatwire.cs.rest.support.DefaultCSLocation;
import com.fatwire.cs.rest.xom.FaultChecker;

public class GetPubIDTest extends AbstractCommandTest {

	public void testExecute() throws CommandException, DocumentException {
		if (!doIntegrationTests)
			return;

		GetPubID command = new GetPubID(client, location);

		command.setPubName(this.target_Site);

		byte[] xml = command.execute();
		Document doc = this.createDocument(xml);
		final XPath xpathSelector = DocumentHelper.createXPath("/message/id");

		List results = xpathSelector.selectNodes(doc);
		if (results.size() == 0)
			fail("no publication returned");

	}

	public void testExecuteWrongPort() throws MalformedURLException {
		if (!doIntegrationTests)
			return;
		
		client.getParams().setSoTimeout(1000);
		GetPubID command = new GetPubID(client, new DefaultCSLocation(
				"http://localhost:9876/cs/ContentServer"));

		command.setPubName("FirstSiteII");
		try {
			command.execute();
			fail();
		} catch (CommandException e) {
			// success
		}

	}

	public void testExecuteWrongCredentials() {
		if (!doIntegrationTests)
			return;

		client.getParams().setSoTimeout(1000);
		GetPubID command = new GetPubID(client,
				new DefaultCSLocation(csUrl,"wrong", "wrong") );

		command.setPubName(this.target_Site);
		try {
			command.execute();
			fail();
		} catch (HttpCommandException e) {
			// success
			assertEquals("expect status return code of 403", 403, e.getStatus());
		} catch (CommandException e) {
			fail();
		}

	}

	public void testExecuteInvalidSite() {
		if (!doIntegrationTests)
			return;

		GetPubID command = new GetPubID(client, location);

		command.setPubName("FirstSiteIII");
		try {
			byte[] xml = command.execute();
			FaultChecker faultChecker = new FaultChecker();
			faultChecker.checkForFaultMessage(xml);
		} catch (CommandException e) {
			fail(e.getMessage());
		} catch (RemoteFaultException e) {
			assertEquals("-10005", e.getErrorCode());
		} catch (RestServiceException e) {
			fail(e.getMessage());
		}

	}

}
