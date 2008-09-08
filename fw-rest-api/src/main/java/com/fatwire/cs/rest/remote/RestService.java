package com.fatwire.cs.rest.remote;

import java.io.InputStream;

import com.fatwire.cs.rest.model.Asset;
import com.fatwire.cs.rest.model.AssetMetaData;
import com.fatwire.cs.rest.model.AssetType;
import com.fatwire.cs.rest.model.Publication;

public interface RestService {

    Iterable<AssetMetaData> getAssetMetaData(String publication, String assettype)
            throws RestServiceException;

    InputStream getAssetAsStream(String assettype, long id)
            throws RestServiceException;

    int setAsset(String publication, String assettype, long id,
            InputStream assetdata) throws RestServiceException;

    Iterable<Publication> getPublications() throws RestServiceException;

    Iterable<AssetType> getAssetTypes(String publication) throws RestServiceException;

    Asset getAsset(String assettype, long id) throws RestServiceException;

}
