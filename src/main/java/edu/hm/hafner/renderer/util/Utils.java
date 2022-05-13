package edu.hm.hafner.renderer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

public class Utils {

    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

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
     * @param subdirectory - subdirectory inside the temporary directory
     * @return the temp directory as string
     */
    public static String setTempDirectory(@Nullable String subdirectory) {
        String tempDirectory = getTempDir();

        if ((subdirectory != null && !subdirectory.isEmpty()) && (tempDirectory != null && !tempDirectory.isEmpty())) {
            return tempDirectory + "/" + subdirectory;
        }
        return tempDirectory;
    }
}