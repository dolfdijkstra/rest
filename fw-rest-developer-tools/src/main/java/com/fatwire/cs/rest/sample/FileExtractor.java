package com.fatwire.cs.rest.sample;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.rest.model.FileField;

public class FileExtractor {
    protected static final Log log = LogFactory.getLog(FileExtractor.class);

    public void extractFiles(Object data, File parent) throws IOException {

        if (data instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) data;
            for (Object x : map.values()) {
                extractFiles(x, parent);

            }
        } else if (data instanceof List) {
            List<?> list = (List<?>) data;
            for (Object x : list) {
                extractFiles(x, parent);
            }

        } else if (data instanceof FileField) {
            FileField ff = (FileField) data;
            if (ff != null) {
                File f = new File(parent, ff.getName());
                FileUtils.writeByteArrayToFile(f, ff.getContent());
            }

        }

    }
}
