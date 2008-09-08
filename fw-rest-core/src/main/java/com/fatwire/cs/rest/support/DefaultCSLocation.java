package com.fatwire.cs.rest.support;

import java.net.MalformedURLException;
import java.net.URL;

import com.fatwire.cs.rest.model.CSLocation;

public class DefaultCSLocation implements CSLocation {

    private final URL hostInfo;

    private final String password;

    private final String username;

    /**
     * @param hostInfo
     */

    public DefaultCSLocation(final URL hostInfo) {
        this(hostInfo, null, null);
    }
    public DefaultCSLocation(final String hostInfo) throws MalformedURLException {
        this(new URL(hostInfo), null, null);
    }

    /**
     * @param hostInfo
     * @param username
     * @param password
     */
    public DefaultCSLocation(final URL hostInfo, final String username,
            final String password) {
        super();
        this.hostInfo = hostInfo;
        this.username = username;
        this.password = password;
    }

    public URL getHostInfo() {
        return hostInfo;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

}
