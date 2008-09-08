package com.fatwire.cs.rest.util;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

public class ConnectionManagerFactory {

	private ConnectionManagerFactory() {
	}

	public static MultiThreadedHttpConnectionManager getConnectionManager() {
		MultiThreadedHttpConnectionManager connectionManager;
		connectionManager = new MultiThreadedHttpConnectionManager();
		connectionManager.getParams().setConnectionTimeout(5000);
		connectionManager.getParams().setMaxTotalConnections(20);
		connectionManager.getParams().setDefaultMaxConnectionsPerHost(20);

		return connectionManager;
	}
}
