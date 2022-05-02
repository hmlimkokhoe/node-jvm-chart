package edu.hm.hafner.renderer.json;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Converts a Configuration object to a JSON file.
 */
public class JsonConverter {

    /**
     * Create a JSON file from a Configuration object.
     */
    public void createJson(Configuration config) {

        if ((config.getWidth() <= 0 || config.getHeight() <= 0) ) {
            System.out.println("Unable to build JSON file due to invalid configuration.");
            return;
        }

        final ObjectMapper mapper = new ObjectMapper();
        File jsonFile = new File("target/config.json");

        try {
            mapper.writeValue(jsonFile, config);
        } catch (IOException e) {
            System.out.println("Unable to build json file.");
            e.printStackTrace();
        }
    }
}
