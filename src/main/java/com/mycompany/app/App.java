package com.mycompany.app;

import com.mycompany.app.output.TextParser;
import com.mycompany.app.util.ResourcesResolver;

import io.apigee.trireme.core.NodeEnvironment;
import io.apigee.trireme.core.NodeException;
import io.apigee.trireme.core.NodeScript;
import io.apigee.trireme.core.ScriptStatus;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
// import com.mycompany.app.json.JsonConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a JSON file and passes it as parameter to Trireme. These steps are used to build an SVG using ECharts.
 * TODO: input Jar file, output to drive
 * TODO: extract JAR
 */
public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    //TODO: createScript String scriptName, String script, String[] args
    public static String createSvgString(String[] params) {
        String svgAsString = "";
        NodeScript echartsInstance = null;

        try {
            final String fileName = "chartRenderer.js";
            final String eChartsPath = "/echarts/" + fileName;
            NodeEnvironment nodeEnv = new NodeEnvironment();
            ResourcesResolver javaScriptResolver = new ResourcesResolver();
            final File eChartsFile = javaScriptResolver.createScriptFile(eChartsPath);

            if (eChartsFile.isFile()) {
                echartsInstance = nodeEnv.createScript(fileName, eChartsFile, params);
            } else {
                LOG.debug("");
            }
        } catch (NodeException | IOException e) {
            LOG.error("Unable to create ECharts script due to missing input stream.", e);
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

    public static void main(String[] args ) {
        //TerminalReader terminalReader = new TerminalReader();
        //terminalReader.mapUserInputToJson();
        //JsonConverter builder = new JsonConverter();
        //builder.createJson();

        String[] params = new String[1];
        params[0] = "";
        String svgAsString = createSvgString(params);
        LOG.info("Created svg string below:");
        LOG.info(svgAsString);

        //System.out.println("Launching ECharts on port 8080");
        //ScriptStatus echartsStatus = echartsInstance.execute().get();
        //echartsInstance.close();
    }
}