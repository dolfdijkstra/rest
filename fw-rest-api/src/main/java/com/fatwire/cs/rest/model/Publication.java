package com.fatwire.cs.rest.model;

public class Publication {
    private long id;

    private String name;

    private String description;

    private String pubroot;

    private String preview;

    private String prefix;

    private String previewasset;

    public Publication() {
        super();
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the id.
     */
    public long getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return Returns the name.
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the pubroot
     */
    public String getPubroot() {
        return pubroot;
    }

    /**
     * @param pubroot the pubroot to set
     */
    public void setPubroot(String pubroot) {
        this.pubroot = pubroot;
    }


    /**
     * @return the preview
     */
    public String getPreview() {
        return preview;
    }

    /**
     * @param preview the preview to set
     */
    public void setPreview(String preview) {
        this.preview = preview;
    }

    /**
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix the prefix to set
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @return the previewasset
     */
    public String getPreviewAsset() {
        return previewasset;
    }

    /**
     * @param previewasset the previewasset to set
     */
    public void setPreviewAsset(String previewasset) {
        this.previewasset = previewasset;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((prefix == null) ? 0 : prefix.hashCode());
        result = prime * result
                + ((preview == null) ? 0 : preview.hashCode());
        result = prime * result
                + ((previewasset == null) ? 0 : previewasset.hashCode());
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((pubroot == null) ? 0 : pubroot.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Publication)) {
            return false;
        }
        Publication other = (Publication) obj;
        if (prefix == null) {
            if (other.prefix != null) {
                return false;
            }
        } else if (!prefix.equals(other.prefix)) {
            return false;
        }
        if (preview == null) {
            if (other.preview != null) {
                return false;
            }
        } else if (!preview.equals(other.preview)) {
            return false;
        }
        if (previewasset == null) {
            if (other.previewasset != null) {
                return false;
            }
        } else if (!previewasset.equals(other.previewasset)) {
            return false;
        }
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (id != other.id) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (pubroot == null) {
            if (other.pubroot != null) {
                return false;
            }
        } else if (!pubroot.equals(other.pubroot)) {
            return false;
        }
        return true;
    }

}
