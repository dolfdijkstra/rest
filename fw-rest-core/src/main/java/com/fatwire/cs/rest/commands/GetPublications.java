package com.fatwire.cs.rest.commands;

import org.apache.commons.httpclient.HttpClient;

import com.fatwire.cs.rest.model.CSLocation;

public class GetPublications extends AbstractGetCommand {

    /**
     * @param httpClient
     * @param location
     */
    public GetPublications(final HttpClient httpClient,
            final CSLocation location) {
        super(httpClient, location);
    }

    @Override
    protected void doAddParameters() {
        // nothing to do
    }
}
