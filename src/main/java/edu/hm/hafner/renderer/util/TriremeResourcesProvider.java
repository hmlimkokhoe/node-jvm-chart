package edu.hm.hafner.renderer.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Provides the ECharts dependency and JavaScript file for chart rendering to Trireme.
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
     * @return Path of temporary directory as string.
     */
    private String createTempDirectory() {
        try {
            Path tempDirectoryPath = Files.createTempDirectory("trireme");

            if (tempDirectoryPath != null) {
                return tempDirectoryPath.toString();
            }

        } catch (IOException e) {
            LOG.error("Failed to a create temporary directory in the operating system", e);
        }
        return "";
    }

    /**
     * Resolves path of directory or file.
     * TODO: Doesn't work in JAR files.
     * @param path Path of directory or file to be resolved.
     * @return Resolved path
     */
    private String resolvePath(String path) {
        try {
            final URL sourceDirUrl = getClass().getResource(path);
            if (sourceDirUrl != null) {
                LOG.debug("Path of node_modules folder to copy: {}", sourceDirUrl.getFile());
                return sourceDirUrl.getPath();
            }
        } catch (NullPointerException e) {
            LOG.error("Failed to create temp directory due to missing source directory.", e);
        }
        return "";
    }

    /**
     * Delete file created in temporary directory upon exit of the application.
     * @param target Path of the file to be deleted.
     */
    private void deleteTempFileOnExit(String target) {
        File tempDirectory = new File(target);
        tempDirectory.deleteOnExit();
    }

    /**
     * Clones the node_modules directory for usage by Trireme.
     * @param nodeModulesPath Path of original node_modules directory
     * @return Path of temporary directory clone
     */
    public String copyNodeModulesFolder(String nodeModulesPath) {
        //example source path: /home/lim/hm/node-jvm-chart/target/classes/echarts/node_modules
        //example target path: /tmp/trireme9684562791274531752
        String tempDirectoryPathString = createTempDirectory();
        String sourceDirectoryPathString = resolvePath(nodeModulesPath);

        String commaSeparatedPaths = "";
        String[] paths;

        try (InputStream is = createInputStreamFromResource("/echarts-content.txt")) {
            if (is != null) {
                commaSeparatedPaths = new String(IOUtils.toByteArray(is), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            LOG.error("Failed to parse comma separated paths.", e);
        }

        if (!commaSeparatedPaths.isEmpty()) {
            paths = commaSeparatedPaths.split(",");

            if (paths.length > 0) {
                for (String path : paths) {
                    if (!path.contains(".package-lock.json")) {
                        try {
                            // example of path: /echarts/build/dev-fast.js
                            // example of fullSourcePathAsStr: /home/lim/hm/node-jvm-chart/target/classes/echarts/node_modules/zrender/src/core/WeakMap.ts
                            // example of fullTargetPathAsStr: /tmp/trireme15335638236408877040/zrender/src/core/WeakMap.ts

                            final String fullSourcePathAsStr = sourceDirectoryPathString + path;
                            final String fullTargetPathAsStr = tempDirectoryPathString + path;

                            //used by Files API.
                            final Path fullSourcePath = Paths.get(fullSourcePathAsStr);
                            final Path fullTargetPath = Paths.get(fullTargetPathAsStr);
                            final Path targetPathWithoutFile = Paths.get(fullTargetPathAsStr.substring(0, fullTargetPathAsStr.lastIndexOf("/")));

                            if (!Files.exists(targetPathWithoutFile)) {
                                Files.createDirectories(targetPathWithoutFile);
                            }
                            if (Files.exists(fullSourcePath)) {
                                Files.copy(fullSourcePath, fullTargetPath, REPLACE_EXISTING);
                                deleteTempFileOnExit(fullTargetPathAsStr);
                            }
                        } catch (IOException | InvalidPathException e) {
                            LOG.error("Failed to copy node_modules files to tmp directory.", e);
                        }
                    }
                }
            }
        }
        return tempDirectoryPathString;
    }
}
