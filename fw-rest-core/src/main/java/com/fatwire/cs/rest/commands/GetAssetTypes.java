package com.fatwire.cs.rest.commands;

import org.apache.commons.httpclient.HttpClient;

import com.fatwire.cs.rest.model.CSLocation;

public class GetAssetTypes extends GetPublications {

    private String publication;

    /**
     * @param httpClient
     * @param location
     */
    public GetAssetTypes(final HttpClient httpClient, final CSLocation location) {
        super(httpClient, location);
    }

    protected void doAddParameters() {
        map.put("resource", publication);
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    /**
     * @return the publication
     */
    public String getPublication() {
        return publication;
    }

}
