package com.fatwire.cs.rest.commands;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import com.fatwire.cs.rest.CommandException;
import com.fatwire.cs.rest.HttpCommandException;
import com.fatwire.cs.rest.model.CSLocation;

public class SetAsset extends AbstractCommand {

    private String assetType;

    private long assetId;

    private byte[] assetData;

    private String publication;

    /**
     * @param httpClient
     * @param location
     */
    public SetAsset(final HttpClient httpClient, final CSLocation location) {
        super(httpClient, location);
    }

    public byte[] execute() throws CommandException {
        return doPost();
    }

    protected final byte[] doPost() throws HttpCommandException {

        map.put("pagename", getPagename());
        doAddParameters();
        final ByteArrayPartSource p = new ByteArrayPartSource("asset.xml",
                assetData);
        final PostMethod post = new PostMethod();
        try {
            final URI u = createUri();
            log.debug(u);
            post.setURI(u);

            final Part[] parts = {
                    new StringPart("authusername", location.getUsername()),
                    new StringPart("authpassword", location.getPassword()),
                    new FilePart("assetdata", p, "text/xml", "UTF-8") };

            post.setRequestEntity(new MultipartRequestEntity(parts, post
                    .getParams()));

        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

        return executeMethod(post);

    }

    @Override
    public String getPagename() {
        return "fatwire/REST/assets/PUT";
    }

    public void setAssetType(final String assetType) {
        this.assetType = assetType;
    }

    public void setAssetId(final long assetId) {
        this.assetId = assetId;
    }

    public void setAssetData(final byte[] assetData) {
        this.assetData = assetData;
    }

    protected void doAddParameters() {
        map.put("resource", getPublication() + "/" + getAssetType() + "/"
                + Long.toString(assetId));

    }

    public void setPublication(String publication) {
        this.publication = publication;

    }

    /**
     * @return the assetId
     */
    public long getAssetId() {
        return assetId;
    }

    /**
     * @return the assetType
     */
    public String getAssetType() {
        return assetType;
    }

    /**
     * @return the publication
     */
    public String getPublication() {
        return publication;
    }

}
