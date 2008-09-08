package com.fatwire.cs.rest.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * A simple association between a name (String) for a file and an array of bytes
 * which is the content of this file.
 * 
 * 
 */
public class FileField {

    private final String name;

    private final byte[] content;

    /**
     * The constructor
     * 
     * @param value
     *            the name of the FileField
     */
    public FileField(final String value, final byte[] bs) {
        name = stripFileNumberFromUpload(value);
        content = bs;
    }

    private String stripFileNumberFromUpload(final String value) {
        final int dot = value.lastIndexOf('.');
        final int comma = value.lastIndexOf(',');
        if ((comma != -1) && (dot > comma)) {
            final StringBuilder b = new StringBuilder(value.substring(0, comma));
            b.append(value.substring(dot, value.length()));
            return b.toString().replace('\\', '/');
        }
        return value.replace('\\', '/');
    }

    /**
     * Gives the value of content for this FileField
     * 
     * @return the value of content
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * Gives the value of name for this FileField
     * 
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Gives the content of the FileField as a String with name as it :
     * "{file:name}"
     * 
     * @return the content of the Field
     */
    public String toString() {
        return "{file:" + name + "}";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if ((obj instanceof FileField) == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        final FileField rhs = (FileField) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj)).append(name,
                rhs.name).append(content, rhs.content)

        .isEquals();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(name).append(content)
                .toHashCode();
    }

}
