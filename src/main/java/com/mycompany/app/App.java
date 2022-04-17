package com.mycompany.app;

import com.mycompany.app.filereader.TextParser;
import com.mycompany.app.util.ResourcesResolver;
import io.apigee.trireme.core.*;
// import io.apigee.trireme.core.ScriptFuture;
// import org.mozilla.javascript.Scriptable;
import java.io.*;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
// import com.mycompany.app.json.JsonConverter;


/**
 * Creates a JSON file and passes it as parameter to Trireme. These steps are used to build an SVG using ECharts.
 * TODO: refactor the throws parameters
 */
public class App {
    public static void main(String[] args ) throws NodeException, ExecutionException, InterruptedException {

        //TerminalReader terminalReader = new TerminalReader();
        //terminalReader.mapUserInputToJson();
        //JsonConverter builder = new JsonConverter();
        //builder.createJson();

        String[] params = new String[1];
        params[0] = "";
        NodeScript echartsInstance = null;

        try {
            NodeEnvironment nodeEnv = new NodeEnvironment();
            ResourcesResolver javaScriptResolver = new ResourcesResolver();
            final String eChartsPath = "/echarts/index.js";
            final InputStream inputStream = javaScriptResolver.createInputStream(eChartsPath);

            if (inputStream != null) {
                final File eChartsFile = javaScriptResolver.convertStreamToFile(inputStream);
                //URL url = javaScriptResolver.getFile("index.js");
                //System.out.println( url);

                echartsInstance = nodeEnv.createScript("index.js", eChartsFile, params);
            } else {
                System.out.println("ECharts project not found.");
            }
        } catch (NodeException e) {
            System.out.println("Failed to create NodeScript instance required for launching ECharts.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (echartsInstance != null) {
            // Scriptable echartsStatus = echartsInstance.execute().get();

            //TODO: Terminate echartsInstance so that below code can be executed.
            final ScriptStatus echartsStatus = echartsInstance.execute().get();
            final String triremeWorkDir = echartsInstance.getWorkingDirectory().toLowerCase();

            // Check the exit code
            System.exit(echartsStatus.getExitCode());

            System.out.println(triremeWorkDir);
            if (triremeWorkDir != null) {
                System.out.println("Trireme WorkDir not found");
            }

            final TextParser textParser = new TextParser();
            final String svgAsString = textParser.parseTextAsString(triremeWorkDir);
            // System.out.println(svgAsString);

            if (Objects.equals(svgAsString, "")) {
                System.out.println("No svg string generated");
            }
        } else  {
            System.out.println("Terminated process due to missing NodeScript instance.");
        }

        //System.out.println("Launching ECharts on port 8080");
        //ScriptStatus echartsStatus = echartsInstance.execute().get();
        //echartsInstance.close();

        /*Scriptable ex = echartsInstance.execute().getModuleResult();
        System.out.println(ex);

        try {
            System.out.println(ex.getModuleResult().getParentScope());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("InterruptedException while attempting to get module result.");
            e.printStackTrace();
        }*/
    }
}
