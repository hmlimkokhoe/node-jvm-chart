package com.mycompany.app;

import io.apigee.trireme.core.NodeEnvironment;
import io.apigee.trireme.core.NodeException;
import io.apigee.trireme.core.NodeScript;
import io.apigee.trireme.core.ScriptStatus;
// import io.apigee.trireme.core.ScriptFuture;
// import org.mozilla.javascript.Scriptable;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
// import com.mycompany.app.json.JsonConverter;
import com.mycompany.app.filereader.TextParser;

/**
 * Creates a JSON file and passes it as parameter to Trireme. These steps are used to build an SVG using ECharts.
 * TODO: refactor the throws parameters
 */
public class App {
    public static void main( String[] args ) throws NodeException, ExecutionException, InterruptedException {

        //JsonConverter builder = new JsonConverter();
        //builder.createJson();
        String[] params = new String[1];
        params[0] = "";

        NodeEnvironment nodeEnv = new NodeEnvironment();
        NodeScript echartsInstance = null;
        TextParser textParser = new TextParser();

        try {
            echartsInstance = nodeEnv.createScript("index.js", new File("src/main/resources/echarts/src/index.js"), params);
        } catch (NodeException e) {
            System.out.println("Failed to create NodeScript instance required for launching ECharts.");
            e.printStackTrace();
        }

        System.out.println("Returned the following string:");
        if (echartsInstance != null) {
            String svgAsString = textParser.parseTextAsString("src/main/resources/echarts/output/svg.txt");
            System.out.println(svgAsString);

            if (Objects.equals(svgAsString, "")) {
                System.out.println("No svg string generated");
            }

            ScriptStatus echartsStatus = echartsInstance.execute().get();

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
