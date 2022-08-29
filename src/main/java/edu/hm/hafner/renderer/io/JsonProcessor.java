package edu.hm.hafner.renderer.io;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Validates JSON parameters if they have valid syntax and valid rendering configurations.
 */
public class JsonProcessor {

    private static final String ERROR_MESSAGE = "Could not execute rendering due to system errors.";

    /**
     * Merges two JSON strings.
     * @param userJsonString Export json string by user.
     * @param ssrJsonString Export json string configuring SSR mode for ECharts.
     * @return JSON string composed of two JSON strings.
     */
    public String mergeJsonStrings(String userJsonString, String ssrJsonString) {
        if (userJsonString.isEmpty() || ssrJsonString == null || ssrJsonString.isEmpty()) {
            throw new IllegalStateException(ERROR_MESSAGE);
        }
        try {
            final ObjectMapper mapper = new ObjectMapper();
            ObjectNode userObjNode = mapper.readTree(userJsonString).deepCopy();
            ObjectNode ssrObjNode = mapper.readTree(ssrJsonString).deepCopy();
            return userObjNode.setAll(ssrObjNode).toString();
        } catch (JacksonException e) {
            throw new IllegalStateException(ERROR_MESSAGE);
        }
    }

    /**
     * Checks if the JSON parameters are valid.
     * @param configJson Chart configuration JSON parameter
     * @param exportJson Export configuration JSON parameter
     */
    public void validateJsons(String configJson, String exportJson) {
        final ObjectMapper mapper = new ObjectMapper();

        JsonNode configJsonNode;
        try {
            configJsonNode = mapper.readTree(configJson);
        } catch (JacksonException e) {
            throw new IllegalArgumentException("Chart configuration parameter has invalid JSON.");
        }

        final JsonNode seriesNode = configJsonNode.get("series");
        if (seriesNode == null) {
            throw new IllegalArgumentException("Series key missing in chart configuration parameter.");
        }
        if (!seriesNode.isArray() || seriesNode.isEmpty()) {
            throw new IllegalArgumentException("Invalid series value in the chart configuration parameter.");
        }

        JsonNode exportJsonNode;
        try {
            exportJsonNode = mapper.readTree(exportJson);
        } catch (JacksonException e) {
            throw new IllegalArgumentException("Export configuration parameter has invalid JSON.");
        }

        final JsonNode widthNode = exportJsonNode.get("width");
        final JsonNode heightNode = exportJsonNode.get("height");
        if (widthNode == null || heightNode == null) {
            throw new IllegalArgumentException("Width and/or height missing in export configuration parameter.");
        }
        if (widthNode.asInt() == 0 || heightNode.asInt() == 0) {
            throw new IllegalArgumentException("Invalid width and/or height values in export configuration parameter.");
        }
    }
}
