package com.fatwire.cs.rest.sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.fatwire.cs.rest.model.Asset;
import com.fatwire.cs.rest.xom.AssetXom;
import com.fatwire.cs.rest.xom.XOMException;

public class DomUtils {
    private DomUtils() {
    }

    public static Document createDocument(final InputStream in) throws DocumentException {

        final SAXReader xmlReader = new SAXReader();
        xmlReader.setEncoding("UTF-8");
        return xmlReader.read(in);
    }

    public static void writeDocument(Document doc, File f) throws IOException {
        f.getParentFile().mkdirs();
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = null;

        try {
            writer = new XMLWriter(new FileWriter(f), format);
            writer.write(doc);
        } finally {
            if (writer != null)
                writer.close();
        }

    }

    public static Asset createAsset(File file) throws IOException, DocumentException, XOMException {
        InputStream stream = new FileInputStream(file);
        try {
            return createAsset(stream);
        } finally {
            stream.close();
        }

    }

    public static Asset createAsset(InputStream stream) throws DocumentException, XOMException {
        Document doc = DomUtils.createDocument(stream);
        return createAsset(doc);

    }
    public static Asset createAsset(Document doc) throws DocumentException, XOMException {
        final AssetXom xom = new AssetXom();
        return xom.toObject(doc);

    }

}
