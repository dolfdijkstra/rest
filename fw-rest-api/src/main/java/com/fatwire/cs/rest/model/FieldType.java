package com.fatwire.cs.rest.model;

public enum FieldType {
    _int("int", true), assetreference("assetreference", true), string("string",
            true), _float("float", true), binary("binary", true), file("file",
            true), date("date", true), ruleset("ruleset", true), array("array",
            false), list("list", false), struct("struct", false), oneof(
            "oneof", false), row("row", false), column("column", true);

    private String name;

    private boolean singleValued;

    private FieldType(String name, boolean single) {
        this.name = name;
        this.singleValued = single;
    }

    /**
     * @return the singleValued
     */
    public boolean isSingleValued() {
        return singleValued;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public static FieldType parse(String s) {
        if ("integer".equals(s))
            return _int;
        if ("assetreference".equals(s))
            return assetreference;
        if ("string".equals(s))
            return string;
        if ("decimal".equals(s))
            return _float;
        if ("binary".equals(s))
            return binary;
        if ("file".equals(s))
            return file;
        if ("date".equals(s))
            return date;
        if ("ruleset".equals(s))
            return ruleset;
        if ("array".equals(s))
            return array;
        if ("list".equals(s))
            return list;
        if ("struct".equals(s))
            return struct;
        if ("oneof".equals(s))
            return oneof;
        if ("row".equals(s))
            return row;
        if ("column".equals(s))
            return column;

        throw new IllegalArgumentException("No Type matches:" + s);
    }

}
