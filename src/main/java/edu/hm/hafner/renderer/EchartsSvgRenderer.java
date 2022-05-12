package edu.hm.hafner.renderer;

import edu.hm.hafner.renderer.output.SvgParser;
import edu.hm.hafner.renderer.util.TriremeResourcesProvider;

import io.apigee.trireme.core.NodeEnvironment;
import io.apigee.trireme.core.NodeException;
import io.apigee.trireme.core.NodeScript;
import io.apigee.trireme.core.ScriptStatus;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a JSON file and passes it as parameter to Trireme. These steps are used to build an SVG using ECharts.
 * TODO: input Jar file, output to drive
 * TODO: extract JAR
 */
public class EchartsSvgRenderer {
    private static final Logger LOG = LoggerFactory.getLogger(EchartsSvgRenderer.class);

    //TODO: param1: String options, param2: String parameters
    private String createSvgString(String[] params) {
        String svgAsString = "";
        NodeScript echartsInstance = null;

        try {
            final String javaScriptFileName = "chartRenderer.js";
            final String javaScriptFilePath = "/echarts/" + javaScriptFileName;
            final String nodeModulesPath = "/echarts/node_modules";

            final NodeEnvironment nodeEnv = new NodeEnvironment();
            final TriremeResourcesProvider triremeResourcesProvider = new TriremeResourcesProvider();

            final File eChartsFile = triremeResourcesProvider.copyJavaScriptFile(javaScriptFilePath);
            final String triremeWorkingDirectoryPath = triremeResourcesProvider.copyNodeModulesFolder(nodeModulesPath);
            params[0] = triremeWorkingDirectoryPath;

            if (eChartsFile.isFile()) {
                echartsInstance = nodeEnv.createScript(javaScriptFileName, eChartsFile, params);
            }
        } catch (NodeException | IOException e) {
            LOG.error("Cannot execute ECharts in Trireme due to missing scripts.", e);
        }

        if (echartsInstance != null) {
            LOG.info("Rendering ECharts charts in Trireme.");
            final SvgParser svgParser = new SvgParser();
            ScriptStatus echartsStatus = null;

            try {
                echartsStatus = echartsInstance.execute().get();
            } catch (NodeException | InterruptedException | ExecutionException e) {
                LOG.error("Failed to execute Trireme instance.", e);
            }

            if (echartsStatus != null && echartsStatus.getExitCode() == 1) {
                LOG.debug("Trireme instance was closed with exit code 1");
            }

            svgAsString = svgParser.parseSvgAsString();
        } else  {
            LOG.debug("Terminated process due to missing NodeScript instance.");
        }
        return svgAsString;
    }

    public String render() {
        try {
            String[] params = new String[1];
            params[0] = "";

            return createSvgString(params);
        } catch (IllegalArgumentException e) { // in case of an invalid user input
            LOG.error("Failed to create SVG String due to system errors", e);
        }
        return null;
    }
}