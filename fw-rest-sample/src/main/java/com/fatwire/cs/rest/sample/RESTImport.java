package com.fatwire.cs.rest.sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.fatwire.cs.rest.model.Asset;
import com.fatwire.cs.rest.model.AssetMetaData;
import com.fatwire.cs.rest.model.AssetType;
import com.fatwire.cs.rest.model.CSLocation;
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

public class RESTImport {
    protected static final Log log = LogFactory.getLog(RESTImport.class);

    CSLocation location;

    HttpConnectionManager getConnectionManager() {
        MultiThreadedHttpConnectionManager connectionManager;
        connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setConnectionTimeout(5000);
        connectionManager.getParams().setMaxTotalConnections(20);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(20);

        return connectionManager;
    }

    public void importAll(Set<String> pubs, Set<String> assettypes)
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
                                        if (f.exists()) {
                                            InputStream stream2 = new FileInputStream(
                                                    f);
                                            Document doc2 = this
                                                    .createDocument(stream2);

                                            final Asset asset2 = xom
                                                    .toObject(doc2);
                                            if (asset2
                                                    .getLastUpdatedDate()
                                                    .after(
                                                            asset
                                                                    .getUpdatedDate())) {
                                                //if the asset on disk is more recent then import 
                                                //alternative would be to do a more extensive comparision on attribute data
                                                stream2 = new FileInputStream(f);
                                                //if asset data needs to be merged, it needs to be merged at the XML DOM level. The Asset class does not support write function
                                                restService.setAsset(pub
                                                        .getName(), assetType
                                                        .getName(), asset
                                                        .getId(), stream2);
                                                stream2.close(); //TODO better exception handling.
                                            }

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

    private Document createDocument(final InputStream xml)
            throws DocumentException {

        final SAXReader xmlReader = new SAXReader();
        Document doc;
        doc = xmlReader.read(xml);

        return doc;
    }

    /**
     * the be called with 3 arguments, url, username and password
     * 
     * @param a
     * @throws Exception
     */

    public static void main(final String[] a) throws Exception {

        final RESTImport sample = new RESTImport();
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
        sample.importAll(all, all);

    }
}
