package com.fatwire.cs.rest.model;

import java.util.Date;

/**
 * 
 * This class is used to get the main metadata of an asset
 * 
 * @author Dolf.Dijkstra
 *
 */
public interface AssetId {

    /**
     * 
     * 
     * @return The assettype of this asset
     */

    public String getAssetType();

    /**
     * 
     * @return as id of the asset
     */

    public long getId();

    /**
     * 
     * @return the LastUpdatedDate (as a hint for the asset version)
     */

    public Date getLastUpdatedDate();

}
