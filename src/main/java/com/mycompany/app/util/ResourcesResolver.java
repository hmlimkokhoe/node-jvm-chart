package com.mycompany.app.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.mycompany.app.output.TextParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resolves the 'chartRenderer.js' entrypoint of ECharts. Required for loading ECharts in a JAR file.
 */
public class ResourcesResolver {

    private static final Logger LOG = LoggerFactory.getLogger(TextParser.class);

    /**
     * Creates an Inputstream on elements found in the resources directory.
     * @param name Path of input elements
     * @return
     */
    private InputStream createInputStreamFromResource(String name) {
        try {
            return getClass().getResourceAsStream(name);
        } catch (NullPointerException e) {
            LOG.error("Failed to create InputStream for file with given name.", e);
        }
        return null;
    }

    /**
     * Copies a JavaScript file resource for use by Trireme.
     * @param filePath Path of JavaScript file
     * @return Copy of JavaScript file as temp file.
     * @throws IOException
     */
    public File createJavaScriptFile(String filePath) throws IOException {
        final InputStream inputStream = createInputStreamFromResource(filePath);

        final File tempFile = File.createTempFile("index", ".js");
        tempFile.deleteOnExit();

        final FileOutputStream out = new FileOutputStream(tempFile);

        if (inputStream != null) {
            IOUtils.copy(inputStream, out);
            LOG.info("Copied ECharts Javascript file for usage by Trireme.");
        }

        return tempFile;
    }

    /**
     * Creates the node_modules directory necessary to execute ECharts in Trireme.
     * @param directoryPrefix Name for creating directory
     * @param folderName
     * @throws IOException
     */
    public void createNodeModulesDirectory(String directoryPrefix, String folderName) throws IOException {
        URL sourceDirUrl = null;
        String sourceDirString = "";
        final String tempDir = Utils.setTempDirectory(directoryPrefix);

        try {
            sourceDirUrl = getClass().getResource(folderName);
        } catch (NullPointerException e) {
            LOG.error("Failed to create temp directory due to missing source directory.", e);
        }

        if (sourceDirUrl != null) {
            sourceDirString = sourceDirUrl.getPath();
        }

        try {
            final Path dirPath = Paths.get(tempDir);
            Files.createDirectories(dirPath);
        } catch (IOException e) {
            LOG.error("Failed to create directory in temp directory", e);
        }

        try {
            final File srcDir = new File(sourceDirString);
            final File destDir = new File(tempDir);
            FileUtils.copyDirectory(srcDir, destDir, false);
        } catch (IOException e) {
            LOG.error("Failed to copy files into temp directory", e);
        }
    }
}
