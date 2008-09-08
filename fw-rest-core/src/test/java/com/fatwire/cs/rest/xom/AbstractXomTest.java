package com.fatwire.cs.rest.xom;

import java.io.IOException;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import junit.framework.TestCase;

public abstract class AbstractXomTest extends TestCase {

	public AbstractXomTest() {
		super();
	}

	protected Document readTestDocument(String name) throws DocumentException, IOException {
	
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(
				"testdata/" + name);
		final SAXReader xmlReader = new SAXReader();
		Document doc;
		doc = xmlReader.read(in);
		in.close();
		return doc;
	}

}