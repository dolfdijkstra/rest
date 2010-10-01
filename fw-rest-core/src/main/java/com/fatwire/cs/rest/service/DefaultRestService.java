package com.fatwire.cs.rest.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.fatwire.cs.rest.CommandException;
import com.fatwire.cs.rest.commands.GetAsset;
import com.fatwire.cs.rest.commands.GetAssetData;
import com.fatwire.cs.rest.commands.GetAssetTypes;
import com.fatwire.cs.rest.commands.GetPubID;
import com.fatwire.cs.rest.commands.GetPublications;
import com.fatwire.cs.rest.commands.SetAsset;
import com.fatwire.cs.rest.model.Asset;
import com.fatwire.cs.rest.model.AssetMetaData;
import com.fatwire.cs.rest.model.AssetType;
import com.fatwire.cs.rest.model.CSLocation;
import com.fatwire.cs.rest.model.Publication;
import com.fatwire.cs.rest.remote.RestService;
import com.fatwire.cs.rest.remote.RestServiceException;
import com.fatwire.cs.rest.xom.AssetMetaDataXom;
import com.fatwire.cs.rest.xom.AssetTypesXom;
import com.fatwire.cs.rest.xom.AssetXom;
import com.fatwire.cs.rest.xom.FaultChecker;
import com.fatwire.cs.rest.xom.PublicationIdXom;
import com.fatwire.cs.rest.xom.PublicationXom;
import com.fatwire.cs.rest.xom.XOMException;

/**
 * http://www.host.com/cs/Satellite?pagename=REST/Assets&resource=PubName/AssetType/assetid
 http://www.host.com/cs/Satellite?pagename=REST/Search&query=(name=joe)

 * 
 * @author Dolf.Dijkstra
 * @since Sep 1, 2008
 */

public class DefaultRestService implements RestService {

    private static final long IDLE_TIMEOUT = 300 * 1000L;

    private volatile HttpClient client;

    private volatile CSLocation location;

    private final HttpConnectionManager connectionManager;

    private final FaultChecker faultChecker = new FaultChecker();

    public DefaultRestService(final HttpConnectionManager connectionManager,
            final CSLocation location) {
        super();
        this.connectionManager = connectionManager;
        this.location = location;
        client = new HttpClient(connectionManager);
    }

    private CSLocation getLocation() {
        return location;
    }

    public InputStream getAssetAsStream(final String assettype,final long id) throws RestServiceException {
        final GetAsset command = new GetAsset(client, getLocation());

        command.setAssetType(assettype);
        command.setAssetId(id);// 1234567890987
        try {
            final byte[] asset = command.execute();

            if ((asset == null) || (asset.length == 0)) {
                throw new RestServiceException("no asset data return for "
                        + assettype + " with id " + id);
            }
            faultChecker.checkForFaultMessage(createDocument(asset));
            return new ByteArrayInputStream(asset);
        } catch (final CommandException e) {
            throw new RestServiceException(e.getMessage(), e);
        } catch (final DocumentException e) {
            throw new RestServiceException(e.getMessage(), e);
        } finally {
            connectionManager.closeIdleConnections(IDLE_TIMEOUT);
        }
    }

    private Document createDocument(final byte[] xml) throws DocumentException {

        final SAXReader xmlReader = new SAXReader();
        xmlReader.setEncoding("UTF-8");
        Document doc;
        doc = xmlReader.read(new ByteArrayInputStream(xml));

        return doc;
    }

    public Iterable<AssetMetaData> getAssetMetaData(
            final String publication, final String assettype)
            throws RestServiceException {
        final GetAssetData command = new GetAssetData(client, getLocation());
        if (publication == null) {
            throw new IllegalArgumentException(
                    "No publication specified");
        }
        command.setPublication(publication);
        command.setAssetType(assettype);
        try {
            final byte[] assets = command.execute();

            final Document doc = createDocument(assets);
            faultChecker.checkForFaultMessage(doc);
            final AssetMetaDataXom xom = new AssetMetaDataXom();
            return xom.toObject(doc);
        } catch (final CommandException e) {
            throw new RestServiceException(e.getMessage(), e);
        } catch (final XOMException e) {
            throw new RestServiceException(e.getMessage(), e);
        } catch (final DocumentException e) {
            throw new RestServiceException(e.getMessage(), e);
        } finally {
            connectionManager.closeIdleConnections(IDLE_TIMEOUT);
        }

    }

