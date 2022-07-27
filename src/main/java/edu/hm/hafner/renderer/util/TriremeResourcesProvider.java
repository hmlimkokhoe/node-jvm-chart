package edu.hm.hafner.renderer.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides the ECharts NPM dependency and JavaScript file for chart rendering to Trireme.
 */
public class TriremeResourcesProvider {

    private static final Logger LOG = LoggerFactory.getLogger(TriremeResourcesProvider.class);

    /**
     * Creates an Inputstream on elements found in the resources directory.
     * @param name Path of input elements
     * @return InputStream of input elements
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
     * Copies a JavaScript rendering file resource for use by Trireme.
     * @param filePath Path of JavaScript rendering file.
     * @return Copy of JavaScript rendering file as temp file.
     * @throws IOException Thrown in case creation of temp file or copying the InputStream fails.
     */
    public File copyJavaScriptFile(String filePath) throws IOException {
        final InputStream inputStream = createInputStreamFromResource(filePath);

        final File tempFile = File.createTempFile("index", ".js");
        tempFile.deleteOnExit();

        final FileOutputStream outputStream = new FileOutputStream(tempFile);

        if (inputStream != null) {
            IOUtils.copy(inputStream, outputStream);
            LOG.info("Copied ECharts Javascript rendering file for usage by Trireme.");
        }

        return tempFile;
    }

    /**
     * Creates a temporary directory for usage by Trireme and returns its respective path as string.
     * @return Path of temporary directory as string. Example in Linux: /tmp/trireme9684562791274531752
     */
    private String createTempDirectory() {
        String tempDirectory = "";

        try {
            Path tempDirectoryPath = Files.createTempDirectory("trireme");
            if (tempDirectoryPath != null) {
                tempDirectory = tempDirectoryPath.toString();
            }
        } catch (IOException e) {
            LOG.error("Failed to a create temporary directory in the operating system", e);
        }
        deleteTempDirectoryOnExit(tempDirectory);

        return tempDirectory;
    }

    /**
     * Parse file paths from NPM dependencies to an array of strings. The dependencies are provided in a txt file
     * created after the Maven Compile process.
     * @return Array with file paths as strings
     */
    private String[] parseEChartsPathsFile() {
        String commaSeparatedPaths = "";

        try (InputStream is = createInputStreamFromResource("/echarts-content.txt")) {
            if (is != null) {
                commaSeparatedPaths = new String(IOUtils.toByteArray(is), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            LOG.error("Failed to parse file paths from node modules dependencies.", e);
        }

        if (!commaSeparatedPaths.isEmpty()) {
            return commaSeparatedPaths.split(",");
        } else {
            return new String[0];
        }
    }

    /**
     * Deletes temp directory upon exit of the application.
     * @param tempDirectory Path of the temp directory
     */
    private void deleteTempDirectoryOnExit(String tempDirectory) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                FileUtils.forceDeleteOnExit(new File(tempDirectory));
            } catch (IOException e) {
                LOG.error("Failed to delete temp directory", e);
            }
        }));
    }

    /**
     * Clones the node_modules directory to provide NPM dependencies for Trireme.
     * @param nodeModulesPath Path of original node_modules directory
     * @return Path of temporary directory clone
     */
    public String copyNodeModulesFolder(String nodeModulesPath) {
        String tempDirectoryPathString = createTempDirectory();
        String[] paths = parseEChartsPathsFile();

        if (paths.length > 0 && !tempDirectoryPathString.isEmpty() && !nodeModulesPath.isEmpty()) {
            // example of relative file path: /zrender/src/svg/domapi.ts
            for (String path : paths) {
                if (!path.contains(".package-lock.json") && !path.isEmpty()) {
                    final String relativeFilePath = nodeModulesPath + path;

                    try (InputStream fileStream = createInputStreamFromResource(relativeFilePath)) {
                        final String fullTargetPathAsStr = tempDirectoryPathString + path;
                        if (fileStream != null) {
                            FileUtils.copyInputStreamToFile(fileStream, new File(fullTargetPathAsStr));
                        }
                    } catch (IOException e) {
                        LOG.error("Failed to copy node modules dependencies", e);
                    }
                }
            }
            LOG.info("Copied node modules dependencies for usage by Trireme.");
        }
        return tempDirectoryPathString;
    }
}