package com.fatwire.cs.rest.commands;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;

import com.fatwire.cs.rest.CommandException;
import com.fatwire.cs.rest.HttpCommandException;
import com.fatwire.cs.rest.model.CSLocation;

public abstract class AbstractGetCommand extends AbstractCommand {

    /**
     * @param httpClient
     * @param location
     */
    public AbstractGetCommand(final HttpClient httpClient,
            final CSLocation location) {
        super(httpClient, location);
    }

    @Override
    public String getPagename() {
        return "fatwire/REST/assets/GET";
    }

    protected abstract void doAddParameters() ;
    
    public byte[] execute() throws CommandException {
        return doGet();
    }
    protected final byte[] doGet() throws HttpCommandException {

        map.put("pagename", getPagename());
        if ((location.getUsername() != null) && (location.getPassword() != null)) {
            map.put("authusername", location.getUsername());
            map.put("authpassword", location.getPassword());
        }
        doAddParameters();
        final GetMethod get = new GetMethod();
        try {
            final URI u = createUri();
            log.debug(u);
            get.setURI(u);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

        return executeMethod(get);

    }


}
