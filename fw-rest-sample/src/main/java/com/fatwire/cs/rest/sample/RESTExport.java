package com.fatwire.cs.rest.sample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.fatwire.cs.rest.model.Asset;
import com.fatwire.cs.rest.model.AssetMetaData;
import com.fatwire.cs.rest.model.AssetType;
import com.fatwire.cs.rest.model.Attribute;
import com.fatwire.cs.rest.model.CSLocation;
import com.fatwire.cs.rest.model.FileField;
import com.fatwire.cs.rest.model.Publication;
import com.fatwire.cs.rest.remote.RestService;
import com.fatwire.cs.rest.service.DefaultRestService;
import com.fatwire.cs.rest.support.DefaultCSLocation;
import com.fatwire.cs.rest.xom.AssetXom;

/**
 * Sample code on how to use the REST services.
 * 
 * 
 * @author Dolf.Dijkstra
 * 
 */

public class RESTExport {
    protected static final Log log = LogFactory.getLog(RESTExport.class);

    CSLocation location;

    HttpConnectionManager getConnectionManager() {
        MultiThreadedHttpConnectionManager connectionManager;
        connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setConnectionTimeout(5000);
        connectionManager.getParams().setMaxTotalConnections(20);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(20);

        return connectionManager;
    }

    public void downLoadAll(Set<String> pubs, Set<String> assettypes)
            throws Exception {
        /*
         * TODO: handle exceptions better
         * 
         */

        final HttpConnectionManager connectionManager = getConnectionManager();

        final RestService restService = new DefaultRestService(
                connectionManager, location);
        long t = System.currentTimeMillis();
        int count = 0;
        boolean doAssets = true;
        AssetXom xom = new AssetXom();
        try {
            //get all sites from ContentServer
            for (final Publication pub : restService.getPublications()) {
                if (pubs.contains(pub.getName())) {
                    System.out.println(pub.getDescription());
                    System.out.println("prefix: " + pub.getPrefix());
                    // get all assetTypes for this site
                    for (final AssetType assetType : restService
                            .getAssetTypes(pub.getName())) {
                        if (assettypes.contains(assetType.getName())) {
                            //get all asset meta data for this assettype and site
                            System.out.println(assetType.getDescription());
                            for (final AssetMetaData amd : restService
                                    .getAssetMetaData(pub.getName(), assetType
                                            .getName())) {
                                System.out.println(amd.getAssetType() + " "
                                        + amd.getId() + " " + amd.getName());
                                if (doAssets) {
                                    try {
                                        //get the actual asset
                                        InputStream stream = restService
                                                .getAssetAsStream(amd
                                                        .getAssetType(), amd
                                                        .getId());
                                        Document doc = this
                                                .createDocument(stream);

                                        final Asset asset = xom.toObject(doc);
                                        File f = new File(pub.getName()
                                                + File.separator
                                                + assetType.getName()
                                                + File.separator
                                                + asset.getId() + ".xml");
                                        f.getParentFile().mkdirs();
                                        writeDocument(doc, f);
                                        for (Attribute attr : asset
                                                .getAttributes()) {
                                            Object data = attr.getData();
                                            extractFiles(data, f
                                                    .getParentFile(), pub
                                                    .getPrefix());
                                        }

                                        count++;
                                    } catch (final Exception e) {
                                        e.printStackTrace();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } finally {
            if (connectionManager instanceof MultiThreadedHttpConnectionManager) {
                ((MultiThreadedHttpConnectionManager) connectionManager)
                        .shutdown();
            }
        }
        if (count > 0) {
            System.out.println((System.currentTimeMillis() - t) / count);
        }
    }

    void extractFiles(Object data, File parent, String prefix)
            throws IOException {

        if (data instanceof Map) {
            Map map = (Map) data;
            for (Object x : map.values()) {
                extractFiles(x, parent, prefix);

            }
        } else if (data instanceof List) {
            List list = (List) data;
            for (Object x : list) {
                extractFiles(x, parent, prefix);
            }

        } else if (data instanceof FileField) {
            FileField ff = (FileField) data;
            if (ff != null) {
                File f = new File(parent, ff.getName());
                if (prefix != null) {
                    //System.out.println(ff.getName());
                    String[] parts = ff.getName().split("/");
                    StringBuilder b = new StringBuilder();
                    for (int i = 0; i < parts.length; i++) {
                        //System.out.println(parts[i]);

                        if (i > 0) {
                            b.append("/");
                        }
                        if (parts[i].startsWith(prefix)) {
                            b.append(parts[i].substring(prefix.length()));
                        } else {
                            b.append(parts[i]);
                        }

                    }
                    //System.out.println(b.toString());
                    f = new File(parent, b.toString());
                    //System.out.println(f.toString());
                }

                f.getParentFile().mkdirs();
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(f);
                    out.write(ff.getContent());
                } finally {
                    if (out != null)
                        out.close();
                }
            }

        }

    }

    private Document createDocument(final InputStream xml)
            throws DocumentException {

        final SAXReader xmlReader = new SAXReader();
        Document doc;
        doc = xmlReader.read(xml);

        return doc;
    }

    private void writeDocument(Document doc, File f) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new FileWriter(f), format);
        writer.write(doc);
        writer.close();

    }

    /**
     * the be called with 3 arguments, url, username and password
     * 
     * @param a
     * @throws Exception
     */

    public static void main(final String[] a) throws Exception {

        final RESTExport sample = new RESTExport();
        sample.location = new DefaultCSLocation(new URL(a[0]), a[1], a[2]);
        Set<String> pubs = new HashSet<String>();
        Set<String> all = new HashSet<String>() {
            public boolean contains(Object o) {
                return true;
            }
        };

        pubs.add("FirstSiteII");
        pubs.add("FirstSite2.0");
        Set<String> assetTypes = new HashSet<String>();
        assetTypes.add("Template");
        assetTypes.add("CSElement");
        assetTypes.add("SiteEntry");
        sample.downLoadAll(all, all);

    }
}
