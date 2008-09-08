package com.fatwire.cs.rest.model;

import java.util.Date;

import org.apache.commons.lang.time.FastDateFormat;

/**
 * 
 * @author Dolf.Dijkstra
 * 
 */

public class AssetIdImpl implements AssetId {
    public static final String DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";

    private static final FastDateFormat df = FastDateFormat
            .getInstance(DATE_FORMAT_STRING);

    private Date lastUpdatedDate;

    private long id;

    private String assetType;

    public AssetIdImpl() {
        super();
    }

    /**
     * @return Returns the lastUpdatedDate.
     */
    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    /**
     * @param lastUpdatedDate
     *            The lastUpdatedDate to set.
     */
    public void setLastUpdatedDate(final Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getAssetType() {
        return assetType;
    }

    public long getId() {
        return id;
    }

    /**
     * @param assetType
     *            The assetType to set.
     */
    public void setAssetType(final String assetType) {
        this.assetType = assetType;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(final long id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuilder(assetType).append('-').append(id).append(' ')
                .append(df.format(lastUpdatedDate)).toString();

    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result
                + ((assetType == null) ? 0 : assetType.hashCode());
        result = PRIME * result + (int) (id ^ (id >>> 32));
        result = PRIME * result
                + ((lastUpdatedDate == null) ? 0 : lastUpdatedDate.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final AssetIdImpl other = (AssetIdImpl) obj;
        if (assetType == null) {
            if (other.assetType != null)
                return false;
        } else if (!assetType.equals(other.assetType))
            return false;
        if (id != other.id)
            return false;
        if (lastUpdatedDate == null) {
            if (other.lastUpdatedDate != null)
                return false;
        } else if (!lastUpdatedDate.equals(other.lastUpdatedDate))
            return false;
        return true;
    }

}
