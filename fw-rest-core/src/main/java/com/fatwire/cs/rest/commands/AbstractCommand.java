package com.fatwire.cs.rest.commands;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpURL;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.rest.HttpCommand;
import com.fatwire.cs.rest.HttpCommandException;
import com.fatwire.cs.rest.model.CSLocation;

public abstract class AbstractCommand implements HttpCommand<byte[]> {
    protected static final Log log = LogFactory.getLog(HttpCommand.class);

    protected final HttpClient httpClient;

    protected final CSLocation location;

    private String version;

    protected final Map<String, String> map = new HashMap<String, String>();

    public AbstractCommand(final HttpClient httpClient,
            final CSLocation location) {
        super();
        this.httpClient = httpClient;
        this.location = location;
    }

    public abstract String getPagename();

    protected String getUri() {
        return location.getHostInfo().toExternalForm();
    }

    protected final byte[] executeMethod(final HttpMethod httpMethod)
            throws HttpCommandException {
        int status = -1;
        try {
            status = httpClient.executeMethod(httpMethod);

            if (status == 200) {
                final Header hostServiceHeader = httpMethod
                        .getResponseHeader("HOST_SERVICE");
                if (hostServiceHeader != null) {
                    final int t = hostServiceHeader.getValue().lastIndexOf(":");
                    if ((t > 0)
                            && (t + 1 < hostServiceHeader.getValue().length())) {
                        version = hostServiceHeader.getValue().substring(t + 1);
                    }
                }
                final ByteArrayOutputStream bos = new ByteArrayOutputStream();
                final InputStream in = httpMethod.getResponseBodyAsStream();
                if (in == null) {
                    throw new HttpCommandException("no body returned from "
                            + httpMethod.getURI().toString() + " "
                            + getPagename(), status);
                }
                try {
                    IOUtils.copyLarge(in, bos);
                } finally {
                    in.close();
                }
                return bos.toByteArray();
            } else {
                throw new HttpCommandException(
                        "HTTP Response status code was not 200 but " + status,
                        status);
            }
        } catch (final HttpException e) {
            throw new HttpCommandException(e, status);
        } catch (final IOException e) {
            throw new HttpCommandException(e, status);
        } finally {
            httpMethod.releaseConnection();
        }

    }

    public String getCSVersionAsInResponseHeader() {
        return version;
    }

    protected URI createUri() throws URIException, NullPointerException,
            UnsupportedEncodingException {
        final HttpURL u = new HttpURL(getUri());
        final String[] k = new String[map.size()];
        final String[] v = new String[map.size()];
        int i = 0;
        for (final Entry<String, String> e : map.entrySet()) {
            k[i] = e.getKey();
            v[i] = e.getValue();
            i++;

        }
        u.setQuery(k, v);
        return u;

    }

}
