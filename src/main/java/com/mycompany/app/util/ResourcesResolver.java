package com.mycompany.app.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.mycompany.app.output.TextParser;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resolves the 'chartRenderer.js' entrypoint of ECharts. Required for loading ECharts in a JAR file.
 */
public class ResourcesResolver {

    private static final Logger LOG = LoggerFactory.getLogger(TextParser.class);

    private InputStream createInputStream(String name) {
        try {
            return getClass().getResourceAsStream(name);
        } catch (NullPointerException e) {
            LOG.error("Failed to create InputStream for ECharts JavaScript file.", e);
        }
        return null;
    }

    public File createScriptFile(String eChartsPath) throws IOException {
        final InputStream inputStream = createInputStream(eChartsPath);

        final File tempFile = File.createTempFile("index", ".js");
        tempFile.deleteOnExit();

        final FileOutputStream out = new FileOutputStream(tempFile);

        if (inputStream != null) {
            IOUtils.copy(inputStream, out);
            LOG.info("Copied ECharts Javascript file for usage by Trireme.");
        }

        return tempFile;
    }
}
