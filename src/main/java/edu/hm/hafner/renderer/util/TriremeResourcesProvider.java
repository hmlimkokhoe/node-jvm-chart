package edu.hm.hafner.renderer.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

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
     */
    public String copyNodeModulesFolder(String nodeModulesPath) {
        String tempDirectoryPathString = createTempDirectory();
        String sourceDirectoryPathString = resolvePath(nodeModulesPath);

        try {
            final Path sourceDirectoryPath = Paths.get(sourceDirectoryPathString);

            try (Stream<Path> walk = Files.walk(sourceDirectoryPath)) {
                final String finalSourceDirString = sourceDirectoryPathString;
                final String finalTempDirectoryPathString = tempDirectoryPathString;

                walk.forEach(source -> {
                            Path target = Paths.get(finalTempDirectoryPathString, source.toString()
                                    .substring(finalSourceDirString.length()));
                            try {
                                LOG.debug(target.toString());
                                Files.copy(source, target, REPLACE_EXISTING);
                                deleteTempFileOnExit(target.toString());
                            } catch (IOException e) {
                                LOG.error("Failed to copy file to target directory.", e);
                            }
                        }
                );
                LOG.info("Copied node_modules to temporary directory for usage by Trireme.");
            }
        } catch (IOException e) {
            LOG.error("Failed to copy files into target directory", e);
        }
        return tempDirectoryPathString;
    }
}
