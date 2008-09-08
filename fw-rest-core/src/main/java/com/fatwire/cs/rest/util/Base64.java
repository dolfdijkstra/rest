package com.fatwire.cs.rest.util;

public class Base64 {

    public static byte[] decode(String text) {
        return org.apache.commons.codec.binary.Base64.decodeBase64(text.getBytes());
    }
}
