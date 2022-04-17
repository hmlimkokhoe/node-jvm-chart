package com.mycompany.app.util;

import java.io.*;
import java.net.URL;
import org.apache.commons.io.IOUtils;

/**
 * Resolves the 'index.js' entrypoint of ECharts. Required for loading ECharts in a JAR file.
 */
public class ResourcesResolver {
    /*public URL getFile(String name) {
        return getClass().getResource(name);
    }*/

    public InputStream createInputStream(String name) {
        try {
            return getClass().getResourceAsStream(name);
        } catch (NullPointerException e) {
            System.out.println("Could not find JavaScript file for ECharts.");
            e.printStackTrace();
        }
        return null;
    }

    public File convertStreamToFile(InputStream inputStream) throws IOException {
        final File tempFile = File.createTempFile("index", ".js");
        tempFile.deleteOnExit();

        final FileOutputStream out = new FileOutputStream(tempFile);
        IOUtils.copy(inputStream, out);

        //printFile(inputStream);
        return tempFile;
    }

    /*private void printFile(InputStream inputStream) {
        StringBuilder out = new StringBuilder();
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
                System.out.println(out);
            }
        } catch (IOException e) {
            System.out.println("Unable to log ECharts JavaScript file");
            e.printStackTrace();
        }
        System.out.println(out);
    }*/
}
