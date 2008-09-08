package com.fatwire.cs.rest.service;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.fatwire.cs.rest.model.AssetMetaData;
import com.fatwire.cs.rest.model.Publication;
import com.fatwire.cs.rest.remote.RestServiceException;
import com.fatwire.cs.rest.support.DefaultCSLocation;
import com.fatwire.cs.rest.util.ConnectionManagerFactory;

public class RestServiceTest extends TestCase {
    public static final String TEST_PROPERTIES_NAME = "/test.properties";

    protected URL url;

    protected String target_Site;

    protected long target_pubid;

    protected long target_CSElement;

    protected long target_Template;

    protected String username;

    protected String password;

    protected boolean doIntegrationTests = false;

    DefaultRestService service;

    private MultiThreadedHttpConnectionManager connectionManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InputStream in = getClass().getResourceAsStream(TEST_PROPERTIES_NAME);
        Properties p = new Properties();
        p.load(in);
        in.close();
        url = new URL(p.getProperty("ws.url"));

        username = p.getProperty("ws.username");
        password = p.getProperty("ws.password");
        this.target_pubid = Long.parseLong(p.getProperty("ws.site.pubid"));
        this.target_CSElement = Long.parseLong(p
                .getProperty("ws.site.CSElement.id"));
        this.target_Template = Long.parseLong(p
                .getProperty("ws.site.Template.id"));
        this.target_Site = p.getProperty("ws.site.name");
        doIntegrationTests = "true".equals(p
                .getProperty("ws.dointegrationtest"));
        if (doIntegrationTests) {
            this.connectionManager = ConnectionManagerFactory
                    .getConnectionManager();
            service = this.getRemoteService();
        }

    }

    @Override
    protected void tearDown() throws Exception {
        if (doIntegrationTests) {
            connectionManager.shutdown();
        }
        super.tearDown();
    }

    public DefaultRestService getRemoteService() throws RestServiceException {

        return new DefaultRestService(connectionManager, new DefaultCSLocation(
                url, username, password));

    }

    public void testGetAssetMetaData() throws RestServiceException {
        if (!doIntegrationTests)
            return;
        Publication p = new Publication();
        p.setId(this.target_pubid);
        Iterable<AssetMetaData> assets = service
                .getAssetMetaData(p.getName(), "Template");
        assertTrue(assets.iterator().hasNext());
    }

    public void testGetAssetData_Page() throws RestServiceException {
        if (!doIntegrationTests)
            return;
        Publication p = new Publication();
        p.setId(this.target_pubid);

        Iterable<AssetMetaData> assets = service.getAssetMetaData(p.getName(), "Page");
        assertTrue(assets.iterator().hasNext());
    }

    public void testGetPubID() throws RestServiceException {
        if (!doIntegrationTests)
            return;

        long pubid = service.getPubID(this.target_Site);
        assertEquals(this.target_pubid, pubid);
    }

    public void testGetPublications() throws RestServiceException {
        if (!doIntegrationTests)
            return;

        Iterable<Publication> pubs = service.getPublications();
        assertTrue(pubs.iterator().hasNext());
    }

    public void testGetAsset() throws RestServiceException, DocumentException {
        if (!doIntegrationTests)
            return;

        InputStream in = service.getAssetAsStream("Template", this.target_Template);
        final SAXReader xmlReader = new SAXReader();
        Document doc;
        doc = xmlReader.read(in);
        List nodes = doc.selectNodes("/document/asset");
        assertEquals(1, nodes.size());

    }

}
