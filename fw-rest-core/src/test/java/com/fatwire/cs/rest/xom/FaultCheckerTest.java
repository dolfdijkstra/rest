package com.fatwire.cs.rest.xom;

import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import com.fatwire.cs.rest.remote.RemoteFaultException;
import com.fatwire.cs.rest.remote.RestServiceException;
import com.fatwire.cs.rest.xom.FaultChecker;

public class FaultCheckerTest extends TestCase {

	public void testCheckForFaultMessageByteArray() {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<message><fault code=\"-1\" string=\"We've got an error\" actor=\"junit\"/></message>";
		try {
			new FaultChecker().checkForFaultMessage(xml.getBytes("UTF-8"));
		} catch (RemoteFaultException e) {
			assertEquals("-1", e.getErrorCode());
			assertEquals("We've got an error", e.getErrorString());
			assertEquals("junit", e.getErrorActor());
			return;
		} catch (UnsupportedEncodingException e) {
			fail(e.getMessage());
		} catch (RestServiceException e) {
			e.printStackTrace();
			fail("we should have got another exception");
		}
		fail("we should have got an exception");

	}

}
