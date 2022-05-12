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

import edu.hm.hafner.renderer.output.TextParser;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides the ECharts dependency and chart rendering JavaScript to Trireme.
 */
public class TriremeResourcesProvider {

    private static final Logger LOG = LoggerFactory.getLogger(TextParser.class);

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
     * Copies a JavaScript file resource for use by Trireme.
     * @param filePath Path of JavaScript file
     * @return Copy of JavaScript file as temp file.
     * @throws IOException
     */
    public File copyJavaScriptFile(String filePath) throws IOException {
        final InputStream inputStream = createInputStreamFromResource(filePath);

        final File tempFile = File.createTempFile("index", ".js");
        tempFile.deleteOnExit();

        final FileOutputStream outputStream = new FileOutputStream(tempFile);

        if (inputStream != null) {
            IOUtils.copy(inputStream, outputStream);
            LOG.info("Copied ECharts Javascript file for usage by Trireme.");
        }

        return tempFile;
    }

    private Path createTempDirectory() {
        try {
            return Files.createTempDirectory("trireme");
        } catch (IOException e) {
            LOG.error("Failed to a temp directory in the operating system", e);
        }
        return null;
    }

    /**
     * TODO: Build a directory with files and pass them to FileUtils.copyDirectory(). While we are passing parameters the correct way,
     *       the File param srcDir doesn't actually exist "in JAR context", despite having a working getAbsolutePath() method.
     * TODO:  (alt) Use Files.walk: https://www.baeldung.com/java-copy-directory (it also complains about
     * TODO:  (alt) Use Files.copy will copy a directory but without any files: https://docs.oracle.com/javase/tutorial/essential/io/copy.html
     * Creates the node_modules directory necessary to execute ECharts in Trireme.
     * @param nodeModulesPath
     * @throws IOException
     */
    public String copyNodeModulesFolder(String nodeModulesPath) throws IOException {
        String sourceDirectoryPathString = "";
        String tempDirectoryPathString = "";
        Path tempDirectoryPath = createTempDirectory();

        //String nodeModulesFolderName = "";
        /*if (!nodeModulesPath.isBlank()) {
            nodeModulesFolderName = nodeModulesPath.split("/")[2];
        }*/

        if (tempDirectoryPath != null) {
            tempDirectoryPathString = tempDirectoryPath.toString();
        }

        //final String tempDirectoryPathString = Utils.setTempDirectory(nodeModulesFolderName);

        //TODO: getResource() doesn't work in JAR files, change to standalone method
        try {
            final URL sourceDirUrl = getClass().getResource(nodeModulesPath);
            if (sourceDirUrl != null) {
                sourceDirectoryPathString = sourceDirUrl.getPath();  //jar: file:/home/lim/Projects/trireme-java/target/trireme-java-1.0-SNAPSHOT-jar-with-dependencies.jar!/echarts/node_modules
                LOG.info(sourceDirUrl.getFile());
            }
        } catch (NullPointerException e) {
            LOG.error("Failed to create temp directory due to missing source directory.", e);
        }

        try {
            final File srcDir = new File(sourceDirectoryPathString);  //jar: file doesn't exist. destDir actually exists
            LOG.info("Check me! '" + srcDir.getAbsolutePath() + "'");  //same as in line 79
            // FileUtils.copyDirectory(srcDir, destDir, false);
            // FileUtils.copyDirectoryToDirectory(srcDir, destDir);

            final Path sourceDirectoryPath = Paths.get(sourceDirectoryPathString);
            // final Path targetDir = Paths.get(tempDirectoryPathString);

            try (Stream<Path> walk = Files.walk(sourceDirectoryPath)) {
                final String finalSourceDirString = sourceDirectoryPathString;
                final String finalTempDirectoryPathString = tempDirectoryPathString;

                walk.forEach(source -> {  //example: /home/lim/Projects/trireme-java/target/classes/echarts/node_modules/zrender/src/core/GestureMgr.ts
                            Path destination = Paths.get(finalTempDirectoryPathString, source.toString()
                                    .substring(finalSourceDirString.length()));
                            try {
                                //LOG.info(source.toString());
                                Files.copy(source, destination);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                );
            }
        } catch (IOException e) {
            LOG.error("Failed to copy files into temp directory", e);
        }
        return tempDirectoryPathString;
    }
}
