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

    public static final String javaScriptFileName = "chartRenderer.js";

    public static final String javaScriptFilePath = "/echarts/" + javaScriptFileName;

    public static final String nodeModulesPath = "/echarts/node_modules";

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

        String[] triremeParameters = new String[3];
        triremeParameters[0] = configOptions;
        triremeParameters[1] = exportOptions;

        NodeScript echartsInstance;

        try {
            final NodeEnvironment nodeEnv = new NodeEnvironment();
            final TriremeResourcesProvider triremeResourcesProvider = new TriremeResourcesProvider();

            final File eChartsFile = triremeResourcesProvider.copyJavaScriptFile(javaScriptFilePath);
            if (!eChartsFile.isFile()) {
                throw new FileNotFoundException("System was unable to provide chart rendering scripts.");
            }

            final String triremeWorkingDirectoryPath = triremeResourcesProvider.copyNodeModulesFolder(nodeModulesPath);
            if (triremeWorkingDirectoryPath.length() < 1) {
                throw new FileNotFoundException("System was unable to provide Node.js library scripts.");
            }

            triremeParameters[triremeParameters.length - 1] = triremeWorkingDirectoryPath;
            echartsInstance = nodeEnv.createScript(javaScriptFileName, eChartsFile, triremeParameters);
        } catch (NodeException | IOException e) {
            throw new IllegalStateException("Could not execute rendering scripts due to system errors.", e);
        }

        if (echartsInstance == null) {
            throw new IllegalStateException("Could not render SVG string due to system errors");
        }

        LOG.info("Rendering ECharts charts in Trireme.");
        final SvgParser svgParser = new SvgParser();

        try {
            // ScriptStatus echartsStatus = echartsInstance.execute().get();
            echartsInstance.execute().get();
        } catch (NodeException | InterruptedException | ExecutionException e) {
            throw new IllegalStateException("System could not execute frontend scripts", e);
        }

        // if (echartsStatus != null && echartsStatus.getExitCode() == 1) {
        //     LOG.debug("Trireme instance was closed with exit code 1");
        // }

        return svgParser.parseSvgAsString();
    }
}