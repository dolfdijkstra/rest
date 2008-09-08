package com.fatwire.cs.rest.commands;

import org.apache.commons.httpclient.HttpClient;

import com.fatwire.cs.rest.model.CSLocation;

public class GetAsset extends GetAssetData {


    private long assetId;

    public GetAsset(final HttpClient httpClient, final CSLocation location) {
        super(httpClient, location);
    }


    public void setAssetId(final long assetId) {
        this.assetId = assetId;
    }

    @Override
    protected void doAddParameters() {
        map.put("resource", getPublication() +"/"+getAssetType() +"/"+  Long.toString(assetId));

        //map.put("id",);

    }

    /**
     * @return the assetId
     */
    public long getAssetId() {
        return assetId;
    }

}
