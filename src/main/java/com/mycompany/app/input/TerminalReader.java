package com.mycompany.app.input;

import com.mycompany.app.json.Configuration;
import com.mycompany.app.json.JsonConverter;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Receives user inputs for ECharts configuration via terminal. For testing purposes only.
 */
public class TerminalReader {

    /**
     * Creates a terminal dialogue to parse user inputs for their ECharts configuration.
     * @param typeParam A field supported in an ECharts configuration
     * @return A field value supported in an ECharts configuration (TODO: add more than just int fields)
     */
    private int handleUserInput(String typeParam) {
        try {
            Scanner input = new Scanner(System.in);
            System.out.println("Please enter a value for your " + typeParam + ", i.e. 800:");

            return input.nextInt();
        } catch (NoSuchElementException | IllegalStateException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Creates and locally stores a configuration JSON object from user inputs that will be read by ECharts.
     */
    public void mapUserInputToJson() {
        int width = handleUserInput("width");
        int height = handleUserInput("height");

        final Configuration configuration = new Configuration(width, height);

        JsonConverter jsonConverter = new JsonConverter();
        jsonConverter.createJson(configuration);
    }
}
