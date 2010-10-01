package com.fatwire.cs.rest.sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;

import com.fatwire.cs.rest.model.Asset;
import com.fatwire.cs.rest.model.AssetMetaData;
import com.fatwire.cs.rest.model.AssetType;
import com.fatwire.cs.rest.model.Attribute;
import com.fatwire.cs.rest.model.CSLocation;
import com.fatwire.cs.rest.model.Publication;
import com.fatwire.cs.rest.remote.RestService;
import com.fatwire.cs.rest.service.DefaultRestService;
import com.fatwire.cs.rest.support.DefaultCSLocation;
import com.fatwire.cs.rest.xom.AssetXom;
import com.fatwire.cs.rest.xom.XOMException;

public class Synchronizer {
    protected static final Log log = LogFactory.getLog(Synchronizer.class);
    private Date compareDate;

    private CSLocation leftLocation;
    private File originals;
    final AssetXom xom = new AssetXom();
    private AssetComparator comparator = new AssetComparator();
    public List<String> pc = new ArrayList<String>();

    protected HttpConnectionManager getConnectionManager() {
        MultiThreadedHttpConnectionManager connectionManager;
        connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setConnectionTimeout(5000);
        connectionManager.getParams().setMaxTotalConnections(20);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(20);

        return connectionManager;
    }

    public void downLoadAndCompare(Set<String> pubs, Set<String> assettypes) throws Exception {
        /*
         * TODO: handle exceptions better
         */

        final HttpConnectionManager connectionManager = getConnectionManager();

        java.util.concurrent.ExecutorService execs = Executors.newFixedThreadPool(4);

        final RestService restService = new DefaultRestService(connectionManager, leftLocation);
        long t = System.currentTimeMillis();
        int count = 0;

        Collection<Future<Asset>> tasks = new ArrayList<Future<Asset>>();
        try {
            // get all sites from ContentServer
            for (final Publication pub : restService.getPublications()) {
                if (pubs.contains(pub.getName())) {
                    log.info(pub.getDescription());
                    log.debug("site prefix: " + pub.getPrefix());
                    // get all assetTypes for this site
                    for (final AssetType assetType : restService.getAssetTypes(pub.getName())) {
                        if (assettypes.contains(assetType.getName())) {
                            // get all asset meta data for this assettype and
                            // site
                            log.info(assetType.getDescription());

                            for (final AssetMetaData amd : restService.getAssetMetaData(pub.getName(),
                                    assetType.getName())) {
                                // only download and compare if it is recently
                                // updated
                                if (compareDate.before(amd.getLastUpdatedDate())) {
                                    try {

                                        tasks.add(execs.submit(new AssetCallable(restService, amd, new File("download",
                                                pub.getName()))));
                                        count++;
                                    } catch (final Exception e) {
                                        log.error(e.getMessage() + " on " + amd.getAssetType() + "-" + amd.getName(), e);
                                    }
                                } else {
                                    log.debug(amd.getAssetType() + " " + amd.getId() + " " + amd.getName() + " "
                                            + amd.getLastUpdatedDate() + " is not changed recently.");
                                }
                            }
                        }
                    }
                }
            }

            // wait for all tasks to finish
            for (Future<Asset> f : tasks) {
                f.get();
            }

        } finally {
            if (connectionManager instanceof MultiThreadedHttpConnectionManager) {
                ((MultiThreadedHttpConnectionManager) connectionManager).shutdown();
            }
            execs.shutdown();
        }
        if (count > 0) {
            System.out.println(count + " assets in " + ((System.currentTimeMillis() - t) / count) + "ms. per assets.");
        }
        FileUtils.writeLines(new File("template.log"), pc);
    }


