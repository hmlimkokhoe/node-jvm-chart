package edu.hm.hafner.renderer.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;

/**
 * Reads a SVG file generated by the ECharts library to return an SVG as string in Java.
 */
public class SvgParser {
    private static final Logger LOG = LoggerFactory.getLogger(SvgParser.class);

    /**
     * Deletes SVG render file from filesystem
     * @param svgFile SVG render file
     */
    private void deleteFile(File svgFile) {
        try {
            FileUtils.delete(svgFile);
        } catch (IOException e) {
            LOG.error("Unable to delete SVG file", e);
        }
    }

    /**
     * Reads the SVG render file by Trireme and parses it as string in Java.
     * @param triremeWorkingDirectoryPath File path of the SVG render file to be read
     * @return SVG render file as string
     */
    public String parseSvgAsString(String triremeWorkingDirectoryPath, String svgFileName) {
        if (triremeWorkingDirectoryPath.isEmpty()) {
            throw new IllegalStateException("Failed to provide SVG render due to system errors");
        }

        String svgAsString = "";
        LOG.debug("Parsing SVG file content as string in Java...");

        try {
            final String svgFilePath = triremeWorkingDirectoryPath + "/" + svgFileName;
            final File svgFile = new File(svgFilePath);

            svgAsString = FileUtils.readFileToString(svgFile, StandardCharsets.UTF_8);
            deleteFile(svgFile);
        } catch (InvalidPathException | IOException e) {
            LOG.error("Failed to read SVG from ECharts.", e);
        }
        return svgAsString;
    }
}