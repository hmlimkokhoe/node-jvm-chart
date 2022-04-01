package com.mycompany.app;
import com.mycompany.app.json.JsonConverter;
import io.apigee.trireme.core.NodeEnvironment;
import io.apigee.trireme.core.NodeException;
import io.apigee.trireme.core.NodeScript;
import io.apigee.trireme.core.ScriptStatus;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Creates a JSON file and passes it as parameter to Trireme. These steps are used to build an SVG using ECharts.
 * TODO: maybe refactor the throws parameters?
 */
public class App {
    public static void main( String[] args ) throws NodeException, ExecutionException, InterruptedException {
        NodeEnvironment env = new NodeEnvironment();
        JsonConverter builder = new JsonConverter();

        builder.createJson();
        System.out.println(builder);

        //TODO: pass JSON to Trireme
        //for testing args parameter in node
        String[] params = new String[1];
        params[0] = "hello";

        NodeScript echartsScript = env.createScript("index.js", new File("src/main/echarts/src/index.js"), params);

        // Wait for the script to complete
        System.out.println("Launching ECharts on port 8080");
        ScriptStatus echartsStatus = echartsScript.execute().get();

        // Check the exit code
        System.exit(echartsStatus.getExitCode());
    }
}
