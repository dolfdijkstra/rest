package com.fatwire.cs.rest.commands;

import org.apache.commons.httpclient.HttpClient;

import com.fatwire.cs.rest.model.CSLocation;

public class GetAssetData extends GetAssetTypes {

    private String assetType;

    public GetAssetData(final HttpClient httpClient, final CSLocation location) {
        super(httpClient, location);
    }


    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(final String assetType) {
        this.assetType = assetType;
    }

    @Override
    protected void doAddParameters() {
        map.put("resource", getPublication() +"/"+getAssetType() );
    }


}
