package edu.hm.hafner.renderer;

import edu.hm.hafner.renderer.util.SvgParser;
import edu.hm.hafner.renderer.util.TriremeResourcesProvider;

import io.apigee.trireme.core.NodeEnvironment;
import io.apigee.trireme.core.NodeException;
import io.apigee.trireme.core.NodeScript;
// import io.apigee.trireme.core.ScriptStatus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Returns an SVG as string, which is rendered using two string parameters specifying the configuration and export
 * options for the desired chart.
 *
 * Example API call: String mySvgChart = echartsSvgRenderer.createSvgString(configurationOptions, exportOptions);
 */
public class EchartsSvgRenderer {
    private static final Logger LOG = LoggerFactory.getLogger(EchartsSvgRenderer.class);

    private static final String ECHARTS_PATH = "/echarts/";

    private static final String JAVASCRIPT_FILENAME = "chartRenderer.js";

    private static final String NODE_MODULES_FOLDER_NAME =  "node_modules";

    private static final String SVG_FILENAME = "echarts-render.svg";

    /**
     * Returns an SVG as string by using two options string parameters.
     * @param configOptions SVG configuration option for modelling the SVG such as chart type, data to display, etc.
     * @param exportOptions SVG export options, specifying dimensions, rendererMode, etc.
     * @return SVG as string
     */
    public String createSvgString(String configOptions, String exportOptions) {
        if (configOptions == null || configOptions.isEmpty()) {
            throw new IllegalArgumentException("An invalid configuration options parameter was passed");
        }
        if (exportOptions == null || exportOptions.isEmpty()) {
            throw new IllegalArgumentException("An invalid export options parameter was passed");
        }

        NodeScript echartsInstance;
        String triremeWorkingDirectoryPath = "";

        try {
            final NodeEnvironment nodeEnv = new NodeEnvironment();
            final TriremeResourcesProvider triremeResourcesProvider = new TriremeResourcesProvider();

            final File eChartsFile =
                    triremeResourcesProvider.copyJavaScriptFile(ECHARTS_PATH + JAVASCRIPT_FILENAME);
            if (!eChartsFile.isFile()) {
                throw new FileNotFoundException("Failed to load rendering scripts due to incorrect installation.");
            }

            triremeWorkingDirectoryPath =
                    triremeResourcesProvider.copyNodeModulesFolder(ECHARTS_PATH + NODE_MODULES_FOLDER_NAME);
            if (triremeWorkingDirectoryPath.length() < 1) {
                throw new FileNotFoundException("Failed to load Node.js scripts due to incorrect installation.");
            }

            // Array length set by both options parameters, SVG file name and file path of Node.js resources
            String[] triremeParameters = new String[4];
            triremeParameters[0] = configOptions;
            triremeParameters[1] = exportOptions;
            triremeParameters[2] = SVG_FILENAME;
            triremeParameters[triremeParameters.length - 1] = triremeWorkingDirectoryPath;

            echartsInstance = nodeEnv.createScript(JAVASCRIPT_FILENAME, eChartsFile, triremeParameters);
        } catch (NodeException | IOException e) {
            throw new IllegalStateException("Failed to execute rendering due to system errors.", e);
        }

        if (echartsInstance == null) {
            throw new IllegalStateException("Could not render SVG string due to system errors");
        }

        try {
            LOG.info("Rendering ECharts charts in Trireme.");
            echartsInstance.execute().get();
        } catch (NodeException | InterruptedException | ExecutionException e) {
            throw new IllegalStateException("System could not execute frontend scripts", e);
        }

        final SvgParser svgParser = new SvgParser();
        String svgAsString = svgParser.parseSvgAsString(triremeWorkingDirectoryPath, SVG_FILENAME);

        if (!svgAsString.isEmpty()) {
            return svgAsString;
        } else {
            throw new IllegalStateException("Failed to provide SVG string due to system errors");
        }
    }
}