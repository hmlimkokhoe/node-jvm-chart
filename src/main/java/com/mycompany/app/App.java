package com.mycompany.app;

import com.mycompany.app.util.ResourcesResolver;
import io.apigee.trireme.core.NodeEnvironment;
import io.apigee.trireme.core.NodeException;
import io.apigee.trireme.core.NodeScript;
import io.apigee.trireme.core.ScriptStatus;
// import io.apigee.trireme.core.ScriptFuture;
// import org.mozilla.javascript.Scriptable;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
// import com.mycompany.app.json.JsonConverter;


/**
 * Creates a JSON file and passes it as parameter to Trireme. These steps are used to build an SVG using ECharts.
 * TODO: refactor the throws parameters
 */
public class App {
    public static void main( String[] args ) throws NodeException, ExecutionException, InterruptedException {

        //TerminalReader terminalReader = new TerminalReader();
        //terminalReader.mapUserInputToJson();
        //JsonConverter builder = new JsonConverter();
        //builder.createJson();

        String[] params = new String[1];
        params[0] = "";
        NodeScript echartsInstance = null;

        try {
            NodeEnvironment nodeEnv = new NodeEnvironment();
            ResourcesResolver resourcesResolver = new ResourcesResolver();
            String eChartsPath = "/echarts/index.js";

            InputStream inputStream = resourcesResolver.createInputStream(eChartsPath);

            if (inputStream == null) {
                System.out.println("NOT HERE");
            }

            File EChartsFile = resourcesResolver.convertStreamToFile(inputStream);
            //URL url = resourcesResolver.getFile("index.js");
            //System.out.println( url);

            StringBuilder out = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    out.append(line);
                }
            }
            System.out.println(out);

            echartsInstance = nodeEnv.createScript("index.js", EChartsFile, params);
        } catch (NodeException e) {
            System.out.println("Failed to create NodeScript instance required for launching ECharts.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Returned the following string:");
        if (echartsInstance != null) {
            ScriptStatus echartsStatus = echartsInstance.execute().get();

            // Check the exit code
            System.exit(echartsStatus.getExitCode());

            /*TextParser textParser = new TextParser();
            String svgAsString = textParser.parseTextAsString("echarts/output/svg.txt");
            System.out.println(svgAsString);

            if (Objects.equals(svgAsString, "")) {
                System.out.println("No svg string generated");
            }*/

            // Check the exit code
            System.exit(echartsStatus.getExitCode());
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
        // Wait for the script to complete
        // System.out.println("end of trireme execution");
    }
}
