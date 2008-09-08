package com.fatwire.cs.rest.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Dolf.Dijkstra
 * @since Sep 3, 2008
 */
public class Asset extends AssetMetaData {

    private String subType;

    private final Map<String, Attribute> attributes = new HashMap<String, Attribute>();

    /**
     * @return the subType
     */
    public String getSubType() {
        return subType;
    }

    /**
     * @param subType
     *            the subType to set
     */
    public void setSubType(final String subType) {
        this.subType = subType;
    }

    /**
     * @return the attributes
     */
    public Collection<Attribute> getAttributes() {
        return attributes.values();
    }

    public Object getAttributeValue(String name) {
        return attributes.get(name);
    }

    public void removeAttribute(String name) {
        attributes.remove(name);

    }

    public void addAttribute(final Attribute attribute) {
        attributes.put(attribute.getName(), attribute);
        if ("name".equalsIgnoreCase(attribute.getName())) {
            this.setName((String) attribute.getData());
        } else if ("description".equalsIgnoreCase(attribute.getName())) {
            this.setDescription((String) attribute.getData());
        } else if ("updatedDate".equalsIgnoreCase(attribute.getName())) {
            this.setUpdatedDate((Date) attribute.getData());
        } else if ("updatedBy".equalsIgnoreCase(attribute.getName())) {
            this.setUpdatedBy((String) attribute.getData());
        } else if ("status".equalsIgnoreCase(attribute.getName())) {
            this.setStatus((String) attribute.getData());
        } else if ("createdBy".equalsIgnoreCase(attribute.getName())) {
            this.setCreatedBy((String) attribute.getData());
        } else if ("createdDate".equalsIgnoreCase(attribute.getName())) {
            this.setCreatedDate((Date) attribute.getData());
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result
                + ((attributes == null) ? 0 : attributes.hashCode());
        result = PRIME * result + ((subType == null) ? 0 : subType.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Asset other = (Asset) obj;
        if (attributes == null) {
            if (other.attributes != null)
                return false;
        } else if (!attributes.equals(other.attributes))
            return false;
        if (subType == null) {
            if (other.subType != null)
                return false;
        } else if (!subType.equals(other.subType))
            return false;
        return true;
    }
}
