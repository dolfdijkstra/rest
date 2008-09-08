package com.fatwire.cs.rest.commands;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.fatwire.cs.rest.model.CSLocation;
import com.fatwire.cs.rest.support.DefaultCSLocation;

public abstract class AbstractCommandTest extends TestCase {
	public static final String TEST_PROPERTIES_NAME = "/test.properties";

	protected HttpClient client;

	protected CSLocation location;

	protected URL csUrl;

	protected String target_Site;

	protected long target_pubid;

	protected long target_CSElement;

	protected long target_Template;

	protected boolean doIntegrationTests = false;

	public AbstractCommandTest() {
		super();
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		InputStream in = getClass().getResourceAsStream(TEST_PROPERTIES_NAME);
		Properties p = new Properties();
		p.load(in);
		in.close();
		csUrl = new URL(p.getProperty("ws.url"));

		String username = p.getProperty("ws.username");
		String password = p.getProperty("ws.password");
		this.target_pubid = Long.parseLong(p.getProperty("ws.site.pubid"));
		this.target_CSElement = Long.parseLong(p
				.getProperty("ws.site.CSElement.id"));
		this.target_Template = Long.parseLong(p
				.getProperty("ws.site.Template.id"));
		this.target_Site = p.getProperty("ws.site.name");

		doIntegrationTests = "true".equals(p
				.getProperty("ws.dointegrationtest"));
		if (doIntegrationTests) {
			client = new HttpClient();
            location = new DefaultCSLocation(csUrl,username, password);
			
		}

	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	protected Document createDocument(final byte[] xml)
			throws DocumentException {

		final SAXReader xmlReader = new SAXReader();
		Document doc;
		doc = xmlReader.read(new ByteArrayInputStream(xml));

		return doc;
	}

}