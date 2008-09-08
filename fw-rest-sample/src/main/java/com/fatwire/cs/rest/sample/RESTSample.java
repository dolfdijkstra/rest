package com.fatwire.cs.rest.sample;

import java.net.URL;

import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.rest.model.Asset;
import com.fatwire.cs.rest.model.AssetMetaData;
import com.fatwire.cs.rest.model.AssetType;
import com.fatwire.cs.rest.model.CSLocation;
import com.fatwire.cs.rest.model.Publication;
import com.fatwire.cs.rest.remote.RestService;
import com.fatwire.cs.rest.service.DefaultRestService;
import com.fatwire.cs.rest.support.DefaultCSLocation;

/**
 * Sample code on how to use the REST services.
 * 
 * 
 * @author Dolf.Dijkstra
 * 
 */

public class RESTSample {
    protected static final Log log = LogFactory.getLog(RESTSample.class);

    CSLocation location;

    HttpConnectionManager getConnectionManager() {
        MultiThreadedHttpConnectionManager connectionManager;
        connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setConnectionTimeout(5000);
        connectionManager.getParams().setMaxTotalConnections(20);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(20);

        return connectionManager;
    }

    public void downLoadAll() throws Exception {
        /*
         * TODO: handle exceptions better
         * 
         */

        final HttpConnectionManager connectionManager = getConnectionManager();

        final RestService restService = new DefaultRestService(
                connectionManager, location);
        long t = System.currentTimeMillis();
        int count = 0;
        boolean doAssets = false;
        try {
            //get all sites from ContentServer
            for (final Publication pub : restService.getPublications()) {
                System.out.println(pub.getDescription());
                // get all assetTypes for this site
                for (final AssetType assetType : restService.getAssetTypes(pub
                        .getName())) {
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
                                final Asset asset = restService.getAsset(amd
                                        .getAssetType(), amd.getId());
                                // do some magic with the asset
                                System.out
                                        .println(asset.getAssetType() + " "
                                                + asset.getId() + " "
                                                + asset.getName());
                                count++;
                            } catch (final Exception e) {
                                e.printStackTrace();
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

    /**
     * the be called with 3 arguments, url, username and password
     * 
     * @param a
     * @throws Exception
     */
    
    public static void main(final String[] a) throws Exception {

        final RESTSample sample = new RESTSample();
        sample.location = new DefaultCSLocation(new URL(a[0]),
                a[1],a[2]);
        sample.downLoadAll();

    }
}
