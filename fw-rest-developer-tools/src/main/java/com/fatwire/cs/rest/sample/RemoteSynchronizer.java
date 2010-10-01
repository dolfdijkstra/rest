package com.fatwire.cs.rest.sample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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

public class RemoteSynchronizer {
    protected static final Log log = LogFactory.getLog(RemoteSynchronizer.class);
    private Date compareDate;

    private CSLocation leftLocation;
    private CSLocation rightLocation;
    private final AssetXom xom = new AssetXom();
    private final AssetComparator comparator = new AssetComparator();

    private File reportDirectory;
    private final FileExtractor fileExtractor = new FileExtractor();

    protected HttpConnectionManager createConnectionManager() {
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

        final HttpConnectionManager connectionManager = createConnectionManager();

        java.util.concurrent.ExecutorService execs = Executors.newFixedThreadPool(4);

        final RestService leftService = new DefaultRestService(connectionManager, leftLocation);
        final RestService rightService = new DefaultRestService(connectionManager, rightLocation);
        long t = System.currentTimeMillis();
        int count = 0;

        Collection<Future<DiffState>> tasks = new ArrayList<Future<DiffState>>();
        try {
            // get all sites from ContentServer
            for (final Publication pub : leftService.getPublications()) {
                if (pubs.contains(pub.getName())) {
                    log.info(pub.getDescription());
                    log.debug("site prefix: " + pub.getPrefix());
                    // get all assetTypes for this site
                    for (final AssetType assetType : leftService.getAssetTypes(pub.getName())) {
                        if (assettypes.contains(assetType.getName())) {
                            // get all asset meta data for this assettype and
                            // site
                            log.info(assetType.getDescription());
                            Map<Long, AssetMetaData> m = new HashMap<Long, AssetMetaData>();

                            for (final AssetMetaData amd : leftService.getAssetMetaData(pub.getName(),
                                    assetType.getName())) {
                                // only download and compare if it is recently
                                // updated
                                if (compareDate.before(amd.getLastUpdatedDate())) {
                                    m.put(amd.getId(), amd);
                                } else {
                                    log.debug(amd.getAssetType() + " " + amd.getId() + " " + amd.getName() + " "
                                            + amd.getLastUpdatedDate() + " is not changed recently.");
                                }
                            }
                            for (final AssetMetaData amd : rightService.getAssetMetaData(pub.getName(),
                                    assetType.getName())) {
                                // only download and compare if it is recently
                                // updated
                                if (compareDate.before(amd.getLastUpdatedDate())) {
                                    m.put(amd.getId(), amd);
                                } else {
                                    log.debug(amd.getAssetType() + " " + amd.getId() + " " + amd.getName() + " "
                                            + amd.getLastUpdatedDate() + " is not changed recently.");
                                }
                            }

                            for (final AssetMetaData amd : m.values()) {
                                try {

                                    tasks.add(execs.submit(new AssetCallable(leftService, rightService, amd, new File(
                                            reportDirectory, pub.getName()))));
                                    count++;
                                } catch (final Exception e) {
                                    log.error(e.getMessage() + " on " + amd.getAssetType() + "-" + amd.getName(), e);
                                }
                            }

                        }
                    }
                }
            }

            // wait for all tasks to finish
            for (Future<DiffState> f : tasks) {
                DiffState state = f.get();
                if (!state.isEqual) {
                    log.warn(state.left.asset.getAssetType() + " " + state.left.asset.getId() + " are not equal");
                }
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

    }

    private void setDaysBack(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, Math.abs(days) * -1);
        compareDate = cal.getTime();
        log.debug("Comparing to " + compareDate);

    }

    class DiffState {

        AssetLocal left;
        AssetLocal right;
        boolean isEqual = true;

        void writeAssets() throws IOException {
            if (left != null)
                left.write();
            if (right != null)
                right.write();
        }
    }

    class AssetLocal {
        Asset asset;
        Document doc;
        File location;

        public AssetLocal(InputStream stream, File location) throws DocumentException, XOMException {
            super();
            doc = DomUtils.createDocument(stream);
            asset = xom.toObject(doc);
            this.location = location;
        }

        void write() throws IOException {
            File leftNormalized = new File(location, ".assets" + File.separator + asset.getAssetType() + File.separator
                    + asset.getId() + ".xml");

            DomUtils.writeDocument(doc, leftNormalized);
            for (Attribute attr : asset.getAttributes()) {
                Object data = attr.getData();
                fileExtractor.extractFiles(data, new File(location, asset.getAssetType()));
            }

        }

    }

    class AssetCallable implements Callable<DiffState> {
        private final AssetMetaData amd;
        private final RestService leftService;
        private final RestService rightService;

        private final File leftDir;
        private final File rightDir;
        private boolean dumpRaw = false;

        AssetCallable(final RestService leftService, final RestService rightService, final AssetMetaData amd,
                File saveLocation) {
            this.amd = amd;
            this.rightService = rightService;
            this.leftService = leftService;
            leftDir = new File(saveLocation, "left");
            rightDir = new File(saveLocation, "right");

        }

        public DiffState call() throws Exception {
            log.info(amd.getAssetType() + " " + amd.getId() + " " + amd.getName());

            DiffState state = new DiffState();
            // get the actual asset
            InputStream leftStream = leftService.getAssetAsStream(amd.getAssetType(), amd.getId());
            leftDir.mkdirs();
            rightDir.mkdirs();
            if (dumpRaw) {
                FileOutputStream out = new FileOutputStream(new File(leftDir, amd.getId() + ".raw.xml"));
                IOUtils.copy(leftStream, out);
                IOUtils.closeQuietly(out);
                leftStream.reset();
            }
            state.left = new AssetLocal(leftStream, leftDir);
            IOUtils.closeQuietly(leftStream);

            InputStream rightStream = rightService.getAssetAsStream(amd.getAssetType(), amd.getId());
            if (dumpRaw) {
                FileOutputStream out = new FileOutputStream(new File(rightDir, amd.getId() + ".raw.xml"));
                IOUtils.copy(rightStream, out);
                IOUtils.closeQuietly(out);
                rightStream.reset();
            }
            state.right = new AssetLocal(rightStream, rightDir);
            IOUtils.closeQuietly(rightStream);

            int comp = comparator.compare(state.left.asset, state.right.asset);
            if (comp != 0) {
                state.isEqual = false;
                state.writeAssets();

            }

            return state;
        }

    };

    static CSLocation createLocation(URI u) throws MalformedURLException, URISyntaxException {

        String userInfo = u.getUserInfo();
        String username = userInfo.split(":")[0];
        String password = userInfo.split(":")[1];
        URI location = new URI(u.getScheme(), null, u.getHost(), u.getPort(), u.getRawPath(), u.getRawQuery(),
                u.getRawFragment());

        return new DefaultCSLocation(location.toURL(), username, password);

    }

    /**
     * the be called with 4 arguments, url, username, password and site name
     * 
     * @param a
     * @throws Exception
     */

    public static void main(final String[] a) throws Exception {

        final RemoteSynchronizer synchronizer = new RemoteSynchronizer();

        synchronizer.leftLocation = createLocation(new URI(a[0]));
        synchronizer.rightLocation = createLocation(new URI(a[1]));

        Set<String> pubs = new HashSet<String>();
        if (a.length > 2 && a[2] != null) {
            pubs.addAll(Arrays.asList(a[2].split("[;,]")));
        }
        // pubs.add(a[3]);
        if (pubs.isEmpty())
            throw new IllegalArgumentException("No site name provided");
        Set<String> assetTypes = new HashSet<String>();
        if (a.length < 4 || a[3] == null) {
            assetTypes.add("Template");
            assetTypes.add("CSElement");
            assetTypes.add("SiteEntry");
        } else {
            assetTypes.addAll(Arrays.asList(a[3].split("[;,]")));
        }
        if (assetTypes.isEmpty())
            throw new IllegalArgumentException("No assettype names provided");
        File reports = new File("reports");
        FileUtils.cleanDirectory(reports);
        synchronizer.setReportDirectory(reports);
        synchronizer.setDaysBack(2);

        synchronizer.downLoadAndCompare(pubs, assetTypes);

    }

    private void setReportDirectory(File dir) {
        if (!dir.exists() && !dir.mkdirs())
            throw new IllegalArgumentException("Can't make directory: " + dir);
        this.reportDirectory = dir;

    }
}