    private void setDaysBack(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, Math.abs(days) * -1);
        compareDate = cal.getTime();
        log.debug("Comparing to " + compareDate);

    }

    private class AssetCallable implements Callable<Asset> {
        private final AssetMetaData amd;
        private final RestService restService;
        private final File remoteDir;
        private final File localDir;
        private FileExtractor fileExtractor = new FileExtractor();

        AssetCallable(final RestService restService, final AssetMetaData amd, File saveLocation) {
            this.amd = amd;
            this.restService = restService;
            remoteDir = new File(saveLocation, "remote");
            localDir = new File(saveLocation, "local");

        }

        public Asset call() throws Exception {
            log.info(amd.getAssetType() + " " + amd.getId() + " " + amd.getName());
            // get the actual asset
            InputStream stream = restService.getAssetAsStream(amd.getAssetType(), amd.getId());
            Document doc = DomUtils.createDocument(stream);

            final Asset asset = xom.toObject(doc);
            File originalFile = new File(new File(originals, asset.getAssetType()), asset.getId() + ".xml");
            File remoteNormalized = new File(remoteDir, ".assets" + File.separator + amd.getAssetType()
                    + File.separator + asset.getId() + ".xml");

            if (originalFile.exists()) {
                InputStream in = new FileInputStream(originalFile);
                Document originalDocument = DomUtils.createDocument(in);
                Asset originalAsset = DomUtils.createAsset(originalDocument);

                int comp = comparator.compare(asset, originalAsset);
                if (comp != 0) {
                    File localNormalized = new File(localDir, ".assets" + File.separator + amd.getAssetType()
                            + File.separator + asset.getId() + ".xml");

                    DomUtils.writeDocument(doc, remoteNormalized);
                    for (Attribute attr : asset.getAttributes()) {
                        Object data = attr.getData();
                        fileExtractor.extractFiles(data, new File(remoteDir, amd.getAssetType()));
                    }

                    DomUtils.writeDocument(originalDocument, localNormalized);

                    for (Attribute attr : originalAsset.getAttributes()) {
                        Object data = attr.getData();
                        fileExtractor.extractFiles(data, new File(localDir, amd.getAssetType()));
                        /*
                         * if ("pagecriteria".equals(attr.getName())) {
                         * Attribute a =
                         * asset.getAttributeValue(attr.getName()); if
                         * (!attr.getData().equals(a.getData())) {
                         * pc.add(asset.getId() + "\t" + attr.getData()); } }
                         */
                    }

                }
            } else {
                log.info("asset " + asset.getAssetType() + " " + asset.getName()
                        + " is not present in orginals leftLocation.");
                DomUtils.writeDocument(doc, remoteNormalized);
                for (Attribute attr : asset.getAttributes()) {
                    Object data = attr.getData();
                    fileExtractor.extractFiles(data, new File(remoteDir, amd.getAssetType()));
                }

            }

            return asset;
        }

    };

    /**
     * the be called with 4 arguments, url, username, password and site name
     * 
     * @param a
     * @throws Exception
     */

    public static void main(final String[] a) throws Exception {

        final Synchronizer synchronizer = new Synchronizer();
        URI u = new URI(a[0]);
        String userInfo = u.getUserInfo();
        String username=userInfo.split(":")[0];
        String password=userInfo.split(":")[1];
        URI location = new URI(u.getScheme(),
                null, u.getHost(), u.getPort(),
                u.getRawPath(), u.getRawQuery(),
                u.getRawFragment());
        
        synchronizer.leftLocation = new DefaultCSLocation(location.toURL(), username, password);
        
        
        Set<String> pubs = new HashSet<String>();

        pubs.add(a[3]);
        Set<String> assetTypes = new HashSet<String>();
        //assetTypes.add("Template");
        assetTypes.add("CSElement");
        // assetTypes.add("SiteEntry");
        synchronizer.setOriginalsLocation(new File("WebContent/.cside/"));
        synchronizer.setDaysBack(700);

        synchronizer.downLoadAndCompare(pubs, assetTypes);

    }

    private void setOriginalsLocation(File dir) {
        if (!dir.isDirectory())
            throw new IllegalArgumentException(dir + " is not a directory.");
        this.originals = dir;

    }
}
