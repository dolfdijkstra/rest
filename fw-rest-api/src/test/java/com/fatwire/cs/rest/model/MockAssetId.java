package com.fatwire.cs.rest.model;

import java.util.Date;

import com.fatwire.cs.rest.model.AssetId;

public class MockAssetId implements AssetId {
    private String assetType;

    private long id;

    private Date lastUpdatedDate;

    /**
     * @param assetType
     * @param id
     * @param lastUpdatedDate
     */
    public MockAssetId(String assetType, long id, Date lastUpdatedDate) {
        super();
        this.assetType = assetType;
        this.id = id;
        this.lastUpdatedDate = lastUpdatedDate;
    }

    /**
     * @param assetType
     * @param id
     */
    public MockAssetId(String assetType, long id) {
        super();
        this.assetType = assetType;
        this.id = id;
    }

    public String getAssetType() {
        return assetType;
    }

    public long getId() {
        return id;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public String toString() {
        return this.assetType + ":" + this.id + ":"
                + this.lastUpdatedDate.toString();
    }

}
