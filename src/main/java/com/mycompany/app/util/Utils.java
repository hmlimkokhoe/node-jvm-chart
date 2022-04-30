package com.mycompany.app.util;

import com.mycompany.app.output.TextParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

    private static final Logger LOG = LoggerFactory.getLogger(TextParser.class);

    private static String getTempDir() {
        String tempDir = "";
        try {
            tempDir = System.getProperty("java.io.tmpdir");
        } catch (SecurityException e) {
            LOG.error("Access to temp directory denied", e);
        } catch (NullPointerException | IllegalArgumentException e) {
            LOG.error("Failed to retrieve temp directory", e);
        }
        return tempDir;
    }

    /**
     * Returns the temp directory of the operating system.
     * @return the temp directory as string
     */
    public static String setTempDirectory(String subdirectory) {
        final String tempDirectory = getTempDir();

        if (subdirectory != null && !subdirectory.isBlank()) {
            return tempDirectory + "/" + subdirectory;
        }
        return tempDirectory;
    }
}