    public long getPubID(final String pubName) throws RestServiceException {
        final GetPubID command = new GetPubID(client, getLocation());

        command.setPubName(pubName);
        try {
            final byte[] xml = command.execute();
            final Document doc = createDocument(xml);
            faultChecker.checkForFaultMessage(doc);

            final PublicationIdXom xom = new PublicationIdXom();
            return xom.toObject(doc);
        } catch (final CommandException e) {
            throw new RestServiceException(e.getMessage(), e);
        } catch (final XOMException e) {
            throw new RestServiceException(e.getMessage(), e);
        } catch (final DocumentException e) {
            throw new RestServiceException(e.getMessage(), e);
        } finally {
            connectionManager.closeIdleConnections(IDLE_TIMEOUT);
        }

    }

    public Iterable<Publication> getPublications() throws RestServiceException {

        final GetPublications command = new GetPublications(client,
                getLocation());

        try {
            byte[] xml;
            xml = command.execute();
            final Document doc = createDocument(xml);
            faultChecker.checkForFaultMessage(doc);

            final PublicationXom xom = new PublicationXom();
            return xom.toObject(doc);

        } catch (final CommandException e) {
            throw new RestServiceException(e.getMessage(), e);
        } catch (final XOMException e) {
            throw new RestServiceException(e.getMessage(), e);
        } catch (final DocumentException e) {
            throw new RestServiceException(e.getMessage(), e);
        } finally {
            connectionManager.closeIdleConnections(IDLE_TIMEOUT);
        }
    }

    public int setAsset(final String publication, final String assettype,
            final long id, final InputStream in) throws RestServiceException {
        final SetAsset setCommand = new SetAsset(client, getLocation());

        setCommand.setAssetType(assettype);
        setCommand.setAssetId(id);

        try {
            setCommand.setAssetData(IOUtils.toByteArray(in));
            setCommand.setPublication(publication);
            final byte[] response = setCommand.execute();
            faultChecker.checkForFaultMessage(response);

        } catch (final IOException e) {
            throw new RestServiceException(e.getMessage(), e);
        } catch (final CommandException e) {
            throw new RestServiceException(e.getMessage(), e);
        } finally {
            connectionManager.closeIdleConnections(IDLE_TIMEOUT);
        }
        return 0;
    }

    public Asset getAsset(final String assettype, final long id)
            throws RestServiceException {
        
        final GetAsset command = new GetAsset(client, getLocation());

        command.setAssetType(assettype);
        command.setAssetId(id);
        try {
            final byte[] asset = command.execute();

            if ((asset == null) || (asset.length == 0)) {
                throw new RestServiceException("no asset data return for "
                        + assettype + " with id " + id);
            }
            final Document doc = createDocument(asset);
            faultChecker.checkForFaultMessage(doc);
            final AssetXom xom = new AssetXom();
            return xom.toObject(doc);
        } catch (final CommandException e) {
            throw new RestServiceException(e.getMessage(), e);
        } catch (final XOMException e) {
            throw new RestServiceException(e.getMessage(), e);
        } catch (final DocumentException e) {
            throw new RestServiceException(e.getMessage(), e);
        } finally {
            connectionManager.closeIdleConnections(IDLE_TIMEOUT);
        }
    }

    public Iterable<AssetType> getAssetTypes(final String publication)
            throws RestServiceException {
        final GetAssetTypes command = new GetAssetTypes(client, getLocation());

        try {
            command.setPublication(publication);
            byte[] xml;
            xml = command.execute();
            final Document doc = createDocument(xml);
            faultChecker.checkForFaultMessage(doc);

            final AssetTypesXom xom = new AssetTypesXom();
            return xom.toObject(doc);

        } catch (final CommandException e) {
            throw new RestServiceException(e.getMessage(), e);
        } catch (final XOMException e) {
            throw new RestServiceException(e.getMessage(), e);
        } catch (final DocumentException e) {
            throw new RestServiceException(e.getMessage(), e);
        } finally {
            connectionManager.closeIdleConnections(IDLE_TIMEOUT);
        }
    }
}
