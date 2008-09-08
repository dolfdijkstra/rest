package com.fatwire.cs.rest.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Dolf.Dijkstra
 * @since Sep 3, 2008
 */
public class AssetType {

    private String name;

    private String description;

    private final Set<String> subTypes = new HashSet<String>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getSubTypes() {
        return Collections.unmodifiableSet(subTypes);
    }

    public void addSubType(String subType) {
        subTypes.add(subType);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result
                + ((description == null) ? 0 : description.hashCode());
        result = PRIME * result + ((name == null) ? 0 : name.hashCode());
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
        final AssetType other = (AssetType) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
