package edu.hm.hafner.renderer;

import edu.hm.hafner.renderer.output.TextParser;
import edu.hm.hafner.renderer.util.ResourcesResolver;

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

    //TODO: createScript String scriptName, String script, String[] args
    private static String createSvgString(String[] params) {
        String svgAsString = "";
        NodeScript echartsInstance = null;

        try {
            final String fileName = "chartRenderer.js";
            final String nodeModulesName = "node_modules";
            final String eChartsPath = "/echarts/" + fileName;
            final String nodeModulesPath = "/echarts/" + nodeModulesName;

            NodeEnvironment nodeEnv = new NodeEnvironment();
            ResourcesResolver javaScriptResolver = new ResourcesResolver();

            final File eChartsFile = javaScriptResolver.createJavaScriptFile(eChartsPath);
            javaScriptResolver.createNodeModulesDirectory(nodeModulesName, nodeModulesPath);

            if (eChartsFile.isFile()) {
                echartsInstance = nodeEnv.createScript(fileName, eChartsFile, params);
            }
        } catch (NodeException | IOException e) {
            LOG.error("Cannot execute ECharts in Trireme due to missing scripts.", e);
        }

        if (echartsInstance != null) {
            LOG.info("Executing ECharts in Trireme.");
            final TextParser textParser = new TextParser();
            ScriptStatus echartsStatus = null;

            try {
                echartsStatus = echartsInstance.execute().get();
            } catch (NodeException | InterruptedException | ExecutionException e) {
                LOG.error("Failed to execute Trireme instance.", e);
            }

            if (echartsStatus != null && echartsStatus.getExitCode() == 1) {
                LOG.debug("Trireme instance was closed with exit code 1");
            }

            svgAsString = textParser.parseTextAsString();
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