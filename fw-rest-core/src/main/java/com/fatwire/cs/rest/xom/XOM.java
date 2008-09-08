package com.fatwire.cs.rest.xom;

import org.dom4j.Document;

public interface XOM<T> {

	T toObject(Document xml) throws XOMException;
}
