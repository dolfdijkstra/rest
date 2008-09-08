package com.fatwire.cs.rest.commands;

import org.apache.commons.httpclient.HttpClient;

import com.fatwire.cs.rest.model.CSLocation;

public class GetPubID extends AbstractGetCommand {

	private String pubName;


    /**
     * @param httpClient
     * @param location
     */
    public GetPubID(final HttpClient httpClient, final CSLocation location) {
        super(httpClient, location);
    }

    protected void doAddParameters() {
		map.put("pubName", pubName);
	}

	@Override
	public String getPagename() {
		return "fatwire/REST/getPubID";
	}

	public String getPubName() {
		return pubName;
	}

	public void setPubName(final String pubName) {
		this.pubName = pubName;
	}

}